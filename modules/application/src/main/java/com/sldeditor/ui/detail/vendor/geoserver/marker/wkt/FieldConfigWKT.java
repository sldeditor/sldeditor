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
package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.FillImpl;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.widgets.FieldPanel;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class FieldConfigWKT wraps a text field GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * A button when clicked on displays a dialog ({@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog})
 * that allows the user to configure a WKT string.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigWKT extends FieldState implements WKTUpdateInterface {

    /** The Constant VALIDITY_KEY. */
    private static final String VALIDITY_KEY = "WKT";

    /** The Constant WKT_PREFIX. */
    private static final String WKT_PREFIX = WKTConversion.WKT_PREFIX;

    /** The Constant WKT_SYMBOL_KEY. */
    private static final String WKT_SYMBOL_KEY = "wkt";

    /** The wkt panel. */
    private WKTDetails wktPanel = null;

    /**
     * The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the
     * field enable/disable field states for the different symbol types
     */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symboltype/SymbolTypeFieldState_WKT.xml";

    /**
     * Instantiates a new field config string.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     */
    public FieldConfigWKT(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
        super(panelId, id, label, valueOnly, SYMBOLTYPE_FIELD_STATE_RESOURCE);
    }

    /**
     * Creates the ui.
     *
     * @param parentBox the parent box
     */
    @Override
    public void createUI(Box parentBox) {
        FieldPanel fieldPanel = createFieldPanel(0, "", parentBox);
        fieldPanel.setLayout(new BorderLayout());
        wktPanel = new WKTDetails(this, FunctionManager.getInstance());

        fieldPanel.add(wktPanel, BorderLayout.CENTER);

        Dimension panelSize = wktPanel.getPanelSize();
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
        if(wktPanel != null)
        {
            wktPanel.setEnabled(enabled);
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
        if(wktPanel != null)
        {
            return wktPanel.getExpression();
        }

        return null;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled()
    {
        if(wktPanel != null)
        {
            return wktPanel.isEnabled();
        }

        return false;
    }

    /**
     * Revert to default value.
     */
    @Override
    public void revertToDefaultValue()
    {
        if(wktPanel != null)
        {
            wktPanel.revertToDefaultValue();
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
        if(wktPanel != null)
        {
            if(objValue instanceof String)
            {
                wktPanel.populateExpression((String) objValue);
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
        if((symbol != null) && (fieldConfigManager != null))
        {
            if(symbol instanceof Mark)
            {
                MarkImpl markerSymbol = (MarkImpl) symbol;

                FillImpl fill = markerSymbol.getFill();

                if(fill != null)
                {
                    Expression expFillColour = fill.getColor();
                    Expression expFillColourOpacity = fill.getOpacity();

                    FieldConfigBase field = fieldConfigManager.get(FieldIdEnum.FILL_COLOUR);
                    if(field != null)
                    {
                        field.populate(expFillColour);
                    }
                    field = fieldConfigManager.get(FieldIdEnum.OPACITY);
                    if(field != null)
                    {
                        field.populate(expFillColourOpacity);
                    }
                }

                if(wktPanel != null)
                {
                    wktPanel.populateExpression(markerSymbol.getWellKnownName().toString());
                }

                if(multiOptionPanel != null)
                {
                    multiOptionPanel.setSelectedItem(WKT_SYMBOL_KEY);
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

        if(fieldConfigManager != null)
        {
            Expression wellKnownName = null;
            if(getConfigField() != null)
            {
                wellKnownName = getConfigField().getExpression();
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
     * Gets the field map.
     *
     * @param fieldConfigManager the field config manager
     * @return the field map
     */
    @Override
    public Map<FieldId, FieldConfigBase> getFieldList(GraphicPanelFieldManager fieldConfigManager)
    {
        Map<FieldId, FieldConfigBase> map = new HashMap<FieldId, FieldConfigBase>();

        map.put(new FieldId(FieldIdEnum.WKT), this);

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

                        if(valueString.startsWith(WKT_PREFIX))
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
        if(wktPanel != null)
        {
            if(wktPanel.getExpression() != null)
            {
                return wktPanel.getExpression().toString();
            }
        }

        return null;
    }

    /**
     * Wkt value updated.
     */
    @Override
    public void wktValueUpdated() {
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
    public void setTestValue(FieldId fieldId, String testValue) {
        if(wktPanel != null)
        {
            wktPanel.populateExpression(testValue);
        }
    }

    /**
     * Method called when the field has been selected from a combo box
     * and may need to be initialised
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
        SelectedSymbol.getInstance().setValidSymbol(VALIDITY_KEY, valid);
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigWKT copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigWKT(fieldConfigBase.getPanelId(),
                    fieldConfigBase.getFieldId(),
                    fieldConfigBase.getLabel(),
                    fieldConfigBase.isValueOnly());
        }
        return copy;
    }

    /**
     * Gets the class type.
     *
     * @return the class type
     */
    @Override
    public Class<?> getClassType() {
        return Geometry.class;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(wktPanel != null)
        {
            wktPanel.setVisible(visible);
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
