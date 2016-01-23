package cupcarbon;

import map.MapLayer;

public class Refresher extends Thread {

	
	@Override
	public void run() {
		for(int i=0; i<10; i++) {
			MapLayer.getMapViewer().repaint();
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
