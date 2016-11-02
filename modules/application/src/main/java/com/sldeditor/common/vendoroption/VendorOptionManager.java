/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sldeditor.common.vendoroption;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ParseXML;

/**
 * Manages access to the supported vendor options.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionManager
{

    /** The Constant RESOURCE_FILE. */
    private static final String RESOURCE_FILE = "/vendoroption/versions.xml";

    /** The instance. */
    private static VendorOptionManager instance = null;

    /** The vendor option map. */
    private Map<String, VendorOptionTypeInterface> vendorOptionMap = new HashMap<String, VendorOptionTypeInterface>();

    /** The vendor option class map. */
    private Map<Class<?>, VendorOptionTypeInterface> vendorOptionClassMap = new HashMap<Class<?>, VendorOptionTypeInterface>();

    /** The default vendor option version. */
    private VendorOptionVersion defaultVendorOptionVersion = null;

    /** The default vendor option. */
    private VendorOptionTypeInterface defaultVendorOption = null;

    /**
     * Instantiates a new vendor option manager.
     */
    private VendorOptionManager()
    {
        internal_addVendorOption(new NoVendorOption());
        internal_addVendorOption(new GeoServerVendorOption());

        populate();
    }

    /**
     * Populate.
     */
    private void populate()
    {
        InputStream fXmlFile = VendorOptionManager.class.getResourceAsStream(RESOURCE_FILE);

        if(fXmlFile == null)
        {
            ConsoleManager.getInstance().error(VendorOptionManager.class, Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource") + RESOURCE_FILE);
            return;
        }

        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getFirstChild().getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String nodeName = nNode.getNodeName();
                    if(nodeName != null)
                    {
                        if(nodeName.compareToIgnoreCase("VendorOption") == 0)
                        {
                            String className = eElement.getAttribute("class");

                            Class<?> classType = Class.forName(className);

                            VendorOptionTypeInterface veType = getClass(classType);

                            // Add the 'Not Set' option
                            veType.addVersion(new VersionData());

                            NodeList versionList = eElement.getElementsByTagName("Version");

                            for(int versionIndex = 0; versionIndex < versionList.getLength(); versionIndex ++)
                            {
                                Node vNode = versionList.item(versionIndex);

                                if (vNode.getNodeType() == Node.ELEMENT_NODE) {

                                    Element vElement = (Element) vNode;

                                    String versionString = vElement.getTextContent();

                                    VersionData versionData = veType.getVersion(versionString);
                                    veType.addVersion(versionData);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal_add vendor option.
     *
     * @param vendorOption the vendor option
     */
    private void internal_addVendorOption(VendorOptionTypeInterface vendorOption)
    {
        vendorOptionClassMap.put(vendorOption.getClass(), vendorOption);
        vendorOptionMap.put(vendorOption.getName(), vendorOption);
    }

    /**
     * Gets the single instance of VendorOptionManager.
     *
     * @return single instance of VendorOptionManager
     */
    public static VendorOptionManager getInstance()
    {
        if(instance == null)
        {
            instance = new VendorOptionManager();
        }

        return instance;
    }

    /**
     * Gets the VendorOption for the given class name.
     *
     * @param classType the class type
     * @return the class
     */
    public VendorOptionTypeInterface getClass(Class<?> classType)
    {
        return vendorOptionClassMap.get(classType);
    }

    /**
     * Gets the vendor option version.
     *
     * @param classType the class type
     * @param startVersion the start version
     * @param endVersion the end version
     * @return the vendor option version
     */
    public VendorOptionVersion getVendorOptionVersion(Class<?> classType, String startVersion, String endVersion)
    {
        if(classType == null)
        {
            return null;
        }

        VendorOptionTypeInterface veType = vendorOptionClassMap.get(classType);

        VersionData minimum = veType.getVersion(startVersion);
        if(minimum == null)
        {
            minimum = VersionData.getEarliestVersion(veType.getClass());
        }
        VersionData maximum = veType.getVersion(endVersion);
        if(maximum == null)
        {
            maximum = VersionData.getLatestVersion(veType.getClass());
        }

        return new VendorOptionVersion(classType, minimum, maximum);
    }

    /**
     * Gets the default vendor option.
     *
     * @return the default vendor option
     */
    public VendorOptionTypeInterface getDefaultVendorOption()
    {
        if(defaultVendorOption == null)
        {
            defaultVendorOption = getClass(NoVendorOption.class);
        }

        return defaultVendorOption;
    }

    /**
     * Gets the default vendor option version.
     *
     * @return the default vendor option version
     */
    public VendorOptionVersion getDefaultVendorOptionVersion()
    {
        if(defaultVendorOptionVersion == null)
        {
            VersionData minimum = VersionData.getEarliestVersion(NoVendorOption.class);
            VersionData maximum = VersionData.getLatestVersion(NoVendorOption.class);

            defaultVendorOptionVersion = new VendorOptionVersion(NoVendorOption.class, minimum, maximum);
        }

        return defaultVendorOptionVersion;
    }

    /**
     * Gets the vendor option version.
     *
     * @param classType the class type
     * @return the vendor option version
     */
    public VendorOptionVersion getVendorOptionVersion(Class<?> classType)
    {
        if(classType == null)
        {
            return null;
        }

        VersionData minimum = VersionData.getEarliestVersion(classType);
        VersionData maximum = VersionData.getLatestVersion(classType);

        return new VendorOptionVersion(classType, minimum, maximum);
    }

    /**
     * Checks if vendor option version is allowed.
     *
     * @param versionList the version list
     * @param vendorOptionVersion the vendor option version
     * @return true, if is allowed
     */
    public boolean isAllowed(List<VersionData> versionList, VendorOptionVersion vendorOptionVersion)
    {
        if((versionList != null) && (vendorOptionVersion != null))
        {
            for(VersionData versionData : versionList)
            {
                if(vendorOptionVersion.isAllowed(versionData))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the default vendor option version data.
     *
     * @return the default vendor option version data
     */
    public VersionData getDefaultVendorOptionVersionData()
    {
        VendorOptionVersion version = getDefaultVendorOptionVersion();

        return version.getLatest();
    }

    /**
     * Gets the title.
     *
     * @param vendorOptionVersion the vendor option version
     * @return the title
     */
    public String getTitle(VendorOptionVersion vendorOptionVersion) {
        StringBuilder title = new StringBuilder();
        title.append("- ");
        title.append(Localisation.getString(ParseXML.class, "ParseXML.vendorOption"));
        title.append(" ");

        if(vendorOptionVersion != null)
        {
            VendorOptionTypeInterface vendorOption = vendorOptionClassMap.get(vendorOptionVersion.getClassType());
            if(vendorOption != null)
            {
                title.append("(");
                title.append(vendorOption.getName());
                title.append(" ");
                VersionData earliest = vendorOptionVersion.getEarliest();
                VersionData latest = vendorOptionVersion.getLatest();
                title.append(earliest.toString());

                if(earliest.toString().compareTo(latest.toString()) != 0)
                {
                    title.append("-");
                    title.append(latest.toString());
                }
                title.append(")");
            }
        }

        return title.toString();
    }
}
