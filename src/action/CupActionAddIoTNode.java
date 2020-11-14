package action;

import device.DeviceList;
import device.IoTNode;

public class CupActionAddIoTNode extends CupAction {	
	
	private IoTNode iotNode;
	
	public CupActionAddIoTNode(IoTNode iotNode) {
		super();
		this.iotNode = iotNode;
	}

	@Override
	public void execute() {
		DeviceList.add(iotNode);
	}

	@Override
	public void antiExecute() {
		DeviceList.delete(iotNode);
	}

}
