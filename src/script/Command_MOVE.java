package script;

import map.Layer;
import device.SensorNode;

public class Command_MOVE extends Command {

	protected String arg1 = "" ;
	
	public Command_MOVE(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}

	@Override
	public long execute() {
		String vArg1 = sensor.getScript().getVariableValue(arg1);
		int t = Integer.valueOf(vArg1);
		if (sensor.canMove()) {
			sensor.moveToNext(true, 0);									
		}
		Layer.getMapViewer().repaint();	
		return t;
	}

	@Override
	public String toString() {
		return "MOVE";
	}
	
}
