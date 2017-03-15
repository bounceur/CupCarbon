package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioDataRate extends CupAction {	
	
	private RadioModule radioModule;
	private int dataRate;
	private int cDataRate;
	
	public CupActionModifRadioDataRate(RadioModule radioModule, int cDataRate, int dataRate) {
		super();
		this.radioModule = radioModule;
		this.dataRate = dataRate;
		this.cDataRate = cDataRate;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setRadioDataRate(dataRate);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setRadioDataRate(cDataRate);
	}

}
