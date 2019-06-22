package radio_module;

import device.SensorNode;
import interference.LoRaTransceiver;
import interference.WiFiTransceiver;
import interference.ZigBeeTransceiver;
import simulation.SimulationInputs;

public class ErrorBits {

	public static final int PROBABILITY = 0;
	public static final int ALPHA_D = 1;
	public static final int AWGN = 2;
	
	public static boolean errorBitsOk(String message, SensorNode transmitter, SensorNode receiver) {
		if (SimulationInputs.ack) {			
			if (SimulationInputs.ackType == PROBABILITY) {
				return (Math.random() <= SimulationInputs.ackProba?true:false);
			}
			
			if (SimulationInputs.ackType == ALPHA_D && transmitter.getStandard() == receiver.getStandard() && transmitter.getStandard() == RadioModule.ZIGBEE_802_15_4) {
				double errorBits = ZigBeeTransceiver.getNumberOfReceivedErrorBits(message, transmitter.getNeighbors().size(), (int) transmitter.getCurrentRadioModule().getRadioRangeRadius(), transmitter.getPerActiveNodes());
				return (errorBits == 0);
			}
			
			if (SimulationInputs.ackType == ALPHA_D && transmitter.getStandard() == receiver.getStandard() && transmitter.getStandard() == RadioModule.WIFI_802_11) {
				double errorBits = WiFiTransceiver.getNumberOfReceivedErrorBits(message, transmitter.getNeighbors().size(), (int) transmitter.getCurrentRadioModule().getRadioRangeRadius(), transmitter.getPerActiveNodes());
				return (errorBits == 0);
			}
			
			if (SimulationInputs.ackType == AWGN && transmitter.getStandard() == receiver.getStandard() && transmitter.getStandard() == RadioModule.LORA) {
				double errorBits = LoRaTransceiver.getNumberOfReceivedErrorBits(message, transmitter.getCurrentRadioModule().getSpreadingFactor(), transmitter.getCurrentRadioModule().getCodeRate());
				return (errorBits == 0);
			}
			System.err.println("[ErrorBits] -> This kind of interference is not considered!");
			return false;
		}		
		return true;
	}
	
}
