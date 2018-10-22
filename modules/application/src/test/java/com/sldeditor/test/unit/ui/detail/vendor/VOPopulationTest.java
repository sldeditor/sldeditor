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

package com.sldeditor.test.unit.ui.detail.vendor;

import static org.junit.jupiter.api.Assertions.*;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.RuleDetails;
import com.sldeditor.ui.detail.vendor.DefaultOverride;
import com.sldeditor.ui.detail.vendor.VOPopulation;
import org.junit.jupiter.api.Test;

/**
 * The unit test for VOPopulation class
 *
 * @author Robert Ward (SCISYS)
 */
class VOPopulationTest {

    class TestVOPopulation extends VOPopulation {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test VO population.
         *
         * @param panelId the panel id
         */
        protected TestVOPopulation(Class<?> panelId) {
            super(panelId);

            readConfigFileNoScrollPane(
                    null, getPanelId(), null, "symbol/text/PanelConfig_Label.xml");
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.detail.vendor.VOPopulation#addOverride(com.sldeditor.common.xml.ui.
         * FieldIdEnum, com.sldeditor.ui.detail.vendor.VOPopulation.DefaultOverride)
         */
        @Override
        protected void addOverride(FieldIdEnum fieldId, DefaultOverride override) {
            super.addOverride(fieldId, override);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.sldeditor.ui.detail.vendor.VOPopulation#includeValue(com.sldeditor.common.xml.ui.
         * FieldIdEnum)
         */
        @Override
        protected boolean includeValue(FieldIdEnum field) {
            return super.includeValue(field);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.VOPopulation#VOPopulation(java.lang.Class)}.
     */
    @Test
    void testVOPopulation() {
        TestVOPopulation testObj = new TestVOPopulation(RuleDetails.class);
        String[] graphicResizeValues = {"Proportional", "Stretch"};

        testObj.addOverride(
                FieldIdEnum.LABEL_GRAPHIC_MARGIN,
                new DefaultOverride(FieldIdEnum.LABEL_GRAPHIC_RESIZE, graphicResizeValues));

        // Field does not exist
        assertFalse(testObj.includeValue(FieldIdEnum.ANCHOR_POINT_V));

        // Enum value does not exist
        assertFalse(testObj.includeValue(FieldIdEnum.LABEL_GRAPHIC_MARGIN));

        // Enum value does exist
        graphicResizeValues = new String[] {"Proportional", "Stretch", "None"};
        testObj.addOverride(
                FieldIdEnum.LABEL_GRAPHIC_MARGIN,
                new DefaultOverride(FieldIdEnum.LABEL_GRAPHIC_RESIZE, graphicResizeValues));

        assertTrue(testObj.includeValue(FieldIdEnum.LABEL_GRAPHIC_MARGIN));
    }
}
