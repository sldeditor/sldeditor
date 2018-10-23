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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.SLDEditorMain;
import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.test.unit.datasource.impl.DummyExternalSLDFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDEditorMain class.
 *
 * <p>{@link com.sldeditor.SLDEditorMain}
 *
 * @author Robert Ward (SCISYS)
 */
class SLDEditorMainTest {

    class TestSLDEditorMain extends SLDEditorMain {

        /**
         * Instantiates a new test SLD editor main.
         *
         * @param appPanel the app panel
         */
        public TestSLDEditorMain(JPanel appPanel) {
            super(appPanel);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.SLDEditorMain#newSLD(java.util.List)
         */
        @Override
        protected void newSLD(List<SLDDataInterface> newSLDList) {
            super.newSLD(newSLDList);
        }
    }

    @BeforeEach
    void prepare() {
        SLDEditorFile.destroyInstance();
    }

    @AfterEach
    void tidyUp() {
        SLDEditorFile.destroyInstance();
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#updateWindowTitle(boolean)}. */
    @Test
    void testUpdateWindowTitle() {

        TestSLDEditorMain testObj = new TestSLDEditorMain(new JPanel());

        JFrame frame = new JFrame();
        Controller.getInstance().setFrame(frame);

        // No SLD Editor file and SLD file
        testObj.updateWindowTitle(false);

        String actual = Controller.getInstance().getFrame().getTitle();

        assertEquals(SLDEditorMain.generateApplicationTitleString() + " -  ", actual);

        testObj.updateWindowTitle(true);

        actual = Controller.getInstance().getFrame().getTitle();
        assertEquals(SLDEditorMain.generateApplicationTitleString() + " - *", actual);

        // SLD Editor file and SLD file
        DummyExternalSLDFile external = new DummyExternalSLDFile();
        SLDEditorFile.getInstance().setSLDData(external.getSLDData());
        String sldEditorFile = "tmp.sldeditor";
        SLDEditorFile.getInstance().setSldEditorFile(new File(sldEditorFile));

        // Data not updated
        testObj.updateWindowTitle(false);

        actual = Controller.getInstance().getFrame().getTitle();

        assertEquals(
                SLDEditorMain.generateApplicationTitleString()
                        + " - "
                        + sldEditorFile
                        + " - "
                        + external.getSLDData().getLayerName()
                        + " ",
                actual);

        // Data updated
        testObj.updateWindowTitle(true);

        actual = Controller.getInstance().getFrame().getTitle();

        assertEquals(
                SLDEditorMain.generateApplicationTitleString()
                        + " - "
                        + sldEditorFile
                        + " - "
                        + external.getSLDData().getLayerName()
                        + "*",
                actual);

        // Just sld layer
        SLDEditorFile.getInstance().setSldEditorFile(null);
        testObj.updateWindowTitle(true);

        actual = Controller.getInstance().getFrame().getTitle();

        assertEquals(
                SLDEditorMain.generateApplicationTitleString()
                        + " - "
                        + external.getSLDData().getLayerName()
                        + "*",
                actual);
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#exitApplication()}. */
    @Test
    void testExitApplication() {
        // Can't test as it exits the application
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#chooseNewSLD()}. */
    @Test
    void testChooseNewSLD() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(new JPanel());

        JFrame frame = new JFrame();
        Controller.getInstance().setFrame(frame);

        testObj.newSLD(null);

        // SLD file
        DummyExternalSLDFile external = new DummyExternalSLDFile();
        List<SLDDataInterface> newSLDList = new ArrayList<SLDDataInterface>();

        newSLDList.add(external.getSLDData());

        testObj.newSLD(newSLDList);
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#saveFile(java.net.URL)}. */
    @Test
    void testSaveFile() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(new JPanel());
        testObj.saveFile(null);

        // SLD file
        DummyExternalSLDFile external = new DummyExternalSLDFile();
        SLDEditorFile.getInstance().setSLDData(external.getSLDData());

        File f = null;
        try {
            f = File.createTempFile("test", ".sld");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            testObj.saveFile(f.toURI().toURL());
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }

        assertTrue(f.exists());
        f.delete();
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#getSLDString()}. */
    @Test
    void testGetSLDString() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(new JPanel());

        JFrame frame = new JFrame();
        Controller.getInstance().setFrame(frame);

        // SLD file
        DummyExternalSLDFile external = new DummyExternalSLDFile();
        SLDEditorFile.getInstance().setSLDData(external.getSLDData());

        assertNotNull(testObj.getSLDString());
        assertNotNull(testObj.getSLDString());
    }

    /**
     * Test method for {@link
     * com.sldeditor.SLDEditorMain#saveSLDData(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    void testSaveSLDData() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(new JPanel());

        // SLD file
        DummyExternalSLDFile external = new DummyExternalSLDFile();

        testObj.saveSLDData(external.getSLDData());
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#getAppPanel()}. */
    @Test
    void testGetAppPanel() {
        JPanel appPanel = new JPanel();
        TestSLDEditorMain testObj = new TestSLDEditorMain(appPanel);

        assertEquals(testObj.getAppPanel(), appPanel);
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#getLoadSLDInterface()}. */
    @Test
    void testGetLoadSLDInterface() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(null);

        assertNotNull(testObj.getLoadSLDInterface());
    }

    /** Test method for {@link com.sldeditor.SLDEditorMain#openFile(java.net.URL)}. */
    @Test
    void testOpenFile() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(null);
        testObj.openFile(null);
    }

    /**
     * Test method for {@link com.sldeditor.SLDEditorMain#refreshPanel(java.lang.Class,
     * java.lang.Class)}.
     */
    @Test
    void testRefreshPanel() {
        TestSLDEditorMain testObj = new TestSLDEditorMain(null);

        testObj.refreshPanel(null, null);
    }
}
