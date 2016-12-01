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

package com.sldeditor.ui.detail.vendor.geoserver.marker.arrow;

import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.markwkt.MeteoMarkFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class ArrowUtils, handles deocoding/encoding of arrow to/from strings
 *
 * @author Robert Ward (SCISYS)
 */
public class ArrowUtils {

    /** The Constant HEAD_BASE_RATIO_ATTRIBUTE. */
    private static final String HEAD_BASE_RATIO_ATTRIBUTE = MeteoMarkFactory.ARROWHEAD_BASE_KEY;

    /** The Constant HEIGHT_OVER_WIDTH_ATTRIBUTE. */
    private static final String HEIGHT_OVER_WIDTH_ATTRIBUTE = MeteoMarkFactory.ARROW_HEIGHT_RATIO_KEY;

    /** The Constant ARROW_THICKNESS_ATTRIBUTE. */
    private static final String ARROW_THICKNESS_ATTRIBUTE = MeteoMarkFactory.ARROW_THICKNESS_KEY;

    /** The Constant ARROW_PREFIX. */
    private static final String ARROW_PREFIX = MeteoMarkFactory.SHAPE_PREFIX + "arrow";

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );

    /** The default map. */
    private static Map<String, Double> defaultMap = new HashMap<String, Double>();

    /**
     * Gets the arrow prefix.
     *
     * @return the arrowPrefix
     */
    public static String getArrowPrefix() {
        return ARROW_PREFIX;
    }

    /**
     * Encode a GeoServer vendor option arrow string.
     *
     * @param hrExpression the hr expression
     * @param tValueExpression the t value expression
     * @param abExpression the ab expression
     * @return the string
     */
    public static String encode(Expression hrExpression,
            Expression tValueExpression,
            Expression abExpression)
    {
        String string = String.format("%s?%s=%s&%s=%s&%s=%s",
                ARROW_PREFIX,
                HEIGHT_OVER_WIDTH_ATTRIBUTE,
                getExpression(hrExpression, HEIGHT_OVER_WIDTH_ATTRIBUTE),
                ARROW_THICKNESS_ATTRIBUTE,
                getExpression(tValueExpression, ARROW_THICKNESS_ATTRIBUTE),
                HEAD_BASE_RATIO_ATTRIBUTE,
                getExpression(abExpression, HEAD_BASE_RATIO_ATTRIBUTE));

        return string;
    }

    /**
     * Gets the expression as a string, if a null expression is supplied return the default value.
     *
     * @param expression the expression
     * @param attribute the attribute
     * @return the string
     */
    private static String getExpression(Expression expression, String attribute) {
        String string;
        if(expression != null)
        {
            string = expression.toString();
        }
        else
        {
            initialise();
            string = String.valueOf(defaultMap.get(attribute));
        }
        return string;
    }

    /**
     * Gets the attribute.
     *
     * @param requiredAttribute the required attribute
     * @param string the string
     * @return the attribute
     */
    private static Expression getAttribute(String requiredAttribute, String string) {
        Expression expression = null;

        if(string != null)
        {
            String[] components = string.split("\\?");

            if(components.length == 2)
            {
                String[] attributes = components[1].split("\\&");

                for(String attribute : attributes)
                {
                    String[] value = attribute.split("\\=");
                    if(value.length == 2)
                    {
                        if(value[0].compareToIgnoreCase(requiredAttribute) == 0)
                        {
                            expression = ff.literal(value[1]);
                        }
                    }
                }
            }
        }

        if(expression == null)
        {
            initialise();
            expression = ff.literal(defaultMap.get(requiredAttribute));
        }
        return expression;
    }

    /**
     * Initialise.
     */
    private static void initialise()
    {
        if(defaultMap.isEmpty())
        {
            defaultMap.put(ARROW_THICKNESS_ATTRIBUTE, 0.2);
            defaultMap.put(HEIGHT_OVER_WIDTH_ATTRIBUTE, 2.0);
            defaultMap.put(HEAD_BASE_RATIO_ATTRIBUTE, 0.5);
        }
    }

    /**
     * Decode arrow thickness.
     *
     * @param string the string
     * @return the expression
     */
    public static Expression decodeArrowThickness(String string)
    {
        Expression expression = getAttribute(ARROW_THICKNESS_ATTRIBUTE, string);

        return expression;
    }

    /**
     * Decode height over width.
     *
     * @param string the string
     * @return the expression
     */
    public static Expression decodeHeightOverWidth(String string) {
        Expression expression = getAttribute(HEIGHT_OVER_WIDTH_ATTRIBUTE, string);

        return expression;
    }

    /**
     * Decode head base ratio.
     *
     * @param string the string
     * @return the expression
     */
    public static Expression decodeHeadBaseRatio(String string) {
        Expression expression = getAttribute(HEAD_BASE_RATIO_ATTRIBUTE, string);

        return expression;
    }
}
