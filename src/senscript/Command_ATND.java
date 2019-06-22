package senscript;

import java.util.List;

import device.SensorNode;
import radio_module.RadioModule;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import simulation.WisenSimulation;

public class Command_ATND extends Command {
	// ND: 
	protected String arg1 = "";
	protected String arg2 = "";
	protected double arg3 = 0.001;//6 seconds (6=0x3C) is the value of the parameter NT: Node Discovery Timeout;
	
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
		WisenSimulation.simLog.add("S" + sensor.getId() + " ATND "+arg1+" "+arg2+ "+arg3");
		List<SensorNode> snList = sensor.getSensorNodeNeighbors();		
		int n = snList.size();
		//System.out.println(n);
		
		sensor.getScript().addVariable(arg1, ""+n);
		
		if(!arg2.equals("")) {
			sensor.getScript().putVector(arg2, n);		
			String [] vector = sensor.getScript().getVector(arg2);
			for(int i=0; i<n; i++) {
				vector[i] = ""+snList.get(i).getId();
			}
		}
		
		String message = "ND";
		
		String frame = message;
		if(sensor.getStandard() == RadioModule.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		
		double ratio = 1.0/sensor.getUartDataRate();		
		return arg3 + (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.nd(); 
		return s;
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
