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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.styling.Graphic;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.common.console.ConsoleManager;

/**
 * Class that manages the conversion of all Esri symbols.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SymbolManager {

    /** The symbol map. */
    private Map<String, EsriSymbolInterface> symbolMap = new HashMap<String, EsriSymbolInterface>();

    /** The line symbol map. */
    private Map<String, EsriLineSymbolInterface> lineSymbolMap = new HashMap<String, EsriLineSymbolInterface>();

    /** The fill symbol map. */
    private Map<String, EsriFillSymbolInterface> fillSymbolMap = new HashMap<String, EsriFillSymbolInterface>();

    /** The fill symbol map. */
    private Map<String, EsriMarkSymbolInterface> markSymbolMap = new HashMap<String, EsriMarkSymbolInterface>();

    /** The text symbol map. */
    private Map<String, EsriTextSymbolInterface> textSymbolMap = new HashMap<String, EsriTextSymbolInterface>();

    /** The singleton instance. */
    private static SymbolManager instance = null;

    /** The logger. */
    private static Logger logger = Logger.getLogger(SymbolManager.class);

    /**
     * Gets the single instance of SymbolManager.
     *
     * @return single instance of SymbolManager
     */
    public static SymbolManager getInstance()
    {
        if(instance == null)
        {
            instance = new SymbolManager();
        }

        return instance;
    }

    /**
     * Instantiates a new symbol manager.
     */
    private SymbolManager()
    {
        initialise();
    }

    /**
     * Convert symbols.
     *
     * @param rule the rule
     * @param layerName the layer name
     * @param transparency the transparency
     * @param element the element
     */
    public void convertSymbols(Rule rule, String layerName, int transparency, JsonElement element) {

        if (element == null) return;

        JsonObject jsonSymbol = element.getAsJsonObject();
        boolean found = false;
        for(String symbolType : symbolMap.keySet())
        {
            JsonElement obj = jsonSymbol.get(symbolType);

            if(obj != null)
            {
                EsriSymbolInterface esriSymbol = symbolMap.get(symbolType);
                esriSymbol.convert(rule, obj, layerName, transparency);
                found = true;
                break;
            }
        }

        if(!found)
        {
            ConsoleManager.getInstance().error(this, "Unsupported symbol : " + SymbolUtils.extractName(element));
        }
    }

    /**
     * Gets the stroke.
     *
     * @param jsonElementSymbol the json element symbol
     * @return the stroke list
     */
    public List<Stroke> getStrokeList(JsonElement jsonElementSymbol) {
        List<Stroke> strokeList = null;

        if(jsonElementSymbol != null)
        {
            JsonObject jsonSymbol = jsonElementSymbol.getAsJsonObject();

            for(String lineSymbolType : lineSymbolMap.keySet())
            {
                JsonElement obj = jsonSymbol.get(lineSymbolType);

                if(obj != null)
                {
                    EsriLineSymbolInterface esriLineSymbol = lineSymbolMap.get(lineSymbolType);
                    strokeList = esriLineSymbol.convert(obj);
                    break;
                }
            }
        }
        return strokeList;
    }

    /**
     * Gets the line symbol.
     *
     * @param obj the obj
     * @return the line symbol
     */
    public EsriLineSymbolInterface getLineSymbol(JsonObject obj) {

        for(String lineSymbolType : lineSymbolMap.keySet())
        {
            JsonElement element = obj.get(lineSymbolType);

            if(element != null)
            {
                return lineSymbolMap.get(lineSymbolType);
            }
        }
        return null;
    }

    /**
     * Gets the fill.
     *
     * @param layerName the layer name
     * @param element the element
     * @param transparency the transparency
     * @return the fill
     */
    public List<Symbolizer> getFillSymbol(String layerName, JsonElement element, int transparency) {

        if (element == null) return null;

        JsonObject fillObj = element.getAsJsonObject();
        List<Symbolizer> symbolizerList = null;

        for(String fillSymbolType : fillSymbolMap.keySet())
        {
            JsonElement obj = fillObj.get(fillSymbolType);

            if(obj != null)
            {
                EsriFillSymbolInterface esriFillSymbol = fillSymbolMap.get(fillSymbolType);
                symbolizerList = esriFillSymbol.convertToFill(layerName, obj, transparency);
                break;
            }
        }

        return symbolizerList;
    }

    /**
     * Gets the marker list.
     *
     * @param jsonSymbol the json symbol
     * @return the marker list
     */
    public List<Graphic> getMarkerList(JsonObject jsonSymbol) {
        List<Graphic> markList = null;

        for(String markSymbolType : markSymbolMap.keySet())
        {
            JsonElement obj = jsonSymbol.get(markSymbolType);

            if(obj != null)
            {
                EsriMarkSymbolInterface esriMarkSymbol = markSymbolMap.get(markSymbolType);
                markList = esriMarkSymbol.convert(obj);
                break;
            }
        }

        return markList;
    }

    public void convertTextSymbols(TextSymbolizer textSymbolizer,
        int transparency, JsonElement jsonElement)
    {
        if (jsonElement == null) return;

        JsonObject jsonSymbol = jsonElement.getAsJsonObject();
        boolean found = false;
        for(String symbolType : textSymbolMap.keySet())
        {
            JsonElement obj = jsonSymbol.get(symbolType);

            if(obj != null)
            {
                EsriTextSymbolInterface esriTextSymbol = textSymbolMap.get(symbolType);
                esriTextSymbol.convert(textSymbolizer, obj, transparency);
                found = true;
                break;
            }
        }

        if(!found)
        {
            ConsoleManager.getInstance().error(this, "Unsupported text symbol : " + SymbolUtils.extractName(jsonElement));
        }
    }
    
    /**
     * Initialise.
     */
    private void initialise()
    {
        registerSymbols();
        
        for(EsriSymbolInterface symbol : symbolMap.values())
        {
            // Line symbols
            if(symbol instanceof EsriLineSymbolInterface)
            {
                lineSymbolMap.put(symbol.getName(), (EsriLineSymbolInterface) symbol);
            }
            
            // Fill symbols
            if(symbol instanceof EsriFillSymbolInterface)
            {
                fillSymbolMap.put(symbol.getName(), (EsriFillSymbolInterface) symbol);
            }

            // Marker symbols
            if(symbol instanceof EsriMarkSymbolInterface)
            {
                markSymbolMap.put(symbol.getName(), (EsriMarkSymbolInterface) symbol);
            }

            // Text symbols
            if(symbol instanceof EsriTextSymbolInterface)
            {
                textSymbolMap.put(symbol.getName(), (EsriTextSymbolInterface) symbol);
            }
        }
    }

    /**
     * Register symbol converters.
     */
    private void registerSymbols() {

        logger.debug("Symbols supported:");

        addSymbol(new CharacterMarkerSymbol());
        addSymbol(new CartographicLineSymbol());
        addSymbol(new TextSymbol());
        addSymbol(new SimpleMarkerSymbol());
        addSymbol(new SimpleLineSymbol());
        addSymbol(new LineFillSymbol());
        addSymbol(new MultiLayerFillSymbol());
        addSymbol(new MultiLayerLineSymbol());
        addSymbol(new MultiLayerMarkerSymbol());
        addSymbol(new SimpleFillSymbol());
        addSymbol(new PictureFillSymbol());
    }

    /**
     * Adds the symbol.
     *
     * @param symbolObj the symbol obj
     */
    private void addSymbol(EsriSymbolInterface symbolObj)
    {
        logger.debug("\t" + symbolObj.getName());
        symbolMap.put(symbolObj.getName(), symbolObj);
    }
}
