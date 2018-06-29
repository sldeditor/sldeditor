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
import org.geotools.styling.Stroke;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.FieldEnableState;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename;
import com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF;
import com.sldeditor.ui.detail.vendor.geoserver.marker.VendorOptionMarkerSymbolFactory;
import com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2;
import com.sldeditor.ui.iface.MultiOptionSelectedInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * A factory for creating FieldConfig objects that appear in
 * {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType}.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SymbolTypeFactory {

    /** The list of symbol type fields. */
    private List<FieldState> symbolTypeFieldList = new ArrayList<FieldState>();

    /** The symbol marker field. */
    private FieldConfigMarker markerField = null;

    /** The external image field. */
    private FieldConfigFilename externalImageField = null;

    /** The ttf field. */
    private FieldConfigTTF ttfField = null;

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
    public SymbolTypeFactory(Class<?> panelId, ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig, FieldIdEnum symbolSelectionField) {
        this.selectionComboBox = symbolSelectionField;

        markerField = new FieldConfigMarker(
                new FieldConfigCommonData(panelId, FieldIdEnum.FILL_COLOUR, "", false),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        externalImageField = new FieldConfigFilename(
                new FieldConfigCommonData(panelId, FieldIdEnum.EXTERNAL_GRAPHIC, "", true),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        ttfField = new FieldConfigTTF(
                new FieldConfigCommonData(panelId, FieldIdEnum.TTF_SYMBOL, "", true),
                fillFieldConfig, strokeFieldConfig, symbolSelectionField);

        symbolTypeFieldList.add(markerField);
        symbolTypeFieldList.add(externalImageField);
        symbolTypeFieldList.add(ttfField);

        VendorOptionMarkerSymbolFactory vendorOptionMarkerSymbolFactory = new VendorOptionMarkerSymbolFactory();
        List<FieldState> voFieldStateList = vendorOptionMarkerSymbolFactory
                .getVendorOptionMarkerSymbols(panelId, fillFieldConfig, strokeFieldConfig,
                        symbolSelectionField);
        symbolTypeFieldList.addAll(voFieldStateList);

        for (FieldState fieldConfig : symbolTypeFieldList) {
            ((FieldConfigBase) fieldConfig).createUI();
        }

        SOLID_FILL_VALUE = FieldConfigMarker.getSolidFillValue();
        NO_FILL_VALUE = FieldConfigMarker.getNoFillValue();
    }

    /**
     * Populate polygon fill details.
     *
     * @param textPanel the text panel
     * @param fieldConfigManager the field config manager
     */
    public void populate(VOGeoServerTextSymbolizer2 textPanel,
            GraphicPanelFieldManager fieldConfigManager) {

        internal_populate(textPanel, textPanel, textPanel, this.selectionComboBox,
                fieldConfigManager);
    }

    /**
     * Populate polygon fill details.
     *
     * @param fillPanel the graphic panel
     * @param fieldConfigManager the field config manager
     */
    public void populate(PolygonFillDetails fillPanel,
            GraphicPanelFieldManager fieldConfigManager) {

        internal_populate(fillPanel, fillPanel, fillPanel, this.selectionComboBox,
                fieldConfigManager);
    }

    /**
     * Populate point fill details.
     *
     * @param fillPanel the graphic panel
     * @param fieldConfigManager the field config manager
     */
    public void populate(PointFillDetails fillPanel, GraphicPanelFieldManager fieldConfigManager) {

        internal_populate(fillPanel, fillPanel, fillPanel, this.selectionComboBox,
                fieldConfigManager);
    }

    /**
     * Populate stroke details.
     *
     * @param strokePanel the stroke panel
     * @param fieldConfigManager the field config manager
     */
    public void populate(StrokeDetails strokePanel, GraphicPanelFieldManager fieldConfigManager) {

        internal_populate(strokePanel, strokePanel, strokePanel, FieldIdEnum.STROKE_STYLE,
                fieldConfigManager);
    }

    /**
     * Internal populate.
     *
     * @param basePanel the base panel
     * @param multiOptionSelected the multi option selected
     * @param updateSymbol the update symbol
     * @param selectionField the selection field
     * @param fieldConfigManager the field config manager
     */
    private void internal_populate(BasePanel basePanel,
            MultiOptionSelectedInterface multiOptionSelected, UpdateSymbolInterface updateSymbol,
            FieldIdEnum selectionField, GraphicPanelFieldManager fieldConfigManager) {
        List<ValueComboBoxDataGroup> combinedSymbolList = new ArrayList<ValueComboBoxDataGroup>();

        /**
         * Populate symbol type list. Given a panel details class iterate over all the field panels
         * asking them to populate the symbol type list.
         */
        for (FieldState panel : symbolTypeFieldList) {
            panel.populateSymbolList(basePanel.getClass(), combinedSymbolList);
        }

        FieldConfigBase field = fieldConfigManager.get(selectionField);
        this.symbolTypeField = (FieldConfigSymbolType) field;
        if (this.symbolTypeField != null) {
            symbolTypeField.populate(multiOptionSelected, combinedSymbolList);

            for (FieldState panel : symbolTypeFieldList) {
                panel.setUpdateSymbolListener(updateSymbol);

                classMap.put(panel.getClass(), panel);

                this.symbolTypeField.addField(panel);

                basePanel.updateFieldConfig(panel.getBasePanel());

                // Transfer all the fields in the child panels into this panel
                Map<FieldIdEnum, FieldConfigBase> map = panel.getFieldList(fieldConfigManager);
                if (map != null) {
                    for (FieldIdEnum panelField : map.keySet()) {
                        fieldConfigManager.add(panelField, map.get(panelField));
                    }
                }
            }
        }
    }

    /**
     * Sets the symbol value.
     *
     * @param symbolizerType the symbolizer type
     * @param fieldConfigManager the field config manager
     * @param graphic the graphic
     * @param symbol the new value
     */
    public void setValue(Class<?> symbolizerType, GraphicPanelFieldManager fieldConfigManager,
            Graphic graphic, GraphicalSymbol symbol) {

        for (FieldState panel : classMap.values()) {
            if (panel != null) {
                if (panel.accept(symbol)) {
                    panel.setValue(symbolizerType, fieldConfigManager, symbolTypeField, graphic,
                            symbol);
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
            Expression symbolType, boolean fillEnabled, boolean strokeEnabled,
            Class<?> selectedPanelId) {
        FieldState panel = classMap.get(selectedPanelId);
        if (panel != null) {
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
    public void setSolidFill(GraphicPanelFieldManager fieldConfigManager, Expression expFillColour,
            Expression expFillColourOpacity) {
        if (symbolTypeField != null) {
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

        if (obj != null) {
            if (!isNone(obj.getKey())) {
                FieldState panel = classMap.get(obj.getPanelId());

                if (panel != null) {
                    fill = panel.getFill(graphicFill, fieldConfigManager);
                }
            }
        }
        return fill;
    }

    /**
     * Gets the field overrides.
     *
     * @param panelDetails the panel details the configuration is for
     * @return the field overrides
     */
    public FieldEnableState getFieldOverrides(Class<?> panelDetails) {
        FieldEnableState fieldEnableState = new FieldEnableState();

        for (FieldState panel : symbolTypeFieldList) {
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
        if (symbolTypeField != null) {
            symbolTypeField.populateField(NO_FILL_VALUE);
        }
    }

    /**
     * Checks if selected item is the 'no fill value'.
     *
     * @param selectedItem the selected item
     * @return true, if is no fill
     */
    public boolean isNone(String selectedItem) {
        return (selectedItem.compareTo(NO_FILL_VALUE) == 0);
    }

    /**
     * Sets the value.
     *
     * @param selectedPanelId the selected panel id
     * @param value the value
     */
    public void setTestValue(Class<?> selectedPanelId, String value) {
        FieldState panel = classMap.get(selectedPanelId);
        if (panel != null) {
            // panel.setTestValue(value);
        }
    }

    /**
     * Checks if symbol uses overall opacity.
     *
     * @param symbolizerType the symbolizer type
     * @param selectedPanelId the selected panel id
     * @return true, if is overall opacity
     */
    public boolean isOverallOpacity(Class<?> symbolizerType, Class<?> selectedPanelId) {
        FieldState panel = classMap.get(selectedPanelId);
        if (panel != null) {
            return panel.isOverallOpacity(symbolizerType);
        }

        return true;
    }

    /**
     * Gets the minimum version for the SLD symbol.
     *
     * @param parentObj the parent obj
     * @param sldObj the sld obj
     * @param vendorOptionsPresentList the vendor options present list
     */
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        GraphicalSymbol symbol = null;

        Graphic graphic = null;

        if (sldObj instanceof Graphic) {
            graphic = (Graphic) sldObj;
        } else if (sldObj instanceof Fill) {
            Fill fill = (Fill) sldObj;
            graphic = fill.getGraphicFill();
        } else if (sldObj instanceof Stroke) {
            Stroke stroke = (Stroke) sldObj;
            graphic = stroke.getGraphicStroke();
        }

        if (graphic != null) {
            List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();

            if ((graphicalSymbolList != null) && !graphicalSymbolList.isEmpty()) {
                symbol = graphicalSymbolList.get(0);
            }
        }

        if (symbol != null) {
            for (FieldState panel : symbolTypeFieldList) {
                if (panel.accept(symbol)) {
                    panel.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
                }
            }
        }
    }
}
