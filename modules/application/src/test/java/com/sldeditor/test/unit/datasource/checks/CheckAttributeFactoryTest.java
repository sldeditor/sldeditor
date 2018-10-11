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

package com.sldeditor.test.unit.datasource.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.checks.CheckAttributeFactory;
import com.sldeditor.datasource.checks.CheckAttributeInterface;
import com.sldeditor.datasource.checks.MissingSLDAttributes;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The unit test for CheckAttributeFactory.
 *
 * @author Robert Ward (SCISYS)
 */
class CheckAttributeFactoryTest {

    /**
     * Test method for {@link com.sldeditor.datasource.checks.CheckAttributeFactory#getCheckList()}.
     */
    @Test
    void testGetCheckList() {
        CheckAttributeFactory.setOverrideCheckList(null);

        assertEquals(1, CheckAttributeFactory.getCheckList().size());
        assertEquals(
                MissingSLDAttributes.class, CheckAttributeFactory.getCheckList().get(0).getClass());

        // Increase code coverage
        @SuppressWarnings("unused")
        CheckAttributeFactory factory = new CheckAttributeFactory();
        factory = null;
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.checks.CheckAttributeFactory#setOverrideCheckList(java.util.List)}.
     */
    @Test
    void testSetOverideCheckList() {
        List<CheckAttributeInterface> checkList = new ArrayList<CheckAttributeInterface>();
        checkList.add(
                new CheckAttributeInterface() {
                    @Override
                    public void checkAttributes(SLDEditorFileInterface editorFile) {}
                });
        checkList.add(
                new CheckAttributeInterface() {
                    @Override
                    public void checkAttributes(SLDEditorFileInterface editorFile) {}
                });
        CheckAttributeFactory.setOverrideCheckList(checkList);

        assertEquals(checkList, CheckAttributeFactory.getCheckList());

        CheckAttributeFactory.setOverrideCheckList(null);
    }
}
