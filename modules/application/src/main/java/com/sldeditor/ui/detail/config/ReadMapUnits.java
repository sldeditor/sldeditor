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
import java.util.List;
import java.util.Map;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.MapUnits;
import com.sldeditor.common.xml.ui.XMLFieldConfigData;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnum;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue.FieldList;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueField;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueItem;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueList;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.defaults.ConfigDefaultFactory;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;

/**
 * The Class ReadMapUnits reads a XML configuration of map unit field configuration
 * structures and instantiates and populates the relevant objects.
 * <p>
 * Configuration files exist at src/main/resources/ui/*
 * 
 * @author Robert Ward (SCISYS)
 */
public class ReadMapUnits implements PanelConfigInterface {

    /** The Constant SCHEMA_RESOURCE. */
    private static final String SCHEMA_RESOURCE = "/xsd/mapunits.xsd";

    /** The panel title. */
    private String panelTitle;


    /**
     * Default constructor
     */
    public ReadMapUnits()
    {
        // Force it so that standard fields are always loaded
        Localisation.preload(ReadPanelConfig.class);
    }

    /**
     * Read configuration file and store the configuration in the object.
     *
     * @param panelId the panel id
     * @param resourceString the resource string
     * @param fieldConfigMapUnits the field config map units
     * @return true, if successful
     */
    public boolean read(Class<?> panelId, String resourceString, FieldConfigMapUnits fieldConfigMapUnits)
    {
        MapUnits mapUnits = (MapUnits) ParseXML.parseUIFile(resourceString, SCHEMA_RESOURCE, MapUnits.class);

        if(mapUnits == null)
        {
            return false;
        }

        Class<?> localisationClass = ReadPanelConfig.class;
        if(mapUnits.getLocalisation() != null)
        {
            try {
                localisationClass = Class.forName(mapUnits.getLocalisation());
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(ReadMapUnits.class, e);
            }
        }

        XMLFieldConfigData xmlFieldConfig = mapUnits.getFieldConfigEnum();
        if(xmlFieldConfig instanceof XMLFieldConfigEnum)
        {
            String defaultValue = xmlFieldConfig.getDefault();

            XMLFieldConfigEnumValueList valueList = ((XMLFieldConfigEnum)xmlFieldConfig).getValueList();

            List<SymbolTypeConfig> configList = readValueListConfig(localisationClass, panelId, valueList);

            fieldConfigMapUnits.addConfig(configList);

            String defaultValueObj = ConfigDefaultFactory.getString(defaultValue);

            if(defaultValueObj != null)
            {
                fieldConfigMapUnits.setDefaultValue(defaultValueObj);
            }
        }

        return true;
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
                return Localisation.getString(ReadMapUnits.class, text.substring(1));
            }
            return Localisation.getString(localisationClass, text);
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
        return null;
    }

    /**
     * Gets the group list read from the configuration.
     *
     * @return the group list
     */
    @Override
    public List<GroupConfigInterface> getGroupList()
    {
        return null;
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
        return null;
    }
}
