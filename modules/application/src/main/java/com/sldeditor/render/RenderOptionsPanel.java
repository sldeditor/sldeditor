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
package com.sldeditor.render;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.geotools.data.DataStore;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.render.RuleRenderOptions;

/**
 * A panel to allow the configuration of render options, fields are currently not shown.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderOptionsPanel extends JPanel implements DataSourceUpdatedInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The render symbol. */
    private RenderSymbolInterface renderSymbol = null;

    /** The renderer list. */
    private List<RenderSymbolInterface> rendererList = null;

    /** The apply transformation check box. */
    private JCheckBox applyTransformationCheckBox = null;

    /**
     * Instantiates a new render options panel.
     *
     * @param renderSymbol the render symbol
     * @param rendererList the renderer list
     */
    public RenderOptionsPanel(RenderSymbolInterface renderSymbol, List<RenderSymbolInterface> rendererList) {
        this.renderSymbol = renderSymbol;
        this.rendererList = rendererList;

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.addListener(this);

        createUI();

        populate();
        
        updateButtonState(GeometryTypeEnum.UNKNOWN);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        applyTransformationCheckBox = new JCheckBox(Localisation.getString(RenderOptionsPanel.class, "RenderOptionsPanel.applyTransformation"));
        applyTransformationCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RuleRenderOptions options = renderSymbol.getRuleRenderOptions();

                options.setApplyTransformation(applyTransformationCheckBox.isSelected());

                updateSymbols();
            }});
        // Hide for now
        //  add(applyTransformationCheckBox);
    }

    /**
     * Refresh the drawing of any symbols in renderers
     */
    private void updateSymbols() {
        if(rendererList != null)
        {
            for(RenderSymbolInterface renderer : rendererList)
            {
                renderer.renderSymbol();
            }
        }
    }

    /**
     * Populate.
     */
    private void populate()
    {
        RuleRenderOptions options = this.renderSymbol.getRuleRenderOptions();

        applyTransformationCheckBox.setSelected(options.isTransformationApplied());
    }

    /**
     * Data source loaded.
     *
     * @param geometryType the geometry type
     * @param isConnectedToDataSourceFlag the is connected to data source flag
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag)
    {
        updateButtonState(geometryType);
    }

    /**
     * Update button state according to the selected symbol type.
     *
     * @param geometryType the geometry type
     */
    private void updateButtonState(GeometryTypeEnum geometryType) {
        boolean applyTransformEnabled = false;

        switch(geometryType)
        {
        case POINT:
        case LINE:
        case POLYGON:
            applyTransformEnabled = true;
            break;
        case RASTER:
            break;
        default:
            break;
        }

        if(applyTransformationCheckBox != null)
        {
            applyTransformationCheckBox.setEnabled(applyTransformEnabled);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }
}
