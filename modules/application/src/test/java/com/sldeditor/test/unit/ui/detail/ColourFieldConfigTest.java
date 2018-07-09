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

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import org.junit.Test;

/**
 * The unit test for ColourFieldConfig.
 *
 * <p>{@link com.sldeditor.ui.detail.ColourFieldConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourFieldConfigTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.ColourFieldConfig#ColourFieldConfig(com.sldeditor.common.xml.ui.FieldIdEnum,
     * com.sldeditor.common.xml.ui.FieldIdEnum, com.sldeditor.common.xml.ui.FieldIdEnum)}. Test
     * method for {@link com.sldeditor.ui.detail.ColourFieldConfig#getColour()}. Test method for
     * {@link com.sldeditor.ui.detail.ColourFieldConfig#getOpacity()}. Test method for {@link
     * com.sldeditor.ui.detail.ColourFieldConfig#getWidth()}.
     */
    @Test
    public void testColourFieldConfig() {
        GroupIdEnum groupId = GroupIdEnum.FILLCOLOUR;
        FieldIdEnum strokeFillColour = FieldIdEnum.STROKE_FILL_COLOUR;
        FieldIdEnum strokeFillOpacity = FieldIdEnum.OVERALL_OPACITY;
        FieldIdEnum strokeFillWidth = FieldIdEnum.STROKE_FILL_WIDTH;
        ColourFieldConfig config =
                new ColourFieldConfig(
                        groupId, strokeFillColour, strokeFillOpacity, strokeFillWidth);

        assertEquals(groupId, config.getGroup());
        assertEquals(strokeFillColour, config.getColour());
        assertEquals(strokeFillOpacity, config.getOpacity());
        assertEquals(strokeFillWidth, config.getWidth());
    }
}
