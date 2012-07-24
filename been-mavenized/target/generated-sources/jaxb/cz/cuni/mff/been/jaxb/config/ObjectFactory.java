//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.24 at 02:35:38 PM CEST 
//


package cz.cuni.mff.been.jaxb.config;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cz.cuni.mff.been.jaxb.config package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Item_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "item");
    private final static QName _Typeargs_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "typeargs");
    private final static QName _Desc_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "desc");
    private final static QName _Default_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "default");
    private final static QName _Group_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "group");
    private final static QName _Type_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "type");
    private final static QName _Value_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "value");
    private final static QName _Constraint_QNAME = new QName("http://been.mff.cuni.cz/benchmarkmanagerng/config", "constraint");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cz.cuni.mff.been.jaxb.config
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link Group }
     * 
     */
    public Group createGroup() {
        return new Group();
    }

    /**
     * Create an instance of {@link Config }
     * 
     */
    public Config createConfig() {
        return new Config();
    }

    /**
     * Create an instance of {@link Type }
     * 
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link Value }
     * 
     */
    public Value createValue() {
        return new Value();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "item")
    public JAXBElement<Item> createItem(Item value) {
        return new JAXBElement<Item>(_Item_QNAME, Item.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "typeargs")
    public JAXBElement<String> createTypeargs(String value) {
        return new JAXBElement<String>(_Typeargs_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "desc")
    public JAXBElement<String> createDesc(String value) {
        return new JAXBElement<String>(_Desc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "default")
    public JAXBElement<String> createDefault(String value) {
        return new JAXBElement<String>(_Default_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Group }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "group")
    public JAXBElement<Group> createGroup(Group value) {
        return new JAXBElement<Group>(_Group_QNAME, Group.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "type")
    public JAXBElement<Type> createType(Type value) {
        return new JAXBElement<Type>(_Type_QNAME, Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Value }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "value")
    public JAXBElement<Value> createValue(Value value) {
        return new JAXBElement<Value>(_Value_QNAME, Value.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://been.mff.cuni.cz/benchmarkmanagerng/config", name = "constraint")
    public JAXBElement<String> createConstraint(String value) {
        return new JAXBElement<String>(_Constraint_QNAME, String.class, null, value);
    }

}
