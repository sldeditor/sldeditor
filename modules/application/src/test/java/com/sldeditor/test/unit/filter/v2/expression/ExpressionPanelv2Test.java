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

package com.sldeditor.test.unit.filter.v2.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for ExpressionPanelv2 class.
 *
 * <p>{@link com.sldeditor.filter.v2.expression.ExpressionPanelv2}
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionPanelv2Test {

    class TestExpressionPanelv2 extends ExpressionPanelv2 {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test expression panelv 2.
         *
         * @param vendorOptionList the vendor option list
         */
        public TestExpressionPanelv2(List<VersionData> vendorOptionList) {
            super(vendorOptionList);
        }

        public void testShowExpressionDialog(Class<?> type, Expression expression) {
            showExpressionDialog(type, expression);
        }

        public void testSelection() {
            treeSelected(null);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.expression.ExpressionPanelv2#ExpressionPanelv2(java.util.List)}.
     */
    @Test
    public void testExpressionPanelv2() {

        TestExpressionPanelv2 testObj = new TestExpressionPanelv2(null);

        assertNull(testObj.getVendorOptionList());
        testObj.dataSourceAboutToUnloaded(null);
        testObj.populate((String) null);
        testObj.configure("title", String.class, false);

        testObj.testShowExpressionDialog(null, null);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Expression expectedExpression = ff.add(ff.property("field"), ff.literal(42));
        testObj.populate(expectedExpression);
        testObj.testShowExpressionDialog(Integer.class, expectedExpression);

        assertEquals(testObj.getExpression().toString(), testObj.getExpressionString());

        testObj.dataApplied();
        testObj.testSelection();
    }
}
