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

import org.geotools.filter.FunctionExpression;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class StyleDetails allows a user to configure style data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FunctionDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param functionManager the function manager
     * @param type the type
     */
    public FunctionDetails(FunctionNameInterface functionManager, Class<?> type)
    {
        super(FunctionDetails.class, functionManager);
        createUI(type);
    }

    /**
     * Creates the ui.
     *
     * @param fieldType the field type
     */
    private void createUI(Class<?> fieldType) {

        readConfigFile(this, "Function.xml", fieldType);
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldId changedField) {
        FieldConfigBase fieldConfig = fieldConfigVisitor.getFieldConfig(changedField);

        Expression expression = fieldConfig.getExpression();

        if(expression instanceof FunctionExpression)
        {
            FunctionExpression functionExpression = (FunctionExpression) expression;

            this.insertGroupConfig(getClass(),
                    this,
                    functionExpression,
                    fieldConfig,
                    true);
        }

        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            FieldConfigBase fieldConfig = fieldConfigVisitor.getFieldConfig(new FieldId(FieldIdEnum.FUNCTION));

            Expression functionString = fieldConfig.getExpression();

            if(functionString != null)
            {
                System.out.println(functionString.toString());
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
}
