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
package com.sldeditor.exportdata.esri.keys;

/**
 * The Class DatasourceKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD data source that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatasourceKeys {

    public static final String PROPERTIES = "properties";
    public static final String PATH = "path";
    public static final String TYPE = "type";

    public static final String FIELD_NAME = "field";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_DECIMAL_PLACES = "dp";
    public static final String FIELD_SIG_FIGS = "sf";
}
