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
package com.sldeditor.filter.v2.expression;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.BinaryComparisonAbstract;
import org.geotools.filter.LogicFilterImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterManager;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * The Class FilterNode.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterNode extends DefaultMutableTreeNode {

    /** The filter. */
    private Filter filter = null;

    /**  The filter configuration. */
    private FilterConfigInterface filterConfig = null;

    /** The type. */
    private Class<?> type = String.class;

    /** The name. */
    private String name = null;

    /** The display string. */
    private String displayString = "";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );

    /**
     * Instantiates a new expression node.
     */
    public FilterNode()
    {
        setDisplayString();
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return displayString;
    }

    /**
     * Sets the display string.
     */
    private void setDisplayString()
    {
        StringBuilder sb = new StringBuilder();

        if(name != null)
        {
            sb.append(name);
            sb.append(" : ");
        }

        if(filter == null)
        {
            sb.append(Localisation.getString(ExpressionPanelv2.class, "FilterNode.filterNotSet"));
        }
        else
        {
            sb.append(Localisation.getField(ExpressionPanelv2.class, "FilterNode.filter") + " ");
            if(filterConfig == null)
            {
                sb.append(Localisation.getString(ExpressionPanelv2.class, "FilterNode.filterNotSet"));
            }
            else
            {
                sb.append(filterConfig.getFilterConfiguration().getFilterName());
            }
        }

        displayString = sb.toString();
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Sets the filter.
     *
     * @param filter the new expression
     * @param filterConfig the filter config
     */
    public void setFilter(Filter filter, FilterConfigInterface filterConfig) {
        this.filter = filter;
        this.filterConfig = filterConfig;

        setDisplayString();

        this.removeAllChildren();

        TypeManager.getInstance().reset();

        if(filterConfig != null)
        {
            FilterName filterName = filterConfig.getFilterConfiguration();

            if(filter instanceof LogicFilterImpl)
            {
                List<Filter> childFilterList = ((LogicFilterImpl)filter).getChildren();

                if(childFilterList.isEmpty())
                {
                    // No child filter so add a minimum 2 to work with.
                    setFilterParameter(null, filterName.getParameter(0));
                    setFilterParameter(null, filterName.getParameter(0));
                }
                else
                {
                    for(Filter childFilter : childFilterList)
                    {
                        setFilterParameter(childFilter, filterName.getParameter(0));
                    }
                }
            }
            else if(filter instanceof BinaryTemporalOperator)
            {
                setExpressionParameter(((BinaryTemporalOperator) filter).getExpression1(), filterName.getParameter(0));
                setExpressionParameter(((BinaryTemporalOperator) filter).getExpression2(), filterName.getParameter(1));
            }
            else if(filter instanceof PropertyIsNull)
            {
                setExpressionParameter(((PropertyIsNull) filter).getExpression(), filterName.getParameter(0));
            }
            else if(filter instanceof PropertyIsBetween)
            {
                setExpressionParameter(((PropertyIsBetween) filter).getLowerBoundary(), filterName.getParameter(0));
                setExpressionParameter(((PropertyIsBetween) filter).getExpression(), filterName.getParameter(1));
                setExpressionParameter(((PropertyIsBetween) filter).getUpperBoundary(), filterName.getParameter(2));
            }
            else if(filter instanceof PropertyIsLike)
            {
                setExpressionParameter(((PropertyIsLike) filter).getExpression(), filterName.getParameter(0));
                setExpressionParameter(ff.literal(((PropertyIsLike) filter).getLiteral()), filterName.getParameter(1));
                setExpressionParameter(ff.literal(((PropertyIsLike) filter).getWildCard()), filterName.getParameter(2));
                setExpressionParameter(ff.literal(((PropertyIsLike) filter).getSingleChar()), filterName.getParameter(3));
                setExpressionParameter(ff.literal(((PropertyIsLike) filter).getEscape()), filterName.getParameter(4));
                setExpressionParameter(ff.literal(((PropertyIsLike) filter).isMatchingCase()), filterName.getParameter(5));
            }
            else if(filter instanceof BinarySpatialOperator)
            {
                setExpressionParameter(((BinaryComparisonAbstract) filter).getExpression1(), filterName.getParameter(0));
                setExpressionParameter(((BinaryComparisonAbstract) filter).getExpression2(), filterName.getParameter(1));
            }
            else if(filter instanceof BinaryComparisonAbstract)
            {
                setExpressionParameter(((BinaryComparisonAbstract) filter).getExpression1(), filterName.getParameter(0));
                setExpressionParameter(((BinaryComparisonAbstract) filter).getExpression2(), filterName.getParameter(1));

                if(filterName.getParameterList().size() > 2) // Gets round the problem with PropertyIsGreaterThan which has no matchCase parameter
                {
                    setExpressionParameter(ff.literal(((BinaryComparisonAbstract) filter).isMatchingCase()), filterName.getParameter(2));
                }
            }
        }
    }

    /**
     * Sets the filter.
     *
     * @param childFilter the new filter
     * @param parameter the parameter
     */
    private void setFilterParameter(Filter childFilter, FilterNameParameter parameter) {
        FilterNode childNode = new FilterNode();
        childNode.setType(parameter.getDataType());
        FilterConfigInterface filterConfig = FilterManager.getInstance().getFilterConfig(childFilter);

        childNode.setFilter(childFilter, filterConfig);
        this.insert(childNode, this.getChildCount());
    }

    /**
     * Sets the expression parameter.
     *
     * @param expression the expression
     * @param parameter the parameter
     */
    private void setExpressionParameter(Expression expression, FilterNameParameter parameter) {

        ExpressionNode childNode = new ExpressionNode();
        childNode.setType(parameter.getDataType());
        childNode.setName(parameter.getName());
        childNode.setExpression(expression);
        childNode.setExpressionType(parameter.getExpressionType());

        this.insert(childNode, this.getChildCount());
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;

        setDisplayString();
    }

    /**
     * Gets the function name.
     *
     * @return the filterName
     */
    public FilterConfigInterface getFilterConfig() {
        return filterConfig;
    }

    /**
     * Adds the filter.
     */
    public void addFilter() {
        if(filterConfig != null)
        {
            FilterName filterName = filterConfig.getFilterConfiguration();
            setFilterParameter(null, filterName.getParameter(0));
        }
    }
}
