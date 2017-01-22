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
import java.util.SortedSet;
import java.util.TreeSet;

// TODO: Auto-generated Javadoc
/**
 * The Class Population that represent a generation of the genetic algorithm,
 * it handle an array of Individuals and contains operations for all of them.
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
	 * @param numIndiv the num indiv
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
			individuals[i] = new Individual(chromosomeSize, true);
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
			individuals[i] = new Individual(chromosomeSize, false);
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
	 * @param index the index of the individual in the array
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
	 * @param numIndiv the number of fittest individuals to be returned
	 *
	 * @return the fittest individuals
	 */
	public Individual[] getFittest(int numIndiv) {

		SortedSet<Individual> sortSet = new TreeSet<>();

		sortSet.addAll(Arrays.asList(individuals));
	
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
