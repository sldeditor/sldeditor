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

package com.sldeditor.test.unit.filter.v2.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.string.ConcatenateFunction;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;

/**
 * Unit test for ExpressionNode class.
 * 
 * <p>{@link com.sldeditor.filter.v2.expression.ExpressionNode}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExpressionNodeTest {

    /**
     * Test method for {@link com.sldeditor.filter.v2.expression.ExpressionNode#ExpressionNode()}.
     * Test method for {@link com.sldeditor.filter.v2.expression.ExpressionNode#toString()}. Test
     * method for {@link com.sldeditor.filter.v2.expression.ExpressionNode#isValueOnly()}. Test
     * method for {@link com.sldeditor.filter.v2.expression.ExpressionNode#setValueOnly(boolean)}.
     */
    @Test
    public void testExpressionNode() {
        ExpressionNode node = new ExpressionNode();
        assertTrue(node.toString().compareTo(Localisation.getString(ExpressionPanelv2.class,
                "ExpressionPanelv2.expressionNotSet")) == 0);

        Class<?> type = String.class;
        node.setType(type);
        assertEquals(type, node.getType());

        boolean isValueOnly = true;
        node.setValueOnly(isValueOnly);
        assertEquals(isValueOnly, node.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.expression.ExpressionNode#getExpression()}.
     */
    @Test
    public void testGetExpression() {
    }

    /**
     * Test method for
     * {@link com.sldeditor.filter.v2.expression.ExpressionNode#setExpression(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testSetExpression() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        ExpressionNode node = new ExpressionNode();

        // Test literal expression
        String expressionString = "Literalexpression";
        Expression literal = ff.literal(expressionString);
        node.setExpression(literal);
        String expected = Localisation.getField(ExpressionPanelv2.class,
                "ExpressionPanelv2.literal") + " " + expressionString;
        String actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);

        // Test attribute expression
        String propertyName = "testproperty";
        Expression attribute = ff.property(propertyName);
        node.setExpression(attribute);
        expected = Localisation.getField(ExpressionPanelv2.class, "ExpressionPanelv2.attribute")
                + " [" + attribute + "]";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);

        // Test attribute expression
        literal = ff.literal(ff.property(propertyName));
        node.setExpression(literal);
        expected = Localisation.getField(ExpressionPanelv2.class, "ExpressionPanelv2.attribute")
                + " [" + attribute + "]";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);

        // Test math expression
        Expression maths = ff.multiply(ff.literal(6), ff.literal(7));
        node.setExpression(maths);
        expected = "*";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);

        // Test function
        FunctionImpl function = new ConcatenateFunction();
        List<Expression> params = new ArrayList<Expression>();
        params.add(ff.literal("world"));
        params.add(ff.literal("dog"));
        function.setParameters(params);
        node.setExpression(function);
        expected = "Concatenate([world], [dog])";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);

        // Test function expression
        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        String name = "strConcat";
        List<Expression> parameters = new ArrayList<Expression>();
        parameters.add(ff.literal("cat"));
        parameters.add(ff.literal("dog"));
        Function functionExpression = functionFactory.function(name, parameters, null);
        node.setExpression(functionExpression);
        expected = "strConcat([cat], [dog])";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);

        // Test environment function
        EnvFunction envExpression = (EnvFunction) ff.function("env", ff.literal("foo"),
                ff.literal(0));
        node.setExpression(envExpression);
        expected = "env([foo], [0])";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
    }

    /**
     * Test method for
     * {@link com.sldeditor.filter.v2.expression.ExpressionNode#setName(java.lang.String)}.
     */
    @Test
    public void testSetName() {
        String expectedName = "test";

        ExpressionNode node = new ExpressionNode();
        assertTrue(node.toString().compareTo(Localisation.getString(ExpressionPanelv2.class,
                "ExpressionPanelv2.expressionNotSet")) == 0);
        node.setName(expectedName);
        assertTrue(node.toString().compareTo(expectedName + " : " + Localisation
                .getString(ExpressionPanelv2.class, "ExpressionPanelv2.expressionNotSet")) == 0);
    }

    /**
     * Test method for
     * {@link com.sldeditor.filter.v2.expression.ExpressionNode#setExpressionType(com.sldeditor.filter.v2.expression.ExpressionTypeEnum)}.
     * Test method for
     * {@link com.sldeditor.filter.v2.expression.ExpressionNode#getExpressionType()}.
     */
    @Test
    public void testSetExpressionType() {
        ExpressionNode node = new ExpressionNode();
        assertTrue(node.toString().compareTo(Localisation.getString(ExpressionPanelv2.class,
                "ExpressionPanelv2.expressionNotSet")) == 0);
        node.setExpressionType(ExpressionTypeEnum.LITERAL);
        assertEquals(ExpressionTypeEnum.LITERAL, node.getExpressionType());
        assertTrue(node.toString().compareTo(Localisation.getString(ExpressionPanelv2.class,
                "ExpressionPanelv2.literalNotSet")) == 0);
        node.setExpressionType(ExpressionTypeEnum.PROPERTY);
        assertEquals(ExpressionTypeEnum.PROPERTY, node.getExpressionType());
        assertTrue(node.toString().compareTo(Localisation.getString(ExpressionPanelv2.class,
                "ExpressionPanelv2.propertyNotSet")) == 0);
        assertNull(node.getExpression());
    }

    /**
     * Test method for
     * {@link com.sldeditor.filter.v2.expression.ExpressionNode#setEnvMgr(com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface)}.
     */
    @Test
    public void testSetEnvMgr() {
    }

}
