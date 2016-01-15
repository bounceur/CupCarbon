package com.virtualys.cupcarbon.communication;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import device.Device;
import device.DeviceList;
import map.MapLayer;
import visualisation.Visualisation;

public class Communication implements ICommunicationListener {
  
	/**
	 * 
	 */
	protected ArrayList<ICommunicationChannel> coChannels = new ArrayList<ICommunicationChannel>();
	/**
	 * 
	 */
	public static boolean cbInitialize = false;
	/**
	 * 
	 */
	public boolean cbBoot = false;
	public String cSFileName = null;	
	
	/**
	 * 
	 */
	protected boolean cbSendError = false;

	/**
	 * 
	 */
	protected boolean cbSendState = false;

	/**
	 * 
	 */
	protected boolean cbSendAllStates = false;

	
	/**
	 * Singleton
	 */
	private static final Communication coCommunication = new Communication();
	
	/**
	 * 
	 */
	private  Communication() {
	}

	/**
	 * Obtient le seul et unique controleur de la communication.
	 * @return le controleur
	 */
	public static Communication getInstance()
	{
		return coCommunication;
	}

	public static void initialize() {
		getInstance().addChannel(new ChannelSocket());			
		getInstance().startCommunication();
		cbInitialize = true;
	}

	public static boolean isInitialize() {
		return cbInitialize;
	}

	/**
	 * 
	 */
	public void addChannel(ICommunicationChannel poChannel) {
		poChannel.addCommunicationListener(this);
		coChannels.add(poChannel);
	}
	
	/**
	 * 
	 */
	public void startCommunication() {
		for (ICommunicationChannel loChannel : coChannels) {
			loChannel.startCommunication();
		}
	}
		
	/**
	 * 
	 */
	public void writeMessage(String pSMessage) {
		try {
			for (ICommunicationChannel loChannel : coChannels) {
				loChannel.writeMessage(pSMessage);
			}
		} catch (IOException e) {System.out.println("Unknowed exception : " + e.getMessage());}
	}
		
	/**
	 * 
	 */
	public synchronized void onEventReceived(CommunicationEvent poEvent) {
		if (poEvent.isValidCmd()) {
			processEventReceived(poEvent);			
		} else {
			String lSMessage = "Unknown command or bad command syntax or bad context (" + poEvent.getMessage() +") : " + poEvent.getError();
			System.out.println(lSMessage);
			writeMessage(lSMessage);
		}
	}
	
	/**
	 * 
	 */
	public synchronized void processEventReceived(CommunicationEvent poEvent) {
		try {
			if (poEvent.isDeviceListAddCmd()) {
				DeviceList.add((Device)poEvent.coConstructor.newInstance(poEvent.coParameterValues));
				MapLayer.getMapViewer().repaint();
				return;
			} else {
				if (poEvent.isSimpleCmd()) {
					Object returnObject = poEvent.coMethod.invoke(null, new Object[]{});
					StringBuffer devicesStr = new StringBuffer("<").append(poEvent.coMethod.getName()).append(">");
					if (returnObject instanceof Collection<?>) {
						Object[] objects = ((Collection<?>) returnObject).toArray();
						System.out.println("GETNODES : " + objects.length);
						for (Object o : objects) {
							List<Field> fieldList = new ArrayList<Field>();
						    Class<?> tmpClass = o.getClass();
							devicesStr.append("<o t=\"").append(tmpClass.getSimpleName()).append("\">");						    
						    while (tmpClass != null) {
						        fieldList.addAll(Arrays.asList(tmpClass .getDeclaredFields()));
						        tmpClass = tmpClass .getSuperclass();
						    }
							for (Field field : fieldList) {
								field.setAccessible(true);
								if (field.getType().isPrimitive() && !java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.get(o).getClass().getName()!="java.lang.Character") {
									//if (field.getName()=="radius"|| field.getName()=="radioRangeRadius") {
									//	System.out.println(field.getName() + "=" + field.get(o).toString());
									//}
									if (field.getName()=="id") {
										System.out.println(field.getName() + "=" + field.get(o).toString());
									}
									devicesStr.append("<v n=\"").append(field.getName()).append("\" v=\"").append(field.get(o).toString()).append("\"/>");
								}
							}
							devicesStr.append("</o>");
						}
					} else {
						
					}
					devicesStr.append("</").append(poEvent.coMethod.getName()).append(">");
					writeMessage(devicesStr.toString());
					return;
				} else {
					if (poEvent.isExitCmd()) {
						writeMessage("exit cupcarbon");
						Thread.sleep(500);
						System.exit(0);
						return;				
					} else {
						if (poEvent.isEchoCmd()) {
							writeMessage("echo cupcarbon");
							System.out.println("echo cupcarbon");
							return;				
						} else {
							if (poEvent.isRepaintCmd()) {
								writeMessage("repaint cupcarbon");
								System.out.println("repaint cupcarbon");
								Visualisation.coArrowsToDrawing.clear();
								MapLayer.mapViewer.repaint();
								return;
							}				
						}
					}
				}
			}
			String lSMessage = "Unknown command or bad command syntax or bad context (" + poEvent.getMessage() +") : " + poEvent.getError();
			System.out.println(lSMessage);
			writeMessage(lSMessage);			
        } catch(Exception ex) {
            System.out.println("COMMUNICATION ERROR : " + ex.getClass().getName() + "(" + ex.getMessage() + ")");
		}
	}
}
