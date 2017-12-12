package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_SET extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	
	public Command_SET(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		boolean contained = false;
		String arg = "";
		WisenSimulation.simLog.add("S" + sensor.getId() + " SET " + arg1 + "=" + arg2);
		String[] match={"(",")","+","-","*","/", "%"};
		int i = 0;
		while (!contained && i< match.length) {
			if (arg2.contains(match[i]))
					contained = true;
			i=i+1;
		}
		if (contained) { 
			SenScriptExpressionCalculate calculator = new SenScriptExpressionCalculate();
			arg = calculator.processInput(arg2, sensor);
		}
		else 
			arg = sensor.getScript().getVariableValue(arg2);
		sensor.getScript().addVariable(arg1, arg);
		return 0 ;
	}

	@Override
	public String toString() {
		return "SET";
	}
	
	@Override
	public String getArduinoForm() {
		String s = arg1 + " = " + arg2.replace("$", "") + ";";
		return s;
	}
	
}
