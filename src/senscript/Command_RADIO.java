package senscript;

import device.DeviceList;
import device.SensorNode;
import simulation.SimulationInputs;
import simulation.WisenSimulation;
import visibility.VisibilityLauncher;

public class Command_RADIO extends Command {

	protected String radioName = "";
	
	public Command_RADIO(SensorNode sensor, String radioName) {
		this.sensor = sensor ;
		this.radioName = radioName ;
	}

	@Override
	public double execute() {
		String sRadioName = sensor.getScript().getVariableValue(radioName);

		WisenSimulation.simLog.add("S" + sensor.getId() + " RADIO module "+sRadioName+" selected");
		
		sensor.selectCurrentRadioModule(sRadioName);
		
		if(SimulationInputs.visibility)
			VisibilityLauncher.calculate(sensor);
		
		if(DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "RADIO "+radioName;
	}
}
