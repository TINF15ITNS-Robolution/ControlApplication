package BattleObserverStandardPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ControlApplication.FileTools;
import ControlApplication.RobotFiles;

public class DnaOperationsStandard {

	/**
	 * liest eine zuf�llige Dna Sequenz aus dem DNA Pool aus und gibt diese zur�ck
	 * 
	 * @param dnaPooltxt
	 *            File-Objekt zum Textdokument des DNA Pools
	 * @return eine DNA Sequenz
	 */
	public static int[] getDnaSequencePerWeightedRouletteWheel(File dnaPooltxt) {
		// ermittle Anzahl der m�glichen Eltern
		int anz = FileTools.numLinesInTextfile(dnaPooltxt);
	
		// w�hle zwei DNA-Str�nge zuf�llig aus
		int dna1 = (int) (Math.random() * anz);
		return FileTools.StringToDnaArray(FileTools.readLineInDnaPool(dnaPooltxt, dna1));
	}

	/**
	 * ermittelt den h�chsten Wert in dem �bergebenden Array
	 * 
	 * @param fitness
	 * @return den h�chsten Wert
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
	 * normalisiert die �bergebenden Fitness-Werte in den bereich 0 ... 1 mit der
	 * Formel (fitness[i]/maxFitness)
	 * 
	 * @param fitness
	 *            Array der Fitness-Werte dieser Generation
	 * @return liefert die Referenz auf das �bergebende Array wieder zur�ck
	 */
	public static double[] normalizeFitnessValues(double[] fitness) {
		double highestFitnessValue = getHeighestFitnessValue(fitness);
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = fitness[i] / highestFitnessValue;
		}
		return fitness;
	}

	/**
	 * Abh�ngig vom Fitness-Wert der jeweiligen DNA, wird diese mehr oder weniger
	 * oft dem zuk�nftigen DNA-Pool hinzugef�gt
	 * 
	 * @param fitness
	 *            Array der Fitness-Werte
	 * @param dnaCollection
	 *            Array der DNA-Sequenzen
	 * @return Liefert eine Liste von DNA-Sequenzen gewichtet nach ihrem
	 *         Fitness-Wert zur�ck
	 */
	public static List<int[]> createListOfWeightedDnaSequences(double[] fitness, int[][] dnaCollection) {
		List<int[]> list = new LinkedList<int[]>();
		for (int i = 0; i < fitness.length; i++) {
			// je kleiner der Wert, umso h�ufiger soll diese DNA im Pool wieder auftauchen
			double normalizedFitness = (1d - fitness[i]) * 10;
			if (normalizedFitness < 1)
				normalizedFitness = 1;
			// abh�ngig von der Fitness wird die Rakete dem Pool x mal zugef�gt
			for (int j = 0; j < normalizedFitness; j++) {
				list.add(dnaCollection[i]);
			}
		}
	
		return list;
	}

	/**
	 * erzeugt ein DNAPool.txt mit einer initialen Menge von zuf�lligen
	 * DNA-Sequenzen
	 */
	public static void createDnaPoolAtBeginningWeightedRouletteWheel() {
		List<int[]> list = new LinkedList<int[]>();
		for (int k = 0; k < 10; k++) {
			int[] initalDNA = new int[BattleObserverStandard.dnaLength];
			for (int i = 0; i < initalDNA.length; i++) {
				initalDNA[i] = ((int) (Math.random() * 2));
			}
			list.add(initalDNA);
		}
	
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);
		//clear
				if(dnaPooltxt.exists())dnaPooltxt.delete();
		FileTools.writeStringInFile(dnaPooltxt, list);
	}

	/**
	 * erzeugt auch der Sammlung der DNA-Sequenzen dieser Generation einen neuen
	 * DNA-Pool
	 */
	public static void createNewDNAPoolWeightedRouletteWheel() {
		// neuer DNAPool
		File collectionOfDNA = new File(RobotFiles.pathDnaCollection);
	
		double[] fitness = new double[10];
		int[][] dnaCollection = new int[10][];
	
		// lese alle DNAs der Sammlung ein in dnaCollection und alle FitnessWerte int
		// fitness[]
	
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(collectionOfDNA))) {
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
		fitness = normalizeFitnessValues(fitness);
	
		// erzeuge eine Liste aus gewichteten DNA-Sequenzen
		List<int[]> list = createListOfWeightedDnaSequences(fitness, dnaCollection);
	
		// schreibe die Liste in das File DNAPool, aus welchem bei RoundStarted die
		// aktuelle DNA erzeugt wird
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);
		//clear
		if(dnaPooltxt.exists())dnaPooltxt.delete();
		FileTools.writeStringInFile(dnaPooltxt, list);
	
		// r�ume die alten DNAs auf
		collectionOfDNA.delete();
	
	}

}
