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

package com.sldeditor.tool.layerstyle;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import java.util.List;
import java.util.Map;

/**
 * The Interface GeoServerLayerUpdateInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface GeoServerLayerUpdateInterface {

    /**
     * Gets the style map.
     *
     * @param connection the connection
     * @return the style map
     */
    Map<String, List<StyleWrapper>> getStyleMap(GeoServerConnection connection);

    /**
     * Update style for layers.
     *
     * @param layerList the list of layers with updated styles
     */
    void updateLayerStyle(List<GeoServerLayer> layerList);
}
