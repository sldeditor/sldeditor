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

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.FillImpl;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.geotools.styling.StrokeImpl;
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
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigWKT wraps a text field GUI component and an optional value/attribute/expression drop down,
 * ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * A button when clicked on displays a dialog ({@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog}) that allows the user to
 * configure a WKT string.
 * <p>
 * Supports undo/redo functionality.
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigWKT extends FieldState implements WKTUpdateInterface {

    /** The Constant WKT_PREFIX. */
    private static final String WKT_PREFIX = WKTConversion.WKT_PREFIX;

    /** The Constant WKT_SYMBOL_KEY. */
    private static final String WKT_SYMBOL_KEY = "wkt";

    /** The wkt panel. */
    private WKTDetails wktPanel = null;

    /** The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the field enable/disable field states for the different symbol types. */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symbol/marker/wkt/SymbolTypeFieldState_WKT.xml";

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
    public FieldConfigWKT(FieldConfigCommonData commonData, ColourFieldConfig fillFieldConfig,
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
        if (wktPanel != null) {
            wktPanel.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    @Override
    protected Expression generateExpression() {
        if (wktPanel != null) {
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
    public boolean isEnabled() {
        if (wktPanel != null) {
            return wktPanel.isEnabled();
        }

        return false;
    }

    /**
     * Revert to default value.
     */
    @Override
    public void revertToDefaultValue() {
        if (wktPanel != null) {
            wktPanel.revertToDefaultValue();
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
        if (wktPanel != null) {
            if (objValue instanceof String) {
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
    public VendorOptionVersion getVendorOption() {
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

                // Fill
                FillImpl fill = markerSymbol.getFill();

                if (fill != null) {
                    populateColour(fieldConfigManager, fillFieldConfig, fill.getColor(),
                            fill.getOpacity());
                }
                
                GroupConfigInterface fillGroup = fieldConfigManager.getGroup(fieldConfigManager.getComponentId(), fillFieldConfig.getGroup());
                if(fillGroup != null)
                {
                    fillGroup.enable(fill != null);
                }

                // Stroke
                StrokeImpl stroke = markerSymbol.getStroke();

                if (stroke != null) {
                    populateColour(fieldConfigManager, strokeFieldConfig, stroke.getColor(),
                            stroke.getOpacity());
                }
                GroupConfigInterface strokeGroup = fieldConfigManager.getGroup(fieldConfigManager.getComponentId(), strokeFieldConfig.getGroup());
                if(strokeGroup != null)
                {
                    strokeGroup.enable(stroke != null);
                }

                if (wktPanel != null) {
                    wktPanel.populateExpression(markerSymbol.getWellKnownName().toString());
                }

                if (multiOptionPanel != null) {
                    multiOptionPanel.setSelectedItem(WKT_SYMBOL_KEY);
                }
            }
        }
    }

    /**
     * Populate colour.
     *
     * @param fieldConfigManager the field config manager
     * @param fieldConfig the field config
     * @param expColour the exp colour
     * @param expOpacity the exp opacity
     */
    private void populateColour(GraphicPanelFieldManager fieldConfigManager,
            ColourFieldConfig fieldConfig, Expression expColour, Expression expOpacity) {
        FieldConfigBase field = fieldConfigManager.get(fieldConfig.getColour());
        if (field != null) {
            field.populate(expColour);
        }

        field = fieldConfigManager.get(fieldConfig.getOpacity());
        if (field != null) {
            field.populate(expOpacity);
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
                    Stroke stroke = null;
                    Fill fill = null;

                    // Stroke colour
                    FieldConfigBase field = null;
                    if (strokeEnabled) {
                        Expression expStrokeColour = null;
                        Expression expStrokeColourOpacity = null;
                        field = fieldConfigManager.get(this.strokeFieldConfig.getColour());
                        if (field != null) {
                            if (field instanceof FieldConfigColour) {
                                FieldConfigColour colourField = (FieldConfigColour) field;

                                expStrokeColour = colourField.getColourExpression();
                            }
                        }

                        // Stroke width
                        Expression strokeWidth = null;
                        field = fieldConfigManager.get(this.strokeFieldConfig.getWidth());
                        if (field != null) {
                            strokeWidth = field.getExpression();
                        }

                        // Opacity
                        field = fieldConfigManager.get(this.strokeFieldConfig.getOpacity());
                        if (field != null) {
                            expStrokeColourOpacity = field.getExpression();
                        }

                        stroke = getStyleFactory().createStroke(expStrokeColour,
                                strokeWidth,
                                expStrokeColourOpacity);
                    }

                    // Fill colour
                    if (fillEnabled) {
                        Expression expFillColour = null;
                        Expression expFillColourOpacity = null;

                        field = fieldConfigManager.get(this.fillFieldConfig.getColour());
                        if (field != null) {
                            if (field instanceof FieldConfigColour) {
                                FieldConfigColour colourField = (FieldConfigColour) field;

                                expFillColour = colourField.getColourExpression();
                            }
                        }

                        // Opacity
                        field = fieldConfigManager.get(this.fillFieldConfig.getOpacity());
                        if (field != null) {
                            expFillColourOpacity = field.getExpression();
                        }
                        fill = getStyleFactory().createFill(expFillColour, expFillColourOpacity);
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
        return getStyleFactory().fill(graphicFill, null, null);
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

        map.put(FieldIdEnum.WKT, this);

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

                        if (valueString.startsWith(WKT_PREFIX)) {
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
        if (wktPanel != null) {
            if (wktPanel.getExpression() != null) {
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
        if (wktPanel != null) {
            wktPanel.populateExpression(testValue);
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
        FieldConfigWKT copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigWKT(fieldConfigBase.getCommonData(), fillFieldConfig,
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
        if (wktPanel != null) {
            wktPanel.setVisible(visible);
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
     * Gets the vendor option version.
     *
     * @return the vendor option version
     */
    public VendorOptionVersion getVendorOptionVersion() {
        if (wktPanel != null) {
            return wktPanel.getVendorOptionVersion();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        VendorOptionPresent voPresent = new VendorOptionPresent(sldObj, getVendorOptionInfo());

        vendorOptionsPresentList.add(voPresent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        if (vendorOptionInfo == null) {
            vendorOptionInfo = new VendorOptionInfo("WKT", getVendorOptionVersion(),
                    Localisation.getString(WKTDetails.class, "WKTDetails.description"));
        }
        return vendorOptionInfo;
    }
}
