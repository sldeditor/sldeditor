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
package com.sldeditor.test.unit.datasource.extension.filesystem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.datasource.extension.filesystem.FileSystemUtils;

/**
 * The unit test for FileSystemUtils.
 * <p>{@link com.sldeditor.datasource.extension.filesystem.FileSystemUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemUtilsTest {

    @Test
    public void test() {

        assertFalse(FileSystemUtils.isFileExtensionSupported(null,  null));

        File f = new File("test.tst");

        List<String> fileExtensionList = new ArrayList<String>();

        assertFalse(FileSystemUtils.isFileExtensionSupported(f,  fileExtensionList));

        fileExtensionList.add("abc");
        fileExtensionList.add("xyz");
        fileExtensionList.add("def");

        assertFalse(FileSystemUtils.isFileExtensionSupported(f,  fileExtensionList));

        fileExtensionList.add("tst");

        assertTrue(FileSystemUtils.isFileExtensionSupported(f,  fileExtensionList));
    }
}
