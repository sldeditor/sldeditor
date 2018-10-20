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

package com.sldeditor.filter;

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import java.util.List;

/**
 * The Class RenderTransformationExpressionPanelv2.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformationExpressionPanelv2 extends ExpressionPanelv2
        implements ExpressionPanelInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new render transformation expression panel v2.
     *
     * @param vendorOptionList the vendor option list
     * @param inTestMode the in test mode
     */
    public RenderTransformationExpressionPanelv2(
            List<VersionData> vendorOptionList, boolean inTestMode) {
        super(vendorOptionList, inTestMode);
    }

    /**
     * Creates the expression node.
     *
     * @return the expression node
     */
    @Override
    protected ExpressionNode createExpressionNode() {
        ExpressionNode node = new ExpressionNode();

        node.setExpressionType(ExpressionTypeEnum.LITERAL);

        return node;
    }
}
