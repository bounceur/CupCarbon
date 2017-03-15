package senscript;

import device.SensorNode;
import wisen_simulation.SimLog;
//H
public class Command_ENDFOR extends Command {
	
	protected boolean trueCondition = true ;	
	protected int index ;

	public Command_ENDFOR(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {		
		SimLog.add("S" + sensor.getId() + " END FOR.");
		
		SenScript script = sensor.getScript();
		
		String n = getCurrentFor().getRight();
		String variable = getCurrentFor().getLeft();
		
		Command_FOR commandFor =  this.getCurrentFor();
		
		String v1 = sensor.getScript().getVariableValue(variable);		
		String v2 = sensor.getScript().getVariableValue(""+commandFor.getStep());		
		double z = 0;
		z = Double.valueOf(v1) + Double.valueOf(v2);
		sensor.getScript().addVariable(variable.substring(1, variable.length()), ""+z);
		
		//Command_PLUS cmd = new Command_PLUS(this.sensor, variable.substring(1, variable.length()), variable, ""+commandFor.getStep());
		//cmd.execute();
		
		SenScriptCondition condition = null;
		if(commandFor.getStep()>=0)
			condition = new SenScriptCondition_LESS(sensor, variable, n);
		else 
			condition = new SenScriptCondition_GREATER(sensor, variable, n);
		trueCondition = condition.evaluate();		
		
		if (trueCondition) {
			script.setIndex(commandFor.getIndex()-1);
		}
		else {
			commandFor.init();
		}
		
		return 0 ;
	}
	
	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	
	public boolean isTrueCondition() {
		return trueCondition;
	}

	@Override
	public String getArduinoForm() {
		return "}";
	}
	
	@Override
	public String toString() {
		return "ENDFOR";
	}
}
