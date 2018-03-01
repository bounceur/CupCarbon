package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifSensorCurrentRadio extends CupAction {	
	
	private SensorNode sensor;
	private String name;
	private String cName;
	
	public CupActionModifSensorCurrentRadio(SensorNode sensor, String cName, String name) {
		super();
		this.sensor = sensor;
		this.name = name;
		this.cName = cName;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		sensor.setSelected(true);
		
		sensor.selectCurrentRadioModule(name);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.radioParamPane.isExpanded())
			CupCarbon.cupCarbonController.radioParamPane.setExpanded(true);
		sensor.setSelected(true);
		
		sensor.selectCurrentRadioModule(cName);
	}

}
