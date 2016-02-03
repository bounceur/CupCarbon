package script;

import device.SensorNode;

public class Command_COORD extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	protected String arg3 = "" ;
	
	public Command_COORD(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String vArg1 = sensor.getScript().getVariableValue(arg1);
		String vArg2 = sensor.getScript().getVariableValue(arg2);
		String vArg3 = sensor.getScript().getVariableValue(arg3);
		sensor.setLongitude(Double.valueOf(vArg1));
		sensor.setLatitude(Double.valueOf(vArg2));		
		double t = Integer.valueOf(vArg3);
		//MapLayer.getMapViewer().repaint();	
		return t/1000. ;
	}

	@Override
	public String toString() {
		return "COORD";
	}
	
}
