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
 * @author Ali Benzerbadj
 * @author Farid Lalem
 * @author Massinissa Saoudi
 * @version 1.0
 */
public class NetworkEnvelopeP extends Thread {

	private boolean withoutRadioDetect = true;
	
	public NetworkEnvelopeP() {
		withoutRadioDetect = true;
	}
	
	public NetworkEnvelopeP(boolean withoutRadioDetect) {
		this.withoutRadioDetect = withoutRadioDetect;
	}
	
	@Override
	public void run() {

		int k = 1 ; // Number of visible sensors
		double m = 1 ; // Angle of the zone (1=90�, 2=45� ...)
		
		SensorNode n1, n2;
		
		int rank1 = 0;
		int rank2 = 0;
		int rank3 = 0;
		int rank4 = 0;
		int rank5 = 0;
		int rank6 = 0;
		int rank7 = 0;
		int rank8 = 0;

		double x1;
		double y1;
		double x2;
		double y2;
		double ay1;
		double ay2;
		double by1;
		double by2;
		
		List<SensorNode> nodes = DeviceList.sensors;

		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).setValue(0);
			nodes.get(i).setMarked(false);
		}

		for (int i = 0; i < nodes.size(); i++) {
			n1 = nodes.get(i);
			rank1 = 0;
			rank2 = 0;
			rank3 = 0;
			rank4 = 0;
			rank5 = 0;
			rank6 = 0;
			rank7 = 0;
			rank8 = 0;
			x1 = n1.getLatitude();
			y1 = n1.getLongitude();
			for (int j = 0; j < nodes.size(); j++) {				
				if (i != j) {		
					n2 = nodes.get(j);					
					x2 = n2.getLatitude();
					y2 = n2.getLongitude();
					//ay1 = -x2 + (x1 + y1);
					//ay2 = x2 + (y1 - x1);
					ay1 = y1-m*(x2-x1);
					ay2 = y1+m*(x2-x1);
					by1 = y1-(1./m)*(x2-x1);
					by2 = y1+(1./m)*(x2-x1); 

					if (n1.radioDetect(n2) || withoutRadioDetect) {
						if ((n1.getLongitude() < n2.getLongitude()) && (n1.getLatitude() < n2.getLatitude()))
							rank1++;
						if ((n1.getLongitude() > n2.getLongitude()) && (n1.getLatitude() < n2.getLatitude()))
							rank2++;
						if ((n1.getLongitude() > n2.getLongitude()) && (n1.getLatitude() > n2.getLatitude()))
							rank3++;
						if ((n1.getLongitude() < n2.getLongitude()) && (n1.getLatitude() > n2.getLatitude()))
							rank4++;
						
						if ((y2 > by1 && y2 < by2)) rank5++;											
						if ((y2 > ay1 && y2 > ay2)) rank6++;
						if ((y2 < by1 && y2 > by2)) rank7++;
						if ((y2 < ay1 && y2 < ay2)) rank8++;
					}
				}
			}
			//System.out.println(i+" "+(rank1+rank2+rank3+rank4)+" | "+rank1+" "+rank2+" "+rank3+" "+rank4);
			if ((rank1 < k) || (rank2 < k) || (rank3 < k) || (rank4 < k)) { 
				n1.setMarked(true); 
				//DeviceList.addEnvelope1(i); 
			}
			else
			if ((rank5 < k) || (rank6 < k) || (rank7 < k) || (rank8 < k)) { 
				n1.setMarked(true); 
				//DeviceList.addEnvelope1(i); 
			}

			MapLayer.repaint();
//			try {
//				sleep(100);
//			} catch (InterruptedException e) {}
		}	
		
//		double min = 10000000;
//		int imin=0;
//		DeviceList.envelope = new LinkedList<Integer>();
//		
//		
//		for (int i = 0; i < DeviceList.getNodes().size(); i++) {
//			DeviceList.getNodes().get(i).setRank(0);
//			DeviceList.getNodes().get(i).setVisited(false);
//			DeviceList.getNodes().get(i).setAlgoSelect(false);
//			if(min>DeviceList.getNodes().get(i).getY()) {
//				min = DeviceList.getNodes().get(i).getY();
//				imin = i;
//			}
//		}
//		DeviceList.getNodes().get(imin).setAlgoSelect(true);
//		DeviceList.getNodes().get(imin).setVisited(true);
//		DeviceList.envelope.add(imin);		
//		Layer.getMapViewer().repaint();
//		
//		
//		for(int i=0; i<DeviceList.getNodes().size()-2; i++) {
//			min = 10000000;
//			imin = -1;
//			for(int j=0; j<DeviceList.getNodes().size(); j++) {
//				node = DeviceList.getNodes().get(j);
//				if((j!=envelope.getLast()) && (!node.isVisited())) {
//					x = node.getY();
//					y = node.getX();
//					angle = getAngle(x0,y0,x,y);					
//					if(angle<min) {
//						min = angle;
//						imin = j;
//					}
//				}
//			}
//			DeviceList.getNodes().get(imin).setAlgoSelect(true);
//			DeviceList.getNodes().get(imin).setVisited(true);			
//			Layer.getMapViewer().repaint();
//			envelope.add(imin);
//		}
		
		System.out.println("Finish !");	
	}

}
