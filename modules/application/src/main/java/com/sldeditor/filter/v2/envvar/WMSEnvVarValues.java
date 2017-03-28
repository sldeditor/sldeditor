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

package com.sldeditor.filter.v2.envvar;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * The Class WMSEnvVarValues, stores values for the WMS environment variable values.
 *
 * @author Robert Ward (SCISYS)
 */
public class WMSEnvVarValues {

    /** The image width. */
    private int imageWidth = 0;

    /** The image height. */
    private int imageHeight = 0;

    /** The map bounds. */
    private ReferencedEnvelope mapBounds = null;

    /**
     * Instantiates a new WMS env var values.
     */
    public WMSEnvVarValues() {
    }

    /**
     * Gets the image width.
     *
     * @return the imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Sets the image width.
     *
     * @param imageWidth the imageWidth to set
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * Gets the image height.
     *
     * @return the imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Sets the image height.
     *
     * @param imageHeight the imageHeight to set
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * Gets the map bounds.
     *
     * @return the mapBounds
     */
    public ReferencedEnvelope getMapBounds() {
        return mapBounds;
    }

    /**
     * Sets the map bounds.
     *
     * @param mapBounds the mapBounds to set
     */
    public void setMapBounds(ReferencedEnvelope mapBounds) {
        this.mapBounds = mapBounds;
    }

}
