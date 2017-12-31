package ControlApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RobotFiles{
	
	public static String pathDNA = "C:\\robocode\\robots\\TestRobot1\\DNA.txt";
	//public static String pathDnaPool = "C:\\Users\\Nikolai\\Documents\\DHBW\\5.Semester\\Robotik\\Git Repo\\Robots\\src\\TestRobot1\\DNAPool.txt";
	public static String pathDnaPool = "C:\\robocode\\robots\\TestRobot1\\DNAPool.txt";
	public static String pathDnaCollection = "C:\\robocode\\robots\\TestRobot1\\collectionOfDNA.txt";

	public static void copyRobotFiles() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("robotDirectory.txt"));
			
			// kopiere Robot-File(s) ins Robocode-Verzeichnis
			File source = new File(reader.readLine());
			reader.close();
			File dest = new File("C:\\robocode\\robots\\TestRobot1");
			
			copyFolder(source, dest);
			/*
			 * // erzeuge Ordner
			 * new File("C:\\robocode\\robots\\TestRobot1").mkdirs();
			 * 
			 * // kopiere .class Datei File source1 = new File(
			 * "C:\\Users\\Nikolai\\Documents\\DHBW\\5.Semester\\Robotik\\Git Repo\\Robots\\bin\\TestRobot1\\TestRobot1Main.class"
			 * ); File dest1 = new
			 * File("C:\\robocode\\robots\\TestRobot1\\TestRobot1Main.class");
			 * Files.copy(source1.toPath(), dest1.toPath(),
			 * StandardCopyOption.REPLACE_EXISTING);
			 * 
			 * // kopiere.java-Datei File source2 = new File(
			 * "C:\\Users\\Nikolai\\Documents\\DHBW\\5.Semester\\Robotik\\Git Repo\\Robots\\src\\TestRobot1\\TestRobot1Main.java"
			 * ); File dest2 = new
			 * File("C:\\robocode\\robots\\TestRobot1\\TestRobot1Main.java");
			 * Files.copy(source2.toPath(), dest2.toPath(),
			 * StandardCopyOption.REPLACE_EXISTING);
			 */

		} catch (IOException e) {
			System.out.println("Problem beim Kopieren der Robot-Dateien: ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void copyFolder(File source, File destination) throws IOException {
		if (source.isDirectory()) {
			if (!destination.exists()) {
				destination.mkdirs();
			}

			String files[] = source.list();

			for (String file : files) {
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);

				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = null;
			OutputStream out = null;

			in = new FileInputStream(source);
			out = new FileOutputStream(destination);

			byte[] buffer = new byte[1024];

			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();

			out.close();

		}

	}
}
