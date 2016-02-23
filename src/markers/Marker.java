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

package markers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;

import device.Device;
import device.DeviceList;
import device.DeviceWithRadio;
import device.StdSensorNode;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

public class Marker extends Device {

	private static String idFL = "M" ; // ID First Letter	
	
	public Marker(double x, double y, double z, double radius) {
		super(x, y, z, radius, 0);
	}
	
	public Marker(String x, String y, String z, String radius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(radius), 0);
	}
		
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(0.5f));
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToPixelMapA(latitude, longitude);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;		
			int rayon = MapCalc.radiusInPixels(this.radius) ;
					
			if (inside || selected) {
				g.setColor(UColor.BLACK_TRANSPARENT);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon+2, y-rayon-3);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon-3, y-rayon+2);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon+2, y+rayon+3);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon-3, y+rayon-2);			
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon-2, y-rayon-3);
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon+3, y-rayon+2);			
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon-2, y+rayon+3);
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon+3, y+rayon-2);
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-2, y - rayon-2, (rayon+2) * 2, (rayon+2) * 2);
			}	
			
			drawMoveArrows(x,y,g) ;
			
			if(hide==0) {
				g.setColor(UColor.RED);
				g.fillOval(x-2, y-2, 4, 4);
			}
		}
	}	
	
	public static Marker getCentre(Marker marker1, Marker marker2, boolean b) {
		// b = true : the created node will be selected
		double x1 = marker1.getLongitude();
		double y1 = marker1.getLatitude();
		double z1 = marker1.getElevation();
		double x2 = marker2.getLongitude();
		double y2 = marker2.getLatitude();
		double z2 = marker2.getElevation();
		
		double x = x1+((x2-x1)/2.0);
		double y = y1+((y2-y1)/2.0);
		double z = z1+((z2-z1)/2.0);
		Marker marker = new Marker(x, y, z, 4) ; 
		if(b) marker.setSelection(true);
		return marker;
	}
	
	@Override
	public void run() {
		selected = false ;
	}

	@Override
	public int getType() {
		return Device.MARKER;
	}
	
	@Override
	public String getIdFL() {
		return idFL ;
	}
	
	@Override
	public String getNodeIdName() {
		return getIdFL()+id;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);
		if(e.getKeyChar()=='u') {
			insertMarker();
		}
		
		if(e.getKeyChar()=='t') {
			transformMarkerToSensor();
		}
	}
	
	public void insertMarker() {
		if(selected) {
			int ix = MarkerList.getIndex(this)+1 ;
			if(ix<MarkerList.size()) {
				MapLayer.addMarker(ix, getCentre(this, MarkerList.get(ix), true));
			}
		}
	}	
	
	public void transformMarkerToSensor() {
		if(selected) {
			DeviceList.add(new StdSensorNode(longitude, latitude, elevation, 0, 100, -1));
		}
	}
	
	public int getInsideRadius() {
		return 6;
	}

	@Override
	public void setRadioRadius(double radiuRadius) {}

	@Override
	public void setSensorUnitRadius(double captureRadius) {}
	
	@Override
	public String getGPSFileName() {
		return "" ;
	}

	@Override
	public void setScriptFileName(String comFileName) {
		
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
	public boolean hasNext() {
		return false;
	}
	
	public void loadScript() {}

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
	public void initForSimulation() {
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
	
	@Override
	public void initBuffer() {
		
	}
}