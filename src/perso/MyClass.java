package perso;

import java.util.List;

import device.DeviceList;
import device.SensorNode;

public class MyClass extends Thread {

	public MyClass() {
		
	}

	@Override
	public void run() {
		System.out.println("Hello CupCarbon!");
		
		// Visit all the sensor nodes :
		for(SensorNode sensor : DeviceList.sensors) {
			// The identifier :
			System.out.println(sensor.getId());
			
			// The name
			System.out.println(sensor.getName());
			
			// Mark the sensor node
			sensor.mark();
			
			// Wait 500 millisecond
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Unmark the sensor node
			sensor.unmark();
			
			// Wait 500 millisecond
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			
			// The list of neighbors
			List<SensorNode> neighbors = sensor.getNeighbors();
						
			System.out.println(neighbors);
			
			// The number of neighbors
			int n = neighbors.size();
			System.out.println(n);
			
			// The distances with neighbors (in meter)
			for(SensorNode sensor2 : neighbors) {
				double distance = sensor.distance(sensor2);
				System.out.println(sensor.getName() + " -- " + distance + " --> " + sensor2.getName());
				// Mark the edge (sensor,sensor2)
				DeviceList.markEdge(sensor, sensor2);
				// Wait 200 millisecond
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Unmark the edge (sensor,sensor2)
				DeviceList.unmarkEdge(sensor, sensor2);
				// Wait 200 millisecond
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// The same work with indices
		// Visit all the sensor nodes :
		for(int i=0; i<DeviceList.sensors.size(); i++) {
			SensorNode sensor = DeviceList.sensors.get(i);
			// The identifier :
			System.out.println(sensor.getId());
			
			// The name
			System.out.println(sensor.getName());
			
			// Mark the sensor node
			sensor.mark();
			
			// Wait 500 millisecond
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Unmark the sensor node
			sensor.unmark();
			
			// Wait 500 millisecond
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// The list of neighbors
			List<SensorNode> neighbors = sensor.getNeighbors();
			System.out.println(neighbors);
			
			// The number of neighbors
			int n = neighbors.size();
			System.out.println(n);
			
			// The distances with neighbors (in meter)
			for(int j=0; j<neighbors.size(); j++) {
				SensorNode sensor2 = neighbors.get(j);
				double distance = sensor.distance(sensor2);
				System.out.println(sensor.getName() + " -- " + distance + " --> " + sensor2.getName());
				
				// Mark the edge (sensor,sensor2)
				DeviceList.markEdge(sensor, sensor2);
				
				// Wait 200 millisecond
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Unmark the edge (sensor,sensor2)
				DeviceList.unmarkEdge(sensor, sensor2);

				// Wait 200 millisecond
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
