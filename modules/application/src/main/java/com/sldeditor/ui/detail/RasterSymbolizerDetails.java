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
package com.sldeditor.ui.detail;

import java.util.List;

import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.ShadedReliefImpl;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.opengis.style.OverlapBehavior;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.raster.VendorOptionRasterFactory;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class RasterSymbolizerDetails allows a user to configure raster data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class RasterSymbolizerDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option raster factory. */
    private VendorOptionRasterFactory vendorOptionRasterFactory = null;

    /**
     * Constructor.
     *
     * @param functionManager the function manager
     */
    public RasterSymbolizerDetails(FunctionNameInterface functionManager)
    {
        super(RasterSymbolizerDetails.class, functionManager);

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        createVendorOptionPanel();

        readRasterConfigFile(vendorOptionRasterFactory, getClass(), this, "Raster.xml");
    }

    /**
     * Creates the vendor option panel.
     *
     * @return the detail panel
     */
    private void createVendorOptionPanel() {

        vendorOptionRasterFactory = new VendorOptionRasterFactory(getClass(), getFunctionManager(), this);

        List<VendorOptionInterface> voList = vendorOptionRasterFactory.getVendorOptionList();
        if(voList != null)
        {
            for(VendorOptionInterface vendorOption : voList)
            {
                vendorOption.setParentPanel(this);
            }
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
    public void populate(SelectedSymbol selectedSymbol) {

        if(selectedSymbol != null)
        {
            RasterSymbolizer rasterSymbolizer = (RasterSymbolizer) selectedSymbol.getSymbolizer();
            if(rasterSymbolizer != null)
            {
                populateStandardData(rasterSymbolizer);

                // Opacity
                fieldConfigVisitor.populateField(FieldIdEnum.RASTER_OPACITY, rasterSymbolizer.getOpacity());

                // Contrast enhancement
                ContrastEnhancement contrast = rasterSymbolizer.getContrastEnhancement();

                GroupConfigInterface group = getGroup(GroupIdEnum.RASTER_CONTRAST);
                if(group != null)
                {
                    group.enable(contrast != null);
                }
                if(contrast != null)
                {
                    Expression gammaValue = contrast.getGammaValue();
                    fieldConfigVisitor.populateField(FieldIdEnum.RASTER_CONTRAST_GAMMAVALUE, gammaValue);

                    populateContrastMethod(contrast, GroupIdEnum.RASTER_OVERALL_CONTRAST_METHOD);
                }

                // Channel selection
                group = getGroup(GroupIdEnum.RASTER_CHANNELSELECTION);
                if(group != null)
                {
                    MultiOptionGroup channelSelectionGroup = (MultiOptionGroup) group;

                    ChannelSelection channelSelection = rasterSymbolizer.getChannelSelection();

                    boolean enableChannelSelection = false;

                    if(channelSelection != null)
                    {
                        SelectedChannelType[] rgbChannels = channelSelection.getRGBChannels();

                        enableChannelSelection = ((channelSelection.getGrayChannel() != null) ||
                                (rgbChannels[0] != null) || (rgbChannels[1] != null) || (rgbChannels[2] != null));
                    }
                    channelSelectionGroup.enable(enableChannelSelection);
                    if(enableChannelSelection)
                    {
                        SelectedChannelType greyChannel = channelSelection.getGrayChannel();
                        if(greyChannel != null)
                        {
                            channelSelectionGroup.setOption(GroupIdEnum.RASTER_GREY_CHANNEL_OPTION);

                            populateContrastEnhancementGroup(GroupIdEnum.RASTER_GREY_CHANNEL,
                                    FieldIdEnum.RASTER_RGB_GREY_NAME,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_GREY_CONTRAST,
                                    FieldIdEnum.RASTER_RGB_CHANNEL_GREY_CONTRAST_GAMMA,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD,
                                    greyChannel);
                        }
                        else
                        {
                            SelectedChannelType[] rgbChannels = channelSelection.getRGBChannels();

                            channelSelectionGroup.setOption(GroupIdEnum.RASTER_RGB_CHANNEL_OPTION);

                            populateContrastEnhancementGroup(GroupIdEnum.RASTER_RGB_CHANNEL_RED,
                                    FieldIdEnum.RASTER_RGB_RED_NAME,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST,
                                    FieldIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST_GAMMA,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD,
                                    rgbChannels[0]);

                            populateContrastEnhancementGroup(GroupIdEnum.RASTER_RGB_CHANNEL_GREEN,
                                    FieldIdEnum.RASTER_RGB_GREEN_NAME,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_GREEN_CONTRAST,
                                    FieldIdEnum.RASTER_RGB_CHANNEL_GREEN_CONTRAST_GAMMA,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD,
                                    rgbChannels[1]);

                            populateContrastEnhancementGroup(GroupIdEnum.RASTER_RGB_CHANNEL_BLUE,
                                    FieldIdEnum.RASTER_RGB_BLUE_NAME,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_BLUE_CONTRAST,
                                    FieldIdEnum.RASTER_RGB_CHANNEL_BLUE_CONTRAST_GAMMA,
                                    GroupIdEnum.RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD,
                                    rgbChannels[2]);
                        }
                    }
                }

                // Colour map
                ColorMap colourMap = rasterSymbolizer.getColorMap();

                fieldConfigVisitor.populateComboBoxField(FieldIdEnum.RASTER_COLOURMAP_TYPE, Integer.valueOf(colourMap.getType()).toString());
                fieldConfigVisitor.populateColourMapField(FieldIdEnum.RASTER_COLOURMAP, colourMap);

                // Shaded relief
                ShadedRelief shadedRelief = rasterSymbolizer.getShadedRelief();

                group = getGroup(GroupIdEnum.RASTER_SHADEDRELIEF);
                if(group != null)
                {
                    group.enable(shadedRelief != null);
                }
                
                if(shadedRelief != null)
                {
                    fieldConfigVisitor.populateBooleanField(FieldIdEnum.RASTER_SHADEDRELIEF_BRIGHTNESS, shadedRelief.isBrightnessOnly());
                    fieldConfigVisitor.populateField(FieldIdEnum.RASTER_SHADEDRELIEF_FACTOR, shadedRelief.getReliefFactor());
                }

                // Overlap behaviour
                OverlapBehavior overlapBehaviour = rasterSymbolizer.getOverlapBehavior();

                group = getGroup(GroupIdEnum.RASTER_OVERLAP);
                if(group != null)
                {
                    group.enable(overlapBehaviour != null);
                }
                if(overlapBehaviour != null)
                {
                    fieldConfigVisitor.populateComboBoxField(FieldIdEnum.RASTER_OVERLAP_BEHAVIOUR, overlapBehaviour.name());
                }

                if(vendorOptionRasterFactory != null)
                {
                    vendorOptionRasterFactory.populate(rasterSymbolizer);
                }
            }
        }
    }

    /**
     * Populate contrast enhancement group.
     *
     * @param channelGroup the channel group
     * @param nameField the name field
     * @param contrastGroup the contrast group
     * @param gammaField the gamma field
     * @param methodField the method field
     * @param channelType the channel type
     */
    private void populateContrastEnhancementGroup(GroupIdEnum channelGroup,
            FieldIdEnum nameField,
            GroupIdEnum contrastGroup,
            FieldIdEnum gammaField,
            GroupIdEnum methodField,
            SelectedChannelType channelType)
    {
        String name = "";

        if(channelType != null)
        {
            name = channelType.getChannelName();
        }
        fieldConfigVisitor.populateTextField(nameField, name);

        GroupConfigInterface contrastGrp = getGroup(contrastGroup);

        ContrastEnhancement contrastEnhancement = null;

        if(channelType != null)
        {
            contrastEnhancement = channelType.getContrastEnhancement();
        }
        contrastGrp.enable(contrastEnhancement != null);
        if(contrastEnhancement != null)
        {
            fieldConfigVisitor.populateField(gammaField, contrastEnhancement.getGammaValue());

            populateContrastMethod(contrastEnhancement, methodField);
        }
    }

    /**
     * Populate contrast method.
     *
     * @param contrastEnhancement the contrast enhancement
     * @param contrastMethodGroup the contrast method group
     */
    private void populateContrastMethod(ContrastEnhancement contrastEnhancement, GroupIdEnum contrastMethodGroup) {
        GroupConfigInterface group = getGroup(contrastMethodGroup);
        if(group != null)
        {
            GroupIdEnum selectedNormalizeMethod = GroupIdEnum.UNKNOWN;
            MultiOptionGroup contrastNormalizeMethodGroup = (MultiOptionGroup) group;
            if(contrastEnhancement != null)
            {
                ContrastMethod contrastMethod = contrastEnhancement.getMethod();

                if(contrastMethod != null)
                {
                    String contrastMethodString = contrastMethod.name();

                    for(OptionGroup option : contrastNormalizeMethodGroup.getGroupList())
                    {
                        if(option.getLabel().compareToIgnoreCase(contrastMethodString) == 0)
                        {
                            selectedNormalizeMethod = option.getId();
                        }
                    }
                }
            }
            contrastNormalizeMethodGroup.setOption(selectedNormalizeMethod);
        }
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {

        StandardData standardData = getStandardData();

        Expression opacityExpression = fieldConfigVisitor.getExpression(FieldIdEnum.RASTER_OPACITY);

        // Contrast enhancement
        Expression gammaValueExpression = fieldConfigVisitor.getExpression(FieldIdEnum.RASTER_CONTRAST_GAMMAVALUE);
        ContrastEnhancement contrastEnhancement = null;

        GroupConfigInterface group = getGroup(GroupIdEnum.RASTER_CONTRAST);
        if(group.isPanelEnabled())
        {
            String method = null;
            group = getGroup(GroupIdEnum.RASTER_OVERALL_CONTRAST_METHOD);
            if(group != null)
            {
                MultiOptionGroup contrastNormalizeMethodGroup = (MultiOptionGroup) group;

                OptionGroup selectedOption = contrastNormalizeMethodGroup.getSelectedOptionGroup();

                if(selectedOption != null)
                {
                    method = selectedOption.getLabel();
                }
            }

            contrastEnhancement = (ContrastEnhancement) getStyleFactory().contrastEnhancement(gammaValueExpression, 
                    method);
        }

        // Colour map
        ColorMap colorMap = fieldConfigVisitor.getColourMap(FieldIdEnum.RASTER_COLOURMAP);
        ValueComboBoxData colourMapType = fieldConfigVisitor.getComboBox(FieldIdEnum.RASTER_COLOURMAP_TYPE);

        colorMap.setType(Integer.valueOf(colourMapType.getKey()));

        // Geometry field
        Expression geometryField = ExtractGeometryField.getGeometryField(fieldConfigVisitor);

        // Channel selection
        ChannelSelection channelSelection = null;
        group = getGroup(GroupIdEnum.RASTER_CHANNELSELECTION);
        if(group != null)
        {
            if(group.isPanelEnabled())
            {
                MultiOptionGroup contrastEnhancementGroup = (MultiOptionGroup) group;

                OptionGroup selectedOption = contrastEnhancementGroup.getSelectedOptionGroup();
                if(selectedOption.getId() == GroupIdEnum.RASTER_GREY_CHANNEL_OPTION)
                {
                    // Grey option group
                    SelectedChannelType greyChannel = extractContrastEnhancementGroup(GroupIdEnum.RASTER_GREY_CHANNEL,
                            FieldIdEnum.RASTER_RGB_GREY_NAME,
                            GroupIdEnum.RASTER_RGB_CHANNEL_GREY_CONTRAST,
                            FieldIdEnum.RASTER_RGB_CHANNEL_GREY_CONTRAST_GAMMA,
                            GroupIdEnum.RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD);

                    channelSelection = getStyleFactory().channelSelection(greyChannel);
                }
                else
                {
                    SelectedChannelType redChannel = extractContrastEnhancementGroup(GroupIdEnum.RASTER_RGB_CHANNEL_RED,
                            FieldIdEnum.RASTER_RGB_RED_NAME,
                            GroupIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST,
                            FieldIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST_GAMMA,
                            GroupIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD);

                    SelectedChannelType greenChannel = extractContrastEnhancementGroup(GroupIdEnum.RASTER_RGB_CHANNEL_GREEN,
                            FieldIdEnum.RASTER_RGB_GREEN_NAME,
                            GroupIdEnum.RASTER_RGB_CHANNEL_GREEN_CONTRAST,
                            FieldIdEnum.RASTER_RGB_CHANNEL_GREEN_CONTRAST_GAMMA,
                            GroupIdEnum.RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD);

                    SelectedChannelType blueChannel = extractContrastEnhancementGroup(GroupIdEnum.RASTER_RGB_CHANNEL_BLUE,
                            FieldIdEnum.RASTER_RGB_BLUE_NAME,
                            GroupIdEnum.RASTER_RGB_CHANNEL_BLUE_CONTRAST,
                            FieldIdEnum.RASTER_RGB_CHANNEL_BLUE_CONTRAST_GAMMA,
                            GroupIdEnum.RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD);

                    SelectedChannelType[] channels = new SelectedChannelType[3];
                    channels[0] = redChannel;
                    channels[1] = greenChannel;
                    channels[2] = blueChannel;
                    channelSelection = getStyleFactory().createChannelSelection(channels);
                }
            }
        }

        //
        // Overlap
        //
        OverlapBehavior overlapBehavior = null;
        group = getGroup(GroupIdEnum.RASTER_OVERLAP);
        if(group.isPanelEnabled())
        {
            ValueComboBoxData overlapBehaviorValue = fieldConfigVisitor.getComboBox(FieldIdEnum.RASTER_OVERLAP_BEHAVIOUR);

            overlapBehavior = OverlapBehavior.valueOf(overlapBehaviorValue.getKey());
        }

        //
        // Shaded relief
        //
        ShadedRelief shadedRelief = null;
        group = getGroup(GroupIdEnum.RASTER_SHADEDRELIEF);
        if(group.isPanelEnabled())
        {
            shadedRelief = new ShadedReliefImpl();
            shadedRelief.setBrightnessOnly(fieldConfigVisitor.getBoolean(FieldIdEnum.RASTER_SHADEDRELIEF_BRIGHTNESS));
            shadedRelief.setReliefFactor(fieldConfigVisitor.getExpression(FieldIdEnum.RASTER_SHADEDRELIEF_FACTOR));
        }
        Symbolizer symbolizer = null;

        RasterSymbolizer rasterSymbolizer = (RasterSymbolizer) getStyleFactory().rasterSymbolizer(standardData.name,
                geometryField,
                standardData.description, 
                standardData.unit, 
                opacityExpression,
                channelSelection,
                overlapBehavior,
                colorMap,
                contrastEnhancement,
                shadedRelief,
                symbolizer);

        if(vendorOptionRasterFactory != null)
        {
            vendorOptionRasterFactory.updateSymbol(rasterSymbolizer);
        }
        rasterSymbolizer.setOverlapBehavior(overlapBehavior);
        SelectedSymbol.getInstance().replaceSymbolizer(rasterSymbolizer);

        this.fireUpdateSymbol();
    }

    /**
     * Extract contrast enhancement group.
     *
     * @param channelGroup the channel group
     * @param nameField the name field
     * @param contrastGroup the contrast group
     * @param gammaField the gamma field
     * @param contrastMethod the contrast method
     * @return the selected channel type
     */
    private SelectedChannelType extractContrastEnhancementGroup(GroupIdEnum channelGroup,
            FieldIdEnum nameField,
            GroupIdEnum contrastGroup,
            FieldIdEnum gammaField,
            GroupIdEnum contrastMethod) {

        SelectedChannelType channelType = null;

        GroupConfigInterface group = getGroup(channelGroup);

        if(group.isPanelEnabled())
        {
            String channelName = fieldConfigVisitor.getText(nameField);

            GroupConfigInterface contrastGrp = getGroup(contrastGroup);

            ContrastEnhancement contrastEnhancement = null;
            if(contrastGrp.isPanelEnabled())
            {
                Expression gammaExpression = fieldConfigVisitor.getExpression(gammaField);

                GroupConfigInterface constrastMethodGroup = getGroup(contrastMethod);
                if(constrastMethodGroup != null)
                {
                    String method = null;
                    MultiOptionGroup constrastMethodGroup2 = (MultiOptionGroup) constrastMethodGroup;
                    OptionGroup selectedOption = constrastMethodGroup2.getSelectedOptionGroup();
                    if(selectedOption != null)
                    {
                        method = selectedOption.getLabel();
                    }

                    contrastEnhancement = (ContrastEnhancement) getStyleFactory().contrastEnhancement(gammaExpression, 
                            method);
                }
            }

            channelType = getStyleFactory().createSelectedChannelType(channelName, contrastEnhancement);
        }
        return channelType;
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
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
        if(vendorOptionRasterFactory != null)
        {
            vendorOptionRasterFactory.updateFieldDataManager(fieldConfigManager);
        }

        return fieldConfigManager;
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

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        vendorOptionRasterFactory.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
    }
}
