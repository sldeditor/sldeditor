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
package com.sldeditor.ui.detail.vendor.geoserver.marker.arrow;

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
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
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
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigArrow wraps a text field GUI component and an optional value/attribute/expression drop down,
 * ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Allows the user to configure a extshape:arrow string.
 * <p>
 * Supports undo/redo functionality.
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigArrow extends FieldState implements ArrowUpdateInterface {

    /** The Constant ARROW_SYMBOL_KEY. */
    private static final String ARROW_SYMBOL_KEY = "Arrow";

    /** The arrow panel. */
    private ArrowDetails arrowPanel = null;

    /** The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the field enable/disable field states for the different symbol types. */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symbol/marker/arrow/SymbolTypeFieldState_Arrow.xml";

    /** The vendor option info. */
    private VendorOptionInfo vendorOptionInfo = null;

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the symbol selection field
     */
    public FieldConfigArrow(FieldConfigCommonData commonData, ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig, FieldIdEnum symbolSelectionField) {
        super(commonData, SYMBOLTYPE_FIELD_STATE_RESOURCE, fillFieldConfig, strokeFieldConfig,
                symbolSelectionField);

    }

    /**
     * Creates the ui.
     */
    @Override
    public void createUI() {
        FieldPanel fieldPanel = createFieldPanel(0, "");
        fieldPanel.setLayout(new BorderLayout());
        arrowPanel = new ArrowDetails(this, FunctionManager.getInstance());

        fieldPanel.add(arrowPanel, BorderLayout.CENTER);

        Dimension panelSize = arrowPanel.getPanelSize();
        fieldPanel.setPreferredSize(panelSize);
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    @Override
    public void attributeSelection(String field) {
        // Not used
    }

    /**
     * Sets the field enabled state.
     *
     * @param enabled the new enabled state
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (arrowPanel != null) {
            arrowPanel.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    @Override
    protected Expression generateExpression() {
        if (arrowPanel != null) {
            return arrowPanel.getExpression();
        }

        return null;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled() {
        if (arrowPanel != null) {
            return arrowPanel.isEnabled();
        }

        return false;
    }

    /**
     * Revert to default value.
     */
    @Override
    public void revertToDefaultValue() {
        if (arrowPanel != null) {
            arrowPanel.revertToDefaultValue();
        }
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue) {
        if (arrowPanel != null) {
            if (objValue instanceof String) {
                arrowPanel.populateExpression((String) objValue);
            }
        }
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    @Override
    public VendorOptionVersion getVendorOption() {
        if (arrowPanel != null) {
            return arrowPanel.getVendorOptionVersion();
        }
        return VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
    }

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    @Override
    public Class<?> getSymbolClass() {
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
    public void setValue(Class<?> symbolizerType, GraphicPanelFieldManager fieldConfigManager,
            FieldConfigSymbolType multiOptionPanel, Graphic graphic, GraphicalSymbol symbol) {
        if ((symbol != null) && (fieldConfigManager != null)) {
            if (symbol instanceof Mark) {
                MarkImpl markerSymbol = (MarkImpl) symbol;

                FillImpl fill = markerSymbol.getFill();

                if (fill != null) {
                    Expression expFillColour = fill.getColor();
                    Expression expFillColourOpacity = fill.getOpacity();

                    FieldConfigBase field = fieldConfigManager.get(FieldIdEnum.FILL_COLOUR);
                    if (field != null) {
                        field.populate(expFillColour);
                    }
                    field = fieldConfigManager.get(FieldIdEnum.OVERALL_OPACITY);
                    if (field != null) {
                        field.populate(expFillColourOpacity);
                    }
                }

                if (arrowPanel != null) {
                    arrowPanel.populateExpression(markerSymbol.getWellKnownName().toString());
                }

                if (multiOptionPanel != null) {
                    multiOptionPanel.setSelectedItem(ARROW_SYMBOL_KEY);
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
            Expression symbolType, boolean fillEnabled, boolean strokeEnabled) {
        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();

        if (fieldConfigManager != null) {
            Expression wellKnownName = null;
            if (getConfigField() != null) {
                wellKnownName = getConfigField().getExpression();
                if (wellKnownName != null) {
                    // Stroke colour
                    FieldConfigBase field = null;
                    Stroke stroke = null;

                    if (strokeEnabled && (strokeFieldConfig != null)) {
                        Expression expStrokeColour = null;
                        Expression expStrokeColourOpacity = null;

                        field = fieldConfigManager.get(strokeFieldConfig.getColour());
                        if (field != null) {
                            if (field instanceof FieldConfigColour) {
                                FieldConfigColour colourField = (FieldConfigColour) field;

                                expStrokeColour = colourField.getColourExpression();
                            }
                        }
                        field = fieldConfigManager.get(strokeFieldConfig.getOpacity());
                        if (field != null) {
                            expStrokeColourOpacity = field.getExpression();
                        }
                        stroke = getStyleFactory().createStroke(expStrokeColour,
                                expStrokeColourOpacity);
                    }

                    // Fill colour
                    Fill fill = null;

                    if (fillEnabled && (fillFieldConfig != null)) {
                        Expression expFillColour = null;
                        Expression expFillColourOpacity = null;

                        field = fieldConfigManager.get(fillFieldConfig.getColour());
                        if (field != null) {
                            if (field instanceof FieldConfigColour) {
                                FieldConfigColour colourField = (FieldConfigColour) field;

                                expFillColour = colourField.getColourExpression();
                            }
                        }
                        field = fieldConfigManager.get(fillFieldConfig.getOpacity());
                        if (field != null) {
                            expFillColourOpacity = field.getExpression();
                        }
                        fill = getStyleFactory().createFill(expFillColour, expFillColourOpacity);
                    }

                    field = fieldConfigManager.get(FieldIdEnum.STROKE_WIDTH);
                    if (field != null) {
                        Expression strokeWidth = field.getExpression();
                        stroke.setWidth(strokeWidth);
                    }

                    Expression symbolSize = null;
                    field = fieldConfigManager.get(FieldIdEnum.STROKE_SYMBOL_SIZE);
                    if (field != null) {
                        symbolSize = field.getExpression();
                    }
                    Expression rotation = null;
                    Mark mark = getStyleFactory().createMark(wellKnownName, stroke, fill,
                            symbolSize, rotation);

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
    public Fill getFill(GraphicFill graphicFill, GraphicPanelFieldManager fieldConfigManager) {
        return null;
    }

    /**
     * Gets the base panel.
     *
     * @return the base panel
     */
    @Override
    public BasePanel getBasePanel() {
        return null;
    }

    /**
     * Gets the field map.
     *
     * @param fieldConfigManager the field config manager
     * @return the field map
     */
    @Override
    public Map<FieldIdEnum, FieldConfigBase> getFieldList(
            GraphicPanelFieldManager fieldConfigManager) {
        Map<FieldIdEnum, FieldConfigBase> map = new HashMap<FieldIdEnum, FieldConfigBase>();

        map.put(FieldIdEnum.VO_ARROW, this);
        map.put(FieldIdEnum.VO_ARROW_HEAD, this);
        map.put(FieldIdEnum.VO_ARROW_HEIGHT_OVER_WIDTH, this);
        map.put(FieldIdEnum.VO_ARROW_THICKNESS, this);

        return map;
    }

    /**
     * Accept.
     *
     * @param symbol the symbol
     * @return true, if successful
     */
    @Override
    public boolean accept(GraphicalSymbol symbol) {
        if (symbol != null) {
            if (symbol instanceof MarkImpl) {
                MarkImpl marker = (MarkImpl) symbol;

                Expression expression = marker.getWellKnownName();

                if (expression instanceof LiteralExpressionImpl) {
                    LiteralExpressionImpl lExpression = (LiteralExpressionImpl) expression;

                    Object value = lExpression.getValue();
                    if (value instanceof String) {
                        String valueString = (String) value;

                        if (valueString.startsWith(ArrowUtils.getArrowPrefix())) {
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
    public FieldConfigBase getConfigField() {
        return this;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        if (arrowPanel != null) {
            if (arrowPanel.getExpression() != null) {
                return arrowPanel.getExpression().toString();
            }
        }

        return null;
    }

    /**
     * Arrow value updated.
     */
    @Override
    public void arrowValueUpdated() {
        setCachedExpression(generateExpression());

        checkSymbolIsValid();

        FieldConfigBase parent = getParent();
        if (parent != null) {
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
        if (arrowPanel != null) {
            arrowPanel.populateExpression(testValue);
        }
    }

    /**
     * Method called when the field has been selected from a combo box and may need to be initialised.
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
        if (expression != null) {
            valid = !expression.toString().isEmpty();
        }
        SelectedSymbol.getInstance().setValidSymbolMarker(valid);
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigArrow copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigArrow(fieldConfigBase.getCommonData(), fillFieldConfig,
                    strokeFieldConfig, symbolSelectionField);
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
        if (arrowPanel != null) {
            arrowPanel.setVisible(visible);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.symboltype.SymbolTypeInterface#populateVendorOptionFieldMap(java.util.Map)
     */
    @Override
    protected void populateVendorOptionFieldMap(
            Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        // No vendor options
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#isOverallOpacity(java.lang.Class)
     */
    @Override
    public boolean isOverallOpacity(Class<?> symbolizerType) {
        return true;
    }

    /**
     * Gets the version data.
     *
     * @return the version data
     */
    public VendorOptionVersion getVendorOptionVersion() {
        if (arrowPanel != null) {
            return arrowPanel.getVendorOptionVersion();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        VendorOptionPresent voPresent = new VendorOptionPresent(sldObj,
                getVendorOptionInfo());

        vendorOptionsPresentList.add(voPresent);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        if(vendorOptionInfo == null)
        {
            vendorOptionInfo = new VendorOptionInfo("extshape://arrow",
                    getVendorOptionVersion(),
                    Localisation.getString(VOGeoServerArrowSymbol.class, "VOGeoServerArrowSymbol.description"));
        }

        return vendorOptionInfo;
    }
}
