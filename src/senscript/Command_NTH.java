package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

public class Command_NTH extends Command {

	protected String data = "";
	protected String [] args = null;
	protected String s_nth = null;
	
	public Command_NTH(SensorNode sensor, String [] args) {
		this.sensor = sensor ;
		this.args = args ;
	}

	@Override
	public double execute() {
		try {
			int nth;
			String symbole = "&";
			if(args[1].charAt(0)=='!') {
				symbole = "" + args[1].charAt(1);
				data = sensor.getScript().getVariableValue(args[4]);
				nth = Double.valueOf(sensor.getScript().getVariableValue(args[3])).intValue();
			}
			else {
				data = sensor.getScript().getVariableValue(args[3]);
				nth = Double.valueOf(sensor.getScript().getVariableValue(args[2])).intValue();
			}
			WisenSimulation.simLog.add("S" + sensor.getId() + " NTH: "+data);
			String [] t = data.split(symbole);
			
			//System.out.println(Arrays.toString(t));
			
			int i = 1;
			if(args[1].charAt(0)=='!') {
				i = 2;
			}
			
			sensor.getScript().addVariable(args[i], t[nth]);
		}
		catch(Exception e) {
			System.err.println("ERROR NTH");
		}
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "NTH";
	}
	
}