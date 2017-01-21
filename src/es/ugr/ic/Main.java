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

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {

	/** 
	 * The Constant POPULATION_SIZE. If you plan to execute the optimized
	 * versions of the algorithm choose a multiple of 4 or 2 to take full 
	 * advantage of multithread 
	 */
	private static final int POPULATION_SIZE = 100;
	
	/** The Constant TAI_256C_OPTIMAL. */
	private static final long TAI_256C_OPTIMAL = 44759294;
	
	/** The Constant TEST. */
	private static final boolean TEST = true;
	
	/** The Constant SEPARATOR. */
	private static final String SEPARATOR = "--------------------------------------------------------------------------------";

	/**
	 * The main method .
	 *
	 * @param args            the arguments
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
				System.out.println("Min fitness: "+population.getFittest(1)[0].getFitness());
				System.out.println("Avg fitness: "+population.getPopulationFitness());
				System.out.println("Mutation rate: "+Algorithm.getMutationRate()+"%");
				
			}
			tFin = System.currentTimeMillis();
			printResults(tIni, tFin, generation, population);

		}
	}

	/**
	 * Prints the results.
	 *
	 * @param tIni the t ini
	 * @param tFin the t fin
	 * @param generation the generation
	 * @param population the population
	 */
	private static void printResults(long tIni, long tFin, int generation, Population population) {
		
		System.out.println(SEPARATOR);
		System.out.println(SEPARATOR);
		
		Individual fittest = population.getFittest(1)[0];
		System.out.println("\n\nFinal results:\n");
		System.out.println("- Algorithm type: "+Algorithm.getAlgType());
		System.out.println("- Gene mutation probability: "+Algorithm.MUTATION_PROB);
		System.out.println("- Tournament size: "+Algorithm.TOURNAMENT_SIZE);
		System.out.println("- Elitism individuals: "+Algorithm.ELITISM_INDIVIDUALS);
		System.out.println(population);
		System.out.println("- Generation: "+generation);
		System.out.println("- Best solution: "+fittest);
		long execTime = tFin - tIni;
		System.out.println("- Time: "+execTime+" ms");
		System.out.println("- Mean time per generation: "+((double)execTime/generation)+" ms");
		if (Data.getDataFileName() == DataFile.tai256c) {
			System.out.println("- Difference from optimal solution: "+
			((((double)fittest.getFitness()-TAI_256C_OPTIMAL)/TAI_256C_OPTIMAL)*100)+"%");
		}
	}

	/**
	 * Test methods of the other classes.
	 */
	@SuppressWarnings("unused")
	private static void testMethods() {
		
		Data.printLoadedFile();
		
		Population population = new Population(POPULATION_SIZE);

		population.generateRandomPopulation();
		Algorithm.evaluatePopulation(population);

		System.out.println("\nAlg is finished: " + Algorithm.isFinished(population));

		System.out.println(population);
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
		childs[0].calcFitness();
		childs[1].calcFitness();

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
		
		Population optPopulation = new Population(POPULATION_SIZE);
		System.out.println("\nGreedy Heuristic:");
		long tIni = 0, tFin=0;
		tIni = System.currentTimeMillis();
		optPopulation.generateGreedyPopulation();
		tFin = System.currentTimeMillis();
		Algorithm.evaluatePopulation(optPopulation);
		System.out.println(optPopulation);
		long execTime = tFin - tIni;
		System.out.println("- Time: "+execTime+" ms");
		
		
		


	}

}
