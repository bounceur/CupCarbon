package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_SPOP extends Command {

	protected String arg1 = "";
	protected String arg2 = null;
	protected String data = "";
	
	public Command_SPOP(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		char symbole = '&';
		data = sensor.getScript().getVariableValue("$"+arg2);
		WisenSimulation.simLog.add("S" + sensor.getId() + " SPOP: "+data);
		String v;
		String ret;
		if(data.indexOf(symbole)<0) {
			v = data;
			ret = "";
		}
		else {
			v = data.substring(0, data.indexOf(symbole));
			ret = data.substring(data.indexOf(symbole)+1);
		}
		
		//System.out.println("<<<< "+v+" "+ret);
		
		sensor.getScript().addVariable(arg1, v);
		sensor.getScript().addVariable(arg2, ret);
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "SPOP";
	}
		
}