package senscript;

public class SenScriptConditionElementOr extends SenScriptConditionElement {

	protected SenScriptConditionElement conditionLeft;
	protected SenScriptConditionElement conditionRight;

	public SenScriptConditionElementOr(SenScriptConditionElement conditionL, SenScriptConditionElement conditionR) {
		this.conditionLeft = conditionL;
		this.conditionRight = conditionR;

	}

	public boolean evaluate() {

		value = conditionLeft.evaluate() || conditionRight.evaluate();

		return value;
	}

	public SenScriptConditionElement getConditionLeft() {
		return conditionLeft;
	}

	public void setConditionLeft(SenScriptConditionElement conditionLeft) {
		this.conditionLeft = conditionLeft;
	}

	public SenScriptConditionElement getConditionRight() {
		return conditionRight;
	}

	public void setConditionRight(SenScriptConditionElement conditionRight) {
		this.conditionRight = conditionRight;
	}

}
