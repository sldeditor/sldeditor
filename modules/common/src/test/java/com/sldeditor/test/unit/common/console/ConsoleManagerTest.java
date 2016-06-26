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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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
     * Have to write all the messages to a file and then check them all.  If you do it individually 
     * there is no guarantee log4j has written the log files and flushing doesn't seem to work.
     * 
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#error(java.lang.Object, java.lang.String)}.
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#warn(java.lang.Object, java.lang.String)}.
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#information(java.lang.Object, java.lang.String)}.
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#exception(java.lang.Object, java.lang.String)}.
     * Test method for {@link com.sldeditor.common.console.ConsoleManager#exception(java.lang.Class<?>, java.lang.String)}.
     */
    @Test
    public void testWarnErrorInfoException() {
        Logger logger = Logger.getLogger(getClass());
        logger.setLevel(Level.DEBUG);
        String errorMessage1 = "errorMessage";
        ConsoleManager.getInstance().error(this, errorMessage1);

        String infoMessage = "information message";
        ConsoleManager.getInstance().information(this, infoMessage);

        String exceptionMessage1 = "file not found 1";

        Exception e = new FileNotFoundException(exceptionMessage1);

        ConsoleManager.getInstance().exception(this, e);

        String exceptionMessage2 = "file not found 2";

        Exception e2 = new FileNotFoundException(exceptionMessage2);

        ConsoleManager.getInstance().exception(ConsoleManagerTest.class, e2);

        int occurances = countOccurences("INFO", infoMessage);

        assertEquals(1, occurances);

        occurances = countOccurences("ERROR", errorMessage1);

        assertEquals(1, occurances);
        
        occurances = countOccurences("ERROR", exceptionMessage1);

        assertEquals(1, occurances);

        occurances = countOccurences("ERROR", exceptionMessage2);

        assertEquals(1, occurances);

        occurances = countOccurences("ERROR", "Does not exist");

        assertEquals(0, occurances);
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

        f.deleteOnExit();

        return count;
    }
}
