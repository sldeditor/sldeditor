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

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import com.sldeditor.ui.tree.SLDTree;
import java.net.URL;
import java.util.List;

/**
 * Class that allows SLDEditor to be run in the integration tests.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorExternal implements SLDEditorTestInterface {

    /** The SLDEditor main object. */
    private SLDEditorMain main = null;

    /**
     * Instantiates a new SLD editor external.
     *
     * @param main the main
     */
    public SLDEditorExternal(SLDEditorMain main) {
        this.main = main;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.SLDEditorTestInterface#openFile(java.net.URL)
     */
    @Override
    public void openFile(URL url) {
        main.openFile(url);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.SLDEditorTestInterface#selectTreeItem(com.sldeditor.TreeSelectionData)
     */
    @Override
    public boolean selectTreeItem(TreeSelectionData data) {
        SLDTree sldTree = SLDEditorUIPanels.getInstance().getSymbolTree();

        if (sldTree == null) {
            return false;
        }

        return sldTree.selectTreeItem(data);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.SLDEditorTestInterface#getSymbolPanel()
     */
    @Override
    public PopulateDetailsInterface getSymbolPanel() {
        SLDTree sldTree = SLDEditorUIPanels.getInstance().getSymbolTree();

        return sldTree.getSelectedSymbolPanel();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.SLDEditorTestInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return SLDEditorUIPanels.getInstance().getFieldDataManager();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.SLDEditorTestInterface#setVendorOptions(java.util.List)
     */
    @Override
    public void setVendorOptions(List<VersionData> vendorOptionList) {
        VendorOptionManager.getInstance().overrideSelectedVendorOptions(vendorOptionList);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.SLDEditorTestInterface#getSLDString()
     */
    @Override
    public String getSLDString() {
        return main.getSLDString();
    }
}
