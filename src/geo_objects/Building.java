package geo_objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.vecmath.Vector3d;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class Building implements MouseListener, KeyListener {
	
	protected int type = Device.BUILDING;
	
	private double[] coordX ;
	private double[] coordY ;
	private int[] iCoordX ;
	private int[] iCoordY ;
	private int n = 0 ;
	private boolean selected = false ;
	
	public Building(int n) {
		this.n=n;
		coordX = new double [n] ;
		coordY = new double [n] ;
		iCoordX = new int [n] ;
		iCoordY = new int [n] ;
	}
	
	public Building(String [] str) {
		n = str.length;
		coordX = new double [n] ;
		coordY = new double [n] ;
		iCoordX = new int [n] ;
		iCoordY = new int [n] ;
		for(int i=0; i<n; i++) {
			coordX[i]=Double.valueOf(str[i*2]);
			coordY[i]=Double.valueOf(str[i*2+1]);
		}
	}
	
	public Building(String str) {
		String [] vStr = str.split(" ");
		n = vStr.length/2;
		coordX = new double [n] ;
		coordY = new double [n] ;
		iCoordX = new int [n] ;
		iCoordY = new int [n] ;
		for(int i=0; i<n; i++) {
			coordX[i]=Double.valueOf(vStr[i*2]);
			coordY[i]=Double.valueOf(vStr[i*2+1]);
		}
	}
	
	public void set(double x, double y, int i) {
		coordX[i]=x;
		coordY[i]=y;
	}
	
	public void set(String x, String y, int i) {
		coordX[i] = Double.valueOf(x);
		coordY[i] = Double.valueOf(y);
	}
	
	public double getXCoords(int i) {
		return coordX[i];
	}
	
	public double [] getXCoords() {
		return coordX;
	}
	
	public double [] getYCoords() {
		return coordY;
	}
	
	public double getYCoords(int i) {
		return coordY[i];
	}
	
	public int getN() {
		return n;
	}
	
	public void draw(Graphics g) {
		int[] coord = null ;	
		for(int i=0; i<n; i++) {
			coord = MapCalc.geoToIntPixelMapXY(Double.valueOf(coordY[i]), Double.valueOf(coordX[i]));
			iCoordX[i] = coord[0];
			iCoordY[i] = coord[1];
		}
		if(selected) {			
			g.setColor(UColor.BLACK_TTTRANSPARENT);				
		}
		else {
			g.setColor(UColor.BLACK_TTTTRANSPARENT);
		}
		g.fillPolygon(iCoordX, iCoordY, n);
		g.setColor(UColor.BLACK_TTRANSPARENT);
		g.drawPolygon(iCoordX, iCoordY, n);
	}

	@Override
	public void mouseClicked(MouseEvent arg) {
		if(inside(arg.getX(),arg.getY())) {
			selected = !selected;
			MapLayer.getMapViewer().repaint();
		}
		//if(shape.contains(x, y))
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean inside(int xs, int ys) {
		Point p = new Point(xs, ys);
		GeoPosition gp = MapLayer.getMapViewer().convertPointToGeoPosition(p);
		Point2D p1 = MapLayer.getMapViewer().getTileFactory().geoToPixel(gp, MapLayer.getMapViewer().getZoom());
		Polygon poly = new Polygon(iCoordX,iCoordY,n);
		return (poly.contains(p1));
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode()==8 && selected) {
			selected = false;
			MapLayer.getMapViewer().removeMouseListener(this);
			MapLayer.getMapViewer().removeMouseListener(this);	
			BuildingList.delete(this);
			MapLayer.getMapViewer().repaint();
		}
		
		if(key.getKeyCode() == 27) {
			selected = false;
		}
		
		if (key.getKeyCode() == 65 && key.isControlDown()) {
			selected = true;
		}
		
		if (key.getKeyChar() == 'i') {
			selected = !selected;
		}
		
		if (key.getKeyChar() == 'w') {
			if (type == MapLayer.selectType)
				selected = true;
			else 
				selected = false;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setSelection(boolean b) {
		selected = b ;
	}

	public boolean isSelected() {
		return selected ;
	}
	
	public boolean intersect(Polygon p) {
		for (int i=0; i<n; i++){
			if(p.contains(iCoordX[i], iCoordY[i]))
				return true;
		}
		return false;
	}

	public int size() {
		return n;
	}
	
	public Vector3d [] toVector3d() {
		Vector3d [] vector = new Vector3d [n] ;
		for(int i=0; i<n; i++) {
			vector[i] = new Vector3d(coordY[i], coordX[i], 10.0);
		}
		return vector;
	}
	
	public Vector3d [] toVector3d(double xref, double yref, double zm) {
		Vector3d [] vector = new Vector3d [n] ;
		for(int i=0; i<n; i++) {
			vector[i] = new Vector3d((coordY[i]-xref)*zm, (coordX[i]-yref)*zm, 10.0);
		}
		return vector;
	}
	
	//public 
	
//	public boolean intersect(int cadreX1, int cadreY1, int cadreX2, int cadreY2) {
//		Polygon poly = new Polygon(iCoordX,iCoordY,n);
//		return poly.intersects((double) cadreX1, (double) cadreY1, (double) cadreX2, (double) cadreY2);
//	}
}
