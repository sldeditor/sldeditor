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
package com.sldeditor.exportdata.esri.symbols;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.exportdata.esri.keys.symbols.CommonPictureKeys;
import com.sldeditor.exportdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.exportdata.esri.keys.symbols.PictureFillSymbolKeys;

/**
 * Converts an Esri PictureFillSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PictureFillSymbol extends BaseSymbol implements EsriSymbolInterface, EsriFillSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#getName()
     */
    @Override
    public String getName() {
        return PictureFillSymbolKeys.PICTURE_FILL_SYMBOL;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        if(rule == null) return;
        if(element == null) return;

        JsonObject obj = element.getAsJsonObject();

        List<Symbolizer> symbolizerList = rule.symbolizers();

        List<Stroke> strokeList = SymbolManager.getInstance().getStrokeList(obj.get(PictureFillSymbolKeys.OUTLINE));

        Stroke stroke = null;

        if(!strokeList.isEmpty())
        {
            stroke = strokeList.get(0);
        }

        Fill fill = getFill(layerName, obj, transparency);

        PolygonSymbolizer polygonSymbolizer = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        symbolizerList.add(polygonSymbolizer);
    }

    /**
     * Gets the fill.
     *
     * @param layerName the layer name
     * @param obj the obj
     * @param transparency the transparency
     * @return the fill
     */
    @SuppressWarnings("unused")
    private Fill getFill(String layerName, JsonObject obj, int transparency)
    {
        double angle = getInt(obj, CommonSymbolKeys.ANGLE);
        double xOffset = getInt(obj, CommonSymbolKeys.X_OFFSET);
        double yOffset = getInt(obj, CommonSymbolKeys.Y_OFFSET);
        double xScale = getInt(obj, PictureFillSymbolKeys.X_SCALE);
        double yScale = getInt(obj, PictureFillSymbolKeys.Y_SCALE);
        Graphic graphic = null;

        JsonElement pictureElement = obj.get(PictureFillSymbolKeys.PICTURE);

        if(pictureElement != null)
        {
            JsonObject pictureObj = pictureElement.getAsJsonObject();

            JsonElement imageElement = pictureObj.get(CommonPictureKeys.IMAGE);

            if(imageElement != null)
            {
                String imageString = imageElement.getAsString();

                byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageString);

                int height = getInt(pictureObj, CommonPictureKeys.HEIGHT);
                int width = getInt(pictureObj, CommonPictureKeys.WIDTH);
                
                String imageType = getString(pictureObj, CommonPictureKeys.TYPE);

                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                BufferedImage image = null;
                try
                {
                    image = ImageIO.read(bis);
                    bis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }

                if(image != null)
                {
                    String filename = String.format("%s.%s", layerName, imageType);
                    File file = new File(filename);
                    BufferedOutputStream buffOutStream = null;
                    try {
                        buffOutStream = new BufferedOutputStream(new FileOutputStream(file));
                        
                        Expression foregroundColour = getColour(obj.get(CommonSymbolKeys.COLOUR));
                        Expression backgroundColour = getColour(obj.get(PictureFillSymbolKeys.BACKGROUND_COLOUR));
                        if((foregroundColour != null) && (backgroundColour != null))
                        {
                            setForegroundColour(foregroundColour, backgroundColour, image);
                        }
                        ImageIO.write(image, imageType, buffOutStream);
                    }
                    catch (IOException e)
                    {
                        ConsoleManager.getInstance().exception(this, e);
                    }            
                    finally {
                        if(buffOutStream != null)
                        {
                            try
                            {
                                buffOutStream.close();
                            }
                            catch (IOException e)
                            {
                                ConsoleManager.getInstance().exception(this, e);
                            }
                        }
                    }

                    String fileExtension = ExternalFilenames.getFileExtension(filename);
                    String imageFormat = ExternalFilenames.getImageFormat(fileExtension);

                    ExternalGraphic externalGraphic = styleFactory.createExternalGraphic(file.toURI().toString(), imageFormat);
                    List<GraphicalSymbol> symbols = getSymbolList(externalGraphic);

                    Expression size = null;
                    Expression opacity = null;
                    Displacement displacement = styleFactory.createDisplacement(ff.literal(xOffset), ff.literal(yOffset));
                    AnchorPoint anchorPoint = null;
                    graphic = styleFactory.graphic(symbols, opacity, size, ff.literal(angle), anchorPoint, displacement);
                }
            }
        }

        Fill fill = styleFactory.createFill(getColour(obj.get(CommonSymbolKeys.COLOUR)),
            getColour(obj.get(PictureFillSymbolKeys.BACKGROUND_COLOUR)),
            getTransparency(transparency), 
            graphic);

        return fill;
    }

    /**
     * Gets the symbol list.
     *
     * @param symbolToAdd the symbol to add
     * @return the symbol list
     */
    private List<GraphicalSymbol> getSymbolList(GraphicalSymbol symbolToAdd)
    {
        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();

        symbolList.add(symbolToAdd);

        return symbolList;
    }

    /**
     * Sets the foreground colour.
     *
     * @param foregroundColourExpression the foreground colour expression
     * @param backgroundColourExpression the background colour expression
     * @param image the image
     */
    private void setForegroundColour(Expression foregroundColourExpression,
        Expression backgroundColourExpression,
        BufferedImage image)
    {
        List<Integer> palette = new ArrayList<Integer>();
        
        for(int x = 0; x < image.getWidth(); x ++)
        {
            for(int y = 0; y < image.getHeight(); y ++)
            {
                int colourValue = image.getRGB(x, y) & 0xFFFFFF;
                
                if(!palette.contains(colourValue))
                {
                    palette.add(colourValue);
                }
            }
        }
        
        if(palette.size() == 2)
        {
            // Strip off the '#'
            int backgroundColour = ColourUtils.getIntColour(backgroundColourExpression);
            int foregroundColour = ColourUtils.getIntColour(foregroundColourExpression);

            for(int x = 0; x < image.getWidth(); x ++)
            {
                for(int y = 0; y < image.getHeight(); y ++)
                {
                    int colourValue = image.getRGB(x, y) & 0xFFFFFF;
                    
                    if(colourValue != backgroundColour)
                    {
                        image.setRGB(x, y, foregroundColour);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriFillSymbolInterface#convertToFill(java.lang.String, com.google.gson.JsonElement, int)
     */
    @Override
    public List<Symbolizer> convertToFill(String layerName, JsonElement element, int transparency) {
        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        List<Symbolizer> symbolizerList = new ArrayList<Symbolizer>();

        Fill fill = getFill(layerName, obj, transparency);

        PolygonSymbolizer polygon = styleFactory.createPolygonSymbolizer();
        polygon.setStroke(null);
        polygon.setFill(fill);
        symbolizerList.add(polygon);

        return symbolizerList;
    }
}
