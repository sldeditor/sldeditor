/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor;

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import java.net.URL;
import java.util.List;

/** @author ward_r */
public interface SLDEditorTestInterface {

    /**
     * Select SLD tree item, called by test framework.
     *
     * @param data the tree selection data
     * @return true, if successful
     */
    boolean selectTreeItem(TreeSelectionData data);

    /**
     * Gets the symbol panel.
     *
     * @return the symbol panel
     */
    PopulateDetailsInterface getSymbolPanel();

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    GraphicPanelFieldManager getFieldDataManager();

    /**
     * Sets the list of vendor options.
     *
     * @param vendorOptionList the list of vendor options
     */
    void setVendorOptions(List<VersionData> vendorOptionList);

    /**
     * Gets the SLD string.
     *
     * @return the SLD string
     */
    String getSLDString();

    /**
     * Open file.
     *
     * @param url the url
     */
    void openFile(URL url);
}
