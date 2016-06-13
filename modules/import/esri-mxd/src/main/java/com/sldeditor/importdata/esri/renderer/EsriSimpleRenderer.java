/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.renderer;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.carto.SimpleRenderer;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.importdata.esri.keys.renderer.SimpleRendererKeys;

/**
 * Class that converts an EsriSimpleRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "SimpleRenderer": {
 *        "description": "getDescription()",
 *        "label": "getLabel()",
 *        "rotationField": "getRotationField()",
 *        "rotationType": getRotationType(),
 *        "sizeExpression": "getSizeRendererExpression()",
 *        "sizeFlags": getSizeRendererFlags(),
 *        "transparencyField": "getTransparencyField()",
 *        "graduatedSymbols": isSymbolsAreGraduated(),
 *        "usesFilter": isUsesFilter(),
 *        "symbol": getSymbol(),
 *        "symbolRotation3DExpressionsX": "getSymbolRotation3DExpressions()",
 *        "symbolRotation3DExpressionsY": "getSymbolRotation3DExpressions()",
 *        "symbolRotation3DExpressionsZ": "getSymbolRotation3DExpressions()",
 *        "symbolRotation3DFlags": getSymbolRotation3DFlags(),
 *        "symbolRotation3DFlags2X": getSymbolRotation3DFlags2(),
 *        "symbolRotation3DFlags2Y": getSymbolRotation3DFlags2(),
 *        "symbolRotation3DFlags2Z": getSymbolRotation3DFlags2(),
 *        "symbolRotation3DRandomRangesMinRotationX": getSymbolRotation3DRandomRanges(),
 *        "symbolRotation3DRandomRangesMinRotationY": getSymbolRotation3DRandomRanges(),
 *        "symbolRotation3DRandomRangesMinRotationZ": getSymbolRotation3DRandomRanges(),
 *        "symbolRotation3DRandomRangesMaxRotationX": getSymbolRotation3DRandomRanges(),
 *        "symbolRotation3DRandomRangesMaxRotationY": getSymbolRotation3DRandomRanges(),
 *        "symbolRotation3DRandomRangesMaxRotationZ": getSymbolRotation3DRandomRanges(),
 *        "symbolRotation3DRotationTypeZ": getSymbolRotation3DRotationTypeZ()
 *     }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriSimpleRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(EsriSimpleRenderer.class);

    /**
     * Convert.
     *
     * @param renderer the renderer
     * @return the json object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#convert(com.esri.arcgis.carto.IFeatureRenderer)
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {

        SimpleRenderer simpleRenderer = (SimpleRenderer) renderer;
        logger.info("SimpleRenderer");

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            rendererObject.addProperty(CommonRendererKeys.DESCRIPTION, simpleRenderer.getDescription());
            rendererObject.addProperty(CommonRendererKeys.LABEL, simpleRenderer.getLabel());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONFIELD, simpleRenderer.getRotationField());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONTYPE, simpleRenderer.getRotationType());
            rendererObject.addProperty(CommonRendererKeys.SIZE_EXPRESSION, simpleRenderer.getSizeRendererExpression());
            rendererObject.addProperty(CommonRendererKeys.SIZE_FLAGS, simpleRenderer.getSizeRendererFlags());
            rendererObject.addProperty(CommonRendererKeys.TRANSPARENCY_FIELD, simpleRenderer.getTransparencyField());
            rendererObject.addProperty(CommonRendererKeys.GRADUATED_SYMBOLS, simpleRenderer.isSymbolsAreGraduated());
            rendererObject.addProperty(CommonRendererKeys.USES_FILTER, simpleRenderer.isUsesFilter());
            rendererObject.add(CommonRendererKeys.SYMBOL, ParseLayer.createSymbol(simpleRenderer.getSymbol()));

            // symbolRotation3DExpressions
            String [] symbolRotation3DExpressionsX = new String[1];
            String [] symbolRotation3DExpressionsY = new String[1];
            String [] symbolRotation3DExpressionsZ = new String[1];

            simpleRenderer.getSymbolRotation3DExpressions(symbolRotation3DExpressionsX, symbolRotation3DExpressionsY, symbolRotation3DExpressionsZ);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_X, symbolRotation3DExpressionsX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_Y, symbolRotation3DExpressionsY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_Z, symbolRotation3DExpressionsZ[0]);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DFLAGS, simpleRenderer.getSymbolRotation3DFlags());

            // symbolRotation3DFlags2
            int [] symbolRotation3DFlags2X = new int[1];
            int [] symbolRotation3DFlags2Y = new int[1];
            int [] symbolRotation3DFlags2Z = new int[1];

            simpleRenderer.getSymbolRotation3DFlags2(symbolRotation3DFlags2X, symbolRotation3DFlags2Y, symbolRotation3DFlags2Z);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DFLAGS2_X, symbolRotation3DFlags2X[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DFLAGS2_Y, symbolRotation3DFlags2Y[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DFLAGS2_Z, symbolRotation3DFlags2Z[0]);

            // symbolRotation3DRandomRanges
            double [] symbolRotation3DRandomRangesMinRotationX = new double[1];
            double [] symbolRotation3DRandomRangesMinRotationY = new double[1];
            double [] symbolRotation3DRandomRangesMinRotationZ = new double[1];
            double [] symbolRotation3DRandomRangesMaxRotationX = new double[1];
            double [] symbolRotation3DRandomRangesMaxRotationY = new double[1];
            double [] symbolRotation3DRandomRangesMaxRotationZ = new double[1];

            simpleRenderer.getSymbolRotation3DRandomRanges(symbolRotation3DRandomRangesMinRotationX, symbolRotation3DRandomRangesMinRotationY, symbolRotation3DRandomRangesMinRotationZ,
                    symbolRotation3DRandomRangesMaxRotationX, symbolRotation3DRandomRangesMaxRotationY, symbolRotation3DRandomRangesMaxRotationZ);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_X, symbolRotation3DRandomRangesMinRotationX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Y, symbolRotation3DRandomRangesMinRotationY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Z, symbolRotation3DRandomRangesMinRotationZ[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_X, symbolRotation3DRandomRangesMaxRotationX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Y, symbolRotation3DRandomRangesMaxRotationY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Z, symbolRotation3DRandomRangesMaxRotationZ[0]);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DROTATIONTYPE_Z, simpleRenderer.getSymbolRotation3DRotationTypeZ());
            jsonObject.add(SimpleRendererKeys.RENDERER_SIMPLE, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * Gets the renderer class.
     *
     * @return the renderer class
     */
    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return SimpleRenderer.class;
    }
}
