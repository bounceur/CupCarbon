package enegy_conso_model;

public class EnergyConsumptionToken {

	public static final int UNKNOWN = -1;
	public static final int NUMBER = 0;
	public static final int OPERATOR = 1;
	public static final int LEFT_PARENTHESIS = 2;
	public static final int RIGHT_PARENTHESIS = 3;

	private int type;
	private double value;
	private char operator;
	private int precedence;

	public EnergyConsumptionToken() {
		type = UNKNOWN;
	}

	public EnergyConsumptionToken(double value) {
		type = NUMBER;
		this.value = value;
	}

	public EnergyConsumptionToken(String contents) {
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

	EnergyConsumptionToken operation(double arg1, double arg2) {
		double result = 0;
		switch (operator) {
		case '+':
			result = EnergyConsumptionModel.getVariableValue(arg1 + "") + EnergyConsumptionModel.getVariableValue(arg2 + "");
			break;
		case '-':
			result = EnergyConsumptionModel.getVariableValue(arg1 + "") - EnergyConsumptionModel.getVariableValue(arg2 + "");
			break;
		case '*':
			result = EnergyConsumptionModel.getVariableValue(arg1 + "") * EnergyConsumptionModel.getVariableValue(arg2 + "");
			break;
		case '/':
			result = EnergyConsumptionModel.getVariableValue(arg1 + "") / EnergyConsumptionModel.getVariableValue(arg2 + "");
			break;
		case '%':
			result = EnergyConsumptionModel.getVariableValue(arg1 + "") % EnergyConsumptionModel.getVariableValue(arg2 + "");
			break;
		case '^':
			result = Math.pow(EnergyConsumptionModel.getVariableValue(arg1 + ""),EnergyConsumptionModel.getVariableValue(arg2 + ""));
			break;
		}
		return new EnergyConsumptionToken(result);
	}
}
