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

package com.sldeditor.test.unit.ui.legend.option;

import static org.junit.Assert.assertEquals;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import org.geotools.styling.StyleBuilder;
import org.junit.Test;

import com.sldeditor.ui.legend.option.FontUtils;

/**
 * The unit test for FontUtils.
 * 
 * <p>{@link com.sldeditor.ui.legend.option.FontUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class FontUtilsTest {

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.FontUtils#getFont(org.geotools.styling.Font)}.
     */
    @Test
    public void testGetFont() {
        // Test does:
        // Create a java.awt.Font, convert it to a org.geotools.styling.Font.
        // Using FontUtils, convert it back to java.awt.Font and check that it
        // is the same as the original.
        StyleBuilder sb = new StyleBuilder();

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts(); // Get the fonts

        // Plain font
        Font javaFont = new Font(fonts[0].getFamily(), Font.PLAIN, 24);

        org.geotools.styling.Font geoToolsFonts = sb.createFont(javaFont);
        Font actualJavaFont = FontUtils.getFont(geoToolsFonts);
        assertEquals(actualJavaFont, javaFont);

        // Bold font
        javaFont = new Font(fonts[0].getFamily(), Font.BOLD, 18);

        geoToolsFonts = sb.createFont(javaFont);
        actualJavaFont = FontUtils.getFont(geoToolsFonts);
        assertEquals(actualJavaFont, javaFont);

        // Italic font
        javaFont = new Font(fonts[0].getFamily(), Font.ITALIC, 18);

        geoToolsFonts = sb.createFont(javaFont);
        actualJavaFont = FontUtils.getFont(geoToolsFonts);
        assertEquals(actualJavaFont, javaFont);

        // Bold and italic font
        javaFont = new Font(fonts[0].getFamily(), Font.BOLD | Font.ITALIC, 18);

        geoToolsFonts = sb.createFont(javaFont);
        actualJavaFont = FontUtils.getFont(geoToolsFonts);
        assertEquals(actualJavaFont, javaFont);
    }

}
