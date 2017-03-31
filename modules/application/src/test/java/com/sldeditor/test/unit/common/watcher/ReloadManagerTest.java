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

package com.sldeditor.test.unit.common.watcher;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Path;

import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Ignore;
import org.junit.Test;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.watcher.ReloadManager;

/**
 * Unit test for ReloadManager.
 * 
 * <p>{@link com.sldeditor.common.watcher.ReloadManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
@Ignore
public class ReloadManagerTest {

    class DummyCallback implements LoadSLDInterface {
        public int reloadCallbackCalled = 0;

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.common.LoadSLDInterface#emptySLD()
         */
        @Override
        public void emptySLD() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.common.LoadSLDInterface#preLoad()
         */
        @Override
        public void preLoad() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.common.LoadSLDInterface#loadSLDString(com.sldeditor.common.filesystem.
         * SelectedFiles)
         */
        @Override
        public boolean loadSLDString(SelectedFiles selectedFiles) {
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.common.LoadSLDInterface#readSLDFile(java.io.File)
         */
        @Override
        public StyledLayerDescriptor readSLDFile(File file) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.common.LoadSLDInterface#reloadSLDFile()
         */
        @Override
        public void reloadSLDFile() {
            reloadCallbackCalled++;
        }

    }

    /**
     * Test method for
     * {@link com.sldeditor.common.watcher.ReloadManager#fileAdded(java.nio.file.Path)}. Test method
     * for {@link com.sldeditor.common.watcher.ReloadManager#fileModified(java.nio.file.Path)}. Test
     * method for
     * {@link com.sldeditor.common.watcher.ReloadManager#fileDeleted(java.nio.file.Path)}. Test
     * method for
     * {@link com.sldeditor.common.watcher.ReloadManager#sldDataUpdated(com.sldeditor.common.SLDDataInterface, boolean)}.
     * Test method for
     * {@link com.sldeditor.common.watcher.ReloadManager#addListener(com.sldeditor.common.LoadSLDInterface)}.
     */
    @Test
    public void testReloadFile() {
        ReloadManager.getInstance().fileAdded(null);
        ReloadManager.getInstance().fileModified(null);
        ReloadManager.getInstance().fileDeleted(null);

        SLDData sldData = new SLDData(new StyleWrapper(), "");
        ReloadManager.getInstance().sldDataUpdated(sldData, true);

        File expectedFile = new File("/tmp/testFile.sld");
        Path path = expectedFile.toPath();

        // Try with no callback
        ReloadManager.getInstance().fileModified(path);

        DummyCallback callback = new DummyCallback();
        ReloadManager.getInstance().addListener(callback);

        assertEquals(0, callback.reloadCallbackCalled);

        // Set loaded file - should match
        sldData.setSLDFile(expectedFile);
        ReloadManager.getInstance().sldDataUpdated(sldData, true);

        ReloadManager.getInstance().fileModified(path);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, callback.reloadCallbackCalled);

        // Set loaded file - won't match
        callback.reloadCallbackCalled = 0;
        File expectedFile2 = new File("/tmp/differenttestFile.sld");
        path = expectedFile2.toPath();

        ReloadManager.getInstance().fileModified(path);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, callback.reloadCallbackCalled);

        // Now try valid multiple calls
        path = expectedFile.toPath();

        ReloadManager.getInstance().fileModified(path);
        ReloadManager.getInstance().fileModified(path);
        ReloadManager.getInstance().fileModified(path);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, callback.reloadCallbackCalled);
    }

    @Test
    public void testReloadSavedFile() {

        SLDData sldData = new SLDData(new StyleWrapper(), "");
        ReloadManager.getInstance().sldDataUpdated(sldData, true);

        DummyCallback callback = new DummyCallback();
        ReloadManager.getInstance().addListener(callback);

        assertEquals(0, callback.reloadCallbackCalled);

        // Set loaded file - should match
        File expectedFile = new File("/tmp/testFile.sld");
        Path path = expectedFile.toPath();
        sldData.setSLDFile(expectedFile);
        ReloadManager.getInstance().sldDataUpdated(sldData, true);

        // Mark as not saved
        ReloadManager.getInstance().fileModified(path);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, callback.reloadCallbackCalled);

        // Mark as saved
        callback.reloadCallbackCalled = 0;
        ReloadManager.getInstance().setFileSaved();
        ReloadManager.getInstance().fileModified(path);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, callback.reloadCallbackCalled);

        // Mark as not saved
        ReloadManager.getInstance().fileModified(path);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, callback.reloadCallbackCalled);
    }
}
