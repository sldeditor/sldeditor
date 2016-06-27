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
package com.sldeditor.tool.legendpanel;

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.TiledImage;

import org.geotools.image.ImageWorker;
import org.geotools.image.palette.CustomPaletteBuilder;
import org.geotools.image.palette.InverseColorMapOp;

/**
 * Provides utility methods for the shared handling of images by the raster map
 * and legend producers.
 * 
 * @author Gabriel Roldan
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @version $Id$
 */
public class ImageUtils {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses.wms.map");

    /**
     * Forces the use of the class as a pure utility methods one by declaring a
     * private default constructor.
     */
    private ImageUtils() {
        // do nothing
    }

    /**
     * Sets up a {@link BufferedImage#TYPE_4BYTE_ABGR} if the paletteInverter is
     * not provided, or a indexed image otherwise. Subclasses may override this
     * method should they need a special kind of image
     *
     * @param width the width of the image to create.
     * @param height the height of the image to create.
     * @param palette the palette
     * @param transparent the transparent
     * @return an image of size <code>width x height</code> appropriate for
     *         the given colour model, if any, and to be used as a transparent
     *         image or not depending on the <code>transparent</code>
     *         parameter.
     */
    public static BufferedImage createImage(final int width, final int height,
            final IndexColorModel palette, final boolean transparent) {
        // WARNING: whenever this method is changed, change getDrawingSurfaceMemoryUse
        // accordingly
        if (palette != null) {
            // unfortunately we can't use packed rasters because line rendering
            // gets completely
            // broken, see GEOS-1312 (http://jira.codehaus.org/browse/GEOS-1312)
            // final WritableRaster raster =
            // palette.createCompatibleWritableRaster(width, height);
            final WritableRaster raster = Raster.createInterleavedRaster(palette.getTransferType(),
                    width, height, 1, null);
            return new BufferedImage(palette, raster, false, null);
        }

        if (transparent) {
            return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        }
        // don't use alpha channel if the image is not transparent (load testing shows this
        // image setup is the fastest to draw and encode on
        return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

    }
    
    /**
     * Computes the memory usage of the buffered image used as the drawing
     * surface. 
     *
     * @param width the width
     * @param height the height
     * @param palette the palette
     * @param transparent the transparent
     * @return the drawing surface memory use
     */
    public static long getDrawingSurfaceMemoryUse(final int width, final int height,
            final IndexColorModel palette, final boolean transparent) {
        long memory = width * height;
        if (palette != null) {
            return memory;
        } 
        if (transparent) {
            return memory * 4;
        }
        return memory * 3;
    }

    /**
     * Sets up and returns a {@link Graphics2D} for the given
     * <code>preparedImage</code>, which is already prepared with a
     * transparent background or the given background color.
     * 
     * @param transparent
     *            whether the graphics is transparent or not.
     * @param bgColor
     *            the background color to fill the graphics with if its not
     *            transparent.
     * @param preparedImage
     *            the image for which to create the graphics.
     * @param extraHints
     *            an optional map of extra rendering hints to apply to the
     *            {@link Graphics2D}, other than
     *            {@link RenderingHints#KEY_ANTIALIASING}.
     * @return a {@link Graphics2D} for <code>preparedImage</code> with
     *         transparent background if <code>transparent == true</code> or
     *         with the background painted with <code>bgColor</code>
     *         otherwise.
     */
    public static Graphics2D prepareTransparency(
            final boolean transparent, 
            final Color bgColor,
            final RenderedImage preparedImage, 
            final Map<RenderingHints.Key, Object> extraHints) {
        final Graphics2D graphic;

        if (preparedImage instanceof BufferedImage) {
            graphic = ((BufferedImage) preparedImage).createGraphics();
        } else if (preparedImage instanceof TiledImage) {
            graphic = ((TiledImage) preparedImage).createGraphics();
        } else if (preparedImage instanceof VolatileImage) {
            graphic = ((VolatileImage) preparedImage).createGraphics();
        } else {
            throw new ServiceException("Unrecognized back-end image type");
        }

        // fill the background with no antialiasing
        Map<RenderingHints.Key, Object> hintsMap;
        if (extraHints == null) {
            hintsMap = new HashMap<RenderingHints.Key, Object>();
        } else {
            hintsMap = new HashMap<RenderingHints.Key, Object>(extraHints);
        }
        hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphic.setRenderingHints(hintsMap);
        if (transparent) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("setting to transparent");
            }

            int type = AlphaComposite.SRC;
            graphic.setComposite(AlphaComposite.getInstance(type));

            Color c = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 0);
            graphic.setBackground(bgColor);
            graphic.setColor(c);
            graphic.fillRect(0, 0, preparedImage.getWidth(), preparedImage.getHeight());
            type = AlphaComposite.SRC_OVER;
            graphic.setComposite(AlphaComposite.getInstance(type));
        } else {
            graphic.setColor(bgColor);
            graphic.fillRect(0, 0, preparedImage.getWidth(), preparedImage.getHeight());
        }
        return graphic;
    }
    
    
    /**
     * Force indexed8 bitmask.
     *
     * @param originalImage the original image
     * @param invColorMap may be {@code null}
     * @return the rendered image
     */
    public static RenderedImage forceIndexed8Bitmask(RenderedImage originalImage, final InverseColorMapOp invColorMap) {
        // /////////////////////////////////////////////////////////////////
        //
        // Check what we need to do depending on the color model of the image we
        // are working on.
        //
        // /////////////////////////////////////////////////////////////////
        final ColorModel cm = originalImage.getColorModel();
        final boolean dataTypeByte = originalImage.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE;
        RenderedImage image;

        // /////////////////////////////////////////////////////////////////
        //
        // IndexColorModel and DataBuffer.TYPE_BYTE
        //
        // ///
        //
        // If we got an image whose color model is already indexed on 8 bits
        // we have to check if it is bitmask or not.
        //
        // /////////////////////////////////////////////////////////////////
        if ((cm instanceof IndexColorModel) && dataTypeByte) {
            final IndexColorModel icm = (IndexColorModel) cm;

            if (icm.getTransparency() != Transparency.TRANSLUCENT) {
                // //
                //
                // The image is indexed on 8 bits and the color model is either
                // opaque or bitmask. WE do not have to do anything.
                //
                // //
                image = originalImage;
            } else {
                // //
                //
                // The image is indexed on 8 bits and the color model is
                // Translucent, we have to perform some color operations in
                // order to convert it to bitmask.
                //
                // //
                image = new ImageWorker(originalImage).forceBitmaskIndexColorModel().getRenderedImage();
            }
        } else {
            // /////////////////////////////////////////////////////////////////
            //
            // NOT IndexColorModel and DataBuffer.TYPE_BYTE
            //
            // ///
            //
            // We got an image that needs to be converted.
            //
            // /////////////////////////////////////////////////////////////////
            image = new ImageWorker(originalImage).rescaleToBytes().getRenderedImage();
            if (invColorMap != null) {

                // make me parametric which means make me work with other image
                // types
                image = invColorMap.filterRenderedImage(image);
            } else {
                // //
                //
                // We do not have a paletteInverter, let's create a palette that
                // is as good as possible.
                //
                // //
                // make sure we start from a componentcolormodel.
                image = new ImageWorker(image).forceComponentColorModel().getRenderedImage();

                // //
                //
                // Build the CustomPaletteBuilder doing some good subsampling.
                //
                // //
                int subsx = 1 + (int) (Math.log(image.getWidth()) / Math.log(32));
                int subsy = 1 + (int) (Math.log(image.getHeight()) / Math.log(32));
                image = new CustomPaletteBuilder(image, 256, subsx, subsy, 1).buildPalette().getIndexedImage();
            }
        }

        return image;
    }
}