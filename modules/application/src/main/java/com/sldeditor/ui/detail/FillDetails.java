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

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Symbolizer;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeFactory;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.fill.VendorOptionFillFactory;
import com.sldeditor.ui.iface.MultiOptionSelectedInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class FillDetails allows a user to configure fill data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FillDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface, MultiOptionSelectedInterface, PrefUpdateVendorOptionInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The symbol type factory. */
    private SymbolTypeFactory symbolTypeFactory = null;

    /**  The field override map, indicates which fields to enable. */
    private FieldEnableState fieldEnableState = null;

    /**  The panel id of the selected fill. */
    private Class<?> selectedFillPanelId = null;

    /** The Constant configFile. */
    private static final String configFile = "PolygonFill.xml";

    /** The vendor option list allowed to be used. */
    private List<VersionData> vendorOptionOptionsList = new ArrayList<VersionData>();

    /** The vendor option fill factory. */
    private VendorOptionFillFactory vendorOptionFillFactory = null;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     * @param functionManager the function manager
     */
    protected FillDetails(Class<?> panelId, FunctionNameInterface functionManager)
    {
        super(panelId, functionManager);

        setUpdateSymbolListener(this);

        Class<?> symbolizerClass = PolygonSymbolizerImpl.class;

        symbolTypeFactory = new SymbolTypeFactory(FillDetails.class, new FieldId(FieldIdEnum.FILL_COLOUR), new FieldId(FieldIdEnum.OPACITY), new FieldId(FieldIdEnum.SYMBOL_TYPE));
        fieldEnableState = symbolTypeFactory.getFieldOverrides(symbolizerClass);
        createUI(symbolizerClass, configFile);
    }

    /**
     * Creates the ui.
     *
     * @param symbolizerClass the symbolizer class
     * @param configFile the config file
     */
    private void createUI(Class<?> symbolizerClass, String configFile) {

        PrefManager.getInstance().addVendorOptionListener(this);

        readConfigFile(this, configFile);

        symbolTypeFactory.populate(this, symbolizerClass, fieldConfigManager);

        createVendorOptionPanel();

    }

    /**
     * Creates the vendor option panel.
     *
     * @return the detail panel
     */
    private void createVendorOptionPanel() {

        vendorOptionFillFactory = new VendorOptionFillFactory(getPanelId(), getFunctionManager());

        List<VendorOptionInterface> voList = vendorOptionFillFactory.getVendorOptionList();
        if(voList != null)
        {
            for(VendorOptionInterface vendorOption : voList)
            {
                vendorOption.setParentPanel(this);
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
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
        Expression expSize = null;
        Expression expRotation = null;
        Expression expAnchorPointX = null;
        Expression expAnchorPointY = null;
        Expression expDisplacementX = null;
        Expression expDisplacementY = null;
        Expression expFillColour = null;
        Expression expOpacity = null;
        Expression expGap = null;
        Expression expInitialGap = null;
        Expression expStrokeColour = null;
        Expression expStrokeColourOpacity = null;
        Expression expStrokeWidth = null;

        boolean hasAnchorPoint = false;
        boolean hasDisplacement = false;
        boolean hasFillColour = false;
        boolean hasStroke = false;

        PolygonSymbolizer polygon = null;

        if(selectedSymbol != null)
        {
            Graphic graphic = selectedSymbol.getGraphic();

            if(graphic == null)
            {
                Symbolizer symbolizer = selectedSymbol.getSymbolizer();

                expOpacity = null;

                Fill fill = null;

                if(symbolizer instanceof PolygonSymbolizerImpl)
                {
                    polygon = (PolygonSymbolizer) symbolizer;

                    fill = polygon.getFill();

                    if(fill != null)
                    {
                        expFillColour = fill.getColor();
                        expOpacity = fill.getOpacity();
                    }
                }

                if(fill == null)
                {
                    hasFillColour = false;
                    symbolTypeFactory.setNoFill(this.fieldConfigManager);
                }
                else
                {
                    hasFillColour = true;
                    symbolTypeFactory.setSolidFill(this.fieldConfigManager, expFillColour, expOpacity);
                }
            }
            else
            {
                expSize = graphic.getSize();
                expRotation = graphic.getRotation();

                // Anchor point
                AnchorPoint anchorPoint = graphic.getAnchorPoint();

                if(anchorPoint != null)
                {
                    hasAnchorPoint = true;
                    expAnchorPointX = anchorPoint.getAnchorPointX();
                    expAnchorPointY = anchorPoint.getAnchorPointY();
                }

                // Offset
                Displacement displacement = graphic.getDisplacement();

                if(displacement != null)
                {
                    hasDisplacement = true;
                    expDisplacementX = displacement.getDisplacementX();
                    expDisplacementY = displacement.getDisplacementY();
                }

                expOpacity = graphic.getOpacity();
                expGap = graphic.getGap();
                expInitialGap = graphic.getInitialGap();

                List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();

                if(!graphicalSymbolList.isEmpty())
                {
                    GraphicalSymbol symbol = graphicalSymbolList.get(0);

                    // Which opacity attribute do we use?
                    if(symbol instanceof MarkImpl)
                    {
                        MarkImpl markSymbol = (MarkImpl)symbol;
                        Fill fill = markSymbol.getFill();

                        if(fill != null)
                        {
                            hasFillColour = true;
                            if(fill.getOpacity() != null)
                            {
                                expOpacity = fill.getOpacity();
                            }
                            expFillColour = fill.getColor();
                        }

                        Stroke stroke = markSymbol.getStroke();
                        if(stroke != null)
                        {
                            hasStroke = true;
                            expStrokeColour = stroke.getColor();
                            expStrokeWidth = stroke.getWidth();
                            expStrokeColourOpacity = stroke.getOpacity();
                        }
                    }
                    symbolTypeFactory.setValue(this.fieldConfigManager, symbol);
                }
            }
        }

        fieldConfigVisitor.populateField(FieldIdEnum.SIZE, expSize);
        fieldConfigVisitor.populateField(FieldIdEnum.ANGLE, expRotation);
        fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_H, expAnchorPointX);
        fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_V, expAnchorPointY);
        fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_X, expDisplacementX);
        fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_Y, expDisplacementY);
        fieldConfigVisitor.populateColourField(new FieldId(FieldIdEnum.FILL_COLOUR), expFillColour);
        fieldConfigVisitor.populateField(FieldIdEnum.OPACITY, expOpacity);
        fieldConfigVisitor.populateField(FieldIdEnum.GAP, expGap);
        fieldConfigVisitor.populateField(FieldIdEnum.INITIAL_GAP, expInitialGap);
        fieldConfigVisitor.populateColourField(new FieldId(FieldIdEnum.STROKE_FILL_COLOUR), expStrokeColour);
        fieldConfigVisitor.populateField(FieldIdEnum.STROKE_FILL_OPACITY, expStrokeColourOpacity);
        fieldConfigVisitor.populateField(FieldIdEnum.STROKE_WIDTH, expStrokeWidth);

        GroupConfigInterface anchorPointGroup = getGroup(GroupIdEnum.ANCHORPOINT);

        if(anchorPointGroup != null)
        {
            anchorPointGroup.enable(hasAnchorPoint);
        }

        GroupConfigInterface displacementGroup = getGroup(GroupIdEnum.DISPLACEMENT);

        if(displacementGroup != null)
        {
            displacementGroup.enable(hasDisplacement);
        }

        GroupConfigInterface fillGroup = getGroup(GroupIdEnum.FILL);

        if(fillGroup != null)
        {
            fillGroup.enable(hasFillColour);
        }

        GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.STROKE);

        if(strokeGroup != null)
        {
            strokeGroup.enable(hasStroke);
        }

        if(vendorOptionFillFactory != null)
        {
            vendorOptionFillFactory.populate(polygon);
        }

        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            Symbolizer symbolizer = SelectedSymbol.getInstance().getSymbolizer();

            if(symbolizer instanceof PointSymbolizer)
            {
                PointSymbolizerImpl newPointSymbolizer = (PointSymbolizerImpl) symbolizer;

                Graphic graphic = getGraphic();

                newPointSymbolizer.setGraphic(graphic);
            }
            else if(symbolizer instanceof PolygonSymbolizer)
            {
                PolygonSymbolizerImpl newPolygonSymbolizer = (PolygonSymbolizerImpl) symbolizer;

                Fill fill = symbolTypeFactory.getFill(getGraphicFill(), this.fieldConfigManager);

                newPolygonSymbolizer.setFill(fill);

                if(vendorOptionFillFactory != null)
                {
                    vendorOptionFillFactory.updateSymbol(newPolygonSymbolizer);
                }
            }

            this.fireUpdateSymbol();
        }
    }

    /**
     * Gets the graphic.
     *
     * @return the graphic
     */
    public Graphic getGraphic() {

        AnchorPoint anchor = null;
        Displacement displacement = null;
        Expression symbolType = fieldConfigVisitor.getExpression(FieldIdEnum.SYMBOL_TYPE);

        GroupConfigInterface fillGroup = getGroup(GroupIdEnum.FILL);
        boolean hasFill = fillGroup.isPanelEnabled();

        GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.STROKE);
        boolean hasStroke = strokeGroup.isPanelEnabled();

        Expression opacity = fieldConfigVisitor.getExpression(FieldIdEnum.OPACITY);
        Expression size = fieldConfigVisitor.getExpression(FieldIdEnum.SIZE);
        Expression rotation = fieldConfigVisitor.getExpression(FieldIdEnum.ANGLE);
        Expression gap = fieldConfigVisitor.getExpression(FieldIdEnum.GAP);
        Expression initialGap = fieldConfigVisitor.getExpression(FieldIdEnum.INITIAL_GAP);

        //
        // Anchor point
        //
        GroupConfigInterface anchorPointPanel = getGroup(GroupIdEnum.ANCHORPOINT);

        if(anchorPointPanel.isPanelEnabled())
        {
            anchor = (AnchorPoint) getStyleFactory().anchorPoint(fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_H),
                    fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_V));
        }

        //
        // Displacement
        //
        GroupConfigInterface displacementPanel = getGroup(GroupIdEnum.DISPLACEMENT);

        if(displacementPanel.isPanelEnabled())
        {
            displacement = getStyleFactory().displacement(fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_X),
                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_Y));
        }
        List<GraphicalSymbol> symbols = symbolTypeFactory.getValue(fieldConfigManager, symbolType, hasFill, hasStroke, selectedFillPanelId);

        Graphic graphic = getStyleFactory().graphic(symbols, opacity, size, rotation, anchor, displacement);

        graphic.setInitialGap(initialGap);
        graphic.setGap(gap);

        return graphic;
    }

    /**
     * Gets the graphic fill.
     *
     * @return the graphic fill
     */
    public GraphicFill getGraphicFill() {
        GroupConfigInterface fillGroup = getGroup(GroupIdEnum.FILL);
        boolean hasFill = fillGroup.isPanelEnabled();

        GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.STROKE);
        boolean hasStroke = strokeGroup.isPanelEnabled();

        Expression opacity = fieldConfigVisitor.getExpression(FieldIdEnum.OPACITY);
        Expression size = fieldConfigVisitor.getExpression(FieldIdEnum.SIZE);
        Expression rotation = fieldConfigVisitor.getExpression(FieldIdEnum.ANGLE);
        Expression symbolType = fieldConfigVisitor.getExpression(FieldIdEnum.SYMBOL_TYPE);

        //
        // Anchor point
        //
        AnchorPoint anchorPoint = null;
        GroupConfigInterface anchorPointPanel = getGroup(GroupIdEnum.ANCHORPOINT);

        if(anchorPointPanel.isPanelEnabled())
        {
            anchorPoint = (AnchorPoint) getStyleFactory().anchorPoint(fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_H),
                    fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_V));
        }

        //
        // Displacement
        //
        Displacement displacement = null;
        GroupConfigInterface displacementPanel = getGroup(GroupIdEnum.DISPLACEMENT);

        if(displacementPanel.isPanelEnabled())
        {
            displacement = getStyleFactory().displacement(fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_X),
                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_Y));
        }

        List<GraphicalSymbol> symbols = symbolTypeFactory.getValue(this.fieldConfigManager, symbolType, hasFill, hasStroke, selectedFillPanelId);

        GraphicFill graphicFill = getStyleFactory().graphicFill(symbols, opacity, size, rotation, anchorPoint, displacement);

        return graphicFill;
    }

    /**
     * Gets the fill colour.
     *
     * @return the fill colour
     */
    public Expression getFillColour() {
        return symbolTypeFactory.getFillColour();
    }

    /**
     * Gets the fill colour opacity.
     *
     * @return the fill colour opacity
     */
    public Expression getFillColourOpacity() {
        return symbolTypeFactory.getFillColourOpacity();
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

        FieldConfigBase fieldConfig = fieldConfigManager.get(FieldIdEnum.SIZE);
        if(fieldConfig.isEnabled())
        {
            Expression expression = fieldConfig.getExpression();

            if(expression instanceof LiteralExpressionImpl)
            {
                LiteralExpressionImpl l = (LiteralExpressionImpl) expression;
                Double d = (Double) l.getValue();
                if(d <= 0.0)
                {
                    fieldConfigVisitor.populateField(FieldIdEnum.SIZE, getFilterFactory().literal(1));
                }
            }
        }

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
            FieldId fieldId = fieldConfig.getFieldId();
            FieldIdEnum field = fieldId.getFieldId();
            if((field != FieldIdEnum.SYMBOL_TYPE) && (list != null))
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
        if(vendorOptionFillFactory != null)
        {
            vendorOptionFillFactory.getFieldDataManager(fieldConfigManager);
        }

        return fieldConfigManager;
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
        return SelectedSymbol.getInstance().hasFill();
    }

    /**
     * Vendor options updated.
     *
     * @param vendorOptionList the vendor option list
     */
    /* (non-Javadoc)
     * @see com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionList)
    {
        this.vendorOptionOptionsList = vendorOptionList;

        updateVendorOptionPanels();
    }

    /**
     * Update vendor option panels.
     */
    private void updateVendorOptionPanels()
    {
        if(vendorOptionFillFactory != null)
        {
            List<VendorOptionInterface> voList = vendorOptionFillFactory.getVendorOptionList();
            if(voList != null)
            {
                for(VendorOptionInterface extension : voList)
                {
                    boolean displayVendorOption = VendorOptionManager.getInstance().isAllowed(vendorOptionOptionsList, extension.getVendorOption());

                    BasePanel optionPanel = extension.getPanel();
                    if(optionPanel != null)
                    {
                        removePanel(optionPanel);

                        if(displayVendorOption)
                        {
                            appendPanel(optionPanel);
                        }
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
