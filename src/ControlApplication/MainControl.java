package ControlApplication;


import robocode.control.*;

public class MainControl {

	// warum das Sternchen hinterm Namen? Keine Ahnung, aber dies wird durch
	// Robocode anscheinend bei custom-Robots hinzugefügt und sonst wird der Robot
	// nicht beim Aufruf gefunden
	public static String robotNames = "TestRobot1.TestRobot1Main*,sample.SittingDuck";
	public static RobocodeEngine engine;

	public static int numGenerations = 1000;
	public static int populationSize = 10;


	public static void main(String[] args) {
		// erzeuge die Robocode-Umgebung
		RobocodeEngine.setLogMessagesEnabled(false);
		engine = new RobocodeEngine(new java.io.File("C:/Robocode")); // Run from C:/Robocode
		engine.addBattleListener(new BattleObserver());
		engine.setVisible(true);
		
		// copy Robot Files
		RobotFiles.replace();
		// create Dna
		DnaOperations.createDnaPoolAtBeginning();

		

		// erzeuge Battlefield-Setups
		//// für jede Generation muss eine eigene Runde gestartet werden - nach jeder
		// generation wird dann die DNA gemixed
		int numberOfRounds = numGenerations * populationSize;
		BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
		RobotSpecification[] selectedRobots = engine.getLocalRepository(robotNames);
		RobotSetup[] setups = new RobotSetup[] {
				new RobotSetup((double) (battlefield.getWidth() / 2), (double) (battlefield.getHeight() / 2), 0d),
				new RobotSetup(50d, 50d, 0d) };

		// Default-Werte aus Standard-Battle-Spezifikation ermittelt
		BattleSpecification battleSpec = new BattleSpecification(battlefield, numberOfRounds, 450, 0.1, 100, false, selectedRobots, setups);
		engine.runBattle(battleSpec, true); // waits till the battle finishes

		engine.close();
		System.exit(0);
	}


}