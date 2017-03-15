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

package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class TilesD implements Runnable {

	private int zoom = 0;

	public TilesD(int zoom) {
		this.zoom = zoom;
	}

	public void start() {
		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
//			double lat1 = 37.12;
//			double lon1 = 4.22;
//			double lat2 = 36.35;
//			double lon2 = 5.9;
			
			double lat1 = 43.6;
			double lon1 = -4.30;
			double lat2 = 43.30;
			double lon2 = -3.5;			

			int xtile1 = (int) Math.floor((lon1 + 180) / 360 * (1 << zoom));
			int ytile1 = (int) Math.floor((1 - Math.log(Math.tan(Math
					.toRadians(lat1)) + 1 / Math.cos(Math.toRadians(lat1)))
					/ Math.PI)
					/ 2 * (1 << zoom));
			String s1 = "" + zoom + "/" + xtile1 + "/" + ytile1;
			System.out.println(s1);

			int xtile2 = (int) Math.floor((lon2 + 180) / 360 * (1 << zoom));
			int ytile2 = (int) Math.floor((1 - Math.log(Math.tan(Math
					.toRadians(lat2)) + 1 / Math.cos(Math.toRadians(lat2)))
					/ Math.PI)
					/ 2 * (1 << zoom));
			String s2 = "" + zoom + "/" + xtile2 + "/" + ytile2;
			System.out.println(s2);
			File f;
			f = new File("tiles/" + zoom);
			f.mkdirs();
			FileOutputStream fos;
			URL u;
			InputStream fis;
			int x;
			System.out.println(xtile1 + " : " + xtile2 + " : " + ytile1 + " : "
					+ ytile2);
			//xtile1 = 32 ;
			//xtile2 = (int) Math.pow(2, zoom);
			//ytile1 = 0 ;
			//ytile2 = (int) Math.pow(2, zoom);;
			for (int i = xtile1; i < xtile2; i++) {
				//System.out.println(i);
				f = new File("tiles/" + zoom + "/" + i);
				f.mkdir();
				for (int j = ytile1; j < ytile2; j++) {
					System.out.println(i+","+j);
					try {
						u = new URL("http://otile1.mqcdn.com/tiles/1.0.0/osm/"
							+ zoom + "/" + i + "/" + j + ".png");
						System.out.println("Downloading : http://otile1.mqcdn.com/tiles/1.0.0/osm/"
						+ zoom + "/" + i + "/" + j + ".png");
						System.out.println("dec");
						fis = u.openStream();
						System.out.println("open");
						fos = new FileOutputStream("tiles/" + zoom + "/" + i + "/"
							+ j + ".png");
						//Thread.sleep(20);
						System.out.println("deb");
						while ((x = fis.read()) != -1) {
							System.out.print("-");
							fos.write(x);
						}
						System.out.println("\nfin");
						fis.close();
						fos.close();
					} catch (Exception e) {System.err.println("ERROR : "+"tiles/" + zoom + "/" + i + "/"+ j + ".png");}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
