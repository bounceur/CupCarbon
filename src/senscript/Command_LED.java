package senscript;

import arduino.BeginInstructions;
import device.SensorNode;
import simulation.WisenSimulation;
import utilities.UColor;

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
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " LED "+arg1+" -> Color = "+arg2);
		int ledColor = Double.valueOf(sensor.getScript().getVariableValue(arg2)).intValue();
		if(ledColor>UColor.colorTab.length-1)
			ledColor = (ledColor % (UColor.colorTab.length))+1;
		else
			ledColor = ledColor % (UColor.colorTab.length+1);
		sensor.setLedColor(ledColor);
		return 0 ;
	}

	@Override
	public String getArduinoForm() {
		BeginInstructions.add("pinMode("+arg1+", OUTPUT);");
		String s = "";
		String v = "'"+arg2.charAt(0)+"'";
		if(arg2.charAt(0)=='$') v = arg2.substring(1)+".charAt(0)";
		
		s = "\tdigitalWrite(13, 1-('1'-"+v+"));";
		
		return s;
	}
	
	@Override
	public String toString() {
		return "LED";
	}
	
}
