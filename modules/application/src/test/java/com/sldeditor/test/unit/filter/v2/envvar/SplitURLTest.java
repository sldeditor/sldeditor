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

package com.sldeditor.test.unit.filter.v2.envvar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.filter.v2.envvar.dialog.SplitURL;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SplitURL class.
 *
 * <p>{@link com.sldeditor.filter.v2.envvar.SplitURL}
 *
 * @author Robert Ward (SCISYS)
 */
class SplitURLTest {

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.dialog.SplitURL#splitQuery(java.net.URL)}.
     */
    @Test
    void testSplitQuery() {
        try {
            String urlString =
                    "http://localhost:8080/geoserver/MyTest/wms?service=WMS&version=1.1.0&request=GetMap&layers=storename:layername&styles=&bbox=-180.0,-90.0,180.0,90.0&width=256&height=256&srs=EPSG:4326";

            Map<String, String> expectedResults = new HashMap<String, String>();
            expectedResults.put("colour", "00FF00");
            expectedResults.put("name", "triangle");
            expectedResults.put("size", "12");
            expectedResults.put("empty", "");

            StringBuilder sb = generateURL(urlString, expectedResults);

            System.out.println(sb.toString());
            URL test1 = new URL(sb.toString());

            Map<String, List<String>> actualResults = SplitURL.splitQuery(test1);

            assertFalse(actualResults.isEmpty());

            List<String> actualEnvList = actualResults.get("env");

            assertNotNull(actualEnvList);

            Map<String, String> actualEnvVarResults = SplitURL.extractEnvVar(actualEnvList);
            assertEquals(actualEnvVarResults, expectedResults);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Test URL is not valid");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("Unsupported URL encoding");
        }
    }

    private StringBuilder generateURL(String urlString, Map<String, String> expectedResults) {
        StringBuilder sb = new StringBuilder();
        sb.append(urlString);

        sb.append("&env=");
        int count = 0;
        for (String key : expectedResults.keySet()) {
            sb.append(key);
            sb.append(":");
            sb.append(expectedResults.get(key));

            if (count < expectedResults.size() - 1) {
                sb.append(";");
            }
            count++;
        }
        return sb;
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.dialog.SplitURL#splitQuery(java.net.URL)}.
     */
    @Test
    void testSplitQueryDoubleEnv() {
        try {
            String urlString =
                    "http://localhost:8080/geoserver/MyTest/wms?service=WMS&version=1.1.0&request=GetMap&layers=storename:layername&styles=&bbox=-180.0,-90.0,180.0,90.0&width=256&height=256&srs=EPSG:4326&env=";

            Map<String, String> expectedResults1 = new HashMap<String, String>();
            expectedResults1.put("colour", "00FF00");
            expectedResults1.put("name", "triangle");
            Map<String, String> expectedResults2 = new HashMap<String, String>();
            expectedResults2.put("size", "12");
            expectedResults2.put("empty", "");
            Map<String, String> expectedResults3 = new HashMap<String, String>();

            Map<String, String> expectedResults = new HashMap<String, String>();
            expectedResults.putAll(expectedResults1);
            expectedResults.putAll(expectedResults2);
            expectedResults.putAll(expectedResults3);

            StringBuilder sb = generateURL(urlString, expectedResults1);
            sb = generateURL(sb.toString(), expectedResults2);
            sb = generateURL(sb.toString(), expectedResults3);

            System.out.println(sb.toString());
            URL test1 = new URL(sb.toString());

            Map<String, List<String>> actualResults = SplitURL.splitQuery(test1);

            assertFalse(actualResults.isEmpty());

            List<String> actualEnvList = actualResults.get("env");

            assertNotNull(actualEnvList);

            Map<String, String> actualEnvVarResults = SplitURL.extractEnvVar(actualEnvList);
            assertEquals(actualEnvVarResults, expectedResults);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Test URL is not valid");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("Unsupported URL encoding");
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.dialog.SplitURL#splitQuery(java.net.URL)}.
     */
    @Test
    void testSplitQueryNull() {

        // Test methods by passing null parameters
        Map<String, List<String>> actualResults = null;
        try {
            actualResults = SplitURL.splitQuery(null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("Unsupported URL encoding");
        }
        assertTrue(actualResults.isEmpty());

        Map<String, String> actualEnvVarResults = SplitURL.extractEnvVar(null);
        assertTrue(actualEnvVarResults.isEmpty());
    }

    @Test
    void testSplitQueryDifferentURL() {
        try {
            String urlString = "http://localhost:8080/geoserver/MyTest/wms?&env=";

            Map<String, String> expectedResults = new HashMap<String, String>();
            expectedResults.put("colour", "00FF00");
            expectedResults.put("name", "triangle");
            expectedResults.put("size", "12");
            expectedResults.put("empty", "");

            StringBuilder sb = new StringBuilder();
            sb.append(urlString);

            for (String key : expectedResults.keySet()) {
                sb.append(key);
                sb.append(":");
                sb.append(expectedResults.get(key));

                sb.append(";");
            }

            // Add extra semi-colon at end of string
            System.out.println(sb.toString());
            URL test1 = new URL(sb.toString());

            Map<String, List<String>> actualResults = SplitURL.splitQuery(test1);

            assertFalse(actualResults.isEmpty());

            List<String> actualEnvList = actualResults.get("env");

            assertNotNull(actualEnvList);

            Map<String, String> actualEnvVarResults = SplitURL.extractEnvVar(actualEnvList);
            assertEquals(actualEnvVarResults, expectedResults);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Test URL is not valid");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("Unsupported URL encoding");
        }
    }
}
