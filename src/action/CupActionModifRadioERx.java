package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioERx extends CupAction {	
	
	private RadioModule radioModule;
	private double eRx;
	private double cERx;
	
	public CupActionModifRadioERx(RadioModule radioModule, double cERx, double eRx) {
		super();
		this.radioModule = radioModule;
		this.eRx = eRx;
		this.cERx = cERx;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setERx(eRx);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setERx(cERx);
	}

}
