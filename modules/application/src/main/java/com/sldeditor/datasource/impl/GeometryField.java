/**
 * 
 */

package com.sldeditor.datasource.impl;

/**
 * The Class GeometryField.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeometryField {

    /** The geometry field name. */
    private String geometryFieldName;

    /** The Constant DEFAULT_GEOMETRY_FIELD_NAME. */
    private static final String DEFAULT_GEOMETRY_FIELD_NAME = "geom";

    /**
     * Default constructor.
     */
    public GeometryField() {
        reset();
    }

    /**
     * Reset to default value.
     */
    public void reset() {
        geometryFieldName = DEFAULT_GEOMETRY_FIELD_NAME;
    }

    /**
     * Gets the geometry field name.
     *
     * @return the geometryFieldName
     */
    public String getGeometryFieldName() {
        return geometryFieldName;
    }

    /**
     * Sets the geometry field name.
     *
     * @param geometryFieldName the geometryFieldName to set
     */
    public void setGeometryFieldName(String geometryFieldName) {
        this.geometryFieldName = geometryFieldName;
    }
}
