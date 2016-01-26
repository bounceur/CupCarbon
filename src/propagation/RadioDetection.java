package propagation;

import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import device.DeviceWithRadio;
import map.MapLayer;
import wisen_simulation.SimulationInputs;

public class RadioDetection {
	
	public static final int SIMPLE_DETECTION = 0;
	public static final int POWER_RECEPTION_DETECTION = 1;
	public static final int THREED_DETECTION = 2;
	public static double frequency = 2.4e9; // GHz
	
	public static boolean simpleDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			GeoPosition gp1 = new GeoPosition(device1.getLatitude(), device1.getLongitude());
			GeoPosition gp2 = new GeoPosition(device2.getLatitude(), device2.getLongitude());
			Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp1, MapLayer.getMapViewer().getZoom());
			Point2D p2 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp2, MapLayer.getMapViewer().getZoom());
			if (SimulationInputs.symmetricalLinks)
				return (device1.getRadioPolygon().contains(p2) || device2.getRadioPolygon().contains(p1));
				//return (device1.contains(p2) || device2.contains(p1));
			else
				//return (device1.contains(p2));
				return (device1.getRadioPolygon().contains(p2));
		}
		return false;
	}

	public static boolean threeDDetection(Device device1, Device device2) {
		if (	device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
			if ( getPowerReception(((DeviceWithRadio)device1), ((DeviceWithRadio)device2)) > ((DeviceWithRadio)device2).getRequiredQuality())
				return true;
		}
		return false;
	}
	
	public static boolean powerReceptionDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (	device1.withRadio() && device2.withRadio() && 
				device1.getNId() == device2.getNId() &&
				device1.getCh() == device2.getCh()
		) {
//			System.out.println(getPowerReception(((DeviceWithRadio)device1), ((DeviceWithRadio)device2)));
//			System.out.println(((DeviceWithRadio)device2).getRequiredQuality());
//			System.out.println("-------");
			if ( getPowerReception(device1, device2) > device2.getRequiredQuality())
				if(device1.contains(device2))
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
