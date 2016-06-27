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
 * The Class CommonRendererKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD common renderers that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class CommonRendererKeys {

    // Common renderer keys
    public static final String DESCRIPTION = "description";
    public static final String LABEL = "label";
    public static final String ROTATIONFIELD = "rotationField";
    public static final String ROTATIONTYPE = "rotationType";
    public static final String SIZE_EXPRESSION = "sizeExpression";
    public static final String SIZE_FLAGS = "sizeFlags";
    public static final String TRANSPARENCY_FIELD = "transparencyField";
    public static final String GRADUATED_SYMBOLS = "graduatedSymbols";
    public static final String USES_FILTER = "usesFilter";
    public static final String SYMBOL = "symbol";
    
    public static final String SYMBOL_ROTATION3DFLAGS = "symbolRotation3DFlags";
    public static final String SYMBOL_ROTATION3DEXPRESSION_X = "symbolRotation3DExpressionsX";
    public static final String SYMBOL_ROTATION3DEXPRESSION_Y = "symbolRotation3DExpressionsY";
    public static final String SYMBOL_ROTATION3DEXPRESSION_Z = "symbolRotation3DExpressionsZ";
    public static final String SYMBOL_ROTATION3DFLAGS2_X = "symbolRotation3DFlags2X";
    public static final String SYMBOL_ROTATION3DFLAGS2_Y = "symbolRotation3DFlags2Y";
    public static final String SYMBOL_ROTATION3DFLAGS2_Z = "symbolRotation3DFlags2Z";

    public static final String SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_X = "symbolRotation3DRandomRangesMinRotationX";
    public static final String SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Y = "symbolRotation3DRandomRangesMinRotationY";
    public static final String SYMBOL_ROTATIONRANDOMRANGED_MIN_ROTATION_Z = "symbolRotation3DRandomRangesMinRotationZ";
    public static final String SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_X = "symbolRotation3DRandomRangesMaxRotationX";
    public static final String SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Y = "symbolRotation3DRandomRangesMaxRotationY";
    public static final String SYMBOL_ROTATIONRANDOMRANGED_MAX_ROTATION_Z = "symbolRotation3DRandomRangesMaxRotationZ";

    public static final String SYMBOL_ROTATION3DROTATIONTYPE_Z = "symbolRotation3DRotationTypeZ";
}
