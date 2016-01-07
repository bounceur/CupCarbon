package script;

import utilities.UColor;
import wisen_simulation.SimLog;
import map.MapLayer;
import device.SensorNode;

public class Command_LED extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	// Note: arg1 is the number of the pin: not used in simulation
	// but it is useful for the code generation to determine which pin
	// will be used for the led
	
	public Command_LED(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public long execute() {
		SimLog.add("S" + sensor.getId() + " LED "+arg1+" -> Color = "+arg2);
		int ledColor = Double.valueOf(sensor.getScript().getVariableValue(arg2)).intValue();
		if(ledColor>UColor.colorTab.length) ledColor= ledColor % UColor.colorTab.length;
		sensor.setLedColor(ledColor);
		MapLayer.getMapViewer().repaint();
		return 0;
	}

	@Override
	public String toString() {
		return "LED";
	}
	
}
