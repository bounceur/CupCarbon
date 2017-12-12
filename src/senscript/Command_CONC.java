package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_CONC extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String symbol = "";
	
	public Command_CONC(SensorNode sensor, String arg1, String symbol, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.symbol = symbol;
	}

	@Override
	public double execute() {
		String v1 = sensor.getScript().getVariableValue(arg2);
		String v2 = sensor.getScript().getVariableValue(arg3);
		symbol = sensor.getScript().getVariableValue(symbol);
		if(symbol.equals("\\")) symbol = "";
		String z = v1+symbol+v2;
		WisenSimulation.simLog.add("S" + sensor.getId() + " " + arg1 + " = (" + v1 + ") + (" + v2 + ") -> " + z);
		sensor.getScript().addVariable(arg1, z);
		return 0 ;
	}

	@Override
	public String toString() {
		return "CONC";
	}
}
