package script;

import java.util.Arrays;

import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_PRINTLN extends Command {

	private String message = "";
	protected String [] arg ;
	
	public Command_PRINTLN(SensorNode sensor, String [] arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " PRINTLN "+Arrays.toString(arg));		
		String part = "";
		message = "";
		//System.out.print("S" + sensor.getId() + " >> ");
		for (int i=1; i<arg.length; i++) {
			part = sensor.getScript().getVariableValue(arg[i]);
			//part.replaceAll("\\", " ");
			//if (!part.equals("\\")) 
			message += part + " ";
		}
		sensor.setMessage(message);
		//System.out.println(message);
		return 0 ;
	}
	
	@Override
	public String toString() {
		return "PRINTLN";
	}
	
	@Override
	public String getArduinoForm() {
		String s = "\tlcd.clear();\n";
		//s += "\tlcd.setCursor(0,0);\n";		
		for (int i=1; i<arg.length; i++) {
			s += "\tlcd.print(" + (arg[i].startsWith("$")?arg[i].substring(1):("\""+arg[i])+" \"") + ");\n";
		}		
		return s;
	}
}
