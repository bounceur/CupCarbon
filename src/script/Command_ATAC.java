package script;

import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import wisen_simulation.SimLog;

public class Command_ATAC extends Command {

	public Command_ATAC(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {
		SimLog.add("S" + sensor.getId() + " ATMY ");
		String message = "AC";
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {		
		String s = XBeeToArduinoFrameGenerator.atac(); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATAC";
	}

}
