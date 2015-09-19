package script;

import arduino.XBeeFrameGenerator;
import wisen_simulation.SimLog;
import device.DataInfo;
import device.SensorNode;

public class Command_ATNID extends Command {

	protected String arg = "" ;
	
	public Command_ATNID(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public int execute() {
		SimLog.add("S" + sensor.getId() + " ATID "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.setNId(Integer.valueOf(args));

		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		String message = "ATID "+args;
		return (int)(Math.round(message.length()*8.*ratio));
		
		//return 0;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeFrameGenerator.at("ID"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "NID";
	}
}
