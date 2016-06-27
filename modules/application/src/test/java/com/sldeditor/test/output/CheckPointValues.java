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

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sldeditor.test.SLDTestRunner;

/**
 * The Class CheckPointValues runs the tests for setting values in point slds.
 * 
 * @author Robert Ward (SCISYS)
 */
public class CheckPointValues
{
    /** The test. */
    private static SLDTestRunner test = null;

    @BeforeClass
	public static void setUpOnce() throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				test = new SLDTestRunner();
			}
		});
	}
    
    @Test
    public void point_outputTestCommon()
    {
        test.runTest("output", "point_outputTestCommon.xml");
    }

    @Test
    public void point_outputTestExternalGraphic()
    {
        test.runTest("output", "point_outputTestExternalGraphic.xml");
    }

    @Test
    public void point_outputTestTTF()
    {
        test.runTest("output", "point_outputTestTTF.xml");
    }

    @Test
    public void point_outputTestWKT()
    {
        test.runTest("output", "point_outputTestWKT.xml");
    }

    @Test
    public void point_outputTestWindBarbs()
    {
        test.runTest("output", "point_outputTestWindbarbs.xml");
    }
}
