/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.tool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.tool.GenerateFilename;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * The Class GenerateFilenameTest.
 *
 * @author Robert Ward (SCISYS)
 */
public class GenerateFilenameTest {

    /**
     * Test method for {@link com.sldeditor.tool.GenerateFilename#findUniqueName(java.lang.String,
     * java.lang.String, java.lang.String)}.
     */
    @Test
    public void testFindUniqueName() {
        String fileExtension = ".tmp";
        File f = null;

        try {
            f = File.createTempFile("test", fileExtension);
        } catch (IOException e) {
            fail(e.getStackTrace().toString());
        }

        String filename = ExternalFilenames.removeSuffix(f.getName());

        String destinationFolder = f.getParent();
        File actualResult =
                GenerateFilename.findUniqueName(destinationFolder, filename, fileExtension);

        assertNotEquals(actualResult.getAbsolutePath(), f.getAbsolutePath());
        String actualFilename = actualResult.getName();
        int counter = 1;
        String expectedFilename = String.format("%s%d%s", filename, counter, fileExtension);
        assertEquals(actualFilename, expectedFilename);
    }
}
