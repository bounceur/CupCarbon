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

package flying_object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.Vector;

import device.Device;
import device.DeviceWithRadio;
import device.MobileG;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public class FlyingGroup extends MobileG {

	private static String idFL = "I"; // ID First Letter
	private Vector<FlyingObject> flyingObjects;
	private int flyingObjectNumber = 0;
	private int height = 100000000;
	private int width = 600000000;

	public FlyingGroup() {
		flyingObjects = new Vector<FlyingObject>();
	}

	public FlyingGroup(double x, double y, double z, double radius) {
		super(x, y, z, radius, "",-1);
		flyingObjects = new Vector<FlyingObject>();
	}

	public FlyingGroup(double x, double y, double z, double radius, int n) {
		this(x, y, z, radius);
		flyingObjectNumber = n ;
		flyingObjects = new Vector<FlyingObject>();
		for (int i = 0; i < n; i++) {
			flyingObjects.add(new FlyingObject(x, y, true));
		}
	}

	public FlyingGroup(double x, double y,double z, double radius, int n, String gpsFileName) {
		this(x, y, z, radius, n);
		setGPSFileName(gpsFileName) ;
	}

	public FlyingGroup(String xStr, String yStr, String zStr, String radiusStr, String nStr, String gpsFileName) {
		this(Double.valueOf(xStr), Double.valueOf(yStr), Double.valueOf(zStr), Double.valueOf(radiusStr), Integer.parseInt(nStr), gpsFileName);
	}

	public int getflyingObjectNumber() {
		return flyingObjectNumber ;
	}
	
	public Point closestLocation(double px, double py, double ox, double oy) {
		double dX = Math.abs(ox - px);
		double dY = Math.abs(oy - py);
		double x = ox;
		double y = oy;
		if (Math.abs(height - ox + px) < dX) {
			dX = height - ox + px;
			x = ox - height;
		}
		if (Math.abs(height - px + ox) < dX) {
			dX = height - px + ox;
			x = ox + height;
		}

		if (Math.abs(width - oy + py) < dY) {
			dY = width - oy + py;
			y = oy - width;
		}
		if (Math.abs(width - py + oy) < dY) {
			dY = width - py + oy;
			y = oy + width;
		}

		return new Point((int) x, (int) y);
	}

	public Point normalisePoint(Point p, double n) {
		if (sizeOfPoint(p) == 0.0) {
			return p;
		} else {
			double weight = n / sizeOfPoint(p);
			return new Point((int) (p.x * weight), (int) (p.y * weight));
		}
	}

	public double sizeOfPoint(Point p) {
		return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
	}

	public Point sumPoints(double p1x, double p1y, double w1, double p2x,
			double p2y, double w2) {
		return new Point((int) (w1 * p1x + w2 * p2x), (int) (w1 * p1y + w2
				* p2y));
	}

	@Override
	public void run() {
		underSimulation = true;
		for (FlyingObject flyingObject : flyingObjects) {
			flyingObject.start();
		}
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//underSimulation = false;
		//thread = null;
	}

	public void draw(Graphics g) {
		if (visible) {
			initDraw(g);
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
			//int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
			int rayon = MapCalc.radiusInPixels(this.radius);

			for (FlyingObject flyingObject : flyingObjects) {
				// 3
				flyingObject.draw(g);
			}

			if (inside || selected) {
				g.setColor(UColor.ORANGE);
				g.drawLine(x - rayon - 3, y - rayon - 3, x - rayon + 2, y
						- rayon - 3);
				g.drawLine(x - rayon - 3, y - rayon - 3, x - rayon - 3, y
						- rayon + 2);
				g.drawLine(x - rayon - 3, y + rayon + 3, x - rayon + 2, y
						+ rayon + 3);
				g.drawLine(x - rayon - 3, y + rayon + 3, x - rayon - 3, y
						+ rayon - 2);
				g.drawLine(x + rayon + 3, y - rayon - 3, x + rayon - 2, y
						- rayon - 3);
				g.drawLine(x + rayon + 3, y - rayon - 3, x + rayon + 3, y
						- rayon + 2);
				g.drawLine(x + rayon + 3, y + rayon + 3, x + rayon - 2, y
						+ rayon + 3);
				g.drawLine(x + rayon + 3, y + rayon + 3, x + rayon + 3, y
						+ rayon - 2);
			}

			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon - 4, y - rayon - 4, (rayon + 4) * 2,
						(rayon + 4) * 2);
			}
			g.setColor(UColor.WHITE_LTRANSPARENT);
			g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			drawMoveArrows(x, y, g);
			drawIncRedDimNode(x, y, g);
			drawRadius(x, y, rayon, g);

			if (displayDetails) {
				g.setColor(Color.RED);
			}

			if (underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x - 3, y - 3, 6, 6);
			} else {
				g.setColor(Color.BLUE);
				g.fillOval(x - 3, y - 3, 6, 6);
			}
			drawId(x, y, g);
		}
	}

	@Override
	public int getType() {
		return Device.FLYING_OBJECT;
	}

	@Override
	public double getSensorUnitRadius() {
		return radius;
	}

	@Override
	public String getIdFL() {
		return idFL;
	}

	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}

	@Override
	public void setRadioRadius(double radiuRadius) {
	}

	@Override
	public void setSensorUnitRadius(double captureRadius) {
	}

	@Override
	public void setScriptFileName(String scriptFileName) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		for (FlyingObject flyingObject : flyingObjects) {
			flyingObject.relativeMove(longitude, latitude);
		}
	}

	@Override
	public void setGPSFileName(String gpsFileName) {
		this.gpsFileName = gpsFileName ;
		for (FlyingObject flyingObject : flyingObjects) {
			flyingObject.setGPSFileName(gpsFileName);
		}
	}

	public void reproduire(int v) {
		for (int i = 0; i < v; i++) {
			FlyingObject si = new FlyingObject(longitude, latitude, true);
			si.setGPSFileName("gps/a1.gps");
			flyingObjects.add(si);
		}
	}	
	
	public FlyingObject getIthflyingObject(int i) {
		return flyingObjects.get(i);
	}
	
	public int size() {
		return flyingObjects.size();
	}
	
	@Override
	public double getNextTime() { return 0 ;}
	
	@Override
	public void loadRouteFromFile() {}
	
	@Override
	public void moveToNext(boolean visual, int visualDelay) {}
	
	@Override
	public boolean canMove() {return false;}
	
	@Override
	public void stopSimulation() {
		underSimulation = false;
		for (FlyingObject flyingObject : flyingObjects) {
			flyingObject.stopSimulation();
		}
		underSimulation = false;
		thread = null;
	}
	
	public void loadScript() {}
	
	public Vector<FlyingObject> getFlyingObjects() {
		return flyingObjects;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawRadioLinks(Graphics g) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void drawRadioPropagations(Graphics g) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initBattery() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public Polygon getRadioPolygon() {
		return null;
	}

	@Override
	public void calculatePropagations() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void resetPropagations() {

	}
	
	@Override
	public boolean radioDetect(DeviceWithRadio device) {
		return false;
	}

	@Override
	public void initGeoZoneList() {
		
	}
}

