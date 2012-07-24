//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.24 at 02:35:38 PM CEST 
//


package cz.cuni.mff.been.jaxb.pmc;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.been.jaxb.AbstractSerializable;


/**
 * <p>Java class for Dependency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Dependency">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://been.mff.cuni.cz/pluggablemodule/config}dependencyAttrGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Dependency")
public class Dependency
    extends AbstractSerializable
    implements Serializable
{

    private final static long serialVersionUID = -9223372036854775808L;
    @XmlAttribute(name = "moduleName", required = true)
    protected String moduleName;
    @XmlAttribute(name = "moduleVersion", required = true)
    protected String moduleVersion;

    /**
     * Gets the value of the moduleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Sets the value of the moduleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModuleName(String value) {
        this.moduleName = value;
    }

    public boolean isSetModuleName() {
        return (this.moduleName!= null);
    }

    /**
     * Gets the value of the moduleVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModuleVersion() {
        return moduleVersion;
    }

    /**
     * Sets the value of the moduleVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModuleVersion(String value) {
        this.moduleVersion = value;
    }

    public boolean isSetModuleVersion() {
        return (this.moduleVersion!= null);
    }

}
