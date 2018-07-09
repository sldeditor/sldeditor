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

package com.sldeditor.common.data;

import java.io.Serializable;

/**
 * Class that encapsulates data describing a GeoServer layer.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerLayer implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2342181945338903327L;

    /** The Constant SEPARATOR. */
    private static final String SEPARATOR = ":";

    /** The default workspace name. */
    private static String defaultWorkspaceName = null;

    /** The layer workspace. */
    private String layerWorkspace = null;

    /** The layer name. */
    private String layerName = null;

    /** The style wrapper. */
    private StyleWrapper styleWrapper = null;

    /** The style string. */
    private String styleString = null;

    /** The connection. */
    private GeoServerConnection connection = null;

    /** Default constructor. */
    public GeoServerLayer() {}

    /** Constructor. */
    public GeoServerLayer(String layerWorkspace, String layerName) {
        this.layerWorkspace = layerWorkspace;
        this.layerName = layerName;
    }

    /**
     * Gets the layer workspace.
     *
     * @return the layer workspace
     */
    public String getLayerWorkspace() {
        return layerWorkspace;
    }

    /**
     * Gets the layer name.
     *
     * @return the layer name
     */
    public String getLayerName() {
        return layerName;
    }

    /**
     * Sets the layer workspace.
     *
     * @param layerWorkspace the new layer workspace
     */
    public void setLayerWorkspace(String layerWorkspace) {
        this.layerWorkspace = layerWorkspace;
    }

    /**
     * Sets the layer name.
     *
     * @param layerName the new layer name
     */
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    /** Encode style. */
    private void encodeStyle() {
        if (styleWrapper.getWorkspace() == null
                || (defaultWorkspaceName == null)
                || (styleWrapper.getWorkspace().compareTo(defaultWorkspaceName) == 0)) {
            styleString = styleWrapper.getStyle();
        } else {
            styleString =
                    String.format(
                            "%s%s%s",
                            styleWrapper.getWorkspace(), SEPARATOR, styleWrapper.getStyle());
        }
    }

    /**
     * Sets the default workspace name.
     *
     * @param workspaceName the new default workspace name
     */
    public static void setDefaultWorkspaceName(String workspaceName) {
        defaultWorkspaceName = workspaceName;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public StyleWrapper getStyle() {
        return styleWrapper;
    }

    /**
     * Sets the style.
     *
     * @param styleWrapper the new style
     */
    public void setStyle(StyleWrapper styleWrapper) {
        this.styleWrapper = styleWrapper;
        encodeStyle();
    }

    /**
     * Gets the style string.
     *
     * @return the style string
     */
    public String getStyleString() {
        return styleString;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    public GeoServerConnection getConnection() {
        return connection;
    }

    /**
     * Sets the connection.
     *
     * @param connection the new connection
     */
    public void setConnection(GeoServerConnection connection) {
        this.connection = connection;
    }
}
