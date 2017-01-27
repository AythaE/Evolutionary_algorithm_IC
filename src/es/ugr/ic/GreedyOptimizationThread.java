/*
 * Archivo: GenerateGreedyPopulationThread.java 
 * Proyecto: Evolutionary_algorithm_IC
 * Práctica de Algoritmos evolutivos: Resolución de problemas NP. QAP
 * 
 * Autor: Aythami Estévez Olivas
 * Email: aythae@correo.ugr.es
 * Fecha: 21-ene-2017
 * Asignatura: Inteligencia computacional
 * Repositorio: https://github.com/AythaE/Evolutionary_algorithm_IC
 * 
 * Master Universitario en Ingeniería Informática
 * Universidad de Granada
 */
package es.ugr.ic;

import es.ugr.ic.Algorithm.AlgorithmType;

/**
 * The Class GreedyOptimizationThread used to make greedy local optimization in
 * parallel using the power of multicore CPUs.
 */
public class GreedyOptimizationThread implements Runnable {

	/** The initial index. */
	private int indexIni = -1;
	
	/** The last index. */
	private int indexFin = -1;
	
	/** The algorithm type. */
	private AlgorithmType algType;
	
	/** The individuals. */
	private Individual[] individuals;
	
	/** The sum fitness. */
	private long sumFitness = 0;

	/**
	 * Instantiates a new thread to execute greedy optimization in the fitness 
	 * calculation more efficiently.
	 *
	 * @param indexIni the initial index for the individuals
	 * @param indexFin the last index for the individuals
	 * @param type the algorithm type
	 * @param individuals the individuals
	 */
	public GreedyOptimizationThread(int indexIni, int indexFin, AlgorithmType type, Individual [] individuals) {
		super();
		this.indexIni = indexIni;
		this.indexFin = indexFin;
		this.algType = type;
		this.individuals = individuals;
	}

	/**
	 * Execute the calculation of the fitness (optimized or not depending on
	 * algType attribute)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (indexIni < Algorithm.ELITISM_INDIVIDUALS) {
			for (int i = 0; i < Algorithm.ELITISM_INDIVIDUALS; i++) {
				sumFitness += individuals[i].calcFitness(algType);
			}
			
			indexIni+=Algorithm.ELITISM_INDIVIDUALS;
		}
		
		for (int i = indexIni; i < indexFin; i++) {
			
			if (Math.random() < Algorithm.OPTIMIZATION_PROB) 
				sumFitness += individuals[i].calcFitness(algType);
			else
				sumFitness += individuals[i].calcFitness(AlgorithmType.STANDARD);

		}
	}

	/**
	 * Gets the sum fitness.
	 *
	 * @return the sum fitness
	 */
	public long getSumFitness() {
		return sumFitness;
	}

}
