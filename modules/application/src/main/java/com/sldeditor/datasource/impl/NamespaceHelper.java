/**
 * 
 */
package com.sldeditor.datasource.impl;

import org.w3c.dom.Node;

/**
 * Class to help with the decoding of XML elements.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class NamespaceHelper {

    private String prefix = "";

    /** The element name. */
    private String elementName = "";

    /**
     * Instantiates a new namespace by decoding a Node object.
     *
     * @param node the node
     */
    public NamespaceHelper(Node node) {
        if(node != null)
        {
            String[] component = node.getNodeName().split(":");

            if(component.length == 2)
            {
                prefix = component[0];
                elementName = component[1];
            }
            else if(component.length == 1)
            {
                prefix = "";
                elementName = component[0];
            }
        }
    }

    /**
     * Checks if this element matches the requested.
     *
     * @param requestedNamespacePrefix the requested namespace prefix
     * @param requestedElementName the requested element name
     * @return true, if is element matches
     */
    public boolean isElement(String requestedNamespacePrefix, String requestedElementName) {
        if((requestedNamespacePrefix != null) && (requestedElementName != null))
        {
            if((requestedNamespacePrefix.compareToIgnoreCase(prefix) == 0) &&
                    (requestedElementName.compareToIgnoreCase(elementName) == 0))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Encode XML element.
     *
     * @param namespacePrefix the namespace prefix
     * @param element the element
     * @return the string
     */
    public static String encode(String namespacePrefix, String element) {
        if((namespacePrefix != null) && !namespacePrefix.isEmpty())
        {
            return String.format("%s:%s", namespacePrefix, element);
        }
        else
        {
            return element;
        }
    }

    /**
     * Gets the full element.
     *
     * @return the full element
     */
    public String getFullElement() {
        return encode(prefix, elementName);
    }
}
