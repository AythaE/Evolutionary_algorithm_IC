/*
 * Archivo: Population.java 
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The Class Population.
 */
public class Population {

	/** The individuals. */
	private Individual[] individuals = null;

	/** The population fitness. */
	private long populationFitness = 0;

	/** The population size. */
	private int populationSize = 0;

	/**
	 * Instantiates a new population.
	 *
	 * @param numIndiv
	 *            the num indiv
	 */
	public Population(int numIndiv) {
		individuals = new Individual[numIndiv];
		populationSize = numIndiv;
	}

	/**
	 * Generate a random population.
	 */
	public void generateRandomPopulation() {

		int chromosomeSize = Data.getSize();
		if (chromosomeSize <= 0) {
			System.err.println("Error initializating population, gene size couldn't be less or equal than 0");
			System.err.println("Aborting population's generation...");
			return;
		}
		for (int i = 0; i < individuals.length; i++) {
			individuals[i] = new Individual(chromosomeSize, true, false);
		}

	}

	/**
	 * Generate a random population and use an 2-opt greedy algorithm to
	 * optimize it.
	 */
	public void generateGreedyPopulation() {

		int chromosomeSize = Data.getSize();
		if (chromosomeSize <= 0) {
			System.err.println("Error initializating population, gene size couldn't be less or equal than 0");
			System.err.println("Aborting population's generation...");
			return;
		}

		//Calculate the number of processors to parallelize
		int processors = Runtime.getRuntime().availableProcessors();
		int numThreads;
		
		if (processors >= 2) {
			if (processors >= 4)
				numThreads = 4;
			else
				numThreads = 2;
			
			// Calculate the part of the population for divide it between threads
			int partIndiv = populationSize / numThreads;

		
			// If populationSize is no an divisible by numThreads then calc the rest
			int rest =populationSize % numThreads;

			//Instantiate the Runnable classes
			GenerateGreedyPopulationThread[] thBodys = new GenerateGreedyPopulationThread[numThreads];
			for (int i = 0; i < thBodys.length; i++) {
				//Add the rest of the division to the last one
				if (i == numThreads - 1) 
					thBodys[i] = new GenerateGreedyPopulationThread(partIndiv * i, partIndiv * (i+1) + rest, chromosomeSize, individuals);
				else
					thBodys[i] = new GenerateGreedyPopulationThread(partIndiv * i, partIndiv * (i+1), chromosomeSize, individuals);
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
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		
		} 
		//If number of processors is less than 2 then execute it on a single thread
		else{
			for (int i = 0; i < individuals.length; i++) {
				individuals[i] = new Individual(chromosomeSize, true, true);
			}
		}
		
		

	}

	/**
	 * Generate empty population.
	 */
	public void generateEmptyPopulation() {

		int chromosomeSize = Data.getSize();
		if (chromosomeSize <= 0) {
			System.err.println("Error initializating population, gene size couldn't be less or equal than 0");
			System.err.println("Aborting population's generation...");
			return;
		}
		for (int i = 0; i < individuals.length; i++) {
			individuals[i] = new Individual(chromosomeSize, false, false);
		}

	}

	/**
	 * Gets the individuals.
	 *
	 * @return the individuals
	 */
	public Individual[] getIndividuals() {
		return individuals;
	}

	/**
	 * Gets a single individual.
	 *
	 * @param index
	 *            the index of the individual in the array
	 * @return the individual
	 */
	public Individual getIndividual(int index) {
		return individuals[index];
	}

	/**
	 * Save a single individual.
	 *
	 * 
	 *
	 * @param indiv
	 *            the individual
	 * @param index
	 *            the index of the individual in the array
	 */
	public void saveIndividual(Individual indiv, int index) {

		individuals[index] = indiv;
	}

	/**
	 * Gets the fittest individuals.
	 * 
	 * @param numIndiv
	 *            TODO
	 *
	 * @return the fittest individuals
	 */
	public Individual[] getFittest(int numIndiv) {

		// TODO Test
		SortedSet<Individual> sortSet = new TreeSet<>();

		sortSet.addAll(Arrays.asList(individuals));
		/*
		 * Individual [] copyOfIndiv = Arrays.copyOf(individuals,
		 * populationSize);
		 * 
		 * 
		 * //Sort a copy of the individual array to select the best numIndiv
		 * //individuals Arrays.sort(copyOfIndiv, new Comparator<Individual>() {
		 * 
		 * @Override public int compare(Individual o1, Individual o2) { if
		 * (o1.getFitness() < o2.getFitness()) { return -1; } else if
		 * (o1.getFitness() > o2.getFitness()) { return 1; } return 0; } });
		 * 
		 */
		Individual[] copyOfIndiv = sortSet.toArray(new Individual[sortSet.size()]);

		return Arrays.copyOf(copyOfIndiv, numIndiv);
	}

	/**
	 * Gets the population fitness.
	 *
	 * @return the population fitness
	 */
	public long getPopulationFitness() {
		return populationFitness;
	}

	/**
	 * Sets the population fitness.
	 *
	 * @param populationFitness
	 *            the new population fitness
	 */
	public void setPopulationFitness(long populationFitness) {
		this.populationFitness = populationFitness;
	}

	/**
	 * Gets the population size.
	 *
	 * @return the population size
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * Sets the population size.
	 *
	 * @param populationSize
	 *            the new population size
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sBuild = new StringBuilder();

		sBuild.append("- Population size: " + this.populationSize + "\n");
		sBuild.append("- Average fitness: " + this.populationFitness + "\n");
		sBuild.append("- Indivduals:\n");
		for (int i = 0; i < this.individuals.length; i++) {
			sBuild.append("  - " + this.individuals[i] + "\n");

		}

		sBuild.append("\n\n- Fittest individuals:\n");
		Individual[] fittest = this.getFittest(Algorithm.ELITISM_INDIVIDUALS);
		for (int i = 0; i < fittest.length; i++) {
			sBuild.append("  - " + fittest[i] + "\n");

		}
		return sBuild.toString();
	}

}
