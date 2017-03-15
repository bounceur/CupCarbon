package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioSF extends CupAction {	
	
	private RadioModule radioModule;
	private int eSF;
	private int cSF;
	
	public CupActionModifRadioSF(RadioModule radioModule, int cSF, int eSF) {
		super();
		this.radioModule = radioModule;
		this.eSF = eSF;
		this.cSF = cSF;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setSpreadingFactor(eSF);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setSpreadingFactor(cSF);
	}

}
