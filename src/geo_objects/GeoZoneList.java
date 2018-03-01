package geo_objects;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import math.Angle;
import utilities.Geometry;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class GeoZoneList {

	private LinkedList<GeoZone> zones = null;
	private double longitude;
	private double latitude;

	public GeoZoneList() {
		zones = new LinkedList<GeoZone>();
	}
	
	public GeoZoneList(LinkedList<GeoZone> geoZoneList) {
		this.zones = geoZoneList ; 
	}
		
	public void add(GeoZone geoZone) {
		zones.add(geoZone);		
	}
	
	public void draw(Graphics g) {
		for(GeoZone geoZone : zones) {
			geoZone.draw(g);
		}
	}
	
	public void init() {
		System.out.println("INIT GZ");
		for(GeoZone geoZone : zones) {
			delete(geoZone);
		}
		zones = new LinkedList<GeoZone>();
	}
	
	public void delete(GeoZone geoZone) {	
		geoZone = null;
		zones.remove(geoZone);
	}
	
	public boolean intersect(Polygon p) {
		for (GeoZone geoZone : zones){
			if (geoZone.intersect(p))
				return true;
		}
		return false;
	}

	public LinkedList<GeoZone> getGeoZoneList() {
		return zones;
	}
	
	public boolean contains(Point2D p) {
		for(GeoZone geoZone : zones) {
			if(geoZone.contains(p)) return true;
		}
		return false;
	}
	
	public boolean contains(double px, double py) {
		for(GeoZone geoZone : zones) {
			if(geoZone.contains(px, py)) return true;
		}
		return false;
	}
	
	public void reduce(double xref, double yref, double zm) {
		for(GeoZone geo : zones) {
			geo.translate(xref, yref, zm);
		}
	}
	
	public int size() {
		return zones.size(); 
	}
	
	public boolean isEmpty() {
		return (zones.size()==0);
	}
	
	public void display() {
		for(GeoZone gz : zones) {
			gz.display();
			System.out.println();
		}
 	}
	
	public void toOneGeoZone() {
		int n = zones.size();
		GeoZone geoc = new GeoZone(n);
		GeoZone geo = new GeoZone(2*n);
		
		int idx1 = 0;
		int idx2 = 0;

		for(GeoZone gz : zones) {
			int [] v = Geometry.getCentre(gz.getICoordX(1), gz.getICoordY(1), 0, gz.getICoordX(2), gz.getICoordY(2), 0);
			geoc.setInt(v[0], v[1], idx1++);
			geo.setInt(gz.getICoordX(1), gz.getICoordY(1), idx2++);
			geo.setInt(gz.getICoordX(2), gz.getICoordY(2), idx2++);
		}

		geo.setCxCy(longitude, latitude);
		int x = geo.getCx();
		int y = geo.getCy();
		
		int s1;
		int s2;		
		
		for(int i=0; i<geoc.size()-1; i++) {
			double a = Angle.getAngle(x-100, y, x, y, geoc.getICoordX(i), geoc.getICoordY(i));
			for(int j=i+1; j<geoc.size(); j++) {
				double na = Angle.getAngle(x-100, y, x, y, geoc.getICoordX(j), geoc.getICoordY(j));
				if (na < a) {
					a = na;
					s1 = geoc.getICoordX(i);
					s2 = geoc.getICoordY(i);					
					geoc.setICoordX(i, geoc.getICoordX(j));
					geoc.setICoordY(i, geoc.getICoordY(j));
					geoc.setICoordX(j, s1);
					geoc.setICoordY(j, s2);
					s1 = geo.getICoordX(i*2);
					s2 = geo.getICoordY(i*2);
					geo.setICoordX(i*2, geo.getICoordX(j*2));
					geo.setICoordY(i*2, geo.getICoordY(j*2));
					geo.setICoordX(j*2, s1);
					geo.setICoordY(j*2, s2);
					s1 = geo.getICoordX(i*2+1);
					s2 = geo.getICoordY(i*2+1);
					geo.setICoordX(i*2+1, geo.getICoordX(j*2+1));
					geo.setICoordY(i*2+1, geo.getICoordY(j*2+1));
					geo.setICoordX(j*2+1, s1);
					geo.setICoordY(j*2+1, s2);
				}
			}
		}	
	
		geo.computeGeoCoords();
		
		zones = new LinkedList<GeoZone>();
		zones.add(geo);		
	}
	
	public void setSelected(boolean b) {
		for(GeoZone gz : zones) {
			gz.setSelected(b);
		}
	}
	
	public void computeIntCoords() {
		for(GeoZone gz : zones) {
			gz.computeIntCoords();
		}
	}
	
	public double getLongitude() {
		return longitude; 
	}
	
	public double getLatitude() {
		return latitude; 
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
