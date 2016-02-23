package script;

import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;

public class Command_ATND extends Command {
	// ND: 
	protected String arg1 = "";
	protected double arg2 = 0.5;//6 seconds (6=0x3C) is the value of the parameter NT: Node Discovery Timeout;
	
	public Command_ATND(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}
	
	public Command_ATND(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg2 ;
	}

	@Override
	public double execute() {
		String args = arg1;
		int n = sensor.getSensorNodeNeighbors().size();
		sensor.getScript().addVariable(args, ""+n);
		
		String message = "ND";
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();		
		return arg2 + (ratio*(frame.length()*8.));
	}
	
	@Override
	public String toString() {
		return "ATND";
	}
	
	@Override
	public boolean isSend() {
		return true;
	}
	
	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " ATND: "+sensor.getSensorNodeNeighbors().size());
	}
}
