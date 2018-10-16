/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.common.output.impl;

import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Style;
import org.geotools.styling.UserLayer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;

/**
 * The Class InlineDatastoreVisitor.
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineDatastoreVisitor extends DuplicatingStyleVisitor {

    /** Instantiates a new inline datastore visitor. */
    public InlineDatastoreVisitor() {
        // Default constructor
    }

    /**
     * (non-Javadoc)
     *
     * @see
     *     org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.UserLayer)
     */
    public void visit(UserLayer layer) {

        Style[] style = layer.getUserStyles();
        int length = style.length;
        Style[] styleCopy = new Style[length];
        for (int i = 0; i < length; i++) {
            if (style[i] != null) {
                style[i].accept(this);
                styleCopy[i] = (Style) pages.pop();
            }
        }

        FeatureTypeConstraint[] lfc = layer.getLayerFeatureConstraints();
        FeatureTypeConstraint[] lfcCopy = new FeatureTypeConstraint[lfc.length];

        length = lfc.length;
        for (int i = 0; i < length; i++) {
            if (lfc[i] != null) {
                lfc[i].accept(this);
                lfcCopy[i] = (FeatureTypeConstraint) pages.pop();
            }
        }

        UserLayer copy = sf.createUserLayer();
        copy.setName(layer.getName());
        copy.setUserStyles(styleCopy);
        copy.setLayerFeatureConstraints(lfcCopy);

        // Reuse the existing inline feature data store
        copy.setInlineFeatureDatastore(layer.getInlineFeatureDatastore());
        copy.setInlineFeatureType(layer.getInlineFeatureType());

        if (STRICT && !copy.equals(layer)) {
            throw new IllegalStateException("Was unable to duplicate provided UserLayer:" + layer);
        }
        pages.push(copy);
    }
}
