package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_WHILE extends Command {
	
	protected String arg = ""; 
	
	protected String left = "";
	protected String right = "";
	protected String ineq = "";
	
	protected int index;
	
	protected Command_WHILE parent = null;
		
	public Command_WHILE getParent() {
		return parent;
	}


	public void setParent(Command_WHILE parent) {
		this.parent = parent;
	}

	public Command_WHILE(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg;
		
		String[] inst = Condition.getTwoParts(arg); 			

		left = inst[0];
		right = inst[1];
		ineq = inst[2];
	}
	
	public String getRight() {
		return right;
	}
	
	public String getLeft() {
		return left;
	}
	
	public String getIneq() {
		return ineq;
	}
	
	@Override
	public int execute() {
		SimLog.add("S" + sensor.getId() + " WHILE ");
		index = sensor.getScript().getIndex();
		return 0;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getArg() {
		return arg; 
	}

	@Override
	public String toString() {
		return "WHILE";
	}
}
