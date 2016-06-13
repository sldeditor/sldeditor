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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;
import com.sldeditor.tool.ToolPanel;

/**
 * Tool which given a list of SLD objects saves them to SLD files.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SaveSLDTool implements ToolInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(ToolPanel.class);

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /** The save all sld. */
    private JButton saveAllSLD;

    /** The group panel. */
    private JPanel groupPanel = null;

    /** The sld data list. */
    private List<SLDDataInterface> sldDataList;

    /**
     * Instantiates a new save sld tool.
     */
    public SaveSLDTool()
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

        if(saveAllSLD != null)
        {
            saveAllSLD.setEnabled(sldDataList.size() > 0);
        }
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        groupPanel = new JPanel();
        groupPanel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.save")));

        saveAllSLD = new ToolButton(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.sld"),
                "tool/savesld.png");
        saveAllSLD.setEnabled(false);
        saveAllSLD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(); 
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.destinationFolder"));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                //
                // Disable the "All files" option.
                //
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showSaveDialog(saveAllSLD) == JFileChooser.APPROVE_OPTION) { 

                    saveAllSLDToFolder(chooser.getSelectedFile());
                }
            }
        });

        groupPanel.add(saveAllSLD);
    }

    /**
     * Save all to folder.
     *
     * @param destinationFolder the destination folder
     */
    private void saveAllSLDToFolder(File destinationFolder) {
        if(sldWriter == null)
        {
            sldWriter = SLDWriterFactory.createSLDWriter(null);
        }

        if(!destinationFolder.exists())
        {
            destinationFolder.mkdirs();
        }

        logger.info(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.saveAllLayersFromMXD"));

        for(SLDDataInterface sldData : sldDataList)
        {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            String layerName = sldData.getLayerName();

            if(sld != null)
            {
                String sldString = sldWriter.encodeSLD(sld);

                String sldFilename = layerName + SLDEditorFile.getSLDFileExtension();
                File fileToSave = new File(destinationFolder, sldFilename);

                ConsoleManager.getInstance().information(this, Localisation.getField(SaveSLDTool.class, "SaveSLDTool.savingLayer") + " " + layerName);
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
        for(NodeInterface node : nodeTypeList)
        {
            if(node instanceof FileTreeNode)
            {
                FileTreeNode fileTreeNode = (FileTreeNode) node;

                if(fileTreeNode.getFileCategory() != FileTreeNodeTypeEnum.SLD)
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
        return true;
    }
}
