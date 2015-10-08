package buildings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import map.Layer;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
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
			coord = MapCalc.geoToIntPixelMapXY(Double.valueOf(coordX[i]), Double.valueOf(coordY[i]));
			iCoordX[i]=coord[0];
			iCoordY[i]=coord[1];
		}
		if(!selected)
			g.setColor(UColor.BLACK_TTTTRANSPARENT);
		else
			g.setColor(UColor.BLACK_TTRANSPARENT);
		g.fillPolygon(iCoordX, iCoordY, n);
		g.setColor(Color.DARK_GRAY);
		g.drawPolygon(iCoordX, iCoordY, n);
	}

	@Override
	public void mouseClicked(MouseEvent arg) {
		if(inside(arg.getX(),arg.getY())) {
			selected = !selected;
			Layer.getMapViewer().repaint();
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
		GeoPosition gp = Layer.getMapViewer().convertPointToGeoPosition(p);
		Point2D p1 = Layer.getMapViewer().getTileFactory().geoToPixel(gp, Layer.getMapViewer().getZoom());
		Polygon poly = new Polygon(iCoordX,iCoordY,n);
		return (poly.contains(p1));
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode()==8 && selected) {
			selected = false;
			Layer.getMapViewer().removeMouseListener(this);
			Layer.getMapViewer().removeMouseListener(this);	
			BuildingList.delete(this);
			Layer.getMapViewer().repaint();
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
			if (type == Layer.selectType)
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
	
//	public boolean intersect(int cadreX1, int cadreY1, int cadreX2, int cadreY2) {
//		Polygon poly = new Polygon(iCoordX,iCoordY,n);
//		return poly.intersects((double) cadreX1, (double) cadreY1, (double) cadreX2, (double) cadreY2);
//	}
}
