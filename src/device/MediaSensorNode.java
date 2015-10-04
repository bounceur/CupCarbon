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

package device;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;


/**
 * @author Ahcene Bounceur
 * @version 1.0
 */

public class MediaSensorNode extends SensorNode {

	protected double mediaRadius = 40;
	protected boolean detection = false; 
	
	protected int mNPoint = 3;	
	protected double mDeg = 0.5;
	protected double dec = 0;
	
	protected int [] mPolyX = new int[mNPoint];
	protected int [] mPolyY = new int[mNPoint];
	
	public MediaSensorNode(double x, double y, double radius, double radioRadius,
			double cuRadius, int id) {
		super(x, y, radius, radioRadius, cuRadius, id);
	}
	

	public void calculateMediaSpace() {
		int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
		int x = coord[0];
		int y = coord[1];
		int radius = MapCalc.radiusInPixels(mediaRadius) ; 
		
		double r2=0;
		double r3=0;
		
		double i=dec;
		
		mPolyX[0] = x;
		mPolyY[0] = y;
		
		for(int k=1; k<mNPoint; k++) {
			r2 = radius*Math.cos(i);
			r3 = radius*Math.sin(i);
			mPolyX[k]=(int)(x+r2);
			mPolyY[k]=(int)(y+r3);
			i+=mDeg;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);
		key = e.getKeyChar();
		if (selected) {
			if (key=='p') {
				dec+=0.1;
			}
			if (key=='o') {
				dec-=0.1;
			}
			if (key == '$') {
				mediaRadius+=5;
			}
			if (key == '^') {
				mediaRadius-=5;
			}
			if (key == '`') {
				mDeg+=0.1;
			}
			if (key == 'ù') {
				mDeg-=0.1;
			}
			calculateMediaSpace();
			Layer.getMapViewer().repaint();
		}
	}

	@Override
	public void drawSensorUnit(Graphics g) {
		calculateMediaSpace();
		g.setColor(UColor.BLEU_TRANSPARENT);
		if (isSensorDetecting())
			g.setColor(UColor.VERTF_TRANSPARENT);
		if (visible) {
			g.fillPolygon(mPolyX, mPolyY, mNPoint);
		}
	}
	
	@Override
	public boolean detect(Device device) {		
		GeoPosition gp = new GeoPosition(device.getLongitude(),device.getLatitude());
		Point2D p1 = Layer.getMapViewer().getTileFactory().geoToPixel(gp, Layer.getMapViewer().getZoom());
		Polygon poly = new Polygon(mPolyX,mPolyY, mNPoint);
		detection = poly.contains(p1); 
		return (poly.contains(p1));
	}
	
	@Override
	public String getIdFL() {
		return "MS";
	}
	
}