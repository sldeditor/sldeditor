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
 * The Class ProportionalRendererKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD proportional renderer that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class ProportionalRendererKeys {

    public static final String PROPORTIONAL_SYMBOL_RENDERER = "ProportionalSymbolRenderer";
    public static final String EXCLUSION_CLAUSE = "exclusionClause";
    public static final String EXCLUSION_DESCRIPTION = "exclusionDescription";
    public static final String EXCLUSION_LABEL = "exclusionLabel";
    public static final String FIELD = "field";
    public static final String MIN_SYMBOL = "minSymbol";
    public static final String NORMALISATION_FIELD = "normalisationField";
    public static final String NORMALISATION_FIELD_ALIAS = "normalisationFieldAlias";
    public static final String NORMALISATION_TOTAL = "normalisationTotal";
    public static final String NORMALISATION_TYPE = "normalisationType";
    public static final String VALUE_REPRESENTATION = "valueRepresentation";
    public static final String VALUE_UNIT = "valueUnit";
    public static final String WEIGHT = "weight";
    public static final String FLANNERY_COMPENSATION = "flanneryCompensation";
    public static final String SHOW_EXCLUSION_CLASS = "showExclusionClass";
    public static final String EXCLUSION_SYMBOL = "exclusionSymbol";
    public static final String BACKGROUND_SYMBOL = "backgroundSymbol";
}
