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

import com.esri.arcgis.carto.DotDensityRenderer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.carto.IGeoFeatureLayer;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.geodatabase.IDataset;
import com.esri.arcgis.geodatabase.IDatasetProxy;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.importdata.esri.keys.renderer.DotDensityRendererKeys;

/**
 * Class that converts an EsriDotDensityRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "DotDensityRenderer": {
 *        "controlLayerName": "getControlLayer().getName()",
 *        "controlLayerBrowseName": "getControlLayer().getDisplayFeatureClass().getBrowseName()",
 *        "exclusionClause": "getExclusionClause()",
 *        "exclusionDescription": "getExclusionDescription()",
 *        "exclusionLabel": "getExclusionLabel()",
 *        "exclusionSymbol": getExclusionSymbol(),
 *        "fields": [
 *          {
 *            "name" : "getField(fieldIndex)",
 *            "alias" : "getFieldAlias(fieldIndex)",
 *          },
 *          ...
 *        ],
 *        "maintainDensityBy": getMaintainDensityBy(),
 *        "maxDensity": getMaxDensity(),
 *        "maxDensityArea": getMaxDensityArea(),
 *        "meanArea": getMeanArea()
 *        "meanDensity": getMeanDensity(),
 *        "minArea": getMinDensity(),
 *        "minDensityArea": getMinDensityArea(),
 *        "graduatedSymbols": isSymbolsAreGraduated(),
 *        "showExclusionClass": isShowExclusionClass(),
 *        "dotDensitySymbol": getDotDensitySymbol()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriDotDensityRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(DotDensityRendererKeys.class);

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#convert(com.esri.arcgis.carto.IFeatureRenderer)
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {

        logger.info(DotDensityRendererKeys.DOT_DENSITY_RENDERER);

        DotDensityRenderer dotDensityRenderer = (DotDensityRenderer) renderer;
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            IFeatureLayer controlLayer = dotDensityRenderer.getControlLayer();

            if(controlLayer != null)
            {
                rendererObject.addProperty(DotDensityRendererKeys.CONTROL_LAYER_NAME, controlLayer.getName());

                IGeoFeatureLayer geoFeatureLayer = (IGeoFeatureLayer) controlLayer;

                IDataset dataSet = new IDatasetProxy(geoFeatureLayer.getDisplayFeatureClass());

                rendererObject.addProperty(DotDensityRendererKeys.CONTROL_LAYER_BROWSE_NAME, dataSet.getBrowseName());
            }

            rendererObject.addProperty(DotDensityRendererKeys.DOT_VALUE, dotDensityRenderer.getDotValue());

            rendererObject.addProperty(DotDensityRendererKeys.EXCLUSION_CLAUSE, dotDensityRenderer.getExclusionClause());
            rendererObject.addProperty(DotDensityRendererKeys.EXCLUSION_DESCRIPTION, dotDensityRenderer.getExclusionDescription());
            rendererObject.addProperty(DotDensityRendererKeys.EXCLUSION_LABEL, dotDensityRenderer.getExclusionLabel());
            rendererObject.add(DotDensityRendererKeys.EXCLUSION_SYMBOL, ParseLayer.createSymbol(dotDensityRenderer.getExclusionSymbol()));

            JsonArray fieldArray = new JsonArray();

            // Fields
            for(int fieldIndex = 0; fieldIndex < dotDensityRenderer.getFieldCount(); fieldIndex ++)
            {
                JsonObject jsonValueObject = new JsonObject();
                jsonValueObject.addProperty(DotDensityRendererKeys.FIELD_NAME, dotDensityRenderer.getField(fieldIndex));
                jsonValueObject.addProperty(DotDensityRendererKeys.FIELD_ALIAS, dotDensityRenderer.getFieldAlias(fieldIndex));

                fieldArray.add(jsonValueObject);
            }

            rendererObject.add(DotDensityRendererKeys.FIELDS, fieldArray);

            rendererObject.addProperty(DotDensityRendererKeys.MAINTAIN_DENSITY_BY, dotDensityRenderer.getMaintainDensityBy());
            rendererObject.addProperty(DotDensityRendererKeys.MAX_DENSITY, dotDensityRenderer.getMaxDensity());
            rendererObject.addProperty(DotDensityRendererKeys.MAX_DENSITY_AREA, dotDensityRenderer.getMaxDensityArea());

            rendererObject.addProperty(DotDensityRendererKeys.MEAN_AREA, dotDensityRenderer.getMeanArea());
            rendererObject.addProperty(DotDensityRendererKeys.MEAN_DENSITY, dotDensityRenderer.getMeanDensity());

            rendererObject.addProperty(DotDensityRendererKeys.MIN_AREA, dotDensityRenderer.getMinDensity());
            rendererObject.addProperty(DotDensityRendererKeys.MIN_DENSITY_AREA, dotDensityRenderer.getMinDensityArea());

            rendererObject.addProperty(DotDensityRendererKeys.MAINTAIN_SIZE, dotDensityRenderer.isMaintainSize());
            rendererObject.addProperty(CommonRendererKeys.GRADUATED_SYMBOLS, dotDensityRenderer.isSymbolsAreGraduated());
            rendererObject.addProperty(DotDensityRendererKeys.SHOW_EXCLUSION_CLASS, dotDensityRenderer.isShowExclusionClass());

            rendererObject.add(DotDensityRendererKeys.DOT_DENSITY_SYMBOL, ParseLayer.createSymbol((ISymbol) dotDensityRenderer.getDotDensitySymbol()));

            jsonObject.add(DotDensityRendererKeys.DOT_DENSITY_RENDERER, rendererObject);

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
        return DotDensityRenderer.class;
    }
}
