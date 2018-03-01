package senscript;

import java.util.Arrays;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_PRINTFILE extends Command {

protected String [] arg ;
	
	public Command_PRINTFILE(SensorNode sensor, String [] arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " PRINTFILE "+Arrays.toString(arg));
		String message = "";
		String part = "";
		for (int i=1; i<arg.length; i++) {
			part = sensor.getScript().getVariableValue(arg[i]); 
			message += part + " ";
		}
		sensor.getScript().printFile(message);
		return 0 ;
	}
	
	@Override
	public String toString() {
		return "PRINTFILE";
	}
}
