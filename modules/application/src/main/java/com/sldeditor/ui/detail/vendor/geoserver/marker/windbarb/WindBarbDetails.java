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
package com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb;

import java.util.List;

import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Symbolizer;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.attribute.AttributeUtils;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class WindBarbDetails panel contains all the fields to configure 
 * the GeoServer vendor option for displaying weather wind barbs.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WindBarbDetails extends StandardPanel implements PopulateDetailsInterface, 
UpdateSymbolInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/marker/windbarb/PanelConfig_WindBarbSymbol.xml";

    /** The Constant HEMISPHERE_S. */
    private static final String HEMISPHERE_S = "?hemisphere=s";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The wind barbs expression. */
    private Expression windBarbsExpression = null;

    /** The parent obj. */
    private WindBarbUpdateInterface parentObj = null;

    /**
     * Instantiates a new feature type style details.
     *
     * @param parentObj the parent obj
     * @param functionManager the function manager
     */
    public WindBarbDetails(WindBarbUpdateInterface parentObj, FunctionNameInterface functionManager)
    {
        super(WindBarbDetails.class, functionManager);

        this.parentObj = parentObj;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, this, PANEL_CONFIG);
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

        Symbolizer symbolizer = selectedSymbol.getSymbolizer();
        Expression wellKnownName = getWellKnownName(symbolizer);

        if(wellKnownName != null)
        {
            populateExpression(wellKnownName.toString());
        }
    }

    /**
     * Gets the well known name.
     *
     * @param symbolizer the symbolizer
     * @return the well known name
     */
    private Expression getWellKnownName(Symbolizer symbolizer) {
        Expression wellKnownName = null;
        if(symbolizer instanceof PointSymbolizerImpl)
        {
            PointSymbolizerImpl point = (PointSymbolizerImpl) symbolizer;

            List<GraphicalSymbol> graphicalSymbolList = point.getGraphic().graphicalSymbols();
            if((graphicalSymbolList != null) && !graphicalSymbolList.isEmpty())
            {
                GraphicalSymbol graphicalSymbol = graphicalSymbolList.get(0);

                if(graphicalSymbol instanceof MarkImpl)
                {
                    MarkImpl mark = (MarkImpl) graphicalSymbol;

                    wellKnownName = mark.getWellKnownName();
                }
            }
        }
        return wellKnownName;
    }

    /**
     * Populate expression.
     *
     * @param wellKnownName the well known name
     */
    public void populateExpression(String wellKnownName) {

        if(wellKnownName != null)
        {
            int startSpeedOpenBracket = wellKnownName.indexOf("(");
            int endSpeedCloseBracket = wellKnownName.indexOf(")");

            if((startSpeedOpenBracket < 0) || (endSpeedCloseBracket < 0))
            {
                // Invalid
                return;
            }
            String windSpeed = wellKnownName.substring(startSpeedOpenBracket + 1, endSpeedCloseBracket);

            int startUnitsOpenBracket = wellKnownName.indexOf("[");
            int endUnitsOpenBracket = wellKnownName.indexOf("]");
            if((startUnitsOpenBracket < 0) || (endUnitsOpenBracket < 0))
            {
                // Invalid
                return;
            }

            String windSpeedUnits = wellKnownName.substring(startUnitsOpenBracket + 1, endUnitsOpenBracket);

            Expression windSpeedExpression = null;
            if(AttributeUtils.isAttribute(windSpeed))
            {
                String propertyName = AttributeUtils.extract(windSpeed);
                windSpeedExpression = getFilterFactory().property(propertyName);
                DataSourceInterface dataSource = DataSourceFactory.getDataSource();

                dataSource.addField(new DataSourceAttributeData(propertyName, Double.class, null));
            }
            else
            {
                windSpeedExpression = getFilterFactory().literal(windSpeed);
            }
            boolean isNorthernHemisphere = !wellKnownName.endsWith(HEMISPHERE_S);
            fieldConfigVisitor.populateField(FieldIdEnum.WINDBARB_WINDSPEED, windSpeedExpression);
            fieldConfigVisitor.populateBooleanField(FieldIdEnum.WINDBARB_NORTHERN_HEMISPHERE, isNorthernHemisphere);
            fieldConfigVisitor.populateComboBoxField(FieldIdEnum.WINDBARB_WINDSPEED_UNITS, windSpeedUnits);
        }
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
            ValueComboBoxData windSpeedUnits = fieldConfigVisitor.getComboBox(FieldIdEnum.WINDBARB_WINDSPEED_UNITS);
            Expression windSpeedExpression = fieldConfigVisitor.getExpression(FieldIdEnum.WINDBARB_WINDSPEED);
            boolean inNorthernHemisphere = fieldConfigVisitor.getBoolean(FieldIdEnum.WINDBARB_NORTHERN_HEMISPHERE);
            Object windSpeed = null;

            if(windSpeedExpression == null)
            {
                windSpeed = Integer.valueOf(0);
            }
            else if(windSpeedExpression instanceof LiteralExpressionImpl)
            {
                LiteralExpressionImpl literalExpression = (LiteralExpressionImpl) windSpeedExpression;
                windSpeed = literalExpression.getValue();
            }
            else if(windSpeedExpression instanceof ConstantExpression)
            {
                ConstantExpression constantExpression = (ConstantExpression) windSpeedExpression;

                windSpeed = constantExpression.getValue();
            }
            else if(windSpeedExpression instanceof AttributeExpressionImpl)
            {
                AttributeExpressionImpl attributeExpression = (AttributeExpressionImpl) windSpeedExpression;

                windSpeed = String.format("<ogc:PropertyName>%s</ogc:PropertyName>",attributeExpression.getPropertyName());;
            }
            else
            {
                ConsoleManager.getInstance().error(this, Localisation.getField(WindBarbDetails.class, "WindBarb.windspeedError1") + windSpeedExpression.getClass().getName());
            }
            String url = String.format("windbarbs://default(%s)[%s]", windSpeed, windSpeedUnits.getKey());

            if(!inNorthernHemisphere)
            {
                url = url + HEMISPHERE_S;
            }
            windBarbsExpression = getFilterFactory().literal(url);

            if(parentObj != null)
            {
                parentObj.windBarbValueUpdated();
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
        if(windBarbsExpression == null)
        {
            if(!Controller.getInstance().isPopulating())
            {
                revertToDefaultValue();
            }

            updateSymbol();
        }
        return windBarbsExpression;
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

    /**
     * Sets the test value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        populateExpression(testValue);

        updateSymbol();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        // Do nothing
    }

}
