package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_VGET extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_VGET(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String tabName = arg2;
		String x_str = sensor.getScript().getVariableValue(arg3);
		String var = arg1;
		
		int x  = Double.valueOf(x_str).intValue();
		String[] tab = sensor.getScript().getVector(tabName);
		String val = (String) tab[x];
		sensor.getScript().putVariable(var, val);	
		WisenSimulation.simLog.add("S" + sensor.getId() + " GET VECTOR VALUE "+tabName+"["+x+"] -> "+val);
		return 0 ;
	}

	@Override
	public String toString() {
		return "VGET";
	}
	
}
