/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2017 CupCarbon
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
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 * This Class allows to calculate for a given sensor node a simple visibility based on a 2D environment
 **/

package visibility;

import java.util.ArrayList;
import java.util.LinkedList;

import buildings.Building;
import buildings.BuildingList;
import device.SensorNode;
import geo_objects.GeoZone;
import geo_objects.GeoZoneList;
import map.MapLayer;
import math.Angle;

/**
 * @author Ahcene Bounceur
 * @version 1
 */

public class VisibilityZones2 extends Thread {	
	
	//-----------------------------------------------------------------------------------------
	// The concerned Sensor Node
	//-----------------------------------------------------------------------------------------
	private SensorNode sensorNode ;
	
	//-----------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------
	public VisibilityZones2(SensorNode sensorNode) {
		this.sensorNode = sensorNode;
	}	

	//-----------------------------------------------------------------------------------------
	// Running the process of calculation the visibility region (zone)
	//-----------------------------------------------------------------------------------------
	@Override
	public void run() {
		//-----------------------------------------------------------------------------------------
		// The list of the points of the final visibility zone
		//-----------------------------------------------------------------------------------------
		ArrayList<double []> visibilityPointList = new ArrayList<double []>();

		//-----------------------------------------------------------------------------------------
		// The zone of interest
		// We assume that the visibility is not infinite and we take a certain radius around 
		// the center of the sensor node
		// Only the buildings that are around this region will be taken into account
		// The other ones will be ignored for the current sensor node
		//-----------------------------------------------------------------------------------------
		int nPoint = 20;
		GeoZone zoneOfInterest = new GeoZone(nPoint);		
		double step = 2.*Math.PI/nPoint;
				
		double deg = 0.0;
		for(int i = 0; i < nPoint; i++) {
			zoneOfInterest.set(sensorNode.getLatitude()+0.001*Math.cos(deg), sensorNode.getLongitude()+0.0015*Math.sin(deg), 0, i);			
			deg += step;
		}	
		
		//-----------------------------------------------------------------------------------------
		// We create a list of building that are inside the zone of interest
		// This will make the program and the calculation quick
		//-----------------------------------------------------------------------------------------
		LinkedList<Building> buildings = new LinkedList<Building>();
		for(Building building : BuildingList.buildings) {
			if(building.intersect(zoneOfInterest)) {
				buildings.add(building);
			}
		}
		
		//-----------------------------------------------------------------------------------------
		// We will test that the edge which is formed by each point of the zoneOfInterest
		// and the center of the current sensor node does not intersect
		// all the edges of the buildings
		//-----------------------------------------------------------------------------------------
		for(int k=0; k<nPoint; k++) {
			boolean intersection = false;
			for(Building building : buildings) {
				if(building.intersect(zoneOfInterest)) {
					if(building.intersect(sensorNode.getLatitude(), sensorNode.getLongitude(), zoneOfInterest.getXCoord(k), zoneOfInterest.getYCoord(k))) {
						intersection = true;
						break;
					}
				}
			}			
			if(!intersection)
				visibilityPointList.add(new double[] {zoneOfInterest.getXCoord(k), zoneOfInterest.getYCoord(k)});
		}

		//-----------------------------------------------------------------------------------------
		// We will take each point of a building and test if the edge formed with this point
		// and the center of the sensor node don't intersect any edge of the buildings
		//-----------------------------------------------------------------------------------------
		for(Building building : buildings) {
			for(int i=0; i<building.getNPoints(); i++) {
				double [] t = new double [2];
				t[0] = building.getYCoords(i);
				t[1] = building.getXCoords(i);
				boolean intersection = false;
				for(Building building2 : buildings) {
					if(building2.intersect(sensorNode.getLatitude(), sensorNode.getLongitude(), t[0], t[1])) {
						intersection = true;
						break;
					}
				}
				if(!intersection)
					visibilityPointList.add(t);
			}
		}

		//-----------------------------------------------------------------------------------------
		// To create the polygon corresponding to the obtained points of the 
		// visibility zone list, we will sort the obtained points
		//-----------------------------------------------------------------------------------------
		for(int i=0; i<visibilityPointList.size()-1; i++) {
			for(int j=i+1; j<visibilityPointList.size(); j++) {
				double a1 = Angle.getAngle(sensorNode.getLongitude()-100, sensorNode.getLatitude(), sensorNode.getLongitude(), sensorNode.getLatitude(), visibilityPointList.get(i)[1], visibilityPointList.get(i)[0]);
				double a2 = Angle.getAngle(sensorNode.getLongitude()-100, sensorNode.getLatitude(), sensorNode.getLongitude(), sensorNode.getLatitude(), visibilityPointList.get(j)[1], visibilityPointList.get(j)[0]);
				if(a1 > a2) {
					double px = visibilityPointList.get(i)[0];
					double py = visibilityPointList.get(i)[1];
					visibilityPointList.get(i)[0] = visibilityPointList.get(j)[0];
					visibilityPointList.get(i)[1] = visibilityPointList.get(j)[1];
					visibilityPointList.get(j)[0] = px;
					visibilityPointList.get(j)[1] = py;
				}
			}
		}
		
		//-----------------------------------------------------------------------------------------
		// Create the zone and,
		// assign the center of the obtained polygon which is the center of the considered  
		// sensor node -> necessary to draw the color of the polygon/zone (degraded)
		//-----------------------------------------------------------------------------------------
		//GeoZone zone = new GeoZone(visibilityPointList.size());
		//zone.setCxCy(sensorNode.getLongitude(), sensorNode.getLatitude());
		
		//-----------------------------------------------------------------------------------------
		// Assign the values of the visibility zone that are the same values of the list:
		// visibilityPointList
		// Assign the zones to the geoZoneList
		//-----------------------------------------------------------------------------------------
		int n = visibilityPointList.size();
		GeoZoneList geoZoneList = new GeoZoneList();
		for(int i=0; i<n-1; i++) {
			GeoZone zone = new GeoZone(3);
			zone.setCxCy(sensorNode.getLongitude(), sensorNode.getLatitude());
			zone.set(sensorNode.getLatitude(), sensorNode.getLongitude(), 0, 0);
			zone.set(visibilityPointList.get(i)[0], visibilityPointList.get(i)[1], 0, 1);
			zone.set(visibilityPointList.get(i+1)[0], visibilityPointList.get(i+1)[1], 0, 2);
			geoZoneList.add(zone);
		}
		GeoZone zone = new GeoZone(3);
		zone.setCxCy(sensorNode.getLongitude(), sensorNode.getLatitude());
		zone.set(sensorNode.getLatitude(), sensorNode.getLongitude(), 0, 0);
		zone.set(visibilityPointList.get(n-1)[0], visibilityPointList.get(n-1)[1], 0, 1);
		zone.set(visibilityPointList.get(0)[0], visibilityPointList.get(0)[1], 0, 2);
		geoZoneList.add(zone);		
		
		//-----------------------------------------------------------------------------------------
		// Assign the obtained geoZonList to the sensor node.
		//-----------------------------------------------------------------------------------------
		sensorNode.setGeoZoneList(geoZoneList);
		
		//-----------------------------------------------------------------------------------------
		// Refresh the graphic
		//-----------------------------------------------------------------------------------------
		MapLayer.repaint();
		
	}

}