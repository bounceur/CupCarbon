package enegy_conso_model;

import java.util.ArrayList;

public class EnergyConsumptionTokenStack {

	private ArrayList<EnergyConsumptionToken> tokens;

	public EnergyConsumptionTokenStack() {
		tokens = new ArrayList<EnergyConsumptionToken>();
	}

	public boolean isEmpty() {
		return tokens.size() == 0;
	}

	public EnergyConsumptionToken top() {
		return tokens.get(tokens.size() - 1);
	}

	public void push(EnergyConsumptionToken t) {
		tokens.add(t);
	}

	public void pop() {
		tokens.remove(tokens.size() - 1);
	}
}
