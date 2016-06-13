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
package com.sldeditor.test.output;

import org.junit.Test;

import com.sldeditor.test.SLDTestRunner;

/**
 * The Class CheckPointValues runs the tests for setting values in point slds.
 * 
 * @author Robert Ward (SCISYS)
 */
public class CheckPointValues
{
    @Test
    public void point_outputTestCommon()
    {
        SLDTestRunner.runTest("output", "point_outputTestCommon.xml");
    }

    @Test
    public void point_outputTestExternalGraphic()
    {
        SLDTestRunner.runTest("output", "point_outputTestExternalGraphic.xml");
    }

    @Test
    public void point_outputTestTTF()
    {
        SLDTestRunner.runTest("output", "point_outputTestTTF.xml");
    }

    @Test
    public void point_outputTestWKT()
    {
        SLDTestRunner.runTest("output", "point_outputTestWKT.xml");
    }

    @Test
    public void point_outputTestWindBarbs()
    {
        SLDTestRunner.runTest("output", "point_outputTestWindbarbs.xml");
    }
}
