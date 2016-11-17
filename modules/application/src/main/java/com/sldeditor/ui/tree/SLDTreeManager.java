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

package com.sldeditor.ui.tree;

import java.util.ArrayList;
import java.util.List;

import com.sldeditor.datasource.RenderSymbolInterface;

/**
 * The Class SLDTreeManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeManager {

    /** The instance. */
    private static SLDTreeManager instance = null;

    /** The list of registered SLD trees. */
    private List<SLDTree> treeList = new ArrayList<SLDTree>();

    /**
     * Gets the single instance of SLDTreeManager.
     *
     * @return single instance of SLDTreeManager
     */
    public static SLDTreeManager getInstance()
    {
        if(instance == null)
        {
            instance = new SLDTreeManager();
        }

        return instance;
    }

    /**
     * Creates the SLD tree.
     *
     * @param renderList the render list
     * @param treeTools the tree tools
     * @return the SLD tree
     */
    public SLDTree createSLDTree(List<RenderSymbolInterface> renderList, SLDTreeTools treeTools)
    {
        SLDTree tree = new SLDTree(renderList, treeTools);

        treeList.add(tree);

        return tree;
    }
    
    /**
     * Rebuild tree.
     *
     * @param originatingTree the originating tree
     */
    public void rebuildTree(SLDTree originatingTree)
    {
        for(SLDTree tree : treeList)
        {
            if(tree != originatingTree)
            {
                tree.populateSLD();
            }
        }
    }
}
