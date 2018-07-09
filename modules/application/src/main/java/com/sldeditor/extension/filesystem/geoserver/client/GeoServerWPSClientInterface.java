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

package com.sldeditor.extension.filesystem.geoserver.client;

import com.sldeditor.common.DataTypeEnum;
import java.util.List;
import net.opengis.wps10.ProcessBriefType;

/**
 * The Interface GeoServerWPSClientInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface GeoServerWPSClientInterface {

    /**
     * Gets the capabilities.
     *
     * @return returns true if capabilities read, false if error
     */
    boolean getCapabilities();

    /**
     * Gets the render transformations.
     *
     * @param typeOfData the type of data
     * @return the render transformations
     */
    List<ProcessBriefType> getRenderTransformations(DataTypeEnum typeOfData);
}
