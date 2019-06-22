package senscript;

import device.SensorNode;
import radio_module.RadioModule;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import simulation.WisenSimulation;

public class Command_ATNID extends Command {

	protected String arg = "" ;
	
	public Command_ATNID(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " ATID "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.getCurrentRadioModule().setNId(Integer.valueOf(args));

		String message = "ID" + Integer.toHexString(Integer.parseInt(args)).toUpperCase();
		
		String frame = message;
		if(sensor.getStandard() == RadioModule.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);

		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("ID"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "NID";
	}
}
