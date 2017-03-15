package script;

import device.SensorNode;

public class Condition_LESS extends Condition {
	
	public Condition_LESS(SensorNode sensor, String arg1, String arg2){
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}
	
	
	@Override
	public boolean evaluate() {		
		double v1 = Double.valueOf(sensor.getScript().getVariableValue(arg1));
		double v2 = Double.valueOf(sensor.getScript().getVariableValue(arg2));
		
		return  (v1 < v2);
	}

	
}
