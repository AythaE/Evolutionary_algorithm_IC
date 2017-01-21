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

// TODO: Auto-generated Javadoc
/**
 * The Class Individual that match to a permutation.
 */
public class Individual {
	
	/** The genes. */
	private int[] genes = null;
	
	/** The fitness value of the current genes. */
	private long fitness = 0;
	
	/** The chromosome size. */
	private int chromosomeSize = 0;
	
	/**
	 * Instantiates a new individual with random genes.
	 *
	 * @param chromosomeSize the gene length
	 * @param initIndiv flag to indicate if the new individual must be 
	 * initialized randomly
	 */
	public Individual(int chromosomeSize, boolean initIndiv)
	{
		this.genes = new int[chromosomeSize];
		Arrays.fill(this.genes, -1);
		
		if (initIndiv) {
			Random rand = new Random();
			
			boolean containsVal = true;
			int gen = -1;
			
			for (int i = 0; i < this.genes.length; i++) {
				gen = rand.nextInt(chromosomeSize);
				
				containsVal = containsAllele(i, gen);
				
				if (containsVal == false) {
					this.genes[i] = gen;
				}
				else{
					i--;
				}
			}
			
		}
		
		this.chromosomeSize = chromosomeSize;
	}

	/**
	 * Instantiates a new individual from other.
	 *
	 * @param individual the original individual to copy
	 */
	public Individual(Individual individual) {
		this.chromosomeSize = individual.getChromosomeSize();
		this.fitness = individual.getFitness();
		this.genes = individual.getGenes();
	}

	/**
	 * Check if the genes contains an allele from the end to the begin.
	 *
	 * @param it the actual iterator value 
	 * @param valueToEval the value of the allele to evaluate
	 * @return true, if successful
	 */
	public boolean containsAllele(int it, int valueToEval){
		
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
	 * Sets a gene.
	 *
	 * @param gen the new gene
	 * @param index index of the new gene
	 */
	public void setGene(int gen, int index) {
		this.genes[index] = gen;
	}
	
	/**
	 * Gets a gene.
	 *
	 * @param index index of the gene
	 * @return the gene
	 */
	public int getGene(int index) {
		return this.genes[index];
	}
	
	/**
	 * Calculate fitness.
	 *
	 * @return the long
	 */
	public long calcFitness(){
		long tempFitness= 1;
		int [][] distance = Data.getDistances();
		long [][] matFlow = Data.getMatFlow();
		
		for (int i = 0; i < distance.length; i++) {
			for (int j = 0; j < distance[i].length; j++) {
				tempFitness += matFlow[i][j] * distance[genes[i]][genes[j]];
			}
		}
		fitness = -tempFitness;
		
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
	
	/**
	 * Gets the chromosome size.
	 *
	 * @return the chromosome size
	 */
	public int getChromosomeSize() {
		return chromosomeSize;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Fitness: "+this.fitness+" "+Arrays.toString(genes);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;
		if (chromosomeSize != other.chromosomeSize)
			return false;
		if (fitness != other.fitness)
			return false;
		if (!Arrays.equals(genes, other.genes))
			return false;
		return true;
	}
}
