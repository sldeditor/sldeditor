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
package com.sldeditor.ui.iface;

import java.util.List;

import javax.swing.JPanel;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;

/**
 * The Interface SymbolPanelInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface SymbolPanelInterface {

    /**
     * Adds the north panel.
     *
     * @return the j panel
     */
    public abstract JPanel addNorthPanel();

    /**
     * Adds the west panel.
     *
     * @return the j panel
     */
    public abstract JPanel addWestPanel();

    /**
     * Adds the centre panel.
     *
     * @return the j panel
     */
    public abstract JPanel addCentrePanel();

    /**
     * Populate the selected symbol in the panels
     *
     * @param selectedSymbol the selected symbol
     */
    public abstract void populate(SelectedSymbol selectedSymbol);

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    public abstract GraphicPanelFieldManager getFieldDataManager();

    /**
     * Gets the minimum version vendor option present in the SLD.
     *
     * @param parentObj the parent obj
     * @param sldObj the sld obj
     * @param vendorOptionsPresentList the vendor options present list
     * @return the minimum version
     */
    void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList);

}
