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

package com.sldeditor.test.unit.datasource.attribute;

import static org.junit.Assert.assertEquals;

import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit test for DataSourceAttributeList class.
 *
 * <p>{@link com.sldeditor.datasource.attribute.DataSourceAttributeList}
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceAttributeListTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.attribute.DataSourceAttributeList#DataSourceAttributeList()}.
     */
    @Test
    public void testDataSourceAttributeList() {

        List<DataSourceAttributeData> expectedAttributeList =
                new ArrayList<DataSourceAttributeData>();
        expectedAttributeList.add(
                new DataSourceAttributeData("field 1", String.class, "test value"));
        expectedAttributeList.add(new DataSourceAttributeData("field 2", Double.class, 42.0));
        expectedAttributeList.add(new DataSourceAttributeData("field 3", Integer.class, 22));
        expectedAttributeList.add(new DataSourceAttributeData("field 4", Long.class, 454));

        DataSourceAttributeList dsaList = new DataSourceAttributeList();
        dsaList.setData(expectedAttributeList);

        assertEquals(expectedAttributeList, dsaList.getData());
    }
}
