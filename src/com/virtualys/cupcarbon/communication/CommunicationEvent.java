package com.virtualys.cupcarbon.communication;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EventObject;


/**
 * 
 * @author olivier MARC (Virtualys - http://www.virtualys.com)
 */
public class CommunicationEvent
	extends EventObject
{
	/**
	 * 
	 */
	final static long serialVersionUID = 0; 
	/**
	 * 
	 */
	private String cSMessage;
	/**
	 * 
	 */
	private String cSError = "";
	/**
	 * 
	 */
	public Method coMethod = null;
	public Constructor<?> coConstructor = null;	
	public Object[] coParameterValues = null;	
	/**
	 * 
	 */
	private enum Type {INVALID_CMD, SIMPLE_CMD, DEVICELIST_ADD_CMD, EXIT_CMD, ECHO_CMD, REPAINT_CMD};
	private Type ceType = Type.INVALID_CMD;
	/**
	 * 
	 */
	public CommunicationEvent(ICommunicationChannel poCommunication, String pSMessage)
	{
		super(poCommunication);
		cSMessage = pSMessage;		
		try {
			String[] st = cSMessage.split(" ");
			//
			//
			if (st.length>1) {
				if ("cupcarbon".equals(st[0])) {
					if (st[1].contains("echo")) {
						ceType = Type.ECHO_CMD;							
					} else {
						if (st[1].contains("exit")) {
							ceType = Type.EXIT_CMD;
						} else {
							if (st[1].contains("repaint")) {
								ceType = Type.REPAINT_CMD;
							}							
						}
					}
				} else {
					if (st.length>2 && "device.DeviceList".equals(st[0]) && "add".equals(st[1])) {
						Class<?> loDeviceListClass = Class.forName(st[0]);
						coMethod = loDeviceListClass.getMethod(st[1], new Class[] { Class.forName("device.Device") } );
						Class<?> loClass = Class.forName(st[2]);
						Class<?>[] parameterClasses = null;			
						if ((st.length-3)>0) {
							parameterClasses = new Class[st.length-3];
							coParameterValues = new Object[st.length-3];
							for (int i=3;i<st.length;++i) {
								parameterClasses[i-3] = String.class;
								coParameterValues[i-3] = st[i].substring(2);
								if (st[i].contains("d=")) {
									parameterClasses[i-3] = double.class;
									coParameterValues[i-3] = Double.parseDouble(st[i].substring(2));
								} else {
									if (st[i].contains("i=")) {
										parameterClasses[i-3] = int.class;
										coParameterValues[i-3] = Integer.parseInt(st[i].substring(2));
									}
								}
							}
							coConstructor = loClass.getConstructor(parameterClasses);
							ceType = Type.DEVICELIST_ADD_CMD;	
						}
					} else {
						Class<?> loClass = Class.forName(st[0]);
						coMethod = loClass.getMethod(st[1], (Class<?>[])null);							
						ceType = Type.SIMPLE_CMD;
					}
				}
			}
		} catch(Exception ex) {
			cSError = ex.getClass().getName() + "(" + ex.getMessage() + ")";
			System.out.println("Unknowed exception : " + ex.getMessage());
		}
	}
	
	public String getMessage() {
		return cSMessage;
	}	

	public String getError() {
		return cSError;
	}	
	
	public boolean isValidCmd() {
		return ceType!=Type.INVALID_CMD;
	}

	public boolean isSimpleCmd() {
		return ceType==Type.SIMPLE_CMD;
	}
	
	public boolean isDeviceListAddCmd() {
		return ceType==Type.DEVICELIST_ADD_CMD;
	}
	
	public boolean isExitCmd() {
		return ceType==Type.EXIT_CMD;
	}
	
	public boolean isEchoCmd() {
		return ceType==Type.ECHO_CMD;
	}
	
	public boolean isRepaintCmd() {
		return ceType==Type.REPAINT_CMD;
	}
}
