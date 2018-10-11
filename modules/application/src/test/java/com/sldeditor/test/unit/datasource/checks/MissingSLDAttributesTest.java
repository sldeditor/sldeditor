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

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.checks.MissingSLDAttributes;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.impl.DataSourceProperties;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * The unit test for MissingSLDAttributes.
 *
 * @author Robert Ward (SCISYS)
 */
class MissingSLDAttributesTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.checks.MissingSLDAttributes#checkAttributes(com.sldeditor.datasource.SLDEditorFileInterface)}.
     */
    @Test
    void testCheckAttributes() {
        MissingSLDAttributes obj = new MissingSLDAttributes();

        obj.checkAttributes(null);

        DummyInternalSLDFile3 testSLD = new DummyInternalSLDFile3();
        SLDDataInterface sldData = testSLD.getSLDData();

        SLDEditorFile.destroyInstance();
        SelectedSymbol.destroyInstance();

        SelectedSymbol.getInstance().setSld(testSLD.getSLD());
        SLDEditorFile editorFile = SLDEditorFile.getInstance();

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put("field1", "value1");
        propertyMap.put("field2", "value2");
        propertyMap.put("field3", "value3");

        DataSourceConnectorInterface dsc = new DataSourceConnector();
        DataSourceProperties dsp = new DataSourceProperties(dsc);

        dsp.setPropertyMap(propertyMap);

        List<DataSourceAttributeData> fieldList = new ArrayList<DataSourceAttributeData>();
        fieldList.add(new DataSourceAttributeData("Field 1", String.class, null));
        fieldList.add(new DataSourceAttributeData("Field 2", Double.class, null));
        fieldList.add(new DataSourceAttributeData("Field 3", Double.class, null));

        // Try when no fields have been specified
        obj.checkAttributes(editorFile);

        editorFile.setSLDData(sldData);
        obj.checkAttributes(editorFile);

        // Specify the field list
        sldData.setFieldList(fieldList);
        sldData.setDataSourceProperties(dsp);
        editorFile.setDataSource(dsp);
        obj.checkAttributes(editorFile);

        // Tidy up
        SLDEditorFile.destroyInstance();
        SelectedSymbol.destroyInstance();
    }
}
