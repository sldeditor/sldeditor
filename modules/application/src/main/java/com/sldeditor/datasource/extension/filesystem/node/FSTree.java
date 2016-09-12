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
package com.sldeditor.datasource.extension.filesystem.node;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.sldeditor.extension.filesystem.FileSelectionInterface;

/**
 * The file system tree class.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FSTree extends JTree
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5333097014279697712L;

    /** The transfer handler. */
    private TreeTransferHandler transferHandler;

    /** The ignore selection flag. */
    private boolean ignoreSelection = false;

    /**
     * Default constructor
     */
    public FSTree()
    {
        transferHandler = new TreeTransferHandler();
        setTransferHandler(transferHandler);
        setAutoscrolls(true);
    }

    /**
     * Checks if a dragging operation is currently happening
     *
     * @return true, if is dragging
     */
    public boolean isDragging()
    {
        return transferHandler.isDragging();
    }

    /**
     * Sets the ignore selection flag state.
     */
    public void setIgnoreSelection(boolean value)
    {
        ignoreSelection = value;
    }

    /**
     * Return ignore selection flag state.
     *
     * @return true, if successful
     */
    private boolean shouldIgnoreSelection()
    {
        return ignoreSelection;
    }

    /**
     * Revert selection.
     *
     * @param previousSelectedPath the previous selected path
     */
    public void revertSelection(TreePath previousSelectedPath) {
        setIgnoreSelection(true);
        setSelectionPath(previousSelectedPath);
        setIgnoreSelection(false);
    }

    /**
     * Sets the tree selection callback.
     *
     * @param fileSystemExtension the new tree selection
     */
    public void setTreeSelection(FileSelectionInterface fileSelection) {

        addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                if(!shouldIgnoreSelection())
                {
                    if(fileSelection != null)
                    {
                        fileSelection.treeSelection(e);
                    }
                }
//                else
//                {
//                    setIgnoreSelection(false);
//                }
            }
        });
    }
}
