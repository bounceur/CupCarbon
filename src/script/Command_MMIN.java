package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_MMIN extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";
	
	public Command_MMIN(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
	}

	@Override
	public double execute() {		
		SimLog.add("S" + sensor.getId() + " MMIN");
		String arg = arg1;
		String arg1s = sensor.getScript().getVariableValue("$"+arg);
		String arg2s = sensor.getScript().getVariableValue(arg2);			  
		String idMin = sensor.getScript().getVariableValue(arg4);
		if(Double.valueOf(arg1s) > Double.valueOf(arg2s)){
			sensor.getScript().addVariable(arg, arg2s);
			sensor.getScript().addVariable(arg3, idMin);
		}		
		return 0 ;		
	}

	@Override
	public String toString() {
		return "MMIN";
	}
	
}
