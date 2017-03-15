package action;

import radio_module.RadioModule;

public class CupActionModifRadioRadius extends CupAction {	
	
	private RadioModule radioModule;
	private double radius;
	private double cRadius;
	
	public CupActionModifRadioRadius(RadioModule radioModule, double cRadius, double radius) {
		super();
		this.radioModule = radioModule;
		this.radius = radius;
		this.cRadius = cRadius;
	}

	@Override
	public void execute() {
		radioModule.setRadioRangeRadius(radius);
		radioModule.setRadioRangeRadiusOri(radius);
	}

	@Override
	public void antiExecute() {
		radioModule.setRadioRangeRadius(cRadius);
		radioModule.setRadioRangeRadiusOri(cRadius);
	}

}
