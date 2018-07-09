//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, vhudson-jaxb-ri-2.1-833
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: [TEXT REMOVED by maven-replacer-plugin]
//

package com.sldeditor.common.xml.ui;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VendorOption" type="{}XMLVendorOption" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PanelTests" type="{}XMLPanelTest" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="testsldfile" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"vendorOption", "panelTests"})
@XmlRootElement(name = "SldEditor_test")
public class SldEditorTest {

    @XmlElement(name = "VendorOption")
    protected List<XMLVendorOption> vendorOption;

    @XmlElement(name = "PanelTests")
    protected List<XMLPanelTest> panelTests;

    @XmlAttribute(required = true)
    protected String testsldfile;

    /**
     * Gets the value of the vendorOption property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the vendorOption property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     *    getVendorOption().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link XMLVendorOption }
     */
    public List<XMLVendorOption> getVendorOption() {
        if (vendorOption == null) {
            vendorOption = new ArrayList<XMLVendorOption>();
        }
        return this.vendorOption;
    }

    /**
     * Gets the value of the panelTests property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the panelTests property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     *    getPanelTests().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link XMLPanelTest }
     */
    public List<XMLPanelTest> getPanelTests() {
        if (panelTests == null) {
            panelTests = new ArrayList<XMLPanelTest>();
        }
        return this.panelTests;
    }

    /**
     * Gets the value of the testsldfile property.
     *
     * @return possible object is {@link String }
     */
    public String getTestsldfile() {
        return testsldfile;
    }

    /**
     * Sets the value of the testsldfile property.
     *
     * @param value allowed object is {@link String }
     */
    public void setTestsldfile(String value) {
        this.testsldfile = value;
    }
}
