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

package com.sldeditor.test.unit.update;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Ignore;
import org.junit.Test;

import com.sldeditor.update.CheckUpdateGitHub;
import com.sldeditor.update.UpdateData;

/**
 * The unit test for CheckUpdateGitHub.
 * <p>{@link com.sldeditor.update.CheckUpdateGitHub}
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdateGitHubTest {

    /**
     * The Class TestCheckUpdateGitHub.
     */
    class TestCheckUpdateGitHub extends CheckUpdateGitHub
    {

        /**
         * Instantiates a new test check update git hub.
         */
        TestCheckUpdateGitHub()
        {
            super();
        }

        /**
         * Test check.
         *
         * @param json the json
         * @return the update data
         */
        public UpdateData testCheck(String json)
        {
            return check(json);
        }

        /**
         * Test read data from URL.
         *
         * @param url the url
         * @return the string
         */
        public String testReadDataFromURL(String url)
        {
            return readDataFromURL(url);
        }
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdateGitHub#getLatest()}.
     */
    @Ignore
    @Test
    public void testGetLatest() {
        // Not called because we may not have an internet connection
        CheckUpdateGitHub obj = new CheckUpdateGitHub();
        assertNull(obj.getLatest());
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdateGitHub#check(java.lang.String)}.
     */
    @Test
    public void testCheck() {

        String testFile = "/update/github.json";

        BufferedReader reader = new BufferedReader(new InputStreamReader(CheckUpdateGitHubTest.class.getResourceAsStream(testFile)));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TestCheckUpdateGitHub obj = new TestCheckUpdateGitHub();
        assertNull(obj.testCheck(null));
        assertNull(obj.testCheck(""));

        UpdateData updateData = obj.testCheck(out.toString());
        assertNotNull(updateData);
        assertEquals("0.5.0", updateData.getVersion().getVersionString());
        assertNotNull(updateData.getDescription());
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdateGitHub#getDownloadURL()}.
     */
    @Test
    public void testGetDownloadURL() {
        CheckUpdateGitHub obj = new CheckUpdateGitHub();
        assertNotNull(obj.getDownloadURL());
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdateGitHub#readDataFromURL(String)}.
     */
    @Test
    public void testReadDataFromURL() {
        TestCheckUpdateGitHub obj = new TestCheckUpdateGitHub();

        assertFalse(obj.isDestinationReached());
        assertTrue(obj.testReadDataFromURL(null).isEmpty());
        assertFalse(obj.isDestinationReached());
        assertTrue(obj.testReadDataFromURL("http://unreachable.com/test").isEmpty());
        assertFalse(obj.isDestinationReached());
    }
}
