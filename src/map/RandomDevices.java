package map;

import device.DeviceList;
import device.StdSensorNode;
import markers.Marker;
import markers.MarkerList;

public class RandomDevices {

	public static void addRandomSensors(int n) {
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		double m1x = marker1.getLongitude();
		double m1y = marker1.getLatitude();
		double m2x = marker2.getLongitude();
		double m2y = marker2.getLatitude();

		double r1 ;
		double r2 ;
		double x ;
		double y ;
		
		for (int i = 0; i < n; i++) {
			r1 = Math.random();
			r2 = Math.random();
			x = ((m2x-m1x)*r1)+m1x;
			y = ((m2y-m1y)*r2)+m1y;				
			DeviceList.add(new StdSensorNode(x, y, 0, 0, 100, 20, -1));
			MapLayer.getMapViewer().repaint();
		}
	}
	
}
