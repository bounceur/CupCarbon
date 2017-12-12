package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_WHILE extends Command {
	
	protected boolean resultOfCondition = true ;
	
	protected String arg = ""; 
	
	protected String left = "";
	protected String right = "";
	protected String ineq = "";
	
	protected int index = -1;
	protected int endWhileIndex = -1;
	
	protected Command_WHILE parent = null;
		
	public Command_WHILE getParent() {
		return parent;
	}
	public void setParent(Command_WHILE parent) {
		this.parent = parent;
	}

	public Command_WHILE(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg;
		
		String[] inst = SenScriptCondition.getTwoParts(arg); 			

		left = inst[0];
		right = inst[1];
		ineq = inst[2];
	}
	
	public String getRight() {
		return right;
	}
	
	public String getLeft() {
		return left;
	}
	
	public String getIneq() {
		return ineq;
	}
	
	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " WHILE ");
		
		index = sensor.getScript().getIndex();
		
		SenScript script = sensor.getScript();
		
		String condition = getArg().replaceFirst("while", "");
		
		SenScriptEvalCondition evalCondtion = new SenScriptEvalCondition(sensor);
		
		SenScriptConditionElement conditionElement = evalCondtion.initCondition(condition);
		resultOfCondition = conditionElement.evaluate();
		
		if(!resultOfCondition)
			script.setIndex(endWhileIndex);
		
		return 0 ;
	}
	
	public boolean getResultOfCondition() {
		return resultOfCondition;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getEndWhileIndex() {
		return endWhileIndex;
	}

	public void setEndWhileIndex(int endWhileIndex) {
		this.endWhileIndex = endWhileIndex;
	}
	
	public String getArg() {
		return arg; 
	}

	@Override
	public String toString() {
		return "WHILE";
	}
}
