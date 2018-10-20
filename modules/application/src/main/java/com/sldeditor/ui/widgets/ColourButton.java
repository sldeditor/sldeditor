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

package com.sldeditor.ui.widgets;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.iface.ColourNotifyInterface;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * A JButton displaying a selected colour, clicking on the button displays a colour editor.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourButton extends JButton {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The observers. */
    private ArrayList<ColourNotifyInterface> observers = new ArrayList<>();

    /** The colour. */
    private Color colour = Color.BLACK;

    private transient BufferedImage image = null;

    /** Instantiates a new colour button. */
    public ColourButton() {
        final JComponent parent = this;

        image =
                new BufferedImage(
                        BasePanel.WIDGET_STANDARD_WIDTH,
                        BasePanel.WIDGET_HEIGHT,
                        BufferedImage.TYPE_INT_ARGB);
        this.setIcon(new ImageIcon(image));
        addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        Color newColour = JColorChooser.showDialog(parent, "Pick color", colour);

                        if (newColour != null) {
                            colour = newColour;

                            displayColour();

                            notifyListeners();
                        }
                    }
                });
    }

    /**
     * Populate.
     *
     * @param newColour the new colour
     */
    public void populate(Color newColour) {
        if (colour == null) {
            colour = hex2Rgb(DefaultSymbols.defaultColour());
        } else {
            colour = newColour;
        }

        displayColour();
    }

    /**
     * Populate.
     *
     * @param value the colour value
     * @param opacity the opacity
     */
    public void populate(String value, Expression opacity) {
        Color tmpColour = null;
        float alpha = DefaultSymbols.defaultColourOpacity();
        if (value == null) {
            tmpColour = hex2Rgb(DefaultSymbols.defaultColour());
        } else {
            tmpColour = hex2Rgb(value);
        }

        if (opacity instanceof LiteralExpressionImpl) {
            LiteralExpressionImpl literal = (LiteralExpressionImpl) opacity;

            Object obj = literal.getValue();
            if (obj instanceof Long) {
                alpha = ((Long) obj).floatValue();
            } else if (obj instanceof Integer) {
                alpha = ((Integer) obj).floatValue();
            } else if (obj instanceof Float) {
                alpha = ((Float) obj).floatValue();
            } else if (obj instanceof Double) {
                alpha = ((Double) obj).floatValue();
            } else if (obj instanceof String) {
                Double d = Double.valueOf((String) obj);
                alpha = d.floatValue();
            } else {
                ConsoleManager.getInstance().error(this, "Unknown number format");
            }
        }
        colour =
                new Color(
                        tmpColour.getRed() / 255.0f,
                        tmpColour.getGreen() / 255.0f,
                        tmpColour.getBlue() / 255.0f,
                        alpha);
        displayColour();
    }

    /** Display colour. */
    private void displayColour() {

        Graphics2D g2 = image.createGraphics();
        g2.setColor(colour);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Display colour text
        g2.setColor(ColourUtils.getTextColour(colour));
        String htmlColour = ColourUtils.fromColour(colour);

        FontMetrics fm = g2.getFontMetrics();
        int totalWidth = (fm.stringWidth(htmlColour) * 2) + 4;

        int x = (getWidth() - totalWidth) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        g2.drawString(htmlColour, x, y);
        setContentAreaFilled(false);
        setOpaque(true);
        setBorderPainted(false);

        repaint();
    }

    /**
     * Convert colour encoded as hex to rgb.
     *
     * @param colourStr the colour string
     * @return the colour
     */
    public static Color hex2Rgb(String colourStr) {
        return new Color(
                Integer.valueOf(colourStr.substring(1, 3), 16),
                Integer.valueOf(colourStr.substring(3, 5), 16),
                Integer.valueOf(colourStr.substring(5, 7), 16));
    }

    /**
     * Register observer.
     *
     * @param observer the observer
     */
    public void registerObserver(ColourNotifyInterface observer) {
        observers.add(observer);
    }

    /** Notify listeners. */
    public void notifyListeners() {
        String colourString = getColourString();
        double opacity = getColourOpacity();

        for (ColourNotifyInterface observer : observers) {
            observer.notify(colourString, opacity);
        }
    }

    /**
     * Gets the colour string.
     *
     * @return the colour string
     */
    public String getColourString() {
        return ColourUtils.fromColour(colour);
    }

    /**
     * Gets the colour opacity.
     *
     * @return the colour opacity
     */
    public double getColourOpacity() {
        return (colour.getAlpha() / 255.0);
    }

    /**
     * Gets the colour.
     *
     * @return the colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Sets the colour.
     *
     * @param colour the new colour
     */
    public void setColour(Color colour) {
        this.colour = colour;
        displayColour();

        repaint();
    }
}
