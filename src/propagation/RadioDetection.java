package propagation;

import device.Device;
import device.DeviceWithRadio;
import wisen_simulation.SimulationInputs;

public class RadioDetection {
	
	public static final int SIMPLE_DETECTION = 0;
	public static final int POWER_RECEPTION_DETECTION = 1;
	public static final int THREED_DETECTION = 2;
	public static double frequency = 2.4e9; // GHz
	
	public static boolean simpleDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.withRadio() && device2.withRadio() && device1.getNId() == device2.getNId() && device1.getCh() == device2.getCh()) {
			if (SimulationInputs.symmetricalLinks)
				return (device1.contains(device2) || device2.contains(device1));
			else
				return (device1.contains(device2));
		}
		return false;
	}

	public static boolean powerReceptionDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.withRadio() && device2.withRadio() && device1.getNId() == device2.getNId() && device1.getCh() == device2.getCh()) {
			if ( getPowerReception(device1, device2) > device2.getRequiredQuality())
				return device1.contains(device2);
		}
		return false;
	}	
	
	public static boolean threeDDetection(Device device1, Device device2) {
		if (device1.withRadio() && device2.withRadio() && device1.getNId() == device2.getNId() && device1.getCh() == device2.getCh()) {
			if ( getPowerReception(((DeviceWithRadio)device1), ((DeviceWithRadio)device2)) > ((DeviceWithRadio)device2).getRequiredQuality())
				return true;
		}
		return false;
	}
	
	//----------------
	
	public static double getAttenuation(double distance) {
		double c = 2.9979e+08;
		double lambda = c / frequency;
		double attenuation = (20 * Math.log10(4 * Math.PI * distance / lambda)) ; 
		return attenuation ;
	}
	
	public static double getPowerReception(DeviceWithRadio device1, DeviceWithRadio device2) {
		double pr = device1.getTransmitPower() - getAttenuation(device1.distance(device2));
		return pr ;
	}
	
}
