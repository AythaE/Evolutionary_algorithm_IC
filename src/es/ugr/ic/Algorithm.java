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

import java.util.Random;

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
	
	
	
	/**
	 * Crossover operator, implements ordered crossover operator.
	 *
	 * @param i1 the individual 1, one of the parents
	 * @param i2 the individual 2, one of the parents
	 * @return the new individual
	 */
	public static Individual crossover(Individual i1, Individual i2){
		
		Random rand = new Random();
		int chromosomeSize = i1.getChromosomeSize();
		Individual newIndiv = new Individual(chromosomeSize, false);
		int num1 = -1, num2 = -1;
		int genesP1 = 0, genesP2 = 0;
		
		//Generate random numbers while numbers are equal, because then the child 
		//wouldn't has genes form parent 1, or the difference between them are
		//the chromosome size, because then the child wouldn't has genes from 
		//parent 2
		while (num1 == num2 || (genesP1 = Math.abs(num1-num2)) >= (chromosomeSize-1) ){
			num1 = rand.nextInt(chromosomeSize); 
			num2 = rand.nextInt(chromosomeSize);
		}
		
		//Add 1 to genesP1 because the array starts at 0 and the genesP1 is
		//the number of genes of the parent 1 - 1
		genesP1++;
		
		//Select the min number
		int pos1 = Math.min(num1, num2);
		
		//Select the max number
		int pos2 = Math.max(num1, num2);
		
		//Copy a fragment of the parent 1
		for (int i = pos1; i <= pos2; i++) {
			newIndiv.setGene(i1.getGene(i), i);
		}
		
		
		int p2Ind=  pos2+1;
		
		//Fill the rest of the new individual with the parent 2 from the beginning
		while((genesP1 + genesP2)< chromosomeSize){
			
			//In case the index is >= than the max value then restart it
			if (p2Ind >= chromosomeSize) {
				p2Ind -= chromosomeSize;
			}
			
			//If the child not contains the allele of the p2 then insert it
			if (newIndiv.containsAllele(chromosomeSize-1, i2.getGene(p2Ind)) == false) {
				//if the actual gene of the child is empty then insert it
				//FIXME Unnecessary iterations, think a way to use pos1 and pos2 to avoid the parent 1 part
				for (int chldInd = 0; chldInd < chromosomeSize; chldInd++) {
					if (newIndiv.getGene(chldInd) == -1) {
						newIndiv.setGene(i2.getGene(p2Ind), chldInd);
						genesP2++;
						break;
					}
				}
				
				
			}
			p2Ind++;

			
		}
		
		return newIndiv;
		
	}

}

