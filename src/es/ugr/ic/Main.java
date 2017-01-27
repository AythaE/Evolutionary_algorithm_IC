/*
 * Archivo: Main.java 
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

import java.io.FileNotFoundException;
import java.util.Scanner;

import es.ugr.ic.Algorithm.AlgorithmType;
import es.ugr.ic.Data.DataFile;

// TODO: Auto-generated Javadoc
/**
 * The Class Main that execute the genetic algorithm, it contains the methods
 * to interact with the user and some test using during the development of 
 * this software.
 */
public class Main {

	/** 
	 * The Constant POPULATION_SIZE. If you plan to execute the optimized
	 * versions of the algorithm choose a multiple of 4 or 2 to take full 
	 * advantage of multithread.
	 */
	private static final int POPULATION_SIZE = 80;
	
	/**  
	 * The Constant MAX_NUM_GENERATIONS. It set the maximum number of generations
	 * in case that the execution of a fixed number of generations were selected. 
	 */
	private static final int MAX_NUM_GENERATIONS = 100;
	
	/** The Constant TEST to execute test methods or production methods. */
	private static final boolean TEST = false;
	
	/** The Constant SEPARATOR. */
	private static final String SEPARATOR = "--------------------------------------------------------------------------------";

	/**
	 * The main method, handle the interaction with the user or execute the test methods.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			Data.readData(DataFile.tai256c);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file " + Data.getDataFile().getAbsolutePath());
			e.printStackTrace();
			return;
		}

		if (TEST) {
			testMethods();
		}
		
		else {
			//The user select the variant of the algorithm that want to execute
			boolean variantSelected = false;
			Scanner sc = new Scanner(System.in);
			do {
				String option;
				System.out.println("Genetic algorithm for the QAP problem");
				System.out.println("\nAuthor: Aythami Estévez Olivas <aythae@gmail.com>");
				System.out.println("Repository: https://github.com/AythaE/Evolutionary_algorithm_IC");
				System.out.println("\n\nChoose the variant of the algorithm");
				System.out.println("1) Standard");
				System.out.println("2) Lamarckian");
				System.out.println("3) Baldwinian");
				System.out.print("\nYour option: ");
				
				option = sc.nextLine().trim().toLowerCase();
				
				switch (option) {
				case "1":
				case "standard":
					Algorithm.setAlgType(AlgorithmType.STANDARD);
					variantSelected = true;
					break;
					
				case "2":
				case "lamarckian":
					Algorithm.setAlgType(AlgorithmType.LAMARCKIAN);
					variantSelected = true;
					break;
					
				case "3":
				case "baldwinian":
					Algorithm.setAlgType(AlgorithmType.BALDWINIAN);
					variantSelected = true;
					break;
					
				default:
					System.err.println("\nWrong option, available options:");
					System.err.println("- 1 or Standard for the standard variant");
					System.err.println("- 2 or Lamarckian for the lamarckian variant");
					System.err.println("- 3 or Baldwinian for the baldwinian variant\n\n\n");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					break;
				}
				
			} while (variantSelected == false);
			
			variantSelected = false;
			
			int maxGenerationWOImprove = 0;
			if (Algorithm.getAlgType() == AlgorithmType.STANDARD) {
				maxGenerationWOImprove = Algorithm.MAX_GENERATION_WO_IMPROVEMENT_STANDARD;
			} else {
				maxGenerationWOImprove = Algorithm.MAX_GENERATION_WO_IMPROVEMENT_OPTIMIZED;
			}
			
			//The user select the termination condition: A fixed number of generations 
			//or execute the algorithm until a fixed number of generation without 
			//improvement were reached (best individual don't change)  
			do {
				String option2;
				
				System.out.println("\n\nDo you want to execute the algorithm:");
				System.out.println("1) during " + MAX_NUM_GENERATIONS
						+ " generations");
				System.out.println("2) until " + maxGenerationWOImprove
						+ " generations"
						+ " without improve have been reached?: ");
				System.out.print("\nYour option: ");
				option2 = sc.nextLine().trim().toLowerCase();
				switch (option2) {
				case "1":
					executeAGivenNumberOfGenerations();
					variantSelected = true;
					break;

				case "2":
					executeUntilNoMoreImprove();
					variantSelected = true;
					break;

				default:
					System.err.println("\nWrong option, available options:");
					System.err.println("- 1 for " + MAX_NUM_GENERATIONS
							+ " generations of the algorithm");
					System.err
							.println("- 2 for executing until "
									+ maxGenerationWOImprove
									+ " generations without improve have been reached\n\n\n");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					break;
				}
			} while (variantSelected == false);
			
			sc.close();
			
			

		}
	}
	
	
	/**
	 * Execute the algorithm until a fixed number of generations without 
	 * improvement where reached. The fixed number of generation is 
	 * different in standard and optimizated algorithms. Check the constants
	 * {@link Algorithm#MAX_GENERATION_WO_IMPROVEMENT_STANDARD} and
	 * {@link Algorithm#MAX_GENERATION_WO_IMPROVEMENT_OPTIMIZED}.
	 */
	private static void executeUntilNoMoreImprove(){
		long tIni, tFin;
		int generation = 0;
		
		System.out.println("\n\n\nEXECUTING "+Algorithm.getAlgType()+" GENETIC ALGORITHM");
		System.out.println("\n"+SEPARATOR);
		tIni = System.currentTimeMillis();
		Population population = new Population(POPULATION_SIZE);
		population.generateRandomPopulation();
		
		Algorithm.evaluatePopulation(population);
		
		while (Algorithm.isFinished(population) == false) {
			generation++;
			population = Algorithm.evolvePopulation(population);
			Algorithm.evaluatePopulation(population);
			
			System.out.println(SEPARATOR);
			System.out.println("Generation: "+generation);
			System.out.println("Best individual: "+population.getFittest(1)[0]);
			System.out.println("Avg fitness: "+population.getPopulationFitness());
			System.out.println("Gene mutation rate: "+Algorithm.getMutationRate()+"%");
			
		}
		tFin = System.currentTimeMillis();
		printResults(tIni, tFin, generation, population);

	}
	
	/**
	 * Execute the algorithm a fixed number of generations. Check the constant
	 * {@link Main#MAX_NUM_GENERATIONS}.
	 */
	private static void executeAGivenNumberOfGenerations(){
		long tIni, tFin;
		int generation = 0;
		
		System.out.println("\n\n\nEXECUTING "+Algorithm.getAlgType()+" GENETIC ALGORITHM");
		System.out.println("\n"+SEPARATOR);
		tIni = System.currentTimeMillis();
		Population population = new Population(POPULATION_SIZE);
		population.generateRandomPopulation();
		
		Algorithm.evaluatePopulation(population);
		
		for(int i=0; i< MAX_NUM_GENERATIONS; i++) {
			generation++;
			population = Algorithm.evolvePopulation(population);
			Algorithm.evaluatePopulation(population);
			
			System.out.println(SEPARATOR);
			System.out.println("Generation: "+generation);
			System.out.println("Best individual: "+population.getFittest(1)[0]);
			System.out.println("Avg fitness: "+population.getPopulationFitness());
			System.out.println("Gene mutation rate: "+Algorithm.getMutationRate()+"%");
			
		}
		tFin = System.currentTimeMillis();
		printResults(tIni, tFin, generation, population);

	}

	/**
	 * Prints the results after the algorithm has finished.
	 *
	 * @param tIni
	 *            the initial time when the algorithm started
	 * @param tFin
	 *            the final time when the algorithm finished
	 * @param generation
	 *            the final number of generations
	 * @param population
	 *            the final population
	 */
	private static void printResults(long tIni, long tFin, int generation, Population population) {

		System.out.println(SEPARATOR);
		System.out.println(SEPARATOR);

		Individual fittest = population.getFittest(1)[0];
		System.out.println("\n\nFinal results:\n");
		System.out.println("- Algorithm type: " + Algorithm.getAlgType());
		System.out.println("- Gene mutation probability: " + Algorithm.GENE_MUTATION_PROB);
		System.out.println("- Optimization probability: " + Algorithm.OPTIMIZATION_PROB);
		System.out.println("- Tournament size: " + Algorithm.TOURNAMENT_SIZE);
		System.out.println("- Elitism individuals: " + Algorithm.ELITISM_INDIVIDUALS);
		System.out.println(population);
		System.out.println("- Generation: " + generation);
		System.out.println("- Best solution: " + fittest);
		System.out.println("- Difference from optimal solution: "
				+ ((((double) fittest.getFitness() - Data.getOptimalSolution()) / Data.getOptimalSolution()) * 100)
				+ "%");

		System.out.println("- Recalculated best fitness (in case of error): " + fittest.calcFitnessStandard());
		long execTime = tFin - tIni;
		System.out.println("- Time: " + execTime + " ms");
		System.out.println("- Mean time per generation: " + ((double) execTime / generation) + " ms");

	}

	/**
	 * Test methods of the other classes.
	 */
	private static void testMethods() {
		
		Data.printLoadedFile();
		
		Population population = new Population(POPULATION_SIZE);
		Algorithm.setAlgType(AlgorithmType.STANDARD);
		population.generateRandomPopulation();
		Algorithm.evaluatePopulation(population);

		System.out.println("\nAlg is finished: " + Algorithm.isFinished(population));

		System.out.println("\n"+population);
		System.out.println("\nFittest 3 indiv: ");
		Individual [] fittest = population.getFittest(3);
		for (int i = 0; i < fittest.length; i++) {
			System.out.println("- "+fittest[i]);
		}

		System.out.println("\nSelection: the results are the parents of the crossover operation");
		
		
		
		Individual parent1 = Algorithm.selection(population);
		
		Individual parent2;
		do {
			parent2= Algorithm.selection(population);
		} while (parent1.equals(parent2));

	
		Individual[] childs = Algorithm.crossover(parent1, parent2);
		childs[0].calcFitness(AlgorithmType.STANDARD);
		childs[1].calcFitness(AlgorithmType.STANDARD);

		System.out.println("\nCrossover:\n-Parent 1: " + parent1 + "\n-Parent 2: "
				+ parent2 + "\n-Child 1:  " + childs[0]+ "\n-Child 2:  " + childs[1]);
		
		Individual i1 = new Individual(population.getIndividual(0));
		
		System.out.println("\nComparing a copy of individuals");
		System.out.println("Is i1 equal to pop 0: "+i1.equals(population.getIndividual(0)));
		
		int mutationCount = Algorithm.mutation(i1);
		Algorithm.evaluatePopulation(population);
		System.out.println("\nMutation:\n-Original: "+i1+"\n-Mutated:  "+ population.getIndividual(0)+"\n-Number of mutations: "+mutationCount);
		
		System.out.println("\nEvolving population:");
		Population evolPopulation = Algorithm.evolvePopulation(population);
		Algorithm.evaluatePopulation(evolPopulation);
		System.out.println(evolPopulation);
		
		
		Individual individual = new Individual(Data.getSize(), true);
		individual.calcFitness(AlgorithmType.STANDARD);
		Individual individualSwaped = new Individual(individual);
		
		int tempGene = individualSwaped.getGene(0);
		individualSwaped.setGene(individualSwaped.getGene(2), 0);
		individualSwaped.setGene(tempGene, 2);
		
		System.out.println("\nCalc new fitness from old one");
		System.out.println("- Original individual: " + individual);
		System.out.println("- Swaped individual (fitness value is wrong): " + individualSwaped);
		System.out.println("- Fitness with calcNewFitness method: "
				+ individualSwaped.calcNewFitness(individual.getFitness(), individual.getGenes(), 0, 2));
		System.out.println("- Good fitness value with the normal calcFitness method: " 
				+ individualSwaped.calcFitness(AlgorithmType.STANDARD));
		
		
		Algorithm.setAlgType(AlgorithmType.LAMARCKIAN);
		Population optPopulation = new Population(POPULATION_SIZE);
		System.out.println("\nLamarckian Algorithm:");
		long tIni = 0, tFin=0;
		
		tIni = System.currentTimeMillis();
		optPopulation.generateRandomPopulation();
		Algorithm.evaluatePopulation(optPopulation);
		tFin = System.currentTimeMillis();
		
		System.out.println(optPopulation);
		long execTime = tFin - tIni;
		System.out.println("- Time: "+execTime+" ms");
		
		
		Algorithm.setAlgType(AlgorithmType.BALDWINIAN);
		optPopulation = new Population(POPULATION_SIZE);
		System.out.println("\nBaldwinian Algorithm:");
		tIni = 0; tFin=0;
		
		tIni = System.currentTimeMillis();
		optPopulation.generateRandomPopulation();
		Algorithm.evaluatePopulation(optPopulation);
		tFin = System.currentTimeMillis();
		
		System.out.println(optPopulation);
		execTime = tFin - tIni;
		System.out.println("- Time: "+execTime+" ms");
		
		System.out.println("\nEvaluating the last population by the standard method:");
		Algorithm.setAlgType(AlgorithmType.STANDARD);
		Algorithm.evaluatePopulation(optPopulation);
		System.out.println(optPopulation);


	}

}
