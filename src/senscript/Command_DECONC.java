package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_DECONC extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String symbol = "";
	
	public Command_DECONC(SensorNode sensor, String symbol, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.symbol = symbol;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String var_n = arg1 ;
		String var_v = arg2 ;
		String val = sensor.getScript().getVariableValue(arg3);
		symbol = sensor.getScript().getVariableValue(symbol);
		if(symbol.equals("\\")) symbol = "";
		
		String [] t = val.split(symbol);
		
		int n = t.length;
		
		sensor.getScript().addVariable(var_n, "" + n);
		
		WisenSimulation.simLog.add("DECONC: " + symbol + " -> " + arg2);
		
		if(!arg3.equals("")) {
			sensor.getScript().putVector(var_v, n);		
			String [] vector = sensor.getScript().getVector(var_v);
			for(int i=0; i<n; i++) {
				vector[i] = "" + t[i];
			}
		}
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "PLUS";
	}
}
