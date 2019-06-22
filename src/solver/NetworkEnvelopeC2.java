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

import java.util.LinkedList;

import map.MapLayer;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public class NetworkEnvelopeC2 extends Thread {
		
	private LinkedList<Integer> existList ;
	
	@Override
	public void run() {
		
		existList = new LinkedList<Integer>();
		
		//Device n1;
		Device n2;

		int p1;
		double px1 = 0;
		double py1 = 0;
		int p2;
		double px2 = 0;
		double py2 = 0;
		int p3;
		double px3 = 0;
		double py3 = 0;
		//int p4;
		double px4 = 0;
		double py4 = 0;
		
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		double angle = 0;
		int cur = 0;
		double min = 10000000;
		int imin = 0;
		
		for (int i = 0; i < DeviceList.sensors.size(); i++) {
			DeviceList.sensors.get(i).setValue(0);
			DeviceList.sensors.get(i).setMarked(false);
			if(min>DeviceList.sensors.get(i).getLatitude()) {
				min = DeviceList.sensors.get(i).getLatitude();
				imin = i;
			}
		}
		DeviceList.sensors.get(imin).setMarked(true);
		MapLayer.repaint();		
		
		try {
			sleep(500);
		} catch (InterruptedException e) {}		
		//DeviceList.sensors.get(imin).setAlgoSelect(false);
		
		existList.add(imin);
		cur = imin ;
		
		x1 = DeviceList.sensors.get(imin).getLatitude();
		y1 = DeviceList.sensors.get(imin).getLongitude();
		
		p1 = imin;
		px1 = x1 ;
		py1 = y1 ;
		px4 = x1 ;
		py4 = y1 ;
		
		min = 10000000;
		imin = 0;
		//n1 = DeviceList.sensors.get(cur);
		//System.out.println(cur);
		for (int j = 0; j < DeviceList.sensors.size(); j++) {
			n2 = DeviceList.sensors.get(j);				
			if ((cur!=j) && !exist(j)) {					
				x2 = n2.getLatitude();					
				y2 = n2.getLongitude();								
				angle = getAngle(x1, y1, x2, y2);
				//System.out.println("  "+j+" "+x2+" "+y2+" ("+Math.toDegrees(angle)+")");
				if(angle < min) {
					imin = j;
					min = angle;
				}
			}
		}
		//System.out.println("    "+imin);
		//System.out.println("--------");
		existList.add(imin);
		cur = imin;
		DeviceList.sensors.get(imin).setMarked(true);
		MapLayer.repaint();
		p2 = imin;
		px2 = x2 ;
		py2 = y2 ;
		
		try {
			sleep(500);
		} catch (InterruptedException e) {}
		//DeviceList.sensors.get(imin).setAlgoSelect(false);
		
		for (int i = 0; i < DeviceList.sensors.size(); i++) {
			min = 10000000;
			imin = 0;
			//n1 = DeviceList.sensors.get(cur);
			//System.out.println(cur);
			for (int j = 0; j < DeviceList.sensors.size(); j++) {
				n2 = DeviceList.sensors.get(j);				
				if ((cur!=j) && !exist(j)) {					
					x2 = n2.getLatitude();					
					y2 = n2.getLongitude();								
					angle = getAngle(x1, y1, x2, y2);
					//System.out.println("  "+j+" "+x2+" "+y2+" ("+Math.toDegrees(angle)+")");
					if(angle < min) {
						imin = j;
						min = angle;
					}
				}
			}
			//System.out.println("    "+imin);
			//System.out.println("--------");
			
			p3 = imin;
			px3 = x2 ;
			py3 = y2 ;
			
			double v = getAngle2(px1-px2, py1-py2, px3-px2, py3-py2);
			System.out.print(p1+" "+p2+" "+p3+" "+v);
			//if(v<Math.PI) {
			if(v>0) {
				System.out.println(" oui");
				existList.add(imin);
				cur = imin;
				DeviceList.sensors.get(imin).setMarked(true);
				MapLayer.repaint();
				try {
					sleep(500);
				} catch (InterruptedException e) {}
				//DeviceList.sensors.get(imin).setAlgoSelect(false);
				p1 = p2;
				p2 = p3;
				px4 = px1;
				py4 = py1;
				px1 = px2;
				py1 = py2;
				px2 = px3;
				py2 = py3;
			}
			else {
				System.out.println(" non");
				px2 = px1;
				py2 = py1;
				px1 = px4;
				py1 = py4;
			}
			
		}	
		System.out.println("Finish !");
	}

	public double getAngle(double x1, double y1, double x2, double y2) {
		x2 = x2 - x1 ;
		y2 = y2 - y1 ;		
		double a = Math.atan2(x2, y2);
		if (a<0) a = (2*Math.PI)+a;		
		return a;
	}
	
	public double getAngle2(double x1, double y1, double x2, double y2) {
		double b = (x1*y2)-(x2*y1);
		/*double a = Math.atan2(x1, y1);
		if (a<0) a = (2*Math.PI)+a;		
		double b = Math.atan2(x2, y2);
		if (b<0) b = (2*Math.PI)+b;
		b = b - a;
		if (b<0) b = (2*Math.PI)+b;*/ 
		return b;
	}
	
	public boolean exist(int j) {
		for(Integer e : existList) {
			if(e==j) return true; 
		}
		return false;
	}
}
