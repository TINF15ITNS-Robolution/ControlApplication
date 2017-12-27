package ControlApplication;

import robocode.BattleResults;
import robocode.control.*;
import robocode.control.events.*;

public class MainControl {
	
	public static String robotNames = "sample.RamFire,sample.Corners";
	public static RobocodeEngine engine;
	
	public static int numGenerations = 1000;
	
	public static void main(String[] args) {
		//erzeuge die Robocode-Umgebung
        RobocodeEngine.setLogMessagesEnabled(false);
        engine = new RobocodeEngine(new java.io.File("C:/Robocode")); // Run from C:/Robocode
        engine.addBattleListener(new BattleObserver());
        engine.setVisible(true);
        
        System.out.println(engine.getLocalRepository().toString());
        System.out.println(RobocodeEngine.getRobotsDir());

        
        //C:\robocode\robots\RobotikProjekt
        
        
       // erzeuge Battlefield-Setups
        int numberOfRounds = 1;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
        RobotSpecification[] selectedRobots = engine.getLocalRepository(robotNames);
        RobotSetup geneticRobotSetup = new RobotSetup((double)(battlefield.getWidth()/2), (double)(battlefield.getHeight()/2), 0d);
        RobotSetup enemyRobotSetup = new RobotSetup(50d, 50d, 0d);

        BattleSpecification battleSpec = null;
        
        for(int i = 0; i < numGenerations; i++) {
        	battleSpec = new BattleSpecification(battlefield, numberOfRounds, 450, 0.1, 100, false, selectedRobots, new RobotSetup[]{geneticRobotSetup, enemyRobotSetup});
            engine.runBattle(battleSpec, true); // waits till the battle finishes

        }
        
        //Default-Werte aus Standard-Battle-Spezifikation ermittelt


        // Run our specified battle and let it run till it is over
        engine.close();
        System.exit(0);
	}
	
	
	
	//
	// Our private battle listener for handling the battle event we are interested in.
	//
	static class BattleObserver extends BattleAdaptor {

	    // Called when the battle is completed successfully with battle results
	    public void onBattleCompleted(BattleCompletedEvent e) {
	        System.out.println("-- Battle has completed --");
	        
	        BattleResults[] results = e.getIndexedResults();
	        
	        //wir können jetzt hier mit RobotResults arbeiten (zur Erzeugung eine RobotResult-Objekt erzeugen und betreffende RobotSpecification und BattleResult-Eintrag mitgegeben
	        //bringt aber eigentlich kein Mehrwert an Informationen. Wenn wir im RobotNames-String den GeneticRobot immer an erster Stelle stellen, stehen dessen Ergebnisse in results[0]
	        RobotSpecification[] selectedRobots = engine.getLocalRepository(robotNames);
	        for(int i = 0; i< selectedRobots.length; i++) {
	        	RobotResults resultGeneticRobot = new RobotResults(selectedRobots[i], results[i]);
	        }
	        
	        
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

}