package script;

import arduino.XBeeFrameGenerator;
import wisen_simulation.SimLog;
import device.DataInfo;
import device.SensorNode;

public class Command_ATSM extends Command {

	protected String arg = "" ;
	
	public Command_ATSM(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public long execute() {
		SimLog.add("S" + sensor.getId() + " ATSM "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		sensor.setSleeping((Integer.valueOf(args)==0)?false:true);

		double ratio = (DataInfo.ChDataRate*1.0)/(DataInfo.UartDataRate);
		String message = "ATSM "+args;
		return (long)(Math.round(message.length()*8.*ratio));
		
		//return 0;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeFrameGenerator.at("SM"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATSM";
	}
}
