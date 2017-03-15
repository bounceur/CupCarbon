package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioNId extends CupAction {	
	
	private RadioModule radioModule;
	private int nId;
	private int cNId;
	
	public CupActionModifRadioNId(RadioModule radioModule, int cNId, int nId) {
		super();
		this.radioModule = radioModule;
		this.nId = nId;
		this.cNId = cNId;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setNId(nId);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setNId(cNId);
	}

}
