/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.XMLFieldConfigBoolean;
import com.sldeditor.common.xml.ui.XMLFieldConfigColour;
import com.sldeditor.common.xml.ui.XMLFieldConfigColourMap;
import com.sldeditor.common.xml.ui.XMLFieldConfigDSProperties;
import com.sldeditor.common.xml.ui.XMLFieldConfigData;
import com.sldeditor.common.xml.ui.XMLFieldConfigDouble;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnum;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueList;
import com.sldeditor.common.xml.ui.XMLFieldConfigFeatureTypeConstraint;
import com.sldeditor.common.xml.ui.XMLFieldConfigFont;
import com.sldeditor.common.xml.ui.XMLFieldConfigFontPreview;
import com.sldeditor.common.xml.ui.XMLFieldConfigGeometry;
import com.sldeditor.common.xml.ui.XMLFieldConfigGeometryField;
import com.sldeditor.common.xml.ui.XMLFieldConfigInlineFeature;
import com.sldeditor.common.xml.ui.XMLFieldConfigInteger;
import com.sldeditor.common.xml.ui.XMLFieldConfigMapUnit;
import com.sldeditor.common.xml.ui.XMLFieldConfigSlider;
import com.sldeditor.common.xml.ui.XMLFieldConfigSortBy;
import com.sldeditor.common.xml.ui.XMLFieldConfigString;
import com.sldeditor.common.xml.ui.XMLFieldConfigSymbolType;
import com.sldeditor.common.xml.ui.XMLFieldConfigTransformation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigDSProperties;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigGeometryField;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigMapUnits;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.defaults.ConfigDefaultFactory;
import com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap;
import com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint;
import com.sldeditor.ui.detail.config.font.FieldConfigFont;
import com.sldeditor.ui.detail.config.font.FieldConfigFontPreview;
import com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature;
import com.sldeditor.ui.detail.config.sortby.FieldConfigSortBy;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.config.transform.FieldConfigTransformation;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Creates fields from the parsed XML configuration.
 *
 * @author Robert Ward (SCISYS)
 */
public class InstantiateFields {

    /** The map of default field value. */
    private Map<FieldIdEnum, Object> defaultFieldMap = new EnumMap<>(FieldIdEnum.class);

    /**
     * Creates the.
     *
     * @param groupConfig the group config
     * @param fieldData the field data
     */
    public void create(GroupConfig groupConfig, FieldData fieldData) {
        XMLFieldConfigData xmlFieldConfig = fieldData.getXmlFieldConfig();
        FieldConfigBase fieldConfig = null;

        if (xmlFieldConfig instanceof XMLFieldConfigString) {
            fieldConfig = addFieldString(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigColourMap) {
            fieldConfig = addFieldColourMap(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigFeatureTypeConstraint) {
            fieldConfig = addFieldFeatureTypeConstraint(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigSortBy) {
            fieldConfig = addFieldSortBy(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigDSProperties) {
            fieldConfig = addFieldDSProperties(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigGeometryField) {
            fieldConfig = addFieldGeometryField(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigFont) {
            fieldConfig = addFieldFont(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigInlineFeature) {
            fieldConfig = addFieldInLineFeature(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigFontPreview) {
            fieldConfig = addFieldFontPreview(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigTransformation) {
            fieldConfig = addFieldTransformation(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigGeometry) {
            fieldConfig = addFieldGeometry(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigBoolean) {
            fieldConfig = addFieldBoolean(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigDouble) {
            fieldConfig = addFieldDouble(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigInteger) {
            fieldConfig = addFieldInteger(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigColour) {
            fieldConfig = addFieldColour(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigSlider) {
            fieldConfig = addFieldSlider(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigSymbolType) {
            fieldConfig = addFieldSymbolType(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigEnum) {
            fieldConfig = addFieldEnum(fieldData);
        } else if (xmlFieldConfig instanceof XMLFieldConfigMapUnit) {
            fieldConfig = addFieldMapUnits(fieldData);
        }

        if (fieldConfig != null) {
            groupConfig.addField(fieldConfig);
        }
    }

    /**
     * Adds the field map units.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldMapUnits(FieldData fieldData) {
        FieldConfigMapUnits valueConfig = new FieldConfigMapUnits(fieldData.getCommonData());

        String defaultValueObj = ConfigDefaultFactory.getString(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            valueConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return valueConfig;
    }

    /**
     * Adds the field enum.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldEnum(FieldData fieldData) {
        FieldConfigEnum valueConfig = new FieldConfigEnum(fieldData.getCommonData());

        XMLFieldConfigEnumValueList valueList =
                ((XMLFieldConfigEnum) fieldData.getXmlFieldConfig()).getValueList();

        List<SymbolTypeConfig> configList =
                readValueListConfig(
                        fieldData.getLocalisationClass(), fieldData.getPanelId(), valueList);

        valueConfig.addConfig(configList);

        String defaultValueObj = ConfigDefaultFactory.getString(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            valueConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return valueConfig;
    }

    /**
     * Adds the field symbol type.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldSymbolType(FieldData fieldData) {
        return new FieldConfigSymbolType(fieldData.getCommonData());
    }

    /**
     * Adds the field slider.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldSlider(FieldData fieldData) {
        XMLFieldConfigSlider xmlSlider = (XMLFieldConfigSlider) fieldData.getXmlFieldConfig();

        FieldConfigSlider sliderConfig = new FieldConfigSlider(fieldData.getCommonData());
        sliderConfig.setDefaultValue(xmlSlider.getDefaultValue());

        Double defaultValueObj = ConfigDefaultFactory.getDouble(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            sliderConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return sliderConfig;
    }

    /**
     * Adds the field colour.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldColour(FieldData fieldData) {
        return new FieldConfigColour(fieldData.getCommonData());
    }

    /**
     * Adds the field integer.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldInteger(FieldData fieldData) {
        XMLFieldConfigInteger xmlInteger = (XMLFieldConfigInteger) fieldData.getXmlFieldConfig();

        FieldConfigInteger integerConfig = new FieldConfigInteger(fieldData.getCommonData());
        integerConfig.setDefaultValue(xmlInteger.getDefaultValue());
        integerConfig.setConfig(
                xmlInteger.getMinValue(), xmlInteger.getMaxValue(), xmlInteger.getStepSize());

        Integer defaultValueObj = ConfigDefaultFactory.getInteger(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            integerConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return integerConfig;
    }

    /**
     * Adds the field double.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldDouble(FieldData fieldData) {
        FieldConfigDouble doubleConfig = new FieldConfigDouble(fieldData.getCommonData());

        XMLFieldConfigDouble xmlDouble = (XMLFieldConfigDouble) fieldData.getXmlFieldConfig();
        doubleConfig.setDefaultValue(xmlDouble.getDefaultValue());
        doubleConfig.setConfig(
                (xmlDouble.getMinValue() == null)
                        ? Double.NEGATIVE_INFINITY
                        : xmlDouble.getMinValue().doubleValue(),
                (xmlDouble.getMaxValue() == null)
                        ? Double.POSITIVE_INFINITY
                        : xmlDouble.getMaxValue().doubleValue(),
                xmlDouble.getStepSize(),
                xmlDouble.getNoOfDecimalPlaces());

        Double defaultValueObj = ConfigDefaultFactory.getDouble(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            doubleConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return doubleConfig;
    }

    /**
     * Adds the field boolean.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldBoolean(FieldData fieldData) {
        FieldConfigBoolean boolConfig = new FieldConfigBoolean(fieldData.getCommonData());

        Boolean defaultValueObj = ConfigDefaultFactory.getBoolean(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            boolConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return boolConfig;
    }

    /**
     * Adds the field geometry.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldGeometry(FieldData fieldData) {
        XMLFieldConfigGeometry xmlGeometryFieldConfig =
                (XMLFieldConfigGeometry) fieldData.getXmlFieldConfig();

        FieldConfigGeometry geometryConfig =
                new FieldConfigGeometry(
                        fieldData.getCommonData(),
                        getLocalisedText(
                                fieldData.getLocalisationClass(),
                                xmlGeometryFieldConfig.getButtonText()));

        String defaultValueObj = ConfigDefaultFactory.getString(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            geometryConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return geometryConfig;
    }

    /**
     * Adds the field transformation.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldTransformation(FieldData fieldData) {
        XMLFieldConfigTransformation xmlTransformationFieldConfig =
                (XMLFieldConfigTransformation) fieldData.getXmlFieldConfig();

        return new FieldConfigTransformation(
                fieldData.getCommonData(),
                getLocalisedText(
                        fieldData.getLocalisationClass(),
                        xmlTransformationFieldConfig.getEditButtonText()),
                getLocalisedText(
                        fieldData.getLocalisationClass(),
                        xmlTransformationFieldConfig.getClearButtonText()));
    }

    /**
     * Adds the field font preview.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldFontPreview(FieldData fieldData) {
        return new FieldConfigFontPreview(fieldData.getCommonData());
    }

    /**
     * Adds the field in line feature.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldInLineFeature(FieldData fieldData) {
        return new FieldConfigInlineFeature(fieldData.getCommonData());
    }

    /**
     * Adds the field font.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldFont(FieldData fieldData) {
        return new FieldConfigFont(fieldData.getCommonData());
    }

    /**
     * Adds the field geometry field.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldGeometryField(FieldData fieldData) {
        FieldConfigGeometryField geometryFieldConfig =
                new FieldConfigGeometryField(fieldData.getCommonData());

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if (dataSource != null) {
            dataSource.addListener(geometryFieldConfig);
        }

        return geometryFieldConfig;
    }

    /**
     * Adds the field DS properties.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldDSProperties(FieldData fieldData) {
        return new FieldConfigDSProperties(fieldData.getCommonData());
    }

    /**
     * Adds the field sort by.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldSortBy(FieldData fieldData) {
        return new FieldConfigSortBy(fieldData.getCommonData());
    }

    /**
     * Adds the field feature type constraint.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldFeatureTypeConstraint(FieldData fieldData) {
        return new FieldConfigFeatureTypeConstraint(fieldData.getCommonData());
    }

    /**
     * Adds the field colour map.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldColourMap(FieldData fieldData) {
        fieldData.getCommonData().setValueOnly(true);
        return new FieldConfigColourMap(fieldData.getCommonData());
    }

    /**
     * Adds the field string.
     *
     * @param fieldData the field data
     * @return the field config base
     */
    private FieldConfigBase addFieldString(FieldData fieldData) {
        XMLFieldConfigString xmlStringFieldConfig =
                (XMLFieldConfigString) fieldData.getXmlFieldConfig();

        FieldConfigString stringConfig =
                new FieldConfigString(
                        fieldData.getCommonData(),
                        getLocalisedText(
                                fieldData.getLocalisationClass(),
                                xmlStringFieldConfig.getButtonText()));

        String defaultValueObj = ConfigDefaultFactory.getString(fieldData.getDefaultValue());

        if (defaultValueObj != null) {
            stringConfig.setDefaultValue(defaultValueObj);
            defaultFieldMap.put(fieldData.getId(), defaultValueObj);
        }

        return stringConfig;
    }

    /**
     * Read value list configuration.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param valueList the xml value obj
     * @return the list
     */
    private List<SymbolTypeConfig> readValueListConfig(
            Class<?> localisationClass, Class<?> panelId, XMLFieldConfigEnumValueList valueList) {
        List<SymbolTypeConfig> configList = new ArrayList<>();

        for (XMLFieldConfigEnumValue valueObj : valueList.getValue()) {
            SymbolTypeConfig config =
                    SymbolTypeConfigParse.parseSymbolTypeConfig(
                            localisationClass, panelId, valueObj);
            configList.add(config);
        }

        return configList;
    }

    /**
     * Gets the localised text.
     *
     * @param localisationClass the localisation class
     * @param text the text
     * @return the localised text
     */
    public static String getLocalisedText(Class<?> localisationClass, String text) {
        if (text == null) {
            return null;
        } else {
            if (text.startsWith("*")) {
                return Localisation.getString(ReadPanelConfig.class, text.substring(1));
            }
            return Localisation.getString(localisationClass, text);
        }
    }

    /**
     * Gets the default map.
     *
     * @return the default map
     */
    public Map<FieldIdEnum, Object> getDefaultMap() {
        return defaultFieldMap;
    }
}
