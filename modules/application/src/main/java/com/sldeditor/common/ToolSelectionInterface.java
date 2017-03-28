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

package com.sldeditor.common;

import java.util.List;

import javax.swing.JPanel;

import com.sldeditor.tool.ToolInterface;

/**
 * The Interface ToolSelectionInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface ToolSelectionInterface {

    /**
     * Sets the selected items.
     *
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     */
    void setSelectedItems(List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList);

    /**
     * Gets the panel containing all the tool panels.
     *
     * @return the panel
     */
    JPanel getPanel();

    /**
     * Refresh selection.
     */
    void refreshSelection();

    /**
     * Gets the application.
     *
     * @return the application
     */
    SLDEditorInterface getApplication();

    /**
     * Register tool.
     *
     * @param nodeType the node type
     * @param toolToRegister the tool to register
     */
    void registerTool(Class<?> nodeType, ToolInterface toolToRegister);

    /**
     * Checks if is recursive flag.
     *
     * @return the recursiveFlag
     */
    boolean isRecursiveFlag();

    /**
     * Sets the recursive flag.
     *
     * @param recursiveFlag the recursiveFlag to set
     */
    void setRecursiveFlag(boolean recursiveFlag);

    /**
     * Adds the recursive listener.
     *
     * @param recursiveUpdate the recursive update
     */
    void addRecursiveListener(RecursiveUpdateInterface recursiveUpdate);

}