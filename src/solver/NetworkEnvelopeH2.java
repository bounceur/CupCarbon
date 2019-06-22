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

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */
public class NetworkEnvelopeH2 extends Thread {

	@Override
	public void run() {

		SensorNode n1, n2, nv;

		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		double xc = 0;
		double yc = 0;
		double angle = 0;
		int cur = 0;
		int prem = 0;
		double min = 0;
		int imin = 0;
		boolean trouve = false;
		boolean fini = false;
		int sm = 0;

		DeviceList.initAll();
		DeviceList.addHull();				
		
		while (true) {
			DeviceList.initLastHull();
			min = 10000000;
			imin = 0;
			for (int i = 0; i < DeviceList.sensors.size(); i++) {
				DeviceList.sensors.get(i).setValue(0);
				DeviceList.sensors.get(i).setMarked(false);
				DeviceList.sensors.get(i).setVisited(false);
				if (!DeviceList.sensors.get(i).isDead())
					if (min > DeviceList.sensors.get(i).getLatitude()) {
						min = DeviceList.sensors.get(i).getLatitude();
						imin = i;
					}
			}
			prem = imin;
			cur = imin;
			DeviceList.sensors.get(imin).setMarked(true);
			DeviceList.sensors.get(imin).setVisited(true);
			MapLayer.repaint();
			DeviceList.addToLastHull(imin);

			delay();

			n1 = DeviceList.sensors.get(cur);
			xc = n1.getLatitude();
			yc = n1.getLongitude();
			x1 = xc - 0.1;
			y1 = yc;
			trouve = false;
			fini = false;
			for (int i = 0; i < DeviceList.sensors.size() - 1; i++) {
				if (!fini) {
					min = 10000000;
					trouve = false;
					for (int j = 0; j < DeviceList.sensors.size(); j++) {
						n2 = DeviceList.sensors.get(j);
						if (!DeviceList.sensors.get(j).isDead())
							if ((cur != j) && n1.radioDetect(n2)) {
								if (!n2.isVisited()) {
									trouve = true;
									x2 = n2.getLatitude();
									y2 = n2.getLongitude();
									angle = getAngle(x1 - xc, y1 - yc, x2 - xc,
											y2 - yc);
									if (angle < min
											&& Math.toDegrees(angle) > 30) {
										imin = j;
										min = angle;
									}
								}
							}
					}
					if (trouve) {
						DeviceList.sensors.get(imin).setMarked(true);
						DeviceList.sensors.get(imin).setVisited(true);
						MapLayer.repaint();
						DeviceList.addToLastHull(imin);

						n1 = DeviceList.sensors.get(imin);
						cur = imin;
						x1 = xc;
						y1 = yc;

						xc = DeviceList.sensors.get(imin).getLatitude();
						yc = DeviceList.sensors.get(imin).getLongitude();
					} else {
						for (int k = 0; k < DeviceList.sensors.size(); k++) {
							nv = DeviceList.sensors.get(k);
							if (n1.radioDetect(nv) && nv.isMarked()) {
								nv.setVisited(false);
							}
						}
					}
					sm = 0;
					for (int k = 0; k < DeviceList.sensors.size(); k++) {
						if ((k != prem)
								&& DeviceList
										.sensors
										.get(k)
										.radioDetect(
												DeviceList.sensors.get(prem))) {
							if (DeviceList.sensors.get(k).isMarked())
								sm++;
						}
					}
					if (sm >= 2)
						fini = true;
					delay();
				}
			}
			// System.out.println("FINISH !");
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public double getAngle1(double x1, double y1, double x2, double y2) {
		x2 = x2 - x1;
		y2 = y2 - y1;
		double a = Math.atan2(x2, y2);
		if (a < 0)
			a = (2 * Math.PI) + a;
		return a;
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

	public static boolean intersect(double p0_x, double p0_y, double p1_x,
			double p1_y, double p2_x, double p2_y, double p3_x, double p3_y) {
		double s1_x, s1_y, s2_x, s2_y;
		s1_x = p1_x - p0_x;
		s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;
		s2_y = p3_y - p2_y;

		double s, t;
		s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y))
				/ (-s2_x * s1_y + s1_x * s2_y);
		t = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x))
				/ (-s2_x * s1_y + s1_x * s2_y);

		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			// Collision detected
			return true;
		}

		return false; // No collision
	}

	public void delay() {
		// try {
		// sleep(5);
		// } catch (InterruptedException e) {}
	}
}
