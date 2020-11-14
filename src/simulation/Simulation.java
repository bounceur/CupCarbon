package simulation;

import map.MapLayer;

public class Simulation {

	public static boolean simulating = false;
	
	public static void setSimulating(boolean sim) {
		simulating = sim;
		MapLayer.repaint();
	}
	
	public boolean getSimulating() {
		return simulating ;
	}
	
}
