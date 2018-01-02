package senscript;

import cupcarbon.CupCarbon;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_RDATA extends Command {

	protected String data = "";
	protected String [] args = null;
	
	public Command_RDATA(SensorNode sensor, String [] args) {
		this.sensor = sensor ;		
		this.args = args ;
		//System.out.println(Arrays.toString(args));
	}

	@Override
	public double execute() {
		String symbole = "#";
		if(args[1].charAt(0)=='!') {
			symbole = "" + args[1].charAt(1);
			data = sensor.getScript().getVariableValue(args[2]);			
		}
		else
			data = sensor.getScript().getVariableValue(args[1]);
		if(data==null) {
			String errMessage = "[CupCarbon ERROR] (S"+sensor.getId()+"): The first argument of RDATA cannot be null";
			System.err.println(errMessage);
			CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
			throw new SenScriptException(errMessage);
		}
		else {
			WisenSimulation.simLog.add("S" + sensor.getId() + " Read DATA: "+data);
			String [] tab = data.split(symbole);
			int i = 2;
			int j = 2;
			if(args[1].charAt(0)=='!') {
				i=3;
				j=3;
			}
			while(i<args.length) {
				try {
					sensor.getScript().addVariable(args[i], tab[i-j]);
				} 
				catch(Exception e) {
					//if(data.length()>0)
					//	System.err.println("S"+sensor.getId()+" [ERROR RDATA: IDX OUT OF RANGE]! -> "+data);
				}
				i++;
			}
		}
		return 0 ;
	}

	@Override
	public String toString() {
		return "RDATA";
	}
	
}