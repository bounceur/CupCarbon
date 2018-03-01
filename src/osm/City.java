package osm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class City {

	//http://wiki.openstreetmap.org/wiki/Nominatim
	
	public static double [] getCityCenter(String name) {	
		double [] t = new double[2];
		try {			
			String host = "http://nominatim.openstreetmap.org/search.php?city="+name+"&limit=1&format=jsonv2";
			URL url = new URL(host);
			URLConnection uc = url.openConnection();		
			uc.setRequestProperty("User-Agent", "CupCarbon");
			InputStream in = uc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s = br.readLine();
			String s1 = (s.split(","))[9].split(":")[1].split("\"")[1];
			String s2 = (s.split(","))[10].split(":")[1].split("\"")[1];			
			t[0] = Double.parseDouble(s1);
			t[1] = Double.parseDouble(s2);
		} catch (Exception e) {
			t[0]=0;
			t[1]=0;
		}
		return t;
	}
	
}
