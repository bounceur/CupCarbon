package cupcarbon_script;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import map.MapLayer;
import map.WorldMap;

public class SCupCarbon {
	
	public static PrintStream slog ;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to CupCarbon simulator U-1");
		CupScript script = new CupScript();			
		
		new WorldMap();
		CupScript.slog.println("Init Wordls Map");
		
		MapLayer.initLists();
		CupScript.slog.println("Init Object lists");
		
		Scanner scan = new Scanner(System.in);
		String instruction = "";
		System.out.print("CupCarbon >> ");
		while(!(instruction=scan.nextLine()).equals("exit")) {
			CupScriptAddCommand.addCommand(script, instruction);
			script.nextAndExecute();
			System.out.print("CupCarbon >> ");
		}
		System.out.println("Bye, thank you for using CupCarbon.");
		CupScript.close();
		scan.close();
	}

}
