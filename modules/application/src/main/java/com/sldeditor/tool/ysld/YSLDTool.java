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
package com.sldeditor.tool.ysld;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;

/**
 * Tool which given a list of SLD objects saves them to SLD files.
 * 
 * @author Robert Ward (SCISYS)
 */
public class YSLDTool implements ToolInterface {

    /** The Constant YSLD_FILE_EXTENSION. */
    private static final String YSLD_FILE_EXTENSION = "ysld";

    /** The export to ysld button. */
    private JButton exportToYSLDButton;

    /** The export to sld button. */
    private JButton exportToSLDButton;

    /** The group panel. */
    private JPanel groupPanel = null;

    /** The sld data list. */
    private List<SLDDataInterface> sldDataList;

    /**
     * Instantiates a new ysld tool.
     */
    public YSLDTool()
    {
        createUI();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return groupPanel;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#setSelectedItems(java.util.List, java.util.List)
     */
    @Override
    public void setSelectedItems(List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        this.sldDataList = sldDataList;

        if(exportToYSLDButton != null)
        {
            int sldFilesCount = 0;
            int ysldFilesCount = 0;

            if(sldDataList != null)
            {
                for(SLDDataInterface sldData : sldDataList)
                {
                    String fileExtension = ExternalFilenames.getFileExtension(sldData.getSLDFile().getAbsolutePath());

                    if(fileExtension.compareTo(SLDEditorFile.getSLDFileExtension()) == 0)
                    {
                        sldFilesCount ++;
                    }
                    else if (fileExtension.compareTo(YSLDTool.YSLD_FILE_EXTENSION) == 0)
                    {
                        ysldFilesCount ++;
                    }
                }
            }
            exportToYSLDButton.setEnabled(sldFilesCount > 0);
            exportToSLDButton.setEnabled(ysldFilesCount > 0);
        }
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        groupPanel = new JPanel();
        groupPanel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(YSLDTool.class, "YSLDTool.groupTitle")));

        // Export to YSLD
        exportToYSLDButton = new ToolButton(Localisation.getString(YSLDTool.class, "YSLDTool.exportToYSLD"),
                "tool/exporttoysld.png");
        exportToYSLDButton.setEnabled(false);
        exportToYSLDButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportToYSLD();
            }
        });

        groupPanel.add(exportToYSLDButton);

        // Export to SLD
        exportToSLDButton = new ToolButton(Localisation.getString(YSLDTool.class, "YSLDTool.exportToSLD"),
                "tool/exporttosld.png");
        exportToSLDButton.setEnabled(false);
        exportToSLDButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportToSLD();
            }
        });

        groupPanel.add(exportToSLDButton);
    }

    /**
     * Export to YSLD
     *
     */
    private void exportToYSLD() {

        SLDWriterInterface ysldWriter = SLDWriterFactory.createWriter(SLDOutputFormatEnum.YSLD);

        for(SLDDataInterface sldData : sldDataList)
        {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            String layerName = sldData.getLayerNameWithOutSuffix();

            if(sld != null)
            {
                String sldString = ysldWriter.encodeSLD(sldData.getResourceLocator(), sld);

                String ysldFilename = layerName + "." + YSLDTool.YSLD_FILE_EXTENSION;
                String destinationFolder = sldData.getSLDFile().getParent();

                File fileToSave = new File(destinationFolder, ysldFilename);

                if(fileToSave.exists())
                {
                    ConsoleManager.getInstance().error(this, Localisation.getField(YSLDTool.class, "YSLDTool.destinationAlreadyExists") + " " + ysldFilename);
                }
                else
                {
                    ConsoleManager.getInstance().information(this, Localisation.getField(YSLDTool.class, "YSLDTool.exportToYSLDMsg") + " " + ysldFilename);
                    BufferedWriter out;
                    try {
                        out = new BufferedWriter(new FileWriter(fileToSave));
                        out.write(sldString);
                        out.close();
                    } catch (IOException e) {
                        ConsoleManager.getInstance().exception(this, e);
                    }
                }
            }
        }
    }

    /**
     * Export to SLD
     */
    private void exportToSLD()
    {
        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(SLDOutputFormatEnum.SLD);

        for(SLDDataInterface sldData : sldDataList)
        {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            String layerName = sldData.getLayerNameWithOutSuffix();

            if(sld != null)
            {
                String sldString = sldWriter.encodeSLD(sldData.getResourceLocator(), sld);

                String sldFilename = layerName + ExternalFilenames.addFileExtensionSeparator(SLDEditorFile.getSLDFileExtension());
                String destinationFolder = sldData.getSLDFile().getParent();

                File fileToSave = new File(destinationFolder, sldFilename);

                if(fileToSave.exists())
                {
                    ConsoleManager.getInstance().error(this, Localisation.getField(YSLDTool.class, "YSLDTool.destinationAlreadyExists") + " " + sldFilename);
                }
                else
                {
                    ConsoleManager.getInstance().information(this, Localisation.getField(YSLDTool.class, "YSLDTool.exportToSLDMsg") + " " + sldFilename);
                    BufferedWriter out;
                    try {
                        out = new BufferedWriter(new FileWriter(fileToSave));
                        out.write(sldString);
                        out.close();
                    } catch (IOException e) {
                        ConsoleManager.getInstance().exception(this, e);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#getToolName()
     */
    @Override
    public String getToolName()
    {
        return getClass().getName();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#supports(java.util.List, java.util.List)
     */
    @Override
    public boolean supports(List<Class<?>> uniqueNodeTypeList, List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList)
    {
        if(nodeTypeList == null)
        {
            return false;
        }

        for(NodeInterface node : nodeTypeList)
        {
            if(node instanceof FileTreeNode)
            {
                FileTreeNode fileTreeNode = (FileTreeNode) node;

                if(fileTreeNode.isDir())
                {
                    // Folder
                    for(int childIndex = 0; childIndex < fileTreeNode.getChildCount(); childIndex ++)
                    {
                        FileTreeNode f = (FileTreeNode) fileTreeNode.getChildAt(childIndex);
                        if(f.getFileCategory() == FileTreeNodeTypeEnum.SLD)
                        {
                            String fileExtension = ExternalFilenames.getFileExtension(f.getFile().getAbsolutePath());
                            return (fileExtension.compareTo(YSLDTool.YSLD_FILE_EXTENSION) == 0) || (fileExtension.compareTo(SLDEditorFile.getSLDFileExtension()) == 0);
                        }
                    }
                }
                else
                {
                    // Single file
                    if(fileTreeNode.getFileCategory() == FileTreeNodeTypeEnum.SLD)
                    {
                        String fileExtension = ExternalFilenames.getFileExtension(fileTreeNode.getFile().getAbsolutePath());
                        return (fileExtension.compareTo(YSLDTool.YSLD_FILE_EXTENSION) == 0) || (fileExtension.compareTo(SLDEditorFile.getSLDFileExtension()) == 0);
                    }
                }
                return false;
            }
            else
            {
                return true;
            }
        }
        return true;
    }
}
