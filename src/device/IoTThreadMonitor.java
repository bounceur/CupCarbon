/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: IoT Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2021 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * 
 * Monitor helps to detect the end of simulation
 * Once all IoTNodes (threads) finished the run section (simulation) we increment the value of n 
 * 	n = total number of IoTNodes
 * 	i = number of IoTNodes having finished their simulation
 * 
 * ----------------------------------------------------------------------------------------------------------------
 **/

package device;

import cupcarbon.CupCarbon;
import map.MapLayer;
import simulation.Simulation;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class IoTThreadMonitor {
	private int n = 0;
	private int i = 0;
	
	public IoTThreadMonitor(int n) {
		this.n = n;
	}
	
	public synchronized void inc(IoTNode iotNode) {
		i++;
		System.out.println("Node "+iotNode.getId()+": simulation finished ");		
		if(i==n) {
			Simulation.setSimulating(false);
			System.out.println("End of IoT Simulation");
			CupCarbon.cupCarbonController.displayShortGoodMessage("End of Simulation");
			CupCarbon.cupCarbonController.qRunIoTSimulationButton.setDisable(false);
			for(SensorNode iotRNode : DeviceList.sensors) {
				if(iotRNode.getType()==Device.RIOT) {
					((IoTRNode)iotRNode).setConnected(false);
					((IoTRNode)iotRNode).publishStopSimulation();
				}
			}
			MapLayer.repaint();
		}
	}
	
	public synchronized int getI() {
		return i;
	}
}
