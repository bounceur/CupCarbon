package action;

import cupcarbon.CupCarbon;
import radio_module.RadioModule;

public class CupActionModifRadioEListen extends CupAction {	
	
	private RadioModule radioModule;
	private double eListen;
	private double cEliten;
	
	public CupActionModifRadioEListen(RadioModule radioModule, double cEListen, double eListen) {
		super();
		this.radioModule = radioModule;
		this.eListen = eListen;
		this.cEliten = cEListen;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setEListen(eListen);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		radioModule.setEListen(cEliten);
	}

}
