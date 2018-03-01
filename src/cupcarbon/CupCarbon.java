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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import action.CupActionStack;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import solver.SolverProxyParams;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 */

public class CupCarbon extends Application {

	public static Stage stage;
	public static CupCarbonController cupCarbonController;
	public static boolean macos = false;	
	
	@Override
	public void start(Stage mainStage) throws Exception {
		CupCarbon.stage = mainStage;
		String os = System.getProperty ("os.name", "UNKNOWN");
		if(os != null && os.startsWith("Mac")) {
			macos = true;
		}
		
		CupActionStack.init();
		setProxy();
		
		setUserAgentStylesheet(STYLESHEET_MODENA);		
	    
		mainStage.setTitle("CupCarbon "+CupCarbonVersion.VERSION);
		mainStage.getIcons().add(new Image(getClass().getResourceAsStream("cupcarbon_logo_small.png")));		
		//stage.setMaximized(true);
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("cupcarbon.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public static void main(String[] args) {		
		String r = "03111441122123263122253111131132311317312221232617123";	//Signature
		System.out.println(r);
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
	        URL url = new URL("http://a.basemaps.cartocdn.com/light_all/0/0/0.png");
	        InputStream is = url.openStream();
	        System.out.println("Internet: OK");
	        int x1 = is.read();
	        int x2 = is.read();
	        int x3 = is.read();
	        int x4 = is.read();
	        int x5 = is.read();
	        is.close();
	        if(x1==137 && x2==80 && x3==78 && x4==71 && x5==13) {
	        	URL url2 = new URL("http://www.cupcarbon.com/download/cupcarbon_update.txt");
	        	InputStream is2 = url2.openStream();
	        	BufferedReader br = new BufferedReader(new InputStreamReader(is2));
	        	int u = Integer.parseInt(br.readLine());
	        	if(u>CupCarbonVersion.UPDATE) {
	        		System.out.println("NEW VERSION AVAILABE");
	        		Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("New Version");
							alert.setHeaderText("New Version is available.");
							alert.setContentText("Visit www.cupcarbon.com");
							alert.showAndWait();
						}
					});
	        	} 
	        	else {
	        		System.out.println("UPDATED VERSION");
	        	}
	        	return true;
	        }
	        else
	        	return false;
	    } catch (MalformedURLException e) {
	        return false;
	    } catch (IOException e) {
	        return false;
	    }
	}
	// -----

	@Override
	public void stop() {
		System.exit(0);
	}
}
