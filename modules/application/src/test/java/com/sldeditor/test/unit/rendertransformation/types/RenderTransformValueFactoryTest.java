/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.rendertransformation.types;

import static org.junit.jupiter.api.Assertions.*;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterManager;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;
import com.sldeditor.rendertransformation.types.RenderTransformValueFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The unit test for RenderTransformValueFactory.
 * 
 * Ensures that all filter parameters can be displayed in the ui.
 *
 * @author Robert Ward (SCISYS)
 */
class RenderTransformValueFactoryTest {

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.RenderTransformValueFactory#getValue(java.lang.Class)}.
     */
    @Test
    void testGetValue() {
        List<FilterConfigInterface> filterList = FilterManager.getInstance().getFilterConfigList();

        for (FilterConfigInterface filter : filterList) {
            FilterName filterName = filter.getFilterConfiguration();
            System.out.println(filterName.getFilterName());

            for (FilterNameParameter param : filterName.getParameterList()) {
                System.out.println(
                        String.format("  %s / %s", param.getName(), param.getDataType()));
                assertNotNull(
                        RenderTransformValueFactory.getInstance().getValue(param.getDataType()));
            }
            System.out.println("  Return type : " + filterName.getReturnType());
            assertNotNull(
                    RenderTransformValueFactory.getInstance().getValue(filterName.getReturnType()));
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.RenderTransformValueFactory#getEnum(java.lang.Class,
     * java.util.List)}.
     */
    @Test
    void testGetEnum() {}

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.RenderTransformValueFactory#getValueCustomProcess(com.sldeditor.rendertransformation.ProcessFunctionParameterValue,
     * net.opengis.wps10.LiteralInputType)}.
     */
    @Test
    void testGetValueCustomProcess() {}
}
