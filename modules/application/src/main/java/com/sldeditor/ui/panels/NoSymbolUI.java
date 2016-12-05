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
package com.sldeditor.ui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.iface.SymbolPanelInterface;
import com.sldeditor.ui.tree.SLDTree;

/**
 * The Class NoSymbolUI is displayed when no symbol is selected.
 * 
 * @author Robert Ward (SCISYS)
 */
public class NoSymbolUI implements SymbolPanelInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#addWestPanel()
     */
    @Override
    public JPanel addWestPanel() {
        return createSymbolSelectionPanel();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#addCentrePanel()
     */
    @Override
    public JPanel addCentrePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel(Localisation.getString(NoSymbolUI.class, "NoSymbolUI.noSymbol"));
        label.setFont(new Font("Arial", Font.BOLD, 16));
        labelPanel.add(label);

        labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(labelPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the symbol selection panel.
     *
     * @return the j panel
     */
    public JPanel createSymbolSelectionPanel() {
        return null;
    }

    /**
     * Gets the symbol tree.
     *
     * @return the symbol tree
     */
    public SLDTree getSymbolTree() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#addNorthPanel()
     */
    @Override
    public JPanel addNorthPanel() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.SymbolPanelInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        // Does nothing
    }
}
