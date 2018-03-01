package perso;

import java.util.List;

import device.DeviceList;
import device.SensorNode;

public class Poly  extends Thread {

	public void run() {
		List<SensorNode> capteurs = DeviceList.sensors;
		
		for(SensorNode capteur : capteurs) {
			System.out.print(capteur.getId()+ " -> ");
			for(SensorNode voisin : capteurs) {
				if(capteur.radioDetect(voisin)) {
					System.out.print(voisin.getId()+" ");
				}
			}
			System.out.println();
		}
	}
}
