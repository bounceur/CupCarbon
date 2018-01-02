package senscript;

import device.SensorNode;

public class SenScriptToken {

	public static final int UNKNOWN = -1;
	public static final int NUMBER = 0;
	public static final int OPERATOR = 1;
	public static final int LEFT_PARENTHESIS = 2;
	public static final int RIGHT_PARENTHESIS = 3;

	private int type;
	private double value;
	private char operator;
	private int precedence;

	public SenScriptToken() {
		type = UNKNOWN;
	}

	public SenScriptToken(double value) {
		type = NUMBER;
		this.value = value;
	}

	public SenScriptToken(String contents) {
		switch (contents) {
		case "+":
			type = OPERATOR;
			operator = contents.charAt(0);
			precedence = 1;
			break;
		case "-":
			type = OPERATOR;
			operator = contents.charAt(0);
			precedence = 1;
			break;
		case "*":
			type = OPERATOR;
			operator = contents.charAt(0);
			precedence = 2;
			break;
		case "/":
			type = OPERATOR;
			operator = contents.charAt(0);
			precedence = 2;
			break;
		case "%":
			type = OPERATOR;
			operator = contents.charAt(0);
			precedence = 2;
			break;
		case "^":
			type = OPERATOR;
			operator = contents.charAt(0);
			precedence = 3;
			break;
		case "(":
			type = LEFT_PARENTHESIS;
			break;
		case ")":
			type = RIGHT_PARENTHESIS;
			break;
		default:
			type = NUMBER;
			try {
				value = Double.parseDouble(contents);
			} catch (Exception ex) {
				type = UNKNOWN;
			}
		}
	}

	int getType() {
		return type;
	}

	double getValue() {
		return value;
	}

	int getPrecedence() {
		return precedence;
	}

	SenScriptToken operation(SensorNode sensor, double arg1, double arg2) {
		double result = 0;
		switch (operator) {
		case '+':
			result = Double.parseDouble(sensor.getScript().getVariableValue(arg1 + ""))
					+ Double.parseDouble(sensor.getScript().getVariableValue(arg2 + ""));
			break;
		case '-':
			result = Double.parseDouble(sensor.getScript().getVariableValue(arg1 + ""))
					- Double.parseDouble(sensor.getScript().getVariableValue(arg2 + ""));
			break;
		case '*':
			result = Double.parseDouble(sensor.getScript().getVariableValue(arg1 + ""))
					* Double.parseDouble(sensor.getScript().getVariableValue(arg2 + ""));
			break;
		case '/':
			result = Double.parseDouble(sensor.getScript().getVariableValue(arg1 + ""))
					/ Double.parseDouble(sensor.getScript().getVariableValue(arg2 + ""));
			break;
		case '%':
			result = Double.parseDouble(sensor.getScript().getVariableValue(arg1 + ""))
					% Double.parseDouble(sensor.getScript().getVariableValue(arg2 + ""));
			break;
		case '^':
			result = Math.pow(Double.parseDouble(sensor.getScript().getVariableValue(arg1 + "")),Double.parseDouble(sensor.getScript().getVariableValue(arg2 + "")));
			break;
		}
		return new SenScriptToken(result);
	}
}
