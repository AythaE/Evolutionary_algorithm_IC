/*
 * Archivo: Algorithm.java 
 * Proyecto: Evolutionary_algorithm_IC
 * Práctica de Algoritmos evolutivos: Resolución de problemas NP. QAP
 * 
 * Autor: Aythami Estévez Olivas
 * Email: aythae@correo.ugr.es
 * Fecha: 20-ene-2017
 * Asignatura: Inteligencia computacional
 * Repositorio: https://github.com/AythaE/Evolutionary_algorithm_IC
 * 
 * Master Universitario en Ingeniería Informática
 * Universidad de Granada
 */
package es.ugr.ic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The Class Algorithm that contains the methods of the genetic algorithm.
 */
public class Algorithm {

	/** 
	 * The Constant MAX_GENERATION_WO_IMPROVEMENT_STANDARD. It handle the 
	 * termination condition in case that the execution until a fixed 
	 * number of generations without improvement and the standard
	 * variant of the algorithm were selected.
	 */
	public static final int MAX_GENERATION_WO_IMPROVEMENT_STANDARD = 200;
	

	/** 
	 * The Constant MAX_GENERATION_WO_IMPROVEMENT_OPTIMIZED. It handle the 
	 * termination condition in case that the execution until a fixed 
	 * number of generations without improvement and the optimized
	 * variants of the algorithm were selected.
	 */
	public static final int MAX_GENERATION_WO_IMPROVEMENT_OPTIMIZED = 50;

	/** The Constant TOURNAMENT_SIZE for the tournament selection. */
	public static final int TOURNAMENT_SIZE = 5;

	/** 
	 * The Constant GENE_MUTATION_PROB that handle the mutation probability of
	 * every single gene. 
	 */
	public static final double GENE_MUTATION_PROB = 0.01;
	
	/** 
	 * The Constant OPTIMIZATION_PROB for the generation of optimized individuals
	 * in the optimized variants of the algorithm. */
	public static final double OPTIMIZATION_PROB = 0.5; 
	
	/** The Constant ELITISM_INDIVIDUALS for the elitism model. */
	public static final int ELITISM_INDIVIDUALS = 2;

	/**
	 * The Enum AlgorithmType that indicate the variant of the genetic algorithm.
	 */
	public enum AlgorithmType {

		/** The standard variant. */
		STANDARD,
		/** The lamarckian variant. */
		LAMARCKIAN,
		/** The baldwinian variant. */
		BALDWINIAN
	}

	/** The min population fitness. */
	private static long minPopulationFitness = Integer.MAX_VALUE;

	/** The generations WO improvement. */
	private static long generationsWOImprovement = 0;

	/** The algorithm type. */
	private static AlgorithmType algType = AlgorithmType.STANDARD;
	
	/** The mutation rate. */
	private static double mutationRate = 0;

	/**
	 * Evolve population.
	 *
	 * @param pop the pop
	 * @return the population
	 */
	public static Population evolvePopulation(Population pop) {

		int popSize = pop.getPopulationSize();
		Population newPop = new Population(popSize);

		//Save the best inividuals to the next population directly
		Individual[] elite = pop.getFittest(ELITISM_INDIVIDUALS);
		for (int i = 0; i < elite.length; i++) {
			newPop.saveIndividual(elite[i], i);
		}

		long mutationCount = 0;

		//For the rest of the individuals make a selection of 2 parents,
		//crossover it, mutate it and save it the new population
		for (int i = ELITISM_INDIVIDUALS; i < popSize; i += 2) {
			Individual parent1 = selection(pop);
			Individual parent2;
			do {
				parent2 = selection(pop);
			} while (parent1.equals(parent2));

			Individual[] childs = crossover(parent1, parent2);

			mutationCount += mutation(childs[0]);
			mutationCount += mutation(childs[1]);

			newPop.saveIndividual(childs[0], i);
			if (i + 1 < popSize) {
				newPop.saveIndividual(childs[1], i + 1);
			}

		}

		//Calculation of the effective mutation rate dividing the total mutated
		//genes by the total genes and multiply it by 100 to get a percent
		mutationRate = ((double) mutationCount / (popSize * elite[0].getChromosomeSize())) * 100;

		return newPop;

	}

	

	/**
	 * Evaluate population.
	 *
	 * @param pop the pop
	 */
	public static void evaluatePopulation(Population pop) {
		Individual[] popIndiv = pop.getIndividuals();
		
		int popSize = pop.getPopulationSize();
	
		long popFitness = 0;
		
		//Calculate the number of processors to parallelize
		int processors = Runtime.getRuntime().availableProcessors();
		int numThreads;
		
		if (processors >= 2) {
			if (processors >= 4)
				numThreads = 4;
			else
				numThreads = 2;
			
			// Calculate the part of the population for divide it between threads
			int partIndiv = popSize / numThreads;

		
			// If populationSize is no an divisible by numThreads then calc the rest
			int rest =popSize % numThreads;

			//Instantiate the Runnable classes
			GreedyOptimizationThread[] thBodys = new GreedyOptimizationThread[numThreads];
			for (int i = 0; i < thBodys.length; i++) {
				//Add the rest of the division to the last one
				if (i == numThreads - 1) 
					thBodys[i] = new GreedyOptimizationThread(partIndiv * i, partIndiv * (i+1) + rest, algType, popIndiv);
				else
					thBodys[i] = new GreedyOptimizationThread(partIndiv * i, partIndiv * (i+1), algType, popIndiv);
			}
			
			//Instantiate and start the threads from back to top
			Thread[] th = new Thread[numThreads];
			for (int i = numThreads - 1; i >= 0; i--) {
				th[i] =  new Thread(thBodys[i]);
				th[i].start();
				
			}
			
			//Wait for the threads to finish their tasks
			for (int i = 0; i < th.length; i++) {
				try {
					th[i].join();
					popFitness += thBodys[i].getSumFitness();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		
		} 
		//If number of processors is less than 2 then execute it on a single thread
		//For the elitism individuals calculate the fitness in an optimized way, 
		//if the optimized variant of the algorithms are selected, for the rest
		//only calculate the optimized fitness for a percent of individuals.
		//See the constant OPTIMIZATION_PROB
		else{
			Individual [] indiv = pop.getIndividuals();
			
			for (int i = 0; i < ELITISM_INDIVIDUALS; i++) {
				popFitness += indiv[i].calcFitness(algType);
			}

			for (int i = ELITISM_INDIVIDUALS; i < popSize; i++) {
				if (Math.random() > OPTIMIZATION_PROB) 
					popFitness += indiv[i].calcFitness(algType);
				else
					popFitness += indiv[i].calcFitness(AlgorithmType.STANDARD);
			}

		}
		
		//Calculate the mean fitness of the population
		popFitness /= pop.getPopulationSize();

		pop.setPopulationFitness(popFitness);

	}


	/**
	 * Check if Algorithm has finished. The termination condition of this
	 * algorithm is a max number of generations without improvement. This number
	 * is represented by the constants 
	 * {@link Algorithm#MAX_GENERATION_WO_IMPROVEMENT_STANDARD} and
	 * {@link Algorithm#MAX_GENERATION_WO_IMPROVEMENT_OPTIMIZED}.
	 *
	 * @param pop the population to check
	 * @return true, if is finished
	 */
	public static boolean isFinished(Population pop) {
		if (minPopulationFitness > pop.getFittest(1)[0].getFitness()) {
			minPopulationFitness = pop.getFittest(1)[0].getFitness();
			generationsWOImprovement = 0;
			return false;
		} else {
			generationsWOImprovement++;
			
			if (algType == AlgorithmType.STANDARD) {
				if (generationsWOImprovement < MAX_GENERATION_WO_IMPROVEMENT_STANDARD)
					return false;
				else
					return true;
			}
			else {
				if (generationsWOImprovement < MAX_GENERATION_WO_IMPROVEMENT_OPTIMIZED)
					return false;
				else
					return true;
			}
			
		}
	}

	/**
	 * Crossover operator, implements ordered crossover operator.
	 *
	 * @param i1
	 *            the individual 1, one of the parents
	 * @param i2
	 *            the individual 2, one of the parents
	 * @return the 2 children
	 */
	public static Individual[] crossover(Individual i1, Individual i2) {

		Random rand = new Random();
		int chromosomeSize = i1.getChromosomeSize();
		Individual[] newIndiv = new Individual[2];
		newIndiv[0] = new Individual(chromosomeSize, false);
		newIndiv[1] = new Individual(chromosomeSize, false);

		int num1 = -1, num2 = -1;
		int genesP1 = 0, genesP2 = 0;

		// Generate random numbers while numbers are equal, because then the
		// child
		// wouldn't has genes form parent 1, or the difference between them are
		// the chromosome size, because then the child wouldn't has genes from
		// parent 2
		while (num1 == num2 || (genesP1 = genesP2 = Math.abs(num1 - num2)) >= (chromosomeSize - 1)) {
			num1 = rand.nextInt(chromosomeSize);
			num2 = rand.nextInt(chromosomeSize);
		}

		// Add 1 to genesP1 and genesP2 because the array starts at 0 and the
		// genesP1 is the number of genes of the parent 1 - 1 and also the
		// genesP2 is the number of genes of the parent 2 - 1
		genesP1++;
		genesP2++;

		// Select the min number
		int pos1 = Math.min(num1, num2);

		// Select the max number
		int pos2 = Math.max(num1, num2);

		// Copy a fragment of the parent 1
		for (int i = pos1; i <= pos2; i++) {
			newIndiv[0].setGene(i1.getGene(i), i);
			newIndiv[1].setGene(i2.getGene(i), i);
		}

		int p2Ind = pos2 + 1;
		int p1Ind = pos2 + 1;

		// Fill the rest of the new individuals with the parents from the
		// beginning
		while ((genesP1 + genesP2) < (chromosomeSize * 2)) {

			// In case the index is >= than the max value then restart it
			if (p2Ind >= chromosomeSize) {
				p2Ind -= chromosomeSize;
			}

			if (p1Ind >= chromosomeSize) {
				p1Ind -= chromosomeSize;
			}

			// If the child 0 not contains the allele of the p2 then insert it
			if (newIndiv[0].containsAllele(chromosomeSize - 1, i2.getGene(p2Ind)) == false) {
				// if the actual gene of the child is empty then insert it
				for (int chldInd = 0; chldInd < chromosomeSize; chldInd++) {
					if (newIndiv[0].getGene(chldInd) == -1) {
						newIndiv[0].setGene(i2.getGene(p2Ind), chldInd);
						genesP2++;
						break;
					}
				}

			}

			// If the child 1 not contains the allele of the p1 then insert it
			if (newIndiv[1].containsAllele(chromosomeSize - 1, i1.getGene(p1Ind)) == false) {
				// if the actual gene of the child is empty then insert it
				
				for (int chldInd = 0; chldInd < chromosomeSize; chldInd++) {
					if (newIndiv[1].getGene(chldInd) == -1) {
						newIndiv[1].setGene(i1.getGene(p1Ind), chldInd);
						genesP1++;
						break;
					}
				}

			}
			p2Ind++;
			p1Ind++;

		}

		return newIndiv;

	}

	/**
	 * Selection method, implements a simple tournament selection.
	 *
	 * @param pop the population
	 * @return the tournament winner individual
	 */
	public static Individual selection(Population pop) {

		Population tournament = new Population(TOURNAMENT_SIZE);
		Random rand = new Random();

		Set<Integer> selectedIndiv = new HashSet<>();

		int popSize = pop.getPopulationSize();

		int popIndex;

		for (int i = 0; i < TOURNAMENT_SIZE; i++) {
			do {
				popIndex = rand.nextInt(popSize);
			} while (selectedIndiv.contains(popIndex) == true);

			tournament.saveIndividual(pop.getIndividual(popIndex), i);
			selectedIndiv.add(popIndex);
		}

		return tournament.getFittest(1)[0];
	}

	/**
	 * Mutation using the swap method.
	 *
	 * @param indiv the individual to mutate
	 * @return the number of mutations
	 */
	public static int mutation(Individual indiv) {

		int chromosomeSize = indiv.getChromosomeSize();
		Random rand = new Random();

		int genesMutated = 0;

		for (int i = 0; i < chromosomeSize; i++) {
			if (rand.nextDouble() <= GENE_MUTATION_PROB) {
				// Finds a gene to swap with this
				int j;
				do {
					j = rand.nextInt(chromosomeSize);
				} while (j == i);

				int temp = indiv.getGene(i);
				indiv.setGene(indiv.getGene(j), i);
				indiv.setGene(temp, j);
				genesMutated++;
			}
		}
		return genesMutated;
	}

	/**
	 * Gets the mutation rate.
	 *
	 * @return the mutation rate
	 */
	public static double getMutationRate() {
		return mutationRate;
	}

	/**
	 * Gets the algorithm type.
	 *
	 * @return the algorithm type
	 */
	public static AlgorithmType getAlgType() {
		return algType;
	}

	/**
	 * Sets the algorithm type.
	 *
	 * @param algType the new algorithm type
	 */
	public static void setAlgType(AlgorithmType algType) {
		Algorithm.algType = algType;
	}

}
