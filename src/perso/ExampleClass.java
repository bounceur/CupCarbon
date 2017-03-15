package perso;

import java.util.List;

import device.Device;
import device.DeviceList;
import device.SensorNode;

public class ExampleClass extends Thread {
		
	protected List<SensorNode> nodes; 
	
	@Override
	public void run() {
		// Get the node list
		nodes = DeviceList.sensors;

		SensorNode s1 = (SensorNode) nodes.get(0);
		//SensorNode s1 = (SensorNode) nodes.get(0);
		s1.loadRouteFromFile();
		s1.fixori();
		//int i=0;
		while(s1.hasNext()) {
			//System.out.println(s1.getEvent2());
			System.out.println(s1.getNextTime());
			s1.moveToNext(true, 50);
		}
		
		System.out.println("finish");
	}
	
	public void displayNumberOfSensors() {
		System.out.println(nodes.size());
	}
	
	public void displaySensorList() {
		for(Device node : nodes) {
			System.out.print(node.getId()+" : " );
			System.out.println(node.getLongitude()+" "+node.getLatitude());
		}
	}
	
	public void delay(int t) {
		try {
			sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
