package radio_module;

import device.DeviceWithRadio;
import simulation.SimulationInputs;

public class RadioDetection {
	
	public static final int SIMPLE_DETECTION = 0;
	public static final int POWER_RECEPTION_DETECTION = 1;
	public static final int THREED_DETECTION = 2;
	public static final int REAL_DETECTION = 3;
	
	public static boolean simpleDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.canCommunicateWith(device2)) { 				
			if (SimulationInputs.symmetricalLinks)
				return (device1.contains(device2) || device2.contains(device1));
			else
				return (device1.contains(device2));
		}
		return false;
	}	
	
	public static boolean powerReceptionDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.canCommunicateWith(device2)) {
			if ( getPowerReception(device1, device2) > device2.getRequiredQuality()) {
				if (SimulationInputs.symmetricalLinks)
					return (device1.contains(device2) || device2.contains(device1)) ;
				else
					return device1.contains(device2);
			}
		}
		return false;
	}	
	
	public static boolean threeDDetection(DeviceWithRadio device1, DeviceWithRadio device2) {
		if (device1.canCommunicateWith(device2)) {
			if ( getPowerReception(((DeviceWithRadio)device1), ((DeviceWithRadio)device2)) > ((DeviceWithRadio)device2).getRequiredQuality())
				return true;
		}
		return false;
	}
	
	public static double getPowerReception(DeviceWithRadio device1, DeviceWithRadio device2) {
		double pr = device1.getTransmitPower() - getAttenuation(device1, device1.distance(device2));
		return pr ;
	}
	
	public static double getAttenuation(DeviceWithRadio device, double distance) {
		double f = 2.9979e+08;
		double lambda = f / device.getCurrentRadioModule().getFrequency();
		double attenuation = (20 * Math.log10(4 * Math.PI * distance / lambda)) ; 
		return attenuation ;
	}
	
}
