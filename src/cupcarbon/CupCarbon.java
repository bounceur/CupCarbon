/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2017 CupCarbon
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
 *----------------------------------------------------------------------------------------------------------------
 * CupCarbon U-One is part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017-01. 
 * ----------------------------------------------------------------------------------------------------------------
 **/

package cupcarbon;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import action.CupActionStack;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import solver.SolverProxyParams;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 3.1.0 (U-One)
 */

public class CupCarbon extends Application {

	public static Stage stage;
	public static CupCarbonController cupCarbonController;
	public static boolean macos = false;
	
	@Override
	public void start(Stage stage) throws IOException {
		String os = System.getProperty ("os.name", "UNKNOWN");
		if(os != null && os.startsWith("Mac")) {
			macos = true;
		}
		
		CupActionStack.init();
		
		CupCarbon.stage = stage;
		
		try {
			System.out.println("> CupCarbon U-One");
			FileInputStream licenceFile = new FileInputStream("utils/cupcarbon_licence.txt");
			int c;
			while ((c = licenceFile.read()) != -1) {
				System.out.print((char) c);
			}
			System.out.println();
			licenceFile.close();
			setProxy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setUserAgentStylesheet(STYLESHEET_MODENA);
	    
		stage.setTitle("CupCarbon "+CupCarbonVersion.VERSION);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("cupcarbon_logo_small.png")));		
		stage.setMaximized(true);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CupCarbon.class.getResource("cupcarbon.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		if(args.length>0) {
			SolverProxyParams.proxyset = args[0];
			SolverProxyParams.host = args[1];
			SolverProxyParams.port = args[2];
		}
		launch(args);
	}
	
	public static void setProxy() {
		System.getProperties().put("http.proxySet", SolverProxyParams.proxyset);
		System.getProperties().put("http.proxyHost", SolverProxyParams.host);
		System.getProperties().put("http.proxyPort", SolverProxyParams.port); 		
	}
	
	public static boolean internetIsAvailable() {
	    try {
	        final URL url = new URL("http://a.basemaps.cartocdn.com/light_all/0/0/0.png");
	        final URLConnection connection = url.openConnection();
	        InputStream is = connection.getInputStream();
	        System.out.println("Internet: OK");
	        int x1 = is.read();
	        int x2 = is.read();
	        int x3 = is.read();
	        int x4 = is.read();
	        int x5 = is.read();
	        is.close();
	        
	        if(x1==137 && x2==80 && x3==78 && x4==71 && x5==13)
	        	return true;
	        else
	        	return false;
	    } catch (MalformedURLException e) {
	        return false;
	    } catch (IOException e) {
	        return false;
	    }
	}
	// -----

}
