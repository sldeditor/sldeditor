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

import com.sldeditor.filter.v2.envvar.EnvVar;
import com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.filter.v2.envvar.dialog.EnvVarDlg;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for EnvVarDlg class.
 *
 * <p>{@link com.sldeditor.filter.v2.envvar.EnvVarDlg}
 *
 * @author Robert Ward (SCISYS)
 */
class EnvVarDlgTest {

    class TestEnvVarDlg extends EnvVarDlg {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test env var dlg.
         *
         * @param envVarMgr the env var mgr
         */
        public TestEnvVarDlg(EnvironmentManagerInterface envVarMgr) {
            super(envVarMgr);
        }

        public void testShowDialog() {
            internalShowDialog();
        }

        /** Ok button pressed. */
        public void testOKButtonPressed() {
            okButtonPressed();
        }

        /** Decode button pressed. */
        public void testDecodeButtonPressed(String url) {
            textField.setText(url);
            decodeButtonPressed();
        }

        /** Adds the button pressed. */
        public void testAddButtonPressed() {
            addButtonPressed();
        }

        /** Removes the button pressed. */
        public void testRemoveButtonPressed() {
            table.setRowSelectionInterval(0, 0);
            removeButtonPressed();
        }
    }

    @BeforeEach
    void beforeEachTest() {
        EnvironmentVariableManager.destroyInstance();
    }

    @AfterEach
    void afterEachTest() {
        EnvironmentVariableManager.destroyInstance();
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.dialog.EnvVarDlg#EnvVarDlg(com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface)}.
     */
    @Test
    void testEnvVarDlgDecode() {

        TestEnvVarDlg testObj = new TestEnvVarDlg(EnvironmentVariableManager.getInstance());

        testObj.testShowDialog();

        List<EnvVar> originalEnvVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        String urlString =
                "http://localhost:8080/geoserver/MyTest/wms?service=WMS&version=1.1.0&request=GetMap&layers=storename:layername&styles=&bbox=-180.0,-90.0,180.0,90.0&width=256&height=256&srs=EPSG:4326";

        Map<String, String> expectedResults = new HashMap<String, String>();
        expectedResults.put("colour", "00FF00");
        expectedResults.put("name", "triangle");
        expectedResults.put("size", "12");
        expectedResults.put("empty", "");

        StringBuilder sb = generateURL(urlString, expectedResults);

        testObj.testDecodeButtonPressed(sb.toString());

        List<EnvVar> envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(envVarList.size() - originalEnvVarList.size(), expectedResults.size());
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.dialog.EnvVarDlg#EnvVarDlg(com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface)}.
     */
    @Test
    void testEnvVarDlgAddNew() {

        TestEnvVarDlg testObj = new TestEnvVarDlg(EnvironmentVariableManager.getInstance());

        testObj.testShowDialog();

        List<EnvVar> originalEnvVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        testObj.testAddButtonPressed();

        List<EnvVar> envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(envVarList.size() - originalEnvVarList.size(), 1);

        testObj.testOKButtonPressed();
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.dialog.EnvVarDlg#EnvVarDlg(com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface)}.
     */
    @Test
    void testEnvVarDlgRemove() {

        TestEnvVarDlg testObj = new TestEnvVarDlg(EnvironmentVariableManager.getInstance());

        testObj.testShowDialog();

        List<EnvVar> originalEnvVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        testObj.testRemoveButtonPressed();

        List<EnvVar> envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(envVarList.size(), originalEnvVarList.size() - 1);

        testObj.testOKButtonPressed();
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
}
