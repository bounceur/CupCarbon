package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_MATH extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";
	
	public Command_MATH(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}
	
	public Command_MATH(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
	}

	@Override
	public double execute() {
		
		String arg = arg2 ;
		double a = Double.valueOf(sensor.getScript().getVariableValue(arg3));
		double b = 0;
		if(!arg4.equals(""))
			b =  Double.valueOf(sensor.getScript().getVariableValue(arg4));
		
		System.out.println(a+" "+b);
		
		double r = 0;
		
		if(arg1.toLowerCase().equals("round"))
			r = Math.round(a);
		if(arg1.toLowerCase().equals("sqrt"))
			r = Math.sqrt(a);
		if(arg1.toLowerCase().equals("sin"))
			r = Math.sin(a);
		if(arg1.toLowerCase().equals("cos"))
			r = Math.cos(a);
		if(arg1.toLowerCase().equals("tan"))
			r = Math.tan(a);		
		if(arg1.toLowerCase().equals("asin"))
			r = Math.asin(a);
		if(arg1.toLowerCase().equals("acos"))
			r = Math.acos(a);
		if(arg1.toLowerCase().equals("atan"))
			r = Math.atan(a);
		if(arg1.toLowerCase().equals("abs"))
			r = Math.abs(a);
		if(arg1.toLowerCase().equals("pow"))
			r = Math.pow(a, b);
		if(arg1.toLowerCase().equals("log"))
			r = Math.log(a);
		if(arg1.toLowerCase().equals("log10"))
			r = Math.log10(a);
		if(arg1.toLowerCase().equals("exp"))
			r = Math.exp(a);

		WisenSimulation.simLog.add("S" + sensor.getId() + arg1.toUpperCase() + " : "+a+" "+b);
		
		sensor.getScript().addVariable(arg,""+r);
		return 0 ;
	}

	@Override
	public String toString() {
		return arg1.toUpperCase();
	}
	
}
