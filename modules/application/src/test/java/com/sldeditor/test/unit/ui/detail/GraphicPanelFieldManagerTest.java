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

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;

/**
 * The unit test for GraphicPanelFieldManager.
 * 
 * <p>{@link com.sldeditor.ui.detail.GraphicPanelFieldManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class GraphicPanelFieldManagerTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#GraphicPanelFieldManager(java.lang.Class)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#get(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#get(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#getFieldEnum(java.lang.Class, com.sldeditor.ui.detail.config.FieldConfigBase)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#getComponentId()}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#addField(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#add(com.sldeditor.ui.detail.config.FieldId, com.sldeditor.ui.detail.config.FieldConfigBase)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#getData(java.lang.Class, com.sldeditor.ui.detail.config.FieldId)}.
     */
    @Test
    public void testGraphicPanelFieldManager() {
        // Try with a null panel id
        GraphicPanelFieldManager mgr = new GraphicPanelFieldManager(null);
        assertNull(mgr.get(FieldIdEnum.FILTER));

        Class<?> expectedPanelId = StrokeDetails.class;
        mgr = new GraphicPanelFieldManager(expectedPanelId);

        assertEquals(FieldIdEnum.UNKNOWN, mgr.getFieldEnum(PointFillDetails.class, null));
        assertEquals(expectedPanelId, mgr.getComponentId());

        // Now try with a field
        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        FieldConfigString stringField = new FieldConfigString(
                new FieldConfigCommonData(String.class, expectedFieldId, "test label", false),
                "button text");

        mgr.addField(null);
        mgr.addField(stringField);
        FieldIdEnum actualFieldId = mgr.getFieldEnum(expectedPanelId, stringField);
        assertEquals(expectedFieldId, actualFieldId);
        assertNull(mgr.getData(null, null));
        assertNull(mgr.getData(expectedPanelId, null));
        assertEquals(stringField, mgr.getData(expectedPanelId, actualFieldId));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#getFields(java.lang.Class)}.
     */
    @Test
    public void testGetFields() {
        GraphicPanelFieldManager mgr = new GraphicPanelFieldManager(null);
        assertTrue(mgr.getFields(null).isEmpty());

        Class<?> expectedPanelId = StrokeDetails.class;
        mgr = new GraphicPanelFieldManager(expectedPanelId);

        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        FieldConfigString stringField = new FieldConfigString(
                new FieldConfigCommonData(String.class, expectedFieldId, "test label", false),
                "button text");
        mgr.addField(stringField);
        assertTrue(mgr.getFields(FieldConfigBoolean.class).isEmpty());

        assertEquals(1, mgr.getFields(FieldConfigString.class).size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#add(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testAddGraphicPanelFieldManager() {

        // Set up manager 1
        Class<?> expectedPanelId = StrokeDetails.class;
        GraphicPanelFieldManager mgr1 = new GraphicPanelFieldManager(expectedPanelId);

        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        FieldConfigString stringField = new FieldConfigString(
                new FieldConfigCommonData(String.class, expectedFieldId, "test label", false),
                "button text");
        mgr1.addField(stringField);
        assertTrue(mgr1.getFields(FieldConfigBoolean.class).isEmpty());

        assertEquals(1, mgr1.getFields(FieldConfigString.class).size());

        // Set up manager 2
        Class<?> expectedPanelId2 = PointFillDetails.class;
        GraphicPanelFieldManager mgr2 = new GraphicPanelFieldManager(expectedPanelId2);

        FieldIdEnum expectedFieldId2 = FieldIdEnum.ANGLE;
        FieldConfigDouble doubleField = new FieldConfigDouble(
                new FieldConfigCommonData(Double.class, expectedFieldId2, "test label", false));
        mgr2.addField(doubleField);
        assertEquals(1, mgr2.getFields(FieldConfigDouble.class).size());

        // Add manager 2 to manager 1
        mgr1.add(mgr2);

        FieldIdEnum actualFieldId = mgr1.getFieldEnum(expectedPanelId, stringField);
        assertEquals(expectedFieldId, actualFieldId);

        actualFieldId = mgr1.getFieldEnum(expectedPanelId2, doubleField);
        assertEquals(expectedFieldId2, actualFieldId);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#addMultiOptionGroup(com.sldeditor.ui.detail.config.base.MultiOptionGroup)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#getMultiOptionGroup(java.lang.Class, com.sldeditor.common.xml.ui.GroupIdEnum)}.
     */
    @Test
    public void testAddMultiOptionGroup() {
        Class<?> expectedPanelId = StrokeDetails.class;
        GraphicPanelFieldManager mgr = new GraphicPanelFieldManager(expectedPanelId);

        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        FieldConfigString stringField = new FieldConfigString(
                new FieldConfigCommonData(String.class, expectedFieldId, "test label", false),
                "button text");
        mgr.addField(stringField);

        MultiOptionGroup multiOption = new MultiOptionGroup();
        GroupIdEnum expectedGroupId = GroupIdEnum.FILLSYMBOL;
        multiOption.setId(expectedGroupId);
        mgr.addMultiOptionGroup(multiOption);

        MultiOptionGroup actualValue = mgr.getMultiOptionGroup(null, null);
        assertNull(actualValue);

        actualValue = mgr.getMultiOptionGroup(expectedPanelId, expectedGroupId);
        assertEquals(multiOption, actualValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#getGroup(java.lang.Class, com.sldeditor.common.xml.ui.GroupIdEnum)}.
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#addGroup(com.sldeditor.ui.detail.config.base.GroupConfig)}.
     */
    @Test
    public void testAddGroup() {
        Class<?> expectedPanelId = StrokeDetails.class;
        GraphicPanelFieldManager mgr = new GraphicPanelFieldManager(expectedPanelId);

        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        FieldConfigString stringField = new FieldConfigString(
                new FieldConfigCommonData(String.class, expectedFieldId, "test label", false),
                "button text");
        mgr.addField(stringField);

        GroupConfig multiOption = new GroupConfig();
        GroupIdEnum expectedGroupId = GroupIdEnum.FILLSYMBOL;
        multiOption.setId(expectedGroupId);
        mgr.addGroup(multiOption);

        GroupConfigInterface actualValue = mgr.getGroup(null, null);
        assertNull(actualValue);

        actualValue = mgr.getGroup(expectedPanelId, expectedGroupId);
        assertEquals(multiOption, actualValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.GraphicPanelFieldManager#removeField(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testRemoveField() {
        Class<?> expectedPanelId = StrokeDetails.class;
        GraphicPanelFieldManager mgr = new GraphicPanelFieldManager(expectedPanelId);

        FieldIdEnum expectedFieldId = FieldIdEnum.NAME;
        FieldConfigString stringField = new FieldConfigString(
                new FieldConfigCommonData(String.class, expectedFieldId, "test label", false),
                "button text");

        mgr.removeField(null);
        // Does n't exists yet
        mgr.removeField(stringField);

        mgr.addField(stringField);
        FieldIdEnum actualFieldId = mgr.getFieldEnum(expectedPanelId, stringField);
        assertEquals(stringField, mgr.getData(expectedPanelId, actualFieldId));

        mgr.removeField(stringField);
        actualFieldId = mgr.getFieldEnum(expectedPanelId, stringField);
        assertEquals(FieldIdEnum.UNKNOWN, actualFieldId);
    }

}
