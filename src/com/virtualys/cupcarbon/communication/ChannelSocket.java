package com.virtualys.cupcarbon.communication;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.event.EventListenerList;

import cupcarbon.CupCarbon;

public class ChannelSocket extends Channel {

	private class Data {
		Data() {}

		public void add(String pSData) {
			synchronized (coServerDataWorker.coDataQueue) {
				if (coServerDataWorker.coDataQueue.size()>=50) {
					coServerDataWorker.coDataQueue.poll();
				}
				coServerDataWorker.coDataQueue.add(pSData);
				if (coServerDataWorker.bFirstData) {
					coServerDataWorker.bFirstData = false;
				} else {
					coServerDataWorker.coDataQueue.notify();
				}
			}
		}

	}

	private class ServerDataWorker implements Runnable {
		protected Selector coDataSelector;
		protected ServerSocketChannel dataServerSocketChannel;
		
		public boolean bFirstData = true;
		public BlockingQueue<String> coDataQueue = new LinkedBlockingQueue<String>();

		public ServerDataWorker(Selector poSelector, ServerSocketChannel poServerSocketChannel) {
			coDataSelector = poSelector;
			dataServerSocketChannel = poServerSocketChannel;
		}

		public void run() {
    		StringBuilder stb = new StringBuilder();
    		String str2Send = new String("");
			//
			//
            while (cbActive) {
            	try { 
            		int n = coDataSelector.select();            		
            		if (n==0) {
            			continue;
            		}
            		str2Send ="";
        			synchronized (coDataQueue) {
        				if (!bFirstData) {
        					coDataQueue.wait();
        				}
        				stb.setLength(0);
        				while (coDataQueue.size() != 0) {
        					stb.append(coDataQueue.poll()).append((char)13).append((char)10);
        				}
        				str2Send = stb.toString();
            		}

            		Iterator<SelectionKey> iter = coDataSelector.selectedKeys().iterator();
		            while (iter.hasNext()) {		            		
		                SelectionKey key = (SelectionKey) iter.next();
		                iter.remove();
		                	
		                if (key.isAcceptable()) {
		                	SocketChannel client = dataServerSocketChannel.accept();
		                	client.configureBlocking(false);
		                	client.register(coDataSelector, SelectionKey.OP_WRITE|SelectionKey.OP_READ); // OP_READ pour d�tecter les d�connexions dans l'exception.
		                }
		                if (key.isWritable() && !"".equals(str2Send)) {		                
		                	SocketChannel client = (SocketChannel) key.channel();
		                	try {		                			
		                		ByteBuffer buffer = ByteBuffer.allocate(str2Send.length());
		                		buffer.put(str2Send.getBytes());
		                		buffer.flip();
		                		while(buffer.hasRemaining()) {
		                			client.write(buffer);
		                		}
			                } catch (Exception ex) {
			                	System.out.println("Unknowed exception : " + ex.getMessage());
			                	client.finishConnect();
			                	client.close();			                	
							}
		                }
		            }
            	} catch (Exception ex) {
        			System.out.println("Communication error : " + ex.getMessage());
            	}
            	
			}
		}
	}

	private class ServerCommand implements Runnable {

		private Selector commandSelector;
		private ServerSocketChannel commandServerSocketChannel;

		public ServerCommand(Selector poSelector, ServerSocketChannel poServerSocketChannel) {
			commandSelector = poSelector;
			commandServerSocketChannel = poServerSocketChannel;
		}
		
		public void run() {

//			Vector<SocketChannel> clients = new Vector<SocketChannel>();
			HashMap<SocketChannel, StringBuilder> clients = new HashMap<SocketChannel, StringBuilder>();
			
            ByteBuffer buffer = ByteBuffer.allocate(256);
			int i, r;            
			
			while (cbActive){
				try {				
            	//	///////////////////////////////////////////////////
					int readyChannels = 0;
					readyChannels = commandSelector.select();
					if (readyChannels==0) continue;
					
					/////////////////////////////////////////////////////
					if (commandSelector.isOpen()){
						Set<SelectionKey> selectedKeys = commandSelector.selectedKeys();
						Iterator<SelectionKey> iter = selectedKeys.iterator();
						while (iter.hasNext()) {
							SelectionKey key = (SelectionKey) iter.next();		                
							/////////////////////////////////////////////////////
							if (key.isAcceptable()) {
								try {
									SocketChannel client = commandServerSocketChannel.accept();
									client.configureBlocking(false);
									client.register(commandSelector, SelectionKey.OP_READ);
//									clients.add(client);
									clients.put(client, new StringBuilder());
								} catch (Exception ex) {
									System.out.println("[RUN_COMMAND_02] Communication error : " + ex.getMessage());
								}
							}
		            		/////////////////////////////////////////////////////
		                	else if (key.isReadable()) {
		                		SocketChannel client = (SocketChannel) key.channel();
		                		try {
		                			if (clients.containsKey(client)){
		                				buffer.rewind();					
		        						r = client.read(buffer);
		        						for (i=0;i<r;i++) {
		        							char c = (char)buffer.get(i);
		        							if (c==13 || c==10) {
		        								fireReceiveEvent(clients.get(client).toString());		        								
		        								clients.get(client).setLength(0);
		        								if ( ((i+1)<r) && (c==13 || c==10) ) {
		        									i++;
		        								}							
		        							} else {
		        								clients.get(client).append(c);	
		        							}
		        						}
		                			}
		                		} catch (IOException ex) {
		                			System.out.println("IO exception : " + ex.getMessage());
			                		clients.remove(client);
								} catch (Exception ex) {
									System.out.println("Unknowed exception : " + ex.getMessage());
								}
		                	}
		            		/////////////////////////////////////////////////////
		                	iter.remove();
		            	}
	            	}
				} catch (Exception ex) {
					System.out.println("Unknowed exception : " + ex.getMessage());
					//System.out.println("[RUN_COMMAND_01] Communication error : " + ex.getMessage());
				}
					
			}
		}
	}
	protected InetAddress coHostAddress;
	protected int ciPortCommand = 0;
	protected int ciPortData = 0;
	protected boolean cbActive = true;

	ServerDataWorker coServerDataWorker = null;	

	Data coData = new Data();

	protected final EventListenerList coListeners = new EventListenerList();

	public ChannelSocket() {
	}

	public void startCommunication() {
		try {
			String lSHost = "";
			if (lSHost == null || "".equals(lSHost) || "localhost".equals(lSHost)) {
				lSHost = "127.0.0.1";				
			}
			coHostAddress = InetAddress.getByName(lSHost);

			try {
				InputStream input = CupCarbon.class.getClassLoader().getResourceAsStream("cupcarbon.properties");
				if (input != null) {
					Properties prop = new Properties();
					prop.load(input);  
					ciPortCommand = Integer.valueOf((prop.getProperty("portCommand")==null) ? "0" : prop.getProperty("portCommand"));
					ciPortData    = Integer.valueOf((prop.getProperty("portData")==null)     ? "0" : prop.getProperty("portData"));
					input.close();
				} 
			} catch (IOException ex) {System.out.println("IO exception : " + ex.getMessage());}
			if (ciPortData == 0) {
				ciPortData = 15501;//9901;
			}
			if (ciPortCommand == 0) {
				ciPortCommand = 15500;//9900;
			}
			
			System.out.println("------------------------------------------------------------------------");
			System.out.println("Server communication mode");
			System.out.println("Communication : socket server data "+ coHostAddress + ":" + ciPortData);
			System.out.println("Communication : socket server command " + coHostAddress + ":" + ciPortCommand);
			//
			Selector selectorData = Selector.open();
			System.out.println("Data selector open : "+ selectorData.isOpen());
			
			ServerSocketChannel serverSocketChannelData = ServerSocketChannel.open();
			serverSocketChannelData.bind(new InetSocketAddress(coHostAddress, ciPortData));
			serverSocketChannelData.configureBlocking(false);			
			
			int ops = serverSocketChannelData.validOps();
			@SuppressWarnings("unused")
			SelectionKey selkeyData = serverSocketChannelData.register(selectorData, ops, null);
			
			coServerDataWorker = new ServerDataWorker(selectorData,serverSocketChannelData);
			Thread loThreadData = new Thread(coServerDataWorker, "ThreadServerData");
			loThreadData.start();			

			//////////////////////////////////////
			Selector selectorCommand = Selector.open();
			System.out.println("Command selector open : "+ selectorCommand.isOpen());

			ServerSocketChannel serverSocketChannelCommand = ServerSocketChannel.open();
			serverSocketChannelCommand.bind(new InetSocketAddress(coHostAddress, ciPortCommand));
			serverSocketChannelCommand.configureBlocking(false);

			ops = serverSocketChannelCommand.validOps();
			@SuppressWarnings("unused")
			SelectionKey selkeyCommand = serverSocketChannelCommand.register(selectorCommand, ops, null);
			
			ServerCommand loServerCommand = new ServerCommand(selectorCommand,serverSocketChannelCommand);
			Thread loThreadCommand = new Thread(loServerCommand, "ThreadServerCommand");
			loThreadCommand.start();
			System.out.println("------------------------------------------------------------------------");
		} catch (Exception ex) {
			System.out.println("[STARTCOMMUNICATION_MAIN] Communication error : " + ex.getMessage());
		}
	}

	public void finalize() {
		try {
		} catch (Exception ex) {
			System.out.println("[FINALIZE_MAIN] Communication error : " + ex.getMessage());
		}
	}

	public void stopChannel() {
		cbActive = true;
	}

	public void run() {
	}

	public void writeMessage(String pSMessage) throws IOException {
		synchronized (coData) {
			coData.add(pSMessage);
		}
	}
}