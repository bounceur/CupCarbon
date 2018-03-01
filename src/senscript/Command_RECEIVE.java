package senscript;

import arduino.Bracket;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_RECEIVE extends Command {
	
	protected String arg = "";
	
	public Command_RECEIVE(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	public Command_RECEIVE(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg;
	}

	@Override
	public double execute() {		
		double event = 0 ;

		if (sensor.dataAvailable()) {			
			WisenSimulation.simLog.add("S" + sensor.getId() + " Buffer available, exit waiting.");
			sensor.getScript().setWaiting(false);
			sensor.readMessage(arg);
			return 0 ;
		} 
		else {
			WisenSimulation.simLog.add("S" + sensor.getId() + " is waiting for data ...");			
			sensor.getScript().setWaiting(true);			
			event = Double.MAX_VALUE;
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
		Bracket.n++;
		
		String s = "";
		s += "\t" + "xbee.readPacket("+arg+");\n";
		s += "\tif (xbee.getResponse().isAvailable()) {\n";
		s += "\tif (xbee.getResponse().getApiId() == RX_64_RESPONSE) {\n";
		return s ;
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
