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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The unit test for ConsoleManager.
 * <p>{@link com.sldeditor.common.console.ConsoleManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class ConsoleManagerTest {

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#getInstance()}.
     */
    @Test
    public void testGetInstance() {
        ConsoleManager instance1 = ConsoleManager.getInstance();
        ConsoleManager instance2 = ConsoleManager.getInstance();
        assertEquals(instance1, instance2);
    }

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        assertNotNull(ConsoleManager.getInstance().getPanel());
    }

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#error(java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testError() {
        String errorMessage = "errorMessage";
        ConsoleManager.getInstance().error(this, errorMessage);

        int occurances = countOccurences("ERROR", errorMessage);

        assertEquals(occurances, 1);
    }

    /**
     * Count occurrences of strings in the log file.
     *
     * @param prefix the prefix
     * @param message the message
     * @return the int
     */
    private int countOccurences(String prefix, String message) {
        File f = new File("consolemanagertest.log");
        int count = 0;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null) {
                if(line.startsWith(prefix) && line.endsWith(message))
                {
                    count ++;
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        f.delete();

        return count;
    }

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#information(java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testInformation() {
        String errorMessage = "information message";
        ConsoleManager.getInstance().information(this, errorMessage);

        int occurances = countOccurences("INFO", errorMessage);

        assertEquals(occurances, 1);
    }

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#exception(java.lang.Class, java.lang.Exception)}.
     */
    @Test
    public void testExceptionClassOfQException() {
        String errorMessage = "file not found 1";

        Exception e = new FileNotFoundException(errorMessage);

        ConsoleManager.getInstance().exception(this, e);

        int occurances = countOccurences("ERROR", errorMessage);

        assertEquals(occurances, 1);
    }

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#exception(java.lang.Object, java.lang.Exception)}.
     */
    @Test
    public void testExceptionObjectException() {
        String errorMessage = "file not found 2";

        Exception e = new FileNotFoundException(errorMessage);

        ConsoleManager.getInstance().exception(ConsoleManagerTest.class, e);

        int occurances = countOccurences("ERROR", errorMessage);

        assertEquals(occurances, 1);
    }

}
