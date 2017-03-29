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
import org.geotools.styling.Displacement;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Symbolizer;

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
 * The Class PointFillDetails allows a user to configure point fill data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PointFillDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface, MultiOptionSelectedInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant configFile. */
    private static final String configFile = "PointFill.xml";

    /** The symbol type factory. */
    private SymbolTypeFactory symbolTypeFactory = null;

    /** The field override map, indicates which fields to enable. */
    private FieldEnableState fieldEnableState = null;

    /** The panel id of the selected fill. */
    private Class<?> selectedFillPanelId = null;

    /** The vendor option fill factory. */
    private VendorOptionFillFactory vendorOptionFillFactory = null;

    /**
     * Constructor.
     */
    public PointFillDetails() {
        super(PointFillDetails.class);

        setUpdateSymbolListener(this);

        symbolTypeFactory = new SymbolTypeFactory(PointFillDetails.class,
                new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR,
                        FieldIdEnum.POINT_FILL_OPACITY, FieldIdEnum.STROKE_WIDTH),
                new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_FILL_COLOUR,
                        FieldIdEnum.POINT_STROKE_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH),
                FieldIdEnum.SYMBOL_TYPE);

        fieldEnableState = symbolTypeFactory.getFieldOverrides(PointFillDetails.class);

        createUI(PointFillDetails.class, configFile);
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
        Expression expGap = null;
        Expression expInitialGap = null;

        PointSymbolizer pointSymbolizer = null;

        if (selectedSymbol != null) {
            Symbolizer symbolizer = selectedSymbol.getSymbolizer();

            if (symbolizer instanceof PointSymbolizerImpl) {
                pointSymbolizer = (PointSymbolizer) symbolizer;
                Graphic graphic = pointSymbolizer.getGraphic();

                if (graphic != null) {
                    expSize = graphic.getSize();
                    expRotation = graphic.getRotation();

                    // Anchor point
                    AnchorPoint anchorPoint = graphic.getAnchorPoint();

                    if (anchorPoint != null) {
                        expAnchorPointX = anchorPoint.getAnchorPointX();
                        expAnchorPointY = anchorPoint.getAnchorPointY();
                    } else {
                        expAnchorPointX = AnchorPoint.DEFAULT.getAnchorPointX();
                        expAnchorPointY = AnchorPoint.DEFAULT.getAnchorPointY();
                    }

                    // Offset
                    Displacement displacement = graphic.getDisplacement();

                    if (displacement != null) {
                        expDisplacementX = displacement.getDisplacementX();
                        expDisplacementY = displacement.getDisplacementY();
                    } else {
                        expDisplacementX = Displacement.DEFAULT.getDisplacementX();
                        expDisplacementY = Displacement.DEFAULT.getDisplacementY();
                    }

                    expGap = graphic.getGap();
                    expInitialGap = graphic.getInitialGap();

                    List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();

                    if (!graphicalSymbolList.isEmpty()) {
                        GraphicalSymbol symbol = graphicalSymbolList.get(0);
                        symbolTypeFactory.setValue(PointSymbolizer.class, this.fieldConfigManager,
                                graphic, symbol);
                    }
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

        if (vendorOptionFillFactory != null) {
            vendorOptionFillFactory.populate(pointSymbolizer);
        }

        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            Symbolizer symbolizer = SelectedSymbol.getInstance().getSymbolizer();

            if (symbolizer instanceof PointSymbolizer) {
                PointSymbolizerImpl newPointSymbolizer = (PointSymbolizerImpl) symbolizer;

                Graphic graphic = getGraphic();

                newPointSymbolizer.setGraphic(graphic);
            }

            this.fireUpdateSymbol();
        }
    }

    /**
     * Gets the graphic.
     *
     * @return the graphic
     */
    private Graphic getGraphic() {

        AnchorPoint anchor = null;
        Displacement displacement = null;
        Expression symbolType = fieldConfigVisitor.getExpression(FieldIdEnum.SYMBOL_TYPE);

        GroupConfigInterface fillGroup = getGroup(GroupIdEnum.FILL);
        boolean hasFill = fillGroup.isPanelEnabled();

        GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.STROKE);
        boolean hasStroke = (strokeGroup == null) ? false : strokeGroup.isPanelEnabled();

        Expression opacity = fieldConfigVisitor.getExpression(FieldIdEnum.OVERALL_OPACITY);
        Expression size = fieldConfigVisitor.getExpression(FieldIdEnum.SIZE);
        Expression rotation = fieldConfigVisitor.getExpression(FieldIdEnum.ANGLE);

        //
        // Anchor point
        //
        GroupConfigInterface anchorPointPanel = getGroup(GroupIdEnum.ANCHORPOINT);

        if (anchorPointPanel.isPanelEnabled()) {
            anchor = (AnchorPoint) getStyleFactory().anchorPoint(
                    fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_H),
                    fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_V));

            // Ignore the anchor point if it is the same as the default
            // so it doesn't appear in the SLD
            if (DetailsUtilities.isSame(AnchorPoint.DEFAULT, anchor)) {
                anchor = null;
            }
        }

        //
        // Displacement
        //
        GroupConfigInterface displacementPanel = getGroup(GroupIdEnum.DISPLACEMENT);

        if (displacementPanel.isPanelEnabled()) {
            displacement = getStyleFactory().displacement(
                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_X),
                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_Y));

            // Ignore the displacement if it is the same as the default so 
            // it doesn't appear in the SLD
            if (DetailsUtilities.isSame(Displacement.DEFAULT, displacement)) {
                displacement = null;
            }
        }

        List<GraphicalSymbol> symbols = symbolTypeFactory.getValue(fieldConfigManager, symbolType,
                hasFill, hasStroke, selectedFillPanelId);

        boolean overallOpacity = symbolTypeFactory.isOverallOpacity(PointSymbolizer.class,
                selectedFillPanelId);

        Graphic graphic = getStyleFactory().graphic(symbols, null, size, rotation, anchor,
                displacement);

        Expression gap = fieldConfigVisitor.getExpression(FieldIdEnum.GAP);
        Expression initialGap = fieldConfigVisitor.getExpression(FieldIdEnum.INITIAL_GAP);

        graphic.setInitialGap(initialGap);
        graphic.setGap(gap);

        if (overallOpacity) {
            graphic.setOpacity(opacity);
        }
        return graphic;
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
