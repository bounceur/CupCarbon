package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_DELAY extends Command {
	
	protected String arg = "";
	
	public Command_DELAY(SensorNode sensor, String arg) {
		this.sensor = sensor ;		
		this.arg = arg;
	}

	@Override
	public double execute() {				
		double carg = Double.parseDouble(sensor.getScript().getVariableValue(arg));
		WisenSimulation.simLog.add("S" + sensor.getId() + " starts delaying for " + carg + " milliseconds");
		return (carg/1000.) * sensor.getDriftTime() ;
	}
	
	public boolean isDelay() {
		return true;
	}
	
	@Override
	public String getArduinoForm() { 
		return "\tdelay("+Integer.valueOf(arg)+");";
	}
	
	@Override
	public String toString() {
		return "DELAY";
	}
	
	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " has finished the delay.");
	}
}
