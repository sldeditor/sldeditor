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
package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

/**
 * Class that contains configuration data describing a WKT type.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WKTType
{

    /** The name. */
    private String name;

    /** The multiple coordinates. */
    private boolean multipleCoordinates = false;

    /** The number of points. */
    private int numOfPoints = 0;

    /** The list item. */
    private String listItem = "Item";

    /** The can have multiple shapes. */
    private boolean canHaveMultipleShapes = false;

    /** The do first and last point have to be same. */
    private boolean doFirstLastHaveToBeSame = false;

    /**
     * Instantiates a new wkt type.
     *
     * @param name the name
     * @param multipleCoordinates the multiple coordinates
     * @param numOfPoints the num of points
     * @param listItem the list item
     * @param canHaveMultipleShapes the can have multiple shapes
     * @param doFirstLastHaveToBeSame the do first last have to be same
     */
    public WKTType(String name, 
            boolean multipleCoordinates,
            int numOfPoints,
            String listItem, 
            boolean canHaveMultipleShapes,
            boolean doFirstLastHaveToBeSame)
    {
        super();
        this.name = name;
        this.multipleCoordinates = multipleCoordinates;
        this.numOfPoints = numOfPoints;
        this.listItem = listItem;
        this.canHaveMultipleShapes = canHaveMultipleShapes;
        this.doFirstLastHaveToBeSame = doFirstLastHaveToBeSame;
    }

    /**
     * Instantiates a new wkt type.
     *
     * @param name the name
     * @param multipleCoordinates the multiple coordinates
     * @param numOfPoints the num of points
     * @param listItem the list item
     */
    public WKTType(String name, boolean multipleCoordinates, int numOfPoints, String listItem, boolean doFirstLastHaveToBeSame) {
        this(name, multipleCoordinates, numOfPoints, listItem ,false, doFirstLastHaveToBeSame);
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if is multiple coordinates.
     *
     * @return true, if is multiple coordinates
     */
    public boolean isMultipleCoordinates() {
        return multipleCoordinates;
    }

    /**
     * To string.
     *
     * @return the string
     */
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the number of points. -1 means multiple
     *
     * @return the number of points
     */
    public int getNumOfPoints() {
        return numOfPoints;
    }

    /**
     * Gets the list item.
     *
     * @return the list item
     */
    public String getListItem() {
        return listItem;
    }

    /**
     * Checks if shape type can have multiple shapes.
     *
     * @return the canHaveMultipleShapes flag
     */
    public boolean canHaveMultipleShapes() {
        return canHaveMultipleShapes;
    }

    /**
     * Do first last have to be same.
     *
     * @return true, if successful
     */
    public boolean doFirstLastHaveToBeSame() {
        return doFirstLastHaveToBeSame;
    }
};