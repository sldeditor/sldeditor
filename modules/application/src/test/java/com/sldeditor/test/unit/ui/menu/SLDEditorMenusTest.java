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

package com.sldeditor.test.unit.ui.menu;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.ui.menu.SLDEditorMenus;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.junit.jupiter.api.Test;

/**
 * The unit test for SLDEditorMenus.
 *
 * <p>{@link com.sldeditor.ui.menu.SLDEditorMenus}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorMenusTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.menu.SLDEditorMenus#SLDEditorMenus(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    public void testSLDEditorMenus() {
        SLDEditorMenus.createMenus(null, null);
        SLDEditorMenus.destroyInstance();

        SLDEditorInterface application =
                new SLDEditorInterface() {

                    @Override
                    public JPanel getAppPanel() {
                        return null;
                    }

                    @Override
                    public void updateWindowTitle(boolean dataEditedFlag) {}

                    @Override
                    public void chooseNewSLD() {}

                    @Override
                    public void exitApplication() {}

                    @Override
                    public void saveFile(URL url) {}

                    @Override
                    public void saveSLDData(SLDDataInterface sldData) {}

                    @Override
                    public LoadSLDInterface getLoadSLDInterface() {
                        return null;
                    }

                    @Override
                    public JFrame getApplicationFrame() {
                        return null;
                    }

                    @Override
                    public void openFile(URL selectedURL) {}

                    @Override
                    public String getAppName() {
                        return null;
                    }

                    @Override
                    public void refreshPanel(Class<?> parent, Class<?> panelClass) {}
                };
        SLDEditorMenus.createMenus(application, null);
        SLDEditorMenus.destroyInstance();

        SLDEditorMenus.createMenus(application, new ArrayList<ExtensionInterface>());
        SLDEditorMenus.destroyInstance();

        EnvironmentVariableManager.destroyInstance();
        SLDEditorFile.destroyInstance();
        VendorOptionManager.destroyInstance();
        UndoManager.destroyInstance();
    }
}
