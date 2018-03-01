package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_SCRIPT extends Command {
	
	protected String arg = "";
	
	public Command_SCRIPT(SensorNode sensor, String arg) {
		this.sensor = sensor ;		
		this.arg = arg;
	}

	@Override
	public double execute() {
		String file = sensor.getScript().getVariableValue(arg);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " RSCRIPT "+file);
		
		sensor.loadScript2(file, false);
		sensor.getScript().executeCommand();
		return 0 ;
	}
	
	public boolean isDelay() {
		return true;
	}
	
	@Override
	public String toString() {
		return "SCRIPT";
	}
	
}
