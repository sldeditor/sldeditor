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
 * The Class SLDCookbookPolygon runs the tests for polygon slds in the SLD Cookbook.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDCookbookPolygon
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
    public void polygon_simplepolygon()
    {
        test.runTest("polygon", "polygon_simplepolygon.xml");
    }

    @Test
    public void polygon_simplepolygonwithstroke()
    {
        test.runTest("polygon", "polygon_simplepolygonwithstroke.xml");
    }

    @Test
    public void polygon_transparentpolygon()
    {
        test.runTest("polygon", "polygon_transparentpolygon.xml");
    }

    @Test
    public void polygon_hatchingfill()
    {
        test.runTest("polygon", "polygon_hatchingfill.xml");
    }

    @Test
    public void polygon_polygonwithdefaultlabel()
    {
        test.runTest("polygon", "polygon_polygonwithdefaultlabel.xml");
    }

    @Test
    public void polygon_labelhalo()
    {
        test.runTest("polygon", "polygon_labelhalo.xml");
    }

    @Test
    public void polygon_attributebasedpolygon()
    {
        test.runTest("polygon", "polygon_attributebasedpolygon.xml");
    }

    @Test
    public void polygon_zoombasedpolygon()
    {
        test.runTest("polygon", "polygon_zoombasedpolygon.xml");
    }

    @Test
    public void polygon_trianglefill()
    {
        test.runTest("polygon", "polygon_trianglefill.xml");
    }

    @Test
    public void polygon_trianglefill_nostroke()
    {
        test.runTest("polygon", "polygon_trianglefill_nostroke.xml");
    }

    @Test
    public void polygon_hatchingfill_transparent()
    {
        test.runTest("polygon", "polygon_hatchingfill_transparent.xml");
    }

    @Test
    public void polygon_picturefill()
    {
        test.runTest("polygon", "polygon_picturefill.xml");
    }
}
