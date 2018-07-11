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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.ui.detail.BasePanelPadding;
import java.awt.Dimension;
import javax.swing.Box;
import org.junit.jupiter.api.Test;

/**
 * The unit test for BasePanelPadding.
 *
 * <p>{@link com.sldeditor.ui.detail.BasePanelPadding}
 *
 * @author Robert Ward (SCISYS)
 */
public class BasePanelPaddingTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.BasePanelPadding#BasePanelPadding(javax.swing.Box)}.
     */
    @Test
    public void testBasePanelPadding() {
        BasePanelPadding p = new BasePanelPadding(null);

        p.removePadding();

        p.addPadding();

        p.removePadding();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.BasePanelPadding#addPadding()}. Test method
     * for {@link com.sldeditor.ui.detail.BasePanelPadding#removePadding()}.
     */
    @Test
    public void testAddPadding() {
        Box box = Box.createVerticalBox();

        Dimension expectedSize = new Dimension(100, 100);
        box.setPreferredSize(expectedSize);
        BasePanelPadding p = new BasePanelPadding(box);

        p.removePadding();

        // Simulate box that needs padding
        Dimension actualSize = p.addPadding();

        assertEquals(1, box.getComponentCount());
        Dimension fillerSize = box.getComponent(0).getPreferredSize();

        assertTrue(Math.abs(actualSize.getWidth() - expectedSize.getWidth()) < 0.001);
        assertEquals(
                (int) (BasePanelPadding.getPanelHeight() - fillerSize.getHeight()),
                (int) expectedSize.getHeight());
        p.removePadding();
        assertEquals(0, box.getComponentCount());

        // Simulate box that is too large for screen
        expectedSize = new Dimension(100, 1000);
        box.setPreferredSize(expectedSize);
        actualSize = p.addPadding();

        assertEquals(0, box.getComponentCount());
        assertTrue(Math.abs(actualSize.getWidth() - expectedSize.getWidth()) < 0.001);
        p.removePadding();
        assertEquals(0, box.getComponentCount());
    }
}
