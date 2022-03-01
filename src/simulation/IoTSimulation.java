package simulation;

import device.Device;
import device.DeviceList;
import device.IoTNode;
import device.IoTRNode;
import device.IoTThreadMonitor;
import device.SensorNode;

public class IoTSimulation implements Runnable {

	public void startIoTSimulation() {
		
		
	}
	
	/*public void init() {
		DeviceList.initAll();
		for (SensorNode sensor : DeviceList.sensors) {
			if(sensor.getType()==Device.IOT) {
				sensor.initForSimulation();
			}
		}
	}*/
	
	public boolean ready() {
		for(SensorNode iotNode : DeviceList.sensors) {
			if(iotNode.getType()==Device.IOT) {
				if(iotNode.getScriptFileName().equals("")) {
					return false; 
				}
			}
		}
		return true;
	}
	
	@Override
	public void run() {
		//MapLayer.showInfos = false;
		//MapLayer.repaint();
		System.out.println("Starting IoT Simulation");
		
		DeviceList.initAll();
		System.out.println("Starting IoT Simulation 2");
		for (SensorNode sensor : DeviceList.sensors) {
			if(sensor.getType()==Device.IOT) {
				System.out.println(sensor.getRoute());
				sensor.initForSimulation();
			}
		}
		
		IoTThreadMonitor thMonitor = new IoTThreadMonitor(DeviceList.getIoTNodeSize());
		Simulation.setSimulating(true);
		
		for(SensorNode iotNode : DeviceList.sensors) {
			if(iotNode.getType()==Device.RIOT) {
				((IoTRNode)iotNode).runIoTScript();
			}
			if(iotNode.getType()==Device.IOT) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						((IoTNode)iotNode).setThreadMonitor(thMonitor);
						((IoTNode)iotNode).runIoTScript();
						((IoTNode)iotNode).runGPS();
					}
				}).start();
			}
		}
	}
		
}
