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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.geotools.styling.SLDInlineFeatureParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.UserLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sldeditor.common.console.ConsoleManager;

/**
 * Utility methods to read/write inline features as GML
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class InlineFeatureUtils {

    private static final String GML_FEATURE_FID_END = "</gml:_Feature>";
    private static final String FEATURE_FID_WITHOUT_PREFIX_END = "</:_Feature>";
    private static final String GML_FEATURE_FID = "<gml:_Feature fid";
    private static final String FEATURE_FID_WITHOUT_PREFIX = "<_Feature fid";
    private static final String SLD_ROOT_ELEMENT_END = "</sld:StyledLayerDescriptor>";
    private static final String SLD_USER_LAYER_END = "</sld:UserLayer>";
    private static final String SLD_USER_LAYER_START = "<sld:UserLayer>";
    private static final String SLD_ROOT_ELEMENT = "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">";
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String GML_START = "<FeatureCollection>";
    private static final String GML_END = "</FeatureCollection>";

    /**
     * Gets the inline features text.
     *
     * @param userLayer the user layer
     * @return the inline features text
     */
    public static String getInlineFeaturesText(UserLayer userLayer)
    {
        // Inline features
        SLDTransformer transform = new SLDTransformer();

        StringWriter stringWriter = new StringWriter();
        try {
            transform.setIndentation(2);
            transform.setOmitXMLDeclaration(true);
            transform.createTransformer();
            transform.transform(userLayer, stringWriter);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        String userLayerXML = stringWriter.toString();

        int beginIndex = userLayerXML.indexOf(GML_START);
        StringBuilder sb = new StringBuilder();
        int index = beginIndex - 1;
        while(index > 0)
        {
            if(userLayerXML.charAt(index) != ' ')
            {
                break;
            }
            else
            {
                sb.append(" ");
                index --;
            }
        }

        int endIndex = userLayerXML.lastIndexOf(GML_END) + GML_END.length();

        String extract = userLayerXML.substring(beginIndex, endIndex);

        extract = extract.replace(sb.toString(), "");
        return(extract);

    }

    /**
     * Sets the inline features.
     *
     * @param userLayer the user layer
     * @param inlineFeatures the inline features
     */
    public static void setInlineFeatures(UserLayer userLayer, String inlineFeatures) {
        if(userLayer == null)
        {
            return;
        }

        if(inlineFeatures == null)
        {
            return;
        }

        try
        {
            StringBuilder sb = new StringBuilder();

            // To extract inline features need to find the XML Node, so fake an XML document
            sb.append(XML_HEADER);
            sb.append(SLD_ROOT_ELEMENT);
            sb.append(SLD_USER_LAYER_START);

            // The hack, put the gml namespace prefix back otherwise the XML parsing fails
            inlineFeatures = inlineFeatures.replace(FEATURE_FID_WITHOUT_PREFIX, GML_FEATURE_FID);
            inlineFeatures = inlineFeatures.replace(FEATURE_FID_WITHOUT_PREFIX_END, GML_FEATURE_FID_END);
            sb.append(inlineFeatures);
            sb.append(SLD_USER_LAYER_END);
            sb.append(SLD_ROOT_ELEMENT_END);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(sb.toString()));
            Document doc = builder.parse(is);

            Node root = doc.getDocumentElement();
            SLDInlineFeatureParser inparser = null;
            try {
                inparser = new SLDInlineFeatureParser(root);
                userLayer.setInlineFeatureDatastore(inparser.dataStore);
                userLayer.setInlineFeatureType(inparser.featureType);
            } catch (Exception e) {
                ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
            }
        }
        catch(IOException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
        } catch (SAXException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
        } catch (ParserConfigurationException e) {
            ConsoleManager.getInstance().exception(InlineFeatureUtils.class, e);
        }
    }
}