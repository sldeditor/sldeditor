/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.ui.tree;

import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;

import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * The Class SLDTreeSymbolizerButtonState.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeSymbolizerButtonState {

    /** The geometry type. */
    private GeometryTypeEnum geometryType;

    /** The overall enable symbolizers override. */
    private boolean overallEnableSymbolizersOverride = false;

    /**
     * Sets the geometry type.
     *
     * @param geometryType the new geometry type
     */
    public void setGeometryType(GeometryTypeEnum geometryType) {
        this.geometryType = geometryType;

        overallEnableSymbolizersOverride = false;
    }

    /**
     * Show symbolizer buttons.
     */
    public void showSymbolizerButtons() {
        overallEnableSymbolizersOverride = true;
    }

    /**
     * Checks if marker button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is marker visible
     */
    public boolean isMarkerVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            enabled = (obj instanceof Rule) && (geometryType == GeometryTypeEnum.POINT);
        }
        return enabled;
    }

    /**
     * Checks if line button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is line visible
     */
    public boolean isLineVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            enabled = (obj instanceof Rule) && (geometryType == GeometryTypeEnum.LINE);
        }
        return enabled;
    }

    /**
     * Checks if polygon button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is polygon visible
     */
    public boolean isPolygonVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            enabled = (obj instanceof Rule) && (geometryType == GeometryTypeEnum.POLYGON);
        }
        return enabled;
    }

    /**
     * Checks if raster button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is raster visible
     */
    public boolean isRasterVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            enabled = (obj instanceof Rule) && (geometryType == GeometryTypeEnum.RASTER);
        }
        return enabled;
    }

    /**
     * Checks if text button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is text visible
     */
    public boolean isTextVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            enabled = (obj instanceof Rule);
        }
        return enabled;
    }

    /**
     * Checks if image outline line button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is image outline line visible
     */
    public boolean isImageOutlineLineVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            if(obj instanceof RasterSymbolizer)
            {
                RasterSymbolizer raster = (RasterSymbolizer) obj;
                enabled = (raster.getImageOutline() == null) && (geometryType == GeometryTypeEnum.RASTER);
            }
        }
        return enabled;
    }

    /**
     * Checks if image outline polygon button is visible.
     *
     * @param parentObj the parent obj
     * @param obj the obj
     * @return true, if is image outline polygon visible
     */
    public boolean isImageOutlinePolygonVisible(Object parentObj, Object obj) {
        boolean enabled = false;
        if(overallEnableSymbolizersOverride)
        {
            if(obj instanceof RasterSymbolizer)
            {
                RasterSymbolizer raster = (RasterSymbolizer) obj;
                enabled = (raster.getImageOutline() == null) && (geometryType == GeometryTypeEnum.RASTER);
            }
        }
        return enabled;
    }

}
