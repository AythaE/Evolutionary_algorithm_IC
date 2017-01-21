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

import es.ugr.ic.Data.DataFile;

/**
 * The Class Main.
 */
public class Main {

	/** The Constant POPULATION_SIZE. */
	private static final int POPULATION_SIZE = 100;
	private static final String SEPARATOR = "--------------------------------------------------------------------------------";

	/**
	 * The main method 
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		try {
			Data.readData(DataFile.tai256c);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file " + Data.getDataFile().getAbsolutePath());
			e.printStackTrace();
			return;
		}

		// Data.printLoadedFile();
		//testMethods();
		long tIni, tFin;
		tIni = System.currentTimeMillis();
		int generation = 0;
		Population population = new Population(POPULATION_SIZE);
		population.generateRandomPopulation();
		
		Algorithm.evaluatePopulation(population);
		
		while (Algorithm.isFinished(population) == false) {
			generation++;
			population = Algorithm.evolvePopulation(population);
			Algorithm.evaluatePopulation(population);
			
			System.out.println(SEPARATOR);
			System.out.println("Generation: "+generation);
			System.out.println("Max fitness: "+population.getFittest(1)[0].getFitness());
			System.out.println("Avg fitness: "+population.getPopulationFitness());
			System.out.println("Mutation rate: "+Algorithm.getMutationRate()+"%");
			
		}
		tFin = System.currentTimeMillis();
		System.out.println(SEPARATOR);
		System.out.println(SEPARATOR);
		
		System.out.println("\n\nFinal results:");
		System.out.println("- Initial population size: "+POPULATION_SIZE);
		System.out.println("- Generation: "+generation);
		System.out.println("- Best solution: "+population.getFittest(1)[0]);
		System.out.println("- Time: "+(tFin- tIni)+" ms");

	}

	/**
	 * Test methods of the other classes.
	 */
	private static void testMethods() {
		Population population = new Population(POPULATION_SIZE);

		population.generateRandomPopulation();
		Algorithm.evaluatePopulation(population);

		System.out.println("\nAlg is finished: " + Algorithm.isFinished(population));

		System.out.println("\nPop fitness: " + population.getPopulationFitness());

		System.out.println("\nIndivduals: ");
		for (int i = 0; i < population.getIndividuals().length; i++) {
			System.out.println(population.getIndividuals()[i]);

		}

		Individual i1;
		System.out.println("\nFittest indiv: " + (i1 = population.getFittest(1)[0]));
		System.out.println("\nFittest 3 indiv: ");
		Individual [] fittest = population.getFittest(3);
		for (int i = 0; i < fittest.length; i++) {
			System.out.println("- "+fittest[i]);
		}

		System.out.println("\nComparing individuals");
		System.out.println("Is i1 equal to fittest: "+i1.equals(population.getFittest(1)[0]));
		System.out.println("\nSelection: the results are the parents of the crossover operation");
		
		
		
		Individual parent1 = Algorithm.selection(population);
		
		Individual parent2;
		do {
			parent2= Algorithm.selection(population);
		} while (parent1.equals(parent2));

	
		Individual[] childs = Algorithm.crossover(parent1, parent2);
		childs[0].calcFitness();
		childs[1].calcFitness();

		System.out.println("\nCrossover:\n-Parent 1: " + parent1 + "\n-Parent 2: "
				+ parent2 + "\n-Child 1:  " + childs[0]+ "\n-Child 2:  " + childs[1]);
		
		i1 = new Individual(population.getIndividual(0));
		
		System.out.println("\nComparing a copy of individuals");
		System.out.println("Is i1 equal to pop 0: "+i1.equals(population.getIndividual(0)));
		
		int mutationCount = Algorithm.mutation(i1);
		Algorithm.evaluatePopulation(population);
		System.out.println("\nMutation:\n-Original: "+i1+"\n-Mutated:  "+ population.getIndividual(0)+"\n-Number of mutations: "+mutationCount);
	}

}
