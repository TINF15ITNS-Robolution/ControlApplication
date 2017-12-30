package ControlApplication;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;


public class DnaOperations {

	public static double mutationPercentage = 0.05;
	
	
	/**
	 * vermischt die beiden übergebenden DNA-Sequenzen. Die neue zurückgegebende DNA erhält die DNA von dnaarray1 bis zum Punkt mid und ab dort die DNA von dnaarray2
	 * @param dnaarray1
	 * @param dnaarray2
	 * @param mid
	 * @return
	 */
	public static int[] mixDNASequences(int[] dnaarray1, int[] dnaarray2, int mid) {
		int[] newDNA = new int[dnaarray1.length];
		for (int i = 0; i < newDNA.length; i++) {
			if (i < mid) {
				newDNA[i] = dnaarray1[i];
			} else {
				newDNA[i] = dnaarray2[i];
			}
		}
		
		return newDNA;
	}
	
	/**
	 * mutiert die DNA-Sequenz mit zufälligen Werten in Abhängigkeit der DnaOperations.mutationPercentage
	 * @param dna
	 * @return
	 */
	public static int[] mutateDNASequences(int[] dna) {
		for (int i = 0; i < dna.length; i++) {
			if (Math.random() < mutationPercentage) {
				dna[i] = (int) (Math.random() * 2);
			}
		}
		
		return dna;
	}
	
	/**
	 * ermittelt den höchsten Wert in dem übergebenden Array
	 * @param fitness
	 * @return
	 */
	public static double getHeighestFitnessValue(double[] fitness) {
		double highestFitnessValue = 0;
		for (int i = 0; i < fitness.length; i++) {
			if (fitness[i] > highestFitnessValue)
				highestFitnessValue = fitness[i];
		}
		return highestFitnessValue;
	}
	
	/**
	 * normalisiert die übergebenden Fitness-Werte in den bereich 0 ... 1 mit der Formel (fitness[i]/maxFitness)
	 * @param fitness Array der Fitness-Werte dieser Generation
	 * @return liefert die Referenz auf das übergebende Array wieder zurück
	 */
	public static double[] normalizeFitnessValues(double[] fitness) {
		double highestFitnessValue = getHeighestFitnessValue(fitness);
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = fitness[i] / highestFitnessValue;
		}
		return fitness;
	}
	
	/**
	 * Abhängig vom Fitness-Wert der jeweiligen DNA, wird diese mehr oder weniger oft dem zukünftigen DNA-Pool hinzugefügt
	 * @param fitness Array der Fitness-Werte
	 * @param dnaCollection Array der DNA-Sequenzen
	 * @return Liefert eine Liste von DNA-Sequenzen gewichtet nach ihrem Fitness-Wert zurück
	 */
	public static List<int[]> createListOfWeightedDnaSequences(double[] fitness, int[][] dnaCollection){
		List<int[]> list = new LinkedList<int[]>();
		for (int i = 0; i < fitness.length; i++) {
			//je kleiner der Wert, umso häufiger soll diese DNA im Pool wieder auftauchen
			double normalizedFitness = (1d-fitness[i]) * 10;
			if (normalizedFitness < 1)
				normalizedFitness = 1;
			// abhängig von der Fitness wird die Rakete dem Pool x mal zugefügt
			for (int j = 0; j < normalizedFitness; j++) {
				list.add(dnaCollection[i]);
			}
		}
		
		return list;
	}


	/**
	 * erzeugt ein DNAPool.txt mit einer initialen Menge von zufaääligen DNA-Sequenzen
	 */
	public static void createDnaPoolAtBeginning() {
		List<int[]> list = new LinkedList<int[]>();
		for(int k = 0; k<10; k++) {
			int[] initalDNA = new int[BattleObserver.dnaLength];
			for (int i = 0; i < initalDNA.length; i++) {
				initalDNA[i] = ((int) (Math.random() * 2));
			}
			list.add(initalDNA);
		}
		
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);
		FileTools.writeStringInFile(dnaPooltxt, list);
	}
	
	
	/**
	 * erzeugt auch der Sammlung der DNA-Sequenzen dieser Generation einen neuen DNA-Pool
	 */
	public static void createNewDNAPool() {
		// neuer DNAPool
		File collectionOfDNA = new File(RobotFiles.pathDnaCollection);

		double[] fitness = new double[10];
		int[][] dnaCollection = new int[10][];
		
		// lese alle DNAs der Sammlung ein in dnaCollection und alle FitnessWerte int
		// fitness[]
	
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(collectionOfDNA))){
			String fitnessplusdna = null;
			int num = 0;
			int[] dna = null;
			while ((fitnessplusdna = bufferedReader.readLine()) != null) {
				String[] parts = fitnessplusdna.split(" ");
				fitness[num] = Integer.parseInt(parts[0]);
				dna = new int[parts.length - 1];
				for (int i = 1; i < parts.length; i++) {
					dna[i - 1] = Integer.parseInt(parts[i]);
				}
				dnaCollection[num] = dna;
				num++;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException();
		}

		// normalisiere die Fitness-Werte auf Werte zw 0 und 1
		fitness = DnaOperations.normalizeFitnessValues(fitness);

		// erzeuge eine Liste aus gewichteten DNA-Sequenzen
		List<int[]> list = DnaOperations.createListOfWeightedDnaSequences(fitness, dnaCollection);

		// schreibe die Liste in das File DNAPool, aus welchem bei RoundStarted die
		// aktuelle DNA erzeugt wird
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);
		FileTools.writeStringInFile(dnaPooltxt, list);
	
		

		// räume die alten DNAs auf
		collectionOfDNA.delete();

	}
}
