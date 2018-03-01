package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioConsoTxModel extends CupAction {	
	
	private RadioModule radioModule;
	private String eCM;
	private String cCM;
	
	public CupActionModifRadioConsoTxModel(RadioModule radioModule, String cCM, String eCM) {
		super();
		this.radioModule = radioModule;
		this.eCM = eCM;
		this.cCM = cCM;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setRadioConsoTxModel(eCM);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setRadioConsoTxModel(cCM);
	}

}
