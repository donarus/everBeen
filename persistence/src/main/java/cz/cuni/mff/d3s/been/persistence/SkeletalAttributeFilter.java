package cz.cuni.mff.d3s.been.persistence;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * A skeletal implementation of the attribute filter
 *
 * @author darklight
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
class SkeletalAttributeFilter implements AttributeFilter {

	protected Map<String, String> values = new HashMap<String, String>();

	/**
	 * Serialization getter for value map
	 *
	 * @return The values
	 */
	public Map<String, String> getValues() {
		return values;
	}

	/**
	 * Serialization setter for value map
	 *
	 * @param values Values to set
	 */
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
}