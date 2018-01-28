package BattleObserverNTournamentPackage;

import java.io.File;

import ControlApplication.DnaOperations;
import ControlApplication.FileTools;
import ControlApplication.GraphDataSink;
import ControlApplication.MainControl;
import ControlApplication.RobotFiles;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;

public class BattleObserverNTournament extends BattleAdaptor {

	static int dnaLength = 10;
	static int nTournamentSizeValue = 3;
	static double mutationPercentage = 0.05;
	static int populationSize;
	private GraphDataSink graphData = new GraphDataSink();
	
	public BattleObserverNTournament(int dnaLength, int nTournamentSize, double mutationPercentage, int populationSize) {
		BattleObserverNTournament.dnaLength = dnaLength;
		BattleObserverNTournament.nTournamentSizeValue = nTournamentSize;
		BattleObserverNTournament.mutationPercentage = mutationPercentage;
		BattleObserverNTournament.populationSize = populationSize;
	}

	@Override
	public void onRoundStarted(RoundStartedEvent e) {

		// erzeuge die DNA aus dem DNA Pool für den Roboter dieser Runde
		File dnatxt = new File(RobotFiles.pathDNA);

		//// jede Line im DNAPool repräsentiert eine DNA Sequenz, also ein mögliches
		//// Elternteil
		File dnaPooltxt = new File(RobotFiles.pathDnaPool);

		int[] dnaarray1 = null;
		int[] dnaarray2 = null;
		dnaarray1 = DnaOperationsNTournament.getDnaSequencePerNTournament(dnaPooltxt, nTournamentSizeValue);
		dnaarray2 = DnaOperationsNTournament.getDnaSequencePerNTournament(dnaPooltxt, nTournamentSizeValue);

		// wähle die Trennpunkt / Mitte aus
		int mid = (int) (Math.random() * dnaLength);

		// vermische die beiden DNA-Stränge
		int[] newDNA = DnaOperations.mixDNASequences(dnaarray1, dnaarray2, mid);

		// Mutation
		newDNA = DnaOperations.mutateDNASequences(newDNA, mutationPercentage);

		// schreibe die ermittelte DNA für diese Runde in das DNA-File, damit es vom
		// Robot eingelesen wird
		FileTools.writeStringInFile(dnatxt, FileTools.DnaArrayToString(newDNA), false);

	}

	@Override
	public void onRoundEnded(RoundEndedEvent e) {

		// berechne FitnessWert
		int turns = e.getTurns();
		System.out.println(
				"[" + e.getRound() + "]\tGeneration: " + (int) Math.floor(e.getRound() / MainControl.populationSize)
						+ " Robot: " + e.getRound() % MainControl.populationSize + " Turns: " + turns);
		graphData.logRound(e.getRound(), (int) Math.floor(e.getRound() / MainControl.populationSize), e.getRound() % MainControl.populationSize, turns);
		// speichere die aktuelle DNA mit ihrem Fitness Wert in einer Sammlung aller
		// agbearbeiteten DNAs dieser Generation
		File dnatxt = new File(RobotFiles.pathDNA);
		String dna = FileTools.readLineInDNA(dnatxt);

		String fitnessplusdna = Integer.toString(turns) + " " + dna;

		File collectionOfDNA = new File(RobotFiles.pathDnaCollection);
		FileTools.writeStringInFile(collectionOfDNA, fitnessplusdna, true);

		// falls die gesamte Population durchgespielt wurde, berechne einen neuen
		// DNAPool (ohne die Fitness-Werte, aber ihr Auftreten gewichtet mit diesen)

		if (e.getRound() % BattleObserverNTournament.populationSize == (BattleObserverNTournament.populationSize-1)) {
			DnaOperationsNTournament.createNewDNAPoolNTournament();
		}

		// lösche aktuelles DNA-File
		// bewirkt nix, wirft aber auch keine Exception
		dnatxt.delete();

	}

	// Called when the battle is completed successfully with battle results
	public void onBattleCompleted(BattleCompletedEvent e) {
		System.out.println("-- Battle has completed --");

		/*
		 * BattleResults[] results = e.getIndexedResults();
		 * 
		 * // wir können jetzt hier mit RobotResults arbeiten (zur Erzeugung eine //
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
		// System.out.println("Msg> " + e.getMessage());
	}

	// Called when the game sends out an error message during the battle
	public void onBattleError(BattleErrorEvent e) {
		System.out.println("Err> " + e.getError());
	}
}
