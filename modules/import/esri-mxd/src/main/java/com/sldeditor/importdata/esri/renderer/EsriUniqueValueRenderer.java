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
import com.esri.arcgis.carto.UniqueValueRenderer;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.importdata.esri.keys.renderer.UniqueValueRendererKeys;

/**
 * Class that converts an EsriUniqueValueRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "UniqueValueRenderer": {
 *        "fieldDelimeter": "getFieldDelimiter()",
 *        "rotationField": "getRotationField()",
 *        "rotationType": getRotationType(),
 *        "transparencyField": "getTransparencyField()",
 *        "flipSymbols": isFlipSymbols(),
 *        "reverseUniqueValuesSorting": isReverseUniqueValuesSorting(),
 *        "graduatedSymbols": isSymbolsAreGraduated(),
 *        "usesFilter": isUsesFilter(),
 *        "usesDefaultSymbol": isUseDefaultSymbol(),
 *        "defaultLabel": "getDefaultLabel()",
 *        "defaultSymbol": getDefaultSymbol(),
 *        "values": [
 *          {
 *            "value": "getValue(valueIndex)",
 *            "heading": "getHeading(valueIndex)",
 *            "label": "getLabel(valueIndex)",
 *            "description": "getDescription(valueIndex)",
 *            "referenceValue": "getReferenceValue(valueIndex)",
 *            "symbol": getSymbol(valueIndex)
 *          },
 *          ...
 *         ],
 *        "fields": [
 *          {
 *             "name" : "getField(fieldIndex)"
 *          },
 *          ...
 *         ]
 *   }
 * }
 *</pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriUniqueValueRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(EsriUniqueValueRenderer.class);

    /**
     * Convert unique value renderer.
     *
     * @param renderer the renderer
     * @return the json object
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {
        UniqueValueRenderer uniqueValueRenderer = (UniqueValueRenderer) renderer;
        logger.info("UniqueValueRenderer");

        String value = null;
        String referenceValue = null;
        int index = -1;

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            JsonArray fieldArray = new JsonArray();

            JsonArray valueArray = new JsonArray();

            rendererObject.addProperty(UniqueValueRendererKeys.FIELD_DELIMETER, uniqueValueRenderer.getFieldDelimiter());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONFIELD, uniqueValueRenderer.getRotationField());
            rendererObject.addProperty(CommonRendererKeys.ROTATIONTYPE, uniqueValueRenderer.getRotationType());
            rendererObject.addProperty(CommonRendererKeys.TRANSPARENCY_FIELD, uniqueValueRenderer.getTransparencyField());
            rendererObject.addProperty(UniqueValueRendererKeys.FLIPSYMBOLS, uniqueValueRenderer.isFlipSymbols());
            rendererObject.addProperty(UniqueValueRendererKeys.REVERSE_UNIQUEVALUES_SORTING, uniqueValueRenderer.isReverseUniqueValuesSorting());
            rendererObject.addProperty(CommonRendererKeys.GRADUATED_SYMBOLS, uniqueValueRenderer.isSymbolsAreGraduated());
            rendererObject.addProperty(CommonRendererKeys.USES_FILTER, uniqueValueRenderer.isUsesFilter());
            rendererObject.addProperty(UniqueValueRendererKeys.USE_DEFAULTSYMBOL, uniqueValueRenderer.isUseDefaultSymbol());

            // Fields
            for(int fieldIndex = 0; fieldIndex < uniqueValueRenderer.getFieldCount(); fieldIndex ++)
            {
                JsonObject jsonValueObject = new JsonObject();
                jsonValueObject.addProperty(UniqueValueRendererKeys.FIELD_NAME, uniqueValueRenderer.getField(fieldIndex));

                fieldArray.add(jsonValueObject);
            }

            // Values
            for(index = 0; index < uniqueValueRenderer.getValueCount(); index ++)
            {
                JsonObject jsonValueObject = new JsonObject();

                try
                {
                    value = uniqueValueRenderer.getValue(index);
                    jsonValueObject.addProperty(UniqueValueRendererKeys.VALUES_VALUE, value);
                }
                catch(AutomationException e)
                {

                }
                try
                {
                    jsonValueObject.addProperty(UniqueValueRendererKeys.VALUES_HEADING, uniqueValueRenderer.getHeading(value));
                }
                catch(AutomationException e)
                {

                }
                try
                {
                    jsonValueObject.addProperty(UniqueValueRendererKeys.VALUES_LABEL, uniqueValueRenderer.getLabel(value));
                }
                catch(AutomationException e)
                {

                }
                try
                {
                    jsonValueObject.addProperty(UniqueValueRendererKeys.VALUES_DESCRIPTION, uniqueValueRenderer.getDescription(value));
                }
                catch(AutomationException e)
                {

                }
                try
                {
                    referenceValue = uniqueValueRenderer.getReferenceValue(value);
                    jsonValueObject.addProperty(UniqueValueRendererKeys.VALUES_REFERENCEVALUE, referenceValue);
                }
                catch(AutomationException e)
                {

                }

                jsonValueObject.add(UniqueValueRendererKeys.VALUES_SYMBOL, ParseLayer.createSymbol(uniqueValueRenderer.getSymbol(value)));

                valueArray.add(jsonValueObject);
            }

            // Add default value
            if(uniqueValueRenderer.isUseDefaultSymbol())
            {
                JsonObject jsonValueObject = new JsonObject();

                jsonValueObject.addProperty(UniqueValueRendererKeys.DEFAULTLABEL, uniqueValueRenderer.getDefaultLabel());
                jsonValueObject.add(UniqueValueRendererKeys.DEFAULTSYMBOL, ParseLayer.createSymbol(uniqueValueRenderer.getDefaultSymbol()));

                valueArray.add(jsonValueObject);
            }

            rendererObject.add(UniqueValueRendererKeys.VALUES, valueArray);
            rendererObject.add(UniqueValueRendererKeys.FIELDS, fieldArray);

            jsonObject.add(UniqueValueRendererKeys.RENDERER_UNIQUEVALUE, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            logger.error(String.format("%s %s %d", value, referenceValue, index));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return UniqueValueRenderer.class;
    }
}
