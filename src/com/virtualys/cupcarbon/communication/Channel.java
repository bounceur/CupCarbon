package com.virtualys.cupcarbon.communication;

import java.io.*;

import javax.swing.event.EventListenerList;


public class Channel extends Thread implements ICommunicationChannel {
	/**
	 * Gestionnaire d'evenements.
	 */
    protected final EventListenerList coListeners = new EventListenerList();
    
	/**
	 * 
	 */
	public Channel() {
	}
	
	/**
	 * 
	 */
	public void addCommunicationListener(ICommunicationListener poListener) {
		coListeners.add(ICommunicationListener.class, poListener);		
	}

	/**
	 * 
	 */
	public void removeCommunicationListener(ICommunicationListener poListener) {
		coListeners.remove(ICommunicationListener.class, poListener);		
	}

	/**
	 * Envoi un evenement exterieur vers le simulateur.
	 */
    protected void fireReceiveEvent(String pSMessage)
    {
        final Object[] laoListeners = coListeners.getListenerList();
        CommunicationEvent loEvent = null;
        for (int i = laoListeners.length - 2; i >= 0; i -= 2) {
            if (laoListeners[i] == ICommunicationListener.class) {
                if (loEvent == null) {
                    loEvent = new CommunicationEvent(this, pSMessage);
                }                
                ((ICommunicationListener) laoListeners[i + 1]).onEventReceived(loEvent);
            }
        }
    }
    
	/**
	 * 
	 */
	public void startCommunication() {
	}
		
	/**
	 * 
	 */
	public void writeMessage(String pSMessage) throws IOException {
	}    
}
