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

package com.sldeditor.ui.detail.config.panelconfig;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.PanelConfig;
import com.sldeditor.common.xml.ui.XMLFieldConfigData;
import com.sldeditor.common.xml.ui.XMLFieldConfigVendorOption;
import com.sldeditor.common.xml.ui.XMLGroupConfig;
import com.sldeditor.common.xml.ui.XMLMultiOptionGroup;
import com.sldeditor.common.xml.ui.XMLOptionGroup;
import com.sldeditor.common.xml.ui.XMLVendorOption;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigVendorOption;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class ReadPanelConfig reads a XML configuration of field configuration structures and
 * instantiates and populates the relevant objects.
 *
 * <p>Configuration files exist at src/main/resources/ui/*
 *
 * @author Robert Ward (SCISYS)
 */
public class ReadPanelConfig implements PanelConfigInterface {

    /** The Constant SCHEMA_RESOURCE. */
    private static final String SCHEMA_RESOURCE = "/xsd/paneldetails.xsd";

    /** The vendor option version. */
    private VendorOptionVersion vendorOptionVersion = null;

    /** The group list. */
    private List<GroupConfigInterface> groupList = null;

    /** The panel title. */
    private String panelTitle;

    /** The vendor option factory. */
    private VendorOptionFactoryInterface vendorOptionFactory = null;

    /** The is raster symbol flag. */
    private boolean isRasterSymbol = false;

    /** The fields. */
    private InstantiateFields fields = new InstantiateFields();

    /**
     * Default constructor.
     *
     * @param vendorOptionFactory the vendor option factory
     * @param isRasterSymbol the is raster symbol
     */
    public ReadPanelConfig(
            VendorOptionFactoryInterface vendorOptionFactory, boolean isRasterSymbol) {
        this.isRasterSymbol = isRasterSymbol;
        this.vendorOptionFactory = vendorOptionFactory;

        // Force it so that standard fields are always loaded
        Localisation.preload(ReadPanelConfig.class);
    }

    /**
     * Read configuration file and store the configuration in the object.
     *
     * @param panelId the panel id
     * @param resourceString the resource string
     * @return true, if successful
     */
    public boolean read(Class<?> panelId, String resourceString) {
        groupList = new ArrayList<>();

        PanelConfig panelConfig =
                (PanelConfig)
                        ParseXML.parseUIFile(resourceString, SCHEMA_RESOURCE, PanelConfig.class);

        if (panelConfig == null) {
            return false;
        }

        Class<?> localisationClass = ReadPanelConfig.class;
        if (panelConfig.getLocalisation() != null) {
            try {
                localisationClass = Class.forName(panelConfig.getLocalisation());
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(ReadPanelConfig.class, e);
            }
        }

        panelTitle =
                InstantiateFields.getLocalisedText(localisationClass, panelConfig.getPanelTitle());
        vendorOptionVersion = getVendorOptionVersion(panelConfig);

        for (Object groupObj : panelConfig.getGroupOrMultiOptionGroup()) {
            if (groupObj instanceof XMLGroupConfig) {
                GroupConfig groupConfig =
                        parseGroup(localisationClass, panelId, (XMLGroupConfig) groupObj);

                groupList.add(groupConfig);
            } else if (groupObj instanceof XMLMultiOptionGroup) {
                MultiOptionGroup groupConfig =
                        parseMultiOptionGroup(
                                localisationClass, panelId, (XMLMultiOptionGroup) groupObj);

                groupList.add(groupConfig);
            }
        }

        return true;
    }

    /**
     * Parses the multi option group.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param xmlMultiGroupObj the xml multi group obj
     * @return the multi option group
     */
    private MultiOptionGroup parseMultiOptionGroup(
            Class<?> localisationClass, Class<?> panelId, XMLMultiOptionGroup xmlMultiGroupObj) {
        MultiOptionGroup multiOptionGroupConfig = new MultiOptionGroup();

        multiOptionGroupConfig.setId(xmlMultiGroupObj.getId());
        multiOptionGroupConfig.setLabel(
                InstantiateFields.getLocalisedText(localisationClass, xmlMultiGroupObj.getLabel()));
        multiOptionGroupConfig.setShowLabel(xmlMultiGroupObj.getShowLabel());
        multiOptionGroupConfig.setOptional(xmlMultiGroupObj.getOption());

        List<XMLOptionGroup> optionGroupList = xmlMultiGroupObj.getOptionGroup();

        for (XMLOptionGroup xmlOptionGroup : optionGroupList) {
            OptionGroup optionGroup = new OptionGroup();

            optionGroup.setId(xmlOptionGroup.getId());
            optionGroup.setLabel(
                    InstantiateFields.getLocalisedText(
                            localisationClass, xmlOptionGroup.getLabel()));
            optionGroup.setShowLabel(xmlOptionGroup.getShowLabel());

            List<XMLGroupConfig> xmlGroupConfigList = xmlOptionGroup.getGroup();
            if (xmlGroupConfigList != null) {
                for (XMLGroupConfig xmlGroupConfig : xmlGroupConfigList) {
                    GroupConfig groupConfig =
                            parseGroup(localisationClass, panelId, xmlGroupConfig);

                    optionGroup.addGroup(groupConfig);
                }
            }

            multiOptionGroupConfig.addGroup(optionGroup);
        }
        return multiOptionGroupConfig;
    }

    /**
     * Gets the vendor option version.
     *
     * @param panelConfig the panel config
     * @return the vendor option version
     */
    private VendorOptionVersion getVendorOptionVersion(PanelConfig panelConfig) {
        String vendorOptionClassName = panelConfig.getVendorOption();
        String startVersion = panelConfig.getStart();
        String endVersion = panelConfig.getEnd();

        VendorOptionVersion localVendorOptionVersion = null;

        if (vendorOptionClassName != null) {
            Class<?> classType;
            try {
                classType = Class.forName(vendorOptionClassName);
                localVendorOptionVersion =
                        VendorOptionManager.getInstance()
                                .getVendorOptionVersion(classType, startVersion, endVersion);
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance()
                        .error(
                                ReadPanelConfig.class,
                                "Unknown vendor option class : " + vendorOptionClassName);
                localVendorOptionVersion =
                        VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
            }
        }

        return localVendorOptionVersion;
    }

    /**
     * Gets the vendor option version read from the configuration.
     *
     * @return the vendor option version
     */
    @Override
    public VendorOptionVersion getVendorOptionVersion() {
        return vendorOptionVersion;
    }

    /**
     * Parses the group if fields.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param xmlGroupObj the xml group object
     * @return the group config
     */
    private GroupConfig parseGroup(
            Class<?> localisationClass, Class<?> panelId, XMLGroupConfig xmlGroupObj) {
        GroupConfig groupConfig = new GroupConfig();

        groupConfig.setId(xmlGroupObj.getId());
        groupConfig.setLabel(
                groupTitle(
                        InstantiateFields.getLocalisedText(
                                localisationClass, xmlGroupObj.getLabel())));
        groupConfig.setShowLabel(xmlGroupObj.getShowLabel());
        groupConfig.setOptional(xmlGroupObj.getOption());

        for (Object obj : xmlGroupObj.getFieldList()) {
            if (obj instanceof XMLFieldConfigData) {
                addField(localisationClass, panelId, groupConfig, (XMLFieldConfigData) obj);
            } else if (obj instanceof XMLGroupConfig) {
                GroupConfig subGroup = parseGroup(localisationClass, panelId, (XMLGroupConfig) obj);

                groupConfig.addGroup(subGroup);
            } else if (obj instanceof XMLMultiOptionGroup) {
                MultiOptionGroup subGroup =
                        parseMultiOptionGroup(
                                localisationClass, panelId, (XMLMultiOptionGroup) obj);

                groupConfig.addGroup(subGroup);
            } else if (obj instanceof XMLFieldConfigVendorOption) {
                XMLFieldConfigVendorOption vendorOption = (XMLFieldConfigVendorOption) obj;
                FieldIdEnum id = vendorOption.getId();
                String label = null;
                boolean valueOnly = true;

                FieldConfigCommonData commonData =
                        new FieldConfigCommonData(panelId, id, label, valueOnly, false);

                List<VendorOptionInterface> veList = null;

                veList = vendorOptionFactory.getVendorOptionList(vendorOption.getClazz());
                if ((veList == null) || veList.isEmpty()) {
                    ConsoleManager.getInstance()
                            .error(
                                    this,
                                    Localisation.getField(
                                                    FieldConfigBase.class,
                                                    "FieldConfigVendorOption.missingVendorOptionClass")
                                            + vendorOption.getClazz());
                }

                FieldConfigVendorOption placeHolder =
                        new FieldConfigVendorOption(commonData, veList);

                groupConfig.addField(placeHolder);
            }
        }

        return groupConfig;
    }

    /**
     * Create Group title.
     *
     * @param groupTitle the group title
     * @return the string
     */
    private String groupTitle(String groupTitle) {
        if (vendorOptionVersion == null) {
            return groupTitle;
        } else {
            return groupTitle
                    + " "
                    + VendorOptionManager.getInstance().getTitle(vendorOptionVersion);
        }
    }

    /**
     * Adds the field.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param groupConfig the group config
     * @param xmlFieldConfig the xml field config
     */
    private void addField(
            Class<?> localisationClass,
            Class<?> panelId,
            GroupConfig groupConfig,
            XMLFieldConfigData xmlFieldConfig) {

        FieldIdEnum id = xmlFieldConfig.getId();
        String label =
                InstantiateFields.getLocalisedText(localisationClass, xmlFieldConfig.getLabel());
        boolean valueOnly = xmlFieldConfig.getValueOnly();
        boolean multipleValues = xmlFieldConfig.getMultipleValues();
        boolean suppress = xmlFieldConfig.getSuppressUpdateOnSet();

        FieldConfigCommonData commonData =
                new FieldConfigCommonData(
                        panelId, id, label, valueOnly, isRasterSymbol, multipleValues, suppress);

        FieldData fieldData = new FieldData(localisationClass, panelId, xmlFieldConfig, commonData);

        fields.create(groupConfig, fieldData);
    }

    /**
     * Decode version data.
     *
     * @param xmlVendorOption the xml vendor option
     * @return the version data
     */
    public static VersionData decodeVersionData(XMLVendorOption xmlVendorOption) {
        VersionData versionData =
                VendorOptionManager.getInstance().getDefaultVendorOptionVersionData();

        if (xmlVendorOption != null) {
            try {
                String vendorOptionClassName = xmlVendorOption.getClassType().trim();
                Class<?> vendorOptionClass = Class.forName(vendorOptionClassName);
                versionData = VersionData.decode(vendorOptionClass, xmlVendorOption.getVersion());
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(ReadPanelConfig.class, e);
            }
        }
        return versionData;
    }

    /**
     * Gets the group list read from the configuration.
     *
     * @return the group list
     */
    @Override
    public List<GroupConfigInterface> getGroupList() {
        return groupList;
    }

    /**
     * Gets the panel title.
     *
     * @return the panelTitle
     */
    @Override
    public String getPanelTitle() {
        return panelTitle;
    }

    /**
     * Gets the default field map.
     *
     * @return the defaultFieldMap
     */
    @Override
    public Map<FieldIdEnum, Object> getDefaultFieldMap() {
        return fields.getDefaultMap();
    }
}
