//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: [TEXT REMOVED by maven-replacer-plugin]
//


package com.sldeditor.common.xml.ui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XMLGroupConfigData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XMLGroupConfigData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="id" use="required" type="{}GroupIdEnum" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="showLabel" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="option" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XMLGroupConfigData")
@XmlSeeAlso({
    XMLGroupConfig.class
})
public class XMLGroupConfigData {

    @XmlAttribute(name = "id", required = true)
    protected GroupIdEnum id;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "showLabel")
    protected Boolean showLabel;
    @XmlAttribute(name = "option")
    protected Boolean option;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link GroupIdEnum }
     *     
     */
    public GroupIdEnum getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupIdEnum }
     *     
     */
    public void setId(GroupIdEnum value) {
        this.id = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the showLabel property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowLabel() {
        if (showLabel == null) {
            return false;
        } else {
            return showLabel;
        }
    }

    /**
     * Sets the value of the showLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowLabel(Boolean value) {
        this.showLabel = value;
    }

    /**
     * Gets the value of the option property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOption() {
        if (option == null) {
            return false;
        } else {
            return option;
        }
    }

    /**
     * Sets the value of the option property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOption(Boolean value) {
        this.option = value;
    }

}
