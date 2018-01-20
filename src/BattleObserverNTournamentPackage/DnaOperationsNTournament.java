package BattleObserverNTournamentPackage;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ControlApplication.FileTools;
import ControlApplication.RobotFiles;

public class DnaOperationsNTournament {

	/**
	 * w�hlt aus dem DNA Pool eine Menge n von m�glichen DNA Sequenzen mit dem
	 * FitnessWert an erster Stelle als Elternteil aus und gibt das mit dem h�chsten
	 * Fitness-Wert zur�ck
	 * 
	 * @param dnaPooltxt
	 *            File-Objekt zum Textdokument des DNA Pools
	 * @param n
	 *            Anzahl der DNA Sequenzen aus denen die beste Sequenz ermittelt
	 *            wird
	 * @return
	 */
	public static int[] getDnaSequencePerNTournament(File dnaPooltxt, int n) {
		// ermittle Anzahl der m�glichen Eltern
		int anz = FileTools.numLinesInTextfile(dnaPooltxt);
		if (anz != 10)
			throw new RuntimeException("f�r n Tournament stehen mehr als 10 Elternsequenzen zur Verf�gung");
	
		int[][] possibleParentDNASequences = new int[n][];
		int maxFitnessValue = 0;
		for (int i = 0; i < n; i++) {
			int temp = (int) (Math.random() * anz);
			possibleParentDNASequences[i] = FileTools.StringToDnaArray(FileTools.readLineInDnaPool(dnaPooltxt, temp));
			if (possibleParentDNASequences[i][0] > maxFitnessValue)
				maxFitnessValue = possibleParentDNASequences[i][0];
		}
	
		int[] backpass = new int[BattleObserverNTournament.dnaLength];
		for (int[] dna : possibleParentDNASequences) {
			if (dna[0] == maxFitnessValue) {
				System.arraycopy(dna, 1, backpass, 0, dna.length - 1);
				return backpass;
			}
		}
	
		throw new RuntimeException("Fehler in der DNA Ermittlung im n Tournament Verfahren");
	
	}

	/**
	 * erzeugt ein DNAPool.txt mit einer initialen Menge von zuf�lligen
	 * DNA-Sequenzen und einem FitnessWert an erster Stelle
	 */
	public static void createDnaPoolAtBeginningNTournament() {
		List<int[]> list = new LinkedList<int[]>();
		for (int k = 0; k < 10; k++) {
			int[] initalDNA = new int[BattleObserverNTournament.dnaLength + 1];
			initalDNA[0] = 0;
			for (int i = 1; i < initalDNA.length; i++) {
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
	public static void createNewDNAPoolNTournament() {
	
		File collectionOfDNA = new File(RobotFiles.pathDnaCollection);
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);
		//clear
		if(dnaPooltxt.exists())dnaPooltxt.delete();
		FileTools.copyFile(collectionOfDNA, dnaPooltxt);
		//bei nTournament muss der DNA Pool nicht mit gewichteten DNA Sequenzen oder �hnlichem bef�llt werden.
		//Die genetische Logik passiert an anderer Stelle (Auswahl aus dem Pool)
	
		// r�ume die alten DNAs auf
		collectionOfDNA.delete();
	
	}

}
