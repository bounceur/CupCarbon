package script;

import wisen_simulation.SimLog;
import arduino.Bracket;
import device.DataInfo;
import device.SensorNode;

public class Command_WAIT extends Command {
	
	protected String arg = "";
	
	public Command_WAIT(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	public Command_WAIT(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = ""+ ((Long.parseLong(sensor.getScript().getVariableValue(arg)) * DataInfo.ChDataRate / 1000.)) ;
	}

	@Override
	public long execute() {		
		long event = 0 ;

		if (sensor.dataAvailable()) {			
			SimLog.add("S" + sensor.getId() + " Buffer available, exit waiting.");
			sensor.getScript().setWiting(false);
			event = 0; //sensor.getDataSize()*8;
		} 
		else {
			SimLog.add("S" + sensor.getId() + " is waiting for data ...");
			
			sensor.getScript().setWiting(true);
			
			if (arg.equals(""))
				event = Long.MAX_VALUE;
			else
				event = Long.parseLong(sensor.getScript().getVariableValue(arg));
		}
		
		return event;
	}

	@Override
	public boolean isWait() {
		return true;
	}
	
	@Override
	public String getArduinoForm() {
		Bracket.n++;
		return "\twhile(Serial.available()>0) {";
	}

	@Override
	public String toString() {
		return "WAIT";
	}

	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " has finished waiting.");
	}
	
}
