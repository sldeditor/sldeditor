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

package com.sldeditor.test.unit.common.console;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.common.console.ConsoleCellRenderer;
import com.sldeditor.common.console.ConsoleData;
import com.sldeditor.common.console.ConsoleDataEnum;
import java.awt.Color;
import javax.swing.JLabel;
import org.junit.jupiter.api.Test;

/**
 * The unit test for ConsoleCellRenderer.
 *
 * <p>{@link com.sldeditor.common.console.ConsoleCellRenderer}
 *
 * @author Robert Ward (SCISYS)
 */
public class ConsoleCellRendererTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.console.ConsoleCellRenderer#getListCellRendererComponent(javax.swing.JList,
     * com.sldeditor.common.console.ConsoleData, int, boolean, boolean)}.
     */
    @Test
    public void testGetListCellRendererComponent() {
        ConsoleCellRenderer renderer = new ConsoleCellRenderer();
        String expectedMessage = "test message";
        ConsoleDataEnum expectedType = ConsoleDataEnum.ERROR;
        ConsoleData value = new ConsoleData(expectedMessage, expectedType);

        JLabel label = (JLabel) renderer.getListCellRendererComponent(null, value, 0, true, true);

        assertEquals(expectedMessage, label.getText());
        assertEquals(Color.RED, label.getForeground());

        expectedMessage = "test message2";
        expectedType = ConsoleDataEnum.INFORMATION;
        value = new ConsoleData(expectedMessage, expectedType);

        label = (JLabel) renderer.getListCellRendererComponent(null, value, 0, true, true);

        assertEquals(expectedMessage, label.getText());
        assertEquals(Color.BLACK, label.getForeground());
    }
}
