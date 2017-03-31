//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.31 at 03:41:39 PM BST 
//


package com.sldeditor.common.xml.ui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configuration to display a multi-option group
 *             
 * 
 * <p>Java class for XMLSetMultiOptionGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XMLSetMultiOptionGroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="multiOptionGroupId" use="required" type="{}GroupIdEnum" /&gt;
 *       &lt;attribute name="option" use="required" type="{}GroupIdEnum" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XMLSetMultiOptionGroup")
public class XMLSetMultiOptionGroup {

    @XmlAttribute(name = "multiOptionGroupId", required = true)
    protected GroupIdEnum multiOptionGroupId;
    @XmlAttribute(name = "option", required = true)
    protected GroupIdEnum option;

    /**
     * Gets the value of the multiOptionGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link GroupIdEnum }
     *     
     */
    public GroupIdEnum getMultiOptionGroupId() {
        return multiOptionGroupId;
    }

    /**
     * Sets the value of the multiOptionGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupIdEnum }
     *     
     */
    public void setMultiOptionGroupId(GroupIdEnum value) {
        this.multiOptionGroupId = value;
    }

    /**
     * Gets the value of the option property.
     * 
     * @return
     *     possible object is
     *     {@link GroupIdEnum }
     *     
     */
    public GroupIdEnum getOption() {
        return option;
    }

    /**
     * Sets the value of the option property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupIdEnum }
     *     
     */
    public void setOption(GroupIdEnum value) {
        this.option = value;
    }

}
