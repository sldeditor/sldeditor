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
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.render.RenderPanelFactory;
import com.sldeditor.tool.legendpanel.LegendManager;
import com.sldeditor.tool.legendpanel.LegendPanel;
import com.sldeditor.tool.legendpanel.option.LegendOptionPanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.SymbolPanelInterface;
import com.sldeditor.ui.iface.SymbolizerSelectedInterface;
import com.sldeditor.ui.tree.SLDTree;

/**
 * The Class SingleLegendUI, coordinates creating all the necessary legend panels
 * to view/edit a single SLD file.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SingleLegendUI implements SymbolPanelInterface, SymbolizerSelectedInterface {

    /** The SLD symbol tree. */
    private SLDTree sldTree = null;

    /** The legend option panel. */
    private LegendOptionPanel legendOptionPanel = null;

    /** The legend panel. */
    private LegendPanel legendPanel = null;

    /** The renderer list. */
    private List<RenderSymbolInterface> rendererList = null;

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#addWestPanel()
     */
    @Override
    public JPanel addWestPanel() {
        return createLegendPanel();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#addCentrePanel()
     */
    @Override
    public JPanel addCentrePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(getLegendOptionsPanel(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Gets the legend options panel.
     *
     * @return the legend options panel
     */
    private LegendOptionPanel getLegendOptionsPanel() {
        if(legendOptionPanel == null)
        {
            legendOptionPanel = LegendManager.getInstance().createLegendOptionsPanel(getLegendPanel());
        }

        return legendOptionPanel;
    }

    /**
     * Creates the legend selection panel.
     *
     * @return the j panel
     */
    public JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(2, 1));

        legendPanel.add(getLegendPanel());

        JPanel symbolTreePanel = getSymbolTree();
        legendPanel.add(symbolTreePanel);

        return legendPanel;
    }

    /**
     * Gets the legend panel.
     *
     * @return the legend panel
     */
    private LegendPanel getLegendPanel()
    {
        if(legendPanel == null)
        {
            legendPanel = new LegendPanel();
        }

        return legendPanel;
    }

    /**
     * Gets the symbol tree.
     *
     * @return the symbol tree
     */
    public SLDTree getSymbolTree() {
        if(sldTree == null)
        {
            // Create symbol tree without tool buttons
            sldTree = new SLDTree(getRendererList(), null);

            // Register for updates to the SLD tree structure
            SelectedSymbol.getInstance().setTreeUpdateListener(sldTree);

            // Register for notifications when user clicks on the SLD tree
            sldTree.addSymbolSelectedListener(this);
        }

        return sldTree;
    }

    /**
     * Gets the renderer list.
     *
     * @return the renderer list
     */
    private List<RenderSymbolInterface> getRendererList() {
        if(rendererList == null)
        {
            rendererList = new ArrayList<RenderSymbolInterface>();

            RenderSymbolInterface renderer = RenderPanelFactory.getRenderer(SingleLegendUI.class.getName());
            rendererList.add(renderer);
            rendererList.add(getLegendPanel());
        }
        return rendererList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.SymbolPanelInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
        getSymbolTree().populateSLD();
        getSymbolTree().selectFirstSymbol();
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
     * @see com.sldeditor.ui.iface.SymbolizerSelectedInterface#getPanel(java.lang.Class, java.lang.String)
     */
    @Override
    public PopulateDetailsInterface getPanel(Class<?> parentClass, String key) {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.SymbolizerSelectedInterface#getPanelIds()
     */
    @Override
    public Set<String> getPanelIds() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.SymbolizerSelectedInterface#show(java.lang.Class, java.lang.Class)
     */
    @Override
    public void show(Class<?> parentClass, Class<?> classSelected) {
        // Currently display the same panel for all types of symbol
    }
}
