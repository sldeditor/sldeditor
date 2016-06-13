/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.symbol;

import com.esri.arcgis.display.ISymbol;
import com.google.gson.JsonObject;

/**
 * The Interface EsriSymbolInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface EsriSymbolInterface {

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    public Class<?> getSymbolClass();

    /**
     * Convert symbol.
     *
     * @param baseSymbol the base symbol
     * @return the json object
     */
    public JsonObject convert(ISymbol baseSymbol);

}
