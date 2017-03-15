package senscript;

import device.SensorNode;

public class SenScriptConditionElementEnd extends SenScriptConditionElement {

	protected SenScriptCondition condition;

	public SenScriptConditionElementEnd(SensorNode sensor, String cond) {

		String[] inst = SenScriptCondition.getTwoParts(cond);

		switch (inst[2]) {
		case ">":
			condition = new SenScriptCondition_GREATER(sensor, inst[0], inst[1]);
			break;
		case ">=":
			condition = new SenScriptCondition_GREATEREQUAL(sensor, inst[0], inst[1]);
			break;
		case "<":
			condition = new SenScriptCondition_LESS(sensor, inst[0], inst[1]);
			break;
		case "<=":
			condition = new SenScriptCondition_LESSEQUAL(sensor, inst[0], inst[1]);
			break;
		case "==":
			condition = new SenScriptCondition_EQUAL(sensor, inst[0], inst[1]);
			break;
		case "!=":
			condition = new SenScriptCondition_NOTEQUAL(sensor, inst[0], inst[1]);
			break;
		default:
		}
	}

	public boolean evaluate() {
		value = condition.evaluate();
		return value;
	}

	public SenScriptCondition getCondition() {
		return condition;
	}

	public void setCondition(SenScriptCondition condition) {
		this.condition = condition;
	}

}
