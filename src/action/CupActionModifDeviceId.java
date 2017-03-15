package action;

import device.Device;

public class CupActionModifDeviceId extends CupAction {	
	
	private Device device;
	private int id;
	private int cId;
	
	public CupActionModifDeviceId(Device device, int cId, int id) {
		super();
		this.device = device;
		this.id = id;
		this.cId = cId;
	}

	@Override
	public void execute() {
		device.setId(id);
	}

	@Override
	public void antiExecute() {
		device.setId(cId);
	}

}
