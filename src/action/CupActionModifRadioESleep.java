package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioESleep extends CupAction {	
	
	private RadioModule radioModule;
	private double eSleep;
	private double cESleep;
	
	public CupActionModifRadioESleep(RadioModule radioModule, double cESleep, double eSleep) {
		super();
		this.radioModule = radioModule;
		this.eSleep = eSleep;
		this.cESleep = cESleep;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setESleep(eSleep);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setESleep(cESleep);
	}

}
