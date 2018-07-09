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

package com.sldeditor.test.unit.render;

import com.sldeditor.render.RendererErrors;
import java.io.FileNotFoundException;
import org.geotools.renderer.RenderListener;
import org.junit.Test;

/**
 * Unit test for RendererErrors class.
 *
 * <p>{@link com.sldeditor.render.RendererErrors}
 *
 * @author Robert Ward (SCISYS)
 */
public class RendererErrorsTest {

    /** Test method for {@link com.sldeditor.render.RendererErrors#RendererErrors(boolean)}. */
    @Test
    public void testRendererErrors() {
        RenderListener instance1 = RendererErrors.getInstance();
        RenderListener instance2 = RendererErrors.getInstance();
        String exceptionMessage1 = "test exception";

        Exception e = new FileNotFoundException(exceptionMessage1);

        instance1.errorOccurred(null);
        instance1.errorOccurred(e);
        instance2.errorOccurred(e);
        instance1.featureRenderer(null);
        instance2.featureRenderer(null);
    }
}
