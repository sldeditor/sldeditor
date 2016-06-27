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
package com.sldeditor.exportdata.esri.keys.renderer;

/**
 * The Class RepresentationRendererKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD representation renderer that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class RepresentationRendererKeys {

    public static final String REPRESENTATION_RENDERER = "RepresentationRenderer";
    public static final String DATA_SOURCE_NAME = "dataSourceName";
    public static final String INVALID_RULE_COLOUR = "invalidRuleColour";
    public static final String INVISIBLE_RULE_COLOUR = "invisibleRuleColour";
    public static final String RELATIVE_BASE = "relativeBase";
    public static final String REPRESENTATION_CLASS = "representationClass";
    public static final String FIELD_NAME = "name";
    public static final String WHERE_CLAUSE = "whereClause";
    public static final String DRAW_INVALID_RULE = "drawInvalidRule";
    public static final String DRAW_INVISIBLE = "drawInvisible";
    public static final String GRADUATED_SYMBOLS = "graduatedSymbols";
    public static final String FIELDS = "fields";
}
