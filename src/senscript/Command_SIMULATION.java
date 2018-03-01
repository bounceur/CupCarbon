package senscript;

import cupcarbon.CupCarbon;
import device.SensorNode;
import javafx.application.Platform;
import simulation.SimulationInputs;
import simulation.WisenSimulation;

public class Command_SIMULATION extends Command {

	protected String arg1 = "";
	protected String arg2 = "";	
	
	public Command_SIMULATION(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		
		WisenSimulation.simLog.add("S" + sensor.getId() + " SIMULATION "+arg1+" "+arg2);
		
		if(arg1.equals("sspeed")) {			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					SimulationInputs.visualDelay = Integer.parseInt(arg2);
					CupCarbon.cupCarbonController.simulationSpeedTextField.setText(arg2);
				}
			});
		}
		else {
			if(arg1.equals("aspeed")) {				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						SimulationInputs.arrowsDelay = Integer.parseInt(arg2);
						CupCarbon.cupCarbonController.arrowSpeedTextField.setText(arg2);
					}
				});
			}
			else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						SimulationInputs.visualDelay = Integer.parseInt(arg1);
						SimulationInputs.arrowsDelay = Integer.parseInt(arg2);
						CupCarbon.cupCarbonController.simulationSpeedTextField.setText(arg1);
						CupCarbon.cupCarbonController.arrowSpeedTextField.setText(arg2);
					}
				});				
			}
		}
		
		
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "PICK";
	}
	
}
