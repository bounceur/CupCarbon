package script;

import arduino.BeginInstructions;
import device.SensorNode;
import visualisation.Visualisation;
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
		int n = Integer.valueOf(args);
		if(n==0) {
			sensor.setMarked(false);
			SimLog.add("S" + sensor.getId() + " UNMARK");
		}
		else {
			sensor.setMarked(true);
			SimLog.add("S" + sensor.getId() + " MARK");
		}
		Visualisation.updateStdSensorNode(sensor);
		//Layer.getMapViewer().repaint();	
		return 0 ;
	}
	
	@Override
	public String getArduinoForm() {
		BeginInstructions.add("pinMode(13, OUTPUT);");
		String s = arg;
		if(arg.charAt(0)=='$') s = arg.substring(1);
		return "\tdigitalWrite(13, "+s+");";
	}

	@Override
	public String toString() {
		return "MARK";
	}
	
}
