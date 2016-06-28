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
package com.sldeditor.test.unit.common.filesystem;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.filesystem.SelectedFiles;

/**
 * The unit test for SelectedFiles.
 * <p>{@link com.sldeditor.common.filesystem.SelectedFiles}
 *
 * @author Robert Ward (SCISYS)
 */
public class SelectedFilesTest {

    @Test
    public void test() {
        SelectedFiles selectedFiles = new SelectedFiles();

        assertTrue(selectedFiles.isDataSource() == false);
        assertTrue(selectedFiles.isFolder() == false);
        assertTrue(selectedFiles.getSldData().isEmpty());

        selectedFiles.setDataSource(true);
        assertTrue(selectedFiles.isDataSource());
        
        selectedFiles.setIsFolder(true);
        assertTrue(selectedFiles.isFolder());
        
        
        List<SLDDataInterface> actualSldData = new ArrayList<SLDDataInterface>();
        actualSldData.add(new SLDData(null, "test1"));
        actualSldData.add(new SLDData(null, "test2"));
        actualSldData.add(new SLDData(null, "test3"));
        
        selectedFiles.setSldData(actualSldData);
        assertTrue(selectedFiles.getSldData().size() == actualSldData.size());
    }
}
