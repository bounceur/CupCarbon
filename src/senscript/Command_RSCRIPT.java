package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_RSCRIPT extends Command {
	
	protected String arg = "";
	
	public Command_RSCRIPT(SensorNode sensor, String arg) {
		this.sensor = sensor ;		
		this.arg = arg;
	}

	@Override
	public double execute() {
		String file = sensor.getScript().getVariableValue(arg);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " RSCRIPT "+file);
		
		sensor.loadScript2(file, true);
		sensor.getScript().executeCommand();
		return 0 ;
	}
	
	public boolean isDelay() {
		return true;
	}
	
	@Override
	public String toString() {
		return "RSCRIPT";
	}
	
}
