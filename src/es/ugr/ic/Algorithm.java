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

/**
 * The Class Algorithm that contains the methods of the genetic algorithm.
 */
public class Algorithm {
	
	/** The Constant MAX_GENERATION_WO_IMPROVEMENT. */
	private static final int MAX_GENERATION_WO_IMPROVEMENT = 500;
	
	/** The max population fitness. */
	private static long maxPopulationFitness = 0;
	
	/** The generations WO improvement. */
	private static long generationsWOImprovement = 0;
	
	/**
	 * Evaluate population.
	 *
	 * @param pop the pop
	 */
	public static void evaluatePopulation(Population pop) {
		Individual[] popIndiv = pop.getIndividuals();
		long popFitness = 0;
		
		for (Individual indiv : popIndiv) {
			popFitness += indiv.calcFitness();
		}
		
		popFitness /= pop.getPopulationSize();
		
		pop.setPopulationFitness(popFitness);
		
		
	}
	
	/**
	 * Check if Algorithm has finished. The termination condition of this
	 * algorithm is a max number of generations without improvement.
	 *
	 * @param pop the pop
	 * @return true, if is finished
	 */
	public static boolean isFinished (Population pop) {
		if (maxPopulationFitness < pop.getPopulationFitness()) {
			maxPopulationFitness = pop.getPopulationFitness();
			generationsWOImprovement = 0;
			return false;
		}
		else {
			generationsWOImprovement++;
			
			if (generationsWOImprovement < MAX_GENERATION_WO_IMPROVEMENT) 
				return false;
			else
				return true;
		}
	}

}

