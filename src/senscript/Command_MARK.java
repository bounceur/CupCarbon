package senscript;

import arduino.BeginInstructions;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_MARK extends Command {

	protected String arg = "" ;
	
	public Command_MARK(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		if(sensor.getScript().getVariableValue(arg)==null)
			System.err.println("[CupCarbon ERROR] (File: "+ sensor.getScriptFileName()+") (S"+sensor.getId()+"): MARK function ("+arg+" is null)");
		String args = sensor.getScript().getVariableValue(arg);
		int n = 1-Integer.valueOf('1'-args.charAt(0));
		if(n==0) {
			sensor.setMarked(false);
			WisenSimulation.simLog.add("S" + sensor.getId() + " UNMARK");
		}
		else {
			sensor.setMarked(true);
			WisenSimulation.simLog.add("S" + sensor.getId() + " MARK");
		}
		return 0 ;
	}
	
	@Override
	public String getArduinoForm() {
		BeginInstructions.add("pinMode(13, OUTPUT);");
		String s = "";
		String v = "'"+arg.charAt(0)+"'";
		if(arg.charAt(0)=='$') v = arg.substring(1)+".charAt(0)";
		
		s = "\tdigitalWrite(13, 1-('1'-"+v+"));";
		
		return s;
	}

	@Override
	public String toString() {
		return "MARK";
	}
	
}
