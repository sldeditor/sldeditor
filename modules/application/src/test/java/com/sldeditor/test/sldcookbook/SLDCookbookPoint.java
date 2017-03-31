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

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sldeditor.test.SLDTestRunner;

/**
 * The Class SLDCookbookPoint runs the tests for point slds in the SLD Cookbook.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDCookbookPoint {
    /** The test. */
    private static SLDTestRunner test = null;

    /**
     * Sets the up once.
     *
     * @throws InvocationTargetException the invocation target exception
     * @throws InterruptedException the interrupted exception
     */
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
    public void point_simplepoint() {
        test.runTest("point", "point_simplepoint.xml");
    }

    @Test
    public void point_simplepointwithstroke() {
        test.runTest("point", "point_simplepointwithstroke.xml");
    }

    @Test
    public void point_transparenttriangle() {
        test.runTest("point", "point_transparenttriangle.xml");
    }

    @Test
    public void point_pointwithrotatedlabel() {
        test.runTest("point", "point_pointwithrotatedlabel.xml");
    }

    @Test
    public void point_pointwithstyledlabel() {
        test.runTest("point", "point_pointwithstyledlabel.xml");
    }

    @Test
    public void point_pointasgraphic() {
        test.runTest("point", "point_pointasgraphic.xml");
    }

    @Test
    public void point_pointasgraphichttp() {
        test.runTest("point", "point_pointasgraphichttp.xml");
    }

    @Test
    public void point_rotatedsquare() {
        test.runTest("point", "point_rotatedsquare.xml");
    }

    @Test
    public void point_attribute() {
        test.runTest("point", "point_attribute.xml");
    }

    @Test
    public void point_zoom() {
        test.runTest("point", "point_zoom.xml");
    }

    @Test
    public void point_wkt() {
        test.runTest("point", "point_wkt.xml");
    }

    @Test
    public void point_pointwithenhancedlabel() {
        test.runTest("point", "point_pointwithenhancedlabel.xml");
    }
}
