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

package com.sldeditor.test.unit.datasource.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.checks.CheckAttributeFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.ExtractValidFieldTypes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The unit test for ExtractValidFieldTypes.
 *
 * @author Robert Ward (SCISYS)
 */
@Disabled
class ExtractValidFieldTypesTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.impl.ExtractValidFieldTypes#fieldTypesUpdated()}.
     */
    @Test
    void testFieldTypesUpdated() {
        SelectedSymbol.destroyInstance();

        assertFalse(ExtractValidFieldTypes.fieldTypesUpdated());

        DummyExternalSLDFileError editorFile = new DummyExternalSLDFileError();

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.connect(
                editorFile.getTypeName(), editorFile, CheckAttributeFactory.getCheckList());

        // Named Layer
        SelectedSymbol.getInstance().setSld(editorFile.getSLD());

        assertTrue(ExtractValidFieldTypes.fieldTypesUpdated());

        // User Layer
        DummyInlineSLDFile2 userLayerTestObj = new DummyInlineSLDFile2();
        SelectedSymbol.getInstance().setSld(userLayerTestObj.getSLD());

        assertFalse(ExtractValidFieldTypes.fieldTypesUpdated());

        SelectedSymbol.destroyInstance();
    }
}
