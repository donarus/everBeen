/*
 *  BEEN: Benchmarking Environment
 *  ==============================
 *
 *  File author: Branislav Repcek
 *
 *  GNU Lesser General Public License Version 2.1
 *  ---------------------------------------------
 *  Copyright (C) 2004-2006 Distributed Systems Research Group,
 *  Faculty of Mathematics and Physics, Charles University in Prague
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License version 2.1, as published by the Free Software Foundation.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *  MA  02111-1307  USA
 */

package cz.cuni.mff.been.hostmanager;

import java.io.Serializable;

/**
 * Simple listener that accepts events generated by the Host Manager.
 *
 * @author Branislav Repcek
 */
public abstract class HostManagerEventListener implements Serializable {

	private static final long	serialVersionUID	= -210628392745545012L;
	
	/**
	 * Flag which specifies whether listener should be unregistered.
	 */
	private boolean remove;
	
	/**
	 * Default ctor.
	 */
	public HostManagerEventListener() {
		
		remove = false;
	}
	
	/**
	 * Process event received from the Host Manager.
	 * 
	 * @param event Event to process.
	 */
	public abstract void processEvent(HostManagerEvent event);
	
	/**
	 * This method sets remove flag so that listener manager can automatically remove listener
	 * from the registry. You can call this method in any moment during the event processing.
	 * When event is processed, listener will be automatically unregistered and will not receive any
	 * further events.
	 * <br>
	 * This is provided as a convenience since you may want to create listeners that wait for
	 * specific event, then perform some action and unregister themselves automatically.
	 */
	public void removeMe() {
		
		remove = true;
	}
	
	/**
	 * Test if this listener should be unregistered.
	 * 
	 * @return <tt>true</tt> if this listener should be unregistered when current event has been
	 *         processed. Returns <tt>false</tt> if listener will not be automatically unregistered.
	 */
	public boolean remove() {
		
		return remove;
	}
}
