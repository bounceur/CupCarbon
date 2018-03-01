/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

// Nearest Polar Connected Node : NPCN Algorithm

package solver;

import java.util.List;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public class CycleFromNode extends Thread {
 
	protected int delayTime = 200;
	
	@Override	
	public void run() {
		List<SensorNode> nodes = DeviceList.sensors;		
		DeviceList.initAll();
		DeviceList.addHull();
		DeviceList.initLastHull();
		
		SensorNode n;		
		n = nodes.get(0);
		System.out.println(cycle(false, n, n));
		//System.out.println("CYCLE : Algo finish");
		DeviceList.addToLastHull(DeviceList.sensors.indexOf(n));
		MapLayer.repaint();
	}
	
	public boolean cycle(boolean result, SensorNode n, SensorNode refNode) {
		if(result) return true;
		System.out.println(n+" "+refNode);
		delay();
		if(n.isMarked()) {
			if(n==refNode) {
				return true;
			}
			else 
				return false;
		}
		else {
			n.setMarked(true);
			DeviceList.addToLastHull(DeviceList.sensors.indexOf(n));
			MapLayer.repaint();
			SensorNode next;
			while((next = getNextUnmarkedNode(n, refNode)) != null) {
				System.out.println(n+" -> "+next);
				if (next==refNode) {
					System.out.println("A");
					cycle(true, next, refNode);
					//return true;
				}
				else
				//if (next==null) {
				//	return false;
				//}
					cycle(false, next, refNode);
			}
			return false;
		}
	}
	
	int b = 0;
	
	public SensorNode getNextUnmarkedNode(SensorNode n, SensorNode ref) {
		SensorNode next = null;
		for(SensorNode node : n.getNeighbors()) {
			if(b>1) if(node==ref) return node;
			if(!node.isMarked()) next = node ;
		}
		b++;
		return next;
	}


	public void delay() {
		try {
			sleep(delayTime);
		} catch (InterruptedException e) {
		}
	}
	
}
