package com.virtualys.cupcarbon.communication.client.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * 
 * @author olive
 *
 */
public class ClientCommand {
	static protected String host = "localhost";	
	static protected int port = 15500;//9900;
	
	//
	//
	public static void main(String[] args) throws Exception {
		try {
			if (args.length>0) {
				if (args[0].contains(":")) {
					String[] connexionParam = args[0].split(":");
					host = connexionParam[0];	
					port = Integer.valueOf(connexionParam[1]);
				} else {
					port = Integer.parseInt(args[0]);
				}
			}
			System.out.println("Connect to " + host + ":" + port);			
			Socket loSocket = new Socket(host, port);
			System.out.println("SOCKET = " + loSocket);
			System.out.println("device.DeviceList add device.StdSensorNode d=48.39188295873048 d=-4.44371223449707 d=0 d=100 d=20 i=-1");
			System.out.println("device.DeviceList add device.StdSensorNode d=48.39052932524008 d=-4.486013352870941 d=0 d=100 d=20 i=-1");
			System.out.println("device.DeviceList add natural_events.Gas d=48.39069318805196 d=-4.4876790046691895 d=100 i=-1");			
			//
			//
			PrintWriter pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(loSocket.getOutputStream())), true);
			//
			// 
			Scanner sc = new Scanner(System.in);
			String in = new String();
			while (!in.equals("cupcarbon exit")) {	        
				if (sc.hasNextLine()) {
					in = sc.nextLine();
					pred.println(in);
				}
			}
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				System.out.println("Unknowed exception : " + e.getMessage());
			}
			sc.close();        
			loSocket.close();
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage() + " port:" + port);
		}
	}
}
