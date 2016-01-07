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

import java.awt.Graphics;

import project.Project;

public abstract class MobileGWR extends DeviceWithRadio {

	//public String gpsFileName = "";
	public int gpsId = 0;
	public static int gpsNbr = 0;

	public MobileGWR(double x, double y, double z, double radius, double radioRadius,
			String gpsFileName, int id) {
		super(x, y, z, radius, radioRadius, id);
		mobile = true;
		if (gpsFileName.equals("")) {
			gpsId = ++gpsNbr;
			gpsFileName = Project.getProjectGpsPath() + "/gps" + gpsId + ".gps";
		} else
			this.gpsFileName = gpsFileName;
	}

	@Override
	public String getGPSFileName() {
		if (gpsFileName.equals(""))
			gpsFileName = "-";
		return gpsFileName;
	}	

	public abstract void draw(Graphics g);
	
	/*
	@Override
	public void run() {
		state = this.getState();
		selected = false;
		underSimulation = true;
		// ------ Mobile -----
		double totalDistance = 0;
		selected = false;
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x2, y2;
		try {
			fis = new FileInputStream(gpsFileName);
			b = new BufferedReader(new InputStreamReader(fis));
			underSimulation = true;
			String desc_str = b.readLine();
			String from_str = b.readLine();
			String to_str = b.readLine();
			System.out.println("Description : " + desc_str);
			System.out.println("From : " + from_str);
			System.out.println("To : " + to_str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ------ END Mobile ----
		long tmpTime = -3600000;
		long cTime = 0;
		long toWait = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		try {
			while (((s = b.readLine()) != null)) {
				x2 = x;
				y2 = y;
				ts = s.split(" ");
				cTime = simpleDateFormat.parse(ts[0]).getTime();
				toWait = cTime - tmpTime;
				tmpTime = cTime;
				x = Double.parseDouble(ts[1]);
				y = Double.parseDouble(ts[2]);
				if (firstTime)
					firstTime = false;
				else {
					// System.out.println((int) MapCalc.distance(x, y, x2,
					// y2));
					totalDistance += MapCalc.distance(x, y, x2, y2);
				}
				try {
					Thread.sleep(toWait / 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Layer.getMapViewer().repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(totalDistance);
		underSimulation = false;
		thread = null;
		try {
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run2() {
		double totalDistance = 0;
		selected = false;
		boolean firstTime = true;
		try {
			FileInputStream fis;
			fis = new FileInputStream(gpsFileName);
			BufferedReader b = new BufferedReader(new InputStreamReader(fis));
			underSimulation = true;
			String[] ts;
			String s;
			double x2, y2;
			String desc_str = b.readLine();
			String from_str = b.readLine();
			String to_str = b.readLine();
			System.out.println("Description : " + desc_str);
			System.out.println("From : " + from_str);
			System.out.println("To : " + to_str);
			while ((s = b.readLine()) != null) {
				x2 = x;
				y2 = y;
				ts = s.split(" ");
				x = Double.parseDouble(ts[0]);
				y = Double.parseDouble(ts[1]);
				if (firstTime)
					firstTime = false;
				else {
					System.out.println((int) MapCalc.distance(x, y, x2, y2));
					totalDistance += MapCalc.distance(x, y, x2, y2);
				}
				Layer.getMapViewer().repaint();
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("-----------------");
			System.out.println(totalDistance);
			JOptionPane.showMessageDialog(new JFrame(), "" + totalDistance);
			Layer.getMapViewer().repaint();
			underSimulation = false;
			thread = null;
			b.close();
		} catch (Exception e1) {
		}
	}
	*/
//	@Override
//	public double getSensorUnitRadius() {
//		return radius;
//	}

}
