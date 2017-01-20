/*
 * Archivo: Individual.java 
 * Proyecto: Evolutionary_algorithm_IC
 * Práctica de Algoritmos evolutivos: Resolución de problemas NP. QAP
 * 
 * Autor: Aythami Estévez Olivas
 * Email: aythae@correo.ugr.es
 * Fecha: 17-ene-2017
 * Asignatura: Inteligencia computacional
 * Repositorio: https://github.com/AythaE/Evolutionary_algorithm_IC
 * 
 * Master Universitario en Ingeniería Informática
 * Universidad de Granada
 */
package es.ugr.ic;

import java.util.Arrays;
import java.util.Random;

/**
 * The Class Individual that match to a permutation.
 */
public class Individual {
	
	/** The genes. */
	private int[] genes = null;
	
	/** The fitness value of the current genes. */
	private long fitness = 0;
	
	/**
	 * Instantiates a new individual with random genes.
	 *
	 * @param geneLenght the gene lenght
	 */
	public Individual(int geneLenght)
	{
		genes = new int[geneLenght];
		Arrays.fill(genes, -1);
		Random rand = new Random();
		
		boolean containsVal = true;
		int gen = -1;
		
		for (int i = 0; i < genes.length; i++) {
			gen = rand.nextInt(geneLenght);
			
			containsVal = containsAlelo(i, gen);
			
			if (containsVal == false) {
				genes[i] = gen;
			}
			else{
				i--;
			}
		}
	}

	/**
	 * Check if the genes contains an alelo.
	 *
	 * @param it the actual iterator value 
	 * @param valueToEval the value of the alelo to evaluate
	 * @return true, if successful
	 */
	private boolean containsAlelo(int it, int valueToEval){
		
		for (int j = it; j >= 0; j--) {
			if(genes[j] == valueToEval){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the genes.
	 *
	 * @return the genes
	 */
	public int[] getGenes() {
		return genes;
	}

	/**
	 * Sets the genes.
	 *
	 * @param genes the new genes
	 */
	public void setGenes(int[] genes) {
		this.genes = genes;
	}
	
	/**
	 * Sets the gen.
	 *
	 * @param gen the new gen
	 * @param index index of the new gen
	 */
	public void setGenes(int gen, int index) {
		this.genes[index] = gen;
	}
	
	/**
	 * Calc fitness.
	 */
	public long calcFitness(){
		long tempFitness= 1;
		int [][] distance = Data.getDistances();
		long [][] matFlow = Data.getMatFlow();
		
		for (int i = 0; i < distance.length; i++) {
			for (int j = 0; j < distance[i].length; j++) {
				tempFitness += -(matFlow[i][j] * distance[genes[i]][genes[j]]);
			}
		}
		fitness = tempFitness;
		
		return fitness;
	}

	/**
	 * Gets the fitness.
	 *
	 * @return the fitness
	 */
	public long getFitness() {
		return fitness;
	}
	
	@Override
	public String toString() {
		return "Fitness: "+this.fitness+" "+Arrays.toString(genes);
	}
}
