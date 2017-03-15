package senscript;

import wisen_simulation.SimLog;
import device.DeviceList;
import device.SensorNode;

public class Command_RADIO extends Command {

	protected String radioName = "";
	
	public Command_RADIO(SensorNode sensor, String radioName) {
		this.sensor = sensor ;
		this.radioName = radioName ;
	}

	@Override
	public double execute() {
		String sRadioName = sensor.getScript().getVariableValue(radioName);

		SimLog.add("S" + sensor.getId() + " Radio module "+sRadioName+" selected");
		
		sensor.selectCurrentRadioModule(sRadioName);
		
		if(DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "RADIO "+radioName;
	}
}
