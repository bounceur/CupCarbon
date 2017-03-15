package senscript;

import device.SensorNode;

public class SenScriptCondition_EQUAL extends SenScriptCondition {
	
	public SenScriptCondition_EQUAL(SensorNode sensor, String arg1, String arg2){
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}
	
	
	@Override
	public boolean evaluate() {		
		String s1 = sensor.getScript().getVariableValue(arg1);
		String s2 = sensor.getScript().getVariableValue(arg2);
		try {
			double v1 = Double.valueOf(s1);
			double v2 = Double.valueOf(s2);			
			return  (v1 == v2);
		}
		catch(NumberFormatException e) {			
			return  (s1.equals(s2));
		}
	}


}
