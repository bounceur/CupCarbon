package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_PICK extends Command {

	protected String arg = ""; 
	
	public Command_PICK(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " PICK");
		sensor.pickMessage(arg);
		return 0 ;
	}

	@Override
	public String toString() {
		return "PICK";
	}
	
}
