package ControlApplication;
import java.io.*;
import java.util.*;

public class GraphDataSink {

	File file;
	public GraphDataSink () {
		Date date = new Date();
		System.out.println(date.toString());
		file = new File(System.currentTimeMillis() + ".csv");
		logToFile("round;generation;robot;turns");
	}
	public void logRound(int round, int generation, int robot, int turns) {
		logToFile(round + ";" + generation + ";" + robot + ";" + turns);
	}
	
	private void logToFile(String text) {
		try {
			Writer writer = new FileWriter(file, true);

			writer.write(text);
			writer.write(System.getProperty("line.separator"));
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException();
		}
	}
}
