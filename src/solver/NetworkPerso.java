/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
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

package solver;

import java.util.List;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @author Tahar Kechadi
 * @version 1.0
 */
public class NetworkPerso extends Thread {
	
protected boolean loop = true ; 
	
	@Override	
	public void run() {

		List<SensorNode> nodes = DeviceList.sensors;

		SensorNode n1, n2;
		
		double x, y;

		//double s = 0.001;
		//for(s=0.0001; s<0.1; s+=0.0001) {
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).setMarked(false);
				MapLayer.repaint();
			}
			//delay();
			for(int i = 0; i < nodes.size(); i++) {
				n1 = nodes.get(i);
				x=0;
				y=0;
				for (int j = 0; j < nodes.size(); j++) {
					if(i!=j) {
						n2 = nodes.get(j);
						if(n1.radioDetect(n2)) {
							x+=(n1.getLatitude()-n2.getLatitude());
							y+=(n1.getLongitude()-n2.getLongitude());
						}
					}
				}
				//System.out.println(i+" "+Math.abs(x)+" "+Math.abs(y));
				double d = Math.sqrt(x*x+y*y);
				n1.setValue(d);
				//System.out.println(i+" "+d);
				//if(Math.abs(x)>=0.001 || Math.abs(y)>=0.001) {
//				if(d>s) {
//					n1.setMarked(true);
//					Layer.getMapViewer().repaint();
//				}				
			}
			double max=0;
			int imax=0;
			for(int k = 0; k < 100; k++) {
				max = 0 ;
				imax = 0;
				for(int i = 0; i < nodes.size(); i++) {
					if((nodes.get(i).getValue()>max) && (!nodes.get(i).isMarked())) {
						max = nodes.get(i).getValue();
						imax = i;
					}
				}
				//System.out.println(imax);
				nodes.get(imax).setMarked(true);
				MapLayer.repaint();
				delay();
			}			
		//}
	}

	public void delay() {
		try {
			sleep(500);
		} catch (InterruptedException e) {
		}
	}
	
}
