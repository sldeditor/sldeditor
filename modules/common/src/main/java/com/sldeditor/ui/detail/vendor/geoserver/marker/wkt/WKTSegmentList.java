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

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a WKT segment lists.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WKTSegmentList {
    
    /** The wkt point list. */
    private List<WKTPoint> wktPointList = null;

    /**
     * Gets the wkt point list.
     *
     * @return the wkt point list
     */
    public List<WKTPoint> getWktPointList() {
        return wktPointList;
    }

    /**
     * Sets the wkt point list.
     *
     * @param wktPointList the new wkt point list
     */
    public void setWktPointList(List<WKTPoint> wktPointList) {
        this.wktPointList = wktPointList;
    }

    /**
     * Adds the point.
     *
     * @param pointToAdd the point to add
     */
    public void addPoint(WKTPoint pointToAdd) {
        if(wktPointList == null)
        {
            wktPointList = new ArrayList<WKTPoint>();
        }
        
        if(!wktPointList.isEmpty())
        {
            if(!pointToAdd.equals(wktPointList.get(wktPointList.size() - 1)))
            {
                wktPointList.add(pointToAdd);
            }
        }
        else
        {
            wktPointList.add(pointToAdd);
        }
    }

    /**
     * Gets the WKT string.
     *
     * @return the WKT string
     */
    public String getWKTString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("(");
        int index = 0;
        for(WKTPoint point : wktPointList)
        {
            if(index > 0)
            {
                sb.append(", ");
            }
            
            sb.append(point.getX());
            sb.append(" ");
            sb.append(point.getY());
            index ++;
        }
        sb.append(")");
        return sb.toString();
    }
}
