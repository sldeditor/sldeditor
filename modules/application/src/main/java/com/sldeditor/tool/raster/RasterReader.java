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

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.CommonFactoryFinder;
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
import org.opengis.style.ContrastMethod;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.common.utils.ExternalFilenames;

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
        if(rasterFile == null)
        {
            return null;
        }

        StyledLayerDescriptor sld = null;

        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        AbstractGridCoverage2DReader reader = null;
        
        try
        {
            reader = format.getReader(rasterFile);
        }
        catch(UnsupportedOperationException e)
        {
            ConsoleManager.getInstance().error(this, 
                    Localisation.getField(RasterTool.class, "RasterReader.unknownFormat") + rasterFile.getAbsolutePath());
            return null;
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(rasterFile);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
            return null;
        }

        WritableRaster raster = img.getRaster();

        Style style = createRGBStyle(reader, raster);
        sld = sf.createStyledLayerDescriptor();
        NamedLayer namedLayer = sf.createNamedLayer();
        namedLayer.addStyle(style);
        sld.addStyledLayer(namedLayer);

        File sldFilename = ExternalFilenames.createSLDFilename(rasterFile);

        StyleWrapper styleWrapper = new StyleWrapper(sldFilename.getName());
        String sldContents = sldWriter.encodeSLD(null, sld);
        SLDData sldData = new SLDData(styleWrapper, sldContents);
        sldData.setSLDFile(sldFilename);
        sldData.setReadOnly(false);

        return sldData;
    }

    /**
     * Creates the rgb style.
     *
     * @param reader the reader
     * @param raster 
     * @return the style
     */
    private Style createRGBStyle(AbstractGridCoverage2DReader reader, WritableRaster raster) {
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
            createRGBImageSymbol(sym, cov, raster);
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
     * @param raster 
     */
    private void createRGBImageSymbol(RasterSymbolizer sym, GridCoverage2D cov, WritableRaster raster) {
        double dest;
        List<Double> valueList = new ArrayList<Double>();

        GridEnvelope2D gridRange2D = cov.getGridGeometry().getGridRange2D();
        for(int x = 0; x < gridRange2D.getWidth(); x++)
        {
            for(int y = 0; y < gridRange2D.getHeight(); y++)
            {
                try {
                    dest = raster.getSampleDouble(x, y, 0);

                    if(!valueList.contains(dest))
                    {
                        valueList.add(dest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ColorMapImpl colourMap = new ColorMapImpl();

        // Sort the unique sample values in ascending order
        Collections.sort(valueList);

        // Create colour amp entries in the colour map for all the sample values
        for(Double value : valueList)
        {
            ColorMapEntry entry = new ColorMapEntryImpl();
            Literal colourExpression = ff.literal(ColourUtils.fromColour(ColourUtils.createRandomColour()));
            entry.setColor(colourExpression);
            entry.setQuantity(ff.literal(value.doubleValue()));

            colourMap.addColorMapEntry(entry);
        }

        colourMap.setType(ColorMap.TYPE_VALUES);
        sym.setColorMap(colourMap);
    }

    /**
     * Creates the RGB channel symbol.
     *
     * @param sym the sym
     * @param cov the cov
     * @param numBands the num bands
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
