/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.rendertransformation.types;

import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class BaseValue.
 *
 * @author Robert Ward (SCISYS)
 */
public abstract class BaseValue {

    /** The filter factory. */
    protected static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    /**
     * Creates the enum.
     *
     * @param commonData the common data
     * @return the field config base
     */
    protected FieldConfigBase createEnum(FieldConfigCommonData commonData) {
        FieldConfigEnum fieldConfigEnum = new FieldConfigEnum(commonData);

        List<SymbolTypeConfig> configList = new ArrayList<>();
        SymbolTypeConfig symbolTypeConfig = new SymbolTypeConfig(null);

        populateSymbolType(symbolTypeConfig);
        configList.add(symbolTypeConfig);
        fieldConfigEnum.addConfig(configList);

        return fieldConfigEnum;
    }

    /**
     * Populate symbol type.
     *
     * @param symbolTypeConfig the symbol type config
     */
    protected abstract void populateSymbolType(SymbolTypeConfig symbolTypeConfig);

    /** The expression. */
    protected Expression expression = null;
}
