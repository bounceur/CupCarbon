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

//import java.awt.event.MouseListener;
import device.Device;
import device.DeviceList;
import device.MapObject;
import device.StdSensorNode;
import map.MapLayer;
import utilities.MapCalc;
import utilities.UColor;

public class Marker extends MapObject {

	private static String idFL = "U" ; // ID First Letter	
	
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
			int rayon = MapCalc.radiusInPixels(this.radius) ;
					
			if (inside || selected) {
				g.setColor(Color.GRAY);
				if(MapLayer.dark) g.setColor(Color.LIGHT_GRAY);
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
				g.setColor(Color.GRAY);
				if(MapLayer.dark) g.setColor(Color.LIGHT_GRAY);
				g.drawOval(x - rayon-2, y - rayon-2, (rayon+2) * 2, (rayon+2) * 2);
			}	
			
			if(hide==0) {
				g.setColor(UColor.BLUE);
				g.fillOval(x-3, y-3, 6, 6);
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
		marker.setSelected(b);
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
	public String getName() {
		return getIdFL()+id;
	}
	
	public void insertInAll() {
		int i = MarkerList.getIndex(this)+1 ;
		if(i<MarkerList.size()) {
			MapLayer.addMarker(i, getCentre(this, MarkerList.get(i), true));
		}
	}
	
	public void insertAfterSelectedMarker() {
		if(selected) {
			int i = MarkerList.getIndex(this)+1 ;
			if(i<MarkerList.size()) {
				MapLayer.addMarker(i, getCentre(this, MarkerList.get(i), true));
			}
		}
	}
	
	public void insertMarkerAndDeselect() {
		if(selected) {
			selected = false;
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

	public Marker cloneMarker() throws CloneNotSupportedException {
		Marker newNode = (Marker) super.clone();
		return newNode;
	}

	@Override
	public void initGeoZoneList() {}
	
}