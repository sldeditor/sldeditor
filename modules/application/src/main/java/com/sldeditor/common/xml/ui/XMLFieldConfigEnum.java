//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.23 at 06:11:16 PM GMT 
//


package com.sldeditor.common.xml.ui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configuration for a drop down list of enumerations field
 *             
 * 
 * <p>Java class for XMLFieldConfigEnum complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XMLFieldConfigEnum"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}XMLFieldConfigData"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ValueList" type="{}XMLFieldConfigEnumValueList"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XMLFieldConfigEnum", propOrder = {
    "valueList"
})
public class XMLFieldConfigEnum
    extends XMLFieldConfigData
{

    @XmlElement(name = "ValueList", required = true)
    protected XMLFieldConfigEnumValueList valueList;

    /**
     * Gets the value of the valueList property.
     * 
     * @return
     *     possible object is
     *     {@link XMLFieldConfigEnumValueList }
     *     
     */
    public XMLFieldConfigEnumValueList getValueList() {
        return valueList;
    }

    /**
     * Sets the value of the valueList property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLFieldConfigEnumValueList }
     *     
     */
    public void setValueList(XMLFieldConfigEnumValueList value) {
        this.valueList = value;
    }

}
