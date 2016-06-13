/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.esri.arcgis.display.IColor;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IGeometryCollection;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.IPointCollection;
import com.esri.arcgis.geometry.IPolygon4;
import com.esri.arcgis.geometry.IRing;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.ITopologicalOperator2;
import com.esri.arcgis.geometry.Line;
import com.esri.arcgis.geometry.Multipoint;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.json.JSONArray;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.arcgis.support.ms.stdole.Font;
import com.esri.arcgis.support.ms.stdole.Picture;
import com.esri.arcgis.system.ServerUtilities;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.symbols.ColourKeys;
import com.sldeditor.importdata.esri.keys.symbols.CommonPictureKeys;
import com.sldeditor.importdata.esri.keys.symbols.FontSymbolKeys;

/**
 * Class provides common utility methods.
 * 
 * @author Robert Ward (SCISYS)
 */
public class CommonObjects {

    /** The Constant SPATIAL_REFERENCE. */
    private static final String SPATIAL_REFERENCE = "spatialReference";

    /** The Constant WKID. */
    private static final String WKID = "wkid";

    /** The Constant RINGS. */
    private static final String RINGS = "rings";

    /** The Constant X. */
    private static final String X = "x";

    /** The Constant Y. */
    private static final String Y = "y";

    /** The Constant Z. */
    private static final String Z = "z";

    /** The Constant LINE. */
    private static final String LINE = "line";

    /** The Constant ATTRIBUTES. */
    public static final String ATTRIBUTES = "attributes";

    /**
     * Creates the picture.
     *
     * @param picture the picture
     * @return the json object
     */
    public static JsonObject createPicture(Picture picture) {
        JsonObject jsonObject = null;
        if(picture != null)
        {
            jsonObject = new JsonObject();
            try {
                BufferedImage image = (BufferedImage) picture.toImage();

                jsonObject.addProperty(CommonPictureKeys.HEIGHT, image.getHeight(null));
                jsonObject.addProperty(CommonPictureKeys.WIDTH, image.getWidth(null));

                String imageType = null;

                switch(picture.getType())
                {
                case 1:
                    imageType = "png";
                    break;
                default:
                    imageType = "png";
                    break;
                }
                jsonObject.addProperty(CommonPictureKeys.TYPE, imageType);
                String encodedImage = encodeToString(toBufferedImage(image), imageType);
                jsonObject.addProperty(CommonPictureKeys.IMAGE, encodedImage);

            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    /**
     * To buffered image.
     *
     * @param img the img
     * @return the buffered image
     */
    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /**
     * Encode to string.
     *
     * @param image the image
     * @param type the type
     * @return the string
     */
    private static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = DatatypeConverter.printBase64Binary(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    /**
     * Creates the font.
     *
     * @param font the font
     * @return the json element
     */
    public static JsonElement createFont(Font font) {
        JsonObject jsonObject = new JsonObject();

        try {
            jsonObject.addProperty(FontSymbolKeys.FONT_NAME, font.getName());
            jsonObject.addProperty(FontSymbolKeys.BOLD, font.getBold());
            jsonObject.addProperty(FontSymbolKeys.CHARSET, font.getCharset());
            jsonObject.addProperty(FontSymbolKeys.ITALIC, font.getItalic());
            jsonObject.addProperty(FontSymbolKeys.FONT_SIZE, font.getSize());
            jsonObject.addProperty(FontSymbolKeys.STRIKE_THROUGH, font.getStrikethrough());
            jsonObject.addProperty(FontSymbolKeys.UNDERLINE, font.getUnderline());
            jsonObject.addProperty(FontSymbolKeys.FONT_WEIGHT, font.getWeight());
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Creates the colour.
     *
     * @param colour the colour
     * @return the json object
     */
    public static JsonObject createColour(IColor colour) {
        JsonObject jsonObject = null;

        if(colour != null)
        {
            try {
                if(!colour.isNullColor())
                {
                    int rgb = colour.getRGB();
                    int blue = (rgb & 0xFF0000) >> 16;
                    int green = (rgb & 0x00FF00) >> 8;
                    int red = rgb & 0x0000FF;

                    jsonObject = new JsonObject();
                    jsonObject.addProperty(ColourKeys.RED, red);
                    jsonObject.addProperty(ColourKeys.GREEN, green);
                    jsonObject.addProperty(ColourKeys.BLUE, blue);
                    jsonObject.addProperty(ColourKeys.TRANSPARENCY, (int)colour.getTransparency());
                }
            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * Creates the point.
     *
     * @param point the point
     * @return the json element
     */
    public static JsonElement createPoint(IPoint point) {
        JsonObject jsonObject = null;

        if(point != null)
        {
            jsonObject = new JsonObject();
            try {
                jsonObject.addProperty("x", point.getX());
                jsonObject.addProperty("y", point.getY());
                jsonObject.addProperty("z", point.getZ());
            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * Creates the geometry.
     *
     * @param geometry the geometry
     * @return the json element
     */
    public static JsonElement createGeometry(IGeometry geometry) {
        JSONObject esriJsonObject = null;

        try {
            switch (geometry.getGeometryType())
            {
            case esriGeometryType.esriGeometryPoint:
                esriJsonObject = getJSONFromPoint((Point)geometry);
                break;

            case esriGeometryType.esriGeometryMultipoint:
                esriJsonObject = ServerUtilities.getJSONFromMultipoint((Multipoint)geometry);
                break;

            case esriGeometryType.esriGeometryPolyline:
                esriJsonObject = ServerUtilities.getJSONFromPolyline((Polyline)geometry);
                break;

            case esriGeometryType.esriGeometryPolygon:
                esriJsonObject = getJSONFromPolygon((Polygon)geometry);
                break;

            case esriGeometryType.esriGeometryEnvelope:
                esriJsonObject = ServerUtilities.getJSONFromEnvelope((Envelope)geometry);
                break;

            case esriGeometryType.esriGeometryLine:
                esriJsonObject = getJSONFromLine((Line)geometry);
                break;

            default:
                System.err.println(
                        "Only geometries of type Point, Multipoint, Polyline, Polygon and Envelope are supported by this conversion utility. "
                                + geometry.getGeometryType());
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jobj = new Gson().fromJson(esriJsonObject.toString(), JsonObject.class);

        return jobj;
    }

    /**
     * Gets the JSON from point.
     *
     * @param geometry the geometry
     * @return the JSON from point
     */
    private static JSONObject getJSONFromPoint(Point geometry)
    {
        JSONObject retVal = null;

        if(geometry != null)
        {
            try
            {
                retVal = new JSONObject();

                Point point = (Point)geometry;
                retVal = new JSONObject();
                retVal.put(X, point.getX());
                retVal.put(Y, point.getY());

                if (point.isZAware() == true)
                {
                    retVal.put(Z, point.getZ());
                }

                if (point.getSpatialReference() != null)
                {
                    JSONObject sr = new JSONObject();
                    sr.put(WKID, point.getSpatialReference().getFactoryCode());
                    retVal.put(SPATIAL_REFERENCE, sr);
                }
            }
            catch (AutomationException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return retVal;
    }

    private static JSONArray getJSONArrayFromPoint(Point point, boolean useZ)
    {
        JSONArray jsonPoint = null;

        if(point != null)
        {
            try
            {
                jsonPoint = new JSONArray();

                jsonPoint.put(point.getX());
                jsonPoint.put(point.getY());

                if ((useZ == true) && (point.isZAware() == true))
                {
                    jsonPoint.put(point.getZ());
                }
            }
            catch (AutomationException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return jsonPoint;
    }

    /**
     * Gets the JSON from polygon.
     *
     * @param polygon the polygon
     * @return the JSON from polygon
     */
    public static JSONObject getJSONFromPolygon(Polygon polygon)
    {
        if (polygon == null) return null;

        JSONObject jsonPolygon = null;

        try {
            polygon.weed(0.1);

            ITopologicalOperator2 topologicalOperator2 = (ITopologicalOperator2)polygon;
            topologicalOperator2.setIsKnownSimple(true);

            jsonPolygon = new JSONObject();

            JSONArray jsonRings = new JSONArray();

            IPolygon4 polygon4 = polygon;

            IGeometryCollection exteriorGeometryCollection = (IGeometryCollection)polygon4.getExteriorRingBag();

            for (int i = 0; i < exteriorGeometryCollection.getGeometryCount(); i++)
            {
                IGeometry exteriorGeometry = exteriorGeometryCollection.getGeometry(i);

                if ((exteriorGeometry != null) && (exteriorGeometry instanceof IPointCollection))
                {
                    JSONArray jsonExteriorRing = new JSONArray();

                    IPointCollection exteriorPointCollection = (IPointCollection)exteriorGeometry;

                    for (int j = 0; j < exteriorPointCollection.getPointCount(); j++)
                    {
                        IPoint point = exteriorPointCollection.getPoint(j);

                        jsonExteriorRing.put(getJSONArrayFromPoint((Point)point, false));
                    }

                    jsonRings.put(jsonExteriorRing);

                    if (exteriorGeometry instanceof IRing)
                    {
                        IRing exteriorRing = (IRing)exteriorGeometry;

                        IGeometryCollection interiorGeometryCollection =
                                (IGeometryCollection)polygon4.getInteriorRingBag(exteriorRing);

                        for (int j = 0; j < interiorGeometryCollection.getGeometryCount(); j++)
                        {
                            IGeometry interiorGeometry = interiorGeometryCollection.getGeometry(j);

                            if ((interiorGeometry != null) && (interiorGeometry instanceof IPointCollection))
                            {
                                JSONArray jsonInteriorRing = new JSONArray();

                                IPointCollection interiorPointCollection = (IPointCollection)interiorGeometry;

                                for (int k = 0; k < interiorPointCollection.getPointCount(); k++)
                                {
                                    IPoint point = interiorPointCollection.getPoint(k);

                                    jsonInteriorRing.put(getJSONArrayFromPoint((Point)point, false));
                                }

                                jsonRings.put(jsonInteriorRing);
                            }
                        }
                    }
                }
            }

            jsonPolygon.put(RINGS, jsonRings);

            ISpatialReference spatialReference = polygon.getSpatialReference();

            if (spatialReference != null)
            {
                JSONObject jsonSpatialReference = new JSONObject();
                jsonSpatialReference.put(WKID, spatialReference.getFactoryCode());

                jsonPolygon.put(SPATIAL_REFERENCE, jsonSpatialReference);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonPolygon;
    }

    /**
     * Gets the JSON from line.
     *
     * @param geometry the geometry
     * @return the JSON from line
     * @throws GISException the GIS exception
     */
    private static JSONObject getJSONFromLine(Line geometry)
    {
        JSONObject retVal = null;

        if(geometry != null)
        {
            retVal = new JSONObject();

            JSONArray line = new JSONArray();

            IPoint fromPoint;
            try {
                fromPoint = geometry.getFromPoint();
                IPoint toPoint = geometry.getToPoint();

                line.put(getJSONArrayFromPoint((Point)fromPoint, true));
                line.put(getJSONArrayFromPoint((Point)toPoint, true));

                retVal.put(LINE, line);

                ISpatialReference spatialReference = geometry.getSpatialReference();

                if (spatialReference != null)
                {
                    JSONObject jsonSpatialReference = new JSONObject();
                    jsonSpatialReference.put(WKID, spatialReference.getFactoryCode());

                    retVal.put(SPATIAL_REFERENCE, jsonSpatialReference);
                }
            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }
}
