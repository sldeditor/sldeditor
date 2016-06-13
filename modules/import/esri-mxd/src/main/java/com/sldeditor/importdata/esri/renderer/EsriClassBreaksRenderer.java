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

import com.esri.arcgis.carto.ClassBreaksRenderer;
import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.ClassBreakRendererKeys;
import com.sldeditor.importdata.esri.keys.renderer.CommonRendererKeys;

/**
 * Class that converts an EsriClassBreaksRenderer into JSON.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EsriClassBreaksRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(ClassBreakRendererKeys.class);

    /**
     * Convert class breaks renderer.
     * <p>
     * <pre>
     * {@code
     *  "ClassBreaksRenderer": {
     *        "deviationInterval" : "getDeviationInterval()",
     *        "rotationField" : "getRotationField()",
     *        "rotationType" : getRotationType(),
     *        "exclusionClause" : "getExclusionClause()",
     *        "exclusionDescription" : "getExclusionDescription()",
     *        "exclusionLabel" : "getExclusionLabel()",
     *        "flipSymbols", isFlipSymbols(),
     *        "maxSampleSize" : getMaxSampleSize(),
     *        "graduatedSymbols", isSymbolsAreGraduated(),
     *        "classificationField" : "getField()",
     *        "minimumBreak" : getMinimumBreak(),
     *        "normalisationField" : "getNormalizationField()",
     *        "normalisationFieldAlias" : "getNormalizationFieldAlias()",
     *        "normalisationFieldTotal" : getNormalizationTotal(),
     *        "normalisationType" : getNormalizationType(),
     *        "normField" : getNormField(),
     *        "samplingMethod" : getSamplingMethod(),
     *        "sizeRendererExpression" : "getSizeRendererExpression()",
     *        "sizeRendererFlags" : getSizeRendererFlags(),
     *        "showClassGaps" : isShowClassGaps(),
     *        "showExclusionGaps" : isShowExclusionClass(),
     *        "sortClassesAscending" : isSortClassesAscending(),
     *        "sizeRendererRandomRangeMin" : getSizeRendererRandomRange(),
     *        "sizeRendererRandomRangeMax" : getSizeRendererRandomRange(),
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
     *        "weight": getWeight(),
     *        "backgroundSymbol": getBackgroundSymbol(),
     *        "exclusionClause": getExclusionClause(),
     *        "numberFormat": getNumberFormat(),
     *        "fields": [
     *           {
     *             "name" : "getField(fieldIndex)"
     *           },
     *           ...
     *        ],
     *        "values": [
     *           {
     *             "break" : getBreak(breakIndex),
     *             "lowBreak" : getLowBreak(breakIndex),
     *             "name" : getLabel(breakIndex),
     *             "description" : getDescription(breakIndex),
     *             "whereClause" : getWhereClause(breakIndex),
     *             "symbol" : getSymbol(breakIndex)
     *           },
     *           ...
     *        ],
     *   }
     * }
     * </pre>
     * @param renderer the renderer
     * @return the json object
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {
        ClassBreaksRenderer classBreaksRenderer = (ClassBreaksRenderer) renderer;
        logger.info(ClassBreakRendererKeys.CLASS_BREAKS_RENDERER);

        int index = -1;

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            JsonArray fieldArray = new JsonArray();

            JsonArray valueArray = new JsonArray();

            rendererObject.addProperty(ClassBreakRendererKeys.DEVIATION_INTERVAL, classBreaksRenderer.getDeviationInterval());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONFIELD, classBreaksRenderer.getRotationField());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONTYPE, classBreaksRenderer.getRotationType());
            rendererObject.addProperty(ClassBreakRendererKeys.EXCLUSION_CLAUSE, classBreaksRenderer.getExclusionClause());
            rendererObject.addProperty(ClassBreakRendererKeys.EXCLUSION_DESCRIPTION, classBreaksRenderer.getExclusionDescription());
            rendererObject.addProperty(ClassBreakRendererKeys.EXCLUSION_LABEL, classBreaksRenderer.getExclusionLabel());

            rendererObject.addProperty(ClassBreakRendererKeys.FLIP_SYMBOLS, classBreaksRenderer.isFlipSymbols());
            rendererObject.addProperty(ClassBreakRendererKeys.MAX_SAMPLE_SIZE, classBreaksRenderer.getMaxSampleSize());
            rendererObject.addProperty(CommonRendererKeys.GRADUATED_SYMBOLS, classBreaksRenderer.isSymbolsAreGraduated());
            rendererObject.addProperty(ClassBreakRendererKeys.CLASSIFICATION_FIELD, classBreaksRenderer.getField());
            rendererObject.addProperty(ClassBreakRendererKeys.MINIMUM_BREAK, classBreaksRenderer.getMinimumBreak());
            rendererObject.addProperty(ClassBreakRendererKeys.NORMALISATION_FIELD, classBreaksRenderer.getNormalizationField());
            rendererObject.addProperty(ClassBreakRendererKeys.NORMALISATION_FIELD_ALIAS, classBreaksRenderer.getNormalizationFieldAlias());
            rendererObject.addProperty(ClassBreakRendererKeys.NORMALISATION_FIELD_TOTAL, classBreaksRenderer.getNormalizationTotal());
            rendererObject.addProperty(ClassBreakRendererKeys.NORMALISATION_TYPE, classBreaksRenderer.getNormalizationType());
            rendererObject.addProperty(ClassBreakRendererKeys.NORM_FIELD, classBreaksRenderer.getNormField());
            rendererObject.addProperty(ClassBreakRendererKeys.SAMPLING_METHOD, classBreaksRenderer.getSamplingMethod());
            rendererObject.addProperty(ClassBreakRendererKeys.SIZE_RENDERER_EXPRESSION, classBreaksRenderer.getSizeRendererExpression());
            rendererObject.addProperty(ClassBreakRendererKeys.SIZE_RENDERER_FLAGS, classBreaksRenderer.getSizeRendererFlags());
            rendererObject.addProperty(ClassBreakRendererKeys.SHOW_CLASS_GAPS, classBreaksRenderer.isShowClassGaps());
            rendererObject.addProperty(ClassBreakRendererKeys.SHOW_EXCLUSION_GAPS, classBreaksRenderer.isShowExclusionClass());
            rendererObject.addProperty(ClassBreakRendererKeys.SORT_CLASSES_ASCENDING, classBreaksRenderer.isSortClassesAscending());

            // sizeRendererRandomRange
            double [] sizeRendererRandomRangeMin = new double[1];
            double [] sizeRendererRandomRangeMax = new double[1];

            classBreaksRenderer.getSizeRendererRandomRange(sizeRendererRandomRangeMin, sizeRendererRandomRangeMax);
            rendererObject.addProperty(ClassBreakRendererKeys.SIZE_RENDERER_RANDOM_RANGE_MIN, sizeRendererRandomRangeMin[0]);
            rendererObject.addProperty(ClassBreakRendererKeys.SIZE_RENDERER_RANDOM_RANGE_MAX, sizeRendererRandomRangeMax[0]);

            // symbolRotation3DExpressions
            String [] symbolRotation3DExpressionsX = new String[1];
            String [] symbolRotation3DExpressionsY = new String[1];
            String [] symbolRotation3DExpressionsZ = new String[1];

            classBreaksRenderer.getSymbolRotation3DExpressions(symbolRotation3DExpressionsX, symbolRotation3DExpressionsY, symbolRotation3DExpressionsZ);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_X, symbolRotation3DExpressionsX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_Y, symbolRotation3DExpressionsY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DEXPRESSION_Z, symbolRotation3DExpressionsZ[0]);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DFLAGS, classBreaksRenderer.getSymbolRotation3DFlags());

            // symbolRotation3DFlags2
            int [] symbolRotation3DFlags2X = new int[1];
            int [] symbolRotation3DFlags2Y = new int[1];
            int [] symbolRotation3DFlags2Z = new int[1];

            classBreaksRenderer.getSymbolRotation3DFlags2(symbolRotation3DFlags2X, symbolRotation3DFlags2Y, symbolRotation3DFlags2Z);

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

            classBreaksRenderer.getSymbolRotation3DRandomRanges(symbolRotation3DRandomRangesMinRotationX, symbolRotation3DRandomRangesMinRotationY, symbolRotation3DRandomRangesMinRotationZ,
                    symbolRotation3DRandomRangesMaxRotationX, symbolRotation3DRandomRangesMaxRotationY, symbolRotation3DRandomRangesMaxRotationZ);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Z, symbolRotation3DRandomRangesMinRotationX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Y, symbolRotation3DRandomRangesMinRotationY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Z, symbolRotation3DRandomRangesMinRotationZ[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_X, symbolRotation3DRandomRangesMaxRotationX[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Y, symbolRotation3DRandomRangesMaxRotationY[0]);
            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Z, symbolRotation3DRandomRangesMaxRotationZ[0]);

            rendererObject.addProperty(CommonRendererKeys.SYMBOL_ROTATION3DROTATIONTYPE_Z, classBreaksRenderer.getSymbolRotation3DRotationTypeZ());

            rendererObject.addProperty(ClassBreakRendererKeys.WEIGHT, classBreaksRenderer.getWeight());

            rendererObject.add(ClassBreakRendererKeys.BACKGROUND_SYMBOL, ParseLayer.createSymbol((ISymbol) classBreaksRenderer.getBackgroundSymbol()));
            rendererObject.add(ClassBreakRendererKeys.EXCLUSION_SYMBOL, ParseLayer.createSymbol((ISymbol) classBreaksRenderer.getExclusionSymbol()));
            rendererObject.add(ClassBreakRendererKeys.NUMBER_FORMAT, ParseLayer.createNumberFormat(classBreaksRenderer.getNumberFormat()));

            // Fields
            for(int fieldIndex = 0; fieldIndex < classBreaksRenderer.getFieldCount(); fieldIndex ++)
            {
                JsonObject jsonValueObject = new JsonObject();
                jsonValueObject.addProperty(ClassBreakRendererKeys.FIELD_NAME, classBreaksRenderer.getField(fieldIndex));

                fieldArray.add(jsonValueObject);
            }

            // Breaks
            for(index = 0; index < classBreaksRenderer.getBreakCount(); index ++)
            {
                JsonObject jsonValueObject = new JsonObject();

                try
                {
                    double breakValue = classBreaksRenderer.getBreak(index);
                    jsonValueObject.addProperty(ClassBreakRendererKeys.BREAK, breakValue);
                }
                catch(AutomationException e)
                {

                }

                try
                {
                    jsonValueObject.addProperty(ClassBreakRendererKeys.LOW_BREAK, classBreaksRenderer.getLowBreak(index));
                }
                catch(AutomationException e)
                {

                }

                try
                {
                    jsonValueObject.addProperty(CommonRendererKeys.LABEL, classBreaksRenderer.getLabel(index));
                }
                catch(AutomationException e)
                {

                }

                try
                {
                    jsonValueObject.addProperty(CommonRendererKeys.DESCRIPTION, classBreaksRenderer.getDescription(index));
                }
                catch(AutomationException e)
                {

                }

                try
                {
                    ITable table = null;
                    jsonValueObject.addProperty(ClassBreakRendererKeys.WHERE_CLAUSE, classBreaksRenderer.getWhereClause(index, table));
                }
                catch(AutomationException e)
                {

                }

                jsonValueObject.add(ClassBreakRendererKeys.SYMBOL, ParseLayer.createSymbol(classBreaksRenderer.getSymbol(index)));

                valueArray.add(jsonValueObject);
            }

            rendererObject.add(ClassBreakRendererKeys.VALUES, valueArray);
            rendererObject.add(ClassBreakRendererKeys.FIELDS, fieldArray);

            jsonObject.add(ClassBreakRendererKeys.CLASS_BREAKS_RENDERER, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            logger.error(String.format("ClassBreaksRender index : %d", index));
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
        return ClassBreaksRenderer.class;
    }
}
