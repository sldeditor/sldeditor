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

package com.sldeditor.test.unit.datasource.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorComboBoxModel;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile;

/**
 * Unit test for DataSourceConnectorComboBoxModel class.
 * <p>{@link com.sldeditor.datasource.connector.DataSourceConnectorComboBoxModel}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConnectorComboBoxModelTest {

    /**
     * Test method for {@link com.sldeditor.datasource.connector.DataSourceConnectorComboBoxModel#DataSourceConnectorComboBoxModel(java.util.Map)}.
     */
    @Test
    public void testDataSourceConnectorComboBoxModel() {
        Map<Class<?>, DataSourceConnectorInterface> dscMap = DataSourceConnectorFactory.getDataSourceConnectorList();

        DataSourceConnectorComboBoxModel dscModel = new DataSourceConnectorComboBoxModel(dscMap);

        assertTrue(dscModel.getSize() == dscMap.size());

        String actualItem1 = dscModel.getElementAt(0);
        String actualItem2 = dscModel.getElementAt(1);

        assertNotEquals(actualItem1, actualItem2);

        dscModel.setSelectedItem(actualItem2);

        assertEquals(actualItem2, dscModel.getSelectedItem());

        DataSourceConnectorInterface actualDsc = dscMap.get(DataSourceConnectorShapeFile.class);
        assertEquals(actualDsc, dscModel.getSelectedDSCItem());
    }

}
