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

package com.sldeditor.tool.savesld;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;
import com.sldeditor.tool.ToolPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * Tool which given a list of SLD objects saves them to SLD files.
 *
 * @author Robert Ward (SCISYS)
 */
public class SaveSLDTool implements ToolInterface {

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 60;

    /** The save all sld. */
    protected JButton saveAllSLD;

    /** The group panel. */
    private JPanel groupPanel = null;

    /** The sld data list. */
    private List<SLDDataInterface> sldDataList;

    /** The business functionality. */
    SaveSLDInterface worker = new SaveSLD();

    /** Instantiates a new save sld tool. */
    public SaveSLDTool() {
        createUI();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return groupPanel;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#setSelectedItems(java.util.List, java.util.List)
     */
    @Override
    public void setSelectedItems(
            List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        this.sldDataList = sldDataList;

        if (saveAllSLD != null) {
            saveAllSLD.setEnabled(sldDataList.size() > 0);
        }
    }

    /** Creates the ui. */
    private void createUI() {
        groupPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) groupPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        groupPanel.setBorder(
                BorderFactory.createTitledBorder(
                        Localisation.getString(SaveSLDTool.class, "SaveSLDTool.save")));

        saveAllSLD =
                new ToolButton(
                        Localisation.getString(SaveSLDTool.class, "SaveSLDTool.sld"),
                        "tool/savesld.png");
        saveAllSLD.setEnabled(false);
        saveAllSLD.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(new java.io.File("."));
                        chooser.setDialogTitle(
                                Localisation.getString(
                                        SaveSLDTool.class, "SaveSLDTool.destinationFolder"));
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        //
                        // Disable the "All files" option.
                        //
                        chooser.setAcceptAllFileFilterUsed(false);

                        // Save external images option
                        JPanel accessory = new JPanel();
                        JCheckBox isOpenBox =
                                new JCheckBox(
                                        Localisation.getString(
                                                SaveSLDTool.class,
                                                "SaveSLDTool.saveExternalImages"));
                        accessory.setLayout(new BorderLayout());
                        accessory.add(isOpenBox, BorderLayout.CENTER);
                        chooser.setAccessory(accessory);

                        if (chooser.showSaveDialog(saveAllSLD) == JFileChooser.APPROVE_OPTION) {

                            worker.saveAllSLDToFolder(
                                    sldDataList, chooser.getSelectedFile(), isOpenBox.isSelected());
                        }
                    }
                });

        groupPanel.add(saveAllSLD);
        saveAllSLD.setPreferredSize(new Dimension(PANEL_WIDTH, ToolPanel.TOOL_PANEL_HEIGHT));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#getToolName()
     */
    @Override
    public String getToolName() {
        return getClass().getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#supports(java.util.List, java.util.List)
     */
    @Override
    public boolean supports(
            List<Class<?>> uniqueNodeTypeList,
            List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        boolean result = false;

        if (nodeTypeList != null) {
            for (NodeInterface node : nodeTypeList) {
                if (node instanceof FileTreeNode) {
                    FileTreeNode fileTreeNode = (FileTreeNode) node;

                    if (fileTreeNode.getFileCategory() == FileTreeNodeTypeEnum.SLD) {
                        result = true;
                    }
                } else if (node instanceof DatabaseFeatureClassNode) {
                    // Not supported
                } else if (node instanceof GeoServerStyleHeadingNode) {
                    result = true;
                } else if (node instanceof GeoServerStyleNode) {
                    result = true;
                } else if (node instanceof GeoServerWorkspaceNode) {
                    GeoServerWorkspaceNode workspaceNode = (GeoServerWorkspaceNode) node;

                    if (workspaceNode.isStyle()) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
}
