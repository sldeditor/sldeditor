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

package com.sldeditor.test.unit.tool.layerstyle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.tool.layerstyle.GeoServerStyleTree;
import com.sldeditor.tool.layerstyle.SelectedStyleInterface;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * The Class GeoServerStyleTreeTest, unit test for GeoServerStyleTree
 *
 * @author Robert Ward (SCISYS)
 */
class GeoServerStyleTreeTest {

    class TestGeoServerStyleTree extends GeoServerStyleTree {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test geo server style tree.
         *
         * @param parent the parent
         */
        public TestGeoServerStyleTree(SelectedStyleInterface parent) {
            super(parent);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.layerstyle.GeoServerStyleTree#clearSelection()
         */
        @Override
        protected void clearSelection() {
            super.clearSelection();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.layerstyle.GeoServerStyleTree#select(com.sldeditor.common.data.
         * StyleWrapper)
         */
        @Override
        protected void select(StyleWrapper styleWrapper) {
            super.select(styleWrapper);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.layerstyle.GeoServerStyleTree#apply()
         */
        @Override
        protected void apply() {
            super.apply();
        }

        public boolean isApplyButtonEnabled() {
            return btnApply.isEnabled();
        }

        public boolean isRevertButtonEnabled() {
            return btnRevert.isEnabled();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.layerstyle.GeoServerStyleTree#revert()
         */
        @Override
        protected void revert() {
            super.revert();
        }

        /**
         * Select style.
         *
         * @param styleWrapper the style wrapper
         */
        public void selectStyle(StyleWrapper styleWrapper) {
            selectStyleInTree(styleWrapper);
        }
    }

    class TestSelectedStyleInterface implements SelectedStyleInterface {
        public StyleWrapper selectedStyle = null;

        /*
         * (non-Javadoc)
         *
         * @see
         * com.sldeditor.tool.layerstyle.SelectedStyleInterface#selectedStyle(com.sldeditor.common.
         * data.StyleWrapper)
         */
        @Override
        public void selectedStyle(StyleWrapper styleWrapper) {
            selectedStyle = styleWrapper;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.layerstyle.GeoServerStyleTree#populate(java.lang.String, java.util.Map)}.
     */
    @Test
    void testPopulate() {
        TestSelectedStyleInterface receiver = new TestSelectedStyleInterface();

        TestGeoServerStyleTree testObj = new TestGeoServerStyleTree(receiver);

        testObj.initialise();
        assertNotNull(testObj.getTree());

        Map<String, List<StyleWrapper>> styleMap = new HashMap<String, List<StyleWrapper>>();

        String workspace1 = "workspace1";
        String workspace2 = "workspace2";
        String workspace3 = "workspace3";

        StyleWrapper expectedStyleWrapper = new StyleWrapper(workspace1, "b.sld");
        styleMap.put(
                workspace1,
                Arrays.asList(
                        new StyleWrapper(workspace1, "a.sld"),
                        expectedStyleWrapper,
                        new StyleWrapper(workspace1, "c.sld")));
        StyleWrapper expectedStyleWrapper2 = new StyleWrapper(workspace2, "aa.sld");
        styleMap.put(workspace2, Arrays.asList(expectedStyleWrapper2));
        StyleWrapper expectedStyleWrapper3 = new StyleWrapper(workspace3, "bbb.sld");
        styleMap.put(
                workspace3,
                Arrays.asList(new StyleWrapper(workspace3, "aaa.sld"), expectedStyleWrapper3));

        String geoserverName = "GeoServer";
        testObj.populate(geoserverName, null);

        testObj.populate(null, styleMap);

        testObj.populate(geoserverName, styleMap);

        assertFalse(testObj.isApplyButtonEnabled());
        assertFalse(testObj.isRevertButtonEnabled());

        // Select style
        assertNull(testObj.getSelectedStyle());

        testObj.select(null);
        assertNull(testObj.getSelectedStyle());

        testObj.select(expectedStyleWrapper);
        assertEquals(expectedStyleWrapper, testObj.getSelectedStyle());

        // Apply
        assertNull(receiver.selectedStyle);
        testObj.apply();
        assertNotNull(receiver.selectedStyle);
        receiver.selectedStyle = null;

        // Clear selection
        testObj.clearSelection();
        assertNull(testObj.getSelectedStyle());

        testObj.apply();
        assertNull(receiver.selectedStyle);

        // Select some invalid styles
        testObj.select(new StyleWrapper("Invalid", "c.sld"));
        assertNull(testObj.getSelectedStyle());

        testObj.select(new StyleWrapper(workspace3, "invalid.sld"));
        assertNull(testObj.getSelectedStyle());

        // Test revert
        testObj.select(expectedStyleWrapper2);
        assertEquals(expectedStyleWrapper2, testObj.getSelectedStyle());

        testObj.selectStyle(expectedStyleWrapper3);
        assertEquals(expectedStyleWrapper3, testObj.getSelectedStyle());
        testObj.revert();
        assertEquals(expectedStyleWrapper2, testObj.getSelectedStyle());
    }
}
