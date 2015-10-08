package script;

import map.Layer;
import device.SensorNode;

public class Command_COORD extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_COORD(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public long execute() {
		String vArg1 = sensor.getScript().getVariableValue(arg1);
		String vArg2 = sensor.getScript().getVariableValue(arg2);
		sensor.setLatitude(Double.valueOf(vArg1));
		sensor.setLongitude(Double.valueOf(vArg2));
		Layer.getMapViewer().repaint();	
		return 0;
	}

	@Override
	public String toString() {
		return "COORD";
	}
	
}
