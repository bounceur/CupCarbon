package com.virtualys.cupcarbon.communication;

import java.io.IOException;

/**
 * @author Olive (Virtualys - http://www.virtualys.com)
 * @version 1.0
 */
public interface ICommunicationChannel
{
	/**
	 * 
	 */
	public void startCommunication();

	/**
	 * 
	 */
	public void writeMessage(String pSMessage) throws IOException ;
	
	/**
	 * 
	 */
	public void addCommunicationListener(ICommunicationListener poListener);
	
	/**
	 * 
	 */
	public void removeCommunicationListener(ICommunicationListener poListener);
	
}
