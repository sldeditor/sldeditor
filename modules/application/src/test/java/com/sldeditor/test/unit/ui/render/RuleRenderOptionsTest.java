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

package com.sldeditor.test.unit.ui.render;

import static org.junit.Assert.assertTrue;

import com.sldeditor.ui.render.RuleRenderOptions;
import org.junit.Test;

/**
 * The unit test for RuleRenderOptions.
 *
 * <p>{@link com.sldeditor.ui.render.RuleRenderOptions}
 *
 * @author Robert Ward (SCISYS)
 */
public class RuleRenderOptionsTest {

    @Test
    public void test() {
        RuleRenderOptions options = new RuleRenderOptions();

        assertTrue(options.isTransformationApplied() == false);

        options.setApplyTransformation(true);
        assertTrue(options.isTransformationApplied() == true);
    }
}
