package cupcarbon_script;

public class CupConditionElementAnd extends CupConditionElement {

	protected CupConditionElement conditionLeft;
	protected CupConditionElement conditionRight;

	public CupConditionElementAnd(CupConditionElement conditionL, CupConditionElement conditionR) {
		this.conditionLeft = conditionL;
		this.conditionRight = conditionR;

	}

	public boolean evaluate() {

		value = conditionLeft.evaluate() && conditionRight.evaluate();

		return value;
	}

	public CupConditionElement getConditionLeft() {
		return conditionLeft;
	}

	public void setConditionLeft(CupConditionElement conditionLeft) {
		this.conditionLeft = conditionLeft;
	}

	public CupConditionElement getConditionRight() {
		return conditionRight;
	}

	public void setConditionRight(CupConditionElement conditionRight) {
		this.conditionRight = conditionRight;
	}

}
