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

import java.util.ArrayList;

import org.junit.Test;

import com.sldeditor.SLDEditor;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.ui.menu.SLDEditorMenus;

/**
 * The unit test for SLDEditorMenus.
 * <p>{@link com.sldeditor.ui.menu.SLDEditorMenus}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorMenusTest {

    /**
     * Test method for {@link com.sldeditor.ui.menu.SLDEditorMenus#SLDEditorMenus(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    public void testSLDEditorMenus() {
        SLDEditorMenus.createMenus(null, null);
        SLDEditorMenus.destroyInstance();

        SLDEditor application = new SLDEditor(null, null, null);
        SLDEditorMenus.createMenus(application, null);
        SLDEditorMenus.destroyInstance();
        
        SLDEditorMenus.createMenus(application, new ArrayList<ExtensionInterface>());
        SLDEditorMenus.destroyInstance();
    }

}
