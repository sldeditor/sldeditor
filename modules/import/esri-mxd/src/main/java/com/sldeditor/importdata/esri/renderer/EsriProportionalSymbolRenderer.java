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
import com.esri.arcgis.carto.ProportionalSymbolRenderer;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.importdata.esri.keys.renderer.ProportionalRendererKeys;

/**
 * Class that converts an EsriProportionalSymbolRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "ProportionalSymbolRenderer": {
 *        "exclusionClause": "getExclusionClause()",
 *        "exclusionDescription": "getExclusionDescription()",
 *        "exclusionLabel": "getExclusionLabel()",
 *        "field": "getField()",
 *        "minSymbol": getMinSymbol(),
 *        "normalisationField": "getNormalizationField()",
 *        "normalisationFieldAlias": "getNormalizationFieldAlias()",
 *        "normalisationTotal": "getNormalizationTotal()",
 *        "normalisationType": "getNormalizationType()",
 *        "rotationField": "getRotationField()",
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
 *        "valueRepresentation": getValueRepresentation(),
 *        "valueUnit": getValueUnit(),
 *        "weight": getWeight(),
 *        "graduatedSymbols": isSymbolsAreGraduated(),
 *        "flanneryCompensation": isFlanneryCompensation(),
 *        "showExclusionClass": isShowExclusionClass(),
 *        "exclusionSymbol": getExclusionSymbol(),
 *        "backgroundSymbol": getBackgroundSymbol()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriProportionalSymbolRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(ProportionalRendererKeys.class);

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#convert(com.esri.arcgis.carto.IFeatureRenderer)
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {

        logger.info(ProportionalRendererKeys.PROPORTIONAL_SYMBOL_RENDERER);

        ProportionalSymbolRenderer proportionalRenderer = (ProportionalSymbolRenderer) renderer;
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            rendererObject.addProperty(ProportionalRendererKeys.EXCLUSION_CLAUSE, proportionalRenderer.getExclusionClause());
            rendererObject.addProperty(ProportionalRendererKeys.EXCLUSION_DESCRIPTION, proportionalRenderer.getExclusionDescription());
            rendererObject.addProperty(ProportionalRendererKeys.EXCLUSION_LABEL, proportionalRenderer.getExclusionLabel());

            rendererObject.addProperty(ProportionalRendererKeys.FIELD, proportionalRenderer.getField());
            rendererObject.add(ProportionalRendererKeys.MIN_SYMBOL, ParseLayer.createSymbol(proportionalRenderer.getMinSymbol()));

            rendererObject.addProperty(ProportionalRendererKeys.NORMALISATION_FIELD, proportionalRenderer.getNormalizationField());
            rendererObject.addProperty(ProportionalRendererKeys.NORMALISATION_FIELD_ALIAS, proportionalRenderer.getNormalizationFieldAlias());
            rendererObject.addProperty(ProportionalRendererKeys.NORMALISATION_TOTAL, proportionalRenderer.getNormalizationTotal());
            rendererObject.addProperty(ProportionalRendererKeys.NORMALISATION_TYPE, proportionalRenderer.getNormalizationType());

            rendererObject.addProperty(CommonRendererKeys.ROTATIONFIELD, proportionalRenderer.getRotationField());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONTYPE, proportionalRenderer.getRotationType());

            // symbolRotation3DExpressions
            String [] symbolRotation3DExpressionsX = new String[1];
            String [] symbolRotation3DExpressionsY = new String[1];
            String [] symbolRotation3DExpressionsZ = new String[1];

            proportionalRenderer.getSymbolRotation3DExpressions(symbolRotation3DExpressionsX, symbolRotation3DExpressionsY, symbolRotation3DExpressionsZ);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_X, symbolRotation3DExpressionsX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_Y, symbolRotation3DExpressionsY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_Z, symbolRotation3DExpressionsZ[0]);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DFLAGS, proportionalRenderer.getSymbolRotation3DFlags());

            // symbolRotation3DFlags2
            int [] symbolRotation3DFlags2X = new int[1];
            int [] symbolRotation3DFlags2Y = new int[1];
            int [] symbolRotation3DFlags2Z = new int[1];

            proportionalRenderer.getSymbolRotation3DFlags2(symbolRotation3DFlags2X, symbolRotation3DFlags2Y, symbolRotation3DFlags2Z);

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

            proportionalRenderer.getSymbolRotation3DRandomRanges(symbolRotation3DRandomRangesMinRotationX, symbolRotation3DRandomRangesMinRotationY, symbolRotation3DRandomRangesMinRotationZ,
                    symbolRotation3DRandomRangesMaxRotationX, symbolRotation3DRandomRangesMaxRotationY, symbolRotation3DRandomRangesMaxRotationZ);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Z, symbolRotation3DRandomRangesMinRotationX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Y, symbolRotation3DRandomRangesMinRotationY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Z, symbolRotation3DRandomRangesMinRotationZ[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_X, symbolRotation3DRandomRangesMaxRotationX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Y, symbolRotation3DRandomRangesMaxRotationY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Z, symbolRotation3DRandomRangesMaxRotationZ[0]);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DROTATIONTYPE_Z, proportionalRenderer.getSymbolRotation3DRotationTypeZ());

            rendererObject.addProperty(ProportionalRendererKeys.VALUE_REPRESENTATION, proportionalRenderer.getValueRepresentation());
            rendererObject.addProperty(ProportionalRendererKeys.VALUE_UNIT, proportionalRenderer.getValueUnit());

            rendererObject.addProperty(ProportionalRendererKeys.WEIGHT, proportionalRenderer.getWeight());

            rendererObject.addProperty(CommonRendererKeys.GRADUATED_SYMBOLS, proportionalRenderer.isSymbolsAreGraduated());
            rendererObject.addProperty(ProportionalRendererKeys.FLANNERY_COMPENSATION, proportionalRenderer.isFlanneryCompensation());
            rendererObject.addProperty(ProportionalRendererKeys.SHOW_EXCLUSION_CLASS, proportionalRenderer.isShowExclusionClass());

            rendererObject.add(ProportionalRendererKeys.EXCLUSION_SYMBOL, ParseLayer.createSymbol(proportionalRenderer.getExclusionSymbol()));
            rendererObject.add(ProportionalRendererKeys.BACKGROUND_SYMBOL, ParseLayer.createSymbol((ISymbol) proportionalRenderer.getBackgroundSymbol()));

            jsonObject.add(ProportionalRendererKeys.PROPORTIONAL_SYMBOL_RENDERER, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return ProportionalSymbolRenderer.class;
    }
}
