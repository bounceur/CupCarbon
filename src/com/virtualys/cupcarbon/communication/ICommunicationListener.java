package com.virtualys.cupcarbon.communication;

import java.util.EventListener;

/**
 * 
 * @author Olive (Virtualys - http://www.virtualys.com)
 * @version 1.0
 *
 */
public interface ICommunicationListener
	extends EventListener
{
	/**
	 * 
	 * @param poEvent
	 */
	public void onEventReceived(CommunicationEvent poEvent);
}
