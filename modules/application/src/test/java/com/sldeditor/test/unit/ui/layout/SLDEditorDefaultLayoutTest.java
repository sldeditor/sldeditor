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

package com.sldeditor.test.unit.ui.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.SLDEditor;
import com.sldeditor.SLDEditorDlgInterface;
import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.watcher.ReloadManager;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.checks.CheckAttributeFactory;
import com.sldeditor.datasource.checks.CheckAttributeInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.map.MapRender;
import com.sldeditor.render.RenderPanelImpl;
import com.sldeditor.ui.layout.SLDEditorDefaultLayout;
import com.sldeditor.ui.layout.UILayoutFactory;
import com.sldeditor.ui.layout.UILayoutInterface;
import com.sldeditor.ui.menu.SLDEditorMenus;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDEditorDefaultLayout class.
 *
 * @author Robert Ward (SCISYS)
 */
class SLDEditorDefaultLayoutTest {

    /** Clean up. */
    @AfterAll
    public static void cleanUp() {
        List<CheckAttributeInterface> checkList = new ArrayList<CheckAttributeInterface>();
        CheckAttributeFactory.setOverrideCheckList(checkList);

        RenderPanelImpl.setUnderTest(false);
        clearDown();
    }

    /** The Class TestSLDEditor. */
    static class TestSLDEditor extends SLDEditor {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test SLD editor.
         *
         * @param filename the filename
         * @param extensionArgList the extension arg list
         * @param overrideSLDEditorDlg the override SLD editor dlg
         */
        public TestSLDEditor(
                String filename,
                List<String> extensionArgList,
                SLDEditorDlgInterface overrideSLDEditorDlg) {
            super(filename, extensionArgList, overrideSLDEditorDlg);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.SLDEditor#populate(com.sldeditor.common.SLDDataInterface)
         */
        @Override
        protected void populate(SLDDataInterface sldData) {
            super.populate(sldData);
        }

        public static TestSLDEditor createAndShowGUI2(
                String filename,
                List<String> extensionArgList,
                boolean underTest,
                SLDEditorDlgInterface overrideSLDEditorDlg) {
            SLDEditor.underTestFlag = underTest;

            frame = new JFrame("test");

            CoordManager.getInstance().populateCRSList();
            Controller.getInstance().setFrame(frame);

            MapRender.setUnderTest(underTest);
            RenderPanelImpl.setUnderTest(underTest);
            ReloadManager.setUnderTest(underTest);

            frame.setDefaultCloseOperation(
                    underTest ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);

            // Add contents to the window.
            TestSLDEditor sldEditor =
                    new TestSLDEditor(filename, extensionArgList, overrideSLDEditorDlg);

            // Display the window.
            frame.pack();

            return sldEditor;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.layout.SLDEditorDockableLayout#createUI(com.sldeditor.common.SLDEditorInterface,
     * com.sldeditor.ui.panels.SLDEditorUIPanels, java.util.List)}.
     */
    @Test
    void testCreateUI() {
        @SuppressWarnings("unused")
        TestSLDEditor testSLDEditor = null;
        PrefData prefData = new PrefData();

        prefData.setUiLayoutClass(SLDEditorDefaultLayout.class.getName());
        PrefManager.getInstance().setPrefData(prefData);
        try {
            testSLDEditor = TestSLDEditor.createAndShowGUI2(null, null, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File tmpFolder = new File(System.getProperty("java.io.tmpdir"));

        String uiLayout = PrefManager.getInstance().getPrefData().getUiLayoutClass();

        UILayoutInterface ui = UILayoutFactory.getUILayout(uiLayout);
        SLDEditorUIPanels uiMgr = new SLDEditorUIPanels();

        ui.createUI(testSLDEditor, uiMgr, new ArrayList<ExtensionInterface>());
        ui.writeLayout(tmpFolder.getAbsolutePath());
        ui.readLayout(tmpFolder.getAbsolutePath());
        File f = new File(tmpFolder, "layout.data");
        f.delete();

        assertEquals(uiLayout, SLDEditorDefaultLayout.class.getName());
    }

    /** Clear down. */
    private static void clearDown() {
        DataSourceFactory.reset();
        SelectedSymbol.destroyInstance();
        SLDEditorFile.destroyInstance();
        SLDEditorMenus.destroyInstance();
        DatabaseConnectionManager.destroyInstance();
        GeoServerConnectionManager.destroyInstance();
        PrefManager.destroyInstance();
        VendorOptionManager.destroyInstance();
        EnvironmentVariableManager.destroyInstance();
        UndoManager.destroyInstance();
    }
}
