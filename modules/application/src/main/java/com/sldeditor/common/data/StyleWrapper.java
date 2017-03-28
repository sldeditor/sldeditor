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
 * The Class StyleWrapper identifies a SLD file on GeoServer using the workspace and style name.
 * 
 * @author Robert Ward (SCISYS)
 */
public class StyleWrapper implements Comparable<StyleWrapper>, Serializable, Cloneable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2100490761170323010L;

    /** The workspace. */
    private String workspace = null;

    /** The style. */
    private String style = null;

    /**
     * Instantiates a new style wrapper.
     *
     * @param workspace the workspace
     * @param style the style
     */
    public StyleWrapper(String workspace, String style) {
        super();
        this.workspace = workspace;
        this.style = style;
    }

    /**
     * Instantiates a new style wrapper.
     */
    public StyleWrapper() {
    }

    /**
     * Instantiates a new style wrapper.
     *
     * @param style the style
     */
    public StyleWrapper(String style) {
        super();
        this.style = style;
    }

    /**
     * Gets the workspace.
     *
     * @return the workspace
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * Sets the workspace.
     *
     * @param workspace the new workspace
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the style.
     *
     * @param style the new style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Compare to.
     *
     * @param o the o
     * @return the int
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(StyleWrapper o) {
        if (o == null) {
            return -1;
        }

        String s1 = this.workspace + this.style;
        String s2 = o.workspace + o.style;

        return s1.compareTo(s2);
    }

    /**
     * Clone.
     *
     * @return the style wrapper
     */
    @Override
    public StyleWrapper clone() {
        StyleWrapper clonedStyleWrapper = new StyleWrapper();
        clonedStyleWrapper.setWorkspace(this.workspace);
        clonedStyleWrapper.setStyle(this.style);

        return clonedStyleWrapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (workspace == null) {
            return style;
        }
        return String.format("%s/%s", workspace, style);
    }
}
