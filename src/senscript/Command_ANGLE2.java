package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_ANGLE2 extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	protected String arg4 = "";
	protected String arg5 = "";
	protected String arg6 = "";
	protected String arg7 = "";
	
	public Command_ANGLE2(SensorNode sensor, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
		this.arg4 = arg4 ;
		this.arg5 = arg5 ;
		this.arg6 = arg6 ;
		this.arg7 = arg7 ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " Calculate Angle.");
		
		double prec_X = Double.valueOf(sensor.getScript().getVariableValue(arg2));
		double prec_Y = Double.valueOf(sensor.getScript().getVariableValue(arg3));
		double local_X = Double.valueOf(sensor.getScript().getVariableValue(arg4));
		double local_Y = Double.valueOf(sensor.getScript().getVariableValue(arg5));
		double next_X = Double.valueOf(sensor.getScript().getVariableValue(arg6));
		double next_Y = Double.valueOf(sensor.getScript().getVariableValue(arg7));
		
		double b = getAngle(prec_X, prec_Y, local_X, local_Y, next_X, next_Y);

		sensor.getScript().addVariable(arg1, ""+b);			
		return 0;
	}
	
	public double getAngle(double prec_X, double prec_Y, double local_X, double local_Y, double next_X, double next_Y) {
		prec_X = prec_X - local_X;
		prec_Y = prec_Y - local_Y;
		next_X = next_X - local_X;
		next_Y = next_Y - local_Y;
		
		double b = 0;
		double a = 0;
		if(prec_X==next_X && prec_Y==next_Y) 
			b = Math.PI*2;
		else{
			a = Math.atan2(prec_X ,prec_Y);
			if (a < 0)
				a = (2 * Math.PI) + a;
			b = Math.atan2(next_X, next_Y);
			if (b < 0)
				b = (2 * Math.PI) + b;
			b = b - a;
			if (b < 0)
				b = (2 * Math.PI) + b;
		}
		return b;
	}

	@Override
	public String toString() {
		return "ANGLE2";
	}
	
}
