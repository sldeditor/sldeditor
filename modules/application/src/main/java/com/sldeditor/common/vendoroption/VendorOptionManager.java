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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.styling.StyledLayerDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.vendoroption.minversion.MinimumVersion;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.ui.panels.GetMinimumVersionInterface;

/**
 * Manages access to the supported vendor options.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionManager {

    /** The Constant RESOURCE_FILE. */
    private static final String RESOURCE_FILE = "/vendoroption/versions.xml";

    /** The instance. */
    private static VendorOptionManager instance = null;

    /** The vendor option map. */
    private Map<String, VendorOptionTypeInterface> vendorOptionMap = new ConcurrentHashMap<String, VendorOptionTypeInterface>();

    /** The vendor option class map. */
    private Map<Class<?>, VendorOptionTypeInterface> vendorOptionClassMap = new ConcurrentHashMap<Class<?>, VendorOptionTypeInterface>();

    /** The default vendor option version. */
    private VendorOptionVersion defaultVendorOptionVersion = null;

    /** The default vendor option. */
    private VendorOptionTypeInterface defaultVendorOption = null;

    /** The vendor option listener list. */
    private List<VendorOptionUpdateInterface> vendorOptionListenerList = Collections
            .synchronizedList(new ArrayList<VendorOptionUpdateInterface>());

    /** The selected vendor options. */
    private List<VersionData> selectedVendorOptions = Collections
            .synchronizedList(new ArrayList<VersionData>());

    /** The flag indicating whether vendor option overridden. */
    private boolean vendorOptionOverridden = false;

    /**
     * Instantiates a new vendor option manager.
     */
    private VendorOptionManager() {
        internal_addVendorOption(new NoVendorOption());
        internal_addVendorOption(new GeoServerVendorOption());

        selectedVendorOptions.add(this.getDefaultVendorOptionVersionData());

        populate();
    }

    /**
     * Populate.
     */
    private void populate() {
        InputStream fXmlFile = VendorOptionManager.class.getResourceAsStream(RESOURCE_FILE);

        if (fXmlFile == null) {
            ConsoleManager.getInstance().error(VendorOptionManager.class,
                    Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource")
                            + RESOURCE_FILE);
            return;
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getFirstChild().getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String nodeName = nNode.getNodeName();
                    if (nodeName != null) {
                        if (nodeName.compareToIgnoreCase("VendorOption") == 0) {
                            String className = eElement.getAttribute("class");

                            Class<?> classType = Class.forName(className);

                            VendorOptionTypeInterface veType = getClass(classType);

                            // Add the 'Not Set' option
                            veType.addVersion(new VersionData());

                            NodeList versionList = eElement.getElementsByTagName("Version");

                            for (int versionIndex = 0; versionIndex < versionList
                                    .getLength(); versionIndex++) {
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
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /**
     * Internal_add vendor option.
     *
     * @param vendorOption the vendor option
     */
    private void internal_addVendorOption(VendorOptionTypeInterface vendorOption) {
        vendorOptionClassMap.put(vendorOption.getClass(), vendorOption);
        vendorOptionMap.put(vendorOption.getName(), vendorOption);
    }

    /**
     * Gets the single instance of VendorOptionManager.
     *
     * @return single instance of VendorOptionManager
     */
    public static VendorOptionManager getInstance() {
        if (instance == null) {
            instance = new VendorOptionManager();
        }

        return instance;
    }

    /**
     * Destroy instance.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Gets the VendorOption for the given class name.
     *
     * @param classType the class type
     * @return the class
     */
    public VendorOptionTypeInterface getClass(Class<?> classType) {
        return vendorOptionClassMap.get(classType);
    }

    /**
     * Gets the vendor option version.
     *
     * @param classType the class type
     * @return the vendor option version
     */
    public VendorOptionVersion getVendorOptionVersion(Class<?> classType) {
        if (classType == null) {
            return null;
        }

        VersionData minimum = VersionData.getEarliestVersion(classType);
        VersionData maximum = VersionData.getLatestVersion(classType);

        return new VendorOptionVersion(classType, minimum, maximum);
    }

    /**
     * Gets the vendor option version.
     *
     * @param classType the class type
     * @param startVersion the start version
     * @param endVersion the end version
     * @return the vendor option version
     */
    public VendorOptionVersion getVendorOptionVersion(Class<?> classType, String startVersion,
            String endVersion) {
        if (classType == null) {
            return null;
        }

        VendorOptionTypeInterface veType = vendorOptionClassMap.get(classType);

        VersionData minimum = veType.getVersion(startVersion);
        if (minimum == null) {
            minimum = VersionData.getEarliestVersion(veType.getClass());
        }
        VersionData maximum = veType.getVersion(endVersion);
        if (maximum == null) {
            maximum = VersionData.getLatestVersion(veType.getClass());
        }

        return new VendorOptionVersion(classType, minimum, maximum);
    }

    /**
     * Gets the default vendor option.
     *
     * @return the default vendor option
     */
    public VendorOptionTypeInterface getDefaultVendorOption() {
        if (defaultVendorOption == null) {
            defaultVendorOption = getClass(NoVendorOption.class);
        }

        return defaultVendorOption;
    }

    /**
     * Gets the default vendor option version.
     *
     * @return the default vendor option version
     */
    public VendorOptionVersion getDefaultVendorOptionVersion() {
        if (defaultVendorOptionVersion == null) {
            VersionData minimum = VersionData.getEarliestVersion(NoVendorOption.class);
            VersionData maximum = VersionData.getLatestVersion(NoVendorOption.class);

            defaultVendorOptionVersion = new VendorOptionVersion(NoVendorOption.class, minimum,
                    maximum);
        }

        return defaultVendorOptionVersion;
    }

    /**
     * Checks if vendor option version is allowed.
     *
     * @param versionList the version list
     * @param vendorOptionVersion the vendor option version
     * @return true, if is allowed
     */
    public boolean isAllowed(List<VersionData> versionList,
            VendorOptionVersion vendorOptionVersion) {
        if ((versionList != null) && (vendorOptionVersion != null)) {
            for (VersionData versionData : versionList) {
                if (vendorOptionVersion.isAllowed(versionData)) {
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
    public VersionData getDefaultVendorOptionVersionData() {
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

        if (vendorOptionVersion != null) {
            VendorOptionTypeInterface vendorOption = vendorOptionClassMap
                    .get(vendorOptionVersion.getClassType());
            if (vendorOption != null) {
                title.append("(");
                title.append(vendorOption.getName());
                title.append(" ");
                VersionData earliest = vendorOptionVersion.getEarliest();
                VersionData latest = vendorOptionVersion.getLatest();
                title.append(earliest.toString());

                if (earliest.toString().compareTo(latest.toString()) != 0) {
                    title.append("-");
                    title.append(latest.toString());
                }
                title.append(")");
            }
        }

        return title.toString();
    }

    /**
     * Gets the latest version data.
     *
     * @return the latest
     */
    public List<VersionData> getLatest() {
        VendorOptionTypeInterface geoServer = getClass(GeoServerVendorOption.class);

        List<VersionData> last = new ArrayList<VersionData>();
        List<VersionData> versionList = geoServer.getVersionList();

        VersionData data = versionList.get(versionList.size() - 1);
        last.add(data);

        return last;
    }

    /**
     * Adds the vendor option listener.
     *
     * @param listener the listener
     */
    public synchronized void addVendorOptionListener(VendorOptionUpdateInterface listener) {
        if (!vendorOptionListenerList.contains(listener)) {
            vendorOptionListenerList.add(listener);
            notifyVendorOptionUpdated();
        }
    }

    /**
     * Sets the selected vendor options.
     *
     * @param selectedVendorOptions the selectedVendorOptions to set
     */
    public void setSelectedVendorOptions(List<VersionData> selectedVendorOptions) {
        if (!this.selectedVendorOptions.equals(selectedVendorOptions)) {
            if (!vendorOptionOverridden) {
                this.selectedVendorOptions = selectedVendorOptions;

                notifyVendorOptionUpdated();
            }
        }
    }

    /**
     * Notify vendor option updated.
     */
    private synchronized void notifyVendorOptionUpdated() {
        for (VendorOptionUpdateInterface listener : vendorOptionListenerList) {
            listener.vendorOptionsUpdated(this.selectedVendorOptions);
        }
    }

    /**
     * Load SLD file.
     *
     * @param uiMgr the ui mgr
     * @param sld the sld
     * @param sldData the sld data
     */
    public void loadSLDFile(GetMinimumVersionInterface uiMgr, StyledLayerDescriptor sld,
            SLDDataInterface sldData) {
        if (sldData != null) {
            List<VersionData> selectedVendorOptionVersion = null;
            String messageString = null;

            if (sldData.getSldEditorFile() == null) {
                MinimumVersion minimumVersion = new MinimumVersion(uiMgr);

                minimumVersion.findMinimumVersion(sld);

                // Find out what the default is
                PrefData prefData = PrefManager.getInstance().getPrefData();

                selectedVendorOptionVersion = minimumVersion
                        .getMinimumVersion(prefData.getVendorOptionVersionList());
                messageString = Localisation.getString(VendorOptionManager.class,
                        "VendorOptionManager.loadedFromFile");
            } else {
                selectedVendorOptionVersion = sldData.getVendorOptionList();
                messageString = Localisation.getString(VendorOptionManager.class,
                        "VendorOptionManager.loadedFromSLDEditorFile");
            }

            if (selectedVendorOptionVersion != null) {
                setSelectedVendorOptions(selectedVendorOptionVersion);

                List<VersionData> listCopy = new ArrayList<>(selectedVendorOptionVersion);
                Collections.sort(listCopy);
                VersionData versionData = listCopy.get(listCopy.size() - 1);

                ConsoleManager.getInstance().information(this, String.format("%s : %s",
                        messageString, VendorOptionStatus.getVersionString(versionData)));
            }
        }
    }

    /**
     * Override selected vendor options.
     *
     * @param vendorOptionList the vendor option list
     */
    public void overrideSelectedVendorOptions(List<VersionData> vendorOptionList) {
        vendorOptionOverridden = false;
        setSelectedVendorOptions(vendorOptionList);
        vendorOptionOverridden = true;
    }
}
