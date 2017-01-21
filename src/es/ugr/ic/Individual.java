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

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;

// TODO: Auto-generated Javadoc
/**
 * The Class Individual that match to a permutation.
 */
public class Individual implements Comparable<Individual>{
	
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
	 * @param applyGreedy TODO
	 */
	public Individual(int chromosomeSize, boolean initIndiv, boolean applyGreedy)
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
			
			if (applyGreedy) {
				Individual optimized = greedyHeuristic();
				
				this.chromosomeSize = optimized.getChromosomeSize();
				this.fitness = optimized.getFitness();
				this.genes = optimized.getGenes();
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
		this.genes = Arrays.copyOf(individual.getGenes(), individual.getGenes().length);
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
	 * Greedy heuristic.
	 *
	 * @return the individual
	 */
	public Individual greedyHeuristic(){
		Individual best;
		Individual S = new Individual(this);
		
		do {
			best = S;
			for (int i = 0; i < genes.length; i++) {
				for (int j = i+1; j < genes.length; j++) {
					Individual T = new Individual(S);
					
					int tempGene = T.getGene(i);
					T.setGene(T.getGene(j), i);
					T.setGene(tempGene, j);
					
					T.calcFitness();
					
					if (T.getFitness() < S.getFitness()) {
						S = new Individual(T);
					}
				}
			}
		} while (best.getFitness() > S.getFitness());
		
		return S;
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
		fitness = tempFitness;
		
		return fitness;
	}

	/**
	 * Gets the fitness.
	 *
	 * @return the fitness
	 */
	public long getFitness() {
		if (fitness == 0) {
			calcFitness();
		}
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Individual o) {
		if (this == o)
			return 0;
		if (o == null)
			return -1;
		if (getClass() != o.getClass())
			return -1;
		Individual other = (Individual) o;
		if (chromosomeSize != other.chromosomeSize)
			return -1;
		//If fitness is diferent then the individual with less fitness is the 
		//less indiv
		if (fitness != other.fitness)
			if (fitness < other.fitness) {
				return -1;
			}
			else {
				return 1;
			}
		//If fitness is equal then check if genes are equal
		else{
			if (Arrays.equals(genes, other.genes))
				return 0;
			//If genes are different return randomly 1 or -1 because there 
			//isn't a way to determine which one is less than the other
			else{
				if(Math.random() > 0.5)
					return 1;
				else 
					return -1;
			}
		}
		
	}
	
	
}
