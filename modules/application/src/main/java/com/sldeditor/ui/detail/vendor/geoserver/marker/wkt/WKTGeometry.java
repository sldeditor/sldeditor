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

import com.sldeditor.common.localisation.Localisation;

/**
 * Class that represents a WKT geometry.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WKTGeometry {

    /** The geometry type. */
    private WktType geometryType = null;

    /** The segment list. */
    private List<List<WKTSegmentList>> segmentList = new ArrayList<List<WKTSegmentList>>();

    /** The is valid flag. */
    private boolean valid = true;

    /**
     * Checks if is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the valid.
     *
     * @param valid the new valid
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets the geometry type.
     *
     * @return the geometry type
     */
    public WktType getGeometryType() {
        return geometryType;
    }

    /**
     * Sets the geometry type.
     *
     * @param geometryType the new geometry type
     */
    public void setGeometryType(WktType geometryType) {
        this.geometryType = geometryType;
    }

    /**
     * Gets the no of segments.
     *
     * @return the no of segments
     */
    public int getNoOfSegments()
    {
        return segmentList.size();
    }

    /**
     * Gets the point list.
     *
     * @param index the index
     * @return the segment list
     */
    public List<WKTSegmentList> getSegmentList(int index) {
        if((index < 0) || (index >= segmentList.size()))
        {
            return null;
        }
        return segmentList.get(index);
    }

    /**
     * Adds the segment list.
     *
     * @param index the index
     * @param segmentListToAdd the segment list to add
     * @return the index of the segment added
     */
    public int addSegmentList(int index, WKTSegmentList segmentListToAdd) {
        List<WKTSegmentList> localSegmentList = null;

        if(index >= segmentList.size())
        {
            localSegmentList = new ArrayList<WKTSegmentList>();
            segmentList.add(localSegmentList);
        }
        else
        {
            localSegmentList = segmentList.get(index);
        }

        localSegmentList.add(segmentListToAdd);
        
        return localSegmentList.size() - 1;
    }

    /**
     * Adds a new segment.
     *
     * @return the int
     */
    public int addNewSegment() {

        return addNewSegment(0);
    }

    /**
     * Adds a new segment.
     *
     * @param index the index
     * @return the index of the segement added
     */
    public int addNewSegment(int index) {

        WKTSegmentList newSegmentList = new WKTSegmentList();
        newSegmentList.addPoint(new WKTPoint());
        if(geometryType.getNumOfPoints() > 1)
        {
            newSegmentList.addPoint(new WKTPoint());
        }
        return addSegmentList(index, newSegmentList);
    }

    /**
     * Adds the new shape.
     *
     * @return the index of the added shape
     */
    public int addNewShape() {
        WKTSegmentList newSegmentList = new WKTSegmentList();
        newSegmentList.addPoint(new WKTPoint());
        if(geometryType.getNumOfPoints() > 1)
        {
            newSegmentList.addPoint(new WKTPoint());
        }
        addSegmentList(segmentList.size(), newSegmentList);

        return segmentList.size();
    }
    
    /**
     * Removes the segment at the specified index.
     *
     * @param shapeIndex the shape index
     * @param segmentIndex the segment index
     */
    public void removeSegment(int shapeIndex, int segmentIndex) {
        List<WKTSegmentList> shapeList = segmentList.get(shapeIndex);
        
        if((segmentIndex >= 0) && (segmentIndex < shapeList.size()))
        {
            shapeList.remove(segmentIndex);
        }
    }

    /**
     * Removes the shape.
     *
     * @param shapeIndex the shape index
     */
    public void removeShape(int shapeIndex) {
        if((shapeIndex >= 0) && (shapeIndex < segmentList.size()))
        {
            segmentList.remove(shapeIndex);
        }
    }
    /**
     * Gets the multi shape name.
     *
     * @param index the index
     * @return the multi shape name
     */
    public String getMultiShapeName(int index) {
        return String.format("%s %d", geometryType.getListItem(), index + 1);
    }

    /**
     * Gets the segment name, caters for a multi shape object or not.
     *
     * @param index the index
     * @return the segment name
     */
    public String getSegmentName(int index) {
        if(geometryType.canHaveMultipleShapes())
        {
            return String.format("%s %d", Localisation.getString(WKTGeometry.class, "WKTDialog.partShape"), index + 1);
        }
        else
        {
            return String.format("%s %d", geometryType.getListItem(), index + 1);
        }
    }

    /**
     * Checks if WKT shape empty, no type set and no multi shapes/segments set
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return (geometryType == null) && segmentList.isEmpty();
    }

}
