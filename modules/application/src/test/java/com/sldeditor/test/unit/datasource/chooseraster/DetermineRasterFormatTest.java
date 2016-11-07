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

package com.sldeditor.test.unit.datasource.chooseraster;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Set;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.gce.image.WorldImageFormat;
import org.junit.Test;

import com.sldeditor.datasource.chooseraster.ChooseRasterFormatInterface;
import com.sldeditor.datasource.chooseraster.DetermineRasterFormat;
import com.sldeditor.test.unit.ui.tree.SLDTreeTest;

/**
 * The unit test for DetermineRasterFormat.
 * <p>{@link com.sldeditor.datasource.chooseraster.DetermineRasterFormat}
 *
 * @author Robert Ward (SCISYS)
 */
public class DetermineRasterFormatTest {

    /**
     * Test method for {@link com.sldeditor.datasource.chooseraster.DetermineRasterFormat#choose(java.io.File, com.sldeditor.datasource.chooseraster.ChooseRasterFormatInterface)}.
     */
    @Test
    public void testChoose() {
        AbstractGridFormat gridFormat = DetermineRasterFormat.choose(null, null);
        assertTrue(UnknownFormat.class == gridFormat.getClass());

        URL url = SLDTreeTest.class.getResource("/raster/sld/sld_cookbook_raster.tif");

        File f = new File(url.getFile());
        gridFormat = DetermineRasterFormat.choose(f, null);

        assertTrue(gridFormat != null);

        // Force to WorldImageFormat
        gridFormat = DetermineRasterFormat.choose(f, new ChooseRasterFormatInterface() {

            @Override
            public AbstractGridFormat showPanel(Set<AbstractGridFormat> formatList) {
                WorldImageFormat wif = new WorldImageFormat();

                return wif;
            }});

        assertTrue(WorldImageFormat.class == gridFormat.getClass());
    }

}
