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
import com.sldeditor.common.RecursiveUpdateInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.ToolSelectionInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 * A manager for handling tools.
 *
 * @author Robert Ward (SCISYS)
 */
public class ToolManager implements ToolSelectionInterface {
    /** The instance. */
    private static ToolManager instance = null;

    /** The tool map. */
    private Map<Class<?>, List<ToolInterface>> toolMap =
            new HashMap<Class<?>, List<ToolInterface>>();

    /** The unique tool map. */
    private Map<Class<?>, ToolInterface> uniqueToolMap = new HashMap<Class<?>, ToolInterface>();

    /** The logger. */
    private static Logger logger = Logger.getLogger(ToolManager.class);

    /** The tool panel. */
    private ToolPanel toolPanel = null;

    /** The last node type list. */
    private List<NodeInterface> lastNodeTypeList = null;

    /** The last sld data list. */
    private List<SLDDataInterface> lastSldDataList = null;

    /** The application. */
    private SLDEditorInterface application = null;

    /** The recursive flag. */
    private boolean recursiveFlag = false;

    /** The recursive update listener. */
    private List<RecursiveUpdateInterface> recursiveUpdateListenerList =
            new ArrayList<RecursiveUpdateInterface>();

    /** Default constructor. */
    private ToolManager() {
        // Default constructor
    }

    /**
     * Register tool.
     *
     * @param nodeType the node type
     * @param toolToRegister the tool to register
     */
    @Override
    public void registerTool(Class<?> nodeType, ToolInterface toolToRegister) {
        ToolInterface uniqueTool = uniqueToolMap.get(toolToRegister.getClass());

        if (uniqueTool == null) {
            uniqueToolMap.put(toolToRegister.getClass(), toolToRegister);
            uniqueTool = toolToRegister;
        }

        List<ToolInterface> toolList = toolMap.get(nodeType);

        if (toolList == null) {
            toolList = new ArrayList<ToolInterface>();
            toolMap.put(nodeType, toolList);
        }
        toolList.add(uniqueTool);
        logger.debug(
                "Registered tool : " + uniqueTool.getToolName() + " for " + nodeType.getName());
    }

    /**
     * Gets the single instance of ToolManager.
     *
     * @return single instance of ToolManager
     */
    public static ToolManager getInstance() {
        if (instance == null) {
            instance = new ToolManager();
        }

        return instance;
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolSelectionInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        if (toolPanel == null) {
            toolPanel = new ToolPanel(this, toolMap);
        }

        return toolPanel;
    }

    /**
     * Sets the selected items.
     *
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolSelectionInterface#setSelectedItems(java.util.List, java.util.List)
     */
    @Override
    public void setSelectedItems(
            List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        lastNodeTypeList = nodeTypeList;
        lastSldDataList = sldDataList;

        forwardSelectedItems(nodeTypeList, sldDataList);
    }

    /**
     * Forward selected items.
     *
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     */
    private void forwardSelectedItems(
            List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        if (toolPanel != null) {
            List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
            if (nodeTypeList != null) {
                for (NodeInterface o : nodeTypeList) {
                    if (!uniqueNodeTypeList.contains(o.getClass())) {
                        uniqueNodeTypeList.add(o.getClass());
                    }
                }
            }
            toolPanel.setSelectedItems(uniqueNodeTypeList, nodeTypeList, sldDataList);
        }
    }

    /** Refresh selection. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolSelectionInterface#refreshSelection()
     */
    @Override
    public void refreshSelection() {
        forwardSelectedItems(lastNodeTypeList, lastSldDataList);
    }

    /**
     * Sets the application.
     *
     * @param application the new application
     */
    public void setApplication(SLDEditorInterface application) {
        this.application = application;
    }

    /**
     * Gets the application.
     *
     * @return the application
     */
    @Override
    public SLDEditorInterface getApplication() {
        return application;
    }

    /**
     * Checks if is recursive flag.
     *
     * @return the recursiveFlag
     */
    @Override
    public boolean isRecursiveFlag() {
        return recursiveFlag;
    }

    /**
     * Sets the recursive flag.
     *
     * @param recursiveFlag the recursiveFlag to set
     */
    @Override
    public void setRecursiveFlag(boolean recursiveFlag) {
        this.recursiveFlag = recursiveFlag;
        for (RecursiveUpdateInterface listener : recursiveUpdateListenerList) {
            listener.recursiveValuesUpdated(this.recursiveFlag);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.ToolSelectionInterface#addRecursiveListener(com.sldeditor.common.RecursiveUpdateInterface)
     */
    @Override
    public void addRecursiveListener(RecursiveUpdateInterface recursiveUpdate) {
        if (!recursiveUpdateListenerList.contains(recursiveUpdate)) {
            recursiveUpdateListenerList.add(recursiveUpdate);
        }
    }
}
