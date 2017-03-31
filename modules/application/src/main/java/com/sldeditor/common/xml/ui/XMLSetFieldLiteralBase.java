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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XMLSetFieldLiteralBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XMLSetFieldLiteralBase"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}XMLFieldBase"&gt;
 *       &lt;attribute name="ignoreCheck" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XMLSetFieldLiteralBase")
@XmlSeeAlso({
    XMLSetFieldLiteralStringEx.class,
    XMLSetFieldLiteralIntEx.class,
    XMLSetFieldLiteralDoubleEx.class,
    XMLSetFieldLiteralBooleanEx.class
})
public class XMLSetFieldLiteralBase
    extends XMLFieldBase
{

    @XmlAttribute(name = "ignoreCheck")
    protected Boolean ignoreCheck;

    /**
     * Gets the value of the ignoreCheck property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIgnoreCheck() {
        if (ignoreCheck == null) {
            return false;
        } else {
            return ignoreCheck;
        }
    }

    /**
     * Sets the value of the ignoreCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreCheck(Boolean value) {
        this.ignoreCheck = value;
    }

}
