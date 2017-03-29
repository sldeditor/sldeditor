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

package com.sldeditor.test.unit.ui.ttf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sldeditor.ui.ttf.CharMap4;

/**
 * The unit test for CharMap4.
 * 
 * <p>{@link com.sldeditor.ui.ttf.CharMap4}
 *
 * @author Robert Ward (SCISYS)
 */
public class CharMap4Test {

    /**
     * Test method for {@link com.sldeditor.ui.ttf.CharMap4#CharMap4()}.
     */
    @Test
    public void testCharMap4() {
        CharMap4 testObj = new CharMap4();
        testObj.loadConfig();

        testObj.setTTFString("ttf://Symbol#221");

        testObj.setSelectedCharacter("a");
        testObj.setStatusText("new status");
        testObj.copyText();

        assertEquals(Character.MAX_CODE_POINT, CharMap4.getMaxUnicode());
        assertEquals(Character.MIN_CODE_POINT, CharMap4.getMinUnicode());
        assertNotNull(testObj.getDisplayFont());
        assertNotNull(CharMap4.getFormatComma());
        assertFalse(CharMap4.isGlyphFlag());

        int expectedValue = 42;
        assertNotNull(CharMap4.charToString(expectedValue));
        assertNotNull(CharMap4.unicodeNotation(expectedValue));
        assertEquals(expectedValue,
                CharMap4.decodeUnicodeNotation(CharMap4.unicodeNotation(expectedValue)));

        for (int i = 0; i < 0x10FFFF + 2; i += 10) {
            assertNotNull(testObj.captionGet(i));
        }
        
        testObj.userKey("GotoEnd");
        testObj.userKey("GotoHome");
        testObj.userKey("LineDown");
        testObj.userKey("LineUp");
        testObj.userKey("PageDown");
        testObj.userKey("PageUp");
        testObj.userKey("ReportShow");
        testObj.userKey("made up");
    }

}
