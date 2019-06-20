/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: A Smart City & IoT Wireless Sensor Network Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2016 CupCarbon
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

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 */

package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import cupcarbon.CupCarbon;
import device.MessageEventList;
import device.Device;
import device.DeviceList;
import device.MultiChannels;
import device.SensorNode;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import map.MapLayer;
import markers.Routes;
import project.Project;

public class WisenSimulation implements Runnable {
	
	public static double time = 0.0;
	public static double sTime = 0.0;
	public static boolean isSimulating = false;
			
	public static double resultsWritingTime = 0.0;  // The time in seconds, of writing the battery level in the results csv file 
	
	private boolean mobilityAndEvents = false;
	
	// Change to true if you want do display some simulation info on the console 
	public static boolean showInConsole = true;

	private boolean generateResults = true ;
	
	public static SimLog simLog; 
	
	public WisenSimulation() {
		
	}

	// ------------------------------------------------------------
	// Run simulation 
	// ------------------------------------------------------------
	public void simulate() {
		if(ready()) {
			start_simulation();
		}
		else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("Not Ready to simulate!");
					alert.showAndWait();
				}
			});
		}
	}

	public void start_simulation() {
		DeviceList.initAll();
		Routes.loadRoutes();
		resultsWritingTime = 0.0;
		
		simLog = new SimLog();
		
		mobilityAndEvents = SimulationInputs.mobilityAndEvents;
		System.out.println("Mobility: "+mobilityAndEvents);		
		generateResults = SimulationInputs.displayResults ;
		System.out.println("Generate Restul File: "+generateResults);
		showInConsole = SimulationInputs.showInConsole ;

		System.out.println("Initialization ... ");
		
		simLog.add("===========================");
		simLog.add("Initialization");
		
		MultiChannels.init();
		MessageEventList.numberOfSentMessages = 0;
		MessageEventList.numberOfReceivedMessages = 0;
		MessageEventList.numberOfAckMessages = 0;		
		MessageEventList.numberOfLostMessages = 0;
		MessageEventList.numberOfSentMessages_b = 0;
		MessageEventList.numberOfReceivedMessages_b = 0;
		MessageEventList.numberOfAckMessages_b = 0;		
		MessageEventList.numberOfLostMessages_b = 0;
		
		for (Device device : DeviceList.devices) {
			device.initForSimulation();
			if (mobilityAndEvents) {				
				if (!device.getGPSFileName().equals(""))
					device.setEvent2(device.getNextTime());
				else
					device.setEvent2(Double.MAX_VALUE);

				if (!device.getNatEventFileName().equals(""))
					device.setEvent3(device.getNextValueTime());
				else
					device.setEvent3(Double.MAX_VALUE);
			}
		}
		for (SensorNode sensor : DeviceList.sensors) {
			sensor.initForSimulation();
			if(SimulationInputs.clockDrift) sensor.drift();
			if (mobilityAndEvents) {				
				if (!sensor.getGPSFileName().equals(""))
					sensor.setEvent2(sensor.getNextTime());
				else
					sensor.setEvent2(Double.MAX_VALUE);
			}
		}
		System.out.println("End of Initialization.");		
		long startTime = System.currentTimeMillis();
		System.out.println("Start Simulation ... ");
		MapLayer.repaint();
		try {
			PrintStream ps = new PrintStream(new FileOutputStream(Project.getProjectResultPath() + File.separator + "wisen_simulation" + ".csv"));
			if (generateResults) {
				ps.print("Time (Sec);");
				for (SensorNode sensor : DeviceList.sensors) {											
					ps.print("S"+sensor.getId() + ";");
				}
				ps.println();
			}
			
			boolean moving = false ;
			String fMessage = "";
			
			simLog.add("======================================================");
			simLog.add("START SIMULATION");
			simLog.add("======================================================");

			// ------------------------------------------------------
			// ------------------------------------------------------
			// ------------------------------------------------------
			//
			// From here the simulation will start
			//
			// ------------------------------------------------------
			// ------------------------------------------------------
			// ------------------------------------------------------			
			stopCondition = false;
			double min = 0;
			boolean allDeadSensors = false;	
						
			double timeEvt = 1.0;
			time = 0.0;
			double previousTime = time;
			
			Platform.runLater(() ->
					CupCarbon.cupCarbonController.simulationTimeLabel.setText("RT")
			);
			//--------> Loop - Simulation starts here
			while (time <= SimulationInputs.simulationTime) {
				
				isSimulating = true;
				
				if (min == Double.MAX_VALUE) {
					System.out.println("Infinite WAITs!");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							updateButtons();
							isSimulating = false;
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Simulation");
							alert.setHeaderText(null);
							alert.setContentText("Infinite WAITs! [time = "+String.format("%4.4f", sTime)+"]");
							alert.showAndWait();
						}
					});
					break;
				}
				
				if(stopCondition) {
					System.out.println("Simulation stopped!");
					break;
				}
				
				if(allDeadSensors) {
					System.out.println("Dead Sensors!");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							updateButtons();
							isSimulating = false;
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Simulation");
							alert.setHeaderText(null);
							alert.setContentText("Dead Sensors! [time = "+String.format("%4.4f", sTime)+"]");
							alert.showAndWait();
						}
					});
					break;
				}
				
				consolPrint(time + " : ");	
				
				simLog.add("");
				simLog.add("----------------------------------------------------------------------------");
				simLog.add("Time : "+time);
				simLog.add("Min (milliseconds) : "+min);
				
				consolPrintln("--------------------------------------");
				consolPrint(""+time);
				
				if(!fMessage.replace("\n", "").equals("")) 
					simLog.add(fMessage);
				
				fMessage = "";
				
				consolPrintln(" + "+min+" = "+time);
				
				MultiChannels.goToTheNextTime(min);
				MultiChannels.receivedMessages();
								
				min = Double.MAX_VALUE;
				for (SensorNode sensor : DeviceList.sensors) {
					if(!sensor.isDead()) {
						consolPrint(sensor + " [" +sensor.getScript().getCurrent().toString()+"] - ");
						sensor.execute();
						if ((min > sensor.getEvent()))
							min = sensor.getEvent();	
						if ((min > sensor.getLocEventTime()))
							min = sensor.getLocEventTime();	
					}
				}

				consolPrintln("");
				
				double minmv = Double.MAX_VALUE;
				if (mobilityAndEvents) {
					for (SensorNode sensor : DeviceList.sensors) {
						if(!sensor.isDead()) {
							if (sensor.getEvent2() == 0) {
								simLog.add(sensor.getIdFL()+sensor.getId()+" SENSOR MOVING");
								if (!sensor.getGPSFileName().equals("")) {
									sensor.moveToNext(true, 0);
									sensor.setEvent2(sensor.getNextTime());									
								}
							}							
							if ((minmv > sensor.getEvent2()))
								minmv = sensor.getEvent2();	
						}
					}
					for (Device device : DeviceList.devices) {
						if(!device.isDead()) {
							if (device.getEvent2() == 0) {
								simLog.add(device.getIdFL()+device.getId()+" DEVICE MOVING");
								if (!device.getGPSFileName().equals("")) {
									device.moveToNext(true, 0);
									device.setEvent2(device.getNextTime());									
								}
							}
							
							if (device.getEvent3() == 0) {
								simLog.add(device.getIdFL()+device.getId()+" VALUE GENERATION");
								if (!device.getNatEventFileName().equals("")) {
									device.generateNextValue();
									device.setEvent3(device.getNextValueTime());									
								}
							}
							
							if ((minmv > device.getEvent2()))
								minmv = device.getEvent2();	
							if ((minmv > device.getEvent3()))
								minmv = device.getEvent3();	
						}
					}
				}
				
				consolPrintln("");
				boolean waitArrow = false;
				if (min > MultiChannels.getMin()) {
					min = MultiChannels.getMin();
					waitArrow = true;
				}
				
				if (mobilityAndEvents) {
					moving = false;
					if (minmv < min) {
						min = minmv;
						moving = true;
					}
				}				
				// min ready 
				
				consolPrintln("");
				if((min > 0) || (moving)) {
					
					if (generateResults && resultsWritingTime<=time) {
						ps.print(time + ";");
						for (SensorNode sensor : DeviceList.sensors) {
							ps.print(sensor.getBatteryLevel() + ";");
							consolPrint(sensor.getBatteryLevel()+" | ");
						}
						ps.println();
						if(resultsWritingTime<=time) {
							resultsWritingTime += SimulationInputs.resultsWritingPeriod;
						}
					}
										
					consolPrintln("");
					
				}			
								
				if(SimulationInputs.clockDrift) {
					if ((timeEvt <= time)) {
						timeEvt += 3600; // Drift each 1 hour
						for (SensorNode sensor : DeviceList.sensors) {					
							if(!sensor.isDead()) {
								for(int i=0; i<min; i++) {
									sensor.drift();
								}
							}
						}
					}
				}
				
				allDeadSensors = true; 
				for (SensorNode sensor : DeviceList.sensors) {
					if(!sensor.isDead()) {
						consolPrint(sensor.getEvent()+" : ");
						
						sensor.gotoTheNextEvent(min);
						sensor.goToNextLocEvent(min);
						sensor.executeLocEvent();
						
						if(sensor.getEvent() == 0) {
							fMessage += sensor.getScript().getCurrent().finishMessage() + "\n";
							sensor.gotoTheNextInstruction() ;								
						}
						
						consolPrint(sensor.getEvent()+" | ");
						if (!sensor.isDead())
							allDeadSensors = false;
					}
				}
				
				for (SensorNode sensor : DeviceList.sensors) {
					if (mobilityAndEvents) {						
						sensor.setEvent2(sensor.getEvent2()-min);						
					}
				}
				for (Device device : DeviceList.devices) {
					if (mobilityAndEvents) {						
						device.setEvent2(device.getEvent2()-min);
						device.setEvent3(device.getEvent3()-min);
					}
				}
				
				time += min;
				if(time<SimulationInputs.simulationTime)
					sTime = time;
				consolPrintln("");
				consolPrintln("------------------------------------------");				
			 	
				try {
					CupCarbon.cupCarbonController.progress.setProgress(time *1.0/ SimulationInputs.simulationTime);
					
					//CupCarbon.cupCarbonController.stlabel.textProperty().bind(new SimpleDoubleProperty(time *1.0/ SimulationInputs.simulationTime).asString());
					//CupCarbon.cupCarbonController.stlabel.setText(""+(time *1.0/ SimulationInputs.simulationTime));
				} catch(Exception e) {
					isSimulating = false;
					System.err.println("[CUPCARBO:Simulation] Simulation Progress: "+(time *1.0/ SimulationInputs.simulationTime));
				}
				
				MapLayer.repaint();
				if (waitArrow && SimulationInputs.arrowsDelay > 0) {
					Thread.sleep((int)(SimulationInputs.arrowsDelay));
				}
				else {
					int d = (int)(SimulationInputs.visualDelay*(time - previousTime));					
					if(time < SimulationInputs.simulationTime) {
						Thread.sleep(d);
					}
					else {
						isSimulating = false;
						System.out.println("Infinite Times!");
					}
				}
				previousTime = time;
				ps.flush();
			}
			try {
				CupCarbon.cupCarbonController.progress.setProgress(0.0);
			} catch(Exception e) {
				isSimulating = false;
				System.err.println("[CUPCARBO:Simulation] Simulation Progress: 0");
			}
			simLog.close();
			ps.close();		
			MultiChannels.init();
			isSimulating = false;
			updateButtons();			
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}
		MapLayer.repaint();
		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.println("End of Simulation.");
		System.out.println(((endTime - startTime) / 1000.) + " sec");
		
		
		
		isSimulating = false;
		
		Platform.runLater(() ->
			CupCarbon.cupCarbonController.simulationTimeLabel.setText(String.format("RT = %4.4f s", ((endTime - startTime) / 1000.)))				
		);

		for (SensorNode sensorNode : DeviceList.sensors) {
			sensorNode.toOri();
			sensorNode.stopAgentSimulation();			
		}
		for (Device device : DeviceList.devices) {
			device.toOri();
			device.stopAgentSimulation();				
		}
		if(DeviceList.propagationsCalculated)
			DeviceList.calculatePropagations();		
		
		System.out.println(String.format("Time: %4.4f s", WisenSimulation.sTime));
		System.out.println("Number of SENT messages: "+MessageEventList.numberOfSentMessages + " ["+ MessageEventList.numberOfSentMessages_b +"]" );
		System.out.println("Number of RECEIVED messages: "+MessageEventList.numberOfReceivedMessages + " ["+ MessageEventList.numberOfReceivedMessages_b +"]");
		System.out.println("Number of SENT & RECEIVED messages: "+(MessageEventList.numberOfReceivedMessages+MessageEventList.numberOfSentMessages) + " ["+ (MessageEventList.numberOfReceivedMessages_b+MessageEventList.numberOfSentMessages_b) +"]");
		System.out.println("Number of ACK messages: "+MessageEventList.numberOfAckMessages + " ["+ MessageEventList.numberOfAckMessages_b +"]");
		System.out.println("Number of LOST messages: "+MessageEventList.numberOfLostMessages + " ["+ MessageEventList.numberOfLostMessages_b +"]");
		System.out.println("Number of Marked Sensors: "+DeviceList.getNumberOfMarkedSensors());
		
		CupCarbon.cupCarbonController.displayShortGoodMessage("End of Simulation");
	}

	// ------------------------------------------------------------
	// Run simulation (call the simulate() method)
	// ------------------------------------------------------------
	@Override
	public void run() {
		simulate();
	}
	
	public static void consolPrint(String txt) {
		if(showInConsole)
			System.out.print(txt);
	}
	
	public static void consolPrintln(String txt) {
		if(showInConsole)
			System.out.println(txt);
	}

	private boolean stopCondition = false;
	
	public void stopSimulation() {
		updateButtons();
		MultiChannels.init();
		isSimulating = false;
		stopCondition = true;
		MapLayer.repaint();
	}
	
	// ------------------------------------------------------------------------
	// Check if everything it's OK for simulation
	// -> Check if each sensor has its own script 
	// ------------------------------------------------------------------------
	/**
	 * Check if each sensor has its own script 
	 */
	public static void check() {
		for(SensorNode sensor : DeviceList.sensors) {
			if(sensor.getScriptFileName().equals("")) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						updateButtons();
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText(null);
						alert.setContentText("Not Ready to simulate!");
						alert.showAndWait();
					}
				});
				return;
			}
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateButtons();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Valid");
				alert.setHeaderText(null);
				alert.setContentText("Ready to simulate!");
				alert.showAndWait();
			}
		});
	} 
	
	public boolean ready() {
		for(SensorNode sensor : DeviceList.sensors) {
			if(sensor.getScriptFileName().equals("")) {
				return false; 
			}
		}
		return true;
	}
	
	public static void updateButtons() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CupCarbon.cupCarbonController.runSimulationButton.setDisable(false);
				CupCarbon.cupCarbonController.qRunSimulationButton.setDisable(false);
				CupCarbon.cupCarbonController.qRunSimulationButton.setDefaultButton(true);
				CupCarbon.cupCarbonController.qStopSimulationButton.setDefaultButton(false);
				CupCarbon.cupCarbonController.updateLabeLInfos();
			}
		});				
	}
	
}
