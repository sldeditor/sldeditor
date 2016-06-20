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
package com.sldeditor.test.unit.importdata.esri.symbols;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;

/**
 * The Class SupportingUtils.
 *
 * @author Robert Ward (SCISYS)
 */
public class SupportingUtils {

    /**
     * Output the symbolizer list.
     *
     * @param symbolizerList the symbolizer list
     */
    public static void outputSymbolizer(List<Symbolizer> symbolizerList) {
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();

        DefaultSymbols.createCompleteSymbolizer(sld, symbolizerList);

        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

        String encodedSLD = sldWriter.encodeSLD(sld);

        System.out.println(encodedSLD);
    }

    /**
     * Output symbolizer.
     *
     * @param symbolizer the symbolizer
     */
    public static void outputSymbolizer(Symbolizer symbolizer) {
        List<Symbolizer> symbolizerList = new ArrayList<Symbolizer>();
        symbolizerList.add(symbolizer);

        outputSymbolizer(symbolizerList);
    }

    /**
     * Gets the expression value.
     *
     * @param expression the expression
     * @return the expression value
     */
    public static Object getExpressionValue(Expression expression)
    {
        return ((LiteralExpressionImpl)expression).getValue();
    }
}
