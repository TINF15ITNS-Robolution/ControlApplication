package ControlApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;

public class BattleObserver extends BattleAdaptor {

	static int dnaLength = 100;

	@Override
	public void onRoundStarted(RoundStartedEvent e) {

		// erzeuge die DNA aus dem DNA Pool f�r den Roboter dieser Runde
		File dnatxt = new File(RobotFiles.pathDNA);

		//// jede Line im DNAPool repr�sentiert eine DNA Sequenz, also ein m�gliches
		//// Elternteil
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);
		////// ermittle Anzahl der m�glichen Eltern
		int anz = FileTools.numLinesInTextfile(dnaPooltxt);

		////// w�hle zwei DNA-Str�nge zuf�llig aus
		int dna1 = (int) (Math.random() * anz);
		int dna2 = (int) (Math.random() * anz);
		////// w�hle die Trennpunkt / Mitte aus
		int mid = (int) (Math.random() * dnaLength);
		//// lese die beiden betreffenden DNA Zeilen ein
		int[] dnaarray1 = new int[dnaLength];
		int[] dnaarray2 = new int[dnaLength];
		dnaarray1 = FileTools.StringToDnaArray(FileTools.readLineInDnaPool(dnaPooltxt, dna1));
		dnaarray2 = FileTools.StringToDnaArray(FileTools.readLineInDnaPool(dnaPooltxt, dna2));

		// vermische die beiden DNA-Str�nge
		int[] newDNA = DnaOperations.mixDNASequences(dnaarray1, dnaarray2, mid);

		// Mutation
		newDNA = DnaOperations.mutateDNASequences(newDNA);

		// schreibe die ermittelte DNA f�r diese Runde in das DNA-File, damit es vom
		// Robot eingelesen wird
		FileTools.writeStringInFile(dnatxt, FileTools.DnaArrayToString(newDNA), false);

	}

	@Override
	public void onRoundEnded(RoundEndedEvent e) {

		// berechne FitnessWert
		int turns = e.getTurns();

		// speichere die aktuelle DNA mit ihrem Fitness Wert in einer Sammlung aller
		// agbearbeiteten DNAs dieser Generation
		File dnatxt = new File(RobotFiles.pathDNA);
		String dna = FileTools.readLineInDNA(dnatxt);

		String fitnessplusdna = Integer.toString(turns) + " " + dna;

		File collectionOfDNA = new File(RobotFiles.pathDnaCollection);
		FileTools.writeStringInFile(collectionOfDNA, fitnessplusdna, true);

		// falls die gesamte Population durchgespielt wurde, berechne einen neuen
		// DNAPool (ohne die Fitness-Werte, aber ihr Auftreten gewichtet mit diesen)


		if (e.getRound() % MainControl.populationSize == 9) {
			DnaOperations.createNewDNAPool();
		}

		// l�sche aktuelles DNA-File
		//bewirkt nix, wirft aber auch keine Exception
		dnatxt.delete();

	}

	// Called when the battle is completed successfully with battle results
	public void onBattleCompleted(BattleCompletedEvent e) {
		System.out.println("-- Battle has completed --");

		/*
		 * BattleResults[] results = e.getIndexedResults();
		 * 
		 * // wir k�nnen jetzt hier mit RobotResults arbeiten (zur Erzeugung eine //
		 * RobotResult-Objekt erzeugen und betreffende RobotSpecification und //
		 * BattleResult-Eintrag mitgegeben // bringt aber eigentlich kein Mehrwert an
		 * Informationen. Wenn wir im // RobotNames-String den GeneticRobot immer an
		 * erster Stelle stellen, stehen // dessen Ergebnisse in results[0]
		 * RobotSpecification[] selectedRobots = engine.getLocalRepository(robotNames);
		 * RobotResults[] robotresults = new RobotResults[selectedRobots.length]; for
		 * (int i = 0; i < selectedRobots.length; i++) { robotresults[i] = new
		 * RobotResults(selectedRobots[i], results[i]); }
		 */

		// Print out the sorted results with the robot names
		System.out.println("Battle results:");
		for (robocode.BattleResults result : e.getSortedResults()) {
			System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
		}

	}

	// Called when the game sends out an information message during the battle
	public void onBattleMessage(BattleMessageEvent e) {
		System.out.println("Msg> " + e.getMessage());
	}

	// Called when the game sends out an error message during the battle
	public void onBattleError(BattleErrorEvent e) {
		System.out.println("Err> " + e.getError());
	}

}
