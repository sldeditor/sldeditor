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

package com.sldeditor.ui.legend;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.ui.legend.filechooser.ImageFilter;

/**
 * Class to draw the legend image into.
 * 
 * <p>Display legend image, allows image to be copied to the clipboard
 * 
 * @author Robert Ward (SCISYS)
 */
public class LegendPanelImage extends JLabel {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The legend. */
    private LegendManager legend = LegendManager.getInstance();

    /** The attribute data. */
    @SuppressWarnings("unused")
    private DataSourceAttributeListInterface attributeData = new DataSourceAttributeList();

    /** The image icon. */
    private ImageIcon imageIcon = null;

    /** The show filename. */
    private boolean showFilename = false;

    /** The show stylename. */
    private boolean showStyleName = false;

    /**
     * Instantiates a new legend panel.
     */
    public LegendPanelImage() {

        setLayout(new BorderLayout(0, 0));

        JMenuItem copyToClipboardMenuItem = new JMenuItem(
                Localisation.getString(LegendPanelImage.class, "LegendPanelImage.copyToClipboard"));
        copyToClipboardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (imageIcon != null) {
                    try {
                        Image image = imageIcon.getImage();
                        BufferedImage buffered = (BufferedImage) image;

                        ImageSelection trans = new ImageSelection(buffered);
                        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                        c.setContents(trans, null);
                    } catch (Exception e) {
                        ConsoleManager.getInstance().exception(LegendPanelImage.class, e);
                    }
                }
            }
        });

        JMenuItem saveAsMenuItem = new JMenuItem(
                Localisation.getString(LegendPanelImage.class, "common.saveas"));
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (imageIcon != null) {
                    JFileChooser fc = new JFileChooser();
                    try {
                        Image image = imageIcon.getImage();

                        List<FileFilter> fileterList = ImageFilter.getFilters();

                        for (FileFilter filter : fileterList) {
                            fc.addChoosableFileFilter(filter);
                        }

                        int returnVal = fc.showSaveDialog(LegendPanelImage.this);

                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            String extension = ImageFilter.defaultExtension();
                            FileFilter selectedFileFilter = fc.getFileFilter();

                            if (selectedFileFilter != null) {
                                if (selectedFileFilter instanceof ImageFilter) {
                                    extension = ((ImageFilter) fc.getFileFilter())
                                            .getFileExtension();
                                }
                            }

                            String fullExtension = "." + extension;

                            String fullPath = fc.getSelectedFile().getCanonicalPath();

                            if (!fullPath.endsWith(fullExtension)) {
                                fullPath = fullPath + fullExtension;
                            }

                            File outputFile = new File(fullPath);

                            BufferedImage buffered = (BufferedImage) image;

                            LegendManager.getInstance().saveLegendImage(buffered, extension,
                                    outputFile);
                        }
                    } catch (Exception e) {
                        ConsoleManager.getInstance().exception(this, e);
                    }
                }
            }
        });

        final JCheckBoxMenuItem showFilenameMenuItem = new JCheckBoxMenuItem(
                Localisation.getString(LegendPanelImage.class, "LegendPanelImage.showFilename"));
        showFilenameMenuItem.setSelected(showFilename);
        showFilenameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showFilename = showFilenameMenuItem.isSelected();

                renderSymbol();
            }
        });

        final JCheckBoxMenuItem showStyleNameMenuItem = new JCheckBoxMenuItem(
                Localisation.getString(LegendPanelImage.class, "LegendPanelImage.showStyleName"));
        showStyleNameMenuItem.setSelected(showStyleName);
        showStyleNameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showStyleName = showStyleNameMenuItem.isSelected();

                renderSymbol();
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.add(showFilenameMenuItem);
        popupMenu.add(showStyleNameMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(copyToClipboardMenuItem);
        popupMenu.add(saveAsMenuItem);

        this.setComponentPopupMenu(popupMenu);
    }

    /**
     * Render symbol.
     */
    public void renderSymbol() {
        if (SelectedSymbol.getInstance().isValid()) {
            StyledLayerDescriptor sld = SelectedSymbol.getInstance().getSld();

            if (sld != null) {
                // Style name
                String styleNameHeading = null;

                if (showStyleName) {
                    if (!sld.layers().isEmpty()) {
                        StyledLayer styledLayer = sld.layers().get(0);
                        if (styledLayer != null) {
                            styleNameHeading = styledLayer.getName();
                        }
                    }

                    if (styleNameHeading == null) {
                        styleNameHeading = "";
                    }
                }

                // Filename
                String filename = null;

                if (showFilename) {
                    filename = SelectedSymbol.getInstance().getFilename();
                    if (filename == null) {
                        filename = "";
                    }
                }

                BufferedImage bImage = legend.createLegend(sld, styleNameHeading, filename);

                if (bImage != null) {
                    imageIcon = new ImageIcon(bImage);
                } else {
                    imageIcon = null;
                }

                this.setIcon(imageIcon);
            } else {
                this.setIcon(null);
            }
        } else {
            this.setIcon(null);
        }
        repaint();
    }

}
