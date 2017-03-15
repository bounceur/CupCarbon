package cupcarbon_script;

import java.io.BufferedReader;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javafx.application.Application;
import javafx.stage.Stage;
import map.MapLayer;
import map.WorldMap;

public abstract class SCupCarbonConsole extends Application{
	
	public static Stage stage;
	
	public static void main(String[] args) throws IOException {
		CupScript script = new CupScript();
		CupScript.slog.println("Script");
		
		new WorldMap();
		CupScript.slog.println("Init Worls Map");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");
		
		JFileChooser fileChooser = new JFileChooser("Open CupCarbon Script file");
		
		FileFilter ScriptFilter = new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return true;
				else if (pathname.getName().endsWith(".scc"))
					return true;
				else
					return false;
			}

			public String getDescription() {
				return "CupCarbon Script files";
			}
		};
		
		fileChooser.setFileFilter(ScriptFilter);
		
		int val = fileChooser.showDialog(fileChooser, "Load CupCarbon Script Source");
		if (val == 0) {
    	loadAndExecute(script, fileChooser.getSelectedFile().getParentFile().toString(), fileChooser.getSelectedFile().getName().toString());
    }

		System.out.println("BYE");
		CupScript.close();
	}

	public static void loadAndExecute(CupScript script, String path, String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + File.separator + fileName));
			String s = "";
			while ((s = br.readLine()) != null) {	
				CupScriptAddCommand.addCommand(script, s.trim());
				script.nextAndExecute();
				}
			br.close();
		} catch (Exception e) {e.printStackTrace();}
		
	}

}
