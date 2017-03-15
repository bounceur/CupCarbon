/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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

import device.DeviceList;
import device.SensorNode;
import graph.Graph;
import graph.GraphStd;
import map.MapLayer;

public class SensorSetCover {	

	public static void sensorSetCover() {
		GraphStd graphe = null;
		
		List<SensorNode> nodes = DeviceList.sensors;
		
		graphe = SensorGraph.toSensorGraph(nodes, DeviceList.sensorListSize());
		
		//
		// graphe.afficher();
		// L'algorithme
		// Separer les noeuds isoles
		int ng = graphe.size();
		for (int i = 0; i < ng; i++) {
			if (graphe.get(i).getNbVoisins() == 0) {
				graphe.supprimer(i);
				i--;
				ng--;
			}
		}
		// int nbCouvrants = 0 ;
		int max, imax;
		while (graphe.size() > 0) {
			// nbCouvrants++ ;
			max = graphe.get(0).getNbVoisins();
			imax = 0;
			for (int i = 1; i < graphe.size(); i++) {
				if (max < graphe.get(i).getNbVoisins()) {
					max = graphe.get(i).getNbVoisins();
					imax = i;
				}
			}
			nodes.get(graphe.get(imax).getNumber()).setMarked(true);
			graphe.supprimerAvecVoisins(imax);
		}
		// System.out.println("Nombre de capteurs couvrants = "+nbCouvrants+"/"+n);
		// int k = 0 ;
		// for(int i=0; i<capteurs.size(); i++) {
		// if(capteurs.get(i).isCouvrant())
		// System.out.println(++k+" : "+i);
		// }
		// return nbCouvrants;
		MapLayer.repaint();
	}

	public static void sensorTargetSetCover() {
		Graph graphe = null;
		List<SensorNode> nodes = DeviceList.sensors;
		graphe = SensorGraph.toSensorTargetGraph(nodes, DeviceList.deviceListSize());
		int ng = graphe.size();
		for (int i = 0; i < ng; i++) {
			if (graphe.get(i).getNbNeignbors() == 0) {
				graphe.remove(i);
				i--;
				ng--;
			}
		}
		int max, imax;
		while (graphe.size() > 0) {
			max = graphe.get(0).getNbNeignbors();
			imax = 0;
			for (int i = 1; i < graphe.size(); i++) {
				if (max < graphe.get(i).getNbNeignbors()) {
					max = graphe.get(i).getNbNeignbors();
					imax = i;
				}
			}
			nodes.get(graphe.get(imax).getNumber()).setMarked(true);
			graphe.removeWithNeighbors(imax);
		}
		MapLayer.repaint();
	}

}
