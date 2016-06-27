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
package com.sldeditor.exportdata.esri.keys.numberformat;

/**
 * The Class LatLonFormatKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD lat/lon format that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class LatLonFormatKeys {

    public static final String LAT_LON_FORMAT = "LatLonFormat";
    public static final String ALIGNMENT_OPTION = "alignmentOption";
    public static final String ALIGNMENT_WIDTH = "alignmentWidth";
    public static final String ROUNDING_OPTION = "roundingOption";
    public static final String ROUNDING_VALUE = "roundingValue";
    public static final String IS_LATITUDE = "isLatitude";
    public static final String SHOW_DIRECTIONS = "showDirections";
    public static final String SHOW_PLUS_SIGN = "showPlusSign";
    public static final String SHOW_ZERO_MINUTES = "showZeroMinutes";
    public static final String SHOW_ZERO_SECONDS = "showZeroSeconds";
    public static final String USE_SEPARATOR = "useSeparator";
    public static final String ZERO_PAD = "zeroPad";
}
