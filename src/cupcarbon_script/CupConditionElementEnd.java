package cupcarbon_script;

public class CupConditionElementEnd extends CupConditionElement {

	protected CupCondition condition;

	public CupConditionElementEnd(CupScript script, String cond) {

		String[] inst = CupCondition.getTwoParts(cond);

		switch (inst[2]) {
		case ">":
			condition = new CupCondition_GREATER(script, inst[0], inst[1]);
			break;
		case ">=":
			condition = new CupCondition_GREATEREQUAL(script, inst[0], inst[1]);
			break;
		case "<":
			condition = new CupCondition_LESS(script, inst[0], inst[1]);
			break;
		case "<=":
			condition = new CupCondition_LESSEQUAL(script, inst[0], inst[1]);
			break;
		case "==":
			condition = new CupCondition_EQUAL(script, inst[0], inst[1]);
			break;
		case "!=":
			condition = new CupCondition_NOTEQUAL(script, inst[0], inst[1]);
			break;
		default:
		}
	}

	public boolean evaluate() {
		value = condition.evaluate();
		return value;
	}

	public CupCondition getCondition() {
		return condition;
	}

	public void setCondition(CupCondition condition) {
		this.condition = condition;
	}

}
