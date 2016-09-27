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
package com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigWindBarbs is a GeoServer vendor option to display wind barbs, weather symbols.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigWindBarbs extends FieldState implements WindBarbUpdateInterface {

    /** The Constant WINDBARBS_PREFIX. */
    private static final String WINDBARBS_PREFIX = "windbarbs://";

    /** The Constant WINDBARB_SYMBOL_KEY. */
    private static final String WINDBARB_SYMBOL_KEY = "windbarb";

    /** The wind barbs panel. */
    private WindBarbDetails windBarbsPanel = null;

    /**
     * The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the
     * field enable/disable field states for the different symbol types
     */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symboltype/SymbolTypeFieldState_WindBarbs.xml";

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigWindBarbs(FieldConfigCommonData commonData) {
        super(commonData, SYMBOLTYPE_FIELD_STATE_RESOURCE);
    }

    /**
     * Creates the ui.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {

        FieldPanel fieldPanel = createFieldPanel(0, "");
        fieldPanel.setLayout(new BorderLayout());
        windBarbsPanel = new WindBarbDetails(this, FunctionManager.getInstance());

        fieldPanel.add(windBarbsPanel, BorderLayout.CENTER);

        Dimension panelSize = windBarbsPanel.getPanelSize();
        fieldPanel.setPreferredSize(panelSize);
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field)
    {
        // Not used
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        // Do nothin
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression()
    {
        if(windBarbsPanel == null)
        {
            return null;
        }
        Expression expression = windBarbsPanel.getExpression();

        return expression;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * Revert to default value.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue()
    {
        if(windBarbsPanel != null)
        {
            windBarbsPanel.revertToDefaultValue();
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
        if(windBarbsPanel != null)
        {
            if(objValue instanceof String)
            {
                windBarbsPanel.populateExpression((String) objValue);
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
     * @param fieldConfigManager the field config manager
     * @param multiOptionPanel the multi option panel
     * @param symbol the symbol
     */
    @Override
    public void setValue(GraphicPanelFieldManager fieldConfigManager,
            FieldConfigSymbolType multiOptionPanel, GraphicalSymbol symbol)
    {
        if(symbol != null)
        {
            if(symbol instanceof Mark)
            {
                MarkImpl markerSymbol = (MarkImpl) symbol;

                if(getConfigField() != null)
                {
                    getConfigField().populate(markerSymbol.getWellKnownName());
                }

                if(multiOptionPanel != null)
                {
                    multiOptionPanel.setSelectedItem(WINDBARB_SYMBOL_KEY);
                }
            }
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
            wellKnownName = symbolType;
            if(wellKnownName != null)
            {
                Expression expFillColour = null;
                Expression expFillColourOpacity = null;

                FieldConfigBase field = fieldConfigManager.get(FieldIdEnum.FILL_COLOUR);
                if(field != null)
                {
                    if(field instanceof FieldConfigColour)
                    {
                        FieldConfigColour colourField = (FieldConfigColour)field;

                        expFillColour = colourField.getColourExpression();
                        expFillColourOpacity = colourField.getColourOpacityExpression();
                    }
                }

                Stroke stroke = null;
                Fill fill = getStyleFactory().createFill(expFillColour, expFillColourOpacity);
                Expression size = null;
                Expression rotation = null;
                Mark mark = getStyleFactory().createMark(wellKnownName, stroke, fill, size, rotation);

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
        return null;
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
     * Gets the field map
     *
     * @param fieldConfigManager the field config manager
     * @return the field map
     */
    @Override
    public Map<FieldId, FieldConfigBase> getFieldList(GraphicPanelFieldManager fieldConfigManager)
    {
        Map<FieldId, FieldConfigBase> map = new HashMap<FieldId, FieldConfigBase>();

        map.put(new FieldId(FieldIdEnum.WINDBARBS), this);
        map.put(new FieldId(FieldIdEnum.WINDBARB_WINDSPEED), this);
        map.put(new FieldId(FieldIdEnum.WINDBARB_WINDSPEED_UNITS), this);
        map.put(new FieldId(FieldIdEnum.WINDBARB_NORTHERN_HEMISPHERE), this);

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

                        if(valueString.startsWith(WINDBARBS_PREFIX))
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
        return null;
    }

    /**
     * Checks if is a single value is legal
     * To be overridden if necessary.
     *
     * @return true, if is a single value
     */
    public boolean isASingleValue()
    {
        return false;
    }

    /**
     * Wind barb value updated.
     */
    @Override
    public void windBarbValueUpdated() {
        setCachedExpression(generateExpression());

        FieldConfigBase parent = getParent();
        if(parent != null)
        {
            parent.valueUpdated();
        }
    }

    /**
     * Method called when the field has been selected from a combo box
     * and may need to be initialised
     * 
     * Will be be overridden if necessary.
     */
    public void justSelected() {
        setCachedExpression(generateExpression());

        setValueFieldState();
    }

    /**
     * Sets the test value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, String testValue) {
        if(windBarbsPanel != null)
        {
            windBarbsPanel.setTestValue(fieldId, testValue);
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
        FieldConfigWindBarbs copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigWindBarbs(fieldConfigBase.getCommonData());
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
        if(windBarbsPanel != null)
        {
            windBarbsPanel.setVisible(visible);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.SymbolTypeInterface#populateVendorOptionFieldMap(java.util.Map)
     */
    @Override
    protected void populateVendorOptionFieldMap(Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        // No vendor options
        
    }
}
