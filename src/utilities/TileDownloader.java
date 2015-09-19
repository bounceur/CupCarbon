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

import java.io.IOException;

public class TileDownloader {

	public static void main(String[] args) throws IOException {
		for(int i=18; i<19; i++) {
		TilesD t1 = new TilesD(i);
		//TilesD t2 = new TilesD(1);
		t1.run() ;
		}
		//t2.start() ;
		// Brest
		// double lat1 = 49.0;
		// double lon1 = -5.80;
		// double lat2 = 47.60 ;
		// double lon2 = -2.4 ;
		// Bejaia
		

		// for(int i=0; i<1000; i++) {
		// File file2 = new
		// File("/Applications/MAMP/htdocs/capcarbon/tile/10/"+i);
		// file2.mkdir();
		// }

		// File file = new File("/Applications/MAMP/htdocs/capcarbon/tile/10");
		// File [] fichiers = file.listFiles();
		// int k=496;
		// for(int i=1; i<fichiers.length; i++) {
		// if(fichiers[i].getPath().endsWith("copie")) {
		// fichiers[i].renameTo(new
		// File("/Applications/MAMP/htdocs/capcarbon/tile/10/"+k));
		// k++;
		// }
		// }
		// for(int i=1; i<300; i++) {
		// System.out.println(i);
		// for(int j=0; j<300; j++) {
		// FileInputStream fis = new
		// FileInputStream("/Applications/MAMP/htdocs/capcarbon/tile/_10/0/"+noms[i]);
		// //System.out.println("/Applications/MAMP/htdocs/capcarbon/tile/_10/0/"+noms[i]);
		// FileOutputStream fos = new
		// FileOutputStream("/Applications/MAMP/htdocs/capcarbon/tile/10/"+j+"/"+noms[i]);
		// //System.out.println("/Applications/MAMP/htdocs/capcarbon/tile/10/"+j+"/"+noms[i]);
		// //System.out.println(fos);
		// while((x=fis.read())!=-1) {
		// fos.write(x);
		// }
		// fis.close();
		// fos.close();
		// }
		// }

		// URL u ;
		// InputStream is ;
		// FileOutputStream fos ;
		// int x;
		// for(int i=0; i<7; i++) {
		// for(int j=0; j<7; j++) {
		// u = new
		// URL("http://otile1.mqcdn.com/tiles/1.0.0/osm/"+j+"/0/"+i+".png") ;
		// is = u.openStream() ;
		// fos = new FileOutputStream("tile/"+j+"/0/"+i+".png");
		// while((x=is.read()) != -1) {
		// fos.write(x);
		// }
		// fos.close();
		// }
		// }
		// System.out.println("fin");
	}
}
