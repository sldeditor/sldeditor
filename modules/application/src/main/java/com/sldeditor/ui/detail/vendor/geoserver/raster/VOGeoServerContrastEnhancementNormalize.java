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
package com.sldeditor.ui.detail.vendor.geoserver.raster;

import java.util.Map;

import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * Class to handle the getting and setting of GeoServer 
 * raster contrast enhancement normalize vendor option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public abstract class VOGeoServerContrastEnhancementNormalize extends StandardPanel implements VendorOptionInterface, PopulateDetailsInterface, UpdateSymbolInterface
{
    /** The Constant MAX_VALUE_OPTION. */
    private static final String MAX_VALUE_OPTION = "maxValue";

    /** The Constant MIN_VALUE_OPTION. */
    private static final String MIN_VALUE_OPTION = "minValue";

    /** The Constant ALGORITHM_OPTION. */
    private static final String ALGORITHM_OPTION = "algorithm";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private UpdateSymbolInterface parentObj = null;

    /** The parent panel. */
    private RasterSymbolizerDetails parentPanel = null;

    private FieldIdEnum algorithmFieldId;
    private FieldIdEnum minValueFieldId;
    private FieldIdEnum maxValueFieldId;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     * @param resourceFile the resource file
     * @param parentPanel the parent panel
     */
    protected VOGeoServerContrastEnhancementNormalize(Class<?> panelId, 
            String resourceFile, 
            RasterSymbolizerDetails parentPanel,
            FieldIdEnum algorithmFieldId,
            FieldIdEnum minValueFieldId,
            FieldIdEnum maxValueFieldId)
    {
        super(panelId, null);

        this.parentPanel = parentPanel;
        this.algorithmFieldId = algorithmFieldId;
        this.minValueFieldId = minValueFieldId;
        this.maxValueFieldId = maxValueFieldId;

        createUI(resourceFile);
    }

    /**
     * Creates the ui.
     *
     * @param resourceFile the resource file
     */
    private void createUI(String resourceFile)
    {
        readConfigFileNoScrollPane(null, this, resourceFile);
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOption()
     */
    @Override
    public VendorOptionVersion getVendorOption()
    {
        return getVendorOptionVersion();
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldId)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField)
    {
        if(parentObj != null)
        {
            parentObj.dataChanged(changedField);
        }
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol)
    {
        // Do nothing
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return this.fieldConfigManager;
    }

    /**
     * Populate.
     *
     * @param textSymbolizer the text symbolizer
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void populate(TextSymbolizer textSymbolizer)
    {
        // Do nothing
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getPanel()
     */
    @Override
    public StandardPanel getPanel()
    {
        return this;
    }

    /**
     * Sets the parent panel.
     *
     * @param parent the new parent panel
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)
     */
    @Override
    public void setParentPanel(UpdateSymbolInterface parent)
    {
        this.parentObj = parent;
    }

    /**
     * Gets the parent panel.
     *
     * @return the parentObj
     */
    @Override
    public UpdateSymbolInterface getParentPanel() {
        return parentObj;
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return true;
    }

    /**
     * Update symbol.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer)
    {
        // Do nothing
    }

    /**
     * Populate.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void populate(PolygonSymbolizer polygonSymbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void updateSymbol(TextSymbolizer textSymbolizer) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void populate(RasterSymbolizer rasterSymbolizer) {
        if(rasterSymbolizer != null)
        {
            ContrastEnhancement contrastEnhancement = getContrastEnhancement(rasterSymbolizer);
            if(contrastEnhancement != null)
            {
                Map<String, Expression> options = contrastEnhancement.getOptions();
                if((options != null) && !options.isEmpty())
                {
                    if(contrastEnhancement.getMethod().equals(ContrastMethod.NORMALIZE))
                    {
                        String algorithm = options.get(ALGORITHM_OPTION).toString();
                        fieldConfigVisitor.populateComboBoxField(algorithmFieldId, algorithm);

                        int minValue = 0;
                        try
                        {
                            Expression expression = options.get(MIN_VALUE_OPTION);
                            minValue = Integer.valueOf(expression.toString());
                        }
                        catch(Exception e)
                        {
                            // Ignore number format conversions
                            ConsoleManager.getInstance().error(this, "Vendor Option - minValue not valid");
                        }
                        fieldConfigVisitor.populateIntegerField(minValueFieldId, minValue);

                        int maxValue = 0;
                        try
                        {
                            Expression expression = options.get(MAX_VALUE_OPTION);
                            maxValue = Integer.valueOf(expression.toString());
                        }
                        catch(Exception e)
                        {
                            // Ignore number format conversions
                            ConsoleManager.getInstance().error(this, "Vendor Option - maxValue not valid");
                        }
                        fieldConfigVisitor.populateIntegerField(maxValueFieldId, maxValue);
                    }
                }
            }
        }

    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void updateSymbol(RasterSymbolizer rasterSymbolizer) {

        if(parentPanel != null)
        {
            GroupConfigInterface group = parentPanel.getGroup(GroupIdEnum.RASTER_CHANNELSELECTION);
            if(group != null)
            {
                if(group.isPanelEnabled())
                {
                    MultiOptionGroup contrastEnhancementGroup = (MultiOptionGroup) group;
                    ChannelSelection channelSelection = rasterSymbolizer.getChannelSelection();

                    OptionGroup selectedOption = contrastEnhancementGroup.getSelectedOptionGroup();

                    ContrastEnhancement contrastEnhancement = getContrastEnhancement(selectedOption.getId(),
                            channelSelection);

                    if(contrastEnhancement != null)
                    {
                        extractNormalizeVendorOption(contrastEnhancement);
                    }
                }
            }
        }
    }

    /**
     * Gets the contrast enhancement.
     *
     * @param id the id
     * @param channelSelection the channel selection
     * @return the contrast enhancement
     */
    protected abstract ContrastEnhancement getContrastEnhancement(GroupIdEnum id,
            ChannelSelection channelSelection);

    /**
     * Gets the contrast enhancement.
     *
     * @param rasterSymbolizer the raster symbolizer
     * @return the contrast enhancement
     */
    protected abstract ContrastEnhancement getContrastEnhancement(RasterSymbolizer rasterSymbolizer);

    /**
     * Extract normalize vendor option.
     *
     * @param contrastEnhancement the contrast enhancement
     */
    protected void extractNormalizeVendorOption(ContrastEnhancement contrastEnhancement) {
        if(contrastEnhancement != null)
        {
            Map<String, Expression> options = contrastEnhancement.getOptions();
            options.clear();

            if(contrastEnhancement.getMethod() != null)
            {
                String method = contrastEnhancement.getMethod().name();
                if(method.compareToIgnoreCase(ContrastMethod.NORMALIZE.name()) == 0)
                {
                    Expression algorithmExpression = fieldConfigVisitor.getExpression(algorithmFieldId);
                    if(algorithmExpression != null)
                    {
                        options.put(ALGORITHM_OPTION, algorithmExpression);
                    }

                    Expression minValueExpression = fieldConfigVisitor.getExpression(minValueFieldId);
                    if(minValueExpression != null)
                    {
                        options.put(MIN_VALUE_OPTION, minValueExpression);
                    }

                    Expression maxValueExpression = fieldConfigVisitor.getExpression(maxValueFieldId);
                    if(maxValueExpression != null)
                    {
                        options.put(MAX_VALUE_OPTION, maxValueExpression);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        VendorOptionInfo info = new VendorOptionInfo("Raster Contrast Enhancement",
                this.getVendorOptionVersion(),
                Localisation.getString(VOGeoServerContrastEnhancementNormalize.class, "VOGeoServerContrastEnhancementNormalize.description"));

        return info;
    }
}
