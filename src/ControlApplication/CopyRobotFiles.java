package ControlApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CopyRobotFiles {

	
	public static void copyRobotFiles() {
		try {
			// kopiere Robot-File(s) ins Robocode-Verzeichnis
			// erzeuge Ordner
			new File("C:\\robocode\\robots\\TestRobot1").mkdirs();
			
			// kopiere .class Datei
			File source1 = new File(
					"C:\\Users\\Nikolai\\Documents\\DHBW\\5.Semester\\Robotik\\Git Repo\\Robots\\bin\\TestRobot1\\TestRobot1Main.class");
			File dest1 = new File("C:\\robocode\\robots\\TestRobot1\\TestRobot1Main.class");
			Files.copy(source1.toPath(), dest1.toPath(), StandardCopyOption.REPLACE_EXISTING);

			// kopiere.java-Datei
			File source2 = new File(
					"C:\\Users\\Nikolai\\Documents\\DHBW\\5.Semester\\Robotik\\Git Repo\\Robots\\src\\TestRobot1\\TestRobot1Main.java");
			File dest2 = new File("C:\\robocode\\robots\\TestRobot1\\TestRobot1Main.java");
			Files.copy(source2.toPath(), dest2.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			System.out.println("Problem beim Kopieren der Robot-Dateien: ");
			e.printStackTrace();
			System.exit(0);
		}
	}
}
