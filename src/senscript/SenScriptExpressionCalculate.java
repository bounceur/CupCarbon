package senscript;

import java.util.ArrayList;
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
  	
  	String script = "";
  	int i = 0;
  	String digit = "";
  	expression.replaceAll("\\s+","");
  	ArrayList<SenScriptToken> tokens = new ArrayList<SenScriptToken>();
  	while ( i < expression.length())
  		if (expression.charAt(i) == '$') {
  			tokens.add(new SenScriptToken(sensor.getScript().getVariableValue("$" + expression.charAt(i+1))));
  			i = i+2;
  		}
  		else if (!Character.isDigit(expression.charAt(i))) {
  			tokens.add(new SenScriptToken(expression.charAt(i)+""));
  			i = i+1;
  			digit="";
  		}
  		else {
  			digit = "" + expression.charAt(i);
  			i = i+1;
  			while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
  				digit = digit+ expression.charAt(i);
  				i = i+1;
  			}
  			tokens.add(new SenScriptToken(digit));
  		}
  	
  			

    // Main loop - process all input tokens
    for (int n = 0; n < tokens.size(); n++) {
        SenScriptToken nextToken = tokens.get(n);
        if (nextToken.getType() == SenScriptToken.NUMBER) {
            argumentStack.push(nextToken);
        } else if (nextToken.getType() == SenScriptToken.OPERATOR) {
            if (operatorStack.isEmpty() || nextToken.getPrecedence() > operatorStack.top().getPrecedence()) {
                operatorStack.push(nextToken);
            } else {
                while (!operatorStack.isEmpty() && nextToken.getPrecedence() <= operatorStack.top().getPrecedence()) {
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
      if(error == false) {
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
