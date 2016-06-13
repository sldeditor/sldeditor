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

import com.esri.arcgis.carto.ChartRenderer;
import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.ChartRendererKeys;

/**
 * Class that converts an EsriChartRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "ChartRenderer": {
 *        "baseSymbol": getBaseSymbol(),
 *        "chartSymbol": getChartSymbol(),
 *        "exclusionClause": "getExclusionClause()",
 *        "exclusionDescription": "getExclusionDescription()",
 *        "exclusionLabel": "getExclusionLabel()",
 *        "exclusionSymbol": getExclusionSymbol(),
 *        "fields": [
 *          {
 *            "name" : "getField(fieldIndex)",
 *            "alias" : "getFieldAlias(fieldIndex)",
 *            "total" : getFieldTotal(fieldIndex)
 *          },
 *          ...
 *        ],
 *        "label": "getLabel()",
 *        "minSize": getMinSize(),
 *        "minValue": getMinValue(),
 *        "normalisationField": "getNormalizationField()",
 *        "normalisationFieldAlias": "getNormalizationFieldAlias()",
 *        "normalisationTotal": "getNormalizationTotal()",
 *        "normalisationType": "getNormalizationType()",
 *        "proportionalField": "getProportionalField()",
 *        "proportionalFieldAlias": "getProportionalFieldAlias()",
 *        "weight": getWeight(),
 *        "flanneryCompensation": isFlanneryCompensation(),
 *        "proportionalBySum": isProportionalBySum(),
 *        "graduatedSymbols": isSymbolsAreGraduated(),
 *        "showExclusionClass": isShowExclusionClass(),
 *        "useOverposter": isUseOverposter()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriChartRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(ChartRendererKeys.class);

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#convert(com.esri.arcgis.carto.IFeatureRenderer)
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {

        logger.info(ChartRendererKeys.CHART_RENDERER);

        ChartRenderer chartRenderer = (ChartRenderer) renderer;
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            rendererObject.add(ChartRendererKeys.BASE_SYMBOL, ParseLayer.createSymbol(chartRenderer.getBaseSymbol()));
            rendererObject.add(ChartRendererKeys.CHART_SYMBOL, ParseLayer.createSymbol((ISymbol) chartRenderer.getChartSymbol()));

            rendererObject.addProperty(ChartRendererKeys.EXCLUSION_CLAUSE, chartRenderer.getExclusionClause());
            rendererObject.addProperty(ChartRendererKeys.EXCLUSION_DESCRIPTION, chartRenderer.getExclusionDescription());
            rendererObject.addProperty(ChartRendererKeys.EXCLUSION_LABEL, chartRenderer.getExclusionLabel());
            rendererObject.add(ChartRendererKeys.EXCLUSION_SYMBOL, ParseLayer.createSymbol(chartRenderer.getExclusionSymbol()));

            JsonArray fieldArray = new JsonArray();

            // Fields
            for(int fieldIndex = 0; fieldIndex < chartRenderer.getFieldCount(); fieldIndex ++)
            {
                JsonObject jsonValueObject = new JsonObject();
                jsonValueObject.addProperty(ChartRendererKeys.NAME, chartRenderer.getField(fieldIndex));
                jsonValueObject.addProperty(ChartRendererKeys.ALIAS, chartRenderer.getFieldAlias(fieldIndex));
                jsonValueObject.addProperty(ChartRendererKeys.TOTAL, chartRenderer.getFieldTotal(fieldIndex));

                fieldArray.add(jsonValueObject);
            }

            rendererObject.add(ChartRendererKeys.FIELDS, fieldArray);

            rendererObject.addProperty(ChartRendererKeys.LABEL, chartRenderer.getLabel());
            rendererObject.addProperty(ChartRendererKeys.MIN_SIZE, chartRenderer.getMinSize());
            rendererObject.addProperty(ChartRendererKeys.MIN_VALUE, chartRenderer.getMinValue());

            rendererObject.addProperty(ChartRendererKeys.NORMALISATION_FIELD, chartRenderer.getNormalizationField());
            rendererObject.addProperty(ChartRendererKeys.NORMALISATION_FIELD_ALIAS, chartRenderer.getNormalizationFieldAlias());
            rendererObject.addProperty(ChartRendererKeys.NORMALISATION_FIELD_TOTAL, chartRenderer.getNormalizationTotal());
            rendererObject.addProperty(ChartRendererKeys.NORMALISATION_TYPE, chartRenderer.getNormalizationType());
            rendererObject.addProperty(ChartRendererKeys.PROPORTIONAL_FIELD, chartRenderer.getProportionalField());
            rendererObject.addProperty(ChartRendererKeys.PROPORTIONAL_FIELD_ALIAS, chartRenderer.getProportionalFieldAlias());
            rendererObject.addProperty(ChartRendererKeys.WEIGHT, chartRenderer.getWeight());

            rendererObject.addProperty(ChartRendererKeys.FLANNERY_COMPENSATION, chartRenderer.isFlanneryCompensation());
            rendererObject.addProperty(ChartRendererKeys.PROPORTIONAL_BY_SUM, chartRenderer.isProportionalBySum());
            rendererObject.addProperty(ChartRendererKeys.GRADUATED_SYMBOLS, chartRenderer.isSymbolsAreGraduated());
            rendererObject.addProperty(ChartRendererKeys.SHOW_EXCLUSION_CLASS, chartRenderer.isShowExclusionClass());
            rendererObject.addProperty(ChartRendererKeys.USE_OVERPOSTER, chartRenderer.isUseOverposter());

            jsonObject.add(ChartRendererKeys.CHART_RENDERER, rendererObject);

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
        return ChartRenderer.class;
    }
}
