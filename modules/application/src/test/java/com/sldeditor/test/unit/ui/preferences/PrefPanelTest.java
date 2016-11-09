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

package com.sldeditor.test.unit.ui.preferences;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.ui.preferences.PrefPanel;

/**
 * Unit test for PrefPanel class.
 * <p>{@link com.sldeditor.ui.preferences.PrefPanel}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class PrefPanelTest {

    class TestPrefPanel extends PrefPanel
    {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        TestPrefPanel()
        {
            super();
        }

        void testPopulate(PrefData prefData) {
            populate(prefData);
        }

    }

    /**
     * Test method for {@link com.sldeditor.ui.preferences.PrefPanel#showDialog(com.sldeditor.common.preferences.PrefData)}.
     */
    @Test
    public void testShowDialog() {
        TestPrefPanel panel = new TestPrefPanel();

        panel.testPopulate(null);

        PrefData prefData = new PrefData();
        prefData.setUiLayoutClass("com.sldeditor.ui.layout.SLDEditorDefaultLayout");
        panel.testPopulate(prefData);

        PrefData actual = panel.getPrefData();

        assertEquals(prefData, actual);
    }

}
