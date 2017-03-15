package script;

public abstract class ConditionElement {
	
	protected boolean value;
	
	public ConditionElement(){

	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public abstract boolean evaluate();
}
