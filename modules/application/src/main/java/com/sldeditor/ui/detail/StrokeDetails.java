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
package com.sldeditor.ui.detail;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.ConstantExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicStroke;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeFactory;
import com.sldeditor.ui.iface.MultiOptionSelectedInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class StrokeDetails allows a user to configure stroke data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class StrokeDetails extends StandardPanel implements MultiOptionSelectedInterface, PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant SOLID_LINE_KEY. */
    private static final String SOLID_LINE_KEY = "solid";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The fill factory. */
    private SymbolTypeFactory fillFactory = null;

    /**  The panel id of the selected fill. */
    private Class<?> selectedFillPanelId = null;

    /**  The field enable state map, indicates which fields to enable. */
    private FieldEnableState fieldEnableState = null;

    /**
     * Constructor.
     */
    public StrokeDetails(FunctionNameInterface functionManager)
    {
        super(StrokeDetails.class, functionManager);

        setUpdateSymbolListener(this);

        Class<?> symbolizerClass = PolygonSymbolizerImpl.class;

        fillFactory = new SymbolTypeFactory(StrokeDetails.class, new FieldId(FieldIdEnum.STROKE_FILL_COLOUR), new FieldId(FieldIdEnum.STROKE_FILL_OPACITY), new FieldId(FieldIdEnum.STROKE_STYLE));

        fieldEnableState = fillFactory.getFieldOverrides(symbolizerClass);

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        readConfigFile(this, "Stroke.xml");

        fillFactory.populate(this, fieldConfigManager);
    }

    /**
     * Parses the dash array.
     *
     * @param text the text
     */
    protected void parseDashArray(String text) {
        List<Float> floatList = createDashArray(text);

        if(floatList != null)
        {
            updateSymbol();
        }
    }

    /**
     * Gets the stroke.
     *
     * @return the stroke
     */
    public Stroke getStroke() {

        Expression join = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_LINE_JOIN);
        Expression lineCap = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_LINE_CAP);
        Expression offset = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_OFFSET);
        Expression strokeWidth = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_WIDTH);

        ValueComboBoxData symbolTypeValue = fieldConfigVisitor.getComboBox(FieldIdEnum.STROKE_STYLE);
        Expression symbolType = null;
        if(symbolTypeValue != null)
        {
            symbolType = getFilterFactory().literal(symbolTypeValue.getKey());
        }

        List<Float> dashList = createDashArray(fieldConfigVisitor.getText(FieldIdEnum.STROKE_DASH_ARRAY));
        float[] dashes = convertDashListToArray(dashList);

        FieldConfigBase fdmFillColour = fieldConfigManager.get(FieldIdEnum.STROKE_FILL_COLOUR);
        FieldConfigColour colourField = (FieldConfigColour)fdmFillColour;
        Expression fillColour = colourField.getColourExpression();
        Expression fillColourOpacity = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_FILL_OPACITY);

        FieldConfigBase fdmStrokeColour = fieldConfigManager.get(FieldIdEnum.STROKE_STROKE_COLOUR);
        FieldConfigColour strokeColourField = (FieldConfigColour)fdmStrokeColour;

        Expression strokeColour = strokeColourField.getColourExpression();
        Expression strokeColourOpacity = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_STROKE_OPACITY);

        boolean isLine = true;
        if(symbolTypeValue != null)
        {
            isLine = (symbolTypeValue.getKey().compareTo(SOLID_LINE_KEY) == 0);
        }

        boolean fillColourEnabled = isPanelEnabled(GroupIdEnum.FILLCOLOUR);
        boolean strokeColourEnabled = isPanelEnabled(GroupIdEnum.STROKECOLOUR);

        Stroke stroke = null;
        if(isLine)
        {
            stroke = getStyleFactory().stroke(fillColour,
                    fillColourOpacity,
                    strokeWidth,
                    join, lineCap, dashes, offset);
        }
        else
        {
            stroke = getStyleFactory().getDefaultStroke();
            stroke.setLineCap(lineCap);
            stroke.setLineJoin(join);
            stroke.setDashOffset(offset);

            List<Expression> dashExpressionList = createDashArrayList(dashes);
            stroke.setDashArray(dashExpressionList);

            AnchorPoint anchorPoint = null;
            if(isPanelEnabled(GroupIdEnum.ANCHORPOINT))
            {
                anchorPoint = getStyleFactory().anchorPoint(fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_ANCHOR_POINT_H),
                        fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_ANCHOR_POINT_V));
            }

            Displacement displacement = null;
            if(isPanelEnabled(GroupIdEnum.DISPLACEMENT))
            {
                anchorPoint = getStyleFactory().anchorPoint(fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_DISPLACEMENT_X),
                        fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_DISPLACEMENT_Y));
            }

            List<GraphicalSymbol> symbols = fillFactory.getValue(this.fieldConfigManager, symbolType, fillColourEnabled, strokeColourEnabled, selectedFillPanelId);

            Expression initalGap = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_INITIAL_GAP);
            Expression gap = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_GAP);

            GraphicStroke graphicStroke = getStyleFactory().graphicStroke(symbols, fillColourOpacity,
                    fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_SIZE),
                    fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_ANGLE), 
                    anchorPoint, displacement,
                    initalGap,
                    gap);

            stroke.setGraphicStroke(graphicStroke);

            if(strokeColourEnabled)
            {
                stroke.setColor(strokeColour);
                stroke.setOpacity(strokeColourOpacity);
                stroke.setWidth(strokeWidth);
            }
        }
        return stroke;
    }

    /**
     * Creates the dash array list for an array of floats.
     *
     * @param dashes the dashes
     * @return the list
     */
    List<Expression> createDashArrayList(float[] dashes) {
        List<Expression> dashExpressionList = null;

        if(dashes != null)
        {
            dashExpressionList = new ArrayList<Expression>();
            for(float dashValue : dashes)
            {
                dashExpressionList.add(getFilterFactory().literal(dashValue));
            }
        }
        return dashExpressionList;
    }

    /**
     * Convert dash list to array.
     *
     * @param dashList the dash list
     * @return the float[]
     */
    private float[] convertDashListToArray(List<Float> dashList)
    {
        if(dashList == null)
        {
            return null;
        }

        float[] dashes = new float[dashList.size()];
        int i = 0;

        for (Float f : dashList) {
            dashes[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return dashes;
    }

    /**
     * Creates the dash array.
     *
     * @param dashString the dash string
     * @return the float[]
     */
    private List<Float> createDashArray(String dashString) {
        String[] dashes = dashString.split(" ");

        List<Float> floatDashArray = new ArrayList<Float>();

        for(String dashValue : dashes)
        {
            try
            {
                floatDashArray.add(Float.parseFloat(dashValue));
            }
            catch(NumberFormatException e)
            {
                return null;
            }
        }
        return floatDashArray;
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        Stroke stroke = getStrokeFromSymbolizer(selectedSymbol);

        populateStroke(stroke);
    }

    /**
     * Gets the stroke from symbolizer.
     *
     * @param selectedSymbol the selected symbol
     * @return the stroke from symbolizer
     */
    private Stroke getStrokeFromSymbolizer(SelectedSymbol selectedSymbol) {
        Stroke stroke = null;

        Symbolizer symbolizer = selectedSymbol.getSymbolizer();
        if(symbolizer instanceof PointSymbolizer)
        {
            Graphic graphic = selectedSymbol.getGraphic();

            List<GraphicalSymbol> graphicalSymbols = graphic.graphicalSymbols();

            if(graphicalSymbols.size() > 0)
            {
                GraphicalSymbol symbol = graphicalSymbols.get(0);

                if(symbol instanceof MarkImpl)
                {
                    MarkImpl markerSymbol = (MarkImpl) symbol;

                    stroke = markerSymbol.getStroke();
                }
            }
        }
        else if(symbolizer instanceof LineSymbolizer)
        {
            LineSymbolizer lineSymbol = (LineSymbolizer) selectedSymbol.getSymbolizer();
            stroke = lineSymbol.getStroke();
        }
        else if(symbolizer instanceof PolygonSymbolizer)
        {
            PolygonSymbolizer polygonSymbol = (PolygonSymbolizer) selectedSymbol.getSymbolizer();
            stroke = polygonSymbol.getStroke();
        }
        return stroke;
    }

    /**
     * Populate stroke.
     *
     * @param stroke the stroke
     */
    private void populateStroke(Stroke stroke) {

        Expression expFillColour = null;
        Expression expFillColourOpacity = null;
        Expression expStrokeColour = null;
        Expression expStrokeWidth = null;
        Expression expStrokeOffset = null;
        Expression expStrokeLineCap = null;
        Expression expStrokeLineJoin = null;
        Expression expStrokeDashArray = null;
        Expression expAnchorPointX = null;
        Expression expAnchorPointY = null;
        Expression expDisplacementX = null;
        Expression expDisplacementY = null;
        Expression expGap = null;
        Expression expInitialGap = null;
        Expression expSymbolSize = null;
        Expression expSymbolRotation = null;
        Expression expStrokeOpacityColour = null;
        Expression expStrokeSymbol = null;

        if(stroke == null)
        {
            expFillColour = getFilterFactory().literal("#000000");
            expFillColourOpacity = getFilterFactory().literal(1.0);
            fillFactory.setSolidFill(fieldConfigManager, expFillColour, expFillColourOpacity);

            expStrokeColour = getFilterFactory().literal("#000000");
            expStrokeWidth = getFilterFactory().literal(1.0);
            expStrokeOffset = getFilterFactory().literal(0.0);
            expFillColourOpacity = getFilterFactory().literal(1.0);
            expStrokeLineCap = getFilterFactory().literal("round");
            expStrokeLineJoin = getFilterFactory().literal("round");
            expStrokeDashArray = getFilterFactory().literal("");
        }
        else
        {
            Graphic graphicFill = stroke.getGraphicFill();
            Graphic graphicStroke = stroke.getGraphicStroke();

            boolean fillColourEnabled = false;
            boolean strokeColourEnabled = false;

            if((graphicFill == null) && (graphicStroke == null))
            {
                fillColourEnabled = true;
                expFillColour = stroke.getColor();
                expFillColourOpacity = stroke.getOpacity();
                fillFactory.setSolidFill(fieldConfigManager, stroke.getColor(), stroke.getOpacity());
            }

            expFillColourOpacity = stroke.getOpacity();
            expStrokeWidth = stroke.getWidth();
            expStrokeOffset = stroke.getDashOffset();
            expStrokeLineCap = stroke.getLineCap();
            expStrokeLineJoin = stroke.getLineJoin();

            List<Float> dashesArray = getStrokeDashArray(stroke);

            expStrokeDashArray = getFilterFactory().literal(createDashArrayString(dashesArray));

            if(graphicStroke != null)
            {
                // Anchor points
                AnchorPoint anchorPoint = graphicStroke.getAnchorPoint();
                if(anchorPoint != null)
                {
                    expAnchorPointX = anchorPoint.getAnchorPointX();
                    expAnchorPointY = anchorPoint.getAnchorPointY();
                }

                // Displacement
                Displacement displacement = graphicStroke.getDisplacement();
                if(displacement != null)
                {
                    expDisplacementX = displacement.getDisplacementX();
                    expDisplacementY = displacement.getDisplacementY();
                }

                expGap = graphicStroke.getGap();
                expInitialGap = graphicStroke.getInitialGap();

                expSymbolSize = graphicStroke.getSize();
                expSymbolRotation = graphicStroke.getRotation();

                List<GraphicalSymbol> graphicSymbolList = graphicStroke.graphicalSymbols();

                for(GraphicalSymbol graphicSymbol : graphicSymbolList)
                {
                    if(graphicSymbol instanceof MarkImpl)
                    {
                        MarkImpl mark = (MarkImpl)graphicSymbol;
                        expStrokeSymbol = mark.getWellKnownName();

                        Mark defaultMark = getStyleFactory().getDefaultMark();

                        expFillColour = defaultMark.getFill().getColor();
                        expFillColourOpacity = defaultMark.getFill().getOpacity();

                        Fill markFill = mark.getFill();

                        if(markFill != null)
                        {
                            fillColourEnabled = true;
                            expFillColour = markFill.getColor();
                            expFillColourOpacity = markFill.getOpacity();
                        }

                        expStrokeColour = defaultMark.getStroke().getColor();
                        expStrokeOpacityColour = defaultMark.getStroke().getOpacity();

                        Stroke markStroke = mark.getStroke();

                        if(markStroke != null)
                        {
                            strokeColourEnabled = true;
                            expStrokeColour = markStroke.getColor();
                            expStrokeOpacityColour = markStroke.getOpacity();
                        }
                    }
                    else if(graphicSymbol instanceof ExternalGraphicImpl)
                    {
                        @SuppressWarnings("unused")
                        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl)graphicSymbol;

                    }
                }
            }

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_STYLE, expStrokeSymbol);

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_WIDTH, expStrokeWidth);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_OFFSET, expStrokeOffset);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_LINE_CAP, expStrokeLineCap);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_LINE_JOIN, expStrokeLineJoin);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_DASH_ARRAY, expStrokeDashArray);

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_GAP, expGap);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_INITIAL_GAP, expInitialGap);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_SIZE, expSymbolSize);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_ANGLE, expSymbolRotation);

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_ANCHOR_POINT_H, expAnchorPointX);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_ANCHOR_POINT_V, expAnchorPointY);

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_DISPLACEMENT_X, expDisplacementX);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_SYMBOL_DISPLACEMENT_Y, expDisplacementY);

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_STROKE_COLOUR, expStrokeColour);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_STROKE_OPACITY, expStrokeOpacityColour);

            GroupConfigInterface fillColourGroup = getGroup(GroupIdEnum.FILLCOLOUR);
            if(fillColourGroup != null)
            {
                fillColourGroup.enable(fillColourEnabled);
            }

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_FILL_COLOUR, expFillColour);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_FILL_OPACITY, expFillColourOpacity);

            GroupConfigInterface strokeColourGroup = getGroup(GroupIdEnum.STROKECOLOUR);
            if(strokeColourGroup != null)
            {
                strokeColourGroup.enable(strokeColourEnabled);
            }

        }
    }

    /**
     * Gets the stroke dash array.
     *
     * @param stroke the stroke
     * @return the stroke dash array
     */
    private List<Float> getStrokeDashArray(Stroke stroke)
    {
        List<Expression> expressionList = stroke.dashArray();
        List<Float> valueList = new ArrayList<Float>();

        if(expressionList != null)
        {
            for(Expression expression : expressionList)
            {
                if(expression instanceof LiteralExpressionImpl)
                {
                    LiteralExpressionImpl lExpression = (LiteralExpressionImpl) expression;

                    Float objValue = null;
                    if(lExpression.getValue() instanceof Float)
                    {
                        objValue = (Float)lExpression.getValue();
                    }
                    else if(lExpression.getValue() instanceof Double)
                    {
                        objValue = ((Double)lExpression.getValue()).floatValue();
                    }

                    valueList.add(objValue);
                }
                else if(expression instanceof ConstantExpression)
                {
                    ConstantExpression cExpression = (ConstantExpression) expression;

                    Float objValue = (Float)cExpression.getValue();

                    valueList.add(objValue);
                }
            }
        }
        return valueList;
    }

    /**
     * Creates the dash array string.
     *
     * @param dashesArray the dashes array
     * @return the string
     */
    private String createDashArrayString(List<Float> dashesArray) {
        StringBuilder sb = new StringBuilder();

        if(dashesArray != null)
        {
            int index = 0;

            for(Float value : dashesArray)
            {
                String str = Float.toString(value);

                if(str.endsWith(".0"))
                {
                    sb.append(value.intValue());
                }
                else
                {
                    sb.append(value);
                }

                if(index < dashesArray.size() - 1)
                {
                    sb.append(" ");
                }
                index ++;
            }
        }

        return sb.toString();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            Stroke stroke = getStroke();

            Symbolizer symbolizer = SelectedSymbol.getInstance().getSymbolizer();
            if(symbolizer instanceof PointSymbolizer)
            {
                PointSymbolizer pointSymbol = (PointSymbolizer) symbolizer;

                Graphic graphic = pointSymbol.getGraphic();

                GraphicalSymbol symbol = graphic.graphicalSymbols().get(0);

                if(symbol instanceof MarkImpl)
                {
                    MarkImpl markerSymbol = (MarkImpl) symbol;

                    markerSymbol.setStroke(stroke);

                    this.fireUpdateSymbol();
                }
            }
            else if(symbolizer instanceof LineSymbolizer)
            {
                LineSymbolizer lineSymbol = (LineSymbolizer) symbolizer;

                lineSymbol.setStroke(stroke);

                this.fireUpdateSymbol();
            }
            else if(symbolizer instanceof PolygonSymbolizer)
            {
                PolygonSymbolizer polygonSymbol = (PolygonSymbolizer) symbolizer;

                polygonSymbol.setStroke(stroke);

                this.fireUpdateSymbol();
            }
        }
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    @Override
    public void dataChanged(FieldId changedField) {
        updateSymbol();
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return fieldConfigManager;
    }

    /**
     * Option selected.
     *
     * @param panelId the panel id
     * @param selectedItem the selected item
     */
    @Override
    public void optionSelected(Class<?> panelId, String selectedItem) {
        setSymbolTypeVisibility(panelId, selectedItem);

        selectedFillPanelId = panelId;

        dataHasChanged();
    }

    /**
     * Sets the symbol type visibility.
     *
     * @param panelId the panel id
     * @param selectedItem the selected item
     */
    private void setSymbolTypeVisibility(Class<?> panelId, String selectedItem)
    {
        List<FieldId> list = fieldEnableState.getFieldIdList(panelId.getName(), selectedItem);

        for(FieldConfigBase fieldConfig : this.getFieldConfigList())
        {
            FieldId fieldId = fieldConfigManager.getFieldEnum(panelId, fieldConfig);
            FieldIdEnum field = fieldId.getFieldId();
            if((field != FieldIdEnum.STROKE_STYLE) && (list != null))
            {
                if(field != FieldIdEnum.UNKNOWN)
                {
                    boolean disable = !list.contains(fieldId);
                    fieldConfig.setFieldStateOverride(disable);
                }
            }
        }
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return SelectedSymbol.getInstance().hasStroke();
    }
}
