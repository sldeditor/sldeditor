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

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.legend.filechooser.ImageFilter;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private transient LegendManager legend = LegendManager.getInstance();

    /** The image icon. */
    private ImageIcon imageIcon = null;

    /** The show filename flag. */
    private boolean showFilename = false;

    /** The show stylename flag. */
    private boolean showStyleName = false;

    /** The show filename menu item. */
    private JCheckBoxMenuItem showFilenameMenuItem;

    /** The show style name menu item. */
    private JCheckBoxMenuItem showStyleNameMenuItem;

    /** Instantiates a new legend panel. */
    public LegendPanelImage() {

        setLayout(new BorderLayout(0, 0));

        JMenuItem copyToClipboardMenuItem =
                new JMenuItem(
                        Localisation.getString(
                                LegendPanelImage.class, "LegendPanelImage.copyToClipboard"));
        copyToClipboardMenuItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        copyToClipboard();
                    }
                });

        JMenuItem saveAsMenuItem =
                new JMenuItem(Localisation.getString(LegendPanelImage.class, "common.saveas"));
        saveAsMenuItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        saveAsMenuItem();
                    }
                });

        showFilenameMenuItem =
                new JCheckBoxMenuItem(
                        Localisation.getString(
                                LegendPanelImage.class, "LegendPanelImage.showFilename"));
        showFilenameMenuItem.setSelected(showFilename);
        showFilenameMenuItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        showFilenamePressed();
                    }
                });

        showStyleNameMenuItem =
                new JCheckBoxMenuItem(
                        Localisation.getString(
                                LegendPanelImage.class, "LegendPanelImage.showStyleName"));
        showStyleNameMenuItem.setSelected(showStyleName);
        showStyleNameMenuItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        showStyleNamePressed();
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

    /** Render symbol. */
    public void renderSymbol() {
        if (SelectedSymbol.getInstance().isValid()) {
            StyledLayerDescriptor sld = SelectedSymbol.getInstance().getSld();

            if (sld != null) {
                // Style name
                String styleNameHeading = null;

                if (showStyleName) {
                    styleNameHeading = showStyleName(sld);
                }

                // Filename
                String filename = null;

                if (showFilename) {
                    filename = SelectedSymbol.getInstance().getFilename();
                    if (filename == null) {
                        filename = "";
                    }
                }

                createLegendIcon(sld, styleNameHeading, filename);
            } else {
                this.setIcon(null);
            }
        } else {
            this.setIcon(null);
        }
        repaint();
    }

    /**
     * Creates the legend icon.
     *
     * @param sld the sld
     * @param styleNameHeading the style name heading
     * @param filename the filename
     */
    private void createLegendIcon(
            StyledLayerDescriptor sld, String styleNameHeading, String filename) {
        BufferedImage bImage = legend.createLegend(sld, styleNameHeading, filename);

        if (bImage != null) {
            imageIcon = new ImageIcon(bImage);
        } else {
            imageIcon = null;
        }

        this.setIcon(imageIcon);
    }

    /**
     * Show style name.
     *
     * @param sld the sld
     * @return the string
     */
    private String showStyleName(StyledLayerDescriptor sld) {
        String styleNameHeading = null;
        if (!sld.layers().isEmpty()) {
            StyledLayer styledLayer = sld.layers().get(0);
            if (styledLayer != null) {
                styleNameHeading = styledLayer.getName();
            }
        }

        if (styleNameHeading == null) {
            styleNameHeading = "";
        }
        return styleNameHeading;
    }

    /** Save as menu item. */
    private void saveAsMenuItem() {
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
                    writeLegendImage(fc.getFileFilter(), fc.getSelectedFile(), image);
                }
            } catch (Exception e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

    /**
     * Write legend image.
     *
     * @param fileFilter the file filter
     * @param selectedFile the selected file
     * @param image the image
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected File writeLegendImage(FileFilter fileFilter, File selectedFile, Image image)
            throws IOException {
        String extension = ImageFilter.defaultExtension();

        if (fileFilter != null) {
            if (fileFilter instanceof ImageFilter) {
                extension = ((ImageFilter) fileFilter).getFileExtension();
            }
        }

        String fullExtension = "." + extension;

        String fullPath = selectedFile.getCanonicalPath();

        if (!fullPath.endsWith(fullExtension)) {
            fullPath = fullPath + fullExtension;
        }

        File outputFile = new File(fullPath);

        BufferedImage buffered = (BufferedImage) image;

        LegendManager.getInstance().saveLegendImage(buffered, extension, outputFile);

        return outputFile;
    }

    /** Copy to clipboard. */
    protected void copyToClipboard() {
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

    /** Show filename pressed. */
    protected void showFilenamePressed() {
        showFilename = showFilenameMenuItem.isSelected();

        renderSymbol();
    }

    /** Show style name pressed. */
    protected void showStyleNamePressed() {
        showStyleName = showStyleNameMenuItem.isSelected();

        renderSymbol();
    }

    /**
     * Sets the style name displayed.
     *
     * @param selected the new style name displayed
     */
    protected void setStyleNameDisplayed(boolean selected) {
        showStyleNameMenuItem.setSelected(selected);
    }

    /**
     * Sets the filename displayed.
     *
     * @param selected the new filename displayed
     */
    protected void setFilenameDisplayed(boolean selected) {
        showFilenameMenuItem.setSelected(selected);
    }

    /**
     * Gets the image icon.
     *
     * @return the image icon
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
