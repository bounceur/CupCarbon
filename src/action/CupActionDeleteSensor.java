package action;

import device.DeviceList;
import device.SensorNode;

public class CupActionDeleteSensor extends CupAction {
	
	private SensorNode sensor;
	
	public CupActionDeleteSensor(SensorNode sensor) {
		super();
		this.sensor = sensor;
	}

	@Override
	public void execute() {
		DeviceList.delete(sensor);
	}

	@Override
	public void antiExecute() {
		DeviceList.add(sensor);
	}

}
