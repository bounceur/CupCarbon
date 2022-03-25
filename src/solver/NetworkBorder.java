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

	Device n1, n2 ;
	int rank = 0;
	
	//Declare the variables in local scope of the code block.
	double min;
	double rmin;

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
		
		
		//Refactoring technique -- Extract method
		//Extract the code block and create a new method with name suiting to its intent.
		
		
		dvc2latlongmax(graphe, nodes);
		
		dvc1longdvc2latmax(graphe, nodes);
		
		dvc1latlongmax(graphe, nodes);
		
		dvc1latdvc2longmax(graphe, nodes);
		
		
	
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


	//Created new methods  for device1 and device2 to find the latitude and longitiude max values 
	//which  suited the purpose of the task performed as part of extract method refactoring technique
	
		//Device2 consisting of max latitude and longitude
	private void dvc2latlongmax(GraphStd graphe, List<SensorNode> nodes) {
		Device n1;
		Device n2;
		int rank;
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
	}
	
	//Device1 consisting of max longitude  and device2  having max latitude
	private void dvc1longdvc2latmax(GraphStd graphe, List<SensorNode> nodes) {
		Device n1;
		Device n2;
		int rank;
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
	}
	
	//Device1 consisting of max longitude and latitude
	
	private void dvc1latlongmax(GraphStd graphe, List<SensorNode> nodes) {
		Device n1;
		Device n2;
		int rank;
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
	}

	//Device1 consisting of max latitude  and device2  having max longitude
	
	private void dvc1latdvc2longmax(GraphStd graphe, List<SensorNode> nodes) {
		Device n1;
		Device n2;
		int rank;
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
	}


	
}
