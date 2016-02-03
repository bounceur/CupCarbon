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

package wisen_simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.swing.JOptionPane;

import cupcarbon.CupCarbon;
import cupcarbon.WisenSimulationWindow;
import device.Channels;
import device.Device;
import device.DeviceList;
import flying_object.FlyingGroup;
import map.MapLayer;
import natural_events.Gas;
import project.Project;

public class WisenSimulation extends Thread {

	public static double time = 0.0;
	
	private boolean mobility = false;
	
	// Change to true if you want do display some simulation info on the console 
	public static boolean showInConsole = false;

	private boolean generateResults = true ;
	
	public WisenSimulation() {

	}

	// ------------------------------------------------------------
	// Run simulation 
	// ------------------------------------------------------------
	public void simulate() {
		SimLog.init();
		mobility = SimulationInputs.mobility;
		System.out.println("mobility "+mobility);		
		generateResults = SimulationInputs.displayResults ;
		showInConsole = SimulationInputs.showInConsole ;

		WisenSimulationWindow.setState("Simulation : initialization ...");
		System.out.println("Initialization ... ");
		List<Device> devices = DeviceList.getNodes();
		
		SimLog.add("===========================");
		SimLog.add("Initialization");
		
		DeviceList.initAll();
		Channels.init();
		
		for (Device device : devices) {
			device.initForSimulation();
			if (mobility) {				
				if (device.canMove())
					device.setEvent2(device.getNextTime());
				else
					device.setEvent2(Double.MAX_VALUE);
				if(device.getType()==Device.GAS) {
					((Gas)device).init();
				}
				if(device.getType()==Device.FLYING_OBJECT) {
					((FlyingGroup)device).start();
				}
			}
		}		
		System.out.println("End of Initialization.");		
		long startTime = System.currentTimeMillis();
		System.out.println("Start Simulation (WISEN : D-Event) ... ");
		WisenSimulationWindow.setState("Simulation : End of initialization.");
		WisenSimulationWindow.setState("Simulate (WISEN) ...");
		MapLayer.getMapViewer().repaint();
		try {
			String as = "";
			if (mobility) as = "_mob";
			PrintStream ps = new PrintStream(new FileOutputStream(Project.getProjectResultsPath() + "/wisen_simulation" + as + ".csv"));
			if (generateResults) {
				ps.print("Time (Sec);");
				for (Device device : devices) {					
					if(device.getType()==Device.SENSOR || device.getType()==Device.MEDIA_SENSOR || device.getType()==Device.BASE_STATION) {						
						ps.print("S"+device.getId() + ";");
					}
				}
				ps.println();
			}
			
			boolean moving = false ;
			String fMessage = "";
			
			SimLog.add("======================================================");
			SimLog.add("START SIMULATION");
			SimLog.add("======================================================");

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
			
//			if (mobility) {
//				for (Device device : devices) {
//					if(!device.isDead()) {
//						if (device.canMove()) {
//							//device.moveToNext(true, 0);
//							device.setEvent2(device.getNextTime());									
//						}
//					}
//				}
//			}			
						
			double timeEvt = 1.0;
			time = 0.0;
			double previousTime = time;
			
			while (time <= SimulationInputs.simulationTime) {
				
				if (min == Double.MAX_VALUE) {
					System.out.println("Infinite WAITs!");
					JOptionPane.showMessageDialog(null, "Infinite WAITs! [time = "+time+"]", "Simulation Stopped", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				
				if(stopCondition) {
					System.out.println("Simulation stopped!");
					JOptionPane.showMessageDialog(null, "Simulation stopped! [time = "+time+"]", "Simulation Stopped", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				
				if(allDeadSensors) {
					System.out.println("Dead Sensors!");
					JOptionPane.showMessageDialog(null, "Dead Sensors! [time = "+time+"]", "Simulation Stopped", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				
				consolPrint(time + " : ");	
			
				SimLog.add("");
				SimLog.add("----------------------------------------------------------------------------");
				SimLog.add("Time : "+time);
				SimLog.add("Min (milliseconds) : "+min);
				
				consolPrintln("--------------------------------------");
				consolPrint(""+time);

				time += min;
				
				SimLog.add("Next time (milliseconds) : "+time);
				SimLog.add("--------------------------------------");
				
				if(!fMessage.replace("\n", "").equals("")) 
					SimLog.add(fMessage);
				
				fMessage = "";
				
				consolPrintln(" + "+min+" = "+time);
				
				Channels.goToTheNextTime(min);
				Channels.receivedMessages();
				
				min = Double.MAX_VALUE;
				for (Device device : devices) {
					if(!device.isDead()) {
						if(device.getType()==Device.SENSOR || device.getType()==Device.MEDIA_SENSOR || device.getType()==Device.BASE_STATION) {
							consolPrint(device + " [" +device.getScript().getCurrent().toString()+"] - ");
							device.execute();
							if ((min > device.getEvent()))
								min = device.getEvent();
						}						
					}
				}
				
				consolPrintln("");
				
				double minmv = Double.MAX_VALUE;
				if (mobility) {
					for (Device device : devices) {
						if(!device.isDead()) {
							if (device.getEvent2() == 0) {
								SimLog.add(device.getIdFL()+device.getId()+" DEPLACEMENT");
								if (device.canMove()) {
									device.moveToNext(true, 0);
									device.setEvent2(device.getNextTime());									
								}
							}
							if ((minmv > device.getEvent2()))
								minmv = device.getEvent2();	
						}
					}
				}
				
				consolPrintln("");
				boolean waitArrow = false;
				if (min > Channels.getMin()) {
					min = Channels.getMin();
					waitArrow = true;
				}

				if (mobility) {
					moving = false;
					if (minmv < min) {
						min = minmv;
						moving = true;
					}
				}
				
//				if (wheather) {
//					
//				}
//				
				consolPrintln("");		
				
//				for (WisenEvent event : WisenEventList.eventList) {
//					
//				}

				consolPrintln("");
				if((min > 0) || (moving)) {
					
					if (generateResults) ps.print(time + ";");
					
					for (Device device : devices) {
						if (generateResults) 
							ps.print(device.getBatteryLevel() + ";");								
						consolPrint(device.getBatteryLevel()+" | ");
						if(device.getType()==Device.GAS && mobility) ((Gas) device).simNext();
					}
					consolPrintln("");
					
					if (generateResults) ps.println();										
				}
				
				
				if(SimulationInputs.cpuDrift) {
					if (timeEvt <= time) {
						timeEvt += 1.0;
						for (Device device : devices) {					
							if(!device.isDead()) {
								if(device.getType()==Device.SENSOR || device.getType()==Device.MEDIA_SENSOR || device.getType()==Device.BASE_STATION) {
									device.drift();
								}
							}
						}
					}
				}
				
				allDeadSensors = true; 
				for (Device device : devices) {
					if(!device.isDead()) {
						if(device.getType()==Device.SENSOR || device.getType()==Device.MEDIA_SENSOR || device.getType()==Device.BASE_STATION) {
							consolPrint(device.getEvent()+" : ");
							
							device.gotoTheNextEvent(min);								
							
							if(device.getEvent() == 0) {
								fMessage += device.getScript().getCurrent().finishMessage() + "\n";
								device.gotoTheNextInstruction() ;								
							}
							
							consolPrint(device.getEvent()+" | ");
							if (!device.isDead())
								allDeadSensors = false;
						}
					}					
					if (mobility) {						
						device.setEvent2(device.getEvent2()-min);
					}
				}
				
				consolPrintln("");
				consolPrintln("------------------------------------------");				
				WisenSimulationWindow.setProgress((int) (1000 * time / SimulationInputs.simulationTime));
				CupCarbon.lblSimulation.setText(" | Simulation: "+ Math.round(time) + "s [" + ((int) (100 * time / SimulationInputs.simulationTime))+"%]");
								
				MapLayer.getMapViewer().repaint();
				if (waitArrow && SimulationInputs.arrowsDelay > 0) {
					sleep((int)(SimulationInputs.arrowsDelay));
				}
				else {
					sleep((int)(SimulationInputs.visualDelay*(time - previousTime)));
				}
				previousTime = time;
			}
			SimLog.close();
			ps.close();
			if(time==SimulationInputs.simulationTime)
				JOptionPane.showMessageDialog(null, "Simulation Finished!", "Simulation", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}
		MapLayer.getMapViewer().repaint();
		long endTime = System.currentTimeMillis();
		System.out.println("End of Simulation (WISEN : D-Event).");
		System.out.println(((endTime - startTime) / 1000.) + " sec");
		WisenSimulationWindow.setState("End (WISEN Simulation) at time " + time + ". CPU Time : " + ((endTime - startTime) / 1000.) + " sec.");
		WisenSimulationWindow.setProgress(0);
		CupCarbon.lblSimulation.setText(" | Simulation: 0%");

		//if (mobility) {
			for (Device device : devices) {
				device.toOri();
				//device.init();
				device.stopSimulation();
			}
			MapLayer.getMapViewer().repaint();
		//}
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
		stopCondition = true;		
	}
	
	// ------------------------------------------------------------------------
	// Check if everything it's OK for simulation
	// -> Check if each sensor has its own script 
	// ------------------------------------------------------------------------
	/**
	 * Check if each sensor has its own script 
	 */
	public static void check() {
		for(Device d : DeviceList.getSensorNodes()) {
			if(d.getScriptFileName().equals("")) {
				JOptionPane.showMessageDialog(null, "Not Ready to simulate!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Ready to simulate!", "Valid", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean ready() {
		for(Device d : DeviceList.getSensorNodes()) {
			if(d.getScriptFileName().equals("")) {
				return false; 
			}
		}
		return true;
	}
	
}
