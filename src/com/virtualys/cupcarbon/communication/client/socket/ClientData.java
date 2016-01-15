package com.virtualys.cupcarbon.communication.client.socket;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientData {
	static protected String host = "localhost";	
	static protected int port = 15501;//9901;
	
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
			//
			//
//			Socket loSocketData = new Socket(host, portData);
//			java.io.PrintWriter pred = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.OutputStreamWriter(loSocketData.getOutputStream())), true);
			
			//-------------------------Modifications DEBUT---------------------------------------------
			System.out.println("Connect to " + host + ":" + port);			
			SocketChannel loSocketChannel = SocketChannel.open();
			loSocketChannel.connect(new InetSocketAddress(host, port));			
			System.out.println("SOCKET = " + loSocketChannel.socket());
			loSocketChannel.configureBlocking(true);
			//-------------------------Modifications FIN---------------------------------------------
			long time, time_init = System.currentTimeMillis();			
			int bufferSize = 256;
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
			StringBuilder stb = new StringBuilder();
			int i, r;
			try {
				while (true) {
					buffer.rewind();					
					r = loSocketChannel.read(buffer);
					for (i=0;i<r;i++) {
						char c = (char)buffer.get(i);
						if (c==13 || c==10) {				
							System.out.print("'" + stb.toString() + "' time=");
							stb.setLength(0);
							if ( ((i+1)<r) && (c==13 || c==10) ) {
								i++;
							}
							time = System.currentTimeMillis();							
							System.out.println((time-time_init));
							time_init = time;
						} else {
							stb.append(c);					
						}
					}
				}
			} catch(Exception e) {
				System.out.println("Exception : " + e.getMessage());
			}
//			loSocketData.close();
			loSocketChannel.close();
			System.out.println("Connection closed.");			
		}  catch(Exception e) {
			System.out.println(e.getMessage() + " port:" + port);
		}
	}
}
