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
	private static final int POPULATION_SIZE=10;
	
	
	/**
	 * The main method to test the other classes.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			Data.readData(DataFile.tai256c);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file "+ Data.getDataFile().getAbsolutePath());
			e.printStackTrace();
		}
		
		Data.printLoadedFile();
		
		Population population = new Population();
		
		
		population.generatePopulation(POPULATION_SIZE);
		Algorithm.evaluatePopulation(population);
		
		
		long generation = 1;
		
		System.out.println("\nAlg is finished: "+Algorithm.isFinished(population));
			
			
		System.out.println("\nPop fitness: "+population.getPopulationFitness());
		
		System.out.println("\nIndivduals: ");
		for (int i = 0; i < population.getIndividuals().length; i++) {
			System.out.println(population.getIndividuals()[i]);
		}
		
		System.out.println("\nFittest indiv: "+population.getFittest());
	}

}
