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
package com.sldeditor.tool.raster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.geometry.DirectPosition;
import org.opengis.style.ContrastMethod;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ColourUtils;

/**
 * The Class RasterReader.
 *
 * @author Robert Ward (SCISYS)
 */
public class RasterReader implements RasterReaderInterface {

    /** The style factory. */
    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    /** The filter factory. */
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /** The sld writer. */
    private SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

    /**
     * Creates the raster sld.
     *
     * @param rasterFile the raster file
     * @return the styled layer descriptor
     */
    @Override
    public SLDDataInterface createRasterSLDData(File rasterFile)
    {
        StyledLayerDescriptor sld = null;

        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        AbstractGridCoverage2DReader reader = format.getReader(rasterFile);

        Style style = createRGBStyle(reader);
        sld = sf.createStyledLayerDescriptor();
        NamedLayer namedLayer = sf.createNamedLayer();
        namedLayer.addStyle(style);
        sld.addStyledLayer(namedLayer);

        StyleWrapper styleWrapper = new StyleWrapper(rasterFile.getName());
        String sldContents = sldWriter.encodeSLD(sld);
        SLDData sldData = new SLDData(styleWrapper, sldContents);
        sldData.setSLDFile(rasterFile);
        sldData.setReadOnly(false);

        return sldData;
    }

    /**
     * Creates the rgb style.
     *
     * @param reader the reader
     * @return the style
     */
    private Style createRGBStyle(AbstractGridCoverage2DReader reader) {
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();

        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3)
        {
            createRGBImageSymbol(sym, cov);
        }
        else
        {
            createRGBChannelSymbol(sym, cov, numBands);
        }
        return SLD.wrapSymbolizers(sym);
    }

    /**
     * Creates the rgb image symbol.
     *
     * @param sym the sym
     * @param cov the cov
     */
    private void createRGBImageSymbol(RasterSymbolizer sym, GridCoverage2D cov) {
        int[] dest = new int[1];
        List<Integer> valueList = new ArrayList<Integer>();

        for(int x = 0; x < cov.getGridGeometry().getGridRange2D().getWidth(); x++)
        {
            for(int y = 0; y < cov.getGridGeometry().getGridRange2D().getHeight(); y++)
            {
                DirectPosition pos = new DirectPosition2D(x, y);
                dest = cov.evaluate(pos, dest);

                if(!valueList.contains(dest[0]))
                {
                    valueList.add(dest[0]);
                }
            }
        }

        ColorMapImpl colourMap = new ColorMapImpl();

        for(Integer value : valueList)
        {
            ColorMapEntry entry = new ColorMapEntryImpl();
            Literal colourExpression = ff.literal(ColourUtils.fromColour(ColourUtils.createRandomColour()));
            entry.setColor(colourExpression);
            entry.setQuantity(ff.literal(value.intValue()));

            colourMap.addColorMapEntry(entry);
        }

        colourMap.setType(ColorMap.TYPE_VALUES);
        sym.setColorMap(colourMap);
    }

    /**
     * @param sym
     * @param cov
     * @param numBands
     */
    private void createRGBChannelSymbol(RasterSymbolizer sym, GridCoverage2D cov, int numBands) {
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }

        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }

        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);
    }
}
