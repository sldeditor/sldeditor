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

package com.sldeditor.tool.scale;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDUtils;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Class given an SLD file will convert it to a list of ScaleSLDData objects describing the scales
 * at which rules are displayed.
 */
public class ScalePanelUtils {

    /**
     * Contains scales.
     *
     * @param sldData the sld data
     * @return the scale sld data
     */
    public static List<ScaleSLDData> containsScales(SLDDataInterface sldData) {

        List<ScaleSLDData> dataList = null;

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
                                    double minScale = rule.getMinScaleDenominator();
                                    double maxScale = rule.getMaxScaleDenominator();

                                    if ((minScale > 0.0) || (maxScale > 0.0)) {
                                        if (dataList == null) {
                                            dataList = new ArrayList<ScaleSLDData>();
                                        }
                                        ScaleSLDData scaleSLDData = new ScaleSLDData(sld, sldData);

                                        scaleSLDData.setNamedLayer(namedLayerImpl.getName());
                                        scaleSLDData.setFeatureTypeStyle(fts.getName());
                                        scaleSLDData.setStyle(style.getName());
                                        scaleSLDData.setRule(rule);
                                        if (minScale > 0.0) {
                                            scaleSLDData.setMinScale(minScale);
                                        }

                                        if (maxScale > 0.0) {
                                            scaleSLDData.setMaxScale(maxScale);
                                        }
                                        dataList.add(scaleSLDData);
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
