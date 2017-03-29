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

package com.sldeditor.test.unit.extension.filesystem;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.ExtensionInterface;

/**
 * Unit test for ExtensionFactory class.
 * 
 * <p>{@link com.sldeditor.extension.ExtensionFactory}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExtensionFactoryTest {

    /**
     * Test method for {@link com.sldeditor.extension.ExtensionFactory#getAvailableExtensions()}.
     */
    @Test
    public void testGetAvailableExtensions() {
        List<ExtensionInterface> extensionList = ExtensionFactory.getAvailableExtensions();
        assertEquals(1, extensionList.size());

    }

    /**
     * Test method for
     * {@link com.sldeditor.extension.ExtensionFactory#getArguments(com.sldeditor.extension.ExtensionInterface, java.util.List)}.
     */
    @Test
    public void testGetArguments() {
        String[] argList1 = { "-ext=Ignored" };
        List<String> actualList = ExtensionFactory.getArgumentList(argList1);

        assertEquals(0, actualList.size());

        String[] argList2 = { "-extension.file.folder=D:\\GitHub\\SLDEditor\\slddata" };
        actualList = ExtensionFactory.getArgumentList(argList2);

        assertEquals(1, actualList.size());
    }

    /**
     * Test method for
     * {@link com.sldeditor.extension.ExtensionFactory#getArgumentList(java.lang.String[])}.
     */
    @Test
    public void testGetArgumentList() {
        List<ExtensionInterface> extensionList = ExtensionFactory.getAvailableExtensions();
        assertEquals(1, extensionList.size());

        ExtensionInterface extension = extensionList.get(0);

        String[] args = { "-extension.file.folder=D:\\GitHub\\SLDEditor\\slddata",
                "-extension.zzz" };
        List<String> extensionArgList = ExtensionFactory.getArgumentList(args);

        List<String> extensionSpecificArgumentList = ExtensionFactory.getArguments(extension,
                extensionArgList);

        assertEquals(1, extensionSpecificArgumentList.size());
        assertEquals("folder=D:\\GitHub\\SLDEditor\\slddata", extensionSpecificArgumentList.get(0));
    }

}
