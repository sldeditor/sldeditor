/**
 * 
 */
package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sldeditor.datasource.impl.NamespaceHelper;

/**
 * The unit test for NamespaceHelper.
 * <p>{@link com.sldeditor.datasource.impl.NamespaceHelper}
 *
 * @author Robert Ward (SCISYS)
 */
public class NamespaceHelperTest {

    /**
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#NamespaceHelper(org.w3c.dom.Node)}.
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#isElement(java.lang.String, java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#encode(java.lang.String, java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#getFullElement()}.
     */
    @Test
    public void testNamespaceHelper() {
        // Try with null
        NamespaceHelper namespace = new NamespaceHelper(null);

        String actualValue = namespace.getFullElement();
        assertTrue(actualValue.compareTo("") == 0);

        // Try with XML element
        String expectedElement = "tst:testElement";
        Element testElement = null;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element root = doc.createElement("root");
            doc.appendChild(root);

            // SLD contents
            testElement = doc.createElement(expectedElement);
            testElement.appendChild(doc.createTextNode("test value"));
            root.appendChild(testElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        namespace = new NamespaceHelper(testElement);

        actualValue = namespace.getFullElement();
        assertTrue(actualValue.compareTo(expectedElement) == 0);

        // Try NamespaceHelper.encode()
        String[] components = expectedElement.split(":");
        actualValue = NamespaceHelper.encode(components[0], components[1]);
        assertTrue(actualValue.compareTo(expectedElement) == 0);

        // Try NamespaceHelper.isElement
        assertFalse(namespace.isElement(null, null));
        assertFalse(namespace.isElement(components[0], null));
        assertFalse(namespace.isElement(null, components[1]));
        assertTrue(namespace.isElement(components[0], components[1]));
        assertFalse(namespace.isElement(components[1], components[0]));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#NamespaceHelper(org.w3c.dom.Node)}.
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#isElement(java.lang.String, java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#encode(java.lang.String, java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.NamespaceHelper#getFullElement()}.
     */
    @Test
    public void testNamespaceHelper2() {
        // Try with XML element
        String expectedElement = "testElement";
        Element testElement = null;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element root = doc.createElement("root");
            doc.appendChild(root);

            // SLD contents
            testElement = doc.createElement(expectedElement);
            testElement.appendChild(doc.createTextNode("test value"));
            root.appendChild(testElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        NamespaceHelper namespace = new NamespaceHelper(testElement);

        String actualValue = namespace.getFullElement();
        assertTrue(actualValue.compareTo(expectedElement) == 0);

        // Try NamespaceHelper.encode()
        actualValue = NamespaceHelper.encode(null, expectedElement);
        assertTrue(actualValue.compareTo(expectedElement) == 0);

        actualValue = NamespaceHelper.encode("", expectedElement);
        assertTrue(actualValue.compareTo(expectedElement) == 0);

        // Try NamespaceHelper.isElement
        assertFalse(namespace.isElement(null, null));
        assertFalse(namespace.isElement(null, expectedElement));
        assertTrue(namespace.isElement("", expectedElement));
    }
}
