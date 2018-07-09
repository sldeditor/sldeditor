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

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.vendoroption.minversion.VendorOptionUI;
import com.sldeditor.datasource.config.DataSourceConfigPanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.iface.SymbolPanelInterface;
import com.sldeditor.ui.tree.SLDTree;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * The Class SLDEditorUIPanels contains references to the panels to display for.:
 *
 * <p>- no symbols selected
 *
 * <p>- one symbol selected
 *
 * <p>- multiple symbols selected
 *
 * <p>Also contains references for the following panels:
 *
 * <p>- DataSourceConfigPanel
 *
 * <p>- Panel layout for the SLD viewing/editing component
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorUIPanels implements GetMinimumVersionInterface {
    /** The Constant SINGLE_SYMBOL. */
    private static final String SINGLE_SYMBOL = "Single Symbol";

    /** The single symbol ui. */
    private SingleSymbolUI singleSymbolUI;

    /** The single symbol ui. */
    private SingleLegendUI singleLegendUI;

    /** The Constant MULTIPLE_SYMBOL. */
    private static final String MULTIPLE_SYMBOL = "Multiple Symbol";

    /** The multiple symbol ui. */
    private MultipleSymbolUI multipleSymbolUI;

    /** The Constant NO_SYMBOL. */
    private static final String NO_SYMBOL = "No Symbol";

    /** The no symbol ui. */
    private NoSymbolUI noSymbolUI;

    /** The ui symbol map. */
    private Map<String, SymbolPanelInterface> uiSymbolMap =
            new HashMap<String, SymbolPanelInterface>();

    /** The ui legend map. */
    private Map<String, SymbolPanelInterface> uiLegendMap =
            new HashMap<String, SymbolPanelInterface>();

    /** The panel data tab. */
    private JPanel panelDataTab = null;

    /** The outer panel data tab. */
    private JPanel outerPanelDataTab = null;

    /** The outer panel legend tab. */
    private JPanel outerPanelLegendTab = null;

    /** The panel legend tab. */
    private JPanel panelLegendTab = null;

    /** The data source config. */
    private DataSourceConfigPanel dataSourceConfig = null;

    /** The vendor option UI. */
    private VendorOptionUI vendorOptionUI = null;

    /** Instantiates a new SLD editor ui manager. */
    public SLDEditorUIPanels() {
        singleSymbolUI = new SingleSymbolUI();
        uiSymbolMap.put(SINGLE_SYMBOL, singleSymbolUI);

        multipleSymbolUI = new MultipleSymbolUI();
        uiSymbolMap.put(MULTIPLE_SYMBOL, multipleSymbolUI);

        noSymbolUI = new NoSymbolUI();
        uiSymbolMap.put(NO_SYMBOL, noSymbolUI);

        // Legend
        singleLegendUI = new SingleLegendUI();
        uiLegendMap.put(SINGLE_SYMBOL, singleLegendUI);

        multipleSymbolUI = new MultipleSymbolUI();
        uiLegendMap.put(MULTIPLE_SYMBOL, multipleSymbolUI);

        noSymbolUI = new NoSymbolUI();
        uiLegendMap.put(NO_SYMBOL, noSymbolUI);
    }

    /**
     * Populate ui.
     *
     * @param noOfItems the no of items
     */
    public void populateUI(int noOfItems) {
        String selectedItem = null;

        if (noOfItems > 1) {
            selectedItem = MULTIPLE_SYMBOL;
        } else if (noOfItems == 1) {
            selectedItem = SINGLE_SYMBOL;
        } else {
            selectedItem = NO_SYMBOL;
        }

        // Symbol panel
        SymbolPanelInterface symbolPanelUI = uiSymbolMap.get(selectedItem);

        if (symbolPanelUI != null) {
            CardLayout cl = (CardLayout) (panelDataTab.getLayout());
            cl.show(panelDataTab, selectedItem);

            symbolPanelUI.populate(SelectedSymbol.getInstance());
        }

        // Legend panel
        SymbolPanelInterface legendPanelUI = uiLegendMap.get(selectedItem);

        if (legendPanelUI != null) {
            CardLayout cl = (CardLayout) (panelLegendTab.getLayout());
            cl.show(panelLegendTab, selectedItem);

            legendPanelUI.populate(SelectedSymbol.getInstance());
        }

        vendorOptionUI.populate(SelectedSymbol.getInstance());
    }

    /**
     * Create panel that contains the SLD symbol viewing/editing panels.
     *
     * @return the panel
     */
    public JPanel getSLDSymbolData() {
        outerPanelDataTab = new JPanel();
        outerPanelDataTab.setLayout(new BorderLayout());

        panelDataTab = new JPanel(false);
        panelDataTab.setLayout(new CardLayout());

        for (String key : uiSymbolMap.keySet()) {
            SymbolPanelInterface symbolPanel = uiSymbolMap.get(key);

            JPanel panel = new JPanel(new BorderLayout());

            if (symbolPanel.addNorthPanel() != null) {
                panel.add(symbolPanel.addNorthPanel(), BorderLayout.NORTH);
            }

            if (symbolPanel.addWestPanel() != null) {
                panel.add(symbolPanel.addWestPanel(), BorderLayout.WEST);
            }

            if (symbolPanel.addCentrePanel() != null) {
                panel.add(symbolPanel.addCentrePanel(), BorderLayout.CENTER);
            }
            panelDataTab.add(panel, key);
        }

        outerPanelDataTab.add(panelDataTab, BorderLayout.CENTER);

        return outerPanelDataTab;
    }

    /**
     * Gets the data source config panel, creates it if it hasn't been already.
     *
     * @return the data source config
     */
    public DataSourceConfigPanel getDataSourceConfig() {
        if (dataSourceConfig == null) {
            dataSourceConfig = new DataSourceConfigPanel();
        }

        return dataSourceConfig;
    }

    /**
     * Gets the symbol tree.
     *
     * @return the symbol tree
     */
    public SLDTree getSymbolTree() {
        return singleSymbolUI.getSymbolTree();
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    public GraphicPanelFieldManager getFieldDataManager() {
        return singleSymbolUI.getFieldDataManager();
    }

    /**
     * Gets the legend data.
     *
     * @return the legend data
     */
    public JComponent getLegendData() {
        outerPanelLegendTab = new JPanel();
        outerPanelLegendTab.setLayout(new BorderLayout());

        panelLegendTab = new JPanel(false);
        panelLegendTab.setLayout(new CardLayout());

        for (String key : uiLegendMap.keySet()) {
            SymbolPanelInterface legendPanel = uiLegendMap.get(key);

            JPanel panel = new JPanel(new BorderLayout());

            if (legendPanel.addNorthPanel() != null) {
                panel.add(legendPanel.addNorthPanel(), BorderLayout.NORTH);
            }

            if (legendPanel.addWestPanel() != null) {
                panel.add(legendPanel.addWestPanel(), BorderLayout.WEST);
            }

            if (legendPanel.addCentrePanel() != null) {
                panel.add(legendPanel.addCentrePanel(), BorderLayout.CENTER);
            }
            panelLegendTab.add(panel, key);
        }

        outerPanelLegendTab.add(panelLegendTab, BorderLayout.CENTER);

        return outerPanelLegendTab;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.panels.GetMinimumVersionInterface#getMinimumVersion(java.lang.Object, java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {
        singleSymbolUI.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    public JPanel getVendorOption() {
        if (vendorOptionUI == null) {
            vendorOptionUI = new VendorOptionUI(this);
        }

        return vendorOptionUI;
    }

    /**
     * Refresh panel.
     *
     * @param parent the parent
     * @param panelClass the panel class
     */
    public void refreshPanel(Class<?> parent, Class<?> panelClass) {
        singleSymbolUI.refreshPanel(parent, panelClass);
    }
}
