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

public abstract class MobileWithRadio extends MobileGWR {

	private static String idFL = "W" ; // ID First Letter 
	
	public MobileWithRadio(double x, double y, double z, double rayon, double radioRadius, int id) {
		this(x, y, z, rayon, radioRadius,"", id);
		withRadio = true ;		
	}
	
	public MobileWithRadio(double x, double y, double z, double rayon, double radioRadius, String gpsFileName, int id) {
		super(x, y, z, rayon, radioRadius, gpsFileName, id);	
	}
	
	public MobileWithRadio(String x, String y, String z, String rayon, String radioRadius, String gpsFileName, int id) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), Double.valueOf(rayon), Double.valueOf(radioRadius), gpsFileName, id);	
	}
		
	@Override
	public void draw(Graphics g) {		
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToPixelMapA(longitude, latitude);
			int x = coord[0];
			int y = coord[1];	
			//int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;		
			int rayon = MapCalc.radiusInPixels(this.getCurrentRadioModule().getRadioRangeRadius()) ;
			int rayon2 = MapCalc.radiusInPixels(this.radius) ;
					
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
	
			switch(hide) {
			case 0 : {
				g.setColor(UColor.BLACK_TTTRANSPARENT);
				g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
				g.fillOval(x - rayon2, y - rayon2, rayon2 * 2, rayon2 * 2);
			}
			case 1 : 
				g.setColor(UColor.WHITE_LTRANSPARENT);
				g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2); 
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			
			//drawMoveArrows(x,y,g) ;
			//drawIncRedDimNode(x,y,g);
			//drawIncDimRadio(x,y,g);
			if(displayRadius) {
				drawRadius(x, y, rayon, g);
				drawRadioRadius(x, y, rayon2, g);
			}
			
			if(underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(Color.BLUE);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId(x,y,g);
		}
	}
		
	@Override
	public String getIdFL() {
		return idFL ;
	}
	
//	@Override
//	public void keyTyped(KeyEvent key) {
//		super.keyTyped(key);
//		if(selected) {
//			if(key.getKeyChar()==';') {
//				radius+=10 ;
//				MapLayer.repaint();
//			}
//			if(key.getKeyChar()==',') {
//				radius-=10 ;
//				MapLayer.repaint();
//			}
//		}
//	}
	
	@Override
	public String getName() {
		return getIdFL()+id;
	}
		
	public void loadScript() {}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initBattery() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initBuffer() {

	}
	
	public void init() {
		super.init();
		getCurrentRadioModule().setPl(100);
	}

}