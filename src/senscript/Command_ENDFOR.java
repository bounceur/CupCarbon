package senscript;

import device.SensorNode;
//H
public class Command_ENDFOR extends Command {
	
	protected boolean trueCondition = true ;	
	protected int index ;

	public Command_ENDFOR(SensorNode sensor) {
		this.sensor = sensor ;
	}

	@Override
	public double execute() {		
		//WisenSimulation.simLog.add("S" + sensor.getId() + " END FOR.");
		
		SenScript script = sensor.getScript();
		
		Command_FOR cmdFor =  this.getCurrentFor();
		if (cmdFor.getTrueCondition())
			script.setIndex(cmdFor.getIndex()-1);
		
		return 0 ;
	}
	
	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	
	public boolean isTrueCondition() {
		return trueCondition;
	}

	@Override
	public String getArduinoForm() {
		return "}";
	}
	
	@Override
	public String toString() {
		return "ENDFOR";
	}
}