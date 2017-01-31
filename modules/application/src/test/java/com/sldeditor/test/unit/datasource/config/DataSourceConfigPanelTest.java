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

package com.sldeditor.test.unit.datasource.config;

import org.junit.Test;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.datasource.config.DataSourceConfigPanel;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * Unit test for DataSourceConfigPanel class.
 * <p>{@link com.sldeditor.datasource.config.DataSourceConfigPanel}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConfigPanelTest {

    class TestDataSourceConfigPanel extends DataSourceConfigPanel
    {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;
        
        public TestDataSourceConfigPanel()
        {
            super();
        }

        public void testAddNewField() {
            super.addNewField();
        }

        public void testRemoveField() {
            super.removeField();
        }

        public void testApplyData(UndoActionInterface parentObj) {
            super.applyData(parentObj);
        }

        public void testCancelData() {
            super.cancelData();
        }
    }
    /**
     * Test method for {@link com.sldeditor.datasource.config.DataSourceConfigPanel#DataSourceConfigPanel()}.
     */
    @Test
    public void testDataSourceConfigPanel() {
        TestDataSourceConfigPanel testObj = new TestDataSourceConfigPanel();
        
        testObj.dataSourceAboutToUnloaded(null);
        testObj.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);
        testObj.testAddNewField();
        testObj.testRemoveField();
        testObj.testApplyData(testObj);
        testObj.testCancelData();
        testObj.undoAction(null);
        testObj.redoAction(null);
        testObj.reset();
    }

}
