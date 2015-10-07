package script;

import map.Layer;
import arduino.BeginInstructions;
import device.MediaSensorNode;
import device.SensorNode;

public class Command_ROTATE extends Command {

	protected String arg = "" ;
	
	public Command_ROTATE(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public long execute() {
		String args = sensor.getScript().getVariableValue(arg);
		double n = Double.valueOf(args);
		((MediaSensorNode) sensor).setSensorUnitDec(n);
		Layer.getMapViewer().repaint();	
		return 0;
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
