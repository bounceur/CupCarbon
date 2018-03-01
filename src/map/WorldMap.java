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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package map;

import java.awt.Toolkit;
import java.io.File;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

import cupcarbon.CupCarbon;

public class WorldMap extends JXMapKit {

	public static boolean darkMap = false;
	
	private static final long serialVersionUID = 1L;
	private static MapLayer layer;
	private final int max = 19;
	public static String tileType = ".png";	
	public static String tileUrl = Tiles.TILE0;	
	public static boolean local = true;
	public static boolean gmap = false; // to use google maps (not yet integrated)
	public static String tileName = Tiles.TILE2;	
	public static int mapIdx = 2;
	
	public WorldMap() {				
		WorldMapExecute();
		layer = new MapLayer(getMainMap());
	}
	
	public void WorldMapExecute() {
				TileFactoryInfo info = new TileFactoryInfo(0, max, max, 256, true, true, "http://tile.openseamap.org", "x", "y", "z") {			
					public String getTileUrl(int x, int y, int zoom) {
						zoom = max - zoom;
						if(local) {
								File file = new File("tiles"+File.separator+tileName);
								tileUrl = file.getAbsolutePath();
								tileUrl = tileUrl.replaceAll(" ", "%20");
								tileUrl = tileUrl.replaceAll("\\\\", "/");
								return "file:///"+tileUrl;
						}
						else {
							if(gmap) {
								return tileUrl + "&x="+x+"&y="+y+"&z="+zoom+"&s=Ga";
							}
							else {
								return tileUrl+zoom+"/"+x+"/"+y+tileType ;					
							}
							
						}
					}
				};

				info.setDefaultZoomLevel(3);

				TileFactory tf = new DefaultTileFactory(info);

				setTileFactory(tf);
				
				setDataProviderCreditShown(true);
				setName("CupCarbon Map");				
				setCenterPosition(new GeoPosition(48.39052932411496, -4.486016035079956));// Brest		
				
				setZoom(2);
	}

	
	public static void simulate() {
		layer.simulate();
	}

	public static void simulateAll() {
		layer.simulateAll();
	}
	
	public static void simulateSensors() {
		layer.simulateSensors();
	}
	
	public static void simulateMobiles() {
		layer.simulateMobiles();
	}

	public static void addNodeInMap(char c) {
		layer.addNodeInMap(c);
	}

	public static void loadCityNodes() {
		layer.loadCityNodes();
	}

	public static void setSelectionOfAllNodes(boolean selection, int type, boolean addSelection) {
		layer.setSelectionOfAllNodes(selection, type, addSelection);
	}
	
	public static void setSelectionOfAllMobileNodes(boolean selection, int type, boolean addSelection) {
		layer.setSelectionOfAllMobileNodes(selection, type, addSelection);
	}

	public static void invertSelection() {
		layer.invertSelection();
	}

	public static void setSelectionOfAllMarkers(boolean selection, boolean addSelection) {
		layer.setSelectionOfAllMarkers(selection, addSelection);
	}
	
	public static void setSelectionOfAllBuildings(boolean selection, boolean addSelection) {
		layer.setSelectionOfAllBuildings(selection, addSelection);
	}
	
	public void setLoc(GeoPosition gp) {
		setCenterPosition(new GeoPosition(48.39188295873048, -4.44371223449707));
	}	
	
	public static void changeMap(int index) {
		MapLayer.mapViewer.setLoadingImage(Toolkit.getDefaultToolkit().getImage("tiles"+File.separator+"mer.png"));
		mapIdx = index;
		switch(index) {
		case 0 : CupCarbon.cupCarbonController.checkMapMenuItem(0); changeTiles(Tiles.TILE0, false); darkMap(false); break;
		case 1 : MapLayer.mapViewer.setLoadingImage(Toolkit.getDefaultToolkit().getImage("tiles/cuptile_black.png"));CupCarbon.cupCarbonController.checkMapMenuItem(1); changeTiles(Tiles.TILE1, false); darkMap(true); break;
		case 2 : CupCarbon.cupCarbonController.checkMapMenuItem(2); changeLocalTiles(Tiles.TILE2); darkMap(false); break;
		case 3 : CupCarbon.cupCarbonController.checkMapMenuItem(3); changeLocalTiles(Tiles.TILE3); darkMap(false); break;
		case 4 : CupCarbon.cupCarbonController.checkMapMenuItem(4); changeLocalTiles(Tiles.TILE4); darkMap(true); break;
		case 5 : CupCarbon.cupCarbonController.checkMapMenuItem(5); changeLocalTiles(Tiles.TILE5); darkMap(true); break;
		case 6 : CupCarbon.cupCarbonController.checkMapMenuItem(6); changeLocalTiles(Tiles.TILE6); darkMap(false); break;
		case 7 : CupCarbon.cupCarbonController.checkMapMenuItem(7); changeLocalTiles(Tiles.TILE7); darkMap(false); break;
		case 8 : CupCarbon.cupCarbonController.checkMapMenuItem(8); changeLocalTiles(Tiles.TILE8); darkMap(false); break;
		case 9 : CupCarbon.cupCarbonController.checkMapMenuItem(9); changeLocalTiles(Tiles.TILE9); darkMap(false); break;
		case 10 : CupCarbon.cupCarbonController.checkMapMenuItem(10); changeTiles(Tiles.TILE10, true); darkMap(false); break;
		case 11 : CupCarbon.cupCarbonController.checkMapMenuItem(11); changeTiles(Tiles.TILE11, true); darkMap(true); break;
		case 12 : CupCarbon.cupCarbonController.checkMapMenuItem(12); changeTiles(Tiles.TILE12, false); darkMap(false); break;
		}
	}
	
	public static void darkMap(boolean b) {
		MapLayer.dark = b;
		if(b && NetworkParameters.radioLinksColor==0) {
			NetworkParameters.radioLinksColor=2;
		}
	}
	
	public static void changeTiles(String s, boolean gmap) {
		WorldMap.gmap = gmap ;
		WorldMap.local = false ;
		WorldMap.tileUrl = s;						
		MapLayer.repaint();
	}	
	
	public static void changeLocalTiles(String s) {
		WorldMap.gmap = false ;
		WorldMap.local = true ;
		WorldMap.tileName = s;
		
		Thread th = new Thread() {
			@Override
			public void run() {
				MapLayer.repaint();
			}
		};
		th.start();
	}
	
}
