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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.rendertransformation.types.FeatureCollectionValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for FeatureCollectionValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.FeatureCollectionValues}
 *
 * @author Robert Ward (SCISYS)
 */
class FeatureCollectionValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestFeatureCollectionValues extends FeatureCollectionValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.FeatureCollectionValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.FeatureCollectionValues#FeatureCollectionValues()}.
     */
    @Test
    void testFeatureCollectionValues() {
        FeatureCollectionValues testObj = new FeatureCollectionValues();
        testObj.createInstance();

        assertEquals(
                Arrays.asList(FeatureCollection.class, SimpleFeatureCollection.class),
                testObj.getType());

        @SuppressWarnings("rawtypes")
        FeatureCollection fc = new DefaultFeatureCollection();

        testObj.setDefaultValue(fc);
        assertNull(testObj.getExpression());

        // FeatureCollection value
        testObj.setValue(fc);
        assertNull(testObj.getExpression());

        // Literal expression
        Expression expectedExpression = ff.literal(fc);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression(), expectedExpression);

        // Attribute expression
        expectedExpression = ff.property("test");
        testObj.setValue(expectedExpression);
        assertEquals(expectedExpression, testObj.getExpression());

        // Not set
        testObj.setValue(null);
        assertNull(testObj.getExpression());

        FieldConfigBase field =
                testObj.getField(
                        new FieldConfigCommonData(
                                FeatureCollectionValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigGeometry.class, field.getClass());

        // Increase code coverage
        TestFeatureCollectionValues testObj2 = new TestFeatureCollectionValues();
        testObj2.populateSymbolType(null);
    }
}
