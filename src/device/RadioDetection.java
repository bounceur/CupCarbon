package device;

import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import map.MapLayer;

public class RadioDetection {
	
	public static final int SIMPLE_DETECTION = 0;
	public static final int POWER_RECEPTION_DETECTION = 1;
	public static final int THREED_DETECTION = 2;
	public static double frequency = 2.4e9; // GHz
	
	public static boolean simpleDetection(Device device1, Device device2) {
		if (device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			GeoPosition gp = new GeoPosition(device2.getLongitude(), device2.getLatitude());
			Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
			return (device1.getRadioPolygon().contains(p1));
		}
		return false;
	}

	public static boolean threeDDetection(Device device1, Device device2) {
		if (	device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			if ( (getPowerReception(((DeviceWithRadio)device1), ((DeviceWithRadio)device2)) > ((DeviceWithRadio)device2).getRequiredQuality()))
				return true;
		}
		return false;
	}
	
	public static boolean powerReceptionDetection(Device device1, Device device2) {
		if (	device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			if ( (getPowerReception(((DeviceWithRadio)device1), ((DeviceWithRadio)device2)) > ((DeviceWithRadio)device2).getRequiredQuality()))
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
	
	public static double getPowerReception(DeviceWithRadio device1, DeviceWithRadio device2) {
		double pr = device1.getTransmitPower() - getAttenuation(device1.distance(device2));
		return pr ;
	}
	
}
