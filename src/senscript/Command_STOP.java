package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_STOP extends Command {
	
	public Command_STOP(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " STOP !");
		sensor.getScript().setBreaked(true);		
		return Double.MAX_VALUE;
	}
	
	@Override
	public String getArduinoForm() { 
		return "break;";
	}
	
	@Override
	public String toString() {
		return "STOP";
	}
}
