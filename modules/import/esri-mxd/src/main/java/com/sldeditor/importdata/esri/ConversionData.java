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
package com.sldeditor.importdata.esri;

import java.util.HashMap;

import com.esri.arcgis.carto.IAnnotateLayerProperties;
import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.ITextBackground;
import com.esri.arcgis.system.INumberFormat;
import com.sldeditor.importdata.esri.label.EsriLabelRendererInterface;
import com.sldeditor.importdata.esri.numberformat.EsriNumberFormatInterface;
import com.sldeditor.importdata.esri.renderer.EsriRendererInterface;
import com.sldeditor.importdata.esri.symbol.EsriSymbolInterface;
import com.sldeditor.importdata.esri.textbackground.EsriTextBackgroundInterface;

/**
 * The Class ConversionData.
 *
 * @author Robert Ward (SCISYS)
 */
public class ConversionData {

    /** The renderer map. */
    private java.util.Map<Class<?>, EsriRendererInterface> rendererMap = new HashMap<Class<?>, EsriRendererInterface>();

    /** The symbol map. */
    private java.util.Map<Class<?>, EsriSymbolInterface> symbolMap = new HashMap<Class<?>, EsriSymbolInterface>();

    /** The label map. */
    private java.util.Map<Class<?>, EsriLabelRendererInterface> labelMap = new HashMap<Class<?>, EsriLabelRendererInterface>();

    /** The text background map. */
    private java.util.Map<Class<?>, EsriTextBackgroundInterface> textBackgroundMap = new HashMap<Class<?>, EsriTextBackgroundInterface>();

    /** The number format map. */
    private java.util.Map<Class<?>, EsriNumberFormatInterface> numberFormatMap = new HashMap<Class<?>, EsriNumberFormatInterface>();

    /**
     * Gets the symbol.
     *
     * @param clazz the clazz
     * @return the symbol
     */
    public EsriSymbolInterface getSymbol(Class<? extends ISymbol> clazz) {
        return symbolMap.get(clazz);
    }

    /**
     * Gets the renderer.
     *
     * @param clazz the clazz
     * @return the renderer
     */
    public EsriRendererInterface getRenderer(Class<? extends IFeatureRenderer> clazz) {
        return rendererMap.get(clazz);
    }

    /**
     * Gets the number format.
     *
     * @param clazz the clazz
     * @return the number format
     */
    public EsriNumberFormatInterface getNumberFormat(Class<? extends INumberFormat> clazz) {
        return numberFormatMap.get(clazz);
    }

    /**
     * Gets the text background map.
     *
     * @param clazz the clazz
     * @return the text background map
     */
    public EsriTextBackgroundInterface getTextBackgroundMap(
            Class<? extends ITextBackground> clazz) {
        return textBackgroundMap.get(clazz);
    }

    /**
     * Gets the label.
     *
     * @param clazz the clazz
     * @return the label
     */
    public EsriLabelRendererInterface getLabel(Class<? extends IAnnotateLayerProperties> clazz) {
        return labelMap.get(clazz);
    }

    /**
     * Adds the renderer.
     *
     * @param rendererObj the renderer obj
     */
    public void addRenderer(EsriRendererInterface rendererObj) {
        rendererMap.put(rendererObj.getRendererClass(), rendererObj);
    }

    /**
     * Adds the label renderer.
     *
     * @param labelRendererObj the label renderer obj
     */
    public void addLabelRenderer(EsriLabelRendererInterface labelRendererObj) {
        labelMap.put(labelRendererObj.getRendererClass(), labelRendererObj);
    }

    /**
     * Adds the symbol.
     *
     * @param symbolObj the symbol obj
     */
    public void addSymbol(EsriSymbolInterface symbolObj) {
        symbolMap.put(symbolObj.getSymbolClass(), symbolObj);
    }

    /**
     * Adds the text background.
     *
     * @param textBackgroundObj the text background obj
     */
    public void addTextBackground(EsriTextBackgroundInterface textBackgroundObj) {
        textBackgroundMap.put(textBackgroundObj.getBackgroundClass(), textBackgroundObj);
    }

    /**
     * Adds the number format.
     *
     * @param numberFormatObj the number format obj
     */
    public void addNumberFormat(EsriNumberFormatInterface numberFormatObj) {
        numberFormatMap.put(numberFormatObj.getNumberFormatClass(), numberFormatObj);
    }

}
