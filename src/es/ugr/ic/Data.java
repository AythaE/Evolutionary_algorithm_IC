/*
 * Archivo: Data.java 
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Class Data to handle the data files for the algorithm.
 */
public class Data {
	
	/** The Constant DATA_LOCATION. */
	private static final String DATA_LOCATION = "qap.data" + File.separatorChar;
	
	/** The Constant DATA_EXTENSION. */
	private static final String DATA_EXTENSION = ".dat";
	
	/**
	 * The Enum DataFile that contains all existent data files.
	 */
	public enum DataFile {

		bur26a, bur26b, bur26c, bur26d, bur26e, bur26f, bur26g, bur26h, chr12a,
		chr12b, chr12c, chr15a, chr15b, chr15c, chr18a, chr18b, chr20a, chr20b,
		chr20c, chr22a, chr22b, chr25a, lipa20a, lipa20b, lipa30a, lipa30b,
		lipa40a, lipa40b, lipa50a, lipa50b, lipa60a, lipa60b, lipa70a, lipa70b,
		lipa80a, lipa80b, lipa90a, lipa90b, nug12, nug14, nug15, nug16a,
		nug16b, nug17, nug18, nug20, nug21, nug22, nug24, nug25, nug27, nug28,
		nug30, tai100a, tai100b, tai150b, tai256c, tai60a, tai60b, tai64c, tai80a,
		tai80b, tho150,	wil100
	}

	/** The data file. */
	private static File dataFile = null;
	
	/** The size. */
	private static int size = 0;
	
	/** The distances. */
	private static int [][] distances = null;
	
	/** The mat flow. */
	private static long [][] matFlow = null;
	
	
	/**
	 * Read data from a data file.
	 *
	 * @param fName the data file name
	 * @throws FileNotFoundException the file not found exception
	 */
	public static void readData(DataFile fName) throws FileNotFoundException{
		Scanner sc = null;
		dataFile = new File(DATA_LOCATION + fName + DATA_EXTENSION);
		
		
			sc = new Scanner(dataFile);
			
			size = sc.nextInt();
			
			distances = new int [size][size];
			matFlow = new long [size][size];
			
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					if (sc.hasNextInt()) {
						distances[i][j] = sc.nextInt();
					}
					else {
						System.err.println("Error reading the file "+
					dataFile.getAbsolutePath()+", it hasn't the expected format.");
					}
				}
			}
			
			for (int i = 0; i < matFlow.length; i++) {
				for (int j = 0; j < matFlow[i].length; j++) {
					if (sc.hasNextLong()) {
						matFlow[i][j] = sc.nextLong();
					}
					else {
						System.err.println("Error reading the file "+
					dataFile.getAbsolutePath()+", it hasn't the expected format.");
					}
				}
			}


			sc.close();
		
		
	}


	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public static int getSize() {
		return size;
	}


	/**
	 * Gets the distance matrix.
	 *
	 * @return the distances
	 */
	public static int[][] getDistances() {
		return distances;
	}


	/**
	 * Gets the material flow matrix.
	 *
	 * @return the mat flow
	 */
	public static long[][] getMatFlow() {
		return matFlow;
	}
	
	/**
	 * Gets the data file.
	 *
	 * @return the data file
	 */
	public static File getDataFile() {
		return dataFile;
	}


	/**
	 * Prints the loaded file.
	 */
	public static void printLoadedFile(){
		System.out.println("Problem size: "+ size);
		
		System.out.println("\nDistance matrix: ");
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances[i].length; j++) {
				System.out.printf("%7d",distances[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("\nMaterial flow matrix: ");
		for (int i = 0; i < matFlow.length; i++) {
			for (int j = 0; j < matFlow[i].length; j++) {
				System.out.printf("%7d",matFlow[i][j]);
			}
			System.out.println();
		}
	}
}
