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
package com.sldeditor.exportdata.esri.label;

import java.util.List;

import org.geotools.styling.Rule;

import com.google.gson.JsonElement;

/**
 * The Interface EsriLabelRendererInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface EsriLabelRendererInterface {

    /**
     * Gets the renderer name.
     *
     * @return the name
     */
    abstract String getName();

    /**
     * Convert.
     *
     * @param ruleList the rule list
     * @param rule the rule
     * @param obj the obj
     * @param transparency the transparency
     */
    abstract void convert(List<Rule> ruleList, Rule rule, JsonElement obj,
            int transparency);
}
