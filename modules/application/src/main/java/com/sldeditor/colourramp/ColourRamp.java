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

package com.sldeditor.colourramp;

import com.sldeditor.colourramp.ramp.ColourRampData;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.ui.detail.BasePanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class ColourRamp.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRamp {

    /** The Constant IMAGE_WIDTH. */
    private static final int IMAGE_WIDTH = BasePanel.WIDGET_EXTENDED_WIDTH;

    /** The Constant IMAGE_HEIGHT. */
    private static final int IMAGE_HEIGHT = BasePanel.WIDGET_HEIGHT;

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /** The colour list. */
    private List<Color> colourList = new ArrayList<Color>();

    /** The range. */
    private int range = Integer.MIN_VALUE;

    /** The last min value. */
    private int lastMinValue = Integer.MIN_VALUE;

    /** The reverse colours flag. */
    private boolean lastReverseColoursFlag = false;

    /** The gradient image. */
    private BufferedImage gradientImage = null;

    /** Instantiates a new colour ramp. */
    public ColourRamp() {}

    /**
     * Adds the colour.
     *
     * @param colour the colour
     */
    public void addColour(Color colour) {
        colourList.add(colour);
    }

    /**
     * Sets the colour ramp.
     *
     * @param firstColour the first colour
     * @param lastColour the last colour
     */
    public void setColourRamp(Color firstColour, Color lastColour) {
        colourList.clear();
        colourList.add(firstColour);
        colourList.add(lastColour);
    }

    /**
     * Gets the colour list.
     *
     * @return the colourList
     */
    public List<Color> getColourList() {
        return colourList;
    }

    /**
     * Gets the start colour.
     *
     * @return the start colour
     */
    public Color getStartColour() {
        if (!colourList.isEmpty()) {
            return colourList.get(0);
        }
        return Color.BLACK;
    }

    /**
     * Gets the end colour.
     *
     * @return the end colour
     */
    public Color getEndColour() {
        if (!colourList.isEmpty()) {
            return colourList.get(colourList.size() - 1);
        }
        return Color.WHITE;
    }

    /**
     * Gets the image icon.
     *
     * @param reverseColours the reverse colours
     * @return the image icon
     */
    public ImageIcon getImageIcon(boolean reverseColours) {
        BufferedImage bufferedImage = createImage(IMAGE_WIDTH, reverseColours);

        ImageIcon imageIcon = new ImageIcon(bufferedImage);

        return imageIcon;
    }

    /**
     * Creates the image.
     *
     * @param width the width
     * @param reverseColours the reverse colours
     * @return the image icon
     */
    private BufferedImage createImage(int width, boolean reverseColours) {
        BufferedImage image = new BufferedImage(width, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        GradientPaint gradient =
                new GradientPaint(
                        0,
                        0,
                        reverseColours ? getEndColour() : getStartColour(),
                        width,
                        0,
                        reverseColours ? getStartColour() : getEndColour());
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, IMAGE_HEIGHT);

        return image;
    }

    /**
     * Gets the colour.
     *
     * @param data the data
     * @param quantityExpression the quantity expression
     * @param reverseColours the reverse colours
     * @return the colour
     */
    public Expression getColour(
            ColourRampData data, Expression quantityExpression, boolean reverseColours) {

        Expression expression = null;
        if (quantityExpression instanceof LiteralExpressionImpl) {
            int value = Integer.valueOf(quantityExpression.toString());

            int tmpRange = Math.abs(data.getMaxValue() - data.getMinValue());

            // Check to see if we have set up the gradient yet
            if ((range != tmpRange)
                    || (lastMinValue != data.getMinValue())
                    || (lastReverseColoursFlag != reverseColours)) {
                range = tmpRange;
                lastMinValue = data.getMinValue();
                gradientImage = createImage(range, reverseColours);
                lastReverseColoursFlag = reverseColours;
            }
            int pos = value - data.getMinValue();

            if (pos >= range) {
                pos = range - 1;
            }

            if (pos < 0) {
                pos = 0;
            }
            int rgb = gradientImage.getRGB(pos, 0);
            Color colour = new Color(rgb);

            expression = ff.literal(ColourUtils.fromColour(colour));
        }
        return expression;
    }
}
