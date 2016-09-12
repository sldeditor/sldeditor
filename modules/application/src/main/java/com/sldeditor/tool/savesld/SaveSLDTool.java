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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.Controller;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDExternalImages;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
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

    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 4096;

    /** The logger. */
    private static Logger logger = Logger.getLogger(ToolPanel.class);

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

                // Save external images option
                JPanel accessory = new JPanel();
                JCheckBox isOpenBox = new JCheckBox(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.saveExternalImages"));
                accessory.setLayout(new BorderLayout());
                accessory.add(isOpenBox, BorderLayout.CENTER);
                chooser.setAccessory(accessory);

                if (chooser.showSaveDialog(saveAllSLD) == JFileChooser.APPROVE_OPTION) { 

                    saveAllSLDToFolder(chooser.getSelectedFile(), isOpenBox.isSelected());
                }
            }
        });

        groupPanel.add(saveAllSLD);
    }

    /**
     * Save all to folder.
     *
     * @param destinationFolder the destination folder
     * @param saveExternalResources the save external resources
     */
    private void saveAllSLDToFolder(File destinationFolder, boolean saveExternalResources) {
        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

        if(!destinationFolder.exists())
        {
            destinationFolder.mkdirs();
        }

        logger.info(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.saveAllSLD"));

        boolean yesToAll = false;

        for(SLDDataInterface sldData : sldDataList)
        {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            String layerName = sldData.getLayerName();

            if(sld != null)
            {
                String sldString = sldWriter.encodeSLD(sldData.getResourceLocator(), sld);

                String sldFilename = layerName + ExternalFilenames.addFileExtensionSeparator(SLDEditorFile.getSLDFileExtension());
                File fileToSave = new File(destinationFolder, sldFilename);

                ConsoleManager.getInstance().information(this, Localisation.getField(SaveSLDTool.class, "SaveSLDTool.savingSLDr") + " " + layerName);
                BufferedWriter out;
                try {
                    out = new BufferedWriter(new FileWriter(fileToSave));
                    out.write(sldString);
                    out.close();
                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }

                // Save external images if requested
                if(saveExternalResources)
                {
                    List<String> externalImageList = SLDExternalImages.getExternalImages(sldData.getResourceLocator(), sld);

                    for(String externalImage : externalImageList)
                    {
                        File output = new File(destinationFolder, externalImage);

                        File parentFolder = output.getParentFile();

                        // Check to see if the destination folder exists
                        if(!parentFolder.exists())
                        {
                            if(output.getParentFile().mkdirs())
                            {
                                ConsoleManager.getInstance().error(this,
                                        Localisation.getField(SaveSLDTool.class, "SaveSLDTool.error1") + 
                                        output.getAbsolutePath());
                            }
                        }

                        if(parentFolder.exists())
                        {
                            boolean writeOutputFile = true;

                            if(output.exists())
                            {
                                if(!yesToAll)
                                {
                                    Object[] options = {"Yes to All", "Yes", "No"};
                                    int n = JOptionPane.showOptionDialog(Controller.getInstance().getFrame(),
                                            "Overwrite destination file?\n" + output.getAbsolutePath(),
                                            "Desintation file exists",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            options,
                                            options[2]);

                                    switch(n)
                                    {
                                    case 0:
                                        yesToAll = true;
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                    default:
                                        writeOutputFile = false;
                                        break;
                                    }
                                }
                            }

                            if(writeOutputFile)
                            {
                                try {
                                    URL input = DataUtilities.extendURL(sldData.getResourceLocator(), externalImage);
                                    URLConnection connection = input.openConnection();

                                    InputStream inputStream = connection.getInputStream();
                                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                                    byte[] buffer = new byte[BUFFER_SIZE];
                                    int n = - 1;

                                    FileOutputStream outputStream = new FileOutputStream(output);
                                    while ( (n = inputStream.read(buffer)) != -1) 
                                    {
                                        outputStream.write(buffer, 0, n);
                                    }
                                    in.close();
                                    outputStream.close();
                                    ConsoleManager.getInstance().information(this,
                                            Localisation.getField(SaveSLDTool.class, "SaveSLDTool.savingExternalImage") + " " + externalImage);
                                } catch (MalformedURLException e) {
                                    ConsoleManager.getInstance().exception(this, e);
                                } catch (IOException e) {
                                    ConsoleManager.getInstance().exception(this, e);
                                }
                            }
                        }
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
