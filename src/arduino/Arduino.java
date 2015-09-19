package arduino;

import java.util.LinkedList;

import script.Command;
import device.DeviceList;
import device.SensorNode;

public class Arduino {

	public static void generateCode() {
		for(SensorNode sensor : DeviceList.getSensorNodes()) {
			
			System.out.println("---------------------------------");
			System.out.println("Sensor: S"+sensor.getId());
			System.out.println("---------------------------------");
			
			LinkedList<Command> cList = sensor.getScript().getCommands();
			
			Bracket.n = 0;
			
			String r = "" ;

			r += "\n";
			
			r += "\tdelay(1000);" + "\n";
			r += "\tSerial.begin(9600);" + "\n";
			r += "\tdelay(1000);" + "\n";
			r += "\n";
			for(Command command : cList) {				
				r += command.getArduinoForm() + "\n";				
			}
			
			r = "void setup() {" + "\n" + BeginInstructions.get() + r ;
			
			r += "\n";
			
			r += "}" + "\n";
			//r += "\n";
			//r += "---------------------------------" + "\n";
			//r += "\n";
			
			for(int i=0; i<Bracket.n; i++) {
				r += "}\n";
			}
			
			System.out.println(r);
		}
	}
	
}
