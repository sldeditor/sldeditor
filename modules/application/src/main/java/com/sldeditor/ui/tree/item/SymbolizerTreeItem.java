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
package com.sldeditor.ui.tree.item;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.ui.iface.SymbolSelectedInterface;
import com.sldeditor.ui.iface.SymbolizerSelectedInterface;

/**
 * Class that display Symbolizer data within the sld tree structure.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SymbolizerTreeItem implements SLDTreeItemInterface {

    /** The Constant DEFAULT_MARKER_NAME. */
    private static final String DEFAULT_MARKER_NAME = "Marker";

    /** The Constant DEFAULT_TEXT_NAME. */
    public static final String DEFAULT_TEXT_NAME = "Text";

    /** The Constant DEFAULT_LINE_NAME. */
    public static final String DEFAULT_LINE_NAME = "Line";

    /** The Constant DEFAULT_POLYGON_NAME. */
    public static final String DEFAULT_POLYGON_NAME = "Polygon";

    /** The Constant DEFAULT_RASTER_NAME. */
    public static final String DEFAULT_RASTER_NAME = "Raster";

    /** The symbol selected listeners. */
    private List<SymbolizerSelectedInterface> symbolSelectedListeners = new ArrayList<SymbolizerSelectedInterface>();

    /** The overall selected listeners. */
    private List<SymbolSelectedInterface> overallSelectedListeners = new ArrayList<SymbolSelectedInterface>();

    /**
     * Adds the symbol selected listener.
     *
     * @param toAdd the to add
     */
    public void addSymbolSelectedListener(SymbolizerSelectedInterface toAdd) {
        symbolSelectedListeners.add(toAdd);
    }

    /**
     * Adds the overall selected listener.
     *
     * @param toAdd the to add
     */
    public void addOverallSelectedListener(SymbolSelectedInterface toAdd)
    {
        overallSelectedListeners.add(toAdd);
    }

    /**
     * Gets the tree string.
     *
     * @param nodeObject the node object
     * @return the tree string
     */
    @Override
    public String getTreeString(Object nodeObject) {
        Symbolizer symbol = (Symbolizer) nodeObject;

        String name = null;

        if(symbol != null)
        {
            name = symbol.getName();

            if((name == null) || name.isEmpty())
            {
                if(symbol instanceof PointSymbolizer)
                {
                    name = DEFAULT_MARKER_NAME;
                }
                else if(symbol instanceof TextSymbolizer)
                {
                    name = DEFAULT_TEXT_NAME;
                }
                else if(symbol instanceof LineSymbolizer)
                {
                    name = DEFAULT_LINE_NAME;
                }
                else if(symbol instanceof PolygonSymbolizer)
                {
                    name = DEFAULT_POLYGON_NAME;
                }
                else if(symbol instanceof RasterSymbolizer)
                {
                    name = DEFAULT_RASTER_NAME;
                }
            }
        }

        return name;
    }

    /**
     * Item selected.
     *
     * @param node the node
     * @param userObject the user object
     */
    @Override
    public void itemSelected(DefaultMutableTreeNode node, Object userObject) {
        SelectedSymbol selectedSymbol = SelectedSymbol.getInstance();

        // Individual symbol selected
        Symbolizer symbolizer = (Symbolizer) userObject;

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        if(parent != null)
        {
            if(parent.getUserObject() instanceof Rule)
            {
                Rule rule = (Rule) parent.getUserObject();
                selectedSymbol.setRule(rule);
            }
        }
        selectedSymbol.setSymbolizer(symbolizer);
    }
}
