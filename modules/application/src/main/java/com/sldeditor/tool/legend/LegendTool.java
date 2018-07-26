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

package com.sldeditor.tool.legend;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;
import com.sldeditor.tool.ToolPanel;
import com.sldeditor.tool.html.ExportHTML;
import com.sldeditor.ui.legend.LegendManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Groups all the legend tools together.
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendTool implements ToolInterface {

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 110;

    /** The Constant INDEX_HTML. */
    private static final String INDEX_HTML = "index.html";

    /** The save all legend button. */
    protected JButton saveAllLegend;

    /** The export all html button. */
    protected JButton exportAllHTML;

    /** The legend panel. */
    private JPanel legendPanel = null;

    /** The logger. */
    private static Logger logger = Logger.getLogger(LegendTool.class);

    /** The sld data list. */
    private List<SLDDataInterface> sldDataList = null;

    /** The attribute data. */
    @SuppressWarnings("unused")
    private DataSourceAttributeListInterface attributeData = new DataSourceAttributeList();

    /** Instantiates a new legend tool. */
    public LegendTool() {
        super();

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        legendPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) legendPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        legendPanel.setBorder(
                BorderFactory.createTitledBorder(
                        Localisation.getString(LegendTool.class, "LegendTool.legend")));

        saveAllLegend =
                new ToolButton(
                        Localisation.getString(LegendTool.class, "LegendTool.legend"),
                        "tool/savealllegend.png");
        legendPanel.add(saveAllLegend);
        saveAllLegend.setEnabled(false);
        saveAllLegend.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        File currentDir = new File(".");
                        chooser.setCurrentDirectory(currentDir);
                        chooser.setDialogTitle(
                                Localisation.getString(
                                        LegendTool.class, "LegendTool.destinationFolder"));
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        // Disable the "All files" option.
                        chooser.setAcceptAllFileFilterUsed(false);

                        if (chooser.showSaveDialog(saveAllLegend) == JFileChooser.APPROVE_OPTION) {
                            saveAllLegendToFolder(chooser.getSelectedFile());
                        }
                    }
                });

        exportAllHTML =
                new ToolButton(
                        Localisation.getString(LegendTool.class, "LegendTool.html"),
                        "tool/legendhtml.png");
        legendPanel.add(exportAllHTML);
        exportAllHTML.setEnabled(false);
        exportAllHTML.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(new java.io.File("."));
                        chooser.setDialogTitle(
                                Localisation.getString(
                                        LegendTool.class, "LegendTool.htmlDestinationFolder"));
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        //
                        // Disable the "All files" option.
                        //
                        chooser.setAcceptAllFileFilterUsed(false);

                        if (chooser.showSaveDialog(exportAllHTML) == JFileChooser.APPROVE_OPTION) {

                            saveAllHTMLToFolder(chooser.getSelectedFile());
                        }
                    }
                });
        legendPanel.setPreferredSize(new Dimension(PANEL_WIDTH, ToolPanel.TOOL_PANEL_HEIGHT));
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return legendPanel;
    }

    /**
     * Save all html to folder.
     *
     * @param destinationFolder the destination folder
     */
    protected void saveAllHTMLToFolder(File destinationFolder) {
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        String filename = INDEX_HTML;

        Color backgroundColour = PrefManager.getInstance().getPrefData().getBackgroundColour();

        ExportHTML.save(destinationFolder, filename, sldDataList, backgroundColour);
    }

    /**
     * Save all legend to folder.
     *
     * @param destinationFolder the destination folder
     */
    protected void saveAllLegendToFolder(File destinationFolder) {
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        logger.info(Localisation.getString(LegendTool.class, "LegendTool.saveAllLayerLegends"));

        for (SLDDataInterface sldData : sldDataList) {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            if (sld != null) {
                String heading = null;
                String filename = null;

                String layerName = sldData.getLayerNameWithOutSuffix();

                List<String> filenameList = new ArrayList<String>();

                LegendManager.getInstance()
                        .saveLegendImage(
                                sld, destinationFolder, layerName, heading, filename, filenameList);
            }
        }
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

        if (saveAllLegend != null) {
            saveAllLegend.setEnabled(sldDataList.size() > 0);
        }

        if (exportAllHTML != null) {
            exportAllHTML.setEnabled(sldDataList.size() > 0);
        }
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
        boolean supported = false;

        if (nodeTypeList != null) {
            for (NodeInterface node : nodeTypeList) {
                if (node instanceof FileTreeNode) {
                    FileTreeNode fileTreeNode = (FileTreeNode) node;

                    if (fileTreeNode.getFileCategory() == FileTreeNodeTypeEnum.SLD) {
                        supported = true;
                    }
                }
            }
        }
        return supported;
    }
}
