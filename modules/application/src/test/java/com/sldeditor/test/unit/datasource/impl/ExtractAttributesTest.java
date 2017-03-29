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

package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.impl.ExtractAttributes;

/**
 * Unit test for ExtractAttributes class.
 * 
 * <p>{@link com.sldeditor.datasource.impl.ExtractAttributes}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExtractAttributesTest {

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Test method for
     * {@link com.sldeditor.datasource.impl.ExtractAttributes#addDefaultFields(org.geotools.feature.simple.SimpleFeatureTypeBuilder, java.lang.String)}.
     */
    @Test
    public void testAddDefaultFields() {

        DummyInternalSLDFile dummy = new DummyInternalSLDFile();

        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(SLDUtils.createSLDFromString(dummy.getSLDData()));

        // Check fields extracted ok
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for (DataSourceAttributeData dataSourceField : actualFieldnameList) {
            if (expectedFieldList.contains(dataSourceField.getName())) {
                count++;
            }
        }
        assertTrue(expectedFieldList.size() == count);

        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(0, actualGeometryFields.size());
    }

    /**
     * Test sld symbol contains non-default geometry field.
     * 
     * {@link com.sldeditor.datasource.impl.ExtractAttributes#addDefaultFields(org.geotools.feature.simple.SimpleFeatureTypeBuilder, java.lang.String)}.
     */
    @Test
    public void testNonStandardGeometryField() {

        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(SLDUtils.createSLDFromString(dummy.getSLDData()));

        // Check fields extracted ok - should be none
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());
        assertEquals(0, actualFieldnameList.size());

        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(1, actualGeometryFields.size());
        String expectedGeometryFieldName = dummy.getExpectedGeometryFieldList().get(0);
        assertTrue(expectedGeometryFieldName.compareTo(actualGeometryFields.get(0)) == 0);
    }

    /**
     * Test sld symbol contains non-default geometry field and non-standard xml namespace.
     * 
     * {@link com.sldeditor.datasource.impl.ExtractAttributes#addDefaultFields(org.geotools.feature.simple.SimpleFeatureTypeBuilder, java.lang.String)}.
     */
    @Test
    public void testNonStandardGeometryNamespace() {

        DummyInternalSLDFile3 dummy = new DummyInternalSLDFile3();

        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(SLDUtils.createSLDFromString(dummy.getSLDData()));

        // Check fields extracted ok - should be none
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for (DataSourceAttributeData dataSourceField : actualFieldnameList) {
            if (expectedFieldList.contains(dataSourceField.getName())) {
                count++;
            }
        }
        assertTrue(expectedFieldList.size() == count);

        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(1, actualGeometryFields.size());
        String expectedGeometryFieldName = dummy.getExpectedGeometryFieldList().get(0);
        assertTrue(expectedGeometryFieldName.compareTo(actualGeometryFields.get(0)) == 0);
    }

    /**
     * Test filter.
     */
    @Test
    public void testFilter() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);
        ExtractAttributes extract = new ExtractAttributes();
        Rule rule = DefaultSymbols.createNewRule();

        // Try it 1) property 2) Literal
        Filter filter = ff.greater(ff.property("width"), ff.literal(42.1));
        rule.setFilter(filter);
        ruleList.add(rule);
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(1, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(Double.class, dataSourceField.getType());
        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(1, actualGeometryFields.size());

        // Try it 1) literal 2) property
        filter = ff.greater(ff.literal(42.1), ff.property("dble"));
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        actualFieldnameList = extract.getFields();
        assertEquals(2, actualFieldnameList.size());
        dataSourceField = actualFieldnameList.get(0);
        assertEquals(Double.class, dataSourceField.getType());
    }

    @Test
    public void testNotFilter() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        ExtractAttributes extract = new ExtractAttributes();
        Rule rule = DefaultSymbols.createNewRule();
        // Try with NOT
        extract = new ExtractAttributes();
        Filter filter = ff.not(ff.greater(ff.literal(42.1), ff.property("dble")));
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(1, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(Double.class, dataSourceField.getType());
    }

    @Test
    public void testMultiComparatorFilter() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        Rule rule = DefaultSymbols.createNewRule();

        // Try with something complex
        Filter filter = ff.and(ff.greater(ff.literal(42), ff.property("int")),
                ff.less(ff.literal(12), ff.property("abc")));
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(2, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(Integer.class, dataSourceField.getType());
    }

    @Test
    public void testBinaryTemporalFilter() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        Rule rule = DefaultSymbols.createNewRule();

        // Try begins
        Filter filter = ff.begins(ff.property("test"), ff.literal("1234"));
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(1, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(Integer.class, dataSourceField.getType());
    }

    @Test
    public void testIsNull() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        Rule rule = DefaultSymbols.createNewRule();

        // Try isNull
        Filter filter = ff.isNull(ff.property("test"));
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(1, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(String.class, dataSourceField.getType());
    }

    @Test
    public void testIsLike() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        Rule rule = DefaultSymbols.createNewRule();

        // Try isLike
        Filter filter = ff.like(ff.property("test"), "abcd1");
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(1, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(String.class, dataSourceField.getType());
    }

    @Test
    public void testIsBetween() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        Rule rule = DefaultSymbols.createNewRule();

        // Try isBetween
        Filter filter = ff.between(ff.property("test"), ff.literal("1.23"), ff.literal(4));
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(1, actualFieldnameList.size());
        DataSourceAttributeData dataSourceField = actualFieldnameList.get(0);
        assertEquals(Double.class, dataSourceField.getType());
    }

    @Test
    public void testBinarySpatialOperator() {
        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        StyledLayerDescriptor sld = createTestSLD(dummy);
        List<Rule> ruleList = getRuleList(sld);

        Rule rule = DefaultSymbols.createNewRule();

        // Try bbox
        String expectedGeometryFieldName = "test geometry";
        Filter filter = ff.bbox(expectedGeometryFieldName, -1.0, 49.0, 2.0, 55.0, "EPSG:4326");
        rule.setFilter(filter);
        ruleList.clear();
        ruleList.add(rule);
        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(sld);

        // Check fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(1, actualGeometryFields.size());
        assertEquals(expectedGeometryFieldName, actualGeometryFields.get(0));
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertEquals(0, actualFieldnameList.size());
    }

    /**
     * Gets the rule list.
     *
     * @param sld the sld
     * @return the rule list
     */
    protected List<Rule> getRuleList(StyledLayerDescriptor sld) {
        NamedLayer namedLayer = (NamedLayer) sld.layers().get(0);
        List<Rule> ruleList = namedLayer.styles().get(0).featureTypeStyles().get(0).rules();
        return ruleList;
    }

    /**
     * Creates the test SLD.
     *
     * @param dummy the dummy
     * @return the styled layer descriptor
     */
    protected StyledLayerDescriptor createTestSLD(DummyInternalSLDFile2 dummy) {

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(dummy.getSLDData());
        return sld;
    }
}
