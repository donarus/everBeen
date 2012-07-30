/*
 *  BEEN: Benchmarking Environment
 *  ==============================
 *
 *  File author: Andrej Podzimek
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
package cz.cuni.mff.been.webinterface.config;

import java.util.TreeSet;

import javax.servlet.ServletContext;

import cz.cuni.mff.been.jaxb.config.Item;
import cz.cuni.mff.been.jaxb.config.Type;
import cz.cuni.mff.been.jaxb.config.Value;
import cz.cuni.mff.been.webinterface.Routines;

/**
 * Converts user-defined enum types to their XHTML form representation. More user-defined types
 * could be defined in the future (type domains with limited value ranges, arbitrary data types
 * and the like). Anyway, this is the only 'special' type right now.
 * 
 * @author Andrej Podzimek
 */
public final class SpecialItemToXHTML implements ItemToXHTML {
	
	/** The enum type description generated by the JAXB parser. */
	private Type type;
	
	/** Whether a multiselect or a pull-down menu will be used. */
	private String multiple;
	
	/**
	 * Creates a transcoder instance from a Type instance.
	 * 
	 * @param type The data (enum) type read from Config.
	 */
	public SpecialItemToXHTML( Type type ) {
		this.type = type;
		this.multiple = type.isSetMulti() ?
			( type.isMulti() ? "\" multiple=\"multiple\">" : "\">" ) : "\">";
	}
	
	public String toXHTML( ServletContext application, String prefix, Item item ) {
		StringBuilder builder = new StringBuilder();
		String fullName;
		String escId;
		String escFullName;
		String escDesc;
		String escValueName;
		String[] inputValues;
		String selected;
		TreeSet< String > inputValueSet;
		
		fullName = prefix + item.getId();
		escId = Routines.htmlspecialchars( item.getId() );
		escFullName = prefix + escId;
		escDesc = Routines.htmlspecialchars( item.getDesc() );		
		inputValues = (String[]) application.getAttribute( fullName );
		
		if ( null == inputValues ) {
			String defaultValue = item.getDefault();
			inputValues = null == defaultValue ?
				new String[ 0 ] : new String[] { defaultValue };
		}
		inputValueSet = new TreeSet< String >();
		for ( String oneValue : inputValues ) {
			inputValueSet.add( oneValue );
		}
		
		builder
		.append( "<label>" )
		.append( escDesc ).append( " (" ).append( escId ).append( ')' )
		.append( "<br/><select name=\"" )
		.append( escFullName )
		.append( "\" title=\"" )
		.append( escDesc )
		.append(
			item.isSetEnabled() ? 
				( item.isEnabled() ? "\"" : "\" disabled=\"disabled\"" ) : "\""
		)
		.append( multiple );
		for ( Value value : type.getValue() ) {
			selected = inputValueSet.contains( value.getName() ) ?
				"\" selected=\"selected\">" : "\">";
			escValueName = Routines.htmlspecialchars( value.getName() );
			builder
			.append( "<option value=\"" )
			.append( Routines.htmlspecialchars( escValueName ) )
			.append( selected )
			.append( Routines.htmlspecialchars( value.getDesc() ) )
			.append( " (" ).append( escValueName ).append( ')' )
			.append( "</option>" );
		}
		builder
		.append( "</select></label>" );
		return builder.toString();
	}
}
