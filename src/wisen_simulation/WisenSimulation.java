package wisen_simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.swing.JOptionPane;

import NaturalEvents.Gas;
import map.Layer;
import project.Project;
import cupcarbon.CupCarbon;
import cupcarbon.WsnSimulationWindow;
import device.Channel;
import device.DataInfo;
import device.Device;
import device.DeviceList;
import flying_object.FlyingGroup;

public class WisenSimulation extends Thread {

	private boolean mobility = false;
	private long iterNumber = 0;

	private int visualDelay;
	
	// Change to true if you want do display some simulation info on the console 
	public static boolean showInConsole = false;

	private boolean generateResults = true ;

	public static boolean stepByStep = false;
	
	public static long ttime = 0;
	
	public WisenSimulation() {

	}

	// ------------------------------------------------------------
	// Run simulation 
	// ------------------------------------------------------------
	public void simulate() {
		SimLog.init();
		mobility = SimulationInputs.mobility;
		System.out.println("mobility "+mobility);
		iterNumber = SimulationInputs.iterNumber;		
		visualDelay = SimulationInputs.visualDelay;
		generateResults = SimulationInputs.displayResults ;
		showInConsole = SimulationInputs.showInConsole ;

		WsnSimulationWindow.setState("Simulation : initialization ...");
		System.out.println("Initialization ... ");
		List<Device> devices = DeviceList.getNodes();
		SimLog.add("===========================");
		SimLog.add("Initialization");
		
		Channel.init();
		
		for (Device device : devices) {
			if(device.getType()==Device.SENSOR || device.getType()==Device.BASE_STATION) {
				device.initForSimulation();
			}
			if (mobility) {
				device.initForSimulation();
				if (device.canMove())
					device.setEvent2(device.getNextTime());
				else
					device.setEvent2(Long.MAX_VALUE);
				if(device.getType()==Device.GAS) {
					((Gas)device).init();
				}
				if(device.getType()==Device.FLYING_OBJECT) {
					((FlyingGroup)device).start();
				}
			}
		}		
		System.out.println("End of Initialization.");		
		double time = 0;
		long startTime = System.currentTimeMillis();
		System.out.println("Start Simulation (WISEN : D-Event) ... ");
		long iter = 0;
		WsnSimulationWindow.setState("Simulation : End of initialization.");
		WsnSimulationWindow.setState("Simulate (WISEN) ...");
		Layer.getMapViewer().repaint();
		try {
			String as = "";
			if (mobility) as = "_mob";
			PrintStream ps = new PrintStream(new FileOutputStream(Project.getProjectResultsPath() + "/wisen_simulation" + as + ".csv"));
			if (generateResults) {
				ps.print("Time (Sec);");
				for (Device device : devices) {					
					if(device.getType()==Device.SENSOR || device.getType()==Device.BASE_STATION) {						
						ps.print("S"+device.getId() + ";");
					}
				}
				ps.println();
			}
			
			boolean moving = false ;
			String fMessage = "";
			
			SimLog.add("======================================================");
			SimLog.add("802.15.4 bit rate: "+DataInfo.ChDataRate+" bits/s");
			SimLog.add("UART bit rate: "+DataInfo.UartDataRate+" baud (bits/s)");
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
			ttime = 0;
			long min = 0;
			boolean allDeadSensors = false;
			for (iter = 0; iter < iterNumber; iter++) {
				if (min == Long.MAX_VALUE) {
					System.out.println(Channel.size());
					System.out.println("Infinite WAITs!");
					JOptionPane.showMessageDialog(null, "Infinite WAITs! [iter: "+iter+"]", "Simulation Stopped", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				
				if(stopCondition) {
					System.out.println("Simulation stopped!");
					JOptionPane.showMessageDialog(null, "Simulation stopped! [iter: "+iter+"]", "Simulation Stopped", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				
				if(allDeadSensors) {
					System.out.println("Dead Sensors!");
					JOptionPane.showMessageDialog(null, "Dead Sensors! [iter: "+iter+"]", "Simulation Stopped", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				
				consolPrint(iter + " : ");	
			
				SimLog.add("");
				SimLog.add("----------------------------------------------------------------------------");
				SimLog.add("Iteration : "+iter);
				SimLog.add("Min (milliseconds) : "+((min*1.0)/DataInfo.ChDataRate*1000.));
				
				consolPrintln("--------------------------------------");
				consolPrint(""+ttime);
				//consolPrint(""+time);
				
				time += (min*1.0)/DataInfo.ChDataRate;
				ttime += min;
				
				SimLog.add("Next time (milliseconds) : "+(time*1000));
				SimLog.add("--------------------------------------");
				
				if(!fMessage.replace("\n", "").equals("")) 
					SimLog.add(fMessage);
				
				fMessage = "";
				
				consolPrintln(" + "+min+" = "+ttime);
				
				Channel.goToTheNextTime(min);
				if (Channel.getMin()==0) {
					Channel.messageReceived();
				}
				
				min = Long.MAX_VALUE;
				for (Device device : devices) {
					if(!device.isDead()) {
						if(device.getType()==Device.SENSOR || device.getType()==Device.BASE_STATION) {
							consolPrint(device + " [" +device.getScript().getCurrent().toString()+"] - ");
							device.execute();
							if ((min > device.getEvent()))
								min = device.getEvent();
						}						
					}
				}
				
				consolPrintln("");
				
				long minmv = Long.MAX_VALUE;
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
				
				if (min>Channel.getMin())
					min = Channel.getMin();
				
				if (mobility) {
					moving = false;
					if (minmv < min) {
						min = minmv;
						moving = true;
					}
				}	
				
				consolPrintln("");
				
				if((min!=0) || (moving)) {
					if (generateResults) ps.print(time + ";");
					
					sleep(visualDelay);
					
					for (Device device : devices) {
						if(!device.isDead()) {
							if(device.getType()==Device.SENSOR || device.getType()==Device.BASE_STATION) {
								if (generateResults) ps.print(device.getBatteryLevel() + ";");								
								consolPrint(device.getBatteryLevel()+" | ");								
								if (device.isSending() || device.isReceiving()) {
									sleep(visualDelay);
									device.setSending(false);
									device.setReceiving(false);
									device.setDistanceMode(false);
								}
							}
							if(device.getType()==Device.GAS && mobility) ((Gas) device).simNext();
						}
					}
					consolPrintln("");
					
					if (generateResults) ps.println();										
				}
				
				Layer.getMapViewer().repaint();
				
				allDeadSensors = true; 
				for (Device device : devices) {
					if(!device.isDead()) {
						if(device.getType()==Device.SENSOR || device.getType()==Device.BASE_STATION) {
							consolPrint(device.getEvent()+" : "); 
							
							if(device.getEvent() != Long.MAX_VALUE)
								device.gotoTheNextEvent(min);								
							
							if(device.getEvent()==0) {
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
				WsnSimulationWindow.setProgress((int) (1000 * iter / iterNumber));
				CupCarbon.lblSimulation.setText(" | Simulation: "+((int) (100 * iter / iterNumber))+"%");
				Layer.getMapViewer().repaint();
			}
			SimLog.close();
			ps.close();
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}
		Layer.getMapViewer().repaint();
		long endTime = System.currentTimeMillis();
		System.out.println("End of Simulation (WISEN : D-Event).");
		System.out.println(((endTime - startTime) / 1000.) + " sec");
		WsnSimulationWindow.setState("End (WISEN Simulation) at iter " + iter
				+ ". Simulation Time : " + ((endTime - startTime) / 1000.)
				+ " sec.");
		WsnSimulationWindow.setProgress(0);
		CupCarbon.lblSimulation.setText(" | Simulation: 0%");

		if (mobility) {
			for (Device device : devices) {
				device.toOri();
				device.stopSimulation();
			}
			Layer.getMapViewer().repaint();
		}
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
