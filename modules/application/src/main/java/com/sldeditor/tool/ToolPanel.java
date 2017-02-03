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

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.localisation.Localisation;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Panel that contains all application tools.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ToolPanel extends JPanel {
    private static final int EMPTY_TOOL_PANEL_HEIGHT = 58;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The tool map. */
    private Map<Class<?>, List<ToolInterface>> toolMap = new HashMap<Class<?>, List<ToolInterface>>();

    /** The displayed panels. */
    private List<JPanel> displayedPanels = new ArrayList<JPanel>();

    /** The logger. */
    private static Logger logger = Logger.getLogger(ToolPanel.class);

    /** The tool panel. */
    private JPanel toolPanel;

    /** The tool selection. */
    private ToolSelectionInterface toolSelection = null;
    
    /**
     * Instantiates a new tool panel.
     *
     * @param toolMap the tool map
     */
    public ToolPanel(ToolSelectionInterface parentObj, Map<Class<?>, List<ToolInterface>> toolMap) {
        this.toolSelection = parentObj;

        toolPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) toolPanel.getLayout();
        flowLayout.setAlignOnBaseline(true);
        flowLayout.setVgap(1);
        flowLayout.setHgap(1);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(toolPanel);

        this.toolMap = toolMap;

        JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        add(optionsPanel);

        JCheckBox chckbxRecursive = new JCheckBox(Localisation.getString(ToolPanel.class, "ToolPanel.recursive"));
        chckbxRecursive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toolSelection.setRecursiveFlag(chckbxRecursive.isSelected());
            }
        });
        optionsPanel.add(chckbxRecursive);
    }

    /**
     * Sets the selected items.
     *
     * @param uniqueNodeTypeList the unique node type list
     * @param nodeTypeList the node type list
     * @param sldDataList the new selected items
     */
    public synchronized void setSelectedItems(List<Class<?>> uniqueNodeTypeList,
            List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        List<ToolInterface> consolidatedToolList = new ArrayList<ToolInterface>();

        if (nodeTypeList != null) {
            for (Class<?> nodeType : uniqueNodeTypeList) {
                List<ToolInterface> toolList = toolMap.get(nodeType);
                if (toolList != null) {
                    for (ToolInterface tool : toolList) {
                        if (!consolidatedToolList.contains(tool)) {
                            if (tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList)) {
                                consolidatedToolList.add(tool);
                            }
                        }
                    }
                }
            }
        }

        // Remove existing panels
        for (JPanel panel : displayedPanels) {
            toolPanel.remove(panel);
        }

        displayedPanels.clear();

        // Add new panels
        for (ToolInterface tool : consolidatedToolList) {
            JPanel panel = tool.getPanel();
            toolPanel.add(panel);
            displayedPanels.add(panel);
            tool.setSelectedItems(nodeTypeList, sldDataList);
            logger.debug("Displaying tool : " + tool.getToolName());
        }

        if (consolidatedToolList.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(50, EMPTY_TOOL_PANEL_HEIGHT));
            add(panel);
            displayedPanels.add(panel);
        }
        revalidate();
        repaint();
    }
}
