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
package com.sldeditor.importdata.esri.keys.renderer;

/**
 * The Class UniqueValueRendererKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD unique value renderer that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class UniqueValueRendererKeys {

    public static final String RENDERER_UNIQUEVALUE = "UniqueValueRenderer";

    public static final String USE_DEFAULTSYMBOL = "usesDefaultSymbol";
    public static final String DEFAULTLABEL = "defaultLabel";
    public static final String DEFAULTSYMBOL = "defaultSymbol";
    public static final String FIELD_DELIMETER = "fieldDelimeter";
    public static final String FLIPSYMBOLS = "flipSymbols";
    public static final String REVERSE_UNIQUEVALUES_SORTING = "reverseUniqueValuesSorting";

    public static final String FIELDS = "fields";
    public static final String FIELD_NAME = "name";

    public static final String VALUES = "values";
    public static final String VALUES_VALUE = "value";
    public static final String VALUES_HEADING = "heading";
    public static final String VALUES_LABEL = "label";
    public static final String VALUES_DESCRIPTION = "description";
    public static final String VALUES_REFERENCEVALUE = "referenceValue";
    public static final String VALUES_SYMBOL = "symbol";
}
