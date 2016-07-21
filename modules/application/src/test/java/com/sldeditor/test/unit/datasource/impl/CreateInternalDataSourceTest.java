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
package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.impl.CreateInternalDataSource;
import com.sldeditor.datasource.impl.DataSourceInfo;

/**
 * Unit test for CreateInternalDataSource.
 * <p>{@link com.sldeditor.datasource.impl.CreateInternalDataSource}
 * 
 * @author Robert Ward (SCISYS)
 */
public class CreateInternalDataSourceTest {

    /**
     * Test method for {@link com.sldeditor.datasource.impl.CreateInternalDataSource#connect(com.sldeditor.datasource.SLDEditorFileInterface)}.
     */
    @Test
    public void testConnect() {
        CreateInternalDataSource ds = new CreateInternalDataSource();
        
        DataSourceInfo dsInfo = ds.connect(null);
        
        assertTrue(dsInfo != null);
        assertNull(dsInfo.getDataStore());
        assertNull(dsInfo.getTypeName());

        SLDEditorFileInterface sldEditor = new DummyInternalSLDFile(); 
        dsInfo = ds.connect(sldEditor);
        
        assertTrue(dsInfo != null);
        assertTrue(dsInfo.getTypeName() != null);
        assertTrue(dsInfo.getDataStore() != null);
    }

}
