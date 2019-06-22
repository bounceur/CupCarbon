package senscript;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;
import radio_module.RadioModule;
import radio_module.XBeeFrameGenerator;
import radio_module.XBeeToArduinoFrameGenerator;
import simulation.WisenSimulation;

public class Command_ATPL extends Command {

	protected String arg = "" ;
	
	public Command_ATPL(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " ATPL "+arg);
		String args = sensor.getScript().getVariableValue(arg);
		
		double v = Double.valueOf(args);

		sensor.setPl(v);
		
		MapLayer.repaint();
		if (DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		String message = "PL" + Integer.toHexString(Double.valueOf(v).intValue()).toUpperCase();
		
		String frame = message;
		if(sensor.getStandard() == RadioModule.ZIGBEE_802_15_4)
			frame = XBeeFrameGenerator.at(message);

		double ratio = 1.0/sensor.getUartDataRate();
		return (ratio*(frame.length()*8.)) ;
	}
	
	@Override
	public String getArduinoForm() {
		String s = XBeeToArduinoFrameGenerator.at("PL"+arg); 
		return s;
	}
	
	@Override
	public String toString() {
		return "ATPL";
	}
}
