package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioCR extends CupAction {	
	
	private RadioModule radioModule;
	private int eCR;
	private int cCR;
	
	public CupActionModifRadioCR(RadioModule radioModule, int cCR, int eCR) {
		super();
		this.radioModule = radioModule;
		this.eCR = eCR;
		this.cCR = cCR;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setCodeRate(eCR);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setCodeRate(cCR);
	}

}
