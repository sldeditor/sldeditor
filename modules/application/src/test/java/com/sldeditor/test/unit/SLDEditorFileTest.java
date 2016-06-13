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
package com.sldeditor.test.unit;

import java.io.File;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.datasource.SLDEditorDataUpdateInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.test.unit.helper.SLDEditorDataUpdateInterfaceTestHelper;

import junit.framework.TestCase;

public class SLDEditorFileTest extends TestCase {

    public void test_getInstance_A$() throws Exception {
        SLDEditorFileInterface actual = SLDEditorFile.getInstance();
        SLDEditorFileInterface expected = SLDEditorFile.getInstance();
        assertEquals(expected, actual);
    }

    public void test_getSLDData_A$() throws Exception {
        SLDEditorFile.destroyInstance();
        SLDEditorFileInterface target = SLDEditorFile.getInstance();
        SLDDataInterface actual = target.getSLDData();
        SLDDataInterface expected = null;
        assertEquals(expected, actual);
    }

    public void test_fileSaved_A$() throws Exception {
        SLDEditorDataUpdateInterfaceTestHelper testReceiver = new SLDEditorDataUpdateInterfaceTestHelper();
        
        SLDEditorFile target = SLDEditorFile.getInstance();
        target.fileOpenedSaved();
        
        assertEquals(false, testReceiver.sldDataUpdated_methodReached);
        
        target.addSLDEditorFileUpdateListener(testReceiver);
        target.fileOpenedSaved();
        assertEquals(true, testReceiver.sldDataUpdated_methodReached);
        assertEquals(false, testReceiver.sldDataUpdated_dataEditedFlag);
        
        testReceiver.reset();
        
        target.renderSymbol();
        assertEquals(true, testReceiver.sldDataUpdated_methodReached);
        assertEquals(true, testReceiver.sldDataUpdated_dataEditedFlag);
    }

    public void test_getSldEditorFile_A$() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        File actual = target.getSldEditorFile();
        File expected = null;
        assertEquals(expected, actual);
    }

    public void test_addSLDEditorFileUpdateListener_A$SLDEditorDataUpdateInterface() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        SLDEditorDataUpdateInterface listener = null;
        target.addSLDEditorFileUpdateListener(listener);
    }

    public void test_dataSourceLoaded_A$GeometryTypeEnum$boolean() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        GeometryTypeEnum geometryType = null;
        boolean isConnectedToDataSourceFlag = false;
        target.dataSourceLoaded(geometryType, isConnectedToDataSourceFlag);
    }

    public void test_addListener_A$SLDOutputInterface() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        SLDOutputInterface sldOutput = null;
        target.addSLDOutputListener(sldOutput);
    }

    public void test_renderSymbol_A$() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        target.renderSymbol();
    }

    public void test_setSLDData_A$SLDDataInterface() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        SLDDataInterface sldData = null;
        target.setSLDData(sldData);
    }

    public void test_setSldFile_A$File() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        File sldFile = null;
        target.setSldFile(sldFile);
    }

    public void test_getDataSource_A$() throws Exception {
        SLDEditorFileInterface target = SLDEditorFile.getInstance();
        DataSourcePropertiesInterface actual = target.getDataSource();
        DataSourcePropertiesInterface expected = null;
        assertEquals(expected, actual);
    }

    public void test_setDataSource_A$DataSourceProperties() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        DataSourcePropertiesInterface dataSourceProperties = null;
        target.setDataSource(dataSourceProperties);
    }

    public void test_setSldEditorFile_A$File() throws Exception {
        SLDEditorFile target = SLDEditorFile.getInstance();
        File sldEditorFile = null;
        target.setSldEditorFile(sldEditorFile);
    }

    public void test_getSLDFileExtension_A$() throws Exception {
        String actual = SLDEditorFile.getSLDFileExtension();
        String expected = ".sld";
        assertEquals(expected, actual);
    }

}
