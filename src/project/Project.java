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

package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import action.CupActionStack;
import buildings.BuildingList;
import cupcarbon.CupCarbon;
import cupcarbon.CupCarbonVersion;
import device.DeviceList;
import device.MultiChannels;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import map.MapLayer;
import map.NetworkParameters;
import map.WorldMap;
import markers.MarkerList;
import markers.Routes;
import simulation.SimulationInputs;

public final class Project {
	
	public static String projectPath = "";
	public static String projectName = "";

	public static void setProjectName(String path, String name) {
		projectPath = path;
		projectName = name;
	}

	public static String getProjectPathName() {
		return projectPath + File.separator + projectName;
	}

	public static String getProjectNodePath() {
		return projectPath + File.separator + "config"+ File.separator +"nodes";
	}

	public static String getProjectMarkerPath() {
		return projectPath + File.separator + "config"+ File.separator +"markers.cfg";
	}

	public static String getProjectStreetVertexPath() {
		return projectPath + File.separator + "config"+ File.separator +"graph.cfg";
	}

	public static String getProjectBuildingPathName() {
		return projectPath + File.separator + "config"+ File.separator +"buildings.cfg";
	}
	
	public static String getProjectGpsPath() {
		return projectPath + File.separator + "gps";
	}

	public static String getProjectScriptPath() {
		return projectPath + File.separator + "scripts";
	}
	
	public static String getProjectNatEventPath() {
		return projectPath + File.separator + "natevents";
	}
	
	public static String getProjectNetworkPath() {
		return projectPath + File.separator + "network";
	}


	public static String getProjectLogPath() {
		return projectPath + File.separator + "logs";
	}

	public static String getProjectResultPath() {
		return projectPath + File.separator + "results";
	}

	public static String getProjectRadioPath(){
		return projectPath + File.separator + "config"+ File.separator +"sensor_radios";
	}
	
	public static void saveProject() {
		cleanProjectDirectories();
		saveParameters();
		if(DeviceList.getSize()>0) {
			DeviceList.saveDevicesAndSensors(getProjectNodePath());
		}
		if(MarkerList.size()>0) MarkerList.save(getProjectMarkerPath());
		if(BuildingList.size()>0) BuildingList.save(getProjectBuildingPathName());
		saveSimulationParams();		
	}
	
	public static void saveSimulationParams() {
		try {
			PrintStream ps = new PrintStream(Project.projectPath + File.separator + "config" + File.separator + "simulationParams.cfg");
			ps.println("simulationtime:" + SimulationInputs.simulationTime);
			ps.println("mobility:" + SimulationInputs.mobilityAndEvents);
			ps.println("simulationspeed:" + SimulationInputs.visualDelay);
			ps.println("arrowspeed:" + SimulationInputs.arrowsDelay);
			ps.println("log:" + SimulationInputs.displayLog);
			ps.println("results:" + SimulationInputs.displayResults);
			ps.println("acktype:" + SimulationInputs.ackType);
			ps.println("ackproba:" + SimulationInputs.ackProba);
			ps.println("acklinks:" + SimulationInputs.showAckLinks);
			ps.println("ack:" + SimulationInputs.ack);
			ps.println("symmetricalinks:" + SimulationInputs.symmetricalLinks);
			ps.println("clockdrift:" + SimulationInputs.clockDrift);
			ps.println("visibility:" + SimulationInputs.visibility);
			ps.println("results_writing_period:" + SimulationInputs.resultsWritingPeriod);
			ps.println("mac_layer:" + SimulationInputs.macLayer);
			ps.println("macproba:" + SimulationInputs.macProba);
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void reset() {
		projectPath = "";
		projectName = "";
		DeviceList.number=1;
		DeviceList.initAll();
		DeviceList.reset();
		MarkerList.reset();
		BuildingList.init();
		MultiChannels.init();
		MapLayer.repaint();
		CupCarbon.cupCarbonController.saveButton.setDisable(false);
		Routes.reset();
	}
	
	public static void openProject(String path, String name) {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				File file = new File(path+File.separator+name);
				if(file.exists()) {
					CupCarbon.cupCarbonController.displayPermanentMessage_th("Loading ...");
					DeviceList.propagationsCalculated = false;
					System.out.println(path);
					System.out.println(name);
					CupActionStack.init();
					reset();
					setProjectName(path, name);
					saveRecentPath();				
					BuildingList.open(getProjectBuildingPathName());
					MarkerList.open(getProjectMarkerPath());		
					DeviceList.open();
					loadParameters();		
					CupCarbon.cupCarbonController.loadSimulationParams();
					CupCarbon.cupCarbonController.applyParameters();
					CupCarbon.cupCarbonController.saveButton.setDisable(false);
					if(DeviceList.propagationsCalculated) DeviceList.calculatePropagations();
					
					if(NetworkParameters.displayAllRoutes) {
						MarkerList.reset();
						Routes.loadRoutes();
					}
					else 
						Routes.hideAll();
					
					CupCarbon.cupCarbonController.displayShortGoodMessage_th("Project loaded");
				}
				else {
					CupCarbon.cupCarbonController.displayLongErrMessageTh("Project does not exist!");
				}
			}
		});
		th.start();
	}

	public static void newProject(String path, String name, boolean reset) {
		CupActionStack.init();
		String path1 = "";
		if(!Project.projectName.equals("")) {
			path1 = Project.projectPath;
		}
		File file = new File(path);
		if(!file.exists()) {
			if(reset) reset();
			setProjectName(path, name + ".cup");
			file.mkdir();
			file = new File(path + File.separator + "xbee");
			file.mkdir();
			file = new File(path + File.separator + "gps");
			file.mkdir();
			file = new File(path + File.separator + "natevents");
			file.mkdir();
			file = new File(path + File.separator + "tmp");
			file.mkdir();			
			file = new File(path + File.separator + "config");
			file.mkdir();
			file = new File(path + File.separator + "scripts");
			file.mkdir();
			file = new File(path + File.separator + "network");
			file.mkdir();
			file = new File(path + File.separator + "xbee");
			file.mkdir();
			file = new File(path + File.separator + "logs");
			file.mkdir();
			file = new File(path + File.separator + "results");
			file.mkdir();
			file = new File(path + File.separator + "config" + File.separator + "nodes");
			file.mkdir();
			file = new File(path + File.separator + "config" + File.separator + "sensor_radios");
			file.mkdir();
			saveParameters();
			saveRecentPath();
			if(!reset && !Project.projectName.equals("")) {
				String path2 = Project.projectPath;
				copyResProjectFiles(path1, path2);
			}
			CupCarbon.cupCarbonController.saveButton.setDisable(false);
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning!");
			alert.setHeaderText("The Project exists!");
			alert.setContentText("New Project");
			alert.showAndWait();
		}
	}
	
	public static void copyFromTo(String path1, String path2, String directory) {
		try {
			File fileS = new File(path1+File.separator+directory);
			String [] s = fileS.list();
			if(s!=null) {
				for(int i=0; i<s.length; i++) {
					FileInputStream copy = new FileInputStream(path1+File.separator+directory+File.separator+s[i]);
					FileOutputStream past = new FileOutputStream(path2+File.separator+directory+File.separator+s[i]);
					int x ;
					while((x=copy.read())!=-1) {
						past.write(x);
					}
					copy.close();
					past.close();
				}
			}
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	public static void copyResProjectFiles(String path1, String path2) {
		copyFromTo(path1, path2, "scripts");
		copyFromTo(path1, path2, "gps");
		copyFromTo(path1, path2, "natevents");
		copyFromTo(path1, path2, "xbee");
		try {
			FileInputStream copy = new FileInputStream(path1+File.separator+"config"+File.separator+"simulationParams.cfg");
			FileOutputStream past = new FileOutputStream(path2+File.separator+"config"+File.separator+"simulationParams.cfg");
			int x ;
			while((x=copy.read())!=-1) {
				past.write(x);
			}
			copy.close();
			past.close();
		}
		catch(FileNotFoundException e) {
			System.err.println("[CupCarbon] No Simulation Parameter file");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addExamples(String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path + File.separator + "transmitter.csc");
			PrintStream ps = new PrintStream(fos);
			ps.println("//Transmitter 2\natget id id\nloop\ndata p $id A\nsend $p\ndelay 1000\ndata p $id B\nsend $p\ndelay 1000");
			ps.close();
			fos.close();
			
			fos = new FileOutputStream(path + File.separator + "router.csc");
			ps = new PrintStream(fos);
			ps.println("//Router 2\natget id id\nloop\nwait\nread rp\nrdata $rp rid v\ndata p $id $v\nsend $p * $rid");
			ps.close();
			fos.close();
			
			fos = new FileOutputStream(path + File.separator + "receiver.csc");
			ps = new PrintStream(fos);
			ps.println("//Receiver 2\nloop\nwait\nread rp\nrdata $rp rid v\nif($v==A)\n  mark 1\nelse\n  mark 0\nend");
			ps.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static LinkedList<String> recentProjectList ;
	public static void saveRecentPath() {
		try {
			recentProjectList = new LinkedList<String>();
			String current = projectPath+"#"+projectName;
			recentProjectList.add(current);			
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+File.separator+"utils"+File.separator+"recent.rec");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String s = "" ;
			int n = 0;
			while((s = br.readLine()) != null)  {
				n++;
				if(!s.equals(current))
					recentProjectList.add(s);
			}
			br.close();
			if(n>10) {
				recentProjectList.removeLast();
			}

			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+File.separator+"utils"+File.separator+"recent.rec");
			PrintStream ps = new PrintStream(fos);	
			for(int i=0; i<recentProjectList.size(); i++) {
				ps.println(recentProjectList.get(i));
			}
			fos.close();
			ps.close();
			CupCarbon.cupCarbonController.initRecentProjectMenu();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadParameters() {		
		try {
			BufferedReader br = new BufferedReader(new FileReader(getProjectPathName()));
			System.out.println(br.readLine());
			System.out.println(br.readLine());
			System.out.println(br.readLine());
			int zoom = Integer.valueOf(br.readLine().split(":")[1]);
			MapLayer.mapViewer.setZoom(zoom);
			double la = Double.parseDouble(br.readLine().split(":")[1]);
			double lo = Double.parseDouble(br.readLine().split(":")[1]);
			int mapIndex = Integer.parseInt(br.readLine().split(":")[1]);
						
			MapLayer.mapViewer.setCenterPosition(new GeoPosition(la, lo));
			
			String [] keyVal ;
			String s ;
			while((s=br.readLine()) != null) {
				keyVal = s.split(":");
				switch(keyVal[0]) {
				case "display_details": NetworkParameters.displayDetails = Boolean.parseBoolean(keyVal[1]); break;
				case "draw_radio_links": NetworkParameters.drawRadioLinks = Boolean.parseBoolean(keyVal[1]); break;
				case "draw_arrows": NetworkParameters.drawSensorArrows = Boolean.parseBoolean(keyVal[1]); break;
				case "draw_sensor_arrows": NetworkParameters.drawSensorArrows = Boolean.parseBoolean(keyVal[1]); break;
				case "radio_links_color": NetworkParameters.radioLinksColor = Integer.parseInt(keyVal[1]); break;
				case "draw_marker_arrows": NetworkParameters.drawMarkerArrows = Boolean.parseBoolean(keyVal[1]); break;
				case "display_rl_distance": NetworkParameters.displayRLDistance = Boolean.parseBoolean(keyVal[1]); break;
				case "propagation": DeviceList.propagationsCalculated = Boolean.parseBoolean(keyVal[1]); break;
				case "display_marker_distance": NetworkParameters.displayMarkerDistance = Boolean.parseBoolean(keyVal[1]); break;
				case "display_radio_messages": NetworkParameters.displayRadioMessages = Boolean.parseBoolean(keyVal[1]); break;
				case "draw_script_file_name":  NetworkParameters.drawScriptFileName = Boolean.parseBoolean(keyVal[1]); break;
				case "display_print_messages":  NetworkParameters.displayPrintMessage = Boolean.parseBoolean(keyVal[1]); break;
				case "display_all_routes":  NetworkParameters.displayAllRoutes = Boolean.parseBoolean(keyVal[1]); break;
				}
			}
			br.close();
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(CupCarbon.internetIsAvailable())
						WorldMap.changeMap(mapIndex);
					else
						WorldMap.changeMap(2);
				}
			});
			
			CupCarbon.cupCarbonController.applyParameters();
			
		} catch (FileNotFoundException e) {
			System.err.println("[CupCarbon ERROR] -> No recent project !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("[CupCarbon ERROR] -> loadParameters() Project -> Param manquant");
		}
	}

	public static void saveParameters() {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(getProjectPathName()));
			fos.println("CupCarbon v. "+CupCarbonVersion.VERSION);
			fos.println("----------------");
			fos.println("name:" + projectName.substring(0, projectName.length() - 4));
			fos.println("zoom:" + MapLayer.mapViewer.getZoom());
			fos.println("centerposition_la:" + MapLayer.mapViewer.getCenterPosition().getLatitude());
			fos.println("centerposition_lo:" + MapLayer.mapViewer.getCenterPosition().getLongitude());
			fos.println("map:" + WorldMap.mapIdx);
			fos.println("display_details:" + NetworkParameters.displayDetails);
			fos.println("draw_radio_links:" + NetworkParameters.drawRadioLinks);
			fos.println("draw_sensor_arrows:" + NetworkParameters.drawSensorArrows);
			fos.println("radio_links_color:" + NetworkParameters.radioLinksColor);
			fos.println("draw_marker_arrows:" + NetworkParameters.drawMarkerArrows);
			fos.println("display_rl_distance:" + NetworkParameters.displayRLDistance);
			fos.println("propagation:" + DeviceList.propagationsCalculated);
			fos.println("display_marker_distance:" + NetworkParameters.displayMarkerDistance);
			fos.println("display_radio_messages:" + NetworkParameters.displayRadioMessages);
			fos.println("draw_script_file_name:" + NetworkParameters.drawScriptFileName);
			fos.println("display_print_messages:" + NetworkParameters.displayPrintMessage);
			fos.println("display_all_routes:" + NetworkParameters.displayAllRoutes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getNatEventFileFromName(String name) {
		if (name.endsWith(".evt"))
			return getProjectNatEventPath() + File.separator + name;
		else
			return getProjectNatEventPath() + File.separator + name + ".evt";
	}

	public static String getNatEventFileExtension(String name) {
		if (name.endsWith(".evt"))
			return name;
		else
			return name + ".evt";
	}
	
	public static String getGpsFileFromName(String name) {
		if (name.endsWith(".gps"))
			return getProjectGpsPath() + File.separator + name;
		else
			return getProjectGpsPath() + File.separator + name + ".gps";
	}

	public static String getGpsFileExtension(String name) {
		if (name.endsWith(".gps"))
			return name;
		else
			return name + ".gps";
	}

	public static String getScriptFileFromName(String name) {
		if (name.endsWith(".csc"))
			return getProjectScriptPath() + File.separator + name;
		else
			return getProjectScriptPath() + File.separator + name + ".csc";
	}

	public static String getScriptFileExtension(String name) {
		if (name.endsWith(".csc"))
			return name;
		else
			return name + ".csc";
	}

	public static String getLogFileFromName(String name) {
		if (name.endsWith(".log"))
			return getProjectLogPath() + File.separator + name;
		else
			return getProjectLogPath() + File.separator + name + ".log";
	}

	public static String getLogFileExtension(String name) {
		if (name.endsWith(".log"))
			return name;
		else
			return name + ".log";
	}

	public static String getResultFileFromName(String name) {
		if (name.endsWith(".res"))
			return getProjectResultPath() + File.separator + name;
		else
			return getProjectResultPath() + File.separator + name + ".res";
	}

	public static String getResultFileExtension(String name) {
		if (name.endsWith(".res"))
			return name;
		else
			return name + ".res";
	}	
	
	public static void cleanProjectDirectories() {
		if(MarkerList.markers != null) 
			if(MarkerList.markers.size()==0) {
				File f = new File(Project.projectPath+File.separator+"config"+File.separator+"markers.cfg");
				f.delete();
			}
		
		if(BuildingList.buildings != null) 
			if(BuildingList.buildings.size()==0) {
				File f = new File(Project.projectPath+File.separator+"config"+File.separator+"buildings.cfg");
				f.delete();
			}
		
		deleteFiles(Project.projectPath+File.separator+"config"+File.separator+"nodes");
		deleteFiles(Project.projectPath+File.separator+"config"+File.separator+"sensor_radios");
	}
		
	public static void deleteFiles(String path) {
		File file = new File(path);
		String [] list = file.list();
		for(int i=0; i<list.length; i++) {
			String s = path+File.separator+list[i];
			File f = new File(s);
			if(f.isFile()) {	
				f.delete();
			}
		}
	}
	
	public static void listParameters() {		
		try {
			BufferedReader br = new BufferedReader(new FileReader(getProjectPathName()));
			System.out.println(br.readLine());
			System.out.println(br.readLine());
			System.out.println(br.readLine());
									
			String [] keyval ;
			String s ;
			while((s=br.readLine()) != null) {
				keyval = s.split(":");
				switch(keyval[0]) {
				case "name": System.out.println("The Name of the current project is: " + keyval[1]); break;
				case "zoom": System.out.println("The Map Zoom in the current project is: " + keyval[1]); break;
				case "centerposition_la": System.out.println("The Map Center Latitude poisition in the current project is: " + keyval[1]); break;
				case "centerposition_lo": System.out.println("The Map Center Longitude poisition in the current project is: " + keyval[1]); break;
				case "map": System.out.println("The Displayed Map for the current project is the map: " + keyval[1]); break;
				case "display_details": System.out.println("The dsiplay_details is: " + keyval[1]); break;
				case "draw_radio_links": System.out.println("The draw_radio_links is: " + keyval[1]); break;
				case "draw_arrows": System.out.println("The draw_arrows is: " + keyval[1]); break;
				case "draw_sensor_arrows": System.out.println("The draw_sensor_arrows is: " + keyval[1]); break;
				case "radio_links_color": System.out.println("The radio_links_color is: " + keyval[1]); break;
				case "draw_marker_arrows": System.out.println("The draw_marker_arrows is: " + keyval[1]); break;
				case "display_rl_distance": System.out.println("The display_rl_distance is: " + keyval[1]); break;
				case "propagation": System.out.println("The propagation is: " + keyval[1]); break;
				}
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("[CupCarbon ERROR] -> No recent project !");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("[CupCarbon ERROR] -> lIstParameters() Project -> Parameters are Missing");
		}
	}
}
