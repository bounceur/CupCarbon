package script;

import device.SensorNode;
import radio_module.Standard;
import radio_module.XBeeFrameGenerator;
import wisen_simulation.SimLog;

public class Command_ATGET extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_ATGET(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String v = "" ;
		
		if(arg1.equals("id")) {
			SimLog.add("S" + sensor.getId() + " ATGET ID.");
			//System.out.println(node.getId());
			v = ""+sensor.getId();
		}
		
		if(arg1.equals("ch")) {
			SimLog.add("S" + sensor.getId() + " ATGET CH.");
			v = ""+sensor.getCh();
		}
		
		if(arg1.equals("my")) {
			SimLog.add("S" + sensor.getId() + " ATGET MY.");
			v = ""+sensor.getMy();
		}
		
		if(arg1.equals("nid")) {
			SimLog.add("S" + sensor.getId() + " ATGET NID.");
			v = ""+sensor.getNId();
		}
		
		sensor.getScript().addVariable(arg2, v);
		//double ratio = (sensor.getRadioDataRate()*1.0)/(sensor.getUartDataRate());
		String message = "XX";
		//String answer = "XXXX"; //v; We assume that the answer contains 4 bytes
		
		String frame = message;
		if(sensor.getStandard() == Standard.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);
		String answer = frame;
		
		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(((answer.length()+frame.length())*8.)));
		//return (long)(Math.round((answer.length()+message.length())*8.0*ratio));
	}

	@Override
	public String toString() {
		return "ATGET";
	}
}
