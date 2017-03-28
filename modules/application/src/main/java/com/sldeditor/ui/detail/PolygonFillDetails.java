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

import java.util.List;
import java.util.Map;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.AnchorPointImpl;
import org.geotools.styling.Displacement;
import org.geotools.styling.DisplacementImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.base.CurrentFieldState;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeFactory;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.fill.VendorOptionFillFactory;
import com.sldeditor.ui.iface.MultiOptionSelectedInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class PolygonFillDetails.
 *
 * @author Robert Ward (SCISYS)
 */
public class PolygonFillDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface, MultiOptionSelectedInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant configFile. */
    private static final String configFile = "PolygonFill.xml";

    /** The symbol type factory. */
    private SymbolTypeFactory symbolTypeFactory = null;

    /** The field override map, indicates which fields to enable. */
    private FieldEnableState fieldEnableState = null;

    /** The panel id of the selected fill. */
    private Class<?> selectedFillPanelId = null;

    /** The vendor option fill factory. */
    private VendorOptionFillFactory vendorOptionFillFactory = null;

    /** The default anchor point. */
    private static AnchorPoint defaultAnchorPoint = AnchorPointImpl.DEFAULT;

    /** The default displacement. */
    private static Displacement defaultDisplacement = DisplacementImpl.DEFAULT;

    /** The symbolizer. */
    private Symbolizer symbolizer = null;

    /**
     * Constructor.
     */
    public PolygonFillDetails() {
        super(PolygonFillDetails.class);

        setUpdateSymbolListener(this);

        symbolTypeFactory = new SymbolTypeFactory(PolygonFillDetails.class,
                new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR,
                        FieldIdEnum.POLYGON_FILL_OPACITY, FieldIdEnum.STROKE_WIDTH),
                new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_FILL_COLOUR,
                        FieldIdEnum.POLYGON_STROKE_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH),
                FieldIdEnum.SYMBOL_TYPE);

        fieldEnableState = symbolTypeFactory.getFieldOverrides(PolygonFillDetails.class);

        createUI(PolygonFillDetails.class, configFile);
    }

    /**
     * Creates the ui.
     *
     * @param panelDetails the panel details the configuration is for
     * @param configFile the config file
     */
    private void createUI(Class<?> panelDetails, String configFile) {

        createVendorOptionPanel();

        readConfigFile(vendorOptionFillFactory, getClass(), this, configFile);

        symbolTypeFactory.populate(this, fieldConfigManager);
    }

    /**
     * Creates the vendor option panel.
     *
     * @return the detail panel
     */
    private void createVendorOptionPanel() {

        vendorOptionFillFactory = new VendorOptionFillFactory(getPanelId());

        List<VendorOptionInterface> voList = vendorOptionFillFactory.getVendorOptionList();
        if (voList != null) {
            for (VendorOptionInterface vendorOption : voList) {
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
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /*
     * (non-Javadoc)
     * 
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
        Expression expGap = null;
        Expression expInitialGap = null;

        Fill fill = null;
        Expression expOpacity = null;

        symbolizer = null;
        if (selectedSymbol != null) {
            symbolizer = selectedSymbol.getSymbolizer();

            Graphic graphic = null;
            if (symbolizer instanceof PointSymbolizerImpl) {
                PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer;
                graphic = pointSymbolizer.getGraphic();
                if (graphic != null) {
                    List<GraphicalSymbol> graphicSymbolList = graphic.graphicalSymbols();
                    if (!graphicSymbolList.isEmpty()) {
                        GraphicalSymbol graphicalSymbol = graphicSymbolList.get(0);
                        if (graphicalSymbol instanceof MarkImpl) {
                            MarkImpl mark = (MarkImpl) graphicalSymbol;
                            fill = mark.getFill();
                            if (fill != null) {
                                expOpacity = fill.getOpacity();
                            }
                        }
                    }
                }
            } else if (symbolizer instanceof PolygonSymbolizerImpl) {
                PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) symbolizer;
                if (polygonSymbolizer != null) {
                    fill = polygonSymbolizer.getFill();

                    if (fill != null) {
                        expOpacity = fill.getOpacity();
                        graphic = fill.getGraphicFill();
                    }
                }
            }

            if (graphic == null) {
                if (fill != null) {
                    expFillColour = fill.getColor();
                }

                if (fill == null) {
                    symbolTypeFactory.setNoFill(this.fieldConfigManager);
                } else {
                    symbolTypeFactory.setSolidFill(this.fieldConfigManager, expFillColour,
                            expOpacity);
                }
            } else {
                expSize = graphic.getSize();
                expRotation = graphic.getRotation();

                // Anchor point
                AnchorPoint anchorPoint = graphic.getAnchorPoint();

                if (anchorPoint != null) {
                    expAnchorPointX = anchorPoint.getAnchorPointX();
                    expAnchorPointY = anchorPoint.getAnchorPointY();
                } else {
                    expAnchorPointX = defaultAnchorPoint.getAnchorPointX();
                    expAnchorPointY = defaultAnchorPoint.getAnchorPointY();
                }

                // Offset
                Displacement displacement = graphic.getDisplacement();

                if (displacement != null) {
                    expDisplacementX = displacement.getDisplacementX();
                    expDisplacementY = displacement.getDisplacementY();
                } else {
                    expDisplacementX = defaultDisplacement.getDisplacementX();
                    expDisplacementY = defaultDisplacement.getDisplacementY();
                }

                expGap = graphic.getGap();
                expInitialGap = graphic.getInitialGap();

                List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();

                if (!graphicalSymbolList.isEmpty()) {
                    GraphicalSymbol symbol = graphicalSymbolList.get(0);
                    symbolTypeFactory.setValue(PolygonSymbolizer.class, this.fieldConfigManager,
                            graphic, symbol);
                }
            }
        }

        fieldConfigVisitor.populateField(FieldIdEnum.SIZE, expSize);
        fieldConfigVisitor.populateField(FieldIdEnum.ANGLE, expRotation);
        fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_H, expAnchorPointX);
        fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_V, expAnchorPointY);
        fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_X, expDisplacementX);
        fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_Y, expDisplacementY);
        fieldConfigVisitor.populateField(FieldIdEnum.GAP, expGap);
        fieldConfigVisitor.populateField(FieldIdEnum.INITIAL_GAP, expInitialGap);
        fieldConfigVisitor.populateField(FieldIdEnum.OVERALL_OPACITY, expOpacity);

        if (vendorOptionFillFactory != null) {
            if (symbolizer instanceof PolygonSymbolizer) {
                vendorOptionFillFactory.populate((PolygonSymbolizer) symbolizer);
            }
        }

        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            if (symbolizer instanceof PolygonSymbolizer) {
                PolygonSymbolizerImpl newPolygonSymbolizer = (PolygonSymbolizerImpl) symbolizer;

                GraphicFill graphicFill = getGraphicFill();
                Fill fill = symbolTypeFactory.getFill(graphicFill, this.fieldConfigManager);

                Expression expOpacity = fieldConfigVisitor
                        .getExpression(FieldIdEnum.OVERALL_OPACITY);

                // If field is not enabled it returns null
                if ((fill != null) && (expOpacity != null)) {
                    fill.setOpacity(expOpacity);
                }

                newPolygonSymbolizer.setFill(fill);

                if (vendorOptionFillFactory != null) {
                    vendorOptionFillFactory.updateSymbol(newPolygonSymbolizer);
                }
            }

            this.fireUpdateSymbol();
        }
    }

    /**
     * Gets the graphic fill.
     *
     * @return the graphic fill
     */
    private GraphicFill getGraphicFill() {
        GroupConfigInterface fillGroup = getGroup(GroupIdEnum.FILL);
        boolean hasFill = fillGroup.isPanelEnabled();

        GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.STROKE);
        boolean hasStroke = (strokeGroup == null) ? false : strokeGroup.isPanelEnabled();

        Expression opacity = null;
        Expression size = fieldConfigVisitor.getExpression(FieldIdEnum.SIZE);
        Expression rotation = fieldConfigVisitor.getExpression(FieldIdEnum.ANGLE);
        Expression symbolType = fieldConfigVisitor.getExpression(FieldIdEnum.SYMBOL_TYPE);

        //
        // Anchor point
        //
        AnchorPoint anchorPoint = null;
        GroupConfigInterface anchorPointPanel = getGroup(GroupIdEnum.ANCHORPOINT);

        if (anchorPointPanel.isPanelEnabled()) {
            anchorPoint = (AnchorPoint) getStyleFactory().anchorPoint(
                    fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_H),
                    fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_V));

            // Ignore the anchor point if it is the same as the default so it doesn't appear in the SLD
            if (DetailsUtilities.isSame(defaultAnchorPoint, anchorPoint)) {
                anchorPoint = null;
            }
        }

        //
        // Displacement
        //
        Displacement displacement = null;
        GroupConfigInterface displacementPanel = getGroup(GroupIdEnum.DISPLACEMENT);

        if (displacementPanel.isPanelEnabled()) {
            displacement = getStyleFactory().displacement(
                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_X),
                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_Y));

            // Ignore the displacement if it is the same as the default so it doesn't appear in the SLD
            if (DetailsUtilities.isSame(defaultDisplacement, displacement)) {
                displacement = null;
            }
        }

        List<GraphicalSymbol> symbols = symbolTypeFactory.getValue(this.fieldConfigManager,
                symbolType, hasFill, hasStroke, selectedFillPanelId);

        GraphicFill graphicFill = getStyleFactory().graphicFill(symbols, opacity, size, rotation,
                anchorPoint, displacement);

        return graphicFill;
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

        FieldConfigBase fieldConfig = fieldConfigManager.get(FieldIdEnum.SIZE);
        if (fieldConfig.isEnabled()) {
            Expression expression = fieldConfig.getExpression();

            if (expression instanceof LiteralExpressionImpl) {
                LiteralExpressionImpl l = (LiteralExpressionImpl) expression;
                Double d = (Double) l.getValue();
                if (d <= 0.0) {
                    fieldConfigVisitor.populateField(FieldIdEnum.SIZE,
                            getFilterFactory().literal(1));
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
    private void setSymbolTypeVisibility(Class<?> panelId, String selectedItem) {
        Map<GroupIdEnum, Boolean> groupList = fieldEnableState.getGroupIdList(panelId.getName(),
                selectedItem);

        for (GroupIdEnum groupId : groupList.keySet()) {
            boolean groupEnabled = groupList.get(groupId);
            GroupConfigInterface groupConfig = fieldConfigManager.getGroup(this.getClass(),
                    groupId);
            if (groupConfig != null) {
                groupConfig.setGroupStateOverride(groupEnabled);
            } else {
                ConsoleManager.getInstance().error(this,
                        "Failed to find group : " + groupId.toString());
            }
        }

        Map<FieldIdEnum, Boolean> fieldList = fieldEnableState.getFieldIdList(panelId.getName(),
                selectedItem);

        for (FieldIdEnum fieldId : fieldList.keySet()) {
            boolean fieldEnabled = fieldList.get(fieldId);
            FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
            if (fieldConfig != null) {
                CurrentFieldState fieldState = fieldConfig.getFieldState();
                fieldState.setFieldEnabled(fieldEnabled);
                fieldConfig.setFieldState(fieldState);
            } else {
                ConsoleManager.getInstance().error(this,
                        "Failed to find field : " + fieldId.toString());
            }
        }
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        if (vendorOptionFillFactory != null) {
            vendorOptionFillFactory.getFieldDataManager(fieldConfigManager);
        }

        return fieldConfigManager;
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent() {
        return SelectedSymbol.getInstance().hasFill();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        symbolTypeFactory.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
        vendorOptionFillFactory.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
    }
}
