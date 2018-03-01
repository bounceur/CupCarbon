package senscript;

import device.SensorNode;

public class SenScriptCondition_GREATER extends SenScriptCondition {
	
	public SenScriptCondition_GREATER(SensorNode sensor, String arg1, String arg2){
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}
	
	
	@Override
	public boolean evaluate() {
		double v1 = 0;
		double v2 = 0;
		try {
			v1 = Double.valueOf(sensor.getScript().getVariableValue(arg1));
		} 
		catch(Exception e) {
			System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): Condition > ("+arg1+" is not a number)");
		}
		
		try {
			v2 = Double.valueOf(sensor.getScript().getVariableValue(arg2));
		} 
		catch(Exception e) {
			System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): Condition > ("+arg1+" is not a number)");
		}
		
		return  (v1 > v2);
	}

}
