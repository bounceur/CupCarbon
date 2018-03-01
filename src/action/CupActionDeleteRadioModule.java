package action;

import cupcarbon.CupCarbon;
import device.SensorNode;
import radio_module.RadioModule;

public class CupActionDeleteRadioModule extends CupAction {	
	
	private SensorNode sensor;
	private RadioModule radioModule;
	
	public CupActionDeleteRadioModule(RadioModule radioModule) {
		super();
		this.sensor = radioModule.getSensorNode();
		this.radioModule = radioModule;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		sensor.removeRadioModule(radioModule);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		radioModule.getSensorNode().setSelected(true);
		
		sensor.addRadioModule(radioModule);		
	}

}
