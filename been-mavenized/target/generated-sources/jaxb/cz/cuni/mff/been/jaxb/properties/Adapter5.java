//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.24 at 02:35:39 PM CEST 
//


package cz.cuni.mff.been.jaxb.properties;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter5
    extends XmlAdapter<String, Boolean>
{


    public Boolean unmarshal(String value) {
        return new Boolean(value);
    }

    public String marshal(Boolean value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
