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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.raster;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.ui.detail.RasterSymbolizerDetails;
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
        testObj.updateSymbol(rasterSymbolizer);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        rasterSymbolizer = styleFactory.createRasterSymbolizer();
        testObj.updateSymbol(rasterSymbolizer);

        String method = null;
        ContrastEnhancement ce = (ContrastEnhancement) styleFactory.contrastEnhancement(null, method);
        rasterSymbolizer.setContrastEnhancement(ce);
        testObj.updateSymbol(rasterSymbolizer);

        ce = (ContrastEnhancement) styleFactory.contrastEnhancement(null, "logarithmic");
        rasterSymbolizer.setContrastEnhancement(ce);
        testObj.updateSymbol(rasterSymbolizer);

        ce = (ContrastEnhancement) styleFactory.contrastEnhancement(null, "normalize");
        rasterSymbolizer.setContrastEnhancement(ce);
        testObj.updateSymbol(rasterSymbolizer);
    }

}
