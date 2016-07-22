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

import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class UserLayerDetails allows a user to configure user layer data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class UserLayerDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public UserLayerDetails(FunctionNameInterface functionManager)
    {
        super(UserLayerDetails.class, functionManager);

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFile(this, "UserLayer.xml");
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if(selectedSymbol != null)
        {
            StyledLayer styledLayer = selectedSymbol.getStyledLayer();
            if(styledLayer instanceof UserLayerImpl)
            {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;

                fieldConfigVisitor.populateTextField(FieldIdEnum.NAME, userLayer.getName());
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged()
     */
    @Override
    public void dataChanged(FieldId changedField) {
        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            String name = fieldConfigVisitor.getText(new FieldId(FieldIdEnum.NAME));
            UserLayer userLayer = getStyleFactory().createUserLayer();
            userLayer.setName(name);

            StyledLayer existingStyledLayer = SelectedSymbol.getInstance().getStyledLayer();
            if(existingStyledLayer instanceof NamedLayerImpl)
            {
                NamedLayerImpl existingNamedLayer = (NamedLayerImpl) existingStyledLayer;

                for(Style style : existingNamedLayer.styles())
                {
                    userLayer.addUserStyle(style);
                }
            }
            SelectedSymbol.getInstance().replaceStyledLayer(userLayer);

            this.fireUpdateSymbol();
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return fieldConfigManager;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
