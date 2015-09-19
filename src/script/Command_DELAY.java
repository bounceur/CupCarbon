package script;

import device.SensorNode;
import device.DataInfo;
import wisen_simulation.SimLog;

public class Command_DELAY extends Command {
	
	protected String arg = "";
	
	public Command_DELAY(SensorNode sensor, String arg) {
		this.sensor = sensor ;		
		this.arg = ""+ ((int) (Integer.parseInt(sensor.getScript().getVariableValue(arg)) * DataInfo.ChDataRate / 1000.)) ;
	}

	@Override
	public int execute() {		
		SimLog.add("S" + sensor.getId() + " starts delaying for " + (Integer.valueOf(arg)/DataInfo.ChDataRate*1000.) + " milliseconds");
		return Integer.parseInt(sensor.getScript().getVariableValue(arg));
	}
	
	public boolean isDelay() {
		return true;
	}
	
	@Override
	public String getArduinoForm() { 
		return "\tdelay("+(Integer.valueOf(arg)/(DataInfo.ChDataRate/1000.))+");";
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
