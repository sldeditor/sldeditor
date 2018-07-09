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

package com.sldeditor.ui.legend;

import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.render.RuleRenderOptions;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.geotools.data.DataStore;

/**
 * Panel to display a SLD legend image.
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendPanel extends JPanel implements RenderSymbolInterface {
    /** The Constant SCROLL_PANE_HEIGHT. */
    private static final int SCROLL_PANE_HEIGHT = 150;

    /** The Constant SCROLL_PANE_WIDTH. */
    private static final int SCROLL_PANE_WIDTH = 100;

    /** The legend image panel. */
    private LegendPanelImage legendImagePanel = new LegendPanelImage();

    /** The scroll pane. */
    private JScrollPane scrollPane = null;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Instantiates a new legend panel. */
    public LegendPanel() {
        setLayout(new BorderLayout(0, 0));

        scrollPane = new JScrollPane(legendImagePanel);
        scrollPane.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        add(scrollPane, BorderLayout.CENTER);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(
            GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag) {
        // Do nothing
    }

    /**
     * Adds the sld output listener.
     *
     * @param sldOutput the sld output
     */
    @Override
    public void addSLDOutputListener(SLDOutputInterface sldOutput) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.render.iface.RenderSymbolInterface#renderSymbol()
     */
    @Override
    public void renderSymbol() {
        legendImagePanel.renderSymbol();
    }

    /**
     * Gets the rule render options.
     *
     * @return the rule render options
     */
    @Override
    public RuleRenderOptions getRuleRenderOptions() {
        // Do nothing
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }
}
