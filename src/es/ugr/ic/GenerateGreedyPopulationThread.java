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

// TODO: Auto-generated Javadoc
/**
 * The Class GenerateGreedyPopulationThread.
 */
public class GenerateGreedyPopulationThread implements Runnable {

	/** The index fin. */
	private int indexIni = -1, indexFin = -1;
	
	/** The chromosome size. */
	private int chromosomeSize;
	
	/** The individuals. */
	private Individual[] individuals;

	/**
	 * Instantiates a new thread to generate greedy population more efficiently.
	 *
	 * @param indexIni the initial index for the population
	 * @param indexFin the end index fin for the population
	 * @param chromosomeSize the chromosome size
	 * @param individuals the individuals
	 */
	public GenerateGreedyPopulationThread(int indexIni, int indexFin, int chromosomeSize, Individual [] individuals) {
		super();
		this.indexIni = indexIni;
		this.indexFin = indexFin;
		this.chromosomeSize = chromosomeSize;
		this.individuals = individuals;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		for (int i = indexIni; i < indexFin; i++) {
			individuals[i] = new Individual(chromosomeSize, true, true);
		}
	}

}
