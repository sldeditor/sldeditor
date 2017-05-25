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

package com.sldeditor.tool.scale;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;
import com.sldeditor.tool.ToolPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Tool to contain scale related tools.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ScaleTool implements ToolInterface {

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 60;

    /** The Scale button. */
    private JButton scaleButton;

    /** The scale panel. */
    private JPanel scaleGroupPanel = null;

    /** The sld data list. */
    private List<SLDDataInterface> sldDataList = null;

    /** The attribute data. */
    @SuppressWarnings("unused")
    private DataSourceAttributeListInterface attributeData = new DataSourceAttributeList();

    /** The application. */
    private SLDEditorInterface application = null;

    /**
     * Constructor.
     *
     * @param application the application
     */
    public ScaleTool(SLDEditorInterface application) {
        super();
        this.application = application;

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        scaleGroupPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) scaleGroupPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        scaleGroupPanel.setBorder(BorderFactory
                .createTitledBorder(Localisation.getString(ScaleTool.class, "ScaleTool.scale")));

        scaleButton = new ToolButton(Localisation.getString(ScaleTool.class, "ScaleTool.scale"),
                "tool/scaletool.png");
        scaleGroupPanel.add(scaleButton);
        scaleButton.setEnabled(false);
        scaleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ScaleToolPanel scalePanel = new ScaleToolPanel(application);

                scalePanel.populate(sldDataList);
                scalePanel.setVisible(true);
            }
        });
        scaleGroupPanel.setPreferredSize(new Dimension(PANEL_WIDTH, ToolPanel.TOOL_PANEL_HEIGHT));
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return scaleGroupPanel;
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
     * @see com.sldeditor.tool.ToolInterface#setSelectedItems(java.util.List, java.util.List)
     */
    @Override
    public void setSelectedItems(List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        this.sldDataList = sldDataList;

        if (scaleButton != null) {
            scaleButton.setEnabled(sldDataList.size() > 0);
        }
    }

    /**
     * Gets the tool name.
     *
     * @return the tool name
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.ToolInterface#getToolName()
     */
    @Override
    public String getToolName() {
        return getClass().getName();
    }

    /**
     * Supports.
     *
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.ToolInterface#supports(java.util.List, java.util.List)
     */
    @Override
    public boolean supports(List<Class<?>> uniqueNodeTypeList, List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        for (NodeInterface node : nodeTypeList) {
            if (node instanceof FileTreeNode) {
                FileTreeNode fileTreeNode = (FileTreeNode) node;

                if (fileTreeNode.getFileCategory() != FileTreeNodeTypeEnum.SLD) {
                    return false;
                }
            } else if (node instanceof GeoServerStyleNode) {
                return true;
            } else if (node instanceof GeoServerStyleHeadingNode) {
                return true;
            } else if (node instanceof GeoServerWorkspaceNode) {
                GeoServerWorkspaceNode workspaceNode = (GeoServerWorkspaceNode) node;
                return workspaceNode.isStyle();
            } else {
                return true;
            }
        }
        return true;
    }
}
