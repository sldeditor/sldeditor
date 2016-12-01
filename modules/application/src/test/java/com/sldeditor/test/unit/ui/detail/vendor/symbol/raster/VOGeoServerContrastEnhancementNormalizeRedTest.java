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

package com.sldeditor.test.unit.ui.detail.vendor.symbol.raster;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.swing.Box;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalizeRed;

/**
 * The unit test for VOGeoServerContrastEnhancementNormalizeRed.
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalizeRed}
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerContrastEnhancementNormalizeRedTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalizeRed#VOGeoServerContrastEnhancementNormalizeRed(java.lang.Class, com.sldeditor.ui.detail.RasterSymbolizerDetails)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalizeRed#getContrastEnhancement(com.sldeditor.common.xml.ui.GroupIdEnum, org.geotools.styling.ChannelSelection)}.
     */
    @Test
    public void testVOGeoServerContrastEnhancementNormalizeRed() {
        RasterSymbolizerDetails panel = new RasterSymbolizerDetails(null);

        VOGeoServerContrastEnhancementNormalizeRed testObj = new VOGeoServerContrastEnhancementNormalizeRed(panel.getClass(), panel);
        RasterSymbolizer rasterSymbolizer = null;
        testObj.setParentPanel(panel);
        testObj.populate(rasterSymbolizer);
        testObj.updateSymbol(rasterSymbolizer);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        rasterSymbolizer = styleFactory.createRasterSymbolizer();
        ChannelSelection channelSelection = createChannelSelection(styleFactory, ContrastMethod.LOGARITHMIC);

        GroupConfigInterface constrastMethodGroup = panel.getGroup(GroupIdEnum.RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD);
        assertNotNull(constrastMethodGroup);
        MultiOptionGroup constrastMethodGroup2 = (MultiOptionGroup) constrastMethodGroup;
        Box box = Box.createVerticalBox();
        constrastMethodGroup2.createUI(panel.getFieldDataManager(), box, panel, panel.getPanelId());
        constrastMethodGroup2.setOption(GroupIdEnum.RASTER_OVERALL_CONTRAST_METHOD_LOGARITHMIC);

        rasterSymbolizer.setChannelSelection(channelSelection);
        testObj.populate(rasterSymbolizer);

        SelectedSymbol.getInstance().setSymbolizer(rasterSymbolizer);
        Controller.getInstance().setPopulating(true);
        panel.populate(SelectedSymbol.getInstance());
        Controller.getInstance().setPopulating(false);
        testObj.updateSymbol(rasterSymbolizer);

        channelSelection = createChannelSelection(styleFactory, ContrastMethod.EXPONENTIAL);
        rasterSymbolizer.setChannelSelection(channelSelection);
        constrastMethodGroup2.setOption(GroupIdEnum.RASTER_OVERALL_CONTRAST_METHOD_EXPONENTIAL);
        testObj.populate(rasterSymbolizer);
        testObj.updateSymbol(rasterSymbolizer);

        channelSelection = createChannelSelection(styleFactory, ContrastMethod.HISTOGRAM);
        rasterSymbolizer.setChannelSelection(channelSelection);
        constrastMethodGroup2.setOption(GroupIdEnum.RASTER_OVERALL_CONTRAST_METHOD_HISTOGRAM);
        testObj.populate(rasterSymbolizer);
        testObj.updateSymbol(rasterSymbolizer);

        channelSelection = createChannelSelection(styleFactory, ContrastMethod.NORMALIZE);
        constrastMethodGroup2.setOption(GroupIdEnum.RASTER_OVERALL_CONTRAST_METHOD_NORMALIZE);
        rasterSymbolizer.setChannelSelection(channelSelection);
        testObj.populate(rasterSymbolizer);
        testObj.updateSymbol(rasterSymbolizer);

        // Error
        channelSelection = createChannelSelectionError(styleFactory, ContrastMethod.NORMALIZE);
        rasterSymbolizer.setChannelSelection(channelSelection);
        testObj.populate(rasterSymbolizer);
        testObj.updateSymbol(rasterSymbolizer);

        // Increase code coverage
        testObj.populate((SelectedSymbol)null);
        testObj.populate((TextSymbolizer)null);
        testObj.populate((PolygonSymbolizer)null);
        testObj.updateSymbol((TextSymbolizer)null);
        testObj.updateSymbol((PolygonSymbolizer)null);
        testObj.preLoadSymbol();

        assertTrue(testObj.isDataPresent());

        testObj.dataChanged(FieldIdEnum.DESCRIPTION);
    }

    /**
     * Creates the channel selection object
     *
     * @param styleFactory the style factory
     * @param contrastMethod the contrast method
     * @return the channel selection
     */
    private ChannelSelection createChannelSelection(StyleFactoryImpl styleFactory, ContrastMethod contrastMethod) {
        ContrastEnhancement contrastEnhancement = (ContrastEnhancement) styleFactory.contrastEnhancement(null, 
                contrastMethod.name());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Map<String, Expression> options = contrastEnhancement.getOptions();
        options.put("algorithm", ff.literal("StretchToMinimumMaximum"));
        options.put("minValue", ff.literal("1"));
        options.put("maxValue", ff.literal("5"));

        SelectedChannelType channelType = styleFactory.createSelectedChannelType("channel name", contrastEnhancement);
        SelectedChannelType[] channels = new SelectedChannelType[3];
        channels[0] = channelType;
        channels[1] = channelType;
        channels[2] = channelType;
        ChannelSelection channelSelection = styleFactory.createChannelSelection(channels);
        return channelSelection;
    }

    /**
     * Creates the channel selection error object
     *
     * @param styleFactory the style factory
     * @param contrastMethod the contrast method
     * @return the channel selection
     */
    private ChannelSelection createChannelSelectionError(StyleFactoryImpl styleFactory, ContrastMethod contrastMethod) {
        ContrastEnhancement contrastEnhancement = (ContrastEnhancement) styleFactory.contrastEnhancement(null, 
                contrastMethod.name());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Map<String, Expression> options = contrastEnhancement.getOptions();
        options.put("algorithm", ff.literal("TestStretchToMinimumMaximum"));
        options.put("minValue", ff.literal("1.0"));
        options.put("maxValue", ff.literal("5.0"));

        SelectedChannelType channelType = styleFactory.createSelectedChannelType("channel name", contrastEnhancement);
        SelectedChannelType[] channels = new SelectedChannelType[3];
        channels[0] = channelType;
        channels[1] = channelType;
        channels[2] = channelType;
        ChannelSelection channelSelection = styleFactory.createChannelSelection(channels);
        return channelSelection;
    }
}
