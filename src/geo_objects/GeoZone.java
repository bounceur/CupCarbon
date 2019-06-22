package geo_objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class GeoZone {
	
	protected int type = Device.GEOZONE;
	
	private double [] coordX ;
	private double [] coordY ;
	private int [] iCoordX ;
	private int [] iCoordY ;
	private int size = 0 ;
	private boolean selected = false ;
	private int mapZoom = 0;
	
	private double longitude = 0;
	private double latitude = 0;
	private int cx = 0;
	private int cy = 0;
	
	public GeoZone(int size) {
		mapZoom = MapLayer.mapViewer.getZoom();
		this.size = size;
		coordX = new double [size] ;
		coordY = new double [size] ;
		iCoordX = new int [size] ;
		iCoordY = new int [size] ;	
	}

	public void set(double x, double y, double z, int i) {		
		coordX[i] = x;
		coordY[i] = y;
		computeIntCoord(i);
	}
	
	public void set(String x, String y, String z, int i) {
		coordX[i] = Double.valueOf(x);
		coordY[i] = Double.valueOf(y);
		computeIntCoord(i);
	}
	
	public double getXCoord(int i) {
		return coordX[i];
	}
	
	public double getYCoord(int i) {
		return coordY[i];
	}
	
	public double [] getXCoords() {
		return coordX;
	}
	
	public double [] getYCoords() {
		return coordY;
	}
	
	public int size() {
		return size;
	}
	
	public void computeIntCoord(int i) {
		int [] coord = MapCalc.geoToPixelMapA(Double.valueOf(coordX[i]), Double.valueOf(coordY[i]));
		iCoordX[i]=coord[0];
		iCoordY[i]=coord[1];
		coord = MapCalc.geoToPixelMapA(latitude, longitude);
		cx = coord[0];
		cy = coord[1];
	}	
	
	public void computeIntCoords() {
		int [] coord = null ;	
		for(int i=0; i<size; i++) {
			coord = MapCalc.geoToPixelMapA(Double.valueOf(coordX[i]), Double.valueOf(coordY[i]));
			iCoordX[i]=coord[0];
			iCoordY[i]=coord[1];
			coord = MapCalc.geoToPixelMapA(latitude, longitude);
			cx = coord[0];
			cy = coord[1];
		}
	}

	public void computeGeoCoords() {
		for(int i=0; i<size; i++) {
			GeoPosition gp = MapCalc.pixelMapToGeo(iCoordX[i], iCoordY[i]); 
			coordX[i] = gp.getLatitude();
			coordY[i] = gp.getLongitude();
		}
	}
	
	public void draw(Graphics g) {
		int newZoom = MapLayer.mapViewer.getZoom();
		if(newZoom != mapZoom) {
			mapZoom = newZoom;
			computeIntCoords() ;
		}	

		Graphics2D g2 = (Graphics2D) g;
		
		Point2D center = new Point2D.Float(cx, cy);
		
		double paintRadius = 300 * (4.00/Math.pow(2, MapLayer.mapViewer.getZoom()));		
		
		float[] dist = {0.0f, 0.2f, 0.7f};
		Color [] colors = new Color[3];
		
		colors[0] = new Color(255, 0, 0, 80);
		colors[1] = new Color(250, 5, 0, 30);
		
		//colors[0] = new Color(0, 102, 204, 90);
		//colors[1] = new Color(0, 102, 204, 20);
		
		colors[2] = new Color(255, 255, 255, 0);
		if(MapLayer.dark) {			
			colors[0] = new Color(255, 0, 0, 80);
			colors[1] = new Color(221, 148,32, 80);
			colors[2] = new Color(221, 0,32, 0);
		}
		if(selected) {			
			colors[0] = new Color(250, 5, 0, 90);
			colors[1] = new Color(250, 5, 0, 40);
			colors[2] = new Color(250, 5, 0, 0);			
		}
		RadialGradientPaint p = new RadialGradientPaint(center, (float) paintRadius, dist, colors);
		g2.setPaint(p);

		g2.fillPolygon(iCoordX, iCoordY, size);
		g2.setColor(UColor.RED_TRANSPARENT);
		if(selected) {
			g2.setColor(UColor.RED);
			if(MapLayer.dark)
				g2.setColor(UColor.ORANGE);			
		}
		g2.drawPolygon(iCoordX, iCoordY, size);
	}
	
	public boolean inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.mapViewer.convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.mapViewer.getTileFactory().geoToPixel(gp, MapLayer.mapViewer.getZoom());
		Polygon poly = new Polygon(iCoordX,iCoordY,size);
		return (poly.contains(p1));
	}

	public void setSelected(boolean b) {
		selected = b ;
	}

	public boolean isSelected() {
		return selected ;
	}
	
	public boolean intersect(Polygon p) {
		for (int i=0; i<size; i++){
			if(p.contains(iCoordX[i], iCoordY[i]))
				return true;
		}
		return false;
	}

	public void translate(double xref, double yref, double zm) {
		for(int i=0; i<size; i++) {
			coordX[i] = coordX[i]/zm+xref ;
			coordY[i] = coordY[i]/zm+yref ;
			computeIntCoord(i);
		}
	}
	
	public boolean contains(Point2D p) {
		Polygon poly = new Polygon(iCoordX, iCoordY, size);
		return (poly.contains(p));
	}
	
	public boolean contains(double px, double py) {
		Polygon poly = new Polygon(iCoordX, iCoordY, size);
		return poly.contains(px, py);
	}
	
	public Polygon toPolygon() {
		Polygon poly = new Polygon(iCoordX, iCoordY, size);
		return poly;
	}
	
	public void display() {
		for(int i=0; i<size; i++) {
			System.out.print("("+iCoordX[i]+", "+iCoordY[i]+") ");
		}
		System.out.println();
	}

	public int getICoordX(int i) {
		return iCoordX[i];
	}
	
	public int getICoordY(int i) {
		return iCoordY[i];
	}
	
	public void setICoordX(int i, int v) {
		iCoordX[i] = v;
	}
	
	public void setICoordY(int i, int v) {
		iCoordY[i] = v;
	}	

	public void setInt(int x, int y, int i) {
		iCoordX[i]=x;
		iCoordY[i]=y;
	}

	public void setCxCy(double longitude, double latitude) {
		this.longitude = longitude ;
		this.latitude = latitude;
		int [] coord = MapCalc.geoToPixelMapA(latitude, longitude);
		cx = coord[0];
		cy = coord[1];
	}
	
	public int getCx() {
		return cx ;
	}
	
	public int getCy() {
		return cy ;
	}
	
}
