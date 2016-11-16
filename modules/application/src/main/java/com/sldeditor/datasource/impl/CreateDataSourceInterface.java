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
package com.sldeditor.datasource.impl;

import java.util.List;

import com.sldeditor.datasource.SLDEditorFileInterface;

/**
 * The Interface CreateDataSourceInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface CreateDataSourceInterface {

    /**
     * Connect to the data source.
     *
     * @param geometryFieldName the geometry field name
     * @param editorFile the editor file
     * @return the list
     */
    List<DataSourceInfo> connect(String geometryFieldName, SLDEditorFileInterface editorFile);

}