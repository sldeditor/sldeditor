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

package com.sldeditor.ui.detail.config.colourmap;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.utils.ColourUtils;
import java.awt.Color;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class ColourMapData.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourMapData {

    /** The colour. */
    private Color colour;

    /** The colour expression. */
    private Expression colourExpression = null;

    /** The colour string. */
    private String colourString;

    /** The opacity. */
    private Expression opacity = null;

    /** The quantity. */
    private Expression quantity = null;

    /** The label. */
    private String label;

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /** Instantiates a new colour map data. */
    public ColourMapData() {
        // Default constructor
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
     * @param colourExpression the new colour
     */
    public void setColour(Expression colourExpression) {
        this.colourExpression = colourExpression;
        if (colourExpression instanceof LiteralExpressionImpl) {
            colourString = ((LiteralExpressionImpl) colourExpression).toString();

            if (ColourUtils.validColourString(colourString)) {
                this.colour = ColourUtils.toColour(colourString);
            } else {
                this.colour = Color.white;
            }
        } else if (colourExpression instanceof Expression) {
            colourString = ((Expression) colourExpression).toString();
            this.colour = Color.white;
        }
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public Expression getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(Expression quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the colour string.
     *
     * @return the colourString
     */
    public String getColourString() {
        return colourString;
    }

    /**
     * Gets the opacity.
     *
     * @return the opacity
     */
    public Expression getOpacity() {
        if (opacity == null) {
            opacity = ff.literal(DefaultSymbols.defaultColourOpacity());
        }
        return opacity;
    }

    /**
     * Sets the opacity.
     *
     * @param opacity the opacity to set
     */
    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the colour expression.
     *
     * @return the colourExpression
     */
    public Expression getColourExpression() {
        return colourExpression;
    }

    /**
     * Gets the next quantity.
     *
     * @return the next quantity
     */
    public Expression getNextQuantity() {
        Expression nextQuantity = null;

        if (quantity != null) {
            if (quantity instanceof LiteralExpressionImpl) {
                double quantityValue = Double.valueOf(quantity.toString());
                nextQuantity = ff.literal(quantityValue + 1);
            } else {
                // Could be a function expression so just leave alone
                nextQuantity = quantity;
            }
        }
        return nextQuantity;
    }

    /**
     * Update the contents of this class with the supplied data.
     *
     * @param newData the new data
     */
    public void update(ColourMapData newData) {
        if (newData != null) {
            if (newData.label != null) {
                this.label = newData.label;
            }

            if (newData.getColourExpression() != null) {
                setColour(newData.getColourExpression());
            }

            if (newData.getOpacity() != null) {
                setOpacity(newData.getOpacity());
            }

            if (newData.getQuantity() != null) {
                setQuantity(newData.getQuantity());
            }
        }
    }
}
