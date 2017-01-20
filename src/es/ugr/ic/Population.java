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

// TODO: Auto-generated Javadoc
/**
 * The Class Population.
 */
public class Population {

	/** The individuals. */
	private Individual [] individuals = null;
	
	/** The population fitness. */
	private long populationFitness = 0;
	
	/** The population size. */
	private int populationSize = 0;
	
	/**
	 * Generate a random population.
	 *
	 * @param numIndiv the number of individuals
	 */
	public void generatePopulation (int numIndiv){
		individuals = new Individual [numIndiv];
		
		int chromosomeSize = Data.getSize();
		if (chromosomeSize <= 0) {
			System.err.println("Error initializating population, gene size couldn't be less or equal than 0");
			System.err.println("Aborting population's generation...");
			return;
		}
		for (int i = 0; i < individuals.length; i++) {
			individuals[i] = new Individual(chromosomeSize);
		}
		
		populationSize = numIndiv;
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
	 * @param index the index of the individual in the array
	 * @return the individual
	 */
	public Individual getIndividual(int index){
		return individuals[index];
	}
	
	/**
	 * Save a single individual.
	 *
	 * 
	 *
	 * @param indiv the individual
	 * @param index the index of the individual in the array
	 */
	public void saveIndividual(Individual indiv, int index){
		
		individuals[index] = indiv;
	}
	
	/**
	 * Gets the fittest individual.
	 *
	 * @return the fittest
	 */
	public Individual getFittest(){
		int fittestIndex = 0;
		
		for (int i = 1; i < individuals.length; i++) {
			if (individuals[fittestIndex].getFitness() < individuals[i].getFitness()) {
				fittestIndex = i;
			}
		}
		return individuals[fittestIndex];
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
	 * @param populationFitness the new population fitness
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
	 * @param populationSize the new population size
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
}
