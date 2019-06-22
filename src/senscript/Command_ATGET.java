package senscript;

import device.SensorNode;
import radio_module.RadioModule;
import radio_module.XBeeFrameGenerator;
import simulation.WisenSimulation;

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
			WisenSimulation.simLog.add("S" + sensor.getId() + " ATGET ID.");
			v = ""+sensor.getId();
		}
		
		if(arg1.equals("ch")) {
			WisenSimulation.simLog.add("S" + sensor.getId() + " ATGET CH.");
			v = ""+sensor.getCurrentRadioModule().getCh();
		}
		
		if(arg1.equals("my")) {
			WisenSimulation.simLog.add("S" + sensor.getId() + " ATGET MY.");
			v = ""+sensor.getCurrentRadioModule().getMy();
		}
		
		if(arg1.equals("nid")) {
			WisenSimulation.simLog.add("S" + sensor.getId() + " ATGET NID.");
			v = ""+sensor.getCurrentRadioModule().getNId();
		}
		
		sensor.getScript().addVariable(arg2, v);
		//double ratio = (sensor.getRadioDataRate()*1.0)/(sensor.getUartDataRate());
		String message = "XX";
		//String answer = "XXXX"; //v; We assume that the answer contains 4 bytes
		
		String frame = message;
		if(sensor.getStandard() == RadioModule.ZIGBEE_802_15_4)
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
