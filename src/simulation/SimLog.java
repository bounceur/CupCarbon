package simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javafx.application.Platform;
import project.Project;

public class SimLog {
	
	private PrintStream logFile ;
	
	public SimLog() {
		try {
			logFile = new PrintStream(new FileOutputStream(Project.getProjectLogPath() + "/log" + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (SimulationInputs.displayLog) {
					logFile.println(s);
				}
			}
		});
	}
	
	public void close() {
		logFile.close();
	}
	
}
