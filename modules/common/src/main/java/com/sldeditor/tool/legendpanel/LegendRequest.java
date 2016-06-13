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
package com.sldeditor.tool.legendpanel;

/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.geotools.styling.Style;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Holds the parsed parameters for a GetLegendGraphic WMS request.
 * 
 * <p>
 * The GET parameters of the GetLegendGraphic operation are defined as follows (from SLD 1.0 spec,
 * ch.12):<br>
 * 
 * <pre>
 * table
 *  <b>Parameter</b><b>Required</b><b>Description</b>
 *  VERSION Required Version as required by OGC interfaces.
 *  REQUEST Required Value must be  GetLegendRequest . 
 *  LAYER Required Layer for which to produce legend graphic. A layergroup can be specified, too. In this case, STYLE and RULE parameters can have multiple values (separated by commas), one for each of the group layers.
 *  STYLE Optional Style of layer for which to produce legend graphic. If not present, the default style is selected. The style may be any valid style available for a layer, including non-SLD internally-defined styles. A list of styles separated by commas can be used to specify styles for single layers of a layergroup. To override default style only for some layers leave empty the not overridden ones in the list (ex. style1,,style3,style4 to use default style for layer 2).
 *  FEATURETYPE Optional Feature type for which to produce the legend graphic. This is not needed if the layer has only a single feature type. 
 *  RULE Optional Rule of style to produce legend graphic for, if applicable. In the case that a style has multiple rules but no specific rule is selected, then the map server is obligated to produce a graphic that is representative of all of the rules of the style. A list of rules separated by commas can be used to specify rules for single layers of a layergroup. To specify rule only for some layers leave empty the not overridden ones in the list (ex. rule1,,rule3,rule4 to not specify rule for layer 2).
 *  SCALE Optional In the case that a RULE is not specified for a style, this parameter may assist the server in selecting a more appropriate representative graphic by eliminating internal rules that are outof- scope. This value is a standardized scale denominator, defined in Section 10.2
 *  SLD Optional This parameter specifies a reference to an external SLD document. It works in the same way as the SLD= parameter of the WMS GetMap operation. 
 *  SLD_BODY Optional This parameter allows an SLD document to be included directly in an HTTP-GET request. It works in the same way as the SLD_BODY= parameter of the WMS GetMap operation.
 *  FORMAT Required This gives the MIME type of the file format in which to return the legend graphic. Allowed values are the same as for the FORMAT= parameter of the WMS GetMap request. 
 *  WIDTH Optional This gives a hint for the width of the returned graphic in pixels. Vector-graphics can use this value as a hint for the level of detail to include. 
 *  HEIGHT Optional This gives a hint for the height of the returned graphic in pixels. 
 *  LANGUAGE Optional Permits to have internationalized text in legend output. 
 *  EXCEPTIONS Optional This gives the MIME type of the format in which to return exceptions. Allowed values are the same as for the EXCEPTIONS= parameter of the WMS GetMap request.
 *  TRANSPARENT Optional <code>true</code> if the legend image background should be transparent. Defaults to <code>false</code>.
 *  table
 * </pre>
 * 
 * <p>
 * There's also a custom {@code STRICT} parameter that defaults to {@code true} and controls whether
 * the mandatory parameters are to be checked. This is useful mainly to be able of requesting a
 * legend graphic for no layer in particular, so the LAYER parameter can be omitted.
 * </p>
 * <p>
 * The GetLegendGraphic operation itself is optional for an SLD-enabled WMS. It provides a general
 * mechanism for acquiring legend symbols, beyond the LegendURL reference of WMS Capabilities.
 * Servers supporting the GetLegendGraphic call might code LegendURL references as GetLegendGraphic
 * for interface consistency. Vendor specific parameters may be added to GetLegendGraphic requests
 * and all of the usual OGC-interface options and rules apply. No XML-POST method for
 * GetLegendGraphic is presently defined.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class LegendRequest {

    /** The Constant SLD_VERSION. */
    public static final String SLD_VERSION = "1.0.0";

    /** default legend graphic width, in pixels, to apply if no WIDTH parameter was passed. */
    public static final int DEFAULT_WIDTH = 20;

    /** default legend graphic height, in pixels, to apply if no WIDTH parameter was passed. */
    public static final int DEFAULT_HEIGHT = 20;

    /**
     * The default image format in which to produce a legend graphic. Not really used when
     * performing user requests, since FORMAT is a mandatory parameter, but by now serves as a
     * default for expressing LegendURL layer attribute in GetCapabilities.
     */
    public static final String DEFAULT_FORMAT = "image/png";

    /**  The featuretype(s) of the requested LAYER(s). */
    private List<FeatureType> layers=new ArrayList<FeatureType>();
    
    /**  The featuretype name -> title association map. */
    private Map<Name,String> titles=new HashMap<Name,String>();

    /**
     * The Style object(s) for styling the legend graphic, or layer's default if not provided. This
     * style can be aquired by evaluating the STYLE parameter, which provides one of the layer's
     * named styles, the SLD parameter, which provides a URL for an external SLD document, or the
     * SLD_BODY parameter, which provides the SLD body in the request body.
     */
    private List<Style> styles=new ArrayList<Style>();

    /**
     * should hold FEATURETYPE parameter value, though not used by now, since GeoServer WMS still
     * does not supports nested layers and layers has only a single feature type. This should change
     * in the future.
     */
    private String featureType;

    /**  holds RULE parameter value(s), or <code>null</code> if not provided. */
    private List<String> rules=new ArrayList<String>();

    /**
     * holds the standarized scale denominator passed as the SCALE parameter value, or
     * <code>-1.0</code> if not provided
     */
    private double scale = -1d;

    /**
     * the mime type of the file format in which to return the legend graphic, as requested by the
     * FORMAT request parameter value.
     */
    private String format;

    /** the width in pixels of the returned graphic, or <code>DEFAULT_WIDTH</code> if not provided. */
    private int width = DEFAULT_WIDTH;

    /** the height in pixels of the returned graphic, or <code>DEFAULT_HEIGHT</code> if not provided. */
    private int height = DEFAULT_HEIGHT;

    /**
     * holds the geoserver-specific getLegendGraphic options for controlling things like the label
     * font, label font style, label font antialiasing, etc.
     */
    @SuppressWarnings("rawtypes")
    private Map legendOptions;

    /**
     * Whether the legend graphic background shall be transparent or not.
     */
    private boolean transparent;

    /** The strict. */
    private boolean strict = true;
    
    /**
     * Optional locale to be used for text in output.
     * 
     */
    private Locale locale;

    /**
     * Creates a new LegendRequest object.
     */
    public LegendRequest() {
    }

    /**
     * Gets the feature type.
     *
     * @return the feature type
     */
    public String getFeatureType() {
        return featureType;
    }

    /**
     * Sets the feature type.
     *
     * @param featureType the new feature type
     */
    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    /**
     * Gets the format.
     *
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format.
     *
     * @param format the new format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height.
     *
     * @param height the new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the layers.
     *
     * @return the layers
     */
    public List<FeatureType> getLayers() {
        return layers;
    }

    /**
     * Sets the layers.
     *
     * @param layers the new layers
     */
    public void setLayers(List<FeatureType> layers) {
        this.layers = layers;
    }
    
    /**
     * Sets the title.
     *
     * @param featureTypeName the feature type name
     * @param title the title
     */
    public void setTitle(Name featureTypeName,String title) {
        titles.put(featureTypeName, title);
    }
    
    /**
     * Gets the title.
     *
     * @param featureTypeName the feature type name
     * @return the title
     */
    public String getTitle(Name featureTypeName) {
        return titles.get(featureTypeName);
    }
    
    /**
     * Sets the layer.
     *
     * @param layer the new layer
     */
    public void setLayer(FeatureType layer) {
        this.layers.clear();
        this.layers.add(layer);
    }

    /**
     * Gets the rules.
     *
     * @return the rules
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * Sets the rules.
     *
     * @param rules the new rules
     */
    public void setRules(List<String> rules) {
        this.rules = rules;
    }
    
    /**
     * Sets the rule.
     *
     * @param rule the new rule
     */
    public void setRule(String rule) {
        this.rules.clear();
        this.rules.add(rule);
    }

    /**
     * Gets the scale.
     *
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets the scale.
     *
     * @param scale the new scale
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Gets the styles.
     *
     * @return the styles
     */
    public List<Style> getStyles() {
        return styles;
    }

    /**
     * Sets the styles.
     *
     * @param styles the new styles
     */
    public void setStyles(List<Style> styles) {
        this.styles = styles;
    }
    
    /**
     * Sets the style.
     *
     * @param style the new style
     */
    public void setStyle(Style style) {
        this.styles.clear();
        this.styles.add(style);
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width.
     *
     * @param width the new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the possibly empty set of key/value pair parameters to control some aspects of legend
     * generation.
     * <p>
     * These parameters are meant to be passed as the request parameter
     * <code>"LEGEND_OPTIONS"</code> with the format
     * <code>LEGEND_OPTIONS=multiKey:val1,val2,val3;singleKey:val</code>.
     * </p>
     * <p>
     * The known options, all optional, are:
     * <ul>
     * <li><code>fontName</code>: name of the system font used for legend rule names. Defaults to
     * "Sans-Serif"
     * <li><code>fontStyle</code>: one of "plain", "italic" or "bold"
     * <li><code>fontSize</code>: integer for the font size in pixels
     * <li><code>fontColor</code>: a <code>String</code> that represents an opaque color as a 24-bit
     * integer
     * <li><code>bgColor</code>: allows to override the legend background color
     * <li><code>fontAntiAliasing</code>: a boolean indicating whether to use anti aliasing in font
     * rendering. Anything of the following works: "yes", "true", "1". Anything else means false.
     * <li><code>forceLabels</code>: "on" means labels will always be drawn, even if only one rule
     * is available. "off" means labels will never be drawn, even if multiple rules are available.
     * <li><code>forceTitles</code>: "off" means titles will never be drawn, even if multiple layers
     * are available.
     * <li><code>minSymbolSize</code>: a number defining the minimum size to be rendered for a 
     * symbol (defaults to 3).
     * 
     * </ul>
     * 
     *
     * @return the legend options
     */
    @SuppressWarnings("rawtypes")
    public Map getLegendOptions() {
        return legendOptions == null ? Collections.EMPTY_MAP : legendOptions;
    }

    /**
     * Sets the legend options parameters.
     * 
     * @param legendOptions
     *            the key/value pair of legend options strings
     * @see #getLegendOptions()
     */
    @SuppressWarnings("rawtypes")
    public void setLegendOptions(Map legendOptions) {
        this.legendOptions = legendOptions;
    }

    /**
     * Sets the value of the background transparency flag depending on the value of the
     * <code>TRANSPARENT</code> request parameter.
     * 
     * @param transparentBackground
     *            whether the legend graphic background shall be transparent or not
     */
    public void setTransparent(boolean transparentBackground) {
        this.transparent = transparentBackground;
    }

    /**
     * Returns the value of the optional request parameter <code>TRANSPARENT</code>, which might be
     * either the literal <code>true</code> or <code>false</code> and specifies if the background of
     * the legend graphic to return shall be transparent or not.
     * <p>
     * If the <code>TRANSPARENT</code> parameter is not specified, this property defaults to
     * <code>false</code>.
     * </p>
     * 
     * @return whether the legend graphic background shall be transparent or not
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Returns the value for the legacy {@code STRICT} parameter that controls whether LAYER is
     * actually required (if not, STYLE shall be provided).
     *
     * @return {@code true} by default, the value set thru {link #setStrict(boolean)} otherwise
     */
    public boolean isStrict() {
        return strict;
    }

    /**
     * Sets the strict.
     *
     * @param strict the new strict
     */
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    /**  SLD replacement. */
    @SuppressWarnings("rawtypes")
    private Map env = new HashMap();

    

    /**
     * Map of strings that make up the SLD environment for variable substitution.
     *
     * @return the environment
     */
    @SuppressWarnings("rawtypes")
    public Map getEnv() {
        return env;
    }

    /**
     * Sets the SLD environment substitution.
     *
     * @param enviroment the new environment
     */
    @SuppressWarnings("rawtypes")
    public void setEnv(Map enviroment) {
        this.env = enviroment;
    }

    /**
     * Sets the optional Locale to be used for text in legend output.
     *
     * @param locale the new locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    /**
     * Gets the locale to be used for text in output
     * (null to use default locale).
     *
     * @return the locale
     */
    public Locale getLocale() {
        return this.locale;
    }
}
