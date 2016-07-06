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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.render.RenderPanelFactory;
import com.sldeditor.tool.legendpanel.LegendManager;
import com.sldeditor.tool.legendpanel.LegendPanel;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.SymbolizerDetailsPanel;
import com.sldeditor.ui.iface.SymbolPanelInterface;
import com.sldeditor.ui.iface.SymbolizerSelectedInterface;
import com.sldeditor.ui.tree.SLDTree;
import com.sldeditor.ui.tree.SLDTreeTools;

/**
 * The Class SingleSymbolUI, coordinates creating all the necessary panels
 * to view/edit a single SLD file.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SingleSymbolUI implements SymbolPanelInterface {

    /** The panel marker symbol. */
    private SLDTree sldTree = null;

    /** The panel marker details. */
    private SymbolizerDetailsPanel panelSymbolizerDetails = null;

    /** The legend panel. */
    private LegendPanel legendPanel = null;

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

        panel.add(getSymbolizerDetailsPanel(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Gets the symbolizer details panel.
     *
     * @return the marker details panel
     */
    private JPanel getSymbolizerDetailsPanel() {
        if(panelSymbolizerDetails == null)
        {
            List<RenderSymbolInterface> rendererList = new ArrayList<RenderSymbolInterface>();
            rendererList.add(RenderPanelFactory.getRenderer(SingleSymbolUI.class.getName()));
            rendererList.add(LegendManager.getInstance().getRendererUpdate());
            rendererList.add(SLDEditorFile.getInstance());
            rendererList.add(RenderPanelFactory.getMapRenderer());
            panelSymbolizerDetails = new SymbolizerDetailsPanel(rendererList, getSymbolTree());
        }

        return panelSymbolizerDetails;
    }

    /**
     * Creates the symbol selection panel.
     *
     * @return the j panel
     */
    public JPanel createSymbolSelectionPanel() {
        JPanel symbolPanel = new JPanel();
        symbolPanel.setLayout(new BoxLayout(symbolPanel, BoxLayout.Y_AXIS));

        RenderSymbolInterface renderSymbol = RenderPanelFactory.getRenderer(SingleSymbolUI.class.getName()); 

        symbolPanel.add(RenderPanelFactory.getRenderOptionPanel(renderSymbol, getRendererList()));

        JPanel symbolTreePanel = getSymbolTree();

        symbolPanel.add(symbolTreePanel);
        symbolPanel.add(getLegendPanel());

        return symbolPanel;
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
            List<RenderSymbolInterface> rendererList = getRendererList();

            SLDTreeTools sldTreeTools = new SLDTreeTools();
            sldTree = new SLDTree(rendererList, sldTreeTools);

            // Register for updates to the SLD tree structure
            SelectedSymbol.getInstance().setTreeUpdateListener(sldTree);

            // Register for notifications when user clicks on the SLD tree
            sldTree.addSymbolSelectedListener((SymbolizerSelectedInterface) getSymbolizerDetailsPanel());
        }

        return sldTree;
    }

    /**
     * Gets the renderer list.
     *
     * @return the renderer list
     */
    private List<RenderSymbolInterface> getRendererList() {
        List<RenderSymbolInterface> rendererList = new ArrayList<RenderSymbolInterface>();

        RenderSymbolInterface renderer = RenderPanelFactory.getRenderer(SingleSymbolUI.class.getName());
        rendererList.add(renderer);
        rendererList.add(getLegendPanel());
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
        GraphicPanelFieldManager mergedData = new GraphicPanelFieldManager(null);

        panelSymbolizerDetails.mergeFieldDataManager(mergedData);

        return mergedData;
    }
}
