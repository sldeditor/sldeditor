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

package com.sldeditor.ui.legend.option;

import com.sldeditor.common.utils.ColourUtils;
import java.awt.Color;
import java.awt.Font;
import org.geoserver.wms.legendgraphic.LegendUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class encapsulating legend configuration data.
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendOptionData {

    /** The Constant BACKGROUND_COLOUR. */
    private static final String BACKGROUND_COLOUR = "backgroundColour";

    /** The Constant BORDER_COLOUR. */
    private static final String BORDER_COLOUR = "borderColour";

    /** The Constant LABEL_FONT_COLOUR. */
    private static final String LABEL_FONT_COLOUR = "labelFontColour";

    /** The Constant LABEL_FONT. */
    private static final String LABEL_FONT = "labelFont";

    /** The Constant BACKGROUND_TRANSPARENT. */
    private static final String BACKGROUND_TRANSPARENT = "transparent";

    /** The Constant BAND_INFORMATION. */
    private static final String BAND_INFORMATION = "bandInformation";

    /** The Constant SHOW_BORDER. */
    private static final String SHOW_BORDER = "border";

    /** The Constant FONT_ANTI_ALIASING. */
    private static final String FONT_ANTI_ALIASING = "fontAntiAliasing";

    /** The Constant SPLIT_SYMBOLIZERS. */
    private static final String SPLIT_SYMBOLIZERS = "splitSymbolizers";

    /** The Constant SHOW_TITLE. */
    private static final String SHOW_TITLE = "showTitle";

    /** The Constant SHOW_LABELS. */
    private static final String SHOW_LABELS = "showLabels";

    /** The Constant MAINTAIN_ASPECT_RATIO. */
    private static final String MAINTAIN_ASPECT_RATIO = "maintainAspectRatio";

    /** The Constant IMAGE_SIZE. */
    private static final String IMAGE_SIZE = "imageSize";

    /** The Constant IMAGE_DPI. */
    private static final String IMAGE_DPI = "dpi";

    /** The Constant IMAGE_HEIGHT. */
    private static final String IMAGE_HEIGHT = "imageHeight";

    /** The Constant IMAGE_WIDTH. */
    private static final String IMAGE_WIDTH = "imageWidth";

    /** The Constant FONT_STYLE. */
    private static final String FONT_STYLE = "style";

    /** The Constant FONT_SIZE. */
    private static final String FONT_SIZE = "size";

    /** The image width. */
    private int imageWidth = 20;

    /** The image height. */
    private int imageHeight = 20;

    /** The maintain aspect ratio. */
    private boolean maintainAspectRatio = true;

    /** The background colour. */
    private Color backgroundColour = Color.WHITE;

    /** The dpi. */
    private int dpi = 96;

    /** The show labels flag. */
    private boolean showLabels = true;

    /** The show title flag. */
    private boolean showTitle = true;

    /** The image size expressed as a percentage. 100% = original image size */
    private int imageSize = 100;

    /** The split symbolizers, if true then legend entries are saved to individual files. */
    private boolean splitSymbolizers = false;

    /** The label font. */
    private Font labelFont = LegendUtils.DEFAULT_FONT;

    /** The label font colour. */
    private Color labelFontColour = LegendUtils.DEFAULT_FONT_COLOR;

    /** The border colour. */
    private Color borderColour = LegendUtils.DEFAULT_BORDER_COLOR;

    /** The font anti aliasing. */
    private boolean fontAntiAliasing = true;

    /** The border flag. */
    private boolean border = false;

    /** The band information flag. */
    private boolean bandInformation = false;

    /** The transparent flag. */
    private boolean transparent = false;

    /**
     * Gets the image width.
     *
     * @return the image width
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Sets the image width.
     *
     * @param imageWidth the new image width
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;

        if (maintainAspectRatio) {
            this.imageHeight = imageWidth;
        }
    }

    /**
     * Gets the image height.
     *
     * @return the image height
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Sets the image height.
     *
     * @param imageHeight the new image height
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;

        if (maintainAspectRatio) {
            this.imageWidth = imageHeight;
        }
    }

    /**
     * Checks if is maintain aspect ratio.
     *
     * @return true, if is maintain aspect ratio
     */
    public boolean isMaintainAspectRatio() {
        return maintainAspectRatio;
    }

    /**
     * Sets the maintain aspect ratio.
     *
     * @param maintainAspectRatio the new maintain aspect ratio
     */
    public void setMaintainAspectRatio(boolean maintainAspectRatio) {
        this.maintainAspectRatio = maintainAspectRatio;
    }

    /**
     * Gets the dpi.
     *
     * @return the dpi
     */
    public int getDpi() {
        return dpi;
    }

    /**
     * Sets the dpi.
     *
     * @param dpi the new dpi
     */
    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    /**
     * Gets the background colour.
     *
     * @return the background colour
     */
    public Color getBackgroundColour() {
        return backgroundColour;
    }

    /**
     * Sets the background colour.
     *
     * @param backgroundColour the new background colour
     */
    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    /**
     * Sets the show labels flag.
     *
     * @param showLabels the new show labels
     */
    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

    /**
     * Gets the image size.
     *
     * @return the image size
     */
    public int getImageSize() {
        return imageSize;
    }

    /**
     * Sets the image size.
     *
     * @param imageSize the new image size
     */
    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    /**
     * Return split symbolizers flag.
     *
     * @return true, if successful
     */
    public boolean splitSymbolizers() {
        return splitSymbolizers;
    }

    /**
     * Sets the split symbolizers.
     *
     * @param splitSymbolizers the new split symbolizers
     */
    public void setSplitSymbolizers(boolean splitSymbolizers) {
        this.splitSymbolizers = splitSymbolizers;
    }

    /**
     * Checks if is show title.
     *
     * @return the showTitle
     */
    public boolean isShowTitle() {
        return showTitle;
    }

    /**
     * Sets the show title.
     *
     * @param showTitle the showTitle to set
     */
    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * Gets the label font.
     *
     * @return the labelFont
     */
    public Font getLabelFont() {
        return labelFont;
    }

    /**
     * Sets the label font.
     *
     * @param labelFont the labelFont to set
     */
    public void setLabelFont(Font labelFont) {
        this.labelFont = labelFont;
    }

    /**
     * Gets the label font colour.
     *
     * @return the labelFontColour
     */
    public Color getLabelFontColour() {
        return labelFontColour;
    }

    /**
     * Sets the label font colour.
     *
     * @param labelFontColour the labelFontColour to set
     */
    public void setLabelFontColour(Color labelFontColour) {
        this.labelFontColour = labelFontColour;
    }

    /**
     * Gets the border colour.
     *
     * @return the borderColour
     */
    public Color getBorderColour() {
        return borderColour;
    }

    /**
     * Sets the border colour.
     *
     * @param borderColour the borderColour to set
     */
    public void setBorderColour(Color borderColour) {
        this.borderColour = borderColour;
    }

    /**
     * Checks if is font anti aliasing.
     *
     * @return the fontAntiAliasing
     */
    public boolean isFontAntiAliasing() {
        return fontAntiAliasing;
    }

    /**
     * Sets the font anti aliasing.
     *
     * @param fontAntiAliasing the fontAntiAliasing to set
     */
    public void setFontAntiAliasing(boolean fontAntiAliasing) {
        this.fontAntiAliasing = fontAntiAliasing;
    }

    /**
     * Checks if is border.
     *
     * @return the border
     */
    public boolean isBorder() {
        return border;
    }

    /**
     * Sets the border.
     *
     * @param border the border to set
     */
    public void setBorder(boolean border) {
        this.border = border;
    }

    /**
     * Checks if is band information.
     *
     * @return the bandInformation
     */
    public boolean isBandInformation() {
        return bandInformation;
    }

    /**
     * Sets the band information.
     *
     * @param bandInformation the bandInformation to set
     */
    public void setBandInformation(boolean bandInformation) {
        this.bandInformation = bandInformation;
    }

    /**
     * Checks if is show labels.
     *
     * @return the showLabels
     */
    public boolean isShowLabels() {
        return showLabels;
    }

    /**
     * Checks if flag is transparent.
     *
     * @return the transparent
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Sets the transparent flag.
     *
     * @param transparent the transparent to set
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    /**
     * Encode XML.
     *
     * @param doc the doc
     * @param root the root
     * @param elementName the legend option element
     */
    public void encodeXML(Document doc, Element root, String elementName) {
        if ((doc == null) || (root == null) || (elementName == null)) {
            return;
        }

        Element lengendOptionElement = doc.createElement(elementName);

        createElement(doc, lengendOptionElement, IMAGE_WIDTH, imageWidth);
        createElement(doc, lengendOptionElement, IMAGE_HEIGHT, imageHeight);
        createElement(doc, lengendOptionElement, IMAGE_DPI, dpi);
        createElement(doc, lengendOptionElement, IMAGE_SIZE, imageSize);
        createElement(doc, lengendOptionElement, MAINTAIN_ASPECT_RATIO, maintainAspectRatio);
        createElement(doc, lengendOptionElement, SHOW_LABELS, showLabels);
        createElement(doc, lengendOptionElement, SHOW_TITLE, showTitle);
        createElement(doc, lengendOptionElement, SPLIT_SYMBOLIZERS, splitSymbolizers);
        createElement(doc, lengendOptionElement, FONT_ANTI_ALIASING, fontAntiAliasing);
        createElement(doc, lengendOptionElement, SHOW_BORDER, border);
        createElement(doc, lengendOptionElement, BAND_INFORMATION, bandInformation);
        createElement(doc, lengendOptionElement, BACKGROUND_TRANSPARENT, transparent);
        createElement(doc, lengendOptionElement, LABEL_FONT, labelFont);
        createElement(doc, lengendOptionElement, LABEL_FONT_COLOUR, labelFontColour);
        createElement(doc, lengendOptionElement, BORDER_COLOUR, borderColour);
        createElement(doc, lengendOptionElement, BACKGROUND_COLOUR, backgroundColour);

        root.appendChild(lengendOptionElement);
    }

    /**
     * Creates the XML element for a boolean value.
     *
     * @param doc the doc
     * @param parentElement the parent element
     * @param elementName the element name
     * @param value the value
     */
    private void createElement(
            Document doc, Element parentElement, String elementName, boolean value) {
        Element element = doc.createElement(elementName);
        element.appendChild(doc.createTextNode(Boolean.toString(value)));

        parentElement.appendChild(element);
    }

    /**
     * Creates the XML element for an integer value.
     *
     * @param doc the doc
     * @param parentElement the parent element
     * @param elementName the element name
     * @param value the value
     */
    private void createElement(Document doc, Element parentElement, String elementName, int value) {
        Element element = doc.createElement(elementName);
        element.appendChild(doc.createTextNode(Integer.toString(value)));

        parentElement.appendChild(element);
    }

    /**
     * Creates the XML element for a colour value.
     *
     * @param doc the doc
     * @param parentElement the parent element
     * @param elementName the element name
     * @param value the value
     */
    private void createElement(
            Document doc, Element parentElement, String elementName, Color value) {
        Element element = doc.createElement(elementName);
        element.appendChild(doc.createTextNode(ColourUtils.fromColour(value)));

        parentElement.appendChild(element);
    }

    /**
     * Creates the XML element for a font value.
     *
     * @param doc the doc
     * @param parentElement the parent element
     * @param elementName the element name
     * @param value the value
     */
    private void createElement(
            Document doc, Element parentElement, String elementName, Font value) {
        Element element = doc.createElement(elementName);
        element.setAttribute(FONT_SIZE, Integer.toString(value.getSize()));
        element.setAttribute(FONT_STYLE, Integer.toString(value.getStyle()));
        element.appendChild(doc.createTextNode(value.getName()));

        parentElement.appendChild(element);
    }

    /**
     * Decode the legend options from XML.
     *
     * @param document the document
     * @param elementName the element name
     * @return the data source properties
     */
    public static LegendOptionData decodeXML(Document document, String elementName) {
        LegendOptionData legendOptionData = new LegendOptionData();

        if ((document != null) && (elementName != null)) {
            NodeList nodeList = document.getElementsByTagName(elementName);
            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);

                Node child = node.getFirstChild();

                while (child != null) {
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        String nodeName = child.getNodeName();
                        if (nodeName.compareToIgnoreCase(IMAGE_WIDTH) == 0) {
                            legendOptionData.setImageWidth(decodeIntElement(child));
                        } else if (nodeName.compareToIgnoreCase(IMAGE_HEIGHT) == 0) {
                            legendOptionData.setImageHeight(decodeIntElement(child));
                        } else if (nodeName.compareToIgnoreCase(IMAGE_DPI) == 0) {
                            legendOptionData.setDpi(decodeIntElement(child));
                        } else if (nodeName.compareToIgnoreCase(IMAGE_SIZE) == 0) {
                            legendOptionData.setImageSize(decodeIntElement(child));
                        } else if (nodeName.compareToIgnoreCase(MAINTAIN_ASPECT_RATIO) == 0) {
                            legendOptionData.setMaintainAspectRatio(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(SHOW_LABELS) == 0) {
                            legendOptionData.setShowLabels(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(SHOW_TITLE) == 0) {
                            legendOptionData.setShowTitle(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(SPLIT_SYMBOLIZERS) == 0) {
                            legendOptionData.setSplitSymbolizers(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(FONT_ANTI_ALIASING) == 0) {
                            legendOptionData.setFontAntiAliasing(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(SHOW_BORDER) == 0) {
                            legendOptionData.setBorder(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(BAND_INFORMATION) == 0) {
                            legendOptionData.setBandInformation(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(BACKGROUND_TRANSPARENT) == 0) {
                            legendOptionData.setTransparent(decodeBooleanElement(child));
                        } else if (nodeName.compareToIgnoreCase(LABEL_FONT) == 0) {
                            legendOptionData.setLabelFont(decodeFontElement(child));
                        } else if (nodeName.compareToIgnoreCase(LABEL_FONT_COLOUR) == 0) {
                            legendOptionData.setLabelFontColour(decodeColourElement(child));
                        } else if (nodeName.compareToIgnoreCase(BORDER_COLOUR) == 0) {
                            legendOptionData.setBorderColour(decodeColourElement(child));
                        } else if (nodeName.compareToIgnoreCase(BACKGROUND_COLOUR) == 0) {
                            legendOptionData.setBackgroundColour(decodeColourElement(child));
                        }
                    }
                    child = child.getNextSibling();
                }
            }
        }
        return legendOptionData;
    }

    /**
     * Decode colour element.
     *
     * @param child the child
     * @return the color
     */
    private static Color decodeColourElement(Node child) {
        return ColourUtils.toColour(child.getTextContent());
    }

    /**
     * Decode font element.
     *
     * @param child the child
     * @return the font
     */
    private static Font decodeFontElement(Node child) {
        String name = child.getTextContent();
        NamedNodeMap attributes = child.getAttributes();
        Node sizeNode = attributes.getNamedItem(FONT_SIZE);
        Node styleNode = attributes.getNamedItem(FONT_STYLE);
        int size = Integer.parseInt(sizeNode.getNodeValue());
        int style = Integer.parseInt(styleNode.getNodeValue());
        return new Font(name, style, size);
    }

    /**
     * Decode boolean element.
     *
     * @param child the child
     * @return true, if successful
     */
    private static boolean decodeBooleanElement(Node child) {
        return Boolean.parseBoolean(child.getTextContent());
    }

    /**
     * Decode int element.
     *
     * @param child the child
     * @return the int
     */
    private static int decodeIntElement(Node child) {
        return Integer.parseInt(child.getTextContent());
    }
}
