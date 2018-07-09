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

package com.sldeditor.tool.vector;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.DatabaseConnection;
import java.io.File;

/**
 * The Interface VectorReaderInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface VectorReaderInterface {

    /**
     * Creates the vector sld from a file.
     *
     * @param vectorFile the vector file
     * @return the styled layer descriptor
     */
    SLDDataInterface createVectorSLDData(File vectorFile);

    /**
     * Creates the vector SLD data from a database feature class.
     *
     * @param databaseConnection the database connection
     * @param featureClass the feature class
     * @return the SLD data interface
     */
    SLDDataInterface createVectorSLDData(
            DatabaseConnection databaseConnection, String featureClass);
}
