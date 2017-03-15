package cupcarbon_script;

public class CupConditionElementOr extends CupConditionElement {

	protected CupConditionElement conditionLeft;
	protected CupConditionElement conditionRight;

	public CupConditionElementOr(CupConditionElement conditionL, CupConditionElement conditionR) {
		this.conditionLeft = conditionL;
		this.conditionRight = conditionR;

	}

	public boolean evaluate() {

		value = conditionLeft.evaluate() || conditionRight.evaluate();

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
