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

package com.sldeditor.datasource.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureSource;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayerImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;

/**
 * The Class ExtractValidFieldTypes checks to see if the extracted field data types are correct.
 * If they are not then they are updated. This is a horrible hack but the symbol is rendered
 * and the exception messages returned are parsed to workout what data type should be set.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtractValidFieldTypes {

    /** The Constant UNABLE_TO_DECODE_PREFIX. */
    private static final String UNABLE_TO_DECODE_PREFIX = "Unable to decode '";

    /** The Constant UNABLE_TO_DECODE_SUFFIX. */
    private static final String UNABLE_TO_DECODE_SUFFIX = "' as a number";

    /**
     * Evaluate fields types.
     *
     * @return true, field types updated
     */
    public static boolean fieldTypesUpdated() {
        boolean fieldsUpdated = false;

        SLDStyleFactory styleFactory = new SLDStyleFactory();

        StyledLayerDescriptor sld = SelectedSymbol.getInstance().getSld();

        if (sld != null) {
            List<StyledLayer> styledLayerList = sld.layers();

            for (StyledLayer styledLayer : styledLayerList) {
                List<org.geotools.styling.Style> styleList = null;

                if (styledLayer instanceof NamedLayerImpl) {
                    NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;

                    styleList = namedLayerImpl.styles();
                } else if (styledLayer instanceof UserLayerImpl) {
                    UserLayerImpl userLayerImpl = (UserLayerImpl) styledLayer;

                    styleList = userLayerImpl.userStyles();
                }

                if (styleList != null) {
                    for (Style style : styleList) {
                        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                            for (Rule rule : fts.rules()) {
                                for (Symbolizer symbolizer : rule.symbolizers()) {
                                    FeatureSource<SimpleFeatureType, SimpleFeature> featureList = 
                                            DataSourceFactory.getDataSource().getFeatureSource();

                                    if (featureList != null) {
                                        Object drawMe = null;
                                        try {
                                            drawMe = featureList.getFeatures().features().next();
                                        } catch (NoSuchElementException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            styleFactory.createStyle(drawMe, symbolizer);
                                        } catch (IllegalArgumentException e) {
                                            String message = e.getMessage();
                                            if (message.startsWith(UNABLE_TO_DECODE_PREFIX)
                                                    && message.endsWith(UNABLE_TO_DECODE_SUFFIX)) {
                                                String fieldName = message.substring(
                                                        UNABLE_TO_DECODE_PREFIX.length(),
                                                        message.length()
                                                                - UNABLE_TO_DECODE_SUFFIX.length());

                                                DataSourceFactory.getDataSource()
                                                        .updateFieldType(fieldName, Long.class);
                                                fieldsUpdated = true;
                                            } else {
                                                ConsoleManager.getInstance()
                                                        .exception(ExtractValidFieldTypes.class, e);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return fieldsUpdated;
    }
}
