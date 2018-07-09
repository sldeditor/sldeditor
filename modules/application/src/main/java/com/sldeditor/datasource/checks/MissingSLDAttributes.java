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

package com.sldeditor.datasource.checks;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.impl.DataSourceImpl;
import com.sldeditor.datasource.impl.ExtractAttributes;
import java.util.List;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * The Class MissingSLDAttributes.
 *
 * @author Robert Ward (SCISYS)
 */
public class MissingSLDAttributes implements CheckAttributeInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.impl.CheckAttributeInterface#checkAttributes(com.sldeditor.datasource.SLDEditorFileInterface)
     */
    @Override
    public void checkAttributes(SLDEditorFileInterface editorFile) {
        ExtractAttributes extract = new ExtractAttributes();
        StyledLayerDescriptor sld = editorFile.getSLD();
        extract.extractDefaultFields(sld);
        List<DataSourceAttributeData> sldFieldList = extract.getFields();

        List<DataSourceAttributeData> dataSourceList = editorFile.getSLDData().getFieldList();

        for (DataSourceAttributeData sldField : sldFieldList) {
            if (!dataSourceList.contains(sldField)) {
                ConsoleManager.getInstance()
                        .error(
                                this,
                                Localisation.getField(
                                                DataSourceImpl.class,
                                                "DataSourceImpl.missingAttribute")
                                        + " "
                                        + sldField.getName());
            }
        }
    }
}
