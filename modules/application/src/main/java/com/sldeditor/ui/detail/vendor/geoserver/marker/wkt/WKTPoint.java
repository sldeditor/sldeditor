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

import org.opengis.geometry.DirectPosition;

/**
 * Class that represents a WKT point.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WKTPoint {

    /** The x coordinate. */
    private double x = 0.0;

    /** The y coordinate. */
    private double y = 0.0;

    /**
     * Instantiates a new WKT point.
     *
     * @param pt the pt
     */
    public WKTPoint(DirectPosition pt) {

        if (pt != null) {
            this.x = pt.getCoordinate()[0];
            this.y = pt.getCoordinate()[1];
        }
    }

    /**
     * Default constructor.
     */
    public WKTPoint() {
    }

    /**
     * Gets the x coordinate.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the new x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the new y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        ;
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WKTPoint other = (WKTPoint) obj;
        double diffX = Math.abs(x - other.x);
        if (diffX > 0.0001) {
            return false;
        }
        double diffY = Math.abs(y - other.y);
        if (diffY > 0.0001) {
            return false;
        }
        return true;
    }
}
