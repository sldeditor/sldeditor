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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Fill;
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
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class FieldConfigMarker handles all the marker symbols, including:
 * <ul>
 * <li>the solid fill</li>
 * <li>no fill</li>
 * <li>vendor option marker symbols</li>
 * </ul>
 * <p>
 * No field is displayed when a marker symbol type is selected.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigMarker extends FieldState {

    /** The Constant GEOSERVER_MARKER_PREFIX. */
    private static final String GEOSERVER_MARKER_PREFIX = "shape://";

    /** The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE, file containing the field enable/disable field states for the different symbol types. */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symbol/marker/standard/SymbolTypeFieldState_Marker.xml";

    /** The Constant NONE_SYMBOL_KEY. */
    private static final String NONE_SYMBOL_KEY = "none";

    /** The Constant SOLID_SYMBOL. */
    private static final String SOLID_SYMBOL_KEY = "solid";

    /** The vendor option version. */
    private VendorOptionVersion vendorOptionVersion = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();

    /**
     * Constructor.
     *
     * @param commonData the common data
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the symbol selection field
     */
    public FieldConfigMarker(FieldConfigCommonData commonData, ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig, FieldIdEnum symbolSelectionField) {
        this(SYMBOLTYPE_FIELD_STATE_RESOURCE, commonData, fillFieldConfig, strokeFieldConfig,
                symbolSelectionField);
    }

    /**
     * Instantiates a new field config marker.
     *
     * @param resourceFile the resource file
     * @param commonData the common data
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the symbol selection field
     */
    protected FieldConfigMarker(String resourceFile, FieldConfigCommonData commonData, ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig, FieldIdEnum symbolSelectionField) {
        super(commonData, resourceFile, fillFieldConfig, strokeFieldConfig,
                symbolSelectionField);
    }

    /**
     * Creates the ui.
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        createFieldPanel(0, "");
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
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        // Not used
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    @Override
    protected Expression generateExpression() {
        Expression expression = null;

        return expression;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    @Override
    public boolean isEnabled() {
        // Not used
        return false;
    }

    /**
     * Revert to default value.
     */
    @Override
    public void revertToDefaultValue() {
        // Not used
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
        // Do nothing
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    @Override
    public VendorOptionVersion getVendorOption() {
        return vendorOptionVersion;
    }

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    @Override
    public Class<?> getSymbolClass() {
        return MarkImpl.class;
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
            MarkImpl markerSymbol = (MarkImpl) symbol;

            Expression wellKnownNameExpression = markerSymbol.getWellKnownName();

            if (wellKnownNameExpression instanceof LiteralExpressionImpl) {
                LiteralExpressionImpl literal = (LiteralExpressionImpl) wellKnownNameExpression;
                FieldConfigBase field = fieldConfigManager.get(symbolSelectionField);
                if (field != null) {
                    field.populate(literal);
                }

                Expression expFillColour = null;
                Expression expFillOpacity = null;
                Expression expStrokeColour = null;
                Expression expStrokeOpacity = null;
                Expression expStrokeWidth = null;

                // Which opacity attribute do we use?
                if (symbol instanceof MarkImpl) {
                    MarkImpl markSymbol = (MarkImpl) symbol;
                    Fill fill = markSymbol.getFill();

                    if (fill != null) {
                        expFillColour = fill.getColor();
                        if (!isOverallOpacity(symbolizerType)) {
                            expFillOpacity = fill.getOpacity();
                        }
                    }

                    Stroke stroke = markSymbol.getStroke();
                    if (stroke != null) {
                        expStrokeColour = stroke.getColor();

                        if (!isOverallOpacity(symbolizerType)) {
                            expStrokeOpacity = stroke.getOpacity();
                        }
                        expStrokeWidth = stroke.getWidth();
                    }
                }

                if (isOverallOpacity(symbolizerType)) {
                    FieldConfigBase opacity = fieldConfigManager.get(FieldIdEnum.OVERALL_OPACITY);
                    if (opacity != null) {
                        opacity.populate(graphic.getOpacity());
                    }
                }

                FieldConfigBase opacity = fieldConfigManager.get(fillFieldConfig.getOpacity());
                if (opacity != null) {
                    opacity.populate(expFillOpacity);
                }

                opacity = fieldConfigManager.get(strokeFieldConfig.getOpacity());
                if (opacity != null) {
                    opacity.populate(expStrokeOpacity);
                }

                Class<?> panelId = getCommonData().getPanelId();
                GroupConfigInterface fillGroup = fieldConfigManager.getGroup(panelId,
                        fillFieldConfig.getGroup());
                GroupConfigInterface strokeGroup = fieldConfigManager.getGroup(panelId,
                        strokeFieldConfig.getGroup());

                if ((fillGroup == null) || (strokeGroup == null)) {
                    return;
                }

                if (literal.toString().startsWith(GEOSERVER_MARKER_PREFIX)) {
                    fillGroup.enable(expStrokeColour != null);
                    FieldConfigBase fillColour = fieldConfigManager
                            .get(fillFieldConfig.getColour());
                    if (fillColour != null) {
                        fillColour.populate(expStrokeColour);
                    }

                    opacity = fieldConfigManager.get(fillFieldConfig.getOpacity());
                    if (opacity != null) {
                        opacity.populate(expStrokeOpacity);
                    }

                    strokeGroup.enable(false);
                } else {
                    fillGroup.enable(expFillColour != null);
                    FieldConfigBase fillColour = fieldConfigManager
                            .get(fillFieldConfig.getColour());
                    if (fillColour != null) {
                        fillColour.populate(expFillColour);
                    }

                    strokeGroup.enable(expStrokeColour != null);
                    FieldConfigBase strokeColour = fieldConfigManager
                            .get(strokeFieldConfig.getColour());
                    if (strokeColour != null) {
                        strokeColour.populate(expStrokeColour);
                    }

                    FieldConfigBase strokeWidth = fieldConfigManager
                            .get(FieldIdEnum.STROKE_FILL_WIDTH);
                    if (strokeWidth != null) {
                        strokeWidth.populate(expStrokeWidth);
                    }
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
        if ((symbolType == null) || (fieldConfigManager == null)) {
            return null;
        }

        Expression symbolTypeExpression = null;

        String symbolTypeName = symbolType.toString();
        if (symbolTypeName.compareTo(SOLID_SYMBOL_KEY) != 0) {
            symbolTypeExpression = symbolType;
        }

        Fill fill = null;
        Stroke stroke = null;

        if (symbolTypeName.startsWith(GEOSERVER_MARKER_PREFIX)) {
            Expression strokeColour = null;
            FieldConfigBase field = fieldConfigManager.get(fillFieldConfig.getColour());
            if (field != null) {
                strokeColour = ((FieldConfigColour) field).getColourExpression();
            }

            Expression strokeColourOpacity = null;
            field = fieldConfigManager.get(fillFieldConfig.getOpacity());
            if (field != null) {
                strokeColourOpacity = field.getExpression();
            }

            Expression strokeWidth = null;
            field = fieldConfigManager.get(fillFieldConfig.getWidth());
            if (field != null) {
                strokeWidth = field.getExpression();
            }

            stroke = getStyleFactory().createStroke(strokeColour, strokeWidth, strokeColourOpacity);
        } else {
            Expression fillColour = null;

            FieldConfigBase field = fieldConfigManager.get(fillFieldConfig.getColour());
            if (field != null) {
                fillColour = ((FieldConfigColour) field).getColourExpression();
            }

            Expression fillColourOpacity = null;
            field = fieldConfigManager.get(fillFieldConfig.getOpacity());
            if (field != null) {
                fillColourOpacity = field.getExpression();
            }

            if (fillEnabled) {
                fill = getStyleFactory().fill(null, fillColour, fillColourOpacity);
            }

            if (strokeEnabled) {
                Expression strokeColour = null;
                field = fieldConfigManager.get(strokeFieldConfig.getColour());
                if (field != null) {
                    strokeColour = ((FieldConfigColour) field).getColourExpression();
                }

                Expression strokeColourOpacity = null;
                field = fieldConfigManager.get(strokeFieldConfig.getOpacity());
                if (field != null) {
                    strokeColourOpacity = field.getExpression();
                }

                Expression strokeWidth = null;
                field = fieldConfigManager.get(strokeFieldConfig.getWidth());
                if (field != null) {
                    strokeWidth = field.getExpression();
                }

                stroke = getStyleFactory().createStroke(strokeColour, strokeWidth,
                        strokeColourOpacity);
            }
        }

        Mark markerSymbol = getStyleFactory().mark(symbolTypeExpression, fill, stroke);

        return SelectedSymbol.getInstance().getSymbolList(markerSymbol);
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
        if (fieldConfigManager == null) {
            return null;
        }
        Expression fillColour = null;
        FieldConfigBase field = fieldConfigManager.get(fillFieldConfig.getColour());
        if (field != null) {
            if ((field instanceof FieldConfigColour) && field.isEnabled()) {
                fillColour = ((FieldConfigColour) field).getColourExpression();
            }
        }

        Expression fillColourOpacity = null;
        field = fieldConfigManager.get(fillFieldConfig.getOpacity());
        if (field != null) {
            fillColourOpacity = field.getExpression();
        }

        GraphicFill _graphicFill = null;
        Expression _fillColour = fillColour;
        Expression _fillColourOpacity = fillColourOpacity;
        if (graphicFill != null) {
            List<GraphicalSymbol> symbolList = graphicFill.graphicalSymbols();

            if ((symbolList != null) && (!symbolList.isEmpty())) {
                GraphicalSymbol symbol = symbolList.get(0);
                Mark mark = (Mark) symbol;
                if (mark.getWellKnownName() != null) {
                    _graphicFill = graphicFill;
                    _fillColour = null;
                    _fillColourOpacity = null;
                }
            }
        }
        Fill fill = getStyleFactory().fill(_graphicFill, _fillColour, _fillColourOpacity);

        return fill;
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
     * Sets the solid fill.
     *
     * @param fieldConfigManager the field config manager
     * @param expFillColour the new solid fill
     * @param expFillColourOpacity the expression fill colour opacity
     */
    public void setSolidFill(GraphicPanelFieldManager fieldConfigManager, Expression expFillColour,
            Expression expFillColourOpacity) {

        if (fieldConfigManager != null) {
            FieldConfigBase field = fieldConfigManager.get(fillFieldConfig.getColour());
            if (field != null) {
                field.populate(expFillColour);
            }

            Class<?> panelId = getCommonData().getPanelId();
            GroupConfigInterface fillGroup = fieldConfigManager.getGroup(panelId, GroupIdEnum.FILL);

            if (fillGroup != null) {
                fillGroup.enable(expFillColour != null);
            }

            // Opacity
            field = fieldConfigManager.get(fillFieldConfig.getOpacity());
            if (field != null) {
                field.populate(expFillColourOpacity);
            }

        }
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

        if (fieldConfigManager != null) {
            if (fillFieldConfig != null) {
                map.put(fillFieldConfig.getColour(),
                        fieldConfigManager.get(fillFieldConfig.getColour()));
                map.put(fillFieldConfig.getOpacity(),
                        fieldConfigManager.get(fillFieldConfig.getOpacity()));
                if (fillFieldConfig.getWidth() != null) {
                    map.put(fillFieldConfig.getWidth(),
                            fieldConfigManager.get(fillFieldConfig.getWidth()));
                }
            }
            if (strokeFieldConfig != null) {
                map.put(strokeFieldConfig.getColour(),
                        fieldConfigManager.get(strokeFieldConfig.getColour()));
                map.put(strokeFieldConfig.getOpacity(),
                        fieldConfigManager.get(strokeFieldConfig.getOpacity()));
                if (strokeFieldConfig.getWidth() != null) {
                    map.put(strokeFieldConfig.getWidth(),
                            fieldConfigManager.get(strokeFieldConfig.getWidth()));
                }
            }
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

                        List<ValueComboBoxData> localSymbolList = getLocalSymbolList();
                        if (localSymbolList != null) {
                            for (ValueComboBoxData data : localSymbolList) {
                                if (data.getKey().compareTo(valueString) == 0) {
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
     * Gets the field.
     *
     * @return the field
     */
    @Override
    public FieldConfigBase getConfigField() {
        return this;
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
    public String getStringValue() {
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

        if (fieldConfigBase != null) {
            copy = new FieldConfigMarker(fieldConfigBase.getCommonData(), this.fillFieldConfig,
                    this.strokeFieldConfig, this.symbolSelectionField);
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
        // Does nothing, always visible
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#isOverallOpacity(com.sldeditor.ui.detail.GraphicPanelFieldManager,
     * org.opengis.filter.expression.Expression)
     */
    @Override
    public boolean isOverallOpacity(Class<?> symbolizerType) {
        return (symbolizerType == LineSymbolizerImpl.class);
    }

    /**
     * Sets the vendor option version.
     *
     * @param vendorOptionVersion the vendorOptionVersion to set
     */
    public void setVendorOptionVersion(VendorOptionVersion vendorOptionVersion) {
        if(vendorOptionVersion != null)
        {
            this.vendorOptionVersion = vendorOptionVersion;
        }
        else
        {
            this.vendorOptionVersion = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#populateVendorOptionFieldMap(java.util.Map)
     */
    @Override
    protected void populateVendorOptionFieldMap(
            Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        
    }
}
