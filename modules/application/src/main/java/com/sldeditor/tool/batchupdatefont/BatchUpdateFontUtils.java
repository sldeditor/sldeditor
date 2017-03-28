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

package com.sldeditor.tool.batchupdatefont;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Font;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDUtils;

/**
 * Class given an SLD file will convert it to a list of ScaleSLDData objects describing the scales at which rules are displayed.
 */
public class BatchUpdateFontUtils {

    /**
     * Contains font details.
     *
     * @param sldData the sld data
     * @return the scale sld data
     */
    public static List<BatchUpdateFontData> containsFonts(SLDDataInterface sldData) {

        List<BatchUpdateFontData> dataList = null;
        if (sldData != null) {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            if (sld != null) {
                List<StyledLayer> styledLayerList = sld.layers();

                if (sld != null) {
                    for (StyledLayer styledLayer : styledLayerList) {
                        if (styledLayer instanceof NamedLayerImpl) {
                            NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;

                            for (Style style : namedLayerImpl.styles()) {
                                for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                                    for (Rule rule : fts.rules()) {
                                        for (Symbolizer symbolizer : rule.symbolizers()) {
                                            if (symbolizer instanceof TextSymbolizer) {
                                                TextSymbolizer textSymbol =
                                                        (TextSymbolizer) symbolizer;
                                                Font font = textSymbol.getFont();
                                                if (font != null) {
                                                    if (dataList == null) {
                                                        dataList =
                                                               new ArrayList<BatchUpdateFontData>();
                                                    }
                                                    BatchUpdateFontData fontSLDData =
                                                            new BatchUpdateFontData(
                                                            sld, sldData);

                                                    fontSLDData.setNamedLayer(
                                                            namedLayerImpl.getName());
                                                    fontSLDData.setFeatureTypeStyle(fts.getName());
                                                    fontSLDData.setStyle(style.getName());
                                                    fontSLDData.setRule(rule);
                                                    fontSLDData.setSymbolizer(textSymbol);
                                                    fontSLDData.setFont(font);
                                                    dataList.add(fontSLDData);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return dataList;
    }
}
