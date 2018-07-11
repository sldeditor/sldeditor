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

package com.sldeditor.test.unit.filter.v2.envvar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.filter.v2.envvar.EnvVar;
import com.sldeditor.filter.v2.envvar.EnvVarUpdateInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.filter.v2.envvar.WMSEnvVarValues;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for EnvironmentVariableManager class.
 *
 * <p>{@link com.sldeditor.filter.v2.envvar.EnvironmentVariableManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvironmentVariableManagerTest {

    class EnvVarNotification implements EnvVarUpdateInterface {
        private boolean notified = false;

        @Override
        public void envVarsUpdated(List<EnvVar> envVarList) {
            notified = true;
        }

        public boolean hasBeenNotified() {
            boolean tmp = notified;
            notified = false;
            return tmp;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#addEnvVarUpdatedListener(com.sldeditor.filter.v2.envvar.EnvVarUpdateInterface)}.
     */
    @Test
    public void testAddEnvVarUpdatedListener() {}

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#getInstance()}. Test method for
     * {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#addNewEnvVar(java.lang.String,
     * java.lang.Class, java.lang.String)}. Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#getEnvVarList()}. Test method for
     * {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#removeEnvVar(com.sldeditor.filter.v2.envvar.EnvVar)}.
     */
    @Test
    public void testAddNewEnvVar() {
        EnvVarNotification listener = new EnvVarNotification();

        EnvironmentVariableManager.getInstance().addEnvVarUpdatedListener(listener);
        EnvironmentVariableManager.getInstance().addEnvVarUpdatedListener(listener);

        List<EnvVar> envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();
        // CHECKSTYLE:OFF
        int preloadedCount = envVarList.size();
        // CHECKSTYLE:ON

        EnvVar actualValue1 =
                EnvironmentVariableManager.getInstance()
                        .addNewEnvVar("testAddNewEnvVar1", String.class, "testvalue1");
        assertNotNull(actualValue1);
        assertFalse(listener.hasBeenNotified());
        EnvVar actualValue2 =
                EnvironmentVariableManager.getInstance()
                        .addNewEnvVar("testAddNewEnvVar2", Integer.class, "42");
        assertNotNull(actualValue2);
        EnvVar actualValue3 =
                EnvironmentVariableManager.getInstance()
                        .addNewEnvVar("testAddNewEnvVar3", Double.class, "3.141");
        assertNotNull(actualValue3);

        // Environment variable already exists
        EnvVar actualValue4 =
                EnvironmentVariableManager.getInstance()
                        .addNewEnvVar("testAddNewEnvVar2", Boolean.class, "false");
        assertNull(actualValue4);
        assertFalse(listener.hasBeenNotified());

        envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(3, envVarList.size() - preloadedCount);

        // Now remove all the env variables
        EnvironmentVariableManager.getInstance().removeEnvVar(null);
        EnvironmentVariableManager.getInstance().removeEnvVar(actualValue2);
        EnvironmentVariableManager.getInstance().removeEnvVar(actualValue3);
        EnvironmentVariableManager.getInstance().removeEnvVar(actualValue1);
        envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(preloadedCount, envVarList.size());
        assertFalse(listener.hasBeenNotified());
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#createExpression(com.sldeditor.filter.v2.envvar.EnvVar)}.
     */
    @Test
    public void testCreateExpression() {
        EnvVar actualValue1 =
                EnvironmentVariableManager.getInstance()
                        .addNewEnvVar("testAddNewEnvVar1", String.class, "testvalue1");
        assertNotNull(actualValue1);

        assertNull(EnvironmentVariableManager.getInstance().createExpression(null));

        Expression expression =
                EnvironmentVariableManager.getInstance().createExpression(actualValue1);
        assertNotNull(expression);
        EnvironmentVariableManager.getInstance().removeEnvVar(actualValue1);
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#update(java.util.List)}.
     */
    @Test
    public void testUpdate() {
        EnvVarNotification listener = new EnvVarNotification();
        EnvironmentVariableManager.getInstance().addEnvVarUpdatedListener(listener);
        List<EnvVar> envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();
        // CHECKSTYLE:OFF
        int preloadedCount = envVarList.size();
        // CHECKSTYLE:ON

        assertFalse(listener.hasBeenNotified());
        EnvironmentVariableManager.getInstance().update(null);
        assertTrue(listener.hasBeenNotified());

        envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(preloadedCount, envVarList.size());

        // New env var
        EnvVar env1 = new EnvVar("testUpdate1", String.class, false);
        EnvVar env2 = new EnvVar("wms_width", Integer.class, false);

        List<EnvVar> newEnvVarList = new ArrayList<EnvVar>();
        newEnvVarList.add(env1);
        newEnvVarList.add(env2);

        assertFalse(listener.hasBeenNotified());
        EnvironmentVariableManager.getInstance().update(newEnvVarList);
        assertTrue(listener.hasBeenNotified());
        envVarList = EnvironmentVariableManager.getInstance().getEnvVarList();

        assertEquals(2, envVarList.size());

        // Put it back to the original
        EnvironmentVariableManager.getInstance().update(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#getEnvVarTypeList()}.
     */
    @Test
    public void testGetEnvVarTypeList() {
        List<Class<?>> typeList = EnvironmentVariableManager.getInstance().getEnvVarTypeList();

        assertNotNull(typeList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#getDataType(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testGetDataType() {
        Class<?> dataType = EnvironmentVariableManager.getInstance().getDataType(null);
        assertEquals(Object.class, dataType);

        dataType = EnvironmentVariableManager.getInstance().getDataType(ConstantExpression.TWO);
        assertEquals(Object.class, dataType);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Expression literal = ff.literal("wms_width");
        dataType = EnvironmentVariableManager.getInstance().getDataType(literal);
        assertEquals(Integer.class, dataType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#setWMSEnvVarValues(com.sldeditor.filter.v2.envvar.WMSEnvVarValues)}.
     */
    @Test
    public void testSetWMSEnvVarValues() {
        EnvironmentVariableManager.getInstance().setWMSEnvVarValues(null);

        WMSEnvVarValues wmsEnvVar = new WMSEnvVarValues();

        wmsEnvVar.setImageHeight(42);
        wmsEnvVar.setImageWidth(454);
        Envelope envelope = new Envelope(1.0, 2.0, 50.0, 51.1);
        ReferencedEnvelope mapBounds =
                ReferencedEnvelope.create(envelope, CoordManager.getInstance().getWGS84());
        wmsEnvVar.setMapBounds(mapBounds);

        EnvironmentVariableManager.getInstance().setWMSEnvVarValues(wmsEnvVar);

        // Update the values again
        WMSEnvVarValues wmsEnvVar2 = new WMSEnvVarValues();
        wmsEnvVar2.setImageHeight(69);
        wmsEnvVar2.setImageWidth(123);
        Envelope envelope2 = new Envelope(-1.0, -2.0, 50.0, 51.1);
        ReferencedEnvelope mapBounds2 =
                ReferencedEnvelope.create(envelope2, CoordManager.getInstance().getWGS84());
        wmsEnvVar2.setMapBounds(mapBounds2);
        EnvironmentVariableManager.getInstance().setWMSEnvVarValues(wmsEnvVar2);
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.envvar.EnvironmentVariableManager#showDialog()}.
     */
    @Test
    public void testShowDialog() {
        // Do nothing
    }
}
