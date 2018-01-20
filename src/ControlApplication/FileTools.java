package ControlApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.management.RuntimeErrorException;

public class FileTools {

	public static int numLinesInTextfile(File f) {
		int anz = 0;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
			// ermittle Anzahl von Zeilen (Anzahl von DNA Einträgen)
			while ((bufferedReader.readLine()) != null)
				anz++;
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		}

		return anz;
	}

	public static String readLineInDnaPool(File f, int lineNum) {
		int currentLine = 0;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (currentLine == lineNum) {
					return line;
				}
				currentLine++;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		throw new RuntimeException();
	}

	public static String readLineInDNA(File f) {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
			return bufferedReader.readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static void writeStringInFile(File f, String dna, boolean append) {
		try {
			Writer writer = new FileWriter(f, append);

			writer.write(dna);
			writer.write(System.getProperty("line.separator"));
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static void writeStringInFile(File f, List<int[]> list) {
		try {
			Writer writer = new FileWriter(f);
			for (int[] i : list) {
				writer.write(DnaArrayToString(i));
				writer.write(System.getProperty("line.separator"));
			}
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public static String DnaArrayToString(int[] dna) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < dna.length; i++) {
			s.append(dna[i] + " ");
		}
		return s.toString();
	}

	public static int[] StringToDnaArray(String s) {
		String[] teile = s.split(" ");
		int[] array = new int[teile.length];

		for (int i = 0; i < teile.length; i++) {
			array[i] = Integer.parseInt(teile[i]);
		}
		return array;
	}

	public static void copyFile(File src, File dest) {
		try {
			RobotFiles.copyFolder(src, dest);
		} catch (IOException e) {
			System.out.println("Fehler beim Kopieren eines Files");
			e.printStackTrace();
		}
	}
}
