package cupcarbon;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://router.project-osrm.org/viaroute?loc=52.503033,13.420526&loc=52.516582,13.429290&instructions=false");
		InputStream bf = url.openStream();
		StringBuffer s = new StringBuffer();
		int x1 ;
		int x2 ;
		boolean st = false;
		x1 = bf.read();
		while((x2=bf.read()) != -1) {
			if((((char)x1) == '"') && (((char)x2) == 'v')) {
				st = true;
			}
			else {
				x1 = x2;
			}
			if (st)
				s.append((char) x2);
		}
		bf.close();
		System.out.println(s);
	}

}
