package script;

import device.SensorNode;

public class Command_IF extends Command {

	protected String arg = "" ;
	protected Command_IF parent = null;
	protected boolean resultOfCondition = false;
	protected int ifIndex = -1;
	protected int elseIndex = -1;
	protected int endIfIndex = -1;
	
	public Command_IF(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
		parent = null;
		resultOfCondition = false;
		ifIndex = -1;
		elseIndex = -1;
		endIfIndex = -1;
	}
	
	@Override
	public int execute() {		
		String condition = arg.replaceFirst("if", "");		
		EvalCondition evalCondtion = new EvalCondition(sensor);
		
		ConditionElement conditionElement = evalCondtion.initCondition(condition);
		resultOfCondition = conditionElement.evaluate();
		
		
		if (!resultOfCondition)
			if (elseIndex != -1) {
				sensor.getScript().setIndex(elseIndex);
			}
			else {
				sensor.getScript().setIndex(endIfIndex);
			}
		
		return 0;
	}
	
	public void setParent(Command_IF parent){
		this.parent = parent;
	}

	public Command_IF getParent() {
		return parent;
	}
	
	public boolean getRestultOfCondition() {
		return resultOfCondition ;
	}
	
	public void setRestultOfCondition(boolean resultOfCondition) {
		this.resultOfCondition = resultOfCondition;
	}
	
	public int getElseIndex() {
		return elseIndex;
	}

	public void setElseIndex(int elseIndex) {
		this.elseIndex = elseIndex;
	}

	public int getEndIfIndex() {
		return endIfIndex;
	}

	public void setEndIfIndex(int endIfIndex) {
		this.endIfIndex = endIfIndex;
	}

	
	public int getIfIndex() {
		return ifIndex;
	}

	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}
	
	@Override
	public String toString() {		
		return "IF";
	}
}
