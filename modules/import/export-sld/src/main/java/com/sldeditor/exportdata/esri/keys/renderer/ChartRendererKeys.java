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
 * The Class ChartRendererKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD chart renderer that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class ChartRendererKeys {

    public static final String CHART_RENDERER = "ChartRenderer";
    public static final String BASE_SYMBOL = "baseSymbol";
    public static final String CHART_SYMBOL = "chartSymbol";
    public static final String EXCLUSION_CLAUSE = "exclusionClause";
    public static final String EXCLUSION_DESCRIPTION = "exclusionDescription";
    public static final String EXCLUSION_LABEL = "exclusionLabel";
    public static final String EXCLUSION_SYMBOL = "exclusionSymbol";
    public static final String NAME = "name";
    public static final String ALIAS = "alias";
    public static final String TOTAL = "total";
    public static final String FIELDS = "fields";
    public static final String LABEL = "label";
    public static final String MIN_SIZE = "minSize";
    public static final String MIN_VALUE = "minValue";
    public static final String NORMALISATION_FIELD = "normalisationField";
    public static final String NORMALISATION_FIELD_ALIAS = "normalisationFieldAlias";
    public static final String NORMALISATION_FIELD_TOTAL = "normalisationFieldTotal";
    public static final String NORMALISATION_TYPE = "normalisationType";
    public static final String PROPORTIONAL_FIELD = "proportionalField";
    public static final String PROPORTIONAL_FIELD_ALIAS = "proportionalFieldAlias";
    public static final String WEIGHT = "weight";
    public static final String FLANNERY_COMPENSATION = "flanneryCompensation";
    public static final String PROPORTIONAL_BY_SUM = "proportionalBySum";
    public static final String GRADUATED_SYMBOLS = "graduatedSymbols";
    public static final String SHOW_EXCLUSION_CLASS = "showExclusionClass";
    public static final String USE_OVERPOSTER = "useOverposter";
}
