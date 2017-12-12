package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_SADD extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String symbol = "&";
	
	public Command_SADD(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String v1 = sensor.getScript().getVariableValue(arg1);
		String v2 = sensor.getScript().getVariableValue("$"+arg2);
		//System.out.println(">>>> "+v1+" "+v2);
		String z = v2;
		if((z == null) || (z.equals(""))) {
			z = v1;
		}
		else
			if(v1 != null) {				
				z = v1 + symbol + z;				
			}
		WisenSimulation.simLog.add("S" + sensor.getId() + " " + arg1 + " = (" + v1 + ") + & + (" + v2 + ") -> " + z);
		sensor.getScript().addVariable(arg2, z);
		return 0 ;
	}

	@Override
	public String toString() {
		return "SADD";
	}
}
