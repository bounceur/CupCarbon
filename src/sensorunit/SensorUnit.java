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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import device.Device;
import map.Layer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */
public class SensorUnit implements KeyListener, Cloneable {

	protected double radius = 10;
	protected double longitude;
	protected double latitude;
	protected Device node;
	protected boolean displayRadius = false;
	
	
	protected int n = 30;
	protected double deg = 0.209333;
	
	protected int [] polyX1 = new int[n];
	protected int [] polyY1 = new int[n];

	/**
	 * Constructor 1 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param node which is associated to this sensor unit
	 */
	public SensorUnit(double longitude, double latitude, Device node) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.node = node;
		calculateSensingArea();
		Layer.getMapViewer().addKeyListener(this);
	}

	/**
	 * Constructor 3 : radius is equal to 10 meter
	 * @param x Position of the sensor unit on the map
	 * @param y Position of the sensor unit on the map
	 * @param cuRadius the value of the radius 
	 * @param node which is associated to this sensor unit
	 */
	public SensorUnit(double longitude, double latitude, double radius, Device node) {
		this(longitude, latitude, node);
		this.radius = radius;
		calculateSensingArea();
	}	

	public void calculateSensingArea() {
		int rayon = MapCalc.radiusInPixels(radius) ; 
		
		double r2=0;
		double r3=0;
		
		double i=0.0;
		for(int k=0; k<n; k++) {
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

	/*public void drawDetectionRadius(int x, int y, int r1, Graphics g) {
		if(r1>0 && displayRadius) {
			g.setColor(UColor.WHITE_TRANSPARENT);
			g.drawLine(x,y,(int)(x-r1),(int)(y));
			g.drawString(""+radius,x-(r1/2),(int)(y-3));
		}
	}*/
	
	/**
	 * Draw the sensor unit
	 */
	public void draw(Graphics g, int mode, boolean detection) {
		 calculateSensingArea();
		if (!detection)
			g.setColor(UColor.WHITE_LLTRANSPARENT);
		if (detection)
			g.setColor(UColor.JAUNE_SENSOR);
	
		if (mode == 0)
			g.fillPolygon(polyX1, polyY1, n);
//			g.fillOval((int) longitude - MapCalc.radiusInPixels(radius), (int) latitude
//					- MapCalc.radiusInPixels(radius),
//					MapCalc.radiusInPixels(radius) * 2,
//					MapCalc.radiusInPixels(radius) * 2);
		g.setColor(UColor.NOIRF_TTTRANSPARENT);
		g.drawPolygon(polyX1, polyY1, n);
//		g.drawOval((int) longitude - MapCalc.radiusInPixels(radius),
//				(int) latitude - MapCalc.radiusInPixels(radius),
//				MapCalc.radiusInPixels(radius) * 2,
//				MapCalc.radiusInPixels(radius) * 2);
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (node.isSelected()) {
			if (key.getKeyChar() == ')') {
				radius += 5;
				Layer.getMapViewer().repaint();
			}
			if (key.getKeyChar() == '(') {
				radius -= 5;
				Layer.getMapViewer().repaint();
			}			
		}
		if (key.getKeyChar() == 'e') {
			displayRadius = true;
		}

		if (key.getKeyChar() == 'r') {
			displayRadius = false;
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

	/**
	 * Clone the sensor unit
	 */
	@Override
	public SensorUnit clone() throws CloneNotSupportedException {
		SensorUnit newCU = (SensorUnit) super.clone();
		Layer.getMapViewer().addKeyListener(newCU);
		return newCU;
	}
	
}
