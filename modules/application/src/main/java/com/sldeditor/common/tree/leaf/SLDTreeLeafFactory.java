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

package com.sldeditor.common.tree.leaf;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Fill;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.style.Symbolizer;

/**
 * A factory for creating SLDTreeLeaf objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafFactory {

    /** The singleton instance. */
    private static SLDTreeLeafFactory instance = null;

    /** The map. */
    private Map<Class<?>, SLDTreeLeafInterface> map = new HashMap<>();

    /** The style factory. */
    private static StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The logger. */
    private static Logger logger = Logger.getLogger(SLDTreeLeafFactory.class.getName());

    /** Instantiates a new SLD tree leaf factory. */
    private SLDTreeLeafFactory() {
        populate();
    }

    /**
     * Gets the singleton instance of SLDTreeLeafFactory.
     *
     * @return singleton instance of SLDTreeLeafFactory
     */
    public static SLDTreeLeafFactory getInstance() {
        if (instance == null) {
            instance = new SLDTreeLeafFactory();
        }
        return instance;
    }

    /** Populate factory. */
    private void populate() {
        add(new SLDTreeLeafPolygon());
        add(new SLDTreeLeafPoint());
        add(new SLDTreeLeafLine());
        add(new SLDTreeLeafText());
        add(new SLDTreeLeafRaster());
    }

    /**
     * Adds the SLDTreeLeafInterface object.
     *
     * @param obj the obj
     */
    private void add(SLDTreeLeafInterface obj) {
        map.put(obj.getSymbolizer(), obj);
    }

    /**
     * Checks for fill.
     *
     * @param symbolizer the symbolizer
     * @return true, if successful
     */
    public boolean hasFill(Symbolizer symbolizer) {
        boolean result = false;
        if (symbolizer != null) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                result = obj.hasFill(symbolizer);
            }
        }
        return result;
    }

    /**
     * Checks for stroke.
     *
     * @param symbolizer the symbolizer
     * @return true, if successful
     */
    public boolean hasStroke(Symbolizer symbolizer) {
        boolean result = false;
        if (symbolizer != null) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                result = obj.hasStroke(symbolizer);
            }
        }
        return result;
    }

    /**
     * Gets the fill.
     *
     * @param symbolizer the symbolizer
     * @return the fill
     */
    public Fill getFill(Symbolizer symbolizer) {
        Fill fill = null;
        if (symbolizer != null) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                fill = obj.getFill(symbolizer);
            }

            if (fill == null) {
                fill = styleFactory.getDefaultFill();
            }
        }
        return fill;
    }

    /**
     * Gets the stroke.
     *
     * @param symbolizer the symbolizer
     * @return the stroke
     */
    public Stroke getStroke(Symbolizer symbolizer) {
        Stroke stroke = null;
        if (symbolizer != null) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                stroke = obj.getStroke(symbolizer);
            }

            if (stroke == null) {
                stroke = styleFactory.getDefaultStroke();
            }
        }
        return stroke;
    }

    /**
     * Update stroke.
     *
     * @param selected the selected
     * @param symbolizer the symbolizer
     * @return the stroke
     */
    public Stroke updateStroke(boolean selected, Symbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        boolean currentValue = hasStroke(symbolizer);

        if (currentValue != selected) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                if (selected) {
                    logger.debug("Set stroke");
                    obj.createStroke(symbolizer);
                } else {
                    logger.debug("Clear stroke");
                    obj.removeStroke(symbolizer);
                }
            }
        }
        return getStroke(symbolizer);
    }

    /**
     * Update fill.
     *
     * @param selected the selected
     * @param symbolizer the symbolizer
     * @return the fill
     */
    public Fill updateFill(boolean selected, Symbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        boolean currentValue = hasFill(symbolizer);

        if (currentValue != selected) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                if (selected) {
                    logger.debug("Set fill");
                    obj.createFill(symbolizer);
                } else {
                    logger.debug("Clear fill");
                    obj.removeFill(symbolizer);
                }
            }
        }
        return getFill(symbolizer);
    }

    /**
     * Checks if item is selected.
     *
     * @param userObject the user object
     * @param parentSymbolizer the parent symbolizer
     * @return true, if is item selected
     */
    public boolean isItemSelected(Object userObject, Symbolizer parentSymbolizer) {
        boolean selectedItem = false;

        if (userObject instanceof Fill) {
            selectedItem = hasFill(parentSymbolizer);
        } else if (userObject instanceof Stroke) {
            selectedItem = hasStroke(parentSymbolizer);
        }

        return selectedItem;
    }

    /**
     * Gets the raster.
     *
     * @param symbolizer the symbolizer
     * @return the raster
     */
    public RasterSymbolizer getRaster(Symbolizer symbolizer) {
        RasterSymbolizer raster = null;
        if (symbolizer != null) {
            SLDTreeLeafInterface obj = map.get(symbolizer.getClass());
            if (obj != null) {
                raster = obj.getRaster(symbolizer);
            }

            if (raster == null) {
                raster = styleFactory.getDefaultRasterSymbolizer();
            }
        }
        return raster;
    }
}
