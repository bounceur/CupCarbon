package action;

import device.DeviceList;
import device.SensorNode;

public class CupActionAddStdSensor extends CupAction {	
	
	private SensorNode sensor;
	
	public CupActionAddStdSensor(SensorNode sensor) {
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
