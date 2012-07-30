/*
 *  BEEN: Benchmarking Environment
 *  ==============================
 *
 *  File author: David Majda
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
package cz.cuni.mff.been.benchmarkmanagerng;

import cz.cuni.mff.been.common.RMISafeException;

/**
 * 
 * @author Jiri Tauber
 */
public class BenchmarkManagerException extends RMISafeException {
    
	private static final long	serialVersionUID	= -1068991141449033455L;

	/**
	 * Allocates a new <code>BenchmarkManagerException</code> object.
	 */
	public BenchmarkManagerException() {
		super();
	}
    
	/**
	 * Allocates a new <code>BenchmarkManagerException</code> object
	 * with specified message.
	 * 
	 * @param message exception message
	 */
	public BenchmarkManagerException(String message) {
		super(message);
	}
    
	/**
	 * Allocates a new <code>BenchmarkManagerException</code> object
	 * with specified cause.
	 *
	 * @param cause exception cause
	 */
	public BenchmarkManagerException(Throwable cause) {
		super(cause);
	}
    
	/**
	 * Allocates a new <code>BenchmarkManagerException</code> object
	 * with specified message and cause.
	 *
	 * @param message exception message
	 * @param cause exception cause
	 */
	public BenchmarkManagerException(String message, Throwable cause) {
		super(message);
	}

}
