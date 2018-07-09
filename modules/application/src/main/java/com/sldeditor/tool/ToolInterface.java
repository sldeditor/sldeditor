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

package com.sldeditor.tool;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import java.util.List;
import javax.swing.JPanel;

/**
 * The Interface ToolInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface ToolInterface {

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public JPanel getPanel();

    /**
     * Sets the selected items.
     *
     * @param nodeTypeList the node type list
     * @param sldDataList the new selected items
     */
    public void setSelectedItems(
            List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList);

    /**
     * Gets the tool name.
     *
     * @return the tool name
     */
    public String getToolName();

    /**
     * Supports the combinations of selected data.
     *
     * @param uniqueNodeTypeList the unique node type list
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     * @return true, if successful
     */
    public boolean supports(
            List<Class<?>> uniqueNodeTypeList,
            List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList);
}
