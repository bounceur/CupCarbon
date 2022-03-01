package senscript;

import arduino.Bracket;
import cupcarbon.CupCarbon;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_RECEIVE extends Command {
	
	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_RECEIVE(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	public Command_RECEIVE(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1;
	}
	
	public Command_RECEIVE(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	@Override
	public double execute() {		
		double event = 0 ;

		if (sensor.dataAvailable()) {			
			sensor.getScript().setWaiting(false);
			String rep = sensor.readMessage(arg1);
			WisenSimulation.simLog.add("S" + sensor.getId() + " Buffer available, exit waiting.");
			WisenSimulation.simLog.add("S" + sensor.getId() + " READ : "+arg1 + " = "+rep);
			return 0 ;
		} 
		else {
			WisenSimulation.simLog.add("S" + sensor.getId() + " is waiting for data ...");			
			sensor.getScript().setWaiting(true);			
			
			if (arg2.equals(""))
				event = Double.MAX_VALUE;
			else {
				if(sensor.getScript().getVariableValue(arg2)==null) {
					System.err.println("[CupCarbon ERROR] (File: "+ sensor.getScriptFileName()+") (S"+sensor.getId()+"): WAIT function ("+arg2+" is null)");
					CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
				}
				sensor.readMessage(arg1);
				WisenSimulation.simLog.add("S" + sensor.getId() + " Buffer empty, exit waiting.");
				WisenSimulation.simLog.add("S" + sensor.getId() + " READ : "+arg1 + " = \"\"");
				event = (Double.parseDouble(sensor.getScript().getVariableValue(arg2))/1000.);
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
		s += "\t" + "xbee.readPacket("+arg1+");\n";
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
