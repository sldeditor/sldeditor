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
package com.sldeditor.test.sldcookbook;

import org.junit.Test;

import com.sldeditor.test.SLDTestRunner;

/**
 * The Class SLDCookbookPoint runs the tests for point slds in the SLD Cookbook.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDCookbookPoint
{
    @Test
    public void point_simplepoint()
    {
        SLDTestRunner.runTest("point", "point_simplepoint.xml");
    }
    
    @Test
    public void point_simplepointwithstroke()
    {
        SLDTestRunner.runTest("point", "point_simplepointwithstroke.xml");
    }

    @Test
    public void point_transparenttriangle()
    {
        SLDTestRunner.runTest("point", "point_transparenttriangle.xml");
    }

    @Test
    public void point_pointwithrotatedlabel()
    {
        SLDTestRunner.runTest("point", "point_pointwithrotatedlabel.xml");
    }

    @Test
    public void point_pointwithstyledlabel()
    {
        SLDTestRunner.runTest("point", "point_pointwithstyledlabel.xml");
    }

    @Test
    public void point_pointasgraphic()
    {
        SLDTestRunner.runTest("point", "point_pointasgraphic.xml");
    }

    @Test
    public void point_rotatedsquare()
    {
        SLDTestRunner.runTest("point", "point_rotatedsquare.xml");
    }
    
    @Test
    public void point_attribute()
    {
        SLDTestRunner.runTest("point", "point_attribute.xml");
    }
    
    @Test
    public void point_zoom()
    {
        SLDTestRunner.runTest("point", "point_zoom.xml");
    }
}
