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
public class StyleWrapper implements Comparable<StyleWrapper>, Serializable {
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

    /** Instantiates a new style wrapper. */
    public StyleWrapper() {}

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
     * Copy constructor.
     *
     * @return the style wrapper to copy
     */
    public StyleWrapper(StyleWrapper clone) {
        this.workspace = clone.getWorkspace();
        this.style = clone.getStyle();
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((style == null) ? 0 : style.hashCode());
        result = prime * result + ((workspace == null) ? 0 : workspace.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        StyleWrapper other = (StyleWrapper) obj;
        if (style == null) {
            if (other.style != null) return false;
        } else if (!style.equals(other.style)) return false;
        if (workspace == null) {
            if (other.workspace != null) return false;
        } else if (!workspace.equals(other.workspace)) return false;
        return true;
    }
}
