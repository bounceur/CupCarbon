package arduino;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import device.DeviceList;
import device.SensorNode;
import project.Project;
import script.Command;

public class Arduino {

	public static String r = "" ;
	public static String [][] xbeeList = {
			{"1","VERT","A","0X13A200","0X40B5EF9E"},			
			{"2","JAUNE","B","0X13A200","0X40B58284"},
			{"3","ROUGE","C","0X13A200","0X40B5EFA6"},
			{"4","BLEU","D","0X13A200","0X40BEBA04"},
			{"5","NEUTRE","FFFF","0X13A200","406E5E6F"}
		};
	
	public static void generateCode() {
		
		for(SensorNode sensor : DeviceList.getSensorNodes()) {
			
			System.out.println("---------------------------------");
			System.out.println("Sensor: S"+sensor.getId());
			System.out.println("---------------------------------");
			
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
				File arduinoDir = new File(Project.projectPath+"/arduino/"+fname);
				arduinoDir.mkdirs();
				FileOutputStream f = new FileOutputStream(Project.projectPath+"/arduino/"+fname+"/"+fname+".ino");
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
