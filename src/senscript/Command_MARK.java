package senscript;

import arduino.BeginInstructions;
import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_MARK extends Command {

	protected String arg = "" ;
	
	public Command_MARK(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		String args = sensor.getScript().getVariableValue(arg);
		int n = 1-Integer.valueOf('1'-args.charAt(0));
		if(n==0) {
			MarkTmp.v--;
			sensor.setMarked(false);
			SimLog.add("S" + sensor.getId() + " UNMARK");
		}
		else {
			MarkTmp.v++;
			sensor.setMarked(true);
			SimLog.add("S" + sensor.getId() + " MARK");
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
