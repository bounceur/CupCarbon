/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
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

package solver;

import java.util.List;

import device.Device;
import device.DeviceList;
import device.SensorNode;
import graph.GraphStd;
import map.MapLayer;


/**
 * @author Ahcene Bounceur
 * @author Ali Benzerbadj
 * @version 1.0
 */
public class NetworkBorder {

	public void execute() {
//		System.out.println("---------------------------");
//		System.out.println("---------------------------");
//		System.out.println("---------------------------");
		// From sensors to Graph
		GraphStd graphe = null;		
		List<SensorNode> nodes = DeviceList.sensors;		
		graphe = SensorGraph.toSensorGraph(nodes, DeviceList.sensorListSize());		
		
		for (int i = 0; i < graphe.size(); i++) {
			nodes.get(i).setValue(0);
			nodes.get(i).setMarked(false);
		}
		
		Device n1, n2 ;
		int rank = 0;
		
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getLongitude() < n2.getLongitude()) && (n1.getLatitude() < n2.getLatitude())) {
					rank++;
				}
			}
			n1.setValue(rank);
			//System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		double min = 10000000;
		double rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getValue();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getValue()<=min)
				nodes.get(i).setMarked(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------		
		
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getLongitude() > n2.getLongitude()) && (n1.getLatitude() < n2.getLatitude())) {
					rank++;
				}
			}
			n1.setValue(rank);
			//System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		min = 10000000;
		rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getValue();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getValue()<=min)
				nodes.get(i).setMarked(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------				
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getLongitude() > n2.getLongitude()) && (n1.getLatitude() > n2.getLatitude())) {
					rank++;
				}
			}
			n1.setValue(rank);
			System.out.println(n1.getName()+" : "+rank);
		}
		
		min = 10000000;
		rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getValue();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getValue()<=min)
				nodes.get(i).setMarked(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------		
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getLongitude() < n2.getLongitude()) && (n1.getLatitude() > n2.getLatitude())) {
					rank++;
				}
			}
			n1.setValue(rank);
			//System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		min = 10000000;
		rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getValue();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getValue()<=min)
				nodes.get(i).setMarked(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------
		//------------------------------------------------------------
		//------------------------------------------------------------
		
		
		
		//------------------------------------------------------------
		// Begin : example 

//		for (int i = 0; i < graphe.size(); i++) {
//			nodes.get(i).setAlgoSelect(true);
//		}
		// End : example
		//------------------------------------------------------------
		
		
		// Update sensors (coloring)
		MapLayer.repaint();
	}
	
}
