/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2022 CupCarbon
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
 * CupCarbon U-One was part of the research project PERSEPTEUR supported by the 
 * French Agence Nationale de la Recherche ANR 
 * under the reference ANR-14-CE24-0017.
 * ----------------------------------------------------------------------------------------------------------------
 * References:
 * Lab-STICC: Ahcene Bounceur
 * MMU: Mohammad Hammoudeh
 * ----------------------------------------------------------------------------------------------------------------
 */

package cupcarbon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;

import action.CupActionStack;
import device.IoTMqttModule;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import solver.SolverProxyParams;

/**
 * @author Ahcene Bounceur
 */

public class CupCarbon extends Application {

	public static String cupcarbonSession = "";
	
	public static Stage stage;
	public static CupCarbonController cupCarbonController;
	public static boolean macos = false;	
	
	@Override
	public void start(Stage mainStage) throws Exception {
		CupCarbon.stage = mainStage;
		String os = System.getProperty ("os.name", "UNKNOWN");
		
		if(os != null && os.startsWith("Mac")) {
			macos = true;
			URL iconURL = CupCarbon.class.getResource("cupcarbon_logo.png");
			java.awt.Image image = new ImageIcon(iconURL).getImage();
			com.apple.eawt.Application.getApplication().setDockIconImage(image);
		}
		
		CupActionStack.init();
		
		setUserAgentStylesheet(STYLESHEET_MODENA);
		//setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
	    
		mainStage.setTitle("CupCarbon "+CupCarbonVersion.VERSION);
		mainStage.getIcons().add(new Image(getClass().getResourceAsStream("cupcarbon_logo_small.png")));
        
		//stage.setMaximized(true);
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(getClass().getResource("cupcarbon.fxml"));
		Parent panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		mainStage.setScene(scene);
		mainStage.show();
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
				File curDir = new File(".");
				for(File dir : curDir.listFiles()) {
					if(dir.getName().startsWith("cupcarbon_paho")) {
						for(File file : dir.listFiles()) {
							file.delete();
						}
						dir.delete();
					}
				}
		    }
		}));
			
	}
	
	public static String generateCode(int size) {
		String code = "";
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			code += chars.charAt(random.nextInt(62));
		}
		return code;
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to CupCarbon Version "+CupCarbonVersion.VERSION);
		System.out.println("Session Generation ...");
		cupcarbonSession = "cupcarbon_"+generateCode(30);
		if(args.length>0) {
			SolverProxyParams.proxyset = "true";
			SolverProxyParams.host = args[0];
			SolverProxyParams.port = args[1];
			setProxy();
		}
		
		File file_code = new File("mqtt_code.par");
		if(file_code.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_code)));
				IoTMqttModule.com_real_node_topic = br.readLine();
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				FileOutputStream fos = new FileOutputStream("mqtt_code.par");
				String topic_code = "cupcarbon_sim/"+generateCode(30);
				IoTMqttModule.com_real_node_topic = topic_code;
				PrintStream ps = new PrintStream(fos);
				ps.println(topic_code);
				ps.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		//CupCarbonServer server = new CupCarbonServer();
		//server.start();		
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					MapLayer.repaint();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();*/
		
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
	        
	      //Original code
	        
//	        int x1 = is.read();
//	        int x2 = is.read();
//	        int x3 = is.read();
//	        int x4 = is.read();
//	        int x5 = is.read();
	        
	        //Refactoring technique Set 1 -- Rename variable
			
		    //Renaming a variable with specifying the variable name as co-ordinates of the above png image
		    //to make it more meaningful 

	        int cordinate1 = is.read();
	        int cordinate2 = is.read();
	        int cordinate3 = is.read();
	        int cordinate4 = is.read();
	        int cordinate5 = is.read();
	        
	    
	        is.close();
	        
	        //Original code
	        //if(x1==137 && x2==80 && x3==78 && x4==71 && x5==13) {
	        	
	        
	        //Using the variable names here after refactoring

	        
	        if(cordinate1==137 && cordinate2==80 && cordinate3==78 && cordinate4==71 && cordinate5==13) 
	        {
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
