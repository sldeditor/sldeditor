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
package com.sldeditor.ui.detail.config.symboltype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.FieldEnableState;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The base class FieldState provides functionality to support methods needed for fields that appear
 * in the FieldConfigSymbolType field.
 * 
 * @author Robert Ward (SCISYS)
 */
public abstract class FieldState extends FieldConfigBase {

    /** The resource file. */
    private String resourceFile;

    /** The local symbol list. */
    private List<ValueComboBoxData> localSymbolList = null;

    /** The field enable map. */
    private Map<Class<?>, List<SymbolTypeConfig> > fieldEnableMap = null;

    /**
     * Instantiates a new field state.
     *
     * @param commonData the common data
     * @param resourceFile the resource file
     */
    protected FieldState(FieldConfigCommonData commonData,
            String resourceFile)
    {
        super(commonData);
        this.resourceFile = resourceFile;
    }

    /**
     * Gets the field configuration of the symbol type.
     *
     * @return the field configuration
     */
    public abstract FieldConfigBase getConfigField();

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    public abstract VendorOptionVersion getVendorOption();

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    public abstract Class<?> getSymbolClass();

    /**
     * Sets the value.
     *
     * @param fieldConfigManager the field config manager
     * @param multiOptionPanel the multi option panel
     * @param graphic the graphic
     * @param symbol the new value
     */
    public abstract void setValue(GraphicPanelFieldManager fieldConfigManager, FieldConfigSymbolType multiOptionPanel, Graphic graphic, GraphicalSymbol symbol);

    /**
     * Gets the value.
     *
     * @param fieldConfigManager the field config manager
     * @param symbolType the symbol type
     * @param fillEnabled the fill enabled
     * @param strokeEnabled the stroke enabled
     * @return the value
     */
    public abstract List<GraphicalSymbol> getValue(GraphicPanelFieldManager fieldConfigManager, Expression symbolType, boolean fillEnabled, boolean strokeEnabled);

    /**
     * Populate symbol list.
     *
     * @param panelDetails the panel details the configuration is for
     * @param symbolList the symbol list
     */
    public void populateSymbolList(Class<?> panelDetails, List<ValueComboBoxDataGroup> symbolList)
    {
        List<SymbolTypeConfig> configList = getFieldMap().get(panelDetails);

        if(configList == null)
        {
            ConsoleManager.getInstance().error(this, "No config for panel details class : " + panelDetails.getName());
        }
        else
        {
            if(localSymbolList == null)
            {
                localSymbolList = new ArrayList<ValueComboBoxData>();
            }
            else
            {
                localSymbolList.clear();
            }

            for(SymbolTypeConfig config : configList)
            {
                List<ValueComboBoxData> groupSymbolList = new ArrayList<ValueComboBoxData>();

                for(String key : config.getKeyOrderList())
                {
                    ValueComboBoxData data = new ValueComboBoxData(key, config.getTitle(key), this.getClass());
                    groupSymbolList.add(data);
                }

                symbolList.add(new ValueComboBoxDataGroup(config.getGroupName(), groupSymbolList, config.isSeparateGroup()));

                localSymbolList.addAll(groupSymbolList);
            }
        }
    }

    /**
     * Gets the field map.
     *
     * @return the field map
     */
    protected Map<Class<?>, List<SymbolTypeConfig> > getFieldMap()
    {
        if(fieldEnableMap == null)
        {
            fieldEnableMap = new HashMap<Class<?>, List<SymbolTypeConfig> >();

            SymbolTypeConfigReader.readConfig(getClass(), resourceFile, fieldEnableMap);

            populateVendorOptionFieldMap(fieldEnableMap);
        }

        return fieldEnableMap;
    }

    /**
     * Populate vendor option field map in derived class.
     *
     * @param fieldEnableMap the field enable map
     */
    protected abstract void populateVendorOptionFieldMap(Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap);

    /**
     * Gets the fill.
     *
     * @param graphicFill the graphic fill
     * @param fieldConfigManager the field config manager
     * @return the fill
     */
    public abstract Fill getFill(GraphicFill graphicFill, GraphicPanelFieldManager fieldConfigManager);

    /**
     * Gets the base panel interface.
     *
     * @return the base panel interface
     */
    public abstract BasePanel getBasePanel();

    /**
     * Populate field override map.
     *
     * @param panelDetails the panel details the configuration is for
     * @param fieldEnableState the field enable state
     */
    public void populateFieldOverrideMap(Class<?> panelDetails, FieldEnableState fieldEnableState)
    {
        Map<Class<?>, List<SymbolTypeConfig>> fieldMap = getFieldMap();
        List<SymbolTypeConfig> configList = fieldMap.get(panelDetails);
        if(configList != null)
        {
            for(SymbolTypeConfig config : configList)
            {
                if(config != null)
                {
                    config.updateFieldState(fieldEnableState, getClass().getName());
                }
            }
        }
        else
        {
            ConsoleManager.getInstance().error(this, "No config for panel details class : " + panelDetails.getName());
        }
    }

    /**
     * Gets the field map.
     *
     * @param fieldConfigManager the field config manager
     * @return the field map
     */
    public abstract Map<FieldIdEnum, FieldConfigBase> getFieldList(GraphicPanelFieldManager fieldConfigManager);

    /**
     * Returns true if the class can handle the graphical symbol.
     *
     * @param symbol the symbol
     * @return true, if successful
     */
    public abstract boolean accept(GraphicalSymbol symbol);

    /**
     * Sets the update symbol listener.
     *
     * @param listener the update symbol listener
     */
    public void setUpdateSymbolListener(UpdateSymbolInterface listener)
    {
        addDataChangedListener(listener);
    }

    /**
     * Gets the local symbol list.
     *
     * @return the localSymbolList
     */
    protected List<ValueComboBoxData> getLocalSymbolList() {
        return localSymbolList;
    }
}
