package senscript;

import java.util.ArrayList;
import java.util.StringTokenizer;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class SenScriptExpressionCalculate {
	private SenScriptTokenStack operatorStack;
	private SenScriptTokenStack argumentStack;
	private boolean error;

	public SenScriptExpressionCalculate() {
		operatorStack = new SenScriptTokenStack();
		argumentStack = new SenScriptTokenStack();
		error = false;
	}

	private void processOperator(SensorNode sensor, SenScriptToken token) {
		SenScriptToken arg1 = null, arg2 = null;
		if (argumentStack.isEmpty()) {
			System.out.println("Expression error");
			error = true;
			return;
		} else {
			arg2 = argumentStack.top();
			argumentStack.pop();
		}
		if (argumentStack.isEmpty()) {
			System.out.println("Expression error");
			error = true;
			return;
		} else {
			arg1 = argumentStack.top();
			argumentStack.pop();
		}
		SenScriptToken result = token.operation(sensor, arg1.getValue(), arg2.getValue());
		argumentStack.push(result);
	}

	public String processInput(String expression, SensorNode sensor) {

		expression.replaceAll("\\s+", "");
		ArrayList<SenScriptToken> tokens = new ArrayList<SenScriptToken>();
		StringTokenizer st = new StringTokenizer(expression, "()+-*/%^", true);

		String script = "";
		String currentToken = "";
		boolean operand = false;
		String operandToken = "";

		while (st.hasMoreTokens()) {
			currentToken = st.nextToken();
			if (currentToken.contains("$")) {
				if (!operandToken.isEmpty() && !operandToken.matches(".*\\d.*")) {
					operandToken = operandToken + "1";
					tokens.add(new SenScriptToken(operandToken));
					tokens.add(new SenScriptToken("*"));
					operandToken = "";
				}
				if(sensor.getScript().getVariableValue(currentToken)==null) {
					System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): SET function ("+currentToken+" does not exist)");
					CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
				}
				operandToken = operandToken + sensor.getScript().getVariableValue(currentToken).trim();
				operand = true;
			} else if (currentToken.equals("(")) {
				if (!operandToken.isEmpty() && !operandToken.matches(".*\\d.*")) {
					operandToken = operandToken + "1";
					tokens.add(new SenScriptToken(operandToken));
					tokens.add(new SenScriptToken("*"));
				}
				tokens.add(new SenScriptToken(currentToken));
				operandToken = "";
			} else if (operand) {
				if (Character.isDigit(currentToken.charAt(0)))
					operandToken += currentToken;
				else if (currentToken.equals(")")) {
					if (!operandToken.isEmpty())
						tokens.add(new SenScriptToken(operandToken));
					tokens.add(new SenScriptToken(currentToken));
					operandToken = "";
				} else {
					if (!operandToken.isEmpty())
						tokens.add(new SenScriptToken(operandToken));
					tokens.add(new SenScriptToken(currentToken));
					operandToken = "";
					operand = false;
				}
			} else {
				if (Character.isDigit(currentToken.charAt(0)))
					operand = true;
				operandToken += currentToken;
			}
		}
		if (!operandToken.isEmpty())
			tokens.add(new SenScriptToken(operandToken));

		// Main loop - process all input tokens
		for (int n = 0; n < tokens.size(); n++) {
			SenScriptToken nextToken = tokens.get(n);
			if (nextToken.getType() == SenScriptToken.NUMBER) {
				argumentStack.push(nextToken);
			} else if (nextToken.getType() == SenScriptToken.OPERATOR) {
				if (operatorStack.isEmpty() || nextToken.getPrecedence() > operatorStack.top().getPrecedence()) {
					operatorStack.push(nextToken);
				} else {
					while (!operatorStack.isEmpty()
							&& nextToken.getPrecedence() <= operatorStack.top().getPrecedence()) {
						SenScriptToken toProcess = operatorStack.top();
						operatorStack.pop();
						processOperator(sensor, toProcess);
					}
					operatorStack.push(nextToken);
				}
			} else if (nextToken.getType() == SenScriptToken.LEFT_PARENTHESIS) {
				operatorStack.push(nextToken);
			} else if (nextToken.getType() == SenScriptToken.RIGHT_PARENTHESIS) {
				while (!operatorStack.isEmpty() && operatorStack.top().getType() == SenScriptToken.OPERATOR) {
					SenScriptToken toProcess = operatorStack.top();
					operatorStack.pop();
					processOperator(sensor, toProcess);
				}
				if (!operatorStack.isEmpty() && operatorStack.top().getType() == SenScriptToken.LEFT_PARENTHESIS) {
					operatorStack.pop();
				} else {
					System.out.println("Error: unbalanced parenthesis.");
					error = true;
				}
			}

		}
		// Empty out the operator stack at the end of the input
		while (!operatorStack.isEmpty() && operatorStack.top().getType() == SenScriptToken.OPERATOR) {
			SenScriptToken toProcess = operatorStack.top();
			operatorStack.pop();
			processOperator(sensor, toProcess);
		}
		// get the result
		if (error == false) {
			SenScriptToken result = argumentStack.top();
			argumentStack.pop();
			if (!operatorStack.isEmpty() || !argumentStack.isEmpty()) {
				return "Expression error";
			} else {
				script = " " + result.getValue();
				return script;
			}
		}
		return "Expression error";
	}
}
