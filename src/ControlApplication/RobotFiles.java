package ControlApplication;

import java.io.*;

public class RobotFiles{
	
	public static String pathDNA = "C:\\robocode\\robots\\TestRobot1\\DNA.txt";
	public static String pathDnaPool = "C:\\robocode\\robots\\TestRobot1\\DNAPool.txt";
	public static String pathDnaCollection = "C:\\robocode\\robots\\TestRobot1\\collectionOfDNA.txt";

	public static void replace() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("robotDirectory.txt"));

			File source = new File(reader.readLine()); //Robot source as defined in user-specific tet file
			reader.close();
			File dest = new File("C:\\robocode\\robots\\TestRobot1");
			
			//delete outdated robot files
			for (File file : dest.listFiles()) file.delete();
			
			//copy new robot files (*.class)
			copyFolder(source, dest);
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
