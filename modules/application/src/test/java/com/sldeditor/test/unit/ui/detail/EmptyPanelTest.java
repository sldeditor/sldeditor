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

package com.sldeditor.test.unit.ui.detail;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.ui.detail.EmptyPanel;
import org.junit.jupiter.api.Test;

/**
 * The unit test for EmptyPanel.
 *
 * <p>{@link com.sldeditor.ui.detail.EmptyPanel}
 *
 * @author Robert Ward (SCISYS)
 */
public class EmptyPanelTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.EmptyPanel#EmptyPanel(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.EmptyPanel#populate(com.sldeditor.common.data.SelectedSymbol)}. Test
     * method for {@link com.sldeditor.ui.detail.EmptyPanel#getFieldDataManager()}. Test method for
     * {@link com.sldeditor.ui.detail.EmptyPanel#isDataPresent()}. Test method for {@link
     * com.sldeditor.ui.detail.EmptyPanel#preLoadSymbol()}.
     */
    @Test
    public void testEmptyPanel() {
        EmptyPanel panel = new EmptyPanel();
        panel.populate(null);
        assertNull(panel.getFieldDataManager());
        assertFalse(panel.isDataPresent());
        panel.preLoadSymbol();
    }
}
