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

import com.sldeditor.test.SLDTestRunner;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The Class CheckTextValues runs the tests for setting values in text slds.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckTextValues {
    /** The test. */
    private static SLDTestRunner test = null;

    /**
     * Sets the up once.
     *
     * @throws InvocationTargetException the invocation target exception
     * @throws InterruptedException the interrupted exception
     */
    @BeforeAll
    public static void setUpOnce() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    @Override
                    public void run() {
                        test = new SLDTestRunner();
                    }
                });
    }

    @Test
    public void text_outputTestCommon() {
        test.runTest("output", "text_outputTestCommon.xml");
    }

    @Test
    public void text_outputTestLabelPlacement() {
        test.runTest("output", "text_outputTestLabelPlacement.xml");
    }

    @Test
    public void text_outputTestGeoServerLabelling() {
        test.runTest("output", "text_outputTestGeoServerLabelling.xml");
    }
}
