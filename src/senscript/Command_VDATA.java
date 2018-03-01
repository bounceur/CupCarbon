package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_VDATA extends Command {

	protected String arg1 = null;
	protected String arg2 = null;
	
	public Command_VDATA(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;		
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String tableName = sensor.getScript().getVariableValue(arg1);
		String data = sensor.getScript().getVariableValue(arg2);
		WisenSimulation.simLog.add("S" + sensor.getId() + " Put DATA " + data + " int the vector: " + tableName);
		String [] tab = data.split("#");
		sensor.getScript().putVector(tableName, tab.length);
		
		String [] vector = sensor.getScript().getVector(tableName);
		for(int i=0; i<tab.length; i++) {
			vector[i] = tab[i];
		}
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "VDATA";
	}
	
}