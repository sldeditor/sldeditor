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

package com.sldeditor.test.unit.filter.v2.expression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.expression.ExpressionFilterInterface;
import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.expression.ExpressionSubPanel;
import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * Unit test for ExpressionSubPanel
 *
 * <p>Test method for {@link com.sldeditor.filter.v2.expression.ExpressionSubPanel}.
 *
 * @author Robert Ward (SCISYS)
 */
class ExpressionSubPanelTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestExpressionSubPanel extends ExpressionSubPanel {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test expression sub panel.
         *
         * @param parent the parent
         */
        public TestExpressionSubPanel(ExpressionFilterInterface parent) {
            super(parent);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionSubPanel#removeParameter()
         */
        @Override
        protected void removeParameter() {
            super.removeParameter();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionSubPanel#applyButton()
         */
        @Override
        protected void applyButton() {
            super.applyButton();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionSubPanel#revertButton()
         */
        @Override
        protected void revertButton() {
            super.revertButton();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionSubPanel#isRemoveButtonEnabled()
         */
        @Override
        protected boolean isRemoveButtonEnabled() {
            return super.isRemoveButtonEnabled();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionSubPanel#isRemoveButtonVisible()
         */
        @Override
        protected boolean isRemoveButtonVisible() {
            return super.isRemoveButtonVisible();
        }
    }

    class TestExpressionFilterInterface implements ExpressionFilterInterface {

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionFilterInterface#dataApplied()
         */
        @Override
        public void dataApplied() {}

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.expression.ExpressionFilterInterface#getVendorOptionList()
         */
        @Override
        public List<VersionData> getVendorOptionList() {
            List<VersionData> list = new ArrayList<VersionData>();
            list.add(VersionData.decode(GeoServerVendorOption.class, "2.0.5"));
            return list;
        }
    }

    /**
     * Testing literal expression
     *
     * <p>Test method for {@link
     * com.sldeditor.filter.v2.expression.ExpressionSubPanel#ExpressionSubPanel(com.sldeditor.filter.v2.expression.ExpressionFilterInterface)}.
     */
    @Test
    void testExpressionSubPanelLiteral() {
        TestExpressionFilterInterface expressionFilter = new TestExpressionFilterInterface();

        TestExpressionSubPanel subPanel = new TestExpressionSubPanel(expressionFilter);

        subPanel.setDataType(String.class, false);

        ExpressionNode propertyNode = new ExpressionNode();
        propertyNode.setExpressionType(ExpressionTypeEnum.PROPERTY);
        propertyNode.setName("property");
        propertyNode.setType(Object.class);
        propertyNode.setExpression(ff.property("test"));

        subPanel.setSelectedNode(propertyNode);
        assertFalse(subPanel.isRemoveButtonVisible());
        subPanel.applyButton();
    }

    /**
     * Testing property expression
     *
     * <p>Test method for {@link
     * com.sldeditor.filter.v2.expression.ExpressionSubPanel#ExpressionSubPanel(com.sldeditor.filter.v2.expression.ExpressionFilterInterface)}.
     */
    @Test
    void testExpressionSubPanelProperty() {
        TestExpressionFilterInterface expressionFilter = new TestExpressionFilterInterface();

        TestExpressionSubPanel subPanel = new TestExpressionSubPanel(expressionFilter);

        subPanel.setDataType(StringBuilder.class, false);

        ExpressionNode literalNode = new ExpressionNode();
        literalNode.setExpressionType(ExpressionTypeEnum.LITERAL);
        literalNode.setName("literal");
        literalNode.setType(Number.class);
        literalNode.setExpression(ff.literal(42));

        subPanel.setSelectedNode(literalNode);
        assertFalse(subPanel.isRemoveButtonVisible());
        subPanel.applyButton();
    }

    /**
     * Testing environment variable expression
     *
     * <p>Test method for {@link
     * com.sldeditor.filter.v2.expression.ExpressionSubPanel#ExpressionSubPanel(com.sldeditor.filter.v2.expression.ExpressionFilterInterface)}.
     */
    @Test
    void testExpressionSubPanelEnvVar() {
        TestExpressionFilterInterface expressionFilter = new TestExpressionFilterInterface();

        TestExpressionSubPanel subPanel = new TestExpressionSubPanel(expressionFilter);

        subPanel.setDataType(StringBuilder.class, false);

        ExpressionNode envVarNode = new ExpressionNode();
        envVarNode.setExpressionType(ExpressionTypeEnum.ENVVAR);
        envVarNode.setName("env var");
        envVarNode.setType(Integer.class);
        envVarNode.setExpression(ff.function("env", ff.literal("wms_width")));

        subPanel.setSelectedNode(envVarNode);
        assertFalse(subPanel.isRemoveButtonVisible());
        subPanel.applyButton();
    }

    /**
     * Testing function expression
     *
     * <p>Test method for {@link
     * com.sldeditor.filter.v2.expression.ExpressionSubPanel#ExpressionSubPanel(com.sldeditor.filter.v2.expression.ExpressionFilterInterface)}.
     */
    @Test
    void testExpressionSubPanelExpression() {
        TestExpressionFilterInterface expressionFilter = new TestExpressionFilterInterface();

        TestExpressionSubPanel subPanel = new TestExpressionSubPanel(expressionFilter);

        subPanel.setDataType(StringBuilder.class, false);

        ExpressionNode expressionNode = new ExpressionNode();
        expressionNode.setExpressionType(ExpressionTypeEnum.EXPRESSION);
        expressionNode.setName("expression");
        expressionNode.setType(Integer.class);
        expressionNode.setExpression(ff.function("Collection_Average", ff.property("geom")));
        subPanel.setSelectedNode(null);
        subPanel.setSelectedNode(expressionNode);
        assertFalse(subPanel.isRemoveButtonVisible());

        expressionNode.setType(String.class);
        expressionNode.setExpression(ff.function("Concatenate", ff.literal("a"), ff.literal("b")));
        subPanel.setSelectedNode(expressionNode);
        assertFalse(subPanel.isRemoveButtonVisible());
        subPanel.applyButton();

        // Parent / child
        ExpressionNode parentNode = new ExpressionNode();
        parentNode.setExpressionType(ExpressionTypeEnum.EXPRESSION);
        parentNode.setName("parent");
        parentNode.setType(Number.class);
        parentNode.setExpression(ff.function("Collection_Average", ff.literal(42)));

        ExpressionNode childNode = new ExpressionNode();
        childNode.setExpressionType(ExpressionTypeEnum.LITERAL);
        childNode.setName("child");
        childNode.setType(Integer.class);
        childNode.setExpression(ff.literal(42));

        parentNode.insert(childNode, 0);

        subPanel.setSelectedNode(childNode);
        assertFalse(subPanel.isRemoveButtonVisible());
        subPanel.applyButton();

        // Parent / child  Concatenate function
        parentNode = new ExpressionNode();
        parentNode.setExpressionType(ExpressionTypeEnum.EXPRESSION);
        parentNode.setName("parent");
        parentNode.setType(String.class);
        parentNode.setExpression(ff.function("Concatenate", ff.literal("abc")));

        childNode = new ExpressionNode();
        childNode.setExpressionType(ExpressionTypeEnum.LITERAL);
        childNode.setName("child");
        childNode.setType(String.class);
        childNode.setExpression(ff.literal("abc"));

        parentNode.insert(childNode, 0);

        subPanel.setSelectedNode(childNode);
        assertTrue(subPanel.isRemoveButtonVisible());
        assertFalse(subPanel.isRemoveButtonEnabled());

        subPanel.removeParameter();
        subPanel.applyButton();

        // Parent / child  FilterFunction_abs function
        parentNode = new ExpressionNode();
        parentNode.setExpressionType(ExpressionTypeEnum.EXPRESSION);
        parentNode.setName("parent");
        parentNode.setType(double.class);
        parentNode.setExpression(ff.function("abs", ff.literal(-1.0)));

        childNode = new ExpressionNode();
        childNode.setExpressionType(ExpressionTypeEnum.LITERAL);
        childNode.setName("child");
        childNode.setType(double.class);
        childNode.setExpression(ff.literal(-1.0));

        parentNode.insert(childNode, 0);

        subPanel.setSelectedNode(childNode);
        assertFalse(subPanel.isRemoveButtonVisible());

        subPanel.removeParameter();
        subPanel.applyButton();
    }
}
