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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.io.IOUtils;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Ignore;
import org.junit.Test;

import com.sldeditor.SLDEditor;
import com.sldeditor.SLDEditorDlgInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;

/**
 * Test the save/save as reload functionality.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class SaveTest {

    /**
     * The Class DummyDlg.
     */
    class DummyDlg implements SLDEditorDlgInterface
    {

        /** The load called. */
        private boolean loadCalled = false;

        /** The reload called. */
        private boolean reloadCalled = false;

        /* (non-Javadoc)
         * @see com.sldeditor.SLDEditorDlgInterface#load(javax.swing.JFrame)
         */
        @Override
        public boolean load(JFrame frame) {
            loadCalled = true;
            return false;
        }

        /**
         * Checks if is load called.
         *
         * @return true, if is load called
         */
        public boolean isLoadCalled()
        {
            boolean tmp = loadCalled;
            loadCalled = false;
            return tmp;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.SLDEditorDlgInterface#reload(javax.swing.JFrame)
         */
        @Override
        public boolean reload(JFrame frame) {
            reloadCalled = true;
            return false;
        }

        /**
         * Checks if is reload called.
         *
         * @return true, if is reload called
         */
        public boolean isReloadCalled()
        {
            boolean tmp = reloadCalled;
            reloadCalled = false;
            return tmp;
        }

    }

    /**
     * Test save functionality.
     */
    @Ignore
    @Test
    public void testSave() {
        String testsldfile = "/line/sld/line_dashdot.sld";
        String testsldfile2 = "/line/sld/line_simpleline.sld";
        SLDEditor sldEditor = null;

        DummyDlg dlg = new DummyDlg();
        InputStream inputStream = SaveTest.class.getResourceAsStream(testsldfile);
        InputStream inputStream2 = SaveTest.class.getResourceAsStream(testsldfile2);

        File file1 = null;
        File file2 = null;
        File file3 = null;

        try {
            file1 = stream2file(inputStream);
            file2 = stream2file(inputStream2);

            sldEditor = SLDEditor.createAndShowGUI(null, null, false, dlg);

            System.out.println("Opening : " + file1.toURI().toURL());
            sldEditor.openFile(file1.toURI().toURL());

            assertFalse(dlg.isLoadCalled()); // Data was not edited
            assertFalse(dlg.isReloadCalled());
            System.out.println("Saving : " + file1.toURI().toURL());
            sldEditor.saveFile(file1.toURI().toURL());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());

            overwriteFile(file2, file1);
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());
            File saveAsFile = new File(file1.getParentFile(), "SaveAs" + file1.getName());
            System.out.println("Saving : " + saveAsFile.toURI().toURL());
            sldEditor.saveFile(saveAsFile.toURI().toURL());
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());
            System.out.println("Saving : " + saveAsFile.toURI().toURL());
            sldEditor.saveFile(saveAsFile.toURI().toURL());
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());

            file3 = File.createTempFile(SaveTest.class.getName(), ".sld");

            SelectedFiles selectedFiles = createNewSLD();
            System.out.println("Creating new file");
            assertTrue(sldEditor.loadSLDString(selectedFiles));
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());
            overwriteFile(file1, file3);
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());
            System.out.println("Saving : " + file3.toURI().toURL());
            sldEditor.saveFile(file3.toURI().toURL());
            assertFalse(dlg.isLoadCalled());
            assertFalse(dlg.isReloadCalled());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            if(file1 != null)
            {
                file1.delete();
            }

            if(file2 != null)
            {
                file2.delete();
            }

            if(file3 != null)
            {
                file3.delete();
            }
        }
    }

    /**
     * Overwrite file.
     *
     * @param sourceFile the source file
     * @param destFile the dest file
     */
    private void overwriteFile(File sourceFile, File destFile) {

        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destFile, true); // appending output stream

            try {
                IOUtils.copy(in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(SaveTest.class.getName(), ".sld");
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return tempFile;
    }

    /**
     * @return
     */
    private SelectedFiles createNewSLD() {
        List<SLDDataInterface> newSLDList = new ArrayList<SLDDataInterface>();

        StyledLayerDescriptor sld = DefaultSymbols.createNewPolygon();

        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

        String newName = "TestSLD";
        newSLDList.add(new SLDData(new StyleWrapper(newName), sldWriter.encodeSLD(null, sld)));

        SelectedFiles selectedFiles = new SelectedFiles();
        selectedFiles.setSldData(newSLDList);
        return selectedFiles;
    }
}
