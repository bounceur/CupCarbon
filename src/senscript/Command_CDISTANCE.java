package senscript;

import device.SensorNode;
import simulation.WisenSimulation;
import utilities.MapCalc;

public class Command_CDISTANCE extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	protected String arg3 = "" ;
	protected String arg4 = "" ;
	protected String arg5 = "" ;
	
	public Command_CDISTANCE(SensorNode sensor, String arg1, String arg2, String arg3, String arg4, String arg5) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
		this.arg5 = arg5 ;
	}

	@Override
	public double execute() {
		double lo1 = Double.parseDouble(sensor.getScript().getVariableValue(arg2));
		double la1 = Double.parseDouble(sensor.getScript().getVariableValue(arg3));
		double lo2 = Double.parseDouble(sensor.getScript().getVariableValue(arg4));
		double la2 = Double.parseDouble(sensor.getScript().getVariableValue(arg5));
		double distance = MapCalc.distance(lo1, la1, lo2, la2);
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " CDISTANCE "+lo1+" "+la1+" "+lo2+" "+la2);
		
		sensor.getScript().addVariable(arg1, "" + distance);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "CDISTANCE";
	}
	
}
