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

package com.sldeditor.test.unit.tool.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.tool.scale.ScalePanelUtils;
import com.sldeditor.tool.scale.ScaleSLDData;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The unit test for ScalePanelUtils.
 *
 * <p>{@link com.sldeditor.tool.scale.ScalePanelUtils}
 *
 * @author Robert Ward (SCISYS)
 */
class ScalePanelUtilsTest {

    /**
     * Test method for {@link
     * com.sldeditor.tool.scale.ScalePanelUtils#containsScales(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    void testContainsScales() {
        DummyScaleSLDFile testData = new DummyScaleSLDFile();

        List<ScaleSLDData> expectedList = testData.getExpectedScaleList();
        List<ScaleSLDData> actualList = ScalePanelUtils.containsScales(testData.getSLDData());

        assertEquals(actualList.size(), expectedList.size());

        for (int index = 0; index < actualList.size(); index++) {
            ScaleSLDData actual = actualList.get(index);
            assertTrue(isSame(actual, expectedList.get(index)));

            assertNotNull(actual.getFeatureTypeStyle());
            assertNotNull(actual.getStyle());
            assertNotNull(actual.getRule());
            assertNull(actual.getWorkspace());
            assertNotNull(actual.getNamedLayer());
            assertNotNull(actual.getName());
            if (actual.isMinScaleSet()) {
                assertNotNull(actual.getMinScaleString());
            }
            if (actual.isMaxScaleSet()) {
                assertNotNull(actual.getMaxScaleString());
            }
        }

        assertNull(ScalePanelUtils.containsScales(null));
    }

    /**
     * Checks if is same.
     *
     * @param actual the actual
     * @param expected the expected
     * @return the object
     */
    private boolean isSame(ScaleSLDData actual, ScaleSLDData expected) {
        boolean result = true;

        if (actual == null || expected == null) {
            result = false;
        } else {
            result = (actual.getRuleName().equals(expected.getRuleName()));

            if (result) {
                result = (actual.isMinScaleSet() == expected.isMinScaleSet());
            }

            if (result) {
                if (actual.isMinScaleSet()) {
                    if (!(Math.abs(actual.getMinScale() - expected.getMinScale()) < 0.0001)) {
                        result = false;
                    }
                }
            }

            if (result) {
                result = (actual.isMaxScaleSet() == expected.isMaxScaleSet());
            }

            if (result) {
                if (actual.isMaxScaleSet()) {
                    if (!(Math.abs(actual.getMaxScale() - expected.getMaxScale()) < 0.0001)) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }
}
