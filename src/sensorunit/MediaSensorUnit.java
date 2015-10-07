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

package sensorunit;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import flying_object.FlyingGroup;
import flying_object.FlyingObject;
import map.Layer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class MediaSensorUnit implements KeyListener, Cloneable {

	protected double radius = 100;
	protected double longitude;
	protected double latitude;
	protected Device node;
	protected boolean displayRadius = false;
	
	
	protected int n = 12;
	protected double deg = 0.1;
	protected double dec = 0;
	
	protected int [] polyX1 = new int[n];
	protected int [] polyY1 = new int[n];

	/**
	 * Constructor 1 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param node which is associated to this sensor unit
	 */
	public MediaSensorUnit(double longitude, double latitude, Device node) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.node = node;
		calculateSensingArea();
		Layer.getMapViewer().addKeyListener(this);
	}
	
	/**
	 * Constructor 2 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param cuRadius the value of the radius 
	 * @param node which is associated to this sensor unit
	 */
	public MediaSensorUnit(double longitude, double latitude, double radius, double deg, double dec, int n, Device node) {
		this(longitude, latitude, node);
		this.radius = radius;
		this.deg = deg;
		this.dec = dec;
		this.n = n;
		calculateSensingArea();
	}

	public void calculateSensingArea() {
		int rayon = MapCalc.radiusInPixels(radius) ; 
		
		double r2=0;
		double r3=0;
		
		polyX1[0] = (int)longitude;
		polyY1[0] = (int)latitude;
		double i=dec;
		for(int k=1; k<n; k++) {
			r2 = rayon*Math.cos(i);
			r3 = rayon*Math.sin(i);
			polyX1[k]=(int)(longitude+r2);
			polyY1[k]=(int)(latitude+r3);
			i+=deg;
		}
	}
	
	/**
	 * @return radius of the sensor unit
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Set the radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Change the position of the sensor unit
	 */
	public void setPosition(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	
	public boolean detect(Device device) {
		if(device.getRadius()>0) {
			Polygon poly = new Polygon(polyX1, polyY1, n);
			if(device.getType()==Device.FLYING_OBJECT) {
				for(FlyingObject d : ((FlyingGroup)device).getFlyingObjects()) {
					GeoPosition gp = new GeoPosition(d.getLongitude(),d.getLatitude());
					Point2D p1 = Layer.getMapViewer().getTileFactory().geoToPixel(gp, Layer.getMapViewer().getZoom());
					if(poly.contains(p1))
						return true;
				}
				return false;
			}
			else {
				GeoPosition gp = new GeoPosition(device.getLongitude(),device.getLatitude());
				Point2D p1 = Layer.getMapViewer().getTileFactory().geoToPixel(gp, Layer.getMapViewer().getZoom());		
				return (poly.contains(p1));
			}
		}
		else
			return false;
	}
	
	/**
	 * Draw the sensor unit
	 */
	public void draw(Graphics g, int mode, boolean detection) {
		 calculateSensingArea();
		if (!detection)
			g.setColor(UColor.BLEU_TRANSPARENT);
		if (detection)
			g.setColor(UColor.VERTF_TRANSPARENT);
	
		if (mode == 0)
			g.fillPolygon(polyX1, polyY1, n);
		g.setColor(UColor.NOIRF_TTTRANSPARENT);
		g.drawPolygon(polyX1, polyY1, n);
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (node.isSelected()) {			
			if (key.getKeyChar() =='p') {
				dec+=0.1;
			}
			if (key.getKeyChar() =='o') {
				dec-=0.1;
			}
			if (key.getKeyChar() == ')') {
				radius+=5;
			}
			if (key.getKeyChar() == '(') {
				radius-=5;
			}
			if (key.getKeyChar() == 'P') {
				deg+=0.01;
			}
			if (key.getKeyChar() == 'O') {
				deg-=0.01;
			}
			Layer.getMapViewer().repaint();
		}
	}

	/**
	 * Coming soon
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	/**
	 * Coming soon
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Coming soon
	 */
	public void setNode(Device node) {
		this.node = node;
	}

	public double getDeg() {
		return deg;
	}
	
	public double getDec() {
		return dec;
	}
	
	public int getN() {
		return n;
	}
	
	public void setDeg(double deg) {
		this.deg = deg ;
	}
	
	public void setDec(double dec) {
		this.dec = dec ;
	}
	
	/**
	 * Clone the sensor unit
	 */
	@Override
	public MediaSensorUnit clone() throws CloneNotSupportedException {
		MediaSensorUnit newCU = (MediaSensorUnit) super.clone();
		Layer.getMapViewer().addKeyListener(newCU);
		return newCU;
	}
	
}
