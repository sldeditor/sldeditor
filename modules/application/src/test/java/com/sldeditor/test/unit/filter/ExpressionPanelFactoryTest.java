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

package com.sldeditor.test.unit.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sldeditor.filter.ExpressionPanelFactory;

/**
 * Unit test for ExpressionPanelFactory class.
 * <p>{@link com.sldeditor.filter.ExpressionPanelFactory}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExpressionPanelFactoryTest {

    /**
     * Test method for {@link com.sldeditor.filter.ExpressionPanelFactory#getFilterPanel(java.lang.String)}.
     */
    @Test
    public void testGetFilterPanel() {
        assertNotNull(ExpressionPanelFactory.getFilterPanel(null));
    }

    /**
     * Test method for {@link com.sldeditor.filter.ExpressionPanelFactory#getExpressionPanel(java.lang.String)}.
     */
    @Test
    public void testGetExpressionPanel() {
        assertNotNull(ExpressionPanelFactory.getExpressionPanel(null));
    }

    /**
     * Test method for {@link com.sldeditor.filter.ExpressionPanelFactory#useAntiAliasUpdated(boolean)}.
     */
    @Test
    public void testUseAntiAliasUpdated() {
        // Do nothing
    }

    /**
     * Test method for {@link com.sldeditor.filter.ExpressionPanelFactory#vendorOptionsUpdated(java.util.List)}.
     */
    @Test
    public void testVendorOptionsUpdated() {
        // Do nothing
    }

    /**
     * Test method for {@link com.sldeditor.filter.ExpressionPanelFactory#backgroundColourUpdate(java.awt.Color)}.
     */
    @Test
    public void testBackgroundColourUpdate() {
        // Do nothing
    }

}
