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

package com.sldeditor.extension.filesystem.database;

import com.sldeditor.common.data.DatabaseConnection;
import java.util.List;

/**
 * The Interface GeoServerReadProgressInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseReadProgressInterface {

    /**
     * Start populating.
     *
     * @param connection the connection
     */
    void startPopulating(DatabaseConnection connection);

    /**
     * Read feature classes complete.
     *
     * @param connection the connection
     * @param featureClassList the feature class list
     */
    void readFeatureClassesComplete(DatabaseConnection connection, List<String> featureClassList);
}
