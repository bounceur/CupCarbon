package action;

import device.DeviceList;
import device.SensorNode;

public class CupActionAddSensor extends CupAction {	
	
	private SensorNode sensor;
	
	public CupActionAddSensor(SensorNode sensor) {
		super();
		this.sensor = sensor;
	}

	@Override
	public void execute() {
		DeviceList.add(sensor);
	}

	@Override
	public void antiExecute() {
		DeviceList.delete(sensor);
	}

}
