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
package com.sldeditor.tool.legendpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.geotools.styling.NamedLayer;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.tool.legendpanel.option.LegendOptionData;
import com.sldeditor.tool.legendpanel.option.LegendOptionDataUpdateInterface;
import com.sldeditor.tool.legendpanel.option.LegendOptionPanel;

/**
 * Manager object, implemented as a singleton, that controls the creation of SLD legend images.
 * 
 * @author Robert Ward (SCISYS)
 */
public class LegendManager implements LegendOptionDataUpdateInterface
{
    /** The Constant OPTIONS_DIALOG_WIDTH. */
    private static final int OPTIONS_DIALOG_WIDTH = 300;

    /** The Constant OPTIONS_DIALOG_HEIGHT. */
    private static final int OPTIONS_DIALOG_HEIGHT = 200;

    /** The singleton instance. */
    private static LegendManager instance = null;

    /** The legend builder. */
    private BufferedImageLegendGraphicBuilder legendBuilder = new BufferedImageLegendGraphicBuilder();

    /** The legend option data. */
    private LegendOptionData legendOptionData = new LegendOptionData();

    /** The refresh symbol. */
    private RenderSymbolInterface refreshSymbol = null;

    /** The Constant LEGEND_IMAGE_FORMAT. */
    private static final String LEGEND_IMAGE_FORMAT = "png";

    /** The Constant INCH_2_CM. */
    private static final double INCH_2_CM = 2.54;

    /** The option panel list. */
    private List<LegendOptionPanel> optionPanelList = new ArrayList<LegendOptionPanel>();

    /**
     * Creates the legend.
     *
     * @param backgroundColour the background colour
     * @param sld the sld
     * @param heading the heading
     * @param filename the filename
     * @return the buffered image
     */
    public BufferedImage createLegend(Color backgroundColour, StyledLayerDescriptor sld, String heading, String filename)
    {
        Map<String, BufferedImage> map = createLegend(backgroundColour, sld, heading, filename, false);

        if(!map.isEmpty())
        {
            String firstKey = map.keySet().iterator().next();

            return map.get(firstKey);
        }

        return null;
    }


    /**
     * Creates the legend.
     *
     * @param backgroundColour the background colour
     * @param sld the sld
     * @param heading the heading
     * @param filename the filename
     * @param separateSymbolizers the separate symbolizers
     * @return the map
     */
    public Map<String, BufferedImage> createLegend(Color backgroundColour,
            StyledLayerDescriptor sld, 
            String heading, 
            String filename,
            boolean separateSymbolizers)
    {
        Map<String, BufferedImage> imageMap = null;

        LegendRequest request = new LegendRequest();

        //
        // Set legend options
        //
        Map<String, String> legendOptions = new HashMap<String, String>();

        // Set background colour
        legendOptions.put("bgColor", ColourUtils.fromColour(backgroundColour));

        if(heading != null)
        {
            legendOptions.put("heading", heading);
        }

        if(filename != null)
        {
            legendOptions.put("filename", filename);
        }

        if(legendOptionData == null)
        {
            legendOptionData = new LegendOptionData();
        }

        request.setWidth(legendOptionData.getImageWidth());
        request.setHeight(legendOptionData.getImageHeight());
        legendOptions.put("dpi", String.valueOf(legendOptionData.getDpi()));
        legendOptions.put("antialias", String.valueOf(legendOptionData.isAntiAlias()));
        legendOptions.put("fontAntiAliasing", String.valueOf(legendOptionData.isAntiAlias()));
        legendOptions.put("forceLabels", String.valueOf(legendOptionData.showLabels()));
        legendOptions.put("imageSize", String.valueOf(legendOptionData.getImageSize()));

        request.setLegendOptions(legendOptions);

        if(sld != null)
        {
            List<StyledLayer> styledLayerList = sld.layers();

            for(StyledLayer styledLayer : styledLayerList)
            {
                if(styledLayer instanceof NamedLayer)
                {
                    NamedLayer namedLayer = (NamedLayer)styledLayer;

                    List<Style> styleList = namedLayer.styles();

                    for(Style style : styleList)
                    {
                        if(!style.featureTypeStyles().isEmpty())
                        {
                            if(style.featureTypeStyles().get(0) != null)
                            {
                                imageMap = legendBuilder.buildLegendGraphic(request, style, separateSymbolizers);
                            }
                        }
                    }
                }
            }
        }

        return imageMap;
    }

    /**
     * Gets the singleton instance of LegendManager.
     *
     * @return single instance of LegendManager
     */
    public static LegendManager getInstance()
    {
        if(instance  == null)
        {
            instance = new LegendManager();
        }
        return instance;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.legend.option.LegendOptionDataUpdateInterface#updateLegendOptionData(com.sldeditor.ui.legend.option.LegendOptionData)
     */
    @Override
    public void updateLegendOptionData(LegendOptionData data)
    {
        legendOptionData = data;

        if(refreshSymbol != null)
        {
            refreshSymbol.renderSymbol();
        }
    }

    /**
     * Adds the refresh.
     *
     * @param obj the obj
     */
    public void addRendererRefresh(RenderSymbolInterface obj)
    {
        refreshSymbol = obj;
    }

    /**
     * Gets the renderer update.
     *
     * @return the renderer update
     */
    public RenderSymbolInterface getRendererUpdate()
    {
        return refreshSymbol;
    }

    /**
     * Save legend image.
     *
     * @param backgroundColour the background colour
     * @param sld the sld
     * @param destinationFolder the destination folder
     * @param layerName the layer name
     * @param heading the heading
     * @param filename the filename
     * @param filenameList the filename list
     * @return true, if successful
     */
    public boolean saveLegendImage(Color backgroundColour,
            StyledLayerDescriptor sld,
            File destinationFolder,
            String layerName,
            String heading,
            String filename,
            List<String> filenameList)
    {
        boolean ok = false;

        Map<String, BufferedImage> imageMap =
                LegendManager.getInstance().createLegend(backgroundColour, sld, heading, filename, legendOptionData.splitSymbolizers());

        if(imageMap != null)
        {
            for(String name : imageMap.keySet())
            {
                BufferedImage image = imageMap.get(name);
                if(image != null)
                {
                    try
                    {
                        String legendFilename;

                        if(name == null)
                        {
                            legendFilename = layerName + "." + LegendManager.getLegendImageFormat();
                        }
                        else
                        {
                            legendFilename = String.format("%s_%s.%s", layerName, name, LegendManager.getLegendImageFormat());
                        }
                        File fileToSave = new File(destinationFolder, legendFilename);

                        filenameList.add(fileToSave.getAbsolutePath());

                        ok = saveGridImage(image, getLegendImageFormat(), fileToSave, legendOptionData.getDpi());
                    }
                    catch (IOException e)
                    {
                        ConsoleManager.getInstance().exception(this, e);
                    }
                }
            }
        }

        return ok;
    }

    /**
     * Gets the legend image format.
     *
     * @return the legend image format
     */
    public static String getLegendImageFormat()
    {
        return LEGEND_IMAGE_FORMAT;
    }

    /**
     * Save image.
     *
     * @param image the grid image
     * @param formatName the format name
     * @param destinationFile the output
     * @param dpi the dpi
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private boolean saveGridImage(BufferedImage image, String formatName, File destinationFile, int dpi) throws IOException {
        destinationFile.delete();

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }

            setDPI(metadata, dpi);

            final ImageOutputStream stream = ImageIO.createImageOutputStream(destinationFile);
            try {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            } finally {
                stream.close();
            }
            break;
        }

        return true;
    }

    /**
     * Sets the dpi.
     *
     * @param metadata the metadata
     * @param dpi the dpi
     * @throws IIOInvalidTreeException the IIO invalid tree exception
     */
    private void setDPI(IIOMetadata metadata, int dpi) throws IIOInvalidTreeException {

        // for PNG, it's dots per millimeter
        double dotsPerMilli = 1.0 * dpi / 10 / INCH_2_CM;

        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }

    /**
     * Save legend image.
     *
     * @param image the image
     * @param extension the extension
     * @param fileToSave the file to save
     */
    public void saveLegendImage(BufferedImage image, String extension, File fileToSave)
    {
        try
        {
            saveGridImage(image, extension, fileToSave, legendOptionData.getDpi());
        }
        catch (IOException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /**
     * Display options panel.
     */
    public void displayOptionsPanel()
    {
        JFrame parentFrame = Controller.getInstance().getFrame();
        JFrame frame = new JFrame(Localisation.getString(LegendManager.class, "LegendManager.optionDlgTitle"));
        frame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        int x = parentFrame.getX() + (parentFrame.getWidth() / 2) - (OPTIONS_DIALOG_WIDTH / 2);
        int y = parentFrame.getY() + (parentFrame.getHeight() / 2) - (OPTIONS_DIALOG_HEIGHT / 2);
        frame.setBounds(x, y, OPTIONS_DIALOG_WIDTH, OPTIONS_DIALOG_HEIGHT);

        LegendOptionPanel optionPanel = new LegendOptionPanel();
        optionPanel.updateData(legendOptionData);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(optionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        JButton btnOk = new JButton(Localisation.getString(LegendManager.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                updateLegendOptionData(optionPanel.getData());

                for(LegendOptionPanel panel : optionPanelList)
                {
                    panel.updateData(optionPanel.getData());
                }
            }
        });
        buttonPanel.add(btnOk);
        JButton btnCancel = new JButton(Localisation.getString(LegendManager.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.legend.option.LegendOptionDataUpdateInterface#registerPanel(com.sldeditor.ui.legend.option.LegendOptionPanel)
     */
    @Override
    public void registerPanel(LegendOptionPanel panel)
    {
        optionPanelList.add(panel);
    }
}
