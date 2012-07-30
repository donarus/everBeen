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
package cz.cuni.mff.been.common.rsl;

import java.util.regex.Pattern;

/**
 * Class representing a RSL condition with "=~" operator.
 * 
 * Class is parametrized by the type of value on the right side of the operator,
 * but this is only for consistency with other conditions - in fact only Pattern
 * is allowed (this is checked by the <code>check</code> method). 
 * 
 * @param <T> type of the value
 * @author David Majda
 */
public class DoesNotMatchCondition<T> extends PatternCondition<T> {

	private static final long	serialVersionUID	= -2406470388353739940L;

	/**
	 * Allocates a new <code>DoesNotMatchCondition</code> object.
	 * 
	 * @param propertyPath property path
	 * @param value regular expression to match against
	 */
	public DoesNotMatchCondition(String propertyPath, T value) {
		super(propertyPath, value);
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return propertyPath + " !~ " + toRSL(value);
	}

	/**
	 * Evaluates represented RSL condition within given context.
	 * 
	 * @param context root of the property tree, which defines the context
	 * @return <code>true</code> if the represented RSL condition holds in
	 *          given context;
	 *          <code>false</code> otherwise
	 *          
	 * @see cz.cuni.mff.been.common.rsl.Condition#evaluate(cz.cuni.mff.been.common.rsl.ContainerProperty)
	 */
	@Override
	public boolean evaluate(ContainerProperty context) {
		for (SimpleProperty p: pathToProperties(propertyPath, context)) {
			if (p.getValue() != null
					&& !((Pattern) this.value).matcher((String) p.getValue()).find()) {
				return true;
			}
		}
		return false;
	}

}
