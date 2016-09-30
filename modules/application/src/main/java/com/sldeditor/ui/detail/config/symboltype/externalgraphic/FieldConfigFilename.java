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
package com.sldeditor.ui.detail.config.symboltype.externalgraphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
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
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigFilename wraps a text field GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * A button when clicked on displays a file dialog, the full path is written to the text field.
 * <p>
 * The field allows external images e.g. png, jpg, svg to be specified.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFilename extends FieldState implements ExternalGraphicUpdateInterface {

    /** The Constant VALIDITY_KEY. */
    private static final String VALIDITY_KEY = "External";

    /** The Constant EXTERNAL_SYMBOL. */
    private static final String EXTERNAL_SYMBOL_KEY = "External";

    /** The external graphic panel. */
    private ExternalGraphicDetails externalGraphicPanel = null;

    /**
     * The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the
     * field enable/disable field states for the different symbol types
     */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symboltype/SymbolTypeFieldState_Filename.xml";

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigFilename(FieldConfigCommonData commonData) {
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
        externalGraphicPanel = new ExternalGraphicDetails(this, FunctionManager.getInstance());

        fieldPanel.add(externalGraphicPanel, BorderLayout.CENTER);

        Dimension panelSize = externalGraphicPanel.getPanelSize();
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
        if(externalGraphicPanel != null)
        {
            externalGraphicPanel.setEnabled(enabled);
        }
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
        Expression expression = null;

        if(externalGraphicPanel != null)
        {
            expression = externalGraphicPanel.getExpression();
        }
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
        if(externalGraphicPanel != null)
        {
            externalGraphicPanel.revertToDefaultValue();
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
        if(externalGraphicPanel != null)
        {
            if(objValue instanceof String)
            {
                externalGraphicPanel.populateExpression((String) objValue);
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
        if(symbol instanceof ExternalGraphicImpl)
        {
            ExternalGraphicImpl markerSymbol = (ExternalGraphicImpl) symbol;

            if(externalGraphicPanel != null)
            {
                externalGraphicPanel.setValue(markerSymbol);
            }

            if(multiOptionPanel != null)
            {
                multiOptionPanel.setSelectedItem(EXTERNAL_SYMBOL_KEY);
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
        List<GraphicalSymbol> symbols = null;
        if(externalGraphicPanel != null)
        {
            ExternalGraphic extGraphic = externalGraphicPanel.getSymbol();

            symbols = SelectedSymbol.getInstance().getSymbolList(extGraphic);
        }
        return symbols;
    }

    /**
     * Gets the fill.
     *
     * @param graphicFill the graphic fill
     * @param fieldConfigManager the field config manager
     * @return the fill
     */
    @Override
    public Fill getFill(GraphicFill graphicFill, GraphicPanelFieldManager fieldConfigManager)
    {
        if(fieldConfigManager == null)
        {
            return null;
        }

        Fill fill = null;
        FieldConfigBase fieldConfig = fieldConfigManager.get(FieldIdEnum.OPACITY);
        if(fieldConfig != null)
        {
            Expression fillColour = null;
            Expression fillColourOpacity = null;

            if(fieldConfig instanceof FieldConfigColour)
            {
                fillColourOpacity = ((FieldConfigColour)fieldConfig).getColourOpacityExpression();
            }
            else
            {
                fillColourOpacity = fieldConfig.getExpression();
            }

            fill = getStyleFactory().fill(graphicFill, fillColour, fillColourOpacity);
        }
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

        map.put(FieldIdEnum.EXTERNAL_GRAPHIC, this);

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
            if(symbol instanceof ExternalGraphicImpl)
            {
                return true;
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
        if(externalGraphicPanel == null)
        {
            return null;
        }

        if(externalGraphicPanel.getExpression() == null)
        {
            return null;
        }
        return externalGraphicPanel.getExpression().toString();
    }

    /**
     * External graphic value updated.
     */
    @Override
    public void externalGraphicValueUpdated() {
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
     * Populate string field.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        if(externalGraphicPanel != null)
        {
            externalGraphicPanel.setValue(value);
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
        FieldConfigFilename copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigFilename(fieldConfigBase.getCommonData());
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
        if(externalGraphicPanel != null)
        {
            externalGraphicPanel.setVisible(visible);
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
