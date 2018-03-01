package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioETx extends CupAction {	
	
	private RadioModule radioModule;
	private double eTx;
	private double cETx;
	
	public CupActionModifRadioETx(RadioModule radioModule, double cETx, double eTx) {
		super();
		this.radioModule = radioModule;
		this.eTx = eTx;
		this.cETx = cETx;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setETx(eTx);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setETx(cETx);
	}

}
