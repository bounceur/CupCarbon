package senscript;

import device.MediaSensorNode;
import device.SensorNode;

public class Command_ROTATE extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_ROTATE(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String vArg1 = sensor.getScript().getVariableValue(arg1);
		String vArg2 = sensor.getScript().getVariableValue(arg2);
		double n = Double.valueOf(vArg1);
		double t = Integer.valueOf(vArg2);
		((MediaSensorNode) sensor).setSensorUnitDec(n);
		//MapLayer.repaint();	
		return t ;
	}

	@Override
	public String toString() {
		return "ROTATE";
	}
	
}
