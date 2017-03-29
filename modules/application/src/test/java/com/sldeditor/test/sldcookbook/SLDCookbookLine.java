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
 * The Class SLDCookbookLine runs the tests for line slds in the SLD Cookbook.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDCookbookLine {
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
    public void line_simpleline() {
        test.runTest("line", "line_simpleline.xml");
    }

    @Test
    public void line_attributebasedline() {
        test.runTest("line", "line_attributebasedline.xml");
    }

    @Test
    public void line_dashdot() {
        test.runTest("line", "line_dashdot.xml");
    }

    @Test
    public void line_dashedline() {
        test.runTest("line", "line_dashedline.xml");
    }

    @Test
    public void line_dashspace() {
        test.runTest("line", "line_dashspace.xml");
    }

    @Test
    public void line_labelfollowingline() {
        test.runTest("line", "line_labelfollowingline.xml");
    }

    @Test
    public void line_linewithborder() {
        test.runTest("line", "line_linewithborder.xml");
    }

    @Test
    public void line_linewithdefaultlabel() {
        test.runTest("line", "line_linewithdefaultlabel.xml");
    }

    @Test
    public void line_optimizedlabel() {
        test.runTest("line", "line_optimizedlabel.xml");
    }

    @Test
    public void line_optimizedstyledlabel() {
        test.runTest("line", "line_optimizedstyledlabel.xml");
    }

    @Test
    public void line_railroad() {
        test.runTest("line", "line_railroad.xml");
    }

    @Test
    public void line_zoombasedline() {
        test.runTest("line", "line_zoombasedline.xml");
    }

    @Test
    public void line_external() {
        test.runTest("line", "line_external.xml");
    }

    @Test
    public void line_wkt() {
        test.runTest("line", "line_wkt.xml");
    }
}
