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

package com.sldeditor.common.data;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class SelectedSymbolMask handles the updating of SLD data.
 *
 * @author Robert Ward (SCISYS)
 */
public class SelectedSymbolMask {

    /** The Enum SymbolMaskEnum representing the different attributes of an SLD symbol. */
    public enum SymbolMaskEnum {

        /** Styled layer. */
        E_STYLED_LAYER,

        /** Style. */
        E_STYLE,

        /** Feature type style. */
        E_FEATURE_TYPE_STYLE,

        /** Rule. */
        E_RULE,

        /** Symbolizer. */
        E_SYMBOLIZER
    }

    /** The mask order list. */
    private static List<SymbolMaskEnum> maskOrderlist = new ArrayList<>();

    /** The mask list. */
    private List<SymbolMaskEnum> maskList = new ArrayList<>();

    /**
     * Instantiates a new selected symbol mask.
     *
     * @param level the level
     */
    public SelectedSymbolMask(SymbolMaskEnum level) {
        if (maskOrderlist.isEmpty()) {
            maskOrderlist.add(SymbolMaskEnum.E_STYLED_LAYER);
            maskOrderlist.add(SymbolMaskEnum.E_STYLE);
            maskOrderlist.add(SymbolMaskEnum.E_FEATURE_TYPE_STYLE);
            maskOrderlist.add(SymbolMaskEnum.E_RULE);
            maskOrderlist.add(SymbolMaskEnum.E_SYMBOLIZER);
        }

        for (SymbolMaskEnum key : maskOrderlist) {
            maskList.add(key);
            if (key == level) {
                break;
            }
        }
    }

    /**
     * Should continue.
     *
     * @param level the level
     * @return true, if successful
     */
    public boolean shouldContinue(SymbolMaskEnum level) {
        return (maskList.contains(level));
    }
}
