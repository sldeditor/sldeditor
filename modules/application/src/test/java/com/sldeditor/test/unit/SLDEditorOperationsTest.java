/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.SLDEditorMain;
import com.sldeditor.SLDEditorOperations;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.extension.filesystem.FileSystemExtensionFactory;
import com.sldeditor.test.unit.datasource.impl.DummyExternalSLDFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDEditorOperations class.
 *
 * <p>{@link com.sldeditor.SLDEditorOperations}
 *
 * @author Robert Ward (SCISYS)
 */
class SLDEditorOperationsTest {

    @BeforeEach
    void prepare() {
        SLDEditorOperations.destroyInstance();
    }

    @AfterEach
    void tidyUp() {
        SLDEditorOperations.destroyInstance();
    }

    /** Test method for {@link com.sldeditor.SLDEditorOperations#emptySLD()}. */
    @Test
    void testEmptySLD() {
        SLDEditorOperations.getInstance().emptySLD();

        assertEquals(SelectedSymbol.getInstance().getFilename(), "");
        assertNull(SLDEditorFile.getInstance().getSLDData());
        assertNull(SelectedSymbol.getInstance().getSld());
    }

    /**
     * Test method for {@link
     * com.sldeditor.SLDEditorOperations#loadSLDString(com.sldeditor.common.filesystem.SelectedFiles)}.
     */
    @Test
    void testLoadSLDString() {
        SelectedFiles selectedFiles = new SelectedFiles();
        selectedFiles.setFolderName("folder");
        selectedFiles.setIsFolder(true);

        assertTrue(SLDEditorOperations.getInstance().loadSLDString(selectedFiles));
    }

    /** Test method for {@link com.sldeditor.SLDEditorOperations#preLoad()}. */
    @Test
    void testPreLoad() {
        SLDEditorOperations.getInstance().preLoad();
    }

    /** Test method for {@link com.sldeditor.SLDEditorOperations#reloadSLDFile()}. */
    @Test
    void testReloadSLDFile() {
        SLDEditorOperations.getInstance().reloadSLDFile();

        DummyExternalSLDFile external = new DummyExternalSLDFile();
        SLDEditorFile.getInstance().setSLDData(external.getSLDData());

        SLDEditorMain testObj = new SLDEditorMain(null);
        File f = null;
        try {
            f = File.createTempFile("test", ".sld");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        external.getSLDData().setSLDFile(f);
        try {
            testObj.saveFile(f.toURI().toURL());
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }

        assertTrue(f.exists());

        FileSystemExtensionFactory.getFileExtensionList(null);
        SLDEditorOperations.getInstance().reloadSLDFile();
        FileSystemExtensionFactory.override(null);

        f.delete();
    }
}
