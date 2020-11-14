package action;

import device.DeviceList;
import device.IoTRNode;

public class CupActionAddIoTRNode extends CupAction {	
	
	private IoTRNode iotRNode;
	
	public CupActionAddIoTRNode(IoTRNode iotRNode) {
		super();
		this.iotRNode = iotRNode;
	}

	@Override
	public void execute() {
		DeviceList.add(iotRNode);
	}

	@Override
	public void antiExecute() {
		DeviceList.delete(iotRNode);
	}

}
