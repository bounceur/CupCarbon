package arduino;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import device.DeviceList;
import device.SensorNode;
import project.Project;
import senscript.Command;

public class Arduino {

	public static String r = "" ;
	
	//"1:VERT:A:0X13A200:0X40B5EF9E"
	//"2:JAUNE:B:0X13A200:0X40B58284"
	//"3:ROUGE:C:0X13A200:0X40B5EFA6"
	//"4:BLEU:D:0X13A200:0X40BEBA04"
	//"5:NEUTRE:FFFF:0X13A200:406E5E6F"
	public static ArrayList<String []> xbeeList ;
	public static Set<String> variables ;
	
	public static void generateCode() {
		
		try {
			System.out.println("XBEE File OK");
			System.out.println(Project.projectPath + File.separator + "xbee" + File.separator + "xbee.txt");
			FileReader xbeeFile = new FileReader(Project.projectPath + File.separator + "xbee" + File.separator + "xbee.txt");
			BufferedReader br = new BufferedReader(xbeeFile);
			
			xbeeList = new ArrayList<String []>();
			String [] row = new String [5];
			String s ;
			try {
				while((s=br.readLine()) != null) {
					row = s.split(":");
					xbeeList.add(row);
				}
				br.close();
				xbeeFile.close();
				for(String [] st : xbeeList) {				
					System.out.println(Arrays.toString(st));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
			
		} catch (FileNotFoundException e1) {
			System.out.println("No XBEE File");
			xbeeList = new ArrayList<String []>();
			String [] row = new String [5];
			row[0] = "1"; row[1] = "GREEN";   row[2] = "A"; row[3] = "0X13A200"; row[4] = "0X40B5EF9E";
			xbeeList.add(row);
			row = new String [5];
			row[0] = "2"; row[1] = "YELLOW";  row[2] = "B"; row[3] = "0X13A200"; row[4] = "0X40B58284";
			xbeeList.add(row);
			row = new String [5];
			row[0] = "3"; row[1] = "RED";  row[2] = "C"; row[3] = "0X13A200"; row[4] = "0X40B5EFA6";
			xbeeList.add(row);
			row = new String [5];
			row[0] = "4"; row[1] = "BLUE";   row[2] = "D"; row[3] = "0X13A200"; row[4] = "0X40BEBA04";
			xbeeList.add(row);
			row = new String [5];
			row[0] = "5"; row[1] = "WHITE"; row[2] = "E"; row[3] = "0X13A200"; row[4] = "0x406E5E6F";
			xbeeList.add(row);
			
			for(String [] st : xbeeList) {				
				System.out.println(Arrays.toString(st));
			}
		}
		
		variables = new HashSet<String>();
		
		File arduinoDir = new File(Project.projectPath + File.separator + "arduino" + File.separator);
		
		for(SensorNode sensor : DeviceList.sensors) {
			
			System.out.println("---------------------------------");
			System.out.println("Sensor: S"+sensor.getId());
			System.out.println("---------------------------------");
			
			sensor.loadScript();			
			LinkedList<Command> cList = sensor.getScript().getCommands();
			
			Bracket.n = 0;
			
			r = "" ;

			r += "\n";
						
			r += "\tdelay(100);" + "\n";
			r += "\tSerial.begin(38400);" + "\n";
			r += "\txbee.setSerial(Serial);\n";
			r += "\tdelay(100);" + "\n";
			r += "\tlcd.begin(16, 2);\n";
			r += "\tlcd.setCursor(0,0);\n";
			r += "\n";
			for(Command command : cList) {				
				r += command.getArduinoForm() + "\n";				
			}
			
			r = "void setup() {" + "\n" + BeginInstructions.get() + r ;			
			r = "\nLiquidCrystal lcd(8, 9, 4, 5, 6, 7);\n\n" + r;
			
			r = "uint8_t* rdata;\n" + r ;			
			r = "uint8_t sdata[30] ;\n" + r ;
			r = "XBeeAddress64 addr;\n" + r ;
			r = "Tx64Request tx;\n" + r ;
			r = "Rx64Response rx = Rx64Response() ;\n" + r ;
			r = "XBee xbee = XBee();\n" + r ;
			r = "#include <LiquidCrystal.h>\n\n" + r ;
			r = "#include <XBee.h>\n" + r ;
			
			
			r += "\n";
						
			for(int i=0; i<Bracket.n; i++) {
				r += "}\n";
			}
			
			r += "}" + "\n";
						
			try {				
				String fname = "arduino_"+sensor.getId();
				arduinoDir = new File(Project.projectPath+"/arduino/"+fname);
				arduinoDir.mkdirs();
				FileOutputStream f = new FileOutputStream(Project.projectPath + File.separator + "arduino" + File.separator + fname + File.separator + fname + ".ino");
				PrintStream p = new PrintStream(f);
				p.print(r);
				p.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Code Generation : Finish!");
			//System.out.println(r);
		}
	}
	
}
