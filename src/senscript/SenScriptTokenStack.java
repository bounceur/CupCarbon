package senscript;

import java.util.ArrayList;

public class SenScriptTokenStack {

  private ArrayList<SenScriptToken> tokens;

  public SenScriptTokenStack() {
    tokens = new ArrayList<SenScriptToken>();
  }

  public boolean isEmpty() {
    return tokens.size() == 0;
  }
  public SenScriptToken top() {
    return tokens.get(tokens.size()-1);
  }

  public void push(SenScriptToken t) {
    tokens.add(t);
  }
  public void pop() {
    tokens.remove(tokens.size()-1);
  }
}
