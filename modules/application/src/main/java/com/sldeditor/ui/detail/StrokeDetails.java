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
import java.util.Map;

import org.geotools.filter.ConstantExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.AnchorPointImpl;
import org.geotools.styling.Displacement;
import org.geotools.styling.DisplacementImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicStroke;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.base.CurrentFieldState;
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

    /** The default anchor point. */
    private static AnchorPoint defaultAnchorPoint = new AnchorPointImpl();

    /** The default displacement. */
    private static Displacement defaultDisplacement = new DisplacementImpl();

    /**
     * Constructor.
     */
    public StrokeDetails(FunctionNameInterface functionManager)
    {
        super(StrokeDetails.class, functionManager);

        setUpdateSymbolListener(this);

        fillFactory = new SymbolTypeFactory(StrokeDetails.class, 
                new ColourFieldConfig(GroupIdEnum.FILLCOLOUR, FieldIdEnum.STROKE_FILL_COLOUR, FieldIdEnum.OPACITY, FieldIdEnum.STROKE_WIDTH),
                new ColourFieldConfig(GroupIdEnum.STROKECOLOUR, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OPACITY, FieldIdEnum.STROKE_FILL_WIDTH),
                FieldIdEnum.STROKE_STYLE);

        fieldEnableState = fillFactory.getFieldOverrides(this.getClass());

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        readConfigFile(null, this, "Stroke.xml");

        fillFactory.populate(this, fieldConfigManager);
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
        Expression opacity = fieldConfigVisitor.getExpression(FieldIdEnum.OPACITY);

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
                    opacity,
                    strokeWidth,
                    join, lineCap, dashes, offset);
        }
        else
        {
            stroke = getStyleFactory().getDefaultStroke();

            AnchorPoint anchorPoint = getStyleFactory().anchorPoint(fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_ANCHOR_POINT_H),
                    fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_ANCHOR_POINT_V));

            // Ignore the anchor point if it is the same as the default so it doesn't appear in the SLD
            if(DetailsUtilities.isSame(defaultAnchorPoint, anchorPoint))
            {
                anchorPoint = null;
            }

            Displacement displacement = getStyleFactory().displacement(fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_DISPLACEMENT_X),
                    fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_DISPLACEMENT_Y));

            // Ignore the displacement if it is the same as the default so it doesn't appear in the SLD
            if(DetailsUtilities.isSame(defaultDisplacement, displacement))
            {
                displacement = null;
            }

            List<GraphicalSymbol> symbols = fillFactory.getValue(this.fieldConfigManager, symbolType, fillColourEnabled, strokeColourEnabled, selectedFillPanelId);

            Expression initalGap = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_INITIAL_GAP);
            Expression gap = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_GAP);

            Expression rotation = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_ANGLE);
            Expression symbolSize = fieldConfigVisitor.getExpression(FieldIdEnum.STROKE_SYMBOL_SIZE);

            GraphicStroke graphicStroke = getStyleFactory().graphicStroke(symbols, opacity,
                    symbolSize,
                    rotation, 
                    anchorPoint, displacement,
                    initalGap,
                    gap);

            stroke.setGraphicStroke(graphicStroke);
            stroke.setWidth(strokeWidth);
        }
        return stroke;
    }

    /**
     * Creates the dash array list for an array of floats.
     *
     * @param dashes the dashes
     * @return the list
     */
    private List<Expression> createDashArrayList(float[] dashes) {
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

        if(selectedSymbol != null)
        {
            Symbolizer symbolizer = selectedSymbol.getSymbolizer();
            if(symbolizer instanceof PointSymbolizer)
            {
                PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer; 
                Graphic graphic = pointSymbolizer.getGraphic(); 

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
                LineSymbolizer lineSymbol = (LineSymbolizer) symbolizer;
                stroke = lineSymbol.getStroke();
            }
            else if(symbolizer instanceof PolygonSymbolizer)
            {
                PolygonSymbolizer polygonSymbol = (PolygonSymbolizer) symbolizer;
                stroke = polygonSymbol.getStroke();
            }
        }
        return stroke;
    }

    /**
     * Populate stroke.
     *
     * @param stroke the stroke
     */
    private void populateStroke(Stroke stroke) {

        Expression expColour = null;
        Expression expStrokeColour = null;
        Expression expOpacity = null;
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

        if(stroke == null)
        {
            expColour = getFilterFactory().literal("#000000");
            expOpacity = getFilterFactory().literal(1.0);
            fillFactory.setSolidFill(fieldConfigManager, expColour, expOpacity);

            expStrokeWidth = getFilterFactory().literal(1.0);
            expStrokeOffset = getFilterFactory().literal(0.0);
            expStrokeLineCap = getFilterFactory().literal("round");
            expStrokeLineJoin = getFilterFactory().literal("round");
            expStrokeDashArray = getFilterFactory().literal("");
        }
        else
        {
            Graphic graphicFill = stroke.getGraphicFill();
            Graphic graphicStroke = stroke.getGraphicStroke();

            boolean strokeColourEnabled = false;

            if((graphicFill == null) && (graphicStroke == null))
            {
                expOpacity = stroke.getOpacity();
                fillFactory.setSolidFill(fieldConfigManager, stroke.getColor(), stroke.getOpacity());
            }

            expOpacity = stroke.getOpacity();
            expStrokeWidth = stroke.getWidth();
            expStrokeOffset = stroke.getDashOffset();
            expStrokeLineCap = stroke.getLineCap();
            expStrokeLineJoin = stroke.getLineJoin();
            expColour = stroke.getColor();

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
                else
                {
                    expAnchorPointX = defaultAnchorPoint.getAnchorPointX();
                    expAnchorPointY = defaultAnchorPoint.getAnchorPointY();
                }

                // Displacement
                Displacement displacement = graphicStroke.getDisplacement();
                if(displacement != null)
                {
                    expDisplacementX = displacement.getDisplacementX();
                    expDisplacementY = displacement.getDisplacementY();
                }
                else
                {
                    expDisplacementX = defaultDisplacement.getDisplacementX();
                    expDisplacementY = defaultDisplacement.getDisplacementY();
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

                        Mark defaultMark = getStyleFactory().getDefaultMark();

                        Fill markFill = mark.getFill();

                        if(markFill != null)
                        {
                            expColour = markFill.getColor();
                        }

                        expStrokeColour = defaultMark.getStroke().getColor();

                        Stroke markStroke = mark.getStroke();

                        if(markStroke != null) 
                        { 
                            strokeColourEnabled = true;
                            expStrokeColour = markStroke.getColor();
                        }
                    }
                    else if(graphicSymbol instanceof ExternalGraphicImpl)
                    {
                        @SuppressWarnings("unused") 
                        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl)graphicSymbol;
                    }
                    fillFactory.setValue(this.fieldConfigManager, graphicStroke, graphicSymbol);
                }
            }

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

            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_FILL_COLOUR, expColour);
            fieldConfigVisitor.populateField(FieldIdEnum.STROKE_STROKE_COLOUR, expStrokeColour);

            GroupConfigInterface fillColourGroup = getGroup(GroupIdEnum.FILLCOLOUR);
            if(fillColourGroup != null)
            {
                fillColourGroup.enable(true);
            }

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
    public void dataChanged(FieldIdEnum changedField) {
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
     * @param fieldPanelId the field panel id
     * @param selectedItem the selected item
     */
    @Override
    public void optionSelected(Class<?> fieldPanelId, String selectedItem) {
        setSymbolTypeVisibility(fieldPanelId, selectedItem);

        selectedFillPanelId = fieldPanelId;

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
        Map<GroupIdEnum, Boolean> groupList = fieldEnableState.getGroupIdList(panelId.getName(), selectedItem);

        for(GroupIdEnum groupId : groupList.keySet())
        {
            boolean groupEnabled = groupList.get(groupId);
            GroupConfigInterface groupConfig = fieldConfigManager.getGroup(this.getClass(), groupId);
            if(groupConfig != null)
            {
                groupConfig.setGroupStateOverride(groupEnabled);
            }
            else
            {
                ConsoleManager.getInstance().error(this, "Failed to find group : " + groupId.toString());
            }
        }

        Map<FieldIdEnum, Boolean> fieldList = fieldEnableState.getFieldIdList(panelId.getName(), selectedItem);

        for(FieldIdEnum fieldId : fieldList.keySet())
        {
            boolean fieldEnabled = fieldList.get(fieldId);
            FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
            if(fieldConfig != null)
            {
                CurrentFieldState fieldState = fieldConfig.getFieldState();
                fieldState.setFieldEnabled(fieldEnabled);
                fieldConfig.setFieldState(fieldState);
            }
            else
            {
                ConsoleManager.getInstance().error(this, "Failed to find field : " + fieldId.toString());
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

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
