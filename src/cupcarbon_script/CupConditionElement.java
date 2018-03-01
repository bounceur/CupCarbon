package cupcarbon_script;

public abstract class CupConditionElement {

	protected boolean value;

	public CupConditionElement() {

	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public abstract boolean evaluate();
}
