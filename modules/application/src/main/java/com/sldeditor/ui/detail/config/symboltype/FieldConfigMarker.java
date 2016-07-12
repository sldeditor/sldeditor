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

import javax.swing.Box;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.FieldEnableState;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.vendor.geoserver.marker.VendorOptionMarkerSymbolFactory;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The Class FieldConfigMarker handles all the marker symbols, including:
 * <ul>
 * <li>the solid fill</li>
 * <li>no fill</li>
 * <li>vendor option marker symbols</li>
 * </ul>
 * <p>
 * No field is displayed when a marker symbol type is selected.
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigMarker extends FieldConfigBase implements SymbolTypeInterface {

    /** The Constant NONE_SYMBOL_KEY. */
    private static final String NONE_SYMBOL_KEY = "none";

    /** The Constant SOLID_SYMBOL. */
    private static final String SOLID_SYMBOL_KEY = "solid";

    /** The local symbol list. */
    private List<ValueComboBoxData> localSymbolList = null;

    /** The field enable map. */
    private Map<Class<?>, List<SymbolTypeConfig> > fieldEnableMap = null;

    /** The colour field. */
    private FieldId colourField;

    /** The opacity field. */
    private FieldId opacityField;

    /** The symbol selection field. */
    private FieldId symbolSelectionField;

    //
    // Vendor Option for marker symbols
    //

    /** The vendor option marker symbol factory. */
    private VendorOptionMarkerSymbolFactory vendorOptionMarkerSymbolFactory = new VendorOptionMarkerSymbolFactory();

    /**
     * Constructor.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     * @param colourField the colour field
     * @param opacityField the opacity field
     * @param symbolSelectionField the symbol selection field
     */
    public FieldConfigMarker(Class<?> panelId, FieldId id, String label, boolean valueOnly,
            FieldId colourField, 
            FieldId opacityField,
            FieldId symbolSelectionField) {
        super(panelId, id, label, valueOnly);

        this.colourField = colourField;
        this.opacityField = opacityField;
        this.symbolSelectionField = symbolSelectionField;

        createUI(null);
    }

    /**
     * Creates the ui.
     *
     * @param parentBox the parent box
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI(Box parentBox) {
        createFieldPanel(0, "", parentBox);
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
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        // Not used
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    @Override
    protected Expression generateExpression()
    {
        Expression expression = null;

        return expression;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled()
    {
        // Not used
        return false;
    }

    /**
     * Revert to default value.
     */
    @Override
    public void revertToDefaultValue()
    {
        // Not used
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     * @param opacity the opacity
     */
    @Override
    public void populateExpression(Object objValue, Expression opacity)
    {
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
        return MarkImpl.class;
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
            MarkImpl markerSymbol = (MarkImpl) symbol;

            Expression wellKnownNameExpression = markerSymbol.getWellKnownName();

            if(wellKnownNameExpression instanceof LiteralExpressionImpl)
            {
                LiteralExpressionImpl literal = (LiteralExpressionImpl) wellKnownNameExpression;
                FieldConfigBase field = fieldConfigManager.get(symbolSelectionField);
                if(field != null)
                {
                    field.populate(literal);
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
        if((symbolType == null) || (fieldConfigManager == null))
        {
            return null;
        }

        Expression fillColour = null;

        FieldConfigBase field = fieldConfigManager.get(colourField);
        if(field != null)
        {
            fillColour = ((FieldConfigColour)field).getColourExpression();
        }

        Expression fillColourOpacity = null;
        field = fieldConfigManager.get(opacityField);
        if(field != null)
        {
            fillColourOpacity = field.getExpression();
        }

        Fill fill = null;
        if(fillEnabled)
        {
            fill = getStyleFactory().fill(null, fillColour, fillColourOpacity);
        }

        Stroke stroke = null;

        if(strokeEnabled)
        {
            Expression strokeColour = null;
            field = fieldConfigManager.get(FieldIdEnum.STROKE_STROKE_COLOUR);
            if(field != null)
            {
                strokeColour = ((FieldConfigColour)field).getColourExpression();
            }

            Expression strokeColourOpacity = null;
            field = fieldConfigManager.get(FieldIdEnum.STROKE_STROKE_OPACITY);
            if(field != null)
            {
                strokeColourOpacity = field.getExpression();
            }

            Expression strokeWidth = null;
            field = fieldConfigManager.get(FieldIdEnum.STROKE_WIDTH);
            if(field != null)
            {
                strokeWidth = field.getExpression();
            }

            stroke = getStyleFactory().createStroke(strokeColour, strokeWidth, strokeColourOpacity);
        }

        Expression expression = null;

        if(symbolType.toString().compareTo(SOLID_SYMBOL_KEY) != 0)
        {
            expression = symbolType;
        }

        Mark markerSymbol = getStyleFactory().mark(expression, fill, stroke);

        return SelectedSymbol.getInstance().getSymbolList(markerSymbol);
    }

    /**
     * Populate symbol list.
     *
     * @param symbolizerClass the symbolizer class
     * @param symbolList the symbol list
     */
    @Override
    public void populateSymbolList(Class<?> symbolizerClass, List<ValueComboBoxDataGroup> symbolList)
    {
        List<SymbolTypeConfig> configList = getFieldMap().get(symbolizerClass);

        if(configList == null)
        {
            ConsoleManager.getInstance().error(this, "No config for symbolizer class : " + symbolizerClass.getName());
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
        FieldConfigBase field = fieldConfigManager.get(colourField);
        if(field != null)
        {
            if(field instanceof FieldConfigColour)
            {
                fillColour = ((FieldConfigColour)field).getColourExpression();
            }
        }

        Expression fillColourOpacity = null;
        field = fieldConfigManager.get(opacityField);
        if(field != null)
        {
            fillColourOpacity = field.getExpression();
        }

        GraphicFill _graphicFill = null;
        if(graphicFill != null)
        {
            List<GraphicalSymbol> symbolList = graphicFill.graphicalSymbols();

            if((symbolList != null) && (!symbolList.isEmpty()))
            {
                GraphicalSymbol symbol = symbolList.get(0);
                Mark mark = (Mark) symbol;
                if(mark.getWellKnownName() != null)
                {
                    _graphicFill = graphicFill;
                }
            }
        }
        Fill fill = getStyleFactory().fill(_graphicFill, fillColour, fillColourOpacity);

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
     * Sets the solid fill.
     *
     * @param fieldConfigManager the field config manager
     * @param expFillColour the new solid fill
     * @param expFillColourOpacity the exp fill colour opacity
     */
    public void setSolidFill(GraphicPanelFieldManager fieldConfigManager, Expression expFillColour, Expression expFillColourOpacity) {

        if(fieldConfigManager != null)
        {
            FieldConfigBase field = fieldConfigManager.get(colourField);
            if(field != null)
            {
                field.populate(expFillColour, expFillColourOpacity);
            }
        }
    }

    /**
     * Populate field override map.
     *
     * @param symbolizerClass the symbolizer class
     * @param fieldEnableState the field enable state
     */
    @Override
    public void populateFieldOverrideMap(Class<?> symbolizerClass, FieldEnableState fieldEnableState)
    {
        List<SymbolTypeConfig> configList = getFieldMap().get(symbolizerClass);
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
            ConsoleManager.getInstance().error(this, "No config for symbolizer class : " + symbolizerClass.getName());
        }
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

        if(fieldConfigManager != null)
        {
            map.put(colourField, fieldConfigManager.get(colourField));
            map.put(opacityField, fieldConfigManager.get(opacityField));
        }
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

                        if(localSymbolList != null)
                        {
                            for(ValueComboBoxData data : localSymbolList)
                            {
                                if(data.getKey().compareTo(valueString) == 0)
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sets the update symbol listener.
     *
     * @param listener the update symbol listener
     */
    @Override
    public void setUpdateSymbolListener(UpdateSymbolInterface listener)
    {
        addDataChangedListener(listener);
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
     * Gets the field map.
     *
     * @return the field map
     */
    private Map<Class<?>, List<SymbolTypeConfig> > getFieldMap()
    {
        if(fieldEnableMap == null)
        {
            fieldEnableMap = new HashMap<Class<?>, List<SymbolTypeConfig> >();

            String fullResourceName = "FillColourPanel.xml";
            SymbolTypeConfigReader.readConfig(getClass(), fullResourceName, fieldEnableMap);

            vendorOptionMarkerSymbolFactory.getFieldMap(fieldEnableMap);
        }

        return fieldEnableMap;
    }

    /**
     * Gets the colour expression.
     *
     * @return the colour expression
     */
    public Expression getColourExpression() {
        return null;
    }

    /**
     * Gets the fill colour opacity.
     *
     * @return the fill colour opacity
     */
    public Expression getFillColourOpacity() {
        return null;
    }

    /**
     * Gets the solid fill value.
     *
     * @return the solid fill value
     */
    public static String getSolidFillValue() {
        return SOLID_SYMBOL_KEY;
    }

    /**
     * Gets the no fill value.
     *
     * @return the no fill value
     */
    public static String getNoFillValue() {
        return NONE_SYMBOL_KEY;
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
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigMarker copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigMarker(fieldConfigBase.getPanelId(),
                    fieldConfigBase.getFieldId(),
                    fieldConfigBase.getLabel(),
                    fieldConfigBase.isValueOnly(),
                    this.colourField,
                    this.opacityField,
                    this.symbolSelectionField);
        }
        return copy;
    }

    /**
     * Gets the class type supported.
     *
     * @return the class type
     */
    @Override
    public Class<?> getClassType() {
        return String.class;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        // Does nothing, always visible
    }
}
