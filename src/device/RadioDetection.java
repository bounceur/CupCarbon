package device;

import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import map.Layer;

public class RadioDetection {
	
	public static final int SIMPLE_DETECTION = 0;
	public static final int POWER_RECEPTION_DETECTION = 1;	
	public static double frequency = 2.4e9; // GHz
	
	public static boolean simpleDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			GeoPosition gp = new GeoPosition(device2.getLongitude(), device2.getLatitude());
			Point2D p1 = Layer.getMapViewer().getTileFactory().geoToPixel(gp, Layer.getMapViewer().getZoom());
			//Polygon poly = new Polygon(polyX[0],polyY[0],nPoint);
			return (device1.getRadioPolygon().contains(p1));
		}
		return false;
	}

	public static boolean powerReceptionDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (	device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			if ( (getPowerReception(device1, device2) > device2.getRequiredQuality()))
				return true;
		}
		return false;
	}
	
	public static double getAttenuation(double d) {
		double c = 2.9979e+08;
		double lambda = c/frequency;
		double att = (20*Math.log10(4*Math.PI*d/lambda)) ; 
		return att ;
	}
	
//	public double getTransmitPower() {
//		return transmitPower;
//	}
	
	public static double getPowerReception(DeviceWithRadio device1, DeviceWithRadio device2) {
		double pr = device1.getTransmitPower() - getAttenuation(device1.distance(device2));
		return pr ;
	}
	
}
