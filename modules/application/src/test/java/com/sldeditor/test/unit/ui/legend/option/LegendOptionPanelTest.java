/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.legend.option;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.ui.legend.option.LegendOptionData;
import com.sldeditor.ui.legend.option.LegendOptionDataUpdateInterface;
import com.sldeditor.ui.legend.option.LegendOptionPanel;
import org.junit.jupiter.api.Test;

/**
 * The unit test for LegendOptionPanel.
 *
 * <p>{@link com.sldeditor.ui.legend.option.LegendOptionPanel}
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendOptionPanelTest {

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionPanel#LegendOptionPanel()}.
     */
    @Test
    public void testLegendOptionPanel() {
        LegendOptionPanel testObj = new LegendOptionPanel();

        testObj.addListener(
                new LegendOptionDataUpdateInterface() {

                    @Override
                    public void updateLegendOptionData(LegendOptionData data) {}
                });
        LegendOptionData newOptionData = new LegendOptionData();
        testObj.updateData(newOptionData);

        LegendOptionData actualData = testObj.getData();

        assertEquals(actualData, newOptionData);
    }
}
