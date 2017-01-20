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

import java.util.List;

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class ArrowDetails panel contains all the fields to configure 
 * an GeoServer vendor option arrow strings.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ArrowDetails extends StandardPanel implements PopulateDetailsInterface, 
UpdateSymbolInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/marker/arrow/PanelConfig_Arrow.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private ArrowUpdateInterface parentObj = null;

    /**
     * Instantiates a new feature type style details.
     *
     * @param parentObj the parent obj
     * @param functionManager the function manager
     */
    public ArrowDetails(ArrowUpdateInterface parentObj, FunctionNameInterface functionManager)
    {
        super(ArrowDetails.class, functionManager);

        this.parentObj = parentObj;
        createUI();
        revertToDefaultValue();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, getClass(), this, PANEL_CONFIG);
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
        // Do nothing
    }

    /**
     * Populate expression.
     *
     * @param wellKnownName the well known name
     */
    public void populateExpression(String wellKnownName) {

        Expression actual = ArrowUtils.decodeArrowThickness(wellKnownName);
        fieldConfigVisitor.populateField(FieldIdEnum.VO_ARROW_THICKNESS, actual);

        actual = ArrowUtils.decodeHeightOverWidth(wellKnownName);
        fieldConfigVisitor.populateField(FieldIdEnum.VO_ARROW_HEIGHT_OVER_WIDTH, actual);

        actual = ArrowUtils.decodeHeadBaseRatio(wellKnownName);
        fieldConfigVisitor.populateField(FieldIdEnum.VO_ARROW_HEAD, actual);
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged()
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            if(parentObj != null)
            {
                parentObj.arrowValueUpdated();
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
        return true;
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        Expression tValueExpression = fieldConfigVisitor.getExpression(FieldIdEnum.VO_ARROW_THICKNESS);
        Expression hrExpression = fieldConfigVisitor.getExpression(FieldIdEnum.VO_ARROW_HEIGHT_OVER_WIDTH);
        Expression abExpression = fieldConfigVisitor.getExpression(FieldIdEnum.VO_ARROW_HEAD);

        Expression expression = getFilterFactory().literal(ArrowUtils.encode(hrExpression, tValueExpression, abExpression));

        return expression;
    }

    /**
     * Revert to default value.
     */
    public void revertToDefaultValue() {
        List<FieldConfigBase> fieldList = fieldConfigManager.getFields(null);

        for(FieldConfigBase field : fieldList)
        {
            if(field != null)
            {
                field.revertToDefaultValue();
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

    /* (non-Javadoc)
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        for(FieldConfigBase fieldConfig : getFieldConfigList())
        {
            fieldConfig.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        // Handled by the FieldConfigArrow
    }
}
