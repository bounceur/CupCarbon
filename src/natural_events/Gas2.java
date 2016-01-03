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

package natural_events;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;

import device.Device;
import device.DeviceWithoutRadio;
import map.Layer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Gas2 extends DeviceWithoutRadio {

	public static String idFL = "G" ; // ID First Letter
	protected int [] polyX = new int[62];
	protected int [] polyY = new int[62];
	protected double value = 0.0f ;
	
	public Gas2(double x, double y, double radius, int id) {
		super(x, y, radius, id);
	}
	
	public Gas2(String x, String y, String radius, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius), id);
	}

	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
			int x = coord[0];
			int y = coord[1];
			int rayon = MapCalc.radiusInPixels(this.radius) ;		
			
			if (inside || selected) {
				g.setColor(UColor.ORANGE);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon+2, y-rayon-3);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon-3, y-rayon+2);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon+2, y+rayon+3);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon-3, y+rayon-2);			
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon-2, y-rayon-3);
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon+3, y-rayon+2);
				
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon-2, y+rayon+3);
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon+3, y+rayon-2);
			}
			
			/*
			if(inside) {			
				g.setColor(Couleur.ROUGEF_TRANSPARENT);			
			}
			else
				g.setColor(Couleur.ROUGE_TRANSPARENT);
	
			switch(hide) {
			case 0 : g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			case 1 : g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2); 
			}
			*/
					
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			g.setColor(Color.orange);		
			g.fillOval(x-6, y-6, 12, 12);
			
			double rayon2=0;
			double rayon3=0;

			Random rnd = new Random();

			int k=0;	
			double v ;
			for(double i=0.1; i<6.28; i+=.1) {			
				v = MapCalc.radiusInPixels(this.radius)*(rnd.nextInt(8))/100.;
				rayon2 = MapCalc.radiusInPixels((radius+v)*Math.cos(i)/1.) ;
				v = MapCalc.radiusInPixels(this.radius)*(rnd.nextInt(8))/100.;
				rayon3 = MapCalc.radiusInPixels((radius+v)*Math.sin(i)/1.) ;
				polyX[k]=(int)(x+rayon2);
				polyY[k]=(int)(y+rayon3);
				k++;
			}
			if(hide!=2) {
				g.setColor(UColor.ORANGE) ;
				g.drawPolygon(polyX, polyY, 62);
			}
			if(hide==0) {
				g.setColor(UColor.ORANGE_TRANSPARENT);
				g.fillPolygon(polyX, polyY, 62);				
			}
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawRadius(x,y,rayon,g);
			if(displayRadius) {
				drawRadius(x, y, rayon, g);
			}
			
			if(displayDetails) {
				g.setColor(Color.RED);
				g.drawString("["+getValue()+"]", x+15, y+20);
			}
			
			if(underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(UColor.RED);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId2(x,y,g);
		}
	}
	
	@Override
	public void run() {
		selected = false ;
		underSimulation = true ;
		fixori();
		value = 0.0f;
		radius = 0;
		for(int i=0;i<1000; i++) {
			simNext();
			Layer.getMapViewer().repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		toOri();
		underSimulation = false ;
		thread = null ;
		Layer.getMapViewer().repaint();
	}

	@Override
	public int getType() {
		return Device.GAS;
	}
	
	@Override
	public double getSensorUnitRadius() {
		return radius ;
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
	public long getNextTime() { return 0 ;}
	
	@Override
	public void loadRouteFromFile() {}
	
	@Override
	public void moveToNext(boolean visual, int visualDelay) {}
	
	@Override
	public boolean canMove() {return false;}

	public void loadScript() {}
	
	public double getValue() {
		return Math.round(value*1000)/1000;
	}
	
	public void init() {
		selected = false ;
		underSimulation = true ;
		fixori();
		value = 0.0f;
		radius = 0;
	}
	
	public void simNext() {
		radius+=0.2 ;
		value+=0.2;
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
	public boolean radioDetect(Device device) {
		return false;
	}
}
