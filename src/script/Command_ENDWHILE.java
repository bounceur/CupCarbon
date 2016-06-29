package script;

import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_ENDWHILE extends Command {

	protected boolean resultOfCondition = true ;	
	protected int index ;

	public Command_ENDWHILE(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " END WHILE.");
		
		Script script = sensor.getScript();
		
		String condition = getCurrentWhile().getArg().replaceFirst("while", "");
		
		EvalCondition evalCondtion = new EvalCondition(sensor);
		
		ConditionElement conditionElement = evalCondtion.initCondition(condition);
		resultOfCondition = conditionElement.evaluate();
		
		Command_WHILE cmdWhile =  this.getCurrentWhile();
		
		if (resultOfCondition)
			script.setIndex(cmdWhile.getIndex()-1);
		
		return 0 ;
	}
	
	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "ENDWHILE";
	}
}
