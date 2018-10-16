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
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.ui.legend.option.LegendOptionData;
import com.sldeditor.ui.legend.option.LegendOptionDataUpdateInterface;
import com.sldeditor.ui.legend.option.LegendOptionPanel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.geoserver.wms.GetLegendGraphicRequest;
import org.geoserver.wms.GetLegendGraphicRequest.LegendRequest;
import org.geoserver.wms.legendgraphic.SLDEditorBufferedImageLegendGraphicBuilder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayerImpl;

/**
 * Manager object, implemented as a singleton, that controls the creation of SLD legend images.
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendManager implements LegendOptionDataUpdateInterface {
    /** The singleton instance. */
    private static LegendManager instance = null;

    /** The legend builder. */
    private SLDEditorBufferedImageLegendGraphicBuilder legendBuilder =
            new SLDEditorBufferedImageLegendGraphicBuilder();

    /** The legend option data. */
    private LegendOptionData legendOptionData = new LegendOptionData();

    /** The refresh symbol. */
    private RenderSymbolInterface refreshSymbol = null;

    /** The Constant LEGEND_IMAGE_FORMAT. */
    private static final String LEGEND_IMAGE_FORMAT = "png";

    /** The Constant INCH_2_CM. */
    private static final double INCH_2_CM = 2.54;

    /** The legend option panel. */
    private LegendOptionPanel legendOptionPanel = null;

    /**
     * Creates the legend.
     *
     * @param sld the sld
     * @param heading the heading
     * @param filename the filename
     * @return the buffered image
     */
    public BufferedImage createLegend(StyledLayerDescriptor sld, String heading, String filename) {
        Map<String, BufferedImage> map = createLegend(sld, heading, filename, false);

        if ((map != null) && !map.isEmpty()) {
            if (map.size() == 1) {
                String firstKey = map.keySet().iterator().next();

                return map.get(firstKey);
            }
        }

        return null;
    }

    /**
     * Creates the legend.
     *
     * @param sld the sld
     * @param heading the heading
     * @param filename the filename
     * @param separateSymbolizers the separate symbolizers
     * @return the map
     */
    public Map<String, BufferedImage> createLegend(
            StyledLayerDescriptor sld,
            String heading,
            String filename,
            boolean separateSymbolizers) {
        Map<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

        //
        // Set legend options
        //
        Map<String, Object> legendOptions = new HashMap<String, Object>();

        if (heading != null) {
            legendOptions.put("heading", heading);
        }

        if (filename != null) {
            legendOptions.put("filename", filename);
        }

        if (legendOptionData == null) {
            legendOptionData = new LegendOptionData();
        }

        GetLegendGraphicRequest request = new GetLegendGraphicRequest();
        request.setWidth(legendOptionData.getImageWidth());
        request.setHeight(legendOptionData.getImageHeight());
        request.setTransparent(legendOptionData.isTransparent());
        request.setStrict(false);

        legendOptions.put(
                "bgColor", ColourUtils.fromColour(legendOptionData.getBackgroundColour()));
        legendOptions.put(
                "fontColor", ColourUtils.fromColour(legendOptionData.getLabelFontColour()));

        //
        // Label Font
        //
        Font font = legendOptionData.getLabelFont();
        legendOptions.put("fontName", font.getFontName());
        String styleValue = null;
        if ((font.getStyle() & java.awt.Font.BOLD) == java.awt.Font.BOLD) {
            styleValue = "bold";
        }
        if ((font.getStyle() & java.awt.Font.ITALIC) == java.awt.Font.ITALIC) {
            styleValue = "italic";
        }
        if (styleValue != null) {
            legendOptions.put("fontStyle", styleValue);
        }

        legendOptions.put("fontSize", String.valueOf(font.getSize()));
        legendOptions.put("dpi", Integer.valueOf(legendOptionData.getDpi()));
        legendOptions.put(
                "fontAntiAliasing", getBooleanValueOnOff(legendOptionData.isFontAntiAliasing()));
        legendOptions.put("forceLabels", getBooleanValueOnOff(legendOptionData.isShowLabels()));
        legendOptions.put("forceTitles", getBooleanValueOnOff(legendOptionData.isShowTitle()));
        legendOptions.put(
                "bandInfo", getBooleanValueTrueFalse(legendOptionData.isBandInformation()));
        legendOptions.put("border", getBooleanValueTrueFalse(legendOptionData.isBorder()));
        legendOptions.put(
                "borderColor", ColourUtils.fromColour(legendOptionData.getBorderColour()));
        legendOptions.put(
                "imageSizeFactor", String.valueOf(legendOptionData.getImageSize() / 100.0));

        request.setLegendOptions(legendOptions);

        if (sld != null) {
            Map<String, Style> styleMap = new LinkedHashMap<String, Style>();
            StyledLayer selectedStyledLayer = SelectedSymbol.getInstance().getStyledLayer();
            Style selectedStyle = SelectedSymbol.getInstance().getStyle();
            if (selectedStyle != null) {
                createSingleStyleLegend(styleMap, selectedStyledLayer, selectedStyle);
            } else {
                createMultipleStyleLegend(sld, styleMap, selectedStyledLayer);
            }

            // Merge symbolizers into 1 image
            if (!separateSymbolizers) {
                for (String key : styleMap.keySet()) {
                    Style style = styleMap.get(key);

                    if (!style.featureTypeStyles().isEmpty()) {
                        FeatureTypeStyle featureTypeStyle = style.featureTypeStyles().get(0);
                        if (featureTypeStyle != null) {
                            if (!featureTypeStyle.rules().isEmpty()) {
                                LegendRequest legendEntryRequest = request.new LegendRequest();
                                request.getLegends().add(legendEntryRequest);
                                legendEntryRequest.setTitle(key);
                                legendEntryRequest.setStyle(style);
                            }
                        }
                    }
                }

                BufferedImage legendGraphic = null;

                try {
                    legendGraphic = legendBuilder.buildLegendGraphic(request);
                } catch (Exception e) {
                    // Ignore
                }
                imageMap.put("", legendGraphic);
            } else {
                for (String key : styleMap.keySet()) {
                    request.getLegends().clear();
                    LegendRequest legendEntryRequest = request.new LegendRequest();
                    legendEntryRequest.setStyle(styleMap.get(key));
                    legendEntryRequest.setStyleName(key);
                    request.getLegends().add(legendEntryRequest);

                    BufferedImage legendGraphic = null;

                    try {
                        legendGraphic = legendBuilder.buildLegendGraphic(request);
                    } catch (Exception e) {
                        // Ignore
                    }
                    imageMap.put(key, legendGraphic);
                }
            }
        }
        return imageMap;
    }

    /**
     * Gets the string on/off for a boolean value.
     *
     * @param flag the flag
     * @return the boolean value
     */
    private static String getBooleanValueOnOff(boolean flag) {
        return flag ? "on" : "off";
    }

    /**
     * Gets the string true/false for a boolean value.
     *
     * @param flag the flag
     * @return the boolean value
     */
    private static String getBooleanValueTrueFalse(boolean flag) {
        return flag ? "true" : "false";
    }

    /**
     * Creates the legend for multiple SLD styles.
     *
     * @param sld the sld
     * @param styleMap the style map
     * @param selectedStyledLayer the selected styled layer
     */
    private void createMultipleStyleLegend(
            StyledLayerDescriptor sld,
            Map<String, Style> styleMap,
            StyledLayer selectedStyledLayer) {
        List<StyledLayer> styledLayerList = null;

        if (selectedStyledLayer == null) {
            styledLayerList = sld.layers();
        } else {
            styledLayerList = new ArrayList<StyledLayer>();
            styledLayerList.add(selectedStyledLayer);
        }

        for (StyledLayer styledLayer : styledLayerList) {
            List<Style> styleList = null;

            if (styledLayer instanceof NamedLayerImpl) {
                NamedLayerImpl namedLayer = (NamedLayerImpl) styledLayer;

                styleList = namedLayer.styles();
            } else if (styledLayer instanceof UserLayerImpl) {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;

                styleList = userLayer.userStyles();
            }

            if (styleList != null) {
                int count = 1;
                for (Style style : styleList) {
                    String styleName;
                    if (style.getName() != null) {
                        styleName = style.getName();
                    } else {
                        styleName = String.format("Style %d", count);
                    }
                    styleMap.put(styleName, style);

                    count++;
                }
            }
        }
    }

    /**
     * Creates the legend for a single SLD style.
     *
     * @param styleMap the style map
     * @param selectedStyledLayer the selected styled layer
     * @param selectedStyle the selected style
     */
    private void createSingleStyleLegend(
            Map<String, Style> styleMap, StyledLayer selectedStyledLayer, Style selectedStyle) {
        // A style has been selected
        List<Style> styleList = null;

        if (selectedStyledLayer instanceof NamedLayerImpl) {
            NamedLayerImpl namedLayer = (NamedLayerImpl) selectedStyledLayer;

            styleList = namedLayer.styles();
        } else if (selectedStyledLayer instanceof UserLayerImpl) {
            UserLayerImpl userLayer = (UserLayerImpl) selectedStyledLayer;

            styleList = userLayer.userStyles();
        }

        String styleName;
        if (selectedStyle.getName() != null) {
            styleName = selectedStyle.getName();
        } else {
            styleName =
                    String.format(
                            "Style %d", (styleList != null) ? styleList.indexOf(selectedStyle) : 0);
        }

        styleMap.put(styleName, selectedStyle);
    }

    /**
     * Gets the singleton instance of LegendManager.
     *
     * @return single instance of LegendManager
     */
    public static LegendManager getInstance() {
        if (instance == null) {
            instance = new LegendManager();
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.legend.option.LegendOptionDataUpdateInterface#updateLegendOptionData(com.
     * sldeditor.ui.legend.option.LegendOptionData)
     */
    @Override
    public void updateLegendOptionData(LegendOptionData data) {
        legendOptionData = data;

        if (refreshSymbol != null) {
            refreshSymbol.renderSymbol();
        }
    }

    /**
     * Gets the renderer update.
     *
     * @return the renderer update
     */
    public RenderSymbolInterface getRendererUpdate() {
        return refreshSymbol;
    }

    /**
     * Save legend image.
     *
     * @param sld the sld
     * @param destinationFolder the destination folder
     * @param layerName the layer name
     * @param heading the heading
     * @param filename the filename
     * @param filenameList the filename list
     * @return true, if successful
     */
    public boolean saveLegendImage(
            StyledLayerDescriptor sld,
            File destinationFolder,
            String layerName,
            String heading,
            String filename,
            List<String> filenameList) {
        boolean ok = false;

        Map<String, BufferedImage> imageMap =
                LegendManager.getInstance()
                        .createLegend(sld, heading, filename, legendOptionData.splitSymbolizers());

        if (imageMap != null) {
            for (String name : imageMap.keySet()) {
                BufferedImage image = imageMap.get(name);
                if (image != null) {
                    try {
                        String legendFilename;

                        if (name == null) {
                            legendFilename = layerName + "." + LegendManager.getLegendImageFormat();
                        } else {
                            legendFilename =
                                    String.format(
                                            "%s_%s.%s",
                                            layerName, name, LegendManager.getLegendImageFormat());
                        }
                        File fileToSave = new File(destinationFolder, legendFilename);

                        filenameList.add(fileToSave.getAbsolutePath());

                        ok =
                                saveGridImage(
                                        image,
                                        getLegendImageFormat(),
                                        fileToSave,
                                        legendOptionData.getDpi());
                    } catch (IOException e) {
                        ConsoleManager.getInstance().exception(this, e);
                    }
                }
            }
        }

        return ok;
    }

    /**
     * Save legend image.
     *
     * @param image the image
     * @param extension the extension
     * @param fileToSave the file to save
     */
    public void saveLegendImage(BufferedImage image, String extension, File fileToSave) {
        if (image != null) {
            try {
                saveGridImage(image, extension, fileToSave, legendOptionData.getDpi());
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

    /**
     * Gets the legend image format.
     *
     * @return the legend image format
     */
    public static String getLegendImageFormat() {
        return LEGEND_IMAGE_FORMAT;
    }

    /**
     * Save image.
     *
     * @param image the grid image
     * @param formatName the format name
     * @param destinationFile the output
     * @param dpi the dpi
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private boolean saveGridImage(
            BufferedImage image, String formatName, File destinationFile, int dpi)
            throws IOException {
        destinationFile.delete();

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName);
                iw.hasNext(); ) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier =
                    ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
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
     * Creates the legend options panel.
     *
     * @param legendPanel the legend panel
     * @return the legend option panel
     */
    public LegendOptionPanel createLegendOptionsPanel(LegendPanel legendPanel) {
        if (legendOptionPanel == null) {
            legendOptionPanel = new LegendOptionPanel();

            legendOptionPanel.addListener(this);
            refreshSymbol = legendPanel;
        }
        return legendOptionPanel;
    }

    /**
     * Called when SLD loaded.
     *
     * @param data the data
     */
    public void SLDLoaded(LegendOptionData data) {
        if (data != null) {
            updateLegendOptionData(data);

            if (legendOptionPanel != null) {
                legendOptionPanel.updateData(data);
            }
        }
    }
}
