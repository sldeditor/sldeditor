/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.datasource.impl;

import java.util.logging.Logger;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class ExtendedSimpleFeatureTypeBuilder extends SimpleFeatureTypeBuilder so
 * that a AttributeDescriptor object can be created. This then allows
 * it be added using SimpleFeatureTypeBuilder.add( int index, AttributeDescriptor descriptor ).
 * The GeoTools class suggests use of this method is
 * discouraged it allows the order of the fields to be preserved.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtendedSimpleFeatureTypeBuilder extends SimpleFeatureTypeBuilder {

    /** The logger. */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.feature");

    /**
     * Instantiates a new extended simple feature type builder.
     */
    public ExtendedSimpleFeatureTypeBuilder() {
    }

    /**
     * Instantiates a new extended simple feature type builder.
     *
     * @param factory the factory
     */
    public ExtendedSimpleFeatureTypeBuilder(FeatureTypeFactory factory) {
        super(factory);
    }

    /**
     * Creates the attribute descriptor.
     *
     * @param name the name
     * @param binding the binding
     * @return the attribute descriptor
     */
    public AttributeDescriptor createAttributeDescriptor(String name, Class<?> binding) {

        AttributeDescriptor descriptor = null;

        attributeBuilder.setBinding(binding);
        attributeBuilder.setName(name);

        // check if this is the name of the default geometry, in that case we
        // better make it a geometry type
        // also check for jts geometry, if we ever actually get to a point where a
        // feature can be backed by another geometry model (like iso), we need
        // to remove this check
        //
        if ((defaultGeometry != null && defaultGeometry.equals(name))
                || Geometry.class.isAssignableFrom(binding)) {

            // if no crs was set, set to defaultCRS
            if (!attributeBuilder.isCRSSet()) {
                if (defaultCrs == null && !defaultCrsSet) {
                    LOGGER.fine("Creating " + name
                            + " with null CoordinateReferenceSystem - did you mean to setCRS?");
                }
                attributeBuilder.setCRS(defaultCrs);
            }

            GeometryType type = attributeBuilder.buildGeometryType();
            descriptor = attributeBuilder.buildDescriptor(name, type);
        } else {
            AttributeType type = attributeBuilder.buildType();
            descriptor = attributeBuilder.buildDescriptor(name, type);
        }

        return (descriptor);
    }

}
