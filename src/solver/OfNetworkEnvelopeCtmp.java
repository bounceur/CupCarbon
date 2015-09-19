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

import map.Layer;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Reinhardt Euler
 * @author Marc Sevaux
 * @author Bernard Pottier 
 * @author Ali Benzerbadj
 * @author Farid Lalem
 * @author Massinissa Saoudi
 * @version 1.0
 */
public class OfNetworkEnvelopeCtmp extends Thread {
	
	@Override
	public void run() {
		
		int p1;
		//double px1 = 0;
		//double py1 = 0;
		int p2;
		//double px2 = 0;
		//double py2 = 0;
		int p3;
		//double px3 = 0;
		//double py3 = 0;
		//int p4;
		//double px4 = 0;
		//double py4 = 0;
		
		double min = 10000000;
		
		int imin = 0;
		
		for (int i = 0; i < DeviceList.getNodes().size(); i++) {
			DeviceList.getNodes().get(i).setValue(0);
			DeviceList.getNodes().get(i).setMarked(false);
			if(min>DeviceList.getNodes().get(i).getLatitude()) {
				min = DeviceList.getNodes().get(i).getLatitude();
				imin = i;
			}
		}
		DeviceList.getNodes().get(imin).setMarked(true);
		Layer.getMapViewer().repaint();		
		
		try {
			sleep(500);
		} catch (InterruptedException e) {}		
		//DeviceList.getNodes().get(imin).setAlgoSelect(false);
		int cur = imin;		
		Device n1 = DeviceList.getNodes().get(cur);
		Device n2 ;
		double x1 = n1.getLatitude();
		double y1 = n1.getLongitude();
		double x2 = 0;
		double y2 = 0;
		double angle ;
		
		System.out.println(cur);
		p1 = cur ;
		//px1 = x1 ;
		//py1 = y1 ;
		//for(int i=0; i<DeviceList.getNodes().size(); i++) {
			min = 10000000;
			imin = -1;
			for(int j=0; j<DeviceList.getNodes().size(); j++) {
				n2 = DeviceList.getNodes().get(j);
				if((j!=cur) && (!n2.isMarked()) && n1.radioDetect(n2)) {
					x2 = n2.getLatitude();
					y2 = n2.getLongitude();
					angle = getAngle(x1,y1,x2,y2);					
					System.out.println("    "+j+" : "+Math.toDegrees(angle));
					if(angle<min) {
						min = angle;
						imin = j;
					}
				}
			}
			cur = imin;
			System.out.println("  > "+cur);
			DeviceList.getNodes().get(cur).setMarked(true);
			Layer.getMapViewer().repaint();
			x2 = DeviceList.getNodes().get(cur).getLatitude();
			y2 = DeviceList.getNodes().get(cur).getLongitude();
			p2 = cur ;
			//px2 = x2;
			//py2 = y2;
			n1 = DeviceList.getNodes().get(cur);
			
			System.out.println(cur);
			min = 10000000;
			imin = -1;
			for(int j=0; j<DeviceList.getNodes().size(); j++) {
				n2 = DeviceList.getNodes().get(j);
				if((j!=cur) && (!n2.isMarked()) && n1.radioDetect(n2)) {
					x2 = n2.getLatitude();
					y2 = n2.getLongitude();
					angle = getAngle(x1,y1,x2,y2);					
					System.out.println("    "+j+" : "+Math.toDegrees(angle));
					if(angle<min) {
						min = angle;
						imin = j;
					}
				}
			}
			cur = imin;
			System.out.println("  > "+cur);
			DeviceList.getNodes().get(cur).setMarked(true);
			Layer.getMapViewer().repaint();
			x2 = DeviceList.getNodes().get(cur).getLatitude();
			y2 = DeviceList.getNodes().get(cur).getLongitude();
			p3 = cur ;
			//px3 = x2 ;
			//py3 = y2 ;
			
			System.out.println("---> "+p1+" "+p2+" "+p3);
		//}

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
}
