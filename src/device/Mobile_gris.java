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

import java.awt.Color;
import java.awt.Graphics;

import utilities.MapCalc;
import utilities.UColor;

public class Mobile_gris extends MobileG {
	
	private static String idFL = "M" ; // ID First Letter

	public Mobile_gris(double x, double y, double rayon, int id) {
		super(x, y, rayon,"", id);
	}
	
	public Mobile_gris(double x, double y, double rayon, String gpsFileName, int id) {
		super(x, y, rayon, gpsFileName, id);	
	}
	
	public Mobile_gris(String x, String y, String rayon, String gpsFileName, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(rayon), gpsFileName, id);	
	}
		
	@Override
	public void draw(Graphics g) {	
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
			int x = coord[0];
			int y = coord[1];	
			//int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;		
			int rayon = MapCalc.radiusInPixels(this.radius) ;
					
			if (inside || selected) {
				g.setColor(UColor.NOIR_TRANSPARENT);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon+2, y-rayon-3);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon-3, y-rayon+2);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon+2, y+rayon+3);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon-3, y+rayon-2);			
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon-2, y-rayon-3);
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon+3, y-rayon+2);			
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon-2, y+rayon+3);
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon+3, y+rayon-2);
			}				
	
			switch(hide) {
			case 0 : {
				g.setColor(UColor.NOIR_TTRANSPARENT);
				g.fillRect(x - rayon, y - rayon, rayon * 2, rayon * 2);
			}
			case 1 : 
				g.setColor(UColor.WHITE_LTRANSPARENT);			
				g.drawRect(x - rayon, y - rayon, rayon * 2, rayon * 2); 
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawRadius(x,y,rayon,g);
			if(displayRadius) {
				drawRadius(x, y, rayon, g);
			}
			
			if(underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(Color.blue);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId(x,y,g);
		}
	}
	
	
	@Override
	public int getType() {
		return Device.MOBILE;
	}

	@Override
	public String getIdFL() {
		return idFL ;
	}
	
	@Override
	public String getNodeIdName() {
		return getIdFL()+id;
	}
	
	public void loadScript() {}

}