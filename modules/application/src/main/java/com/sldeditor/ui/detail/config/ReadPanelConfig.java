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
package com.sldeditor.ui.detail.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.PanelConfig;
import com.sldeditor.common.xml.ui.XMLFieldConfigBoolean;
import com.sldeditor.common.xml.ui.XMLFieldConfigColour;
import com.sldeditor.common.xml.ui.XMLFieldConfigColourMap;
import com.sldeditor.common.xml.ui.XMLFieldConfigData;
import com.sldeditor.common.xml.ui.XMLFieldConfigDouble;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnum;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue.FieldList;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueField;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueItem;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueList;
import com.sldeditor.common.xml.ui.XMLFieldConfigFont;
import com.sldeditor.common.xml.ui.XMLFieldConfigFontPreview;
import com.sldeditor.common.xml.ui.XMLFieldConfigGeometry;
import com.sldeditor.common.xml.ui.XMLFieldConfigInteger;
import com.sldeditor.common.xml.ui.XMLFieldConfigSlider;
import com.sldeditor.common.xml.ui.XMLFieldConfigString;
import com.sldeditor.common.xml.ui.XMLFieldConfigSymbolType;
import com.sldeditor.common.xml.ui.XMLFieldConfigTransformation;
import com.sldeditor.common.xml.ui.XMLGroupConfig;
import com.sldeditor.common.xml.ui.XMLMultiOptionGroup;
import com.sldeditor.common.xml.ui.XMLOptionGroup;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.config.base.defaults.ConfigDefaultFactory;
import com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap;
import com.sldeditor.ui.detail.config.font.FieldConfigFont;
import com.sldeditor.ui.detail.config.font.FieldConfigFontPreview;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.config.transform.FieldConfigTransformation;

/**
 * The Class ReadPanelConfig reads a XML configuration of field configuration
 * structures and instantiates and populates the relevant objects.
 * <p>
 * Configuration files exist at src/main/resources/ui/*
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

    /** The map of default field value. */
    private Map<FieldId, Object> defaultFieldMap = new HashMap<FieldId, Object>();

    /** The field type. */
    private Class<?> fieldType = String.class;

    /**
     * Default constructor
     */
    public ReadPanelConfig()
    {
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
    public boolean read(Class<?> panelId, String resourceString)
    {
        groupList = new ArrayList<GroupConfigInterface>();

        PanelConfig panelConfig = (PanelConfig) ParseXML.parseUIFile(resourceString, SCHEMA_RESOURCE, PanelConfig.class);

        if(panelConfig == null)
        {
            return false;
        }

        Class<?> localisationClass = ReadPanelConfig.class;
        if(panelConfig.getLocalisation() != null)
        {
            try {
                localisationClass = Class.forName(panelConfig.getLocalisation());
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(ReadPanelConfig.class, e);
            }
        }

        panelTitle = getLocalisedText(localisationClass, panelConfig.getPanelTitle());
        vendorOptionVersion = getVendorOptionVersion(panelConfig);

        for(Object groupObj : panelConfig.getGroupOrMultiOptionGroup())
        {
            if(groupObj instanceof XMLGroupConfig)
            {
                GroupConfig groupConfig = parseGroup(localisationClass, panelId, (XMLGroupConfig)groupObj);

                groupList.add(groupConfig);
            }
            else if(groupObj instanceof XMLMultiOptionGroup)
            {
                MultiOptionGroup groupConfig = parseMultiOptionGroup(localisationClass, panelId, (XMLMultiOptionGroup)groupObj);

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
    private MultiOptionGroup parseMultiOptionGroup(Class<?> localisationClass, Class<?> panelId, XMLMultiOptionGroup xmlMultiGroupObj) {
        MultiOptionGroup multiOptionGroupConfig = new MultiOptionGroup();

        multiOptionGroupConfig.setId(xmlMultiGroupObj.getId());
        multiOptionGroupConfig.setLabel(getLocalisedText(localisationClass, xmlMultiGroupObj.getLabel()));
        multiOptionGroupConfig.setShowLabel(xmlMultiGroupObj.isShowLabel());
        multiOptionGroupConfig.setOptional(xmlMultiGroupObj.isOption());

        List<XMLOptionGroup> optionGroupList = xmlMultiGroupObj.getOptionGroup();

        for(XMLOptionGroup xmlOptionGroup : optionGroupList)
        {
            OptionGroup optionGroup = new OptionGroup();

            optionGroup.setId(xmlOptionGroup.getId());
            optionGroup.setLabel(getLocalisedText(localisationClass, xmlOptionGroup.getLabel()));
            optionGroup.setShowLabel(xmlOptionGroup.isShowLabel());

            List<XMLGroupConfig> xmlGroupConfigList = xmlOptionGroup.getGroup();
            if(xmlGroupConfigList != null)
            {
                for(XMLGroupConfig xmlGroupConfig : xmlGroupConfigList)
                {
                    GroupConfig groupConfig = parseGroup(localisationClass, panelId, xmlGroupConfig);

                    optionGroup.addGroup(groupConfig);
                }
            }

            multiOptionGroupConfig.addGroup(optionGroup);
        }
        return multiOptionGroupConfig;
    }

    /**
     * Gets the localised text.
     *
     * @param localisationClass the localisation class
     * @param text the text
     * @return the localised text
     */
    private static String getLocalisedText(Class<?> localisationClass, String text) {
        if(text == null)
        {
            return null;
        }
        else
        {
            if(text.startsWith("*"))
            {
                return Localisation.getString(ReadPanelConfig.class, text.substring(1));
            }
            return Localisation.getString(localisationClass, text);
        }
    }

    /**
     * Gets the vendor option version.
     *
     * @param panelConfig the panel config
     * @return the vendor option version
     */
    private VendorOptionVersion getVendorOptionVersion(PanelConfig panelConfig)
    {
        String vendorOptionClassName = panelConfig.getVendorOption();
        String startVersion = panelConfig.getStart();
        String endVersion = panelConfig.getEnd();

        VendorOptionVersion vendorOptionVersion = null;

        if(vendorOptionClassName != null)
        {
            Class<?> classType;
            try
            {
                classType = Class.forName(vendorOptionClassName);
                vendorOptionVersion = VendorOptionManager.getInstance().getVendorOptionVersion(classType, startVersion, endVersion);
            }
            catch (ClassNotFoundException e)
            {
                ConsoleManager.getInstance().error(ReadPanelConfig.class, "Unknown vendor option class : " + vendorOptionClassName);
                vendorOptionVersion = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
            }
        }

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
    private GroupConfig parseGroup(Class<?> localisationClass, Class<?> panelId, XMLGroupConfig xmlGroupObj) {
        GroupConfig groupConfig = new GroupConfig();

        groupConfig.setId(xmlGroupObj.getId());
        groupConfig.setLabel(getLocalisedText(localisationClass, xmlGroupObj.getLabel()));
        groupConfig.setShowLabel(xmlGroupObj.isShowLabel());
        groupConfig.setOptional(xmlGroupObj.isOption());

        for(Object obj : xmlGroupObj.getFieldList())
        {
            if(obj instanceof XMLFieldConfigData)
            {
                addField(localisationClass, panelId, groupConfig, (XMLFieldConfigData)obj);
            }
            else if(obj instanceof XMLGroupConfig)
            {
                GroupConfig subGroup = parseGroup(localisationClass, panelId, (XMLGroupConfig)obj);

                groupConfig.addGroup(subGroup);
            }
        }

        return groupConfig;
    }

    /**
     * Adds the field.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param groupConfig the group config
     * @param xmlFieldConfig the xml field config
     */
    private void addField(Class<?> localisationClass, Class<?> panelId, GroupConfig groupConfig, XMLFieldConfigData xmlFieldConfig) {

        FieldId id = new FieldId(xmlFieldConfig.getId());
        String label = getLocalisedText(localisationClass, xmlFieldConfig.getLabel());
        boolean valueOnly = xmlFieldConfig.isValueOnly();
        String defaultValue = xmlFieldConfig.getDefault();

        if(xmlFieldConfig instanceof XMLFieldConfigString)
        {
            XMLFieldConfigString xmlStringFieldConfig = (XMLFieldConfigString) xmlFieldConfig;

            FieldConfigString stringConfig = new FieldConfigString(panelId, id, label, valueOnly, getLocalisedText(localisationClass, xmlStringFieldConfig.getButtonText()));

            groupConfig.addField(stringConfig);

            String defaultValueObj = ConfigDefaultFactory.getString(defaultValue);

            if(defaultValueObj != null)
            {
                stringConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigColourMap)
        {
            FieldConfigColourMap stringConfig = new FieldConfigColourMap(panelId, id, label);

            groupConfig.addField(stringConfig);
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigFont)
        {
            FieldConfigFont fontConfig = new FieldConfigFont(panelId, id, label, valueOnly);

            groupConfig.addField(fontConfig);
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigFontPreview)
        {
            FieldConfigFontPreview fontPreviewConfig = new FieldConfigFontPreview(panelId, id, label, valueOnly);

            groupConfig.addField(fontPreviewConfig);
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigTransformation)
        {
            XMLFieldConfigTransformation xmlTransformationFieldConfig = (XMLFieldConfigTransformation) xmlFieldConfig;

            FieldConfigTransformation transformationConfig = new FieldConfigTransformation(panelId, id, label, true, 
                    getLocalisedText(localisationClass, xmlTransformationFieldConfig.getEditButtonText()), 
                    getLocalisedText(localisationClass, xmlTransformationFieldConfig.getClearButtonText()));

            groupConfig.addField(transformationConfig);
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigGeometry)
        {
            XMLFieldConfigGeometry xmlGeometryFieldConfig = (XMLFieldConfigGeometry) xmlFieldConfig;

            FieldConfigGeometry geometryConfig = new FieldConfigGeometry(panelId, id, label, valueOnly, getLocalisedText(localisationClass, xmlGeometryFieldConfig.getButtonText()));

            groupConfig.addField(geometryConfig);

            String defaultValueObj = ConfigDefaultFactory.getString(defaultValue);

            if(defaultValueObj != null)
            {
                geometryConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigBoolean)
        {
            FieldConfigBoolean boolConfig = new FieldConfigBoolean(panelId, id, label, valueOnly);

            groupConfig.addField(boolConfig);

            Boolean defaultValueObj = ConfigDefaultFactory.getBoolean(defaultValue);

            if(defaultValueObj != null)
            {
                boolConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigDouble)
        {
            FieldConfigDouble doubleConfig = new FieldConfigDouble(panelId, id, label, valueOnly);

            XMLFieldConfigDouble xmlDouble = (XMLFieldConfigDouble)xmlFieldConfig;
            doubleConfig.setDefaultValue(xmlDouble.getDefaultValue());
            doubleConfig.setConfig(xmlDouble.getMinValue(), xmlDouble.getMaxValue(), xmlDouble.getStepSize(), xmlDouble.getNoOfDecimalPlaces());

            groupConfig.addField(doubleConfig);

            Double defaultValueObj = ConfigDefaultFactory.getDouble(defaultValue);

            if(defaultValueObj != null)
            {
                doubleConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigInteger)
        {
            XMLFieldConfigInteger xmlInteger = (XMLFieldConfigInteger)xmlFieldConfig;

            FieldConfigInteger integerConfig = new FieldConfigInteger(panelId, id, label, valueOnly);
            integerConfig.setDefaultValue(xmlInteger.getDefaultValue());
            integerConfig.setConfig(xmlInteger.getMinValue(), xmlInteger.getMaxValue(), xmlInteger.getStepSize());

            groupConfig.addField(integerConfig);

            Integer defaultValueObj = ConfigDefaultFactory.getInteger(defaultValue);

            if(defaultValueObj != null)
            {
                integerConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigColour)
        {
            FieldConfigColour colourConfig = new FieldConfigColour(panelId, id, label, valueOnly);

            groupConfig.addField(colourConfig);
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigSlider)
        {
            FieldConfigSlider sliderConfig = new FieldConfigSlider(panelId, id, label, valueOnly);

            groupConfig.addField(sliderConfig);

            Double defaultValueObj = ConfigDefaultFactory.getDouble(defaultValue);

            if(defaultValueObj != null)
            {
                sliderConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigSymbolType)
        {
            FieldConfigSymbolType fillSymbolConfig = new FieldConfigSymbolType(panelId, id, label, valueOnly);

            groupConfig.addField(fillSymbolConfig);
        }
        else if(xmlFieldConfig instanceof XMLFieldConfigEnum)
        {
            FieldConfigEnum valueConfig = new FieldConfigEnum(panelId, id, label, valueOnly);

            XMLFieldConfigEnumValueList valueList = ((XMLFieldConfigEnum)xmlFieldConfig).getValueList();

            List<SymbolTypeConfig> configList = readValueListConfig(localisationClass, panelId, valueList);

            valueConfig.addConfig(configList);

            String defaultValueObj = ConfigDefaultFactory.getString(defaultValue);

            if(defaultValueObj != null)
            {
                valueConfig.setDefaultValue(defaultValueObj);
                defaultFieldMap.put(id, defaultValueObj);
            }

            groupConfig.addField(valueConfig);
        }
    }

    /**
     * Read value list configuration.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param valueList the xml value obj
     * @return the list
     */
    private List<SymbolTypeConfig> readValueListConfig(Class<?> localisationClass, Class<?> panelId, XMLFieldConfigEnumValueList valueList)
    {
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();

        for(XMLFieldConfigEnumValue valueObj : valueList.getValue())
        {
            SymbolTypeConfig config = parseSymbolTypeConfig(localisationClass, panelId, valueObj);
            configList.add(config);
        }

        return configList;
    }

    /**
     * Parses the symbol type configuration.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param valueObj the value obj
     * @return the symbol type config
     */
    public static SymbolTypeConfig parseSymbolTypeConfig(Class<?> localisationClass, Class<?> panelId, XMLFieldConfigEnumValue valueObj) {
        SymbolTypeConfig config = new SymbolTypeConfig(panelId);

        String groupName = valueObj.getGroupName();
        boolean isSeparateGroup = valueObj.isSeparateGroup();

        if(groupName != null)
        {
            config.setGroupName(groupName);
        }
        config.setSeparateGroup(isSeparateGroup);

        for(XMLFieldConfigEnumValueItem itemObj : valueObj.getItem())
        {
            config.addOption(itemObj.getId(), getLocalisedText(localisationClass, itemObj.getLabel()));
        }

        FieldList fieldList = valueObj.getFieldList();

        if(fieldList != null)
        {
            for(XMLFieldConfigEnumValueField field : fieldList.getField())
            {
                config.addField(new FieldId(field.getId()), field.isEnabled());
            }
        }
        return config;
    }

    /**
     * Gets the vendor option version read from the configuration.
     *
     * @return the vendor option version
     */
    @Override
    public VendorOptionVersion getVendorOptionVersion()
    {
        return vendorOptionVersion;
    }

    /**
     * Gets the group list read from the configuration.
     *
     * @return the group list
     */
    @Override
    public List<GroupConfigInterface> getGroupList()
    {
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
    public Map<FieldId, Object> getDefaultFieldMap() {
        return defaultFieldMap;
    }

    /**
     * Sets the field type.
     *
     * @param fieldType the fieldType to set
     */
    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }
}
