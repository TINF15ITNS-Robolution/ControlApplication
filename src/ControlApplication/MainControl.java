package ControlApplication;

import java.util.Iterator;

import BattleObserverNTournamentPackage.BattleObserverNTournament;
import BattleObserverNTournamentPackage.DnaOperationsNTournament;
import BattleObserverStandardPackage.BattleObserverStandard;
import BattleObserverStandardPackage.DnaOperationsStandard;
import robocode.control.*;
import robocode.control.events.IBattleListener;

public class MainControl {

	// warum das Sternchen hinterm Namen? Keine Ahnung, aber dies wird durch
	// Robocode anscheinend bei custom-Robots hinzugefügt und sonst wird der Robot
	// nicht beim Aufruf gefunden
	public static String robotNames = "TestRobot1.TestRobot1Main*,sample.SittingDuck";
	public static RobocodeEngine engine;

	public static int numGenerations = 500;

	public static void main(String[] args) {
		// erzeuge die Robocode-Umgebung
		RobocodeEngine.setLogMessagesEnabled(false);
		engine = new RobocodeEngine(new java.io.File("C:/Robocode")); // Run from C:/Robocode
		engine.setVisible(true);

		// copy Robot Files
		RobotFiles.replace();

		int[] populationSizeValues = new int[] { 5, 10, 20 };
		double[] mutationPercentageValues = new double[] { 0.03, 0.05, 0.10 };
		int[] dnaLengthValues = new int[] { 7, 10, 20 };
		boolean[] selectionAlgorithmValues = new boolean[] { true, false };
		int[] nTournamentSizeValues = new int[] { 3, 5, 7 };

		for (int populationSize : populationSizeValues) {
			
			int numberOfRounds = numGenerations * populationSize;
			// erzeuge Battlefield-Setups
			//// für jede Generation muss eine eigene Runde gestartet werden - nach jeder
			// Generation wird dann die DNA gemixed
			BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
			RobotSpecification[] selectedRobots = engine.getLocalRepository(robotNames);
			RobotSetup[] setups = new RobotSetup[] {
					new RobotSetup((double) (battlefield.getWidth() / 2), (double) (battlefield.getHeight() / 2), 0d),
					new RobotSetup(50d, 50d, 0d) };
			// Default-Werte aus Standard-Battle-Spezifikation ermittelt
			BattleSpecification battleSpecification = new BattleSpecification(battlefield, numberOfRounds, 450, 0.1, 100, false,
					selectedRobots, setups);
			
			for (double mutationPercentage : mutationPercentageValues) {

				for (int dnaLength : dnaLengthValues) {

					for (int nTournamentSize : nTournamentSizeValues) {

						for (boolean selectionAlgorithm : selectionAlgorithmValues) {
							RobotFiles.deleteOldDNAFiles();
							
							IBattleListener listener;							
							if (selectionAlgorithm) {//Standard
								listener = new BattleObserverStandard(dnaLength, mutationPercentage, populationSize);
								DnaOperationsStandard.createDnaPoolAtBeginningWeightedRouletteWheel();
							} else {//nTournament
								listener = new BattleObserverNTournament(dnaLength, nTournamentSize, mutationPercentage, populationSize);
								DnaOperationsNTournament.createDnaPoolAtBeginningNTournament();
							}

							engine.addBattleListener(listener);
							engine.runBattle(battleSpecification, true);
							engine.removeBattleListener(listener);

						}
					}
				}
			}
		}
		
		engine.close();
		System.exit(0);
	}

}