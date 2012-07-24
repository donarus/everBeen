//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.24 at 02:35:39 PM CEST 
//


package cz.cuni.mff.been.jaxb.td;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import cz.cuni.mff.been.jaxb.AbstractSerializable;


/**
 * <p>Java class for TaskProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaskProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://been.mff.cuni.cz/taskmanager/td}taskProperty" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaskProperties", propOrder = {
    "taskProperty"
})
public class TaskProperties
    extends AbstractSerializable
    implements Serializable
{

    private final static long serialVersionUID = -9223372036854775808L;
    protected List<TaskProperty> taskProperty;

    /**
     * Gets the value of the taskProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taskProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaskProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaskProperty }
     * 
     * 
     */
    public List<TaskProperty> getTaskProperty() {
        if (taskProperty == null) {
            taskProperty = new ArrayList<TaskProperty>();
        }
        return this.taskProperty;
    }

    public boolean isSetTaskProperty() {
        return ((this.taskProperty!= null)&&(!this.taskProperty.isEmpty()));
    }

    public void unsetTaskProperty() {
        this.taskProperty = null;
    }

}
