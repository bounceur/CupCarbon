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
	//private double [] coordZ ;
	private int [] iCoordX ;
	private int [] iCoordY ;
	//private int [] iCoordZ ;
	private int size = 0 ;
	private boolean selected = false ;
	private int mapZoom = 0;
	
	private double longitude = 0;
	private double latitude = 0;
	private int cx = 0;
	private int cy = 0;
	
	//private static Color color = new Color(197,0,117,100);//new Color(246, 131,234, 120);
	private static Color color = UColor.RED_TRANSPARENT;
			//new Color(197,0,117,100);//UColor.RED_TRANSPARENT;// new Color(221, 148,32, 100);
	
	public GeoZone(int size) {
		mapZoom = MapLayer.mapViewer.getZoom();
		this.size = size;
		coordX = new double [size] ;
		coordY = new double [size] ;
		//coordZ = new double [size] ;
		iCoordX = new int [size] ;
		iCoordY = new int [size] ;
		//iCoordZ = new int [size] ;	
	}

	public void set(double x, double y, double z, int i) {		
		coordX[i] = x;
		coordY[i] = y;
		//coordZ[i] = z;
		computeIntCoord(i);
	}
	
	public void set(String x, String y, String z, int i) {
		coordX[i] = Double.valueOf(x);
		coordY[i] = Double.valueOf(y);
		//coordZ[i] = Double.valueOf(z);
		computeIntCoord(i);
	}
	
	public double getXCoord(int i) {
		return coordX[i];
	}
	
	public double getYCoord(int i) {
		return coordY[i];
	}
	
//	public double getZCoords(int i) {
//		return coordZ[i];
//	}
	
	public double [] getXCoords() {
		return coordX;
	}
	
	public double [] getYCoords() {
		return coordY;
	}
	
//	public double [] getZCoords() {
//		return coordZ;
//	}
	
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
		
//		if (color==0)
//			g.setColor(UColor.RED_TTRANSPARENT);//.ORANGE_TRANSPARENT2);
//		if (color==1)
//			g.setColor(UColor.GREEND_TRANSPARENT);
//		if (color==2)
//			g.setColor(UColor.PURPLE_TRANSPARENT);

		Graphics2D g2 = (Graphics2D) g;
		
		Point2D center = new Point2D.Float(cx, cy);
		double paintRadius = 300 * (4.00/Math.pow(2, MapLayer.mapViewer.getZoom()));
		float[] dist = {0.0f, 0.2f, 0.5f};
		//float[] dist = {0.0f, 0.5f};
		Color[] colors = {color, new Color(221, 148,32, 100), UColor.TRANSPARENT};
		//Color[] colors = {UColor.RED_TRANSPARENT, new Color(221, 148,32, 100), UColor.TRANSPARENT};
		//Color[] colors = {UColor.ORANGE_TRANSPARENT2, UColor.YELLOW_TRANSPARENT, UColor.TRANSPARENT};
		RadialGradientPaint p = new RadialGradientPaint(center, (float) paintRadius, dist, colors);
		g2.setPaint(p);
		
		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		//Paint p = new GradientPaint(0, 0, UColor.RED_TRANSPARENT, 100, 100, UColor.ORANGE_TRANSPARENT2, true);

		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		//g.setColor(UColor.ORANGE_TRANSPARENT2);
		g.fillPolygon(iCoordX, iCoordY, size);
		if(selected) {
			g.setColor(UColor.BLACK_TRANSPARENT);
			g.drawPolygon(iCoordX, iCoordY, size);
//			for(int i=0; i<size; i++) {
//				g.fillOval(iCoordX[i]-2, iCoordY[i]-2, 4, 4);
//			}
		}	
	}
	
	public boolean inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
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
	
//	public Vector3d [] toVector3d() {
//		Vector3d [] vector = new Vector3d [size] ;
//		for(int i=0; i<size; i++) {
//			vector[i] = new Vector3d(coordY[i], coordX[i], 10.0);
//		}
//		return vector;
//	}
//	
//	public Vector3d [] toVector3d(int m) {
//		Vector3d [] vector = new Vector3d [size] ;
//		for(int i=0; i<size; i++) {
//			vector[i] = new Vector3d(coordY[i]/m, coordX[i]/m, 10.0);
//		}
//		return vector;
//	}

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
		//System.out.println("x : "+iCoordX[i]);
		//System.out.println("y : "+iCoordY[i]);
		//coordX[i] = MapCalc.pixelMapToGeo(iCoordX[i], iCoordY[i]).getLatitude();
		
		//GeoPosition gg = MapLayer.getMapViewer().getTileFactory().pixelToGeo(new Point((int)p.getX(), (int)p.getY()), MapLayer.getMapViewer().getZoom());
		
		//System.out.println("i : "+coordX[i]);
	}
	
	public void setICoordY(int i, int v) {
		iCoordY[i] = v;
		//coordY[i] = MapCalc.pixelMapToGeo(iCoordX[i], iCoordY[i]).getLongitude();
	}	

	public void setInt(int x, int y, int i) {
		iCoordX[i]=x;
		iCoordY[i]=y;
//		coordX[i] = MapCalc.pixelMapToGeo(x, y).getLatitude();
//		coordY[i] = MapCalc.pixelMapToGeo(x, y).getLongitude();
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
