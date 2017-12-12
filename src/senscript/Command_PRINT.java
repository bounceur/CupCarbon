package senscript;

import java.util.Arrays;

import device.SensorNode;
import map.MapLayer;
import simulation.WisenSimulation;

public class Command_PRINT extends Command {

	private String message = "";
	protected String [] arg ;
	
	public Command_PRINT(SensorNode sensor, String [] arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " PRINT "+Arrays.toString(arg));		
		String part = "";
		message = "";
		for (int i=1; i<arg.length; i++) {
			part = sensor.getScript().getVariableValue(arg[i]);
			message += part + " ";
		}
		sensor.setMessage(message);
		MapLayer.repaint();
		return 0 ;
	}
	
	@Override
	public String toString() {
		return "PRINT";
	}
	
	@Override
	public String getArduinoForm() {
		String s = "\tlcd.clear();\n";
		if(arg[1].length()>1 || !arg[1].equals("\\")) 
			for (int i=1; i<arg.length; i++) {			
				s += "\tlcd.print(" + (arg[i].startsWith("$")?arg[i].substring(1):("\""+arg[i])+"\"") + ");\n";
			}		
		return s;
	}
}
