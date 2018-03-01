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

import action.CupAction;
import action.CupActionAddBuilding;
import action.CupActionBlock;
import action.CupActionStack;
import buildings.Building;
import buildings.BuildingList;
import map.MapLayer;

/**
 * @author Yann Allain
 * @author Julien Benkhellate
 * @author Bounceur Ahcène
 * @version 1.0
 */
public class OsmOverpass {

	protected double bottomLeftLat; 
	protected double bottomLeftLng; 
	protected double topRightLat;
	protected double topRightLng;
	
	public OsmOverpass(double bottomLeftLat, double bottomLeftLng, double topRightLat, double topRightLng) {
		this.bottomLeftLat = Math.min(bottomLeftLat, topRightLat); 
		this.bottomLeftLng = Math.min(bottomLeftLng, topRightLng);
		this.topRightLat = Math.max(bottomLeftLat, topRightLat);
		this.topRightLng = Math.max(bottomLeftLng, topRightLng);
	}
	
	public void load(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				BuildingList.isLoading = true;
				try {
				    URL url = new URL("http://overpass-api.de/api/map?bbox="+bottomLeftLat+","+bottomLeftLng+","+topRightLat+","+topRightLng);
				    System.out.println(url);		
				    System.out.println("[Buildings] File downloading...");		    
				    Osm data = (Osm) JAXBContext.newInstance(Osm.class).createUnmarshaller().unmarshal( url );
				    System.out.println("[Buildings] File downloaded.");
				    System.out.println("[Buildings] Processing...");
					List<OsmWay> ways= data.getWay();
					List<OsmNode> nodes= data.getNode();
					List<OsmNd> nds;
					Building building;
					
					CupActionBlock block = new CupActionBlock();
					
			        for(OsmWay way:ways){ // For each shape
			        	if (way.isBuilding()){
			        		nds = way.getNd();
				        	building = new Building(nds.size());
				        	
				        	for (int i=nds.size()-1; i>=0; i--){ // Get all nodes referenced by the shape
				        		for(OsmNode node:nodes){
				        			if (nds.get(i).getRef().equals(node.getId())){
				        				building.set(node.getLon(), node.getLat(), i);
				        				break;
				        			}
				    	        }
				        	}
				        	
				        	MapLayer.mapViewer.addMouseListener(building);
				    		MapLayer.mapViewer.addKeyListener(building);
				        	CupAction action = new CupActionAddBuilding(building);
							block.addAction(action);
			        	}
			        }
			        if(block.size()>0) {
				        CupActionStack.add(block);			
						CupActionStack.execute();
			        }
			        BuildingList.isLoading = false;
			        MapLayer.repaint();
			        System.out.println("[Buildings] Building loaded: SECCESS!");	        
				} catch (JAXBException e) {
					e.printStackTrace();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
