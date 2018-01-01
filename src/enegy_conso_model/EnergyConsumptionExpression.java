package enegy_conso_model;

import java.util.ArrayList;
import java.util.StringTokenizer;

import cupcarbon.CupCarbon;

public class EnergyConsumptionExpression {
	
	private EnergyConsumptionTokenStack operatorStack;
	private EnergyConsumptionTokenStack argumentStack;
	private boolean error;

	public EnergyConsumptionExpression() {
		operatorStack = new EnergyConsumptionTokenStack();
		argumentStack = new EnergyConsumptionTokenStack();
		error = false;
	}

	private void processOperator(EnergyConsumptionToken token) {
		EnergyConsumptionToken arg1 = null, arg2 = null;
		if (argumentStack.isEmpty()) {
			CupCarbon.cupCarbonController.displayShortErrMessage("ERROR!");
			System.err.println(">> ERREOR Energy consumption evaluation!");
			System.err.println(">> Type: "+EnergyConsumptionModel.type + " - Sensor: S" + EnergyConsumptionModel.id+" - Radio: "+EnergyConsumptionModel.rn);
			System.err.println(">> Expression error 1.");
			error = true;
			return;
		} else {
			arg2 = argumentStack.top();
			argumentStack.pop();
		}
		if (argumentStack.isEmpty()) {
			CupCarbon.cupCarbonController.displayShortErrMessage("ERROR!");
			System.err.println(">> ERREOR Energy consumption evaluation!");
			System.err.println(">> Type: "+EnergyConsumptionModel.type + " - Sensor: S" + EnergyConsumptionModel.id+" - Radio: "+EnergyConsumptionModel.rn);
			System.err.println(">> Expression error 2 (problem in the model!)");
			error = true;
			return;
		} else {
			arg1 = argumentStack.top();
			argumentStack.pop();
		}
		EnergyConsumptionToken result = token.operation(arg1.getValue(), arg2.getValue());
		argumentStack.push(result);
	}

	public String processInput(String expression) {
		expression.replaceAll("\\s+", "");
		ArrayList<EnergyConsumptionToken> tokens = new ArrayList<EnergyConsumptionToken>();
		StringTokenizer st = new StringTokenizer(expression, "()+-*/%^", true);

		String script = "";
		String currentToken = "";
		boolean operand = false;
		String operandToken = "";

		while (st.hasMoreTokens()) {
			currentToken = st.nextToken();
			if ((currentToken.startsWith("n")) ||
				(currentToken.startsWith("r")) ||
				(currentToken.startsWith("t")) ||
				(currentToken.startsWith("p")) ||
				(currentToken.startsWith("etx")) ||
				(currentToken.startsWith("erx"))
					) {
				if (!operandToken.isEmpty() && !operandToken.matches(".*\\d.*")) {
					operandToken = operandToken + "1";
					tokens.add(new EnergyConsumptionToken(operandToken));
					tokens.add(new EnergyConsumptionToken("*"));
					operandToken = "";
				}

				operandToken = operandToken + EnergyConsumptionModel.getVariableValue(currentToken);
				operand = true;
			} else if (currentToken.equals("(")) {
				if (!operandToken.isEmpty() && !operandToken.matches(".*\\d.*")) {
					operandToken = operandToken + "1";
					tokens.add(new EnergyConsumptionToken(operandToken));
					tokens.add(new EnergyConsumptionToken("*"));
				}
				tokens.add(new EnergyConsumptionToken(currentToken));
				operandToken = "";
			} else if (operand) {
				if (Character.isDigit(currentToken.charAt(0)))
					operandToken += currentToken;
				else if (currentToken.equals(")")) {
					if (!operandToken.isEmpty())
						tokens.add(new EnergyConsumptionToken(operandToken));
					tokens.add(new EnergyConsumptionToken(currentToken));
					operandToken = "";
				} else {
					if (!operandToken.isEmpty())
						tokens.add(new EnergyConsumptionToken(operandToken));
					tokens.add(new EnergyConsumptionToken(currentToken));
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
			tokens.add(new EnergyConsumptionToken(operandToken));

		// Main loop - process all input tokens
		for (int n = 0; n < tokens.size(); n++) {
			EnergyConsumptionToken nextToken = tokens.get(n);
			if (nextToken.getType() == EnergyConsumptionToken.NUMBER) {
				argumentStack.push(nextToken);
			} else if (nextToken.getType() == EnergyConsumptionToken.OPERATOR) {
				if (operatorStack.isEmpty() || nextToken.getPrecedence() > operatorStack.top().getPrecedence()) {
					operatorStack.push(nextToken);
				} else {
					while (!operatorStack.isEmpty()
							&& nextToken.getPrecedence() <= operatorStack.top().getPrecedence()) {
						EnergyConsumptionToken toProcess = operatorStack.top();
						operatorStack.pop();
						processOperator(toProcess);
					}
					operatorStack.push(nextToken);
				}
			} else if (nextToken.getType() == EnergyConsumptionToken.LEFT_PARENTHESIS) {
				operatorStack.push(nextToken);
			} else if (nextToken.getType() == EnergyConsumptionToken.RIGHT_PARENTHESIS) {
				while (!operatorStack.isEmpty() && operatorStack.top().getType() == EnergyConsumptionToken.OPERATOR) {
					EnergyConsumptionToken toProcess = operatorStack.top();
					operatorStack.pop();
					processOperator(toProcess);
				}
				if (!operatorStack.isEmpty() && operatorStack.top().getType() == EnergyConsumptionToken.LEFT_PARENTHESIS) {
					operatorStack.pop();
				} else {
					CupCarbon.cupCarbonController.displayShortErrMessage("ERROR!");
					System.err.println(">> ERREOR Energy consumption evaluation!");
					System.err.println(">> Type: "+EnergyConsumptionModel.type + " - Sensor: S" + EnergyConsumptionModel.id+" - Radio: "+EnergyConsumptionModel.rn);
					System.err.println(">> Error: unbalanced parenthesis.");
					error = true;
				}
			}

		}
		
		while (!operatorStack.isEmpty() && operatorStack.top().getType() == EnergyConsumptionToken.OPERATOR) {
			EnergyConsumptionToken toProcess = operatorStack.top();
			operatorStack.pop();
			processOperator(toProcess);
		}

		if (!error) {
			EnergyConsumptionToken result = argumentStack.top();
			argumentStack.pop();
			if (!operatorStack.isEmpty() || !argumentStack.isEmpty()) {
				CupCarbon.cupCarbonController.displayShortErrMessage("ERROR!");
				System.err.println(">> ERREOR Energy consumption evaluation!");
				System.err.println(">> Type: "+EnergyConsumptionModel.type + " - Sensor: S" + EnergyConsumptionModel.id+" - Radio: "+EnergyConsumptionModel.rn);
				System.err.println(">> Expression error 3");
				return ">> Expression error 3";
			} else {
				script = " " + result.getValue();
				return script;
			}
		}
		CupCarbon.cupCarbonController.displayShortErrMessage("ERROR!");
		System.err.println(">> ERREOR Energy consumption evaluation!");
		System.err.println(">> Type: "+EnergyConsumptionModel.type + " - Sensor: S" + EnergyConsumptionModel.id+" - Radio: "+EnergyConsumptionModel.rn);
		System.err.println(">> May be a variable in the energy consumption model does not exist!");
		return "Expression error 4";
	}
}
