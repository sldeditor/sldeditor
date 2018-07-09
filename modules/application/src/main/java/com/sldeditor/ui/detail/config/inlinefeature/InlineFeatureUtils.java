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

package com.sldeditor.ui.detail.config.inlinefeature;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.datasource.impl.GeometryTypeMapping;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.styling.SLDInlineFeatureParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Utility methods to read/write inline features as GML.
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineFeatureUtils {

    /** The Constant GML_NAMESPACE_PREFIX. */
    private static final String GML_NAMESPACE_PREFIX = "gml:";

    /** The Constant SLD_INLINE_FEATURE_START. */
    private static final String SLD_INLINE_FEATURE_START = "<sld:InlineFeature>";

    /** The Constant SLD_INLINE_FEATURE_END. */
    private static final String SLD_INLINE_FEATURE_END = "</sld:InlineFeature>";

    /** The Constant GML_FEATURE_FID_END. */
    private static final String GML_FEATURE_FID_END = "</gml:_Feature>";

    /** The Constant FEATURE_FID_WITHOUT_PREFIX_END. */
    private static final String FEATURE_FID_WITHOUT_PREFIX_END = "</:_Feature>";

    /** The Constant GML_FEATURE_FID. */
    private static final String GML_FEATURE_FID = "<gml:_Feature fid";

    /** The Constant FEATURE_FID_WITHOUT_PREFIX. */
    private static final String FEATURE_FID_WITHOUT_PREFIX = "<_Feature fid";

    /** The Constant SLD_ROOT_ELEMENT_END. */
    private static final String SLD_ROOT_ELEMENT_END = "</sld:StyledLayerDescriptor>";

    /** The Constant SLD_USER_LAYER_END. */
    private static final String SLD_USER_LAYER_END = "</sld:UserLayer>";

    /** The Constant SLD_USER_LAYER_START. */
    private static final String SLD_USER_LAYER_START = "<sld:UserLayer>";

    /** The Constant SLD_ROOT_ELEMENT. */
    private static final String SLD_ROOT_ELEMENT =
            "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">";

    /** The Constant XML_HEADER. */
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /** The Constant GML_START. */
    private static final String GML_START = "<FeatureCollection>";

    /** The Constant GML_END. */
    private static final String GML_END = "</FeatureCollection>";

    /**
     * Gets the inline features text.
     *
     * @param userLayer the user layer
     * @return the inline features text
     */
    public static String getInlineFeaturesText(UserLayer userLayer) {
        if (userLayer == null) {
            return "";
        }

        // Handle the case where there is a featurecollection but no features
        if (userLayer.getInlineFeatureDatastore() != null) {
            String typeName = userLayer.getInlineFeatureType().getTypeName();
            SimpleFeatureSource featureSource;
            try {
                featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);
                if (featureSource.getFeatures().isEmpty()) {
                    return "";
                }
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
                return "";
            }
        }

        // Inline features
        SLDTransformer transform = new SLDTransformer();

        StringWriter stringWriter = new StringWriter();
        try {
            transform.setIndentation(2);
            transform.setOmitXMLDeclaration(true);
            transform.createTransformer();
            transform.transform(userLayer, stringWriter);
        } catch (TransformerException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
            return null;
        }

        String userLayerXML = stringWriter.toString();

        // Check to see if there are any inline features
        if (!userLayerXML.contains(SLD_INLINE_FEATURE_START)) {
            return "";
        }

        int beginIndex = userLayerXML.indexOf(GML_START);
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        int index = beginIndex - 1;
        while (index > 0) {
            if (userLayerXML.charAt(index) != ' ') {
                break;
            } else {
                sb.append(" ");
                index--;
            }
        }

        int endIndex = userLayerXML.lastIndexOf(GML_END) + GML_END.length();

        if (beginIndex < 0) {
            beginIndex = 0;
        }
        String extract = userLayerXML.substring(beginIndex, endIndex);

        extract = extract.replace(sb.toString(), "\n");

        return (extract);
    }

    /**
     * Sets the inline features.
     *
     * @param userLayer the user layer
     * @param inlineFeatures the inline features
     */
    public static void setInlineFeatures(UserLayer userLayer, String inlineFeatures) {
        if (userLayer == null) {
            return;
        }

        if (inlineFeatures == null) {
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();

            // To extract inline features need to find the XML Node, so fake an XML document
            sb.append(XML_HEADER);
            sb.append(SLD_ROOT_ELEMENT);
            sb.append(SLD_USER_LAYER_START);
            sb.append(SLD_INLINE_FEATURE_START);

            // There is an issue with the inline features parser, when generating the
            // xml it adds gml: namespace prefixes and the parser can't read them!

            // Remove gml namespace prefix
            inlineFeatures = inlineFeatures.replace(GML_NAMESPACE_PREFIX, "");

            // The hack, put the gml namespace prefix back otherwise the XML parsing fails
            inlineFeatures = inlineFeatures.replace(FEATURE_FID_WITHOUT_PREFIX, GML_FEATURE_FID);
            inlineFeatures =
                    inlineFeatures.replace(FEATURE_FID_WITHOUT_PREFIX_END, GML_FEATURE_FID_END);

            // Remove empty namespace prefixes
            inlineFeatures = inlineFeatures.replace("<:", "<");
            inlineFeatures = inlineFeatures.replace("</:", "</");

            sb.append(inlineFeatures);
            sb.append(SLD_INLINE_FEATURE_END);
            sb.append(SLD_USER_LAYER_END);
            sb.append(SLD_ROOT_ELEMENT_END);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(sb.toString()));
            Document doc = builder.parse(is);

            Node root = doc.getDocumentElement();
            Node userLayerNode = root.getFirstChild();
            Node inlineFeatureNode = userLayerNode.getFirstChild();

            SLDInlineFeatureParser inparser = null;
            try {
                inparser = new SLDInlineFeatureParser(inlineFeatureNode);
                userLayer.setInlineFeatureDatastore(inparser.dataStore);
                userLayer.setInlineFeatureType(inparser.featureType);
            } catch (Exception e) {
                ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
            }
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
        } catch (SAXException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
        } catch (ParserConfigurationException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
        }
    }

    /**
     * Checks to see if SLD contains inline features.
     *
     * @param sld the sld
     * @return true, if sld contains inline features
     */
    public static boolean containsInLineFeatures(StyledLayerDescriptor sld) {
        if (sld != null) {
            for (StyledLayer layer : sld.layers()) {
                if (layer instanceof UserLayer) {
                    UserLayer userLayer = (UserLayer) layer;

                    if (userLayer.getInlineFeatureDatastore() != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Extract user layers from an SLD.
     *
     * @param sld the sld
     * @return the list of user layers
     */
    public static List<UserLayer> extractUserLayers(StyledLayerDescriptor sld) {
        List<UserLayer> userLayerList = new ArrayList<UserLayer>();

        if (sld != null) {
            for (StyledLayer layer : sld.layers()) {
                if (layer instanceof UserLayer) {
                    UserLayer userLayer = (UserLayer) layer;

                    userLayerList.add(userLayer);
                }
            }
        }
        return userLayerList;
    }

    /**
     * Determine geometry type.
     *
     * @param geometryDescriptor the geometry descriptor
     * @param simpleFeatureCollection the simple feature collection
     * @return the geometry type enum
     */
    public static GeometryTypeEnum determineGeometryType(
            GeometryDescriptor geometryDescriptor,
            SimpleFeatureCollection simpleFeatureCollection) {

        if (geometryDescriptor == null) {
            return GeometryTypeEnum.UNKNOWN;
        }

        if (simpleFeatureCollection == null) {
            return GeometryTypeEnum.UNKNOWN;
        }

        Class<?> bindingType = geometryDescriptor.getType().getBinding();

        if (bindingType == Geometry.class) {
            Name geometryName = geometryDescriptor.getName();
            SimpleFeatureIterator iterator = simpleFeatureCollection.features();

            List<GeometryTypeEnum> geometryFeatures = new ArrayList<GeometryTypeEnum>();

            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();

                Object value = feature.getAttribute(geometryName);

                if (value != null) {
                    GeometryTypeEnum geometryType =
                            GeometryTypeMapping.getGeometryType(value.getClass());

                    if (!geometryFeatures.contains(geometryType)) {
                        geometryFeatures.add(geometryType);
                    }
                }
            }
            return (combineGeometryType(geometryFeatures));
        } else {
            return GeometryTypeMapping.getGeometryType(bindingType);
        }
    }

    /**
     * Combine geometry type.
     *
     * @param geometryFeatures the geometry features
     * @return the geometry type enum
     */
    public static GeometryTypeEnum combineGeometryType(List<GeometryTypeEnum> geometryFeatures) {
        if (geometryFeatures != null) {
            if (geometryFeatures.size() == 1) {
                return geometryFeatures.get(0);
            } else {
                return geometryFeatures.get(0);
            }
        }
        return GeometryTypeEnum.UNKNOWN;
    }
}
