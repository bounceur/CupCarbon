package senscript;

public abstract class SenScriptConditionElement {
	
	protected boolean value;
	
	public SenScriptConditionElement(){

	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public abstract boolean evaluate();
}
