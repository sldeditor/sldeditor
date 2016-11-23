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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.FieldEnableState;
import com.sldeditor.ui.detail.FillDetails;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename;
import com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF;
import com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * A factory for creating FieldConfig objects that appear in {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType}.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SymbolTypeFactory {

    /** The list of symbol type fields. */
    private List<FieldState> symbolTypeFieldList = new ArrayList<FieldState>();

    /**  The symbol marker field. */
    private FieldConfigMarker markerField = null;

    /** The external image field. */
    private FieldConfigFilename externalImageField = null;

    /** The ttf field. */
    private FieldConfigTTF ttfField = null;

    /** The wind barbs field. */
    private FieldConfigWindBarbs windBarbs = null;

    /** The WKT shape field. */
    private FieldConfigWKT wktShape = null;

    /** The class map. */
    private Map<Class<?>, FieldState> classMap = new HashMap<Class<?>, FieldState>();

    /** The symbol type field. */
    private FieldConfigSymbolType symbolTypeField = null;

    /** The solid fill value. */
    private String SOLID_FILL_VALUE = "";

    /** The no fill value. */
    private String NO_FILL_VALUE = "";

    /** The filter factory. */
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The selection combo box. */
    private FieldIdEnum selectionComboBox = null;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the selection combo box
     */
    public SymbolTypeFactory(Class<?> panelId,
            ColourFieldConfig fillFieldConfig, 
            ColourFieldConfig strokeFieldConfig, 
            FieldIdEnum symbolSelectionField)
    {
        this.selectionComboBox = symbolSelectionField;

        markerField = new FieldConfigMarker(new FieldConfigCommonData(panelId, FieldIdEnum.FILL_COLOUR, "", false),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        externalImageField = new FieldConfigFilename(new FieldConfigCommonData(panelId, FieldIdEnum.EXTERNAL_GRAPHIC, "", true),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        ttfField = new FieldConfigTTF(new FieldConfigCommonData(panelId, FieldIdEnum.TTF_SYMBOL, "", true),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        windBarbs = new FieldConfigWindBarbs(new FieldConfigCommonData(panelId, FieldIdEnum.WINDBARBS, "", true),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        wktShape = new FieldConfigWKT(new FieldConfigCommonData(panelId, FieldIdEnum.WKT, "", true),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);

        symbolTypeFieldList.add(markerField);
        symbolTypeFieldList.add(externalImageField);
        symbolTypeFieldList.add(ttfField);
        symbolTypeFieldList.add(windBarbs);
        symbolTypeFieldList.add(wktShape);

        // Create the ui for the fields
        for(FieldState fieldConfig : symbolTypeFieldList)
        {
            ((FieldConfigBase)fieldConfig).createUI();
        }

        SOLID_FILL_VALUE = FieldConfigMarker.getSolidFillValue();
        NO_FILL_VALUE = FieldConfigMarker.getNoFillValue();
    }

    /**
     * Populate fill details.
     *
     * @param fillPanel the graphic panel
     * @param panelDetails the panel details the configuration is for
     * @param fieldConfigManager the field config manager
     */
    public void populate(FillDetails fillPanel,
            Class<?> panelDetails,
            GraphicPanelFieldManager fieldConfigManager) {

        List<ValueComboBoxDataGroup> combinedSymbolList = populateSymbolList(panelDetails);

        FieldConfigBase field = fieldConfigManager.get(this.selectionComboBox);
        this.symbolTypeField = (FieldConfigSymbolType)field;
        symbolTypeField.populate(fillPanel, combinedSymbolList);

        for(FieldState panel : symbolTypeFieldList)
        {
            panel.setUpdateSymbolListener(fillPanel);

            classMap.put(panel.getClass(), panel);

            this.symbolTypeField.addField(panel);

            fillPanel.updateFieldConfig(panel.getBasePanel());

            Map<FieldIdEnum, FieldConfigBase> map = panel.getFieldList(fieldConfigManager);
            if(map != null)
            {
                for(FieldIdEnum panelField : map.keySet())
                {
                    fieldConfigManager.add(panelField, map.get(panelField));
                }
            }
        }
    }

    /**
     * Populate stroke details.
     *
     * @param strokePanel the stroke panel
     * @param fieldConfigManager the field config manager
     */
    public void populate(StrokeDetails strokePanel, GraphicPanelFieldManager fieldConfigManager) {

        List<ValueComboBoxDataGroup> combinedSymbolList = populateSymbolList(strokePanel.getClass());

        FieldConfigBase fieldConfig = fieldConfigManager.get(FieldIdEnum.STROKE_STYLE);
        this.symbolTypeField = (FieldConfigSymbolType)fieldConfig;
        symbolTypeField.populate(strokePanel, combinedSymbolList);

        for(FieldState panel : symbolTypeFieldList)
        {
            panel.setUpdateSymbolListener(strokePanel);

            classMap.put(panel.getClass(), panel);

            this.symbolTypeField.addField(panel);

            strokePanel.updateFieldConfig(panel.getBasePanel());

            Map<FieldIdEnum, FieldConfigBase> map = panel.getFieldList(fieldConfigManager);
            if(map != null)
            {
                for(FieldIdEnum fieldId : map.keySet())
                {
                    fieldConfigManager.add(fieldId, map.get(fieldId));
                }
            }
        }
    }
    /**
     * Populate symbol type list. Given a panel details class iterate over all the field panels
     * asking them to populate the symbol type list.
     * <p>
     * The returned the list contains all possible symbol types. 
     *
     * @param panelDetails the panel details the configuration is for
     * @return the list of symbol types
     */
    private List<ValueComboBoxDataGroup> populateSymbolList(Class<?> panelDetails)
    {
        List<ValueComboBoxDataGroup> combinedSymbolList = new ArrayList<ValueComboBoxDataGroup>();

        for(FieldState panel : symbolTypeFieldList)
        {
            panel.populateSymbolList(panelDetails, combinedSymbolList);
        }

        return combinedSymbolList;
    }

    /**
     * Sets the symbol value.
     *
     * @param fieldConfigManager the field config manager
     * @param graphic the graphic
     * @param symbol the new value
     */
    public void setValue(GraphicPanelFieldManager fieldConfigManager, Graphic graphic, GraphicalSymbol symbol) {

        for(FieldState panel : classMap.values())
        {
            if(panel != null)
            {
                if (panel.accept(symbol))
                {
                    panel.setValue(fieldConfigManager, symbolTypeField, graphic, symbol);
                }
            }
        }
    }

    /**
     * Gets the symbol value.
     *
     * @param fieldConfigManager the field config manager
     * @param symbolType the symbol type
     * @param fillEnabled the fill enabled
     * @param strokeEnabled the stroke enabled
     * @param selectedPanelId the selected panel id
     * @return the value
     */
    public List<GraphicalSymbol> getValue(GraphicPanelFieldManager fieldConfigManager,
            Expression symbolType, 
            boolean fillEnabled,
            boolean strokeEnabled,
            Class<?> selectedPanelId) {
        FieldState panel = classMap.get(selectedPanelId);
        if(panel != null)
        {
            return panel.getValue(fieldConfigManager, symbolType, fillEnabled, strokeEnabled);
        }

        return null;
    }

    /**
     * Sets the solid fill.
     *
     * @param fieldConfigManager the field config manager
     * @param expFillColour the exp fill colour
     * @param expFillColourOpacity the exp fill colour opacity
     */
    public void setSolidFill(GraphicPanelFieldManager fieldConfigManager, Expression expFillColour, Expression expFillColourOpacity) {
        if(symbolTypeField != null)
        { 
            FieldConfigBase field = fieldConfigManager.get(this.selectionComboBox);
            Literal expression = ff.literal(SOLID_FILL_VALUE);
            field.populate(expression);

            markerField.setSolidFill(fieldConfigManager, expFillColour, expFillColourOpacity);
        }
    }

    /**
     * Gets the fill for the selected value.
     *
     * @param graphicFill the graphic fill
     * @param fieldConfigManager the field config manager
     * @return the fill
     */
    public Fill getFill(GraphicFill graphicFill, GraphicPanelFieldManager fieldConfigManager) {

        Fill fill = null;
        ValueComboBoxData obj = symbolTypeField.getSelectedValueObj();

        if(obj != null)
        {
            if(!isNone(obj.getKey()))
            {
                FieldState panel = classMap.get(obj.getPanelId());

                if(panel != null)
                {
                    fill = panel.getFill(graphicFill, fieldConfigManager);
                }
            }
        }
        return fill;
    }

    /**
     * Gets the fill colour.
     *
     * @return the fill colour
     */
    public Expression getFillColour() {
        if(markerField != null)
        {
            return markerField.getColourExpression();
        }

        return null;
    }

    /**
     * Gets the fill colour opacity.
     *
     * @return the fill colour opacity
     */
    public Expression getFillColourOpacity() {
        if(markerField != null)
        {
            return markerField.getFillColourOpacity();
        }
        return null;
    }

    /**
     * Gets the field overrides.
     *
     * @param panelDetails the panel details the configuration is for
     * @return the field overrides
     */
    public FieldEnableState getFieldOverrides(Class<?> panelDetails) {
        FieldEnableState fieldEnableState = new FieldEnableState();

        for(FieldState panel : symbolTypeFieldList)
        {
            panel.populateFieldOverrideMap(panelDetails, fieldEnableState);
        }
        return fieldEnableState;
    }

    /**
     * Sets the no fill value.
     *
     * @param fieldConfigManager the field config manager
     */
    public void setNoFill(GraphicPanelFieldManager fieldConfigManager) {
        if(symbolTypeField != null)
        {
            symbolTypeField.populateField(NO_FILL_VALUE);
        }
    }

    /**
     * Checks if selected item is the 'no fill value'.
     *
     * @param selectedItem the selected item
     * @return true, if is no fill
     */
    public boolean isNone(String selectedItem)
    {
        return (selectedItem.compareTo(NO_FILL_VALUE) == 0);
    }

    /**
     * Gets the panel list.
     *
     * @return the panel list
     */
    public List<FieldState> getPanelList()
    {
        return symbolTypeFieldList;
    }

    /**
     * Sets the value.
     *
     * @param selectedPanelId the selected panel id
     * @param value the value
     */
    public void setTestValue(Class<?> selectedPanelId, String value)
    {
        FieldState panel = classMap.get(selectedPanelId);
        if(panel != null)
        {
            //          panel.setTestValue(value);
        }
    }
}
