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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                     Provides colour ramp definitions
 *                 
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TwoColourRampList" type="{}XMLTwoColourRampList"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "twoColourRampList"
})
@XmlRootElement(name = "ColourRampPresets")
public class ColourRampPresets {

    @XmlElement(name = "TwoColourRampList", required = true)
    protected XMLTwoColourRampList twoColourRampList;

    /**
     * Gets the value of the twoColourRampList property.
     * 
     * @return
     *     possible object is
     *     {@link XMLTwoColourRampList }
     *     
     */
    public XMLTwoColourRampList getTwoColourRampList() {
        return twoColourRampList;
    }

    /**
     * Sets the value of the twoColourRampList property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLTwoColourRampList }
     *     
     */
    public void setTwoColourRampList(XMLTwoColourRampList value) {
        this.twoColourRampList = value;
    }

}
