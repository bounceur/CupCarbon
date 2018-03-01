package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioCh extends CupAction {	
	
	private RadioModule radioModule;
	private int ch;
	private int cCh;
	
	public CupActionModifRadioCh(RadioModule radioModule, int cCh, int ch) {
		super();
		this.radioModule = radioModule;
		this.ch = ch;
		this.cCh = cCh;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setCh(ch);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setCh(cCh);
	}

}
