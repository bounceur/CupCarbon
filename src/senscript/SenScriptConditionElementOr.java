package script;

import device.SensorNode;

public class ConditionElementOr extends ConditionElement{
	
	
		
	protected ConditionElement conditionLeft;
	protected ConditionElement conditionRight;
	
	
	
	public ConditionElementOr(SensorNode sensor, ConditionElement conditionL, ConditionElement conditionR){
		this.conditionLeft = conditionL;
		this.conditionRight = conditionR;
		
	}
	
	public boolean evaluate(){
		
		value = conditionLeft.evaluate() || conditionRight.evaluate();
		
		return value;
	}
	
	
	public ConditionElement getConditionLeft() {
		return conditionLeft;
	}



	public void setConditionLeft(ConditionElement conditionLeft) {
		this.conditionLeft = conditionLeft;
	}



	public ConditionElement getConditionRight() {
		return conditionRight;
	}



	public void setConditionRight(ConditionElement conditionRight) {
		this.conditionRight = conditionRight;
	}

}
