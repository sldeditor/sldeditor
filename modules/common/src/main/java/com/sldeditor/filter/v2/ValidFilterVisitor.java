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
package com.sldeditor.filter.v2;

import java.text.DecimalFormatSymbols;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

/**
 * Class to ensure string literals are correctly quoted.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ValidFilterVisitor extends DuplicatingFilterVisitor {

    private static final String SINGLE_QUOTE = "'";

    /**
     * Default constructor
     */
    public ValidFilterVisitor()
    {
    }

    /**
     * Instantiates a new valid filter visitor.
     *
     * @param factory the factory
     */
    public ValidFilterVisitor(FilterFactory2 factory) {
        super(factory);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        updateExpression(filter.getExpression());
        updateExpression(filter.getLowerBoundary());
        updateExpression(filter.getUpperBoundary());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        updateExpression(filter.getExpression());

        String literal = filter.getLiteral();

        if(!(literal.startsWith(SINGLE_QUOTE) && literal.endsWith(SINGLE_QUOTE)))
        {
            PropertyIsLike newFilter = ff.like(filter.getExpression(), 
                    SINGLE_QUOTE + literal + SINGLE_QUOTE,
                    filter.getWildCard(),
                    filter.getSingleChar(), 
                    filter.getEscape(),
                    filter.isMatchingCase(),
                    filter.getMatchAction());
            return super.visit(newFilter, extraData);
        }

        return super.visit(filter, extraData);
    }

    /**
     * Visit.
     *
     * @param filter the filter
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(Equals filter, Object extraData) {
        updateExpression(filter.getExpression1());
        updateExpression(filter.getExpression2());

        return super.visit(filter, extraData);
    }

    /**
     * Update expression.
     *
     * @param expression the expression
     */
    private void updateExpression(Expression expression) {
        if(expression instanceof LiteralExpressionImpl)
        {
            LiteralExpressionImpl literal = (LiteralExpressionImpl) expression;

            Object value = literal.getValue();
            if(value instanceof String)
            {
                String stringValue = (String)value;

                if(!isStringNumeric(stringValue))
                {
                    if(!(stringValue.startsWith(SINGLE_QUOTE) && stringValue.endsWith(SINGLE_QUOTE)))
                    {
                        String newValue = SINGLE_QUOTE + stringValue + SINGLE_QUOTE;

                        literal.setValue(newValue);
                    }
                }
            }
        }
    }

    /**
     * Checks if is string numeric.
     *
     * @param str the str
     * @return true, if is string numeric
     */
    public static boolean isStringNumeric( String str )
    {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if ( !Character.isDigit( str.charAt( 0 ) ) && str.charAt( 0 ) != localeMinusSign ) return false;

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for ( char c : str.substring( 1 ).toCharArray() )
        {
            if ( !Character.isDigit( c ) )
            {
                if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
                {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Visit.
     *
     * @param after the after
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(After after, Object extraData) {
        updateExpression(after.getExpression1());
        updateExpression(after.getExpression2());

        return super.visit(after, extraData);
    }

    /**
     * Visit.
     *
     * @param anyInteracts the any interacts
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        updateExpression(anyInteracts.getExpression1());
        updateExpression(anyInteracts.getExpression2());

        return super.visit(anyInteracts, extraData);
    }

    /**
     * Visit.
     *
     * @param before the before
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(Before before, Object extraData) {
        updateExpression(before.getExpression1());
        updateExpression(before.getExpression2());

        return super.visit(before, extraData);
    }

    /**
     * Visit.
     *
     * @param begins the begins
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(Begins begins, Object extraData) {
        updateExpression(begins.getExpression1());
        updateExpression(begins.getExpression2());

        return super.visit(begins, extraData);
    }

    /**
     * Visit.
     *
     * @param begunBy the begun by
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        updateExpression(begunBy.getExpression1());
        updateExpression(begunBy.getExpression2());

        return super.visit(begunBy, extraData);
    }

    /**
     * Visit.
     *
     * @param during the during
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(During during, Object extraData) {
        updateExpression(during.getExpression1());
        updateExpression(during.getExpression2());

        return super.visit(during, extraData);
    }

    /**
     * Visit.
     *
     * @param endedBy the ended by
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        updateExpression(endedBy.getExpression1());
        updateExpression(endedBy.getExpression2());

        return super.visit(endedBy, extraData);
    }

    /**
     * Visit.
     *
     * @param ends the ends
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(Ends ends, Object extraData) {
        updateExpression(ends.getExpression1());
        updateExpression(ends.getExpression2());

        return super.visit(ends, extraData);
    }

    /**
     * Visit.
     *
     * @param meets the meets
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(Meets meets, Object extraData) {
        updateExpression(meets.getExpression1());
        updateExpression(meets.getExpression2());

        return super.visit(meets, extraData);
    }

    /**
     * Visit.
     *
     * @param metBy the met by
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(MetBy metBy, Object extraData) {
        updateExpression(metBy.getExpression1());
        updateExpression(metBy.getExpression2());

        return super.visit(metBy, extraData);
    }

    /**
     * Visit.
     *
     * @param overlappedBy the overlapped by
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        updateExpression(overlappedBy.getExpression1());
        updateExpression(overlappedBy.getExpression2());

        return super.visit(overlappedBy, extraData);
    }

    /**
     * Visit.
     *
     * @param contains the contains
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(TContains contains, Object extraData) {
        updateExpression(contains.getExpression1());
        updateExpression(contains.getExpression2());

        return super.visit(contains, extraData);
    }

    /**
     * Visit.
     *
     * @param equals the equals
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(TEquals equals, Object extraData) {
        updateExpression(equals.getExpression1());
        updateExpression(equals.getExpression2());

        return super.visit(equals, extraData);
    }

    /**
     * Visit.
     *
     * @param contains the contains
     * @param extraData the extra data
     * @return the object
     */
    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        updateExpression(contains.getExpression1());
        updateExpression(contains.getExpression2());

        return super.visit(contains, extraData);
    }

}
