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
package com.sldeditor.ui.detail.config.symboltype.ttf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.FillImpl;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigTTF wraps a text field GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * A button when clicked on displays a dialog ({@link com.sldeditor.ui.ttf.CharMap4}) that allows the user to select a character from a TrueType font.
 * When the character is selected the encoded string is written to the text field.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigTTF extends FieldState implements TTFUpdateInterface {

    /** The Constant TTF_PREFIX. */
    private static final String TTF_PREFIX = "ttf://";

    /** The Constant TTF_SYMBOL_KEY. */
    private static final String TTF_SYMBOL_KEY = "ttf";

    /** The ttf panel. */
    private TTFDetails ttfPanel = null;

    /** The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the field enable/disable field states for the different symbol types. */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symbol/marker/ttf/SymbolTypeFieldState_TTF.xml";

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the symbol selection field
     */
    public FieldConfigTTF(FieldConfigCommonData commonData,
            ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig,
            FieldIdEnum symbolSelectionField) {
        super(commonData, SYMBOLTYPE_FIELD_STATE_RESOURCE, fillFieldConfig, strokeFieldConfig, symbolSelectionField);
    }

    /**
     * Creates the ui.
     */
    @Override
    public void createUI() {

        FieldPanel fieldPanel = createFieldPanel(0, "");
        fieldPanel.setLayout(new BorderLayout());
        ttfPanel = new TTFDetails(this, FunctionManager.getInstance());

        fieldPanel.add(ttfPanel, BorderLayout.CENTER);

        Dimension panelSize = ttfPanel.getPanelSize();
        fieldPanel.setPreferredSize(panelSize);
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    @Override
    public void attributeSelection(String field)
    {
        // Not used
    }

    /**
     * Sets the field enabled state.
     *
     * @param enabled the new enabled state
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        if(ttfPanel != null)
        {
            ttfPanel.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    @Override
    protected Expression generateExpression()
    {
        if(ttfPanel == null)
        {
            return null;
        }
        return ttfPanel.getExpression();
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled()
    {
        if(ttfPanel != null)
        {
            return ttfPanel.isEnabled();
        }

        return false;
    }

    /**
     * Revert to default value.
     */
    @Override
    public void revertToDefaultValue()
    {
        if(ttfPanel != null)
        {
            ttfPanel.revertToDefaultValue();
        }
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue)
    {
        if(ttfPanel != null)
        {
            if(objValue instanceof String)
            {
                ttfPanel.populateExpression((String) objValue);
            }
        }
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    @Override
    public VendorOptionVersion getVendorOption()
    {
        return VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
    }

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    @Override
    public Class<?> getSymbolClass()
    {
        return ExternalGraphicImpl.class;
    }

    /**
     * Sets the value.
     *
     * @param symbolizerType the symbolizer type
     * @param fieldConfigManager the field config manager
     * @param multiOptionPanel the multi option panel
     * @param graphic the graphic
     * @param symbol the symbol
     */
    @Override
    public void setValue(Class<?> symbolizerType, 
            GraphicPanelFieldManager fieldConfigManager,
            FieldConfigSymbolType multiOptionPanel, Graphic graphic, GraphicalSymbol symbol)
    {
        if(symbol == null)
        {
            return;
        }

        if(fieldConfigManager == null)
        {
            return;
        }

        MarkImpl markerSymbol = (MarkImpl) symbol;

        FillImpl fill = markerSymbol.getFill();

        Expression expFillColour = null;
        Expression expFillOpacity = null;

        if(fill != null)
        {
            expFillColour = fill.getColor();
            if(!isOverallOpacity(symbolizerType))
            {
                expFillOpacity = fill.getOpacity();
            }
        }

        FieldConfigBase field = fieldConfigManager.get(fillFieldConfig.getColour());
        if(field != null)
        {
            field.populate(expFillColour);
        }

        // Opacity
        if(isOverallOpacity(symbolizerType))
        {
            FieldConfigBase opacity = fieldConfigManager.get(FieldIdEnum.OVERALL_OPACITY);
            if(opacity != null)
            {
                opacity.populate(graphic.getOpacity());
            }
        }

        field = fieldConfigManager.get(fillFieldConfig.getOpacity());
        if(field != null)
        {
            field.populate(expFillOpacity);
        }

        Class<?> panelId = getCommonData().getPanelId();
        GroupConfigInterface fillGroup = fieldConfigManager.getGroup(panelId, fillFieldConfig.getGroup());

        if(fillGroup != null)
        {
            fillGroup.enable(expFillColour != null);
        }

        if(ttfPanel != null)
        {
            Expression wellKnownNameExpression = markerSymbol.getWellKnownName();
            String wellKnownName = null;
            if(wellKnownNameExpression != null)
            {
                wellKnownName = wellKnownNameExpression.toString();
            }
            ttfPanel.populateExpression(wellKnownName);
        }

        if(multiOptionPanel != null)
        {
            multiOptionPanel.setSelectedItem(TTF_SYMBOL_KEY);
        }
    }

    /**
     * Gets the value.
     *
     * @param fieldConfigManager the field config manager
     * @param symbolType the symbol type
     * @param fillEnabled the fill enabled
     * @param strokeEnabled the stroke enabled
     * @return the value
     */
    @Override
    public List<GraphicalSymbol> getValue(GraphicPanelFieldManager fieldConfigManager,
            Expression symbolType, boolean fillEnabled, boolean strokeEnabled)
    {
        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();

        Expression wellKnownName = null;
        if((getConfigField() != null) && (fieldConfigManager != null))
        {
            wellKnownName = getConfigField().getExpression();
            if(wellKnownName != null)
            {
                Stroke stroke = null;
                Fill fill = null;

                if(fillEnabled)
                {
                    Expression expFillColour = null;
                    Expression expFillColourOpacity = null;

                    FieldConfigBase field = fieldConfigManager.get(fillFieldConfig.getColour());
                    if(field != null)
                    {
                        FieldConfigColour colourField = (FieldConfigColour)field;

                        expFillColour = colourField.getColourExpression();
                    }

                    field = fieldConfigManager.get(fillFieldConfig.getOpacity());
                    if(field != null)
                    {
                        expFillColourOpacity = field.getExpression();
                    }
                    fill = getStyleFactory().createFill(expFillColour, expFillColourOpacity);
                }

                // Size
                Expression expSize = null;

                // Rotation
                Expression expRotation = null;

                Mark mark = getStyleFactory().createMark(wellKnownName, stroke, fill, expSize, expRotation);

                symbolList.add(mark);
            }
        }
        return symbolList;
    }

    /**
     * Gets the fill.
     *
     * @param graphicFill the graphic fill
     * @param fieldConfigManager the field config manager
     * @return the fill
     */
    @Override
    public Fill getFill(GraphicFill graphicFill,
            GraphicPanelFieldManager fieldConfigManager)
    {
        if(fieldConfigManager == null)
        {
            return null;
        }

        Expression fillColour = null;
        Expression fillColourOpacity = null;
        Fill fill = getStyleFactory().fill(graphicFill, fillColour, fillColourOpacity);

        return fill;
    }

    /**
     * Gets the base panel.
     *
     * @return the base panel
     */
    @Override
    public BasePanel getBasePanel()
    {
        return null;
    }

    /**
     * Gets the field map.
     *
     * @param fieldConfigManager the field config manager
     * @return the field map
     */
    @Override
    public Map<FieldIdEnum, FieldConfigBase> getFieldList(GraphicPanelFieldManager fieldConfigManager)
    {
        Map<FieldIdEnum, FieldConfigBase> map = new HashMap<FieldIdEnum, FieldConfigBase>();

        map.put(FieldIdEnum.TTF_SYMBOL, this);

        return map;
    }

    /**
     * Accept.
     *
     * @param symbol the symbol
     * @return true, if successful
     */
    @Override
    public boolean accept(GraphicalSymbol symbol)
    {
        if(symbol != null)
        {
            if(symbol instanceof MarkImpl)
            {
                MarkImpl marker = (MarkImpl) symbol;

                Expression expression = marker.getWellKnownName();

                if(expression instanceof LiteralExpressionImpl)
                {
                    LiteralExpressionImpl lExpression = (LiteralExpressionImpl) expression;

                    Object value = lExpression.getValue();
                    if(value instanceof String)
                    {
                        String valueString = (String) value;

                        if(valueString.startsWith(TTF_PREFIX))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    @Override
    public FieldConfigBase getConfigField()
    {
        return this;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue()
    {
        if(ttfPanel == null)
        {
            return null;
        }

        if(ttfPanel.getExpression() == null)
        {
            return null;
        }
        return ttfPanel.getExpression().toString();
    }

    /**
     * Method called when the field has been selected from a combo box
     * and may need to be initialised.
     */
    @Override
    public void justSelected() {
        setCachedExpression(generateExpression());

        checkSymbolIsValid();
    }

    /**
     * Check symbol is valid.
     */
    public void checkSymbolIsValid() {
        // Mark symbol as valid/invalid
        boolean valid = false;
        Expression expression = getExpression();
        if(expression != null)
        {
            valid = !expression.toString().isEmpty();
        }
        SelectedSymbol.getInstance().setValidSymbolMarker(valid);
    }

    /**
     * TTF field value updated.
     */
    @Override
    public void ttfValueUpdated() {
        setCachedExpression(generateExpression());

        checkSymbolIsValid();

        FieldConfigBase parent = getParent();
        if(parent != null)
        {
            parent.valueUpdated();
        }
    }

    /**
     * Sets the test value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        if(ttfPanel != null)
        {
            ttfPanel.populateExpression(value);
        }
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigTTF copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigTTF(fieldConfigBase.getCommonData(), fillFieldConfig, strokeFieldConfig, symbolSelectionField);
        }
        return copy;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(ttfPanel != null)
        {
            ttfPanel.setVisible(visible);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.SymbolTypeInterface#populateVendorOptionFieldMap(java.util.Map)
     */
    @Override
    protected void populateVendorOptionFieldMap(Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        // No vendor options
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#isOverallOpacity(java.lang.Class)
     */
    @Override
    public boolean isOverallOpacity(Class<?> symbolizerType) {
        return (symbolizerType == LineSymbolizerImpl.class);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        // Strict SLD
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        // Strict SLD
        return null;
    }
}
