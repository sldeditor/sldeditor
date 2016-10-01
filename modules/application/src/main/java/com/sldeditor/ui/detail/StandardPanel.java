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

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.text.Text;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Rule;
import org.opengis.util.InternationalString;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;

/**
 * The Class StandardPanel responsible for populating/extracting
 *  standard data - name, description, unit of measure.
 * 
 * @author Robert Ward (SCISYS)
 */
public class StandardPanel extends BasePanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Internal class containing SLD common data.
     */
    protected class StandardData
    {
        /** The name. */
        public String name;

        /** The description. */
        public Description description = null;

        /** The unit. */
        public Unit<Length> unit = null;
    }

    /**
     * Constructor.
     *
     * @param panelId the panel id
     * @param functionManager the function manager
     */
    protected StandardPanel(Class<?> panelId, FunctionNameInterface functionManager) {
        super(panelId, functionManager);
    }

    /**
     * Populate standard data.
     *
     * @param style the style
     */
    protected void populateStandardData(Style style)
    {
        StandardData standardData = new StandardData();

        if(style != null)
        {
            standardData.name = style.getName();
            standardData.description = style.getDescription();
        }

        populateStandardData(standardData);
    }

    /**
     * Populate standard data.
     *
     * @param rule the rule
     */
    protected void populateStandardData(Rule rule)
    {
        StandardData standardData = new StandardData();

        if(rule != null)
        {
            standardData.name = rule.getName();
            standardData.description = (Description) rule.getDescription();
        }

        populateStandardData(standardData);
    }

    /**
     * Populate standard data.
     *
     * @param featureTypeStyle the feature type style
     */
    protected void populateStandardData(FeatureTypeStyle featureTypeStyle) {
        StandardData standardData = new StandardData();

        if(featureTypeStyle != null)
        {
            standardData.name = featureTypeStyle.getName();
            standardData.description = featureTypeStyle.getDescription();
        }

        populateStandardData(standardData);
    }

    /**
     * Populate standard data.
     *
     * @param standardData the standard data
     */
    private void populateStandardData(StandardData standardData)
    {
        Description description = standardData.description;
        String titleString = "";
        String descriptionString = "";
        if(description != null)
        {
            InternationalString title = description.getTitle();

            if(title != null)
            {
                titleString = title.toString();
            }

            InternationalString abstractDesc = description.getAbstract();

            if(abstractDesc != null)
            {
                descriptionString = abstractDesc.toString();
            }
        }
        fieldConfigVisitor.populateTextField(FieldIdEnum.NAME, standardData.name);
        fieldConfigVisitor.populateTextField(FieldIdEnum.TITLE, titleString);
        fieldConfigVisitor.populateTextField(FieldIdEnum.DESCRIPTION, descriptionString);

        FieldConfigBase uomFieldConfig = fieldConfigManager.get(FieldIdEnum.UOM);
        if(uomFieldConfig != null)
        {
            uomFieldConfig.updateAttributeSelection(SelectedSymbol.getInstance().isRasterSymbol());
            String uomString = UnitsOfMeasure.getInstance().convert(standardData.unit);
            fieldConfigVisitor.populateField(FieldIdEnum.UOM, getFilterFactory().literal(uomString));
        }
    }

    /**
     * Populate standard data.
     *
     * @param symbolizer the symbolizer
     */
    protected void populateStandardData(Symbolizer symbolizer) {
        StandardData standardData = new StandardData();

        if(symbolizer != null)
        {
            standardData.name = symbolizer.getName();
            standardData.description = symbolizer.getDescription();
            standardData.unit = symbolizer.getUnitOfMeasure();
        }

        populateStandardData(standardData);
    }

    /**
     * Gets the standard data.
     *
     * @return the standard data
     */
    protected StandardData getStandardData() {
        StandardData standardData = new StandardData();

        standardData.name = fieldConfigVisitor.getText(FieldIdEnum.NAME);

        InternationalString titleString = Text.text(fieldConfigVisitor.getText(FieldIdEnum.TITLE));
        InternationalString descriptionString = Text.text(fieldConfigVisitor.getText(FieldIdEnum.DESCRIPTION));

        standardData.description = (Description) getStyleFactory().description(titleString, descriptionString);

        FieldConfigPopulate uomFieldConfig = fieldConfigManager.get(FieldIdEnum.UOM);
        if(uomFieldConfig != null)
        {
            Expression uomExpression = fieldConfigVisitor.getExpression(FieldIdEnum.UOM);

            String uomString = "";
            if(uomExpression instanceof LiteralExpressionImpl)
            {
                uomString = (String)((LiteralExpressionImpl)uomExpression).getValue();
            }
            else
            {
                if(uomExpression != null)
                {
                    ConsoleManager.getInstance().error(this, Localisation.getString(StandardPanel.class, "StandardPanel.unsupportedUOM") + uomExpression.getClass().getName());
                }
            }
            standardData.unit = UnitsOfMeasure.getInstance().convert(uomString);
        }

        return standardData;
    }
}
