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
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.config.DataSourceConfigPanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.iface.SymbolPanelInterface;
import com.sldeditor.ui.tree.SLDTree;

/**
 * The Class SLDEditorUIPanels contains references to the panels to display for:
 * - no symbols selected
 * - one symbol selected
 * - multiple symbols selected
 * 
 * Also contains references for the following panels:
 * - DataSourceConfigPanel
 * - Panel layout for the SLD viewing/editing component
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorUIPanels
{
    /** The Constant SINGLE_SYMBOL. */
    private static final String SINGLE_SYMBOL = "Single Symbol";

    /** The single symbol ui. */
    private SingleSymbolUI singleSymbolUI;

    /** The Constant MULTIPLE_SYMBOL. */
    private static final String MULTIPLE_SYMBOL = "Multiple Symbol";

    /** The multiple symbol ui. */
    private MultipleSymbolUI multipleSymbolUI;

    /** The Constant NO_SYMBOL. */
    private static final String NO_SYMBOL = "No Symbol";

    /** The no symbol ui. */
    private NoSymbolUI noSymbolUI;

    /** The ui map. */
    private Map<String, SymbolPanelInterface> uiMap = new HashMap<String, SymbolPanelInterface>();

    /** The panel data tab. */
    private JPanel panelDataTab = null;

    /** The outer panel data tab. */
    private JPanel outerPanelDataTab = null;

    /** The data source config. */
    private DataSourceConfigPanel dataSourceConfig = null;

    /**
     * Instantiates a new SLD editor ui manager.
     */
    public SLDEditorUIPanels()
    {
        singleSymbolUI = new SingleSymbolUI();
        uiMap.put(SINGLE_SYMBOL, singleSymbolUI);

        multipleSymbolUI = new MultipleSymbolUI();
        uiMap.put(MULTIPLE_SYMBOL, multipleSymbolUI);

        noSymbolUI = new NoSymbolUI();
        uiMap.put(NO_SYMBOL, noSymbolUI);
    }

    /**
     * Populate ui.
     *
     * @param noOfItems the no of items
     */
    public void populateUI(int noOfItems) {
        String selectedItem = null;

        if(noOfItems > 1)
        {
            selectedItem = MULTIPLE_SYMBOL;
        }
        else if(noOfItems == 1)
        {
            selectedItem = SINGLE_SYMBOL;
        }
        else
        {
            selectedItem = NO_SYMBOL;
        }

        SymbolPanelInterface panelUI = uiMap.get(selectedItem);

        if(panelUI != null)
        {
            CardLayout cl = (CardLayout)(panelDataTab.getLayout());
            cl.show(panelDataTab, selectedItem);

            panelUI.populate(SelectedSymbol.getInstance());
        }
    }

    /**
     * Create panel that contains the SLD symbol viewing/editing panels
     *
     * @return the panel
     */
    public JPanel getSLDSymbolData() {
        outerPanelDataTab = new JPanel();
        outerPanelDataTab.setLayout(new BorderLayout());

        panelDataTab = new JPanel(false);
        panelDataTab.setLayout(new CardLayout());

        for(String key : uiMap.keySet())
        {
            SymbolPanelInterface symbolPanel = uiMap.get(key);

            JPanel panel = new JPanel(new BorderLayout());

            if(symbolPanel.addNorthPanel() != null)
            {
                panel.add(symbolPanel.addNorthPanel(), BorderLayout.NORTH);
            }

            if(symbolPanel.addWestPanel() != null)
            {
                panel.add(symbolPanel.addWestPanel(), BorderLayout.WEST);
            }

            if(symbolPanel.addCentrePanel() != null)
            {
                panel.add(symbolPanel.addCentrePanel(), BorderLayout.CENTER);
            }
            panelDataTab.add(panel, key);
        }

        outerPanelDataTab.add(panelDataTab, BorderLayout.CENTER);

        return outerPanelDataTab;
    }

    /**
     * Gets the data source config panel, creates it if it hasn't been already
     *
     * @return the data source config
     */
    public DataSourceConfigPanel getDataSourceConfig() {
        if(dataSourceConfig  == null)
        {
            dataSourceConfig = new DataSourceConfigPanel();
        }

        return dataSourceConfig;
    }

    /**
     * Gets the symbol tree.
     *
     * @return the symbol tree
     */
    public SLDTree getSymbolTree()
    {
        return singleSymbolUI.getSymbolTree();
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return singleSymbolUI.getFieldDataManager();
    }

}
