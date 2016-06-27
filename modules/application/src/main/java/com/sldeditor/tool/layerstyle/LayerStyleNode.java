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
package com.sldeditor.tool.layerstyle;

import javax.swing.tree.DefaultMutableTreeNode;

import com.sldeditor.common.data.StyleWrapper;

/**
 * The Class LayerStyleNode.
 *
 * @author Robert Ward (SCISYS)
 */
public class LayerStyleNode extends DefaultMutableTreeNode {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The style wrapper. */
    private StyleWrapper styleWrapper = null;
    
    /**
     * Instantiates a new layer style node.
     *
     * @param styleWrapper the style wrapper
     */
    public LayerStyleNode(StyleWrapper styleWrapper)
    {
        this.styleWrapper = styleWrapper;
    }

    /**
     * Gets the style wrapper.
     *
     * @return the styleWrapper
     */
    public StyleWrapper getStyleWrapper() {
        return styleWrapper;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return styleWrapper.getStyle();
    }
}
