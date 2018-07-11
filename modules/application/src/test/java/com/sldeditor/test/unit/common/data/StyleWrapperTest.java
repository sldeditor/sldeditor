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

package com.sldeditor.test.unit.common.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.StyleWrapper;
import org.junit.jupiter.api.Test;

/**
 * The unit test for StyleWrapper.
 *
 * <p>{@link com.sldeditor.common.data.StyleWrapper}
 *
 * @author Robert Ward (SCISYS)
 */
public class StyleWrapperTest {

    @Test
    public void test() {
        StyleWrapper styleWrapper = new StyleWrapper();

        assertNull(styleWrapper.getStyle());
        assertNull(styleWrapper.getWorkspace());

        String expectedStyle = "test style";
        styleWrapper.setStyle(expectedStyle);
        assertEquals(styleWrapper.getStyle().compareTo(expectedStyle), 0);

        String expectedWorkspace = "test workspace";
        styleWrapper.setWorkspace(expectedWorkspace);
        assertEquals(styleWrapper.getWorkspace().compareTo(expectedWorkspace), 0);

        // Clone
        StyleWrapper clone = styleWrapper.clone();

        assertTrue(styleWrapper.compareTo(null) != 0);
        assertTrue(styleWrapper.compareTo(clone) == 0);
    }
}
