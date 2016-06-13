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
}
