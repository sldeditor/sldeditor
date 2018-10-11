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

package com.sldeditor.test.unit.render;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.checks.CheckAttributeFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.render.RenderPanelImpl;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile3;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile4;
import java.awt.Color;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for RenderPanelImpl
 *
 * @author Robert Ward (SCISYS)
 */
class RenderPanelImplTest {

    /** Test method for {@link com.sldeditor.render.RenderPanelImpl#RenderPanelImpl()}. */
    @Test
    void testVectorData() {
        RenderPanelImpl render = new RenderPanelImpl(false);

        render.setSize(200, 200);

        RenderPanelImpl.setUnderTest(false);
        DummyInternalSLDFile3 testSLD = new DummyInternalSLDFile3();

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.connect(
                "test", SLDEditorFile.getInstance(), CheckAttributeFactory.getCheckList());

        SelectedSymbol.getInstance().setSld(testSLD.getSLD());

        render.renderSymbol();
        render.dataSourceLoaded(GeometryTypeEnum.POINT, false);
        render.dataSourceLoaded(GeometryTypeEnum.POINT, false);

        render.useAntiAliasUpdated(true);

        assertNotNull(render.getRuleRenderOptions());
        render.backgroundColourUpdate(null);
        render.backgroundColourUpdate(Color.BLUE);

        RenderPanelImpl.setUnderTest(true);
        DataSourceFactory.reset();
        SelectedSymbol.destroyInstance();
    }

    /** Test method for {@link com.sldeditor.render.RenderPanelImpl#RenderPanelImpl()}. */
    @Test
    void testRasterData() {
        RenderPanelImpl render = new RenderPanelImpl(false);

        render.setSize(200, 200);

        RenderPanelImpl.setUnderTest(false);
        DummyInternalSLDFile4 testSLD = new DummyInternalSLDFile4();

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.connect(
                "test", SLDEditorFile.getInstance(), CheckAttributeFactory.getCheckList());

        SelectedSymbol.getInstance().setSld(testSLD.getSLD());

        render.renderSymbol();
        render.dataSourceLoaded(GeometryTypeEnum.RASTER, false);
        render.dataSourceLoaded(GeometryTypeEnum.RASTER, false);

        render.useAntiAliasUpdated(true);

        assertNotNull(render.getRuleRenderOptions());
        render.backgroundColourUpdate(null);
        render.backgroundColourUpdate(Color.WHITE);

        RenderPanelImpl.setUnderTest(true);
        DataSourceFactory.reset();
        SelectedSymbol.destroyInstance();
    }
}
