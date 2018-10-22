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

package com.sldeditor.test.unit.common.localisation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.common.localisation.Localisation;
import java.util.Locale;
import org.junit.jupiter.api.Test;

/**
 * The unit test for Localisation class
 *
 * @author Robert Ward (SCISYS)
 */
class LocalisationTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.localisation.Localisation#parseCommandLine(java.lang.String[])}.
     */
    @Test
    void testParseCommandLine() {
        Localisation.getInstance().parseCommandLine(null);

        // No arguments passed
        Locale actual = Localisation.getInstance().getCurrentLocale();
        assertEquals("en", actual.getLanguage());
        assertEquals("UK", actual.getCountry());

        // Valid locale
        String expected = "-locale=it:IT";
        Localisation.getInstance().parseCommandLine(new String[] {expected});

        actual = Localisation.getInstance().getCurrentLocale();
        assertEquals("it", actual.getLanguage());
        assertEquals("IT", actual.getCountry());

        // Too few values
        expected = "-locale=it";
        Localisation.getInstance().parseCommandLine(new String[] {expected});

        actual = Localisation.getInstance().getCurrentLocale();
        assertEquals("en", actual.getLanguage());
        assertEquals("UK", actual.getCountry());

        // Too many values
        expected = "-locale=de:DE:DE";
        Localisation.getInstance().parseCommandLine(new String[] {expected});

        actual = Localisation.getInstance().getCurrentLocale();
        assertEquals("en", actual.getLanguage());
        assertEquals("UK", actual.getCountry());
    }
}
