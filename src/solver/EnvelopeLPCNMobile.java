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

import device.Device;
import device.DeviceList;
import device.SensorNode;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public class EnvelopeLPCNMobile extends Thread {

protected boolean loop = true ; 
	
	private int delayTime = 10; 

	@Override	
	public void run() {

		List<SensorNode> nodes = DeviceList.sensors;
		List<Device> mobiles = DeviceList.getMobileNodes();

		SensorNode n1, n2;

		double xp1 = 0;
		double yp1 = 0;
		double xp2 = 0;
		double yp2 = 0;
		
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		double xc = 0;
		double yc = 0;
		double angle = 0;
		int current = 0;
		int first = 0;
		int previous = 0;
		double min = 0;
		int imin = 0;
		boolean stop = false;
		
		for(Device mob : mobiles) {
			mob.loadRouteFromFile();
			mob.fixori();
		}
		
		DeviceList.initAll();
		DeviceList.addHull();
		while(loop) {		
			DeviceList.initLastHull();
			min = 1000;
			imin = 0;
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).loadRouteFromFile();
				nodes.get(i).fixori();
				nodes.get(i).setMarked(false);
				if(nodes.get(i).getLongitude()<min) {
					min = nodes.get(i).getLongitude();
					imin = i; 
				}
			}
			first = imin;
			previous = imin;
			current = imin;
			nodes.get(imin).setMarked(true);
			MapLayer.repaint();
			DeviceList.addToLastHull(imin);
	
			delay();
	
			n1 = nodes.get(current);
			xc = n1.getLongitude();
			yc = n1.getLatitude();
	
			x1 = xc - 0.1;
			y1 = yc;
	
			stop = false;
			boolean intersection = false ;
			while (!stop) {		
				for(Device mob : mobiles) {
					if(mob.hasNext())
						mob.moveToNext(true, delayTime);
				}
				min = 1000;
				imin = -1;
				for (int j = 0; j < nodes.size(); j++) {						
					n2 = nodes.get(j);
					if(n2.hasNext())
						n2.moveToNext(true, delayTime);
					if (!nodes.get(j).isDead()) 
						if ((current != j) && (n1.radioDetect(n2) || n2.radioDetect(n1))) {
							if (j != previous) {							
								x2 = n2.getLongitude();
								y2 = n2.getLatitude();
								angle = getAngle(x1 - xc, y1 - yc, x2 - xc, y2 - yc);
								intersection = false;
								int k = DeviceList.getLastHullSize()-1;
								while(k>0 && !intersection) {
									xp1 = nodes.get(DeviceList.getLastHull().get(k-1)).getLongitude();
									yp1 = nodes.get(DeviceList.getLastHull().get(k-1)).getLatitude();
									xp2 = nodes.get(DeviceList.getLastHull().get(k)).getLongitude();
									yp2 = nodes.get(DeviceList.getLastHull().get(k)).getLatitude();
									intersection = intersect(xp1, yp1, xp2, yp2, xc, yc, x2, y2);
									k--;
								}
								if ((angle < min) && (!intersection)) {
									imin = j;
									min = angle;
								}
							}
						}
				}
	
				if (imin == first)
					stop = true;
				
				if (imin == -1)
					imin = current;	
				
				nodes.get(imin).setMarked(true);
				MapLayer.repaint();				
				DeviceList.addToLastHull(imin);
	
				previous = current;
				n1 = nodes.get(imin);
				current = imin;
				x1 = xc;
				y1 = yc;
				xc = nodes.get(imin).getLongitude();
				yc = nodes.get(imin).getLatitude();
				delay();
			}
			
			for(int d=0; d<20; d++) {
				nodes.get(100).moveToNext(true, delayTime);
				mobiles.get(0).moveToNext(true, delayTime);
			}
		}
		for(Device mob : mobiles) {
			mob.toOri();
			mob.stopSimByAlgo();
		}
		System.out.println("FINISH !");
	}

	public double getAngle(double x1, double y1, double x2, double y2) {
		double a = Math.atan2(x1, y1);
		if (a < 0)
			a = (2 * Math.PI) + a;
		double b = Math.atan2(x2, y2);
		if (b < 0)
			b = (2 * Math.PI) + b;
		b = b - a;
		if (b < 0)
			b = (2 * Math.PI) + b;
		return b;
	}

	public static boolean intersect(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		if(x0==x2 && y0==y2) return false;
		if(x0==x3 && y0==y3) return false;
		if(x1==x2 && y1==y2) return false;
		if(x1==x3 && y1==y3) return false;
		double dx1, dy1, dx2, dy2;
		dx1 = x1 - x0;
		dy1 = y1 - y0;
		dx2 = x3 - x2;
		dy2 = y3 - y2;

		double s, t;
		s = (-dy1 * (x0 - x2) + dx1 * (y0 - y2)) / (-dx2 * dy1 + dx1 * dy2);
		t = (dx2 * (y0 - y2) - dy2 * (x0 - x2)) / (-dx2 * dy1 + dx1 * dy2);
		
		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			return true;
		}
		return false;
	}

	public void delay() {
		try {
			sleep(delayTime);
		} catch (InterruptedException e) {
		}
	}
	
	public void stopAlgorithm() {
		loop = false;
	}
}
