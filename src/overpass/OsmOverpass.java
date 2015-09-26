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

package overpass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import buildings.Building;
import buildings.BuildingList;
import map.Layer;

/**
 * @author BOYER Yoann
 * @author BOUNCEUR Ahcène
 * @version 1.0
 */
public class OsmOverpass extends Thread {

	protected double bottomLeftLat; 
	protected double bottomLeftLng; 
	protected double topRightLat;
	protected double topRightLng;
	public static boolean isLoading = false;
	
	public OsmOverpass(double bottomLeftLat, double bottomLeftLng, double topRightLat, double topRightLng) {
		this.bottomLeftLat = bottomLeftLat; 
		this.bottomLeftLng = bottomLeftLng; 
		this.topRightLat = topRightLat;
		this.topRightLng = topRightLng;
	}
	
	@Override
	public void run(){
		isLoading = true;
		try {
		    URL url = new URL("http://overpass-api.de/api/map?bbox="+bottomLeftLng+","+bottomLeftLat+","+topRightLng+","+topRightLat);
		    //System.out.println("http://overpass-api.de/api/map?bbox="+bottomLeftLng+","+bottomLeftLat+","+topRightLng+","+topRightLat);
		    System.out.println("[Buildings] File downloading...");
		    Osm data = (Osm) JAXBContext.newInstance(Osm.class).createUnmarshaller().unmarshal( url );
		    System.out.println("[Buildings] File downloaded.");
		    System.out.println("[Buildings] Processing...");
			

			List<OsmWay> ways= data.getWay();
			List<OsmNode> nodes= data.getNode();
			List<OsmNd> nds;
			Building building;


	        for(OsmWay way:ways){ // For each shapes
	        	if (way.isBuilding()){
	        		nds = way.getNd();
		        	building = new Building(nds.size());
		        	
		        	for (int i=nds.size()-1; i>=0; i--){ // Get all nodes referenced by the shape
		        		for(OsmNode node:nodes){
		        			if (nds.get(i).getRef().equals(node.getId())){
		        				building.set(node.getLat(), node.getLon(), i);
		        				break;
		        			}
		    	        }
		        	}
		        	
		        	Layer.getMapViewer().addMouseListener(building);
		    		Layer.getMapViewer().addKeyListener(building);
		        	BuildingList.add(building);
	        	}
	        }
	        isLoading = false;
	        Layer.mapViewer.repaint();
	        System.out.println("[Buildings] Building maker finished.");	        
		} catch (JAXBException e) {
			e.printStackTrace();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}
