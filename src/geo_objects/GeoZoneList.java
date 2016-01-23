package geo_objects;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.LinkedList;

import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class GeoZoneList extends Thread {

	public static LinkedList<GeoZone> geoZoneList = null;		
	
	public GeoZoneList() {
		geoZoneList = new LinkedList<GeoZone>();
	}
	
	public void add(String str) {
		GeoZone geoZone = new GeoZone(str);
		MapLayer.getMapViewer().addMouseListener(geoZone);
		MapLayer.getMapViewer().addKeyListener(geoZone);
		geoZoneList.add(geoZone);
	}
	
	public void add(String [] str) {		
		for(int i=0; i<str.length; i++) {
			geoZoneList.add(new GeoZone(str));
		}
	}
	
	public static void add(GeoZone geoZone) {
		MapLayer.getMapViewer().addMouseListener(geoZone);
		MapLayer.getMapViewer().addKeyListener(geoZone);
		geoZoneList.add(geoZone);		
	}
	
	public void draw(Graphics g) {
		for(GeoZone geoZone : geoZoneList) {
			geoZone.draw(g);
		}
	}
	
	public void init() {
		for(GeoZone geoZone : geoZoneList) {
			MapLayer.getMapViewer().removeMouseListener(geoZone);
			MapLayer.getMapViewer().removeKeyListener(geoZone);
			geoZone = null;
		}
		geoZoneList = new LinkedList<GeoZone>();
	}
	
	public void delete(GeoZone geoZone) {
		for(GeoZone b : geoZoneList) {			
			if(b==geoZone) {
				MapLayer.getMapViewer().removeMouseListener(b);
				MapLayer.getMapViewer().removeKeyListener(b);
				geoZoneList.remove(b);
				break;
			}
		}
	}
	
	public boolean intersect(Polygon p) {
		for (GeoZone geoZone : geoZoneList){
			if (geoZone.intersect(p))
				return true;
		}
		return false;
	}

	public LinkedList<GeoZone> getGeoZoneList() {
		return geoZoneList;
	}
}
