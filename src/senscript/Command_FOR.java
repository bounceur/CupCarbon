package senscript;

import device.SensorNode;

public class Command_FOR extends Command {
	
	//protected boolean resultOfCondition = true ;
	
	protected String arg1 ;
	protected String arg2 ;
	
	protected String sStep ;
	protected double step ;
	
	protected String left = "";
	protected String right ;
	
	protected boolean trueCondition = true;
	
	protected boolean first = true ;
	protected boolean exist = false ;
	
	protected int index = -1;
	protected int endForIndex = -1;
	
	protected Command_FOR parent = null;
	
	public boolean isCondition() {
		return trueCondition;
	}

	public void setCondition(boolean isCondition) {
		this.trueCondition = isCondition;
	}
	
	public Command_FOR getParent() {
		return parent;
	}

	public void setParent(Command_FOR parent) {
		this.parent = parent;
	}

	public Command_FOR(SensorNode sensor, String arg1, String arg2, String arg3, String arg4) {
		// for i 0 10 1
		this.sensor = sensor ; 	
		
		trueCondition = true;
		first = true ;
		exist = false ;
				
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		left = "$"+arg1;	
		right = arg3;
		sStep = arg4;
		
	}
	
	public String getRight() {
		return right;
	}
	
	public String getLeft() {
		return left;
	}
	
	public double getStep() {
		return step ;
	}
	
	private boolean firstVerif = true ;
	@Override
	public double execute() {
		//WisenSimulation.simLog.add("S" + sensor.getId() + " FOR ");
		if (first) {
			step = Double.valueOf(sensor.getScript().getVariableValue(sStep));
			first = false ;
			exist = sensor.getScript().variableExist(arg1);
			String arg = sensor.getScript().getVariableValue(arg2);
			sensor.getScript().addVariable(arg1, arg);
		}
		
		index = sensor.getScript().getIndex();
		
		SenScript script = sensor.getScript();
		
		String n = getRight();
		String variable = getLeft();
		
		//Command_FOR commandFor =  this.getCurrentFor();
		
		String v1 = sensor.getScript().getVariableValue(variable);		
		String v2 = sensor.getScript().getVariableValue(""+getStep());		
		
		double z = 0;
		if(firstVerif) {
			firstVerif = false;
			z = Double.valueOf(v1);
		}
		else {
			z = Double.valueOf(v1) + Double.valueOf(v2);
		}
		
		sensor.getScript().addVariable(variable.substring(1, variable.length()), ""+z);
		
		SenScriptCondition condition = null;
		
		if(getStep()>=0)
			condition = new SenScriptCondition_LESS(sensor, variable, n);
		else 
			condition = new SenScriptCondition_GREATER(sensor, variable, n);
		
		trueCondition = condition.evaluate();		
		
		if (!trueCondition) {
		//	script.setIndex(commandFor.getIndex()-1);
		//}
		//else {
			script.setIndex(endForIndex);
			init();
		}
		
		//index = sensor.getScript().getIndex();
		
		
		return 0 ;
	}
	
	public boolean getTrueCondition() {
		return trueCondition;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
	public int getEndForIndex() {
		return endForIndex;
	}

	public void setEndForIndex(int endForIndex) {
		this.endForIndex = endForIndex;
	}
	
	
	public void removeVar() {
		sensor.getScript().removeVar(arg1);
	}
	
	public void init() {
		first = true ;
		firstVerif = true;
		if(!exist) {
			String arg = sensor.getScript().getVariableValue(arg2);
			sensor.getScript().addVariable(arg1, arg);
			sensor.getScript().removeVar(arg1);
		}
	}
		
	@Override
	public String getArduinoForm() {
		String sarg2 = arg2.startsWith("$")?arg2.substring(1)+".toInt()":arg2;
		String sright = right.startsWith("$")?right.substring(1)+".toInt()":right;
		String ssStep = sStep.startsWith("$")?sStep.substring(1)+".toInt()":sStep;
		String s = "for(int "+arg1+"="+sarg2+";"+arg1+"<"+sright+";"+arg1+"+="+ssStep+") {";
		
		return s;
	}
	
	@Override
	public String toString() {
		return "FOR";
	}
}
