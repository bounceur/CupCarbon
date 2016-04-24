package script;

import java.util.List;

import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;

public class Command_ATND extends Command {
	// ND: 
	protected String arg1 = "";
	protected String arg2 = "";
	protected double arg3 = 0.5;//6 seconds (6=0x3C) is the value of the parameter NT: Node Discovery Timeout;
	
	public Command_ATND(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = "" ;
	}
	
	public Command_ATND(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		//String args = arg1;
		List<SensorNode> snList = sensor.getSensorNodeNeighbors();
		
		int n = snList.size();
		sensor.getScript().addVariable(arg1, ""+n);
		sensor.getScript().putTable(arg2, n, 1);
		
		for(int i=0; i<n; i++) {
			Object [][] tab = sensor.getScript().getTable(arg2);
			tab[i][0] = ""+snList.get(i).getId();
		}
		
		String message = "ND";
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();		
		return arg3 + (ratio*(frame.length()*8.)) ;
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
