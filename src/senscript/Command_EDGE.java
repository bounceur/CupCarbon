package senscript;

import cupcarbon.CupCarbon;
import device.DeviceList;
import device.SensorNode;
import simulation.WisenSimulation;

public class Command_EDGE extends Command {

	protected String arg1 = "" ;
	protected String arg2 = "" ;
	
	public Command_EDGE(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		if(arg1.equals("")) {
			System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): EDGE function ("+arg1+" is null)");
			CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
		}
		if(arg2.equals("")) {
			System.err.println("[CupCarbon ERROR] (S"+sensor.getId()+"): EDGE function ("+arg2+" is null)");
			CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
		}
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " EDGE "+arg1+ " "+ arg2);
		
		int v = Integer.parseInt(sensor.getScript().getVariableValue(arg1));
		int id = Integer.parseInt(sensor.getScript().getVariableValue(arg2));		
		if(id>0) {
			if(sensor.getId()!=id) {
				SensorNode sensor2 = DeviceList.getSensorNodeById(id);
				if(v==1)
					DeviceList.addEdge(sensor, sensor2);
				else
					DeviceList.removeEdge(sensor, sensor2);
			}
		}
		else {
			System.out.println("[CupCarbon WARNING] (S"+sensor.getId()+"): EDGE function (id must be >0) ... id="+id);
		}
		return 0 ;
	}

	@Override
	public String toString() {
		return "EDGE";
	}
	
}
