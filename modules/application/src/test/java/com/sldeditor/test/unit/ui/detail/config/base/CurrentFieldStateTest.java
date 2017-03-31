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

package com.sldeditor.test.unit.ui.detail.config.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.ui.detail.config.base.CurrentFieldState;

/**
 * The unit test for CurrentFieldState.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.base.CurrentFieldState}
 *
 * @author Robert Ward (SCISYS)
 */
public class CurrentFieldStateTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.base.CurrentFieldState#getFieldEnabledState()}.
     */
    @Test
    public void testGetFieldEnabledState() {
        CurrentFieldState obj = new CurrentFieldState();
        assertTrue(obj.isGroupEnabled());
        assertTrue(obj.isGroupSelected());
        assertTrue(obj.isFieldEnabled());

        // Test group enabled
        obj.setGroupEnabled(false);
        obj.setGroupSelected(false);
        obj.setFieldEnabled(false);
        assertFalse(obj.getFieldEnabledState());

        obj.setGroupEnabled(true);
        obj.setGroupSelected(false);
        obj.setFieldEnabled(false);
        assertFalse(obj.getFieldEnabledState());

        // Group selected
        obj.setGroupSelected(false);
        obj.setFieldEnabled(true);
        assertFalse(obj.getFieldEnabledState());

        obj.setGroupSelected(true);
        assertTrue(obj.getFieldEnabledState());

        obj.setFieldEnabled(false);
        assertFalse(obj.getFieldEnabledState());
    }

}
