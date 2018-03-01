package senscript;

import arduino.Bracket;
import cupcarbon.CupCarbon;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_WAIT extends Command {
	
	protected String arg = "";
	
	public Command_WAIT(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	public Command_WAIT(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg;
	}

	@Override
	public double execute() {
		double event = 0 ;

		if (sensor.dataAvailable()) {			
			WisenSimulation.simLog.add("S" + sensor.getId() + " Buffer available, exit waiting.");
			sensor.getScript().setWaiting(false);
			return 0 ;
		} 
		else {
			WisenSimulation.simLog.add("S" + sensor.getId() + " is waiting for data ...");			
			sensor.getScript().setWaiting(true);			
			if (arg.equals(""))
				event = Double.MAX_VALUE;
			else {
				if(sensor.getScript().getVariableValue(arg)==null) {
					System.err.println("[CupCarbon ERROR] (File: "+ sensor.getScriptFileName()+") (S"+sensor.getId()+"): WAIT function ("+arg+" is null)");
					CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
				}
				event = (Double.parseDouble(sensor.getScript().getVariableValue(arg))/1000.);
			}
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
