package script;

import map.Layer;
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
	public String toString() {
		return "ROTATE";
	}
	
}
