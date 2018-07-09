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

package com.sldeditor.test.unit.common.undo;

import com.sldeditor.common.PopulatingInterface;

/**
 * The Class DummyPopulating.
 *
 * @author Robert Ward (SCISYS)
 */
class DummyPopulating implements PopulatingInterface {

    /** The populating. */
    private boolean populating = false;

    /* (non-Javadoc)
     * @see com.sldeditor.common.PopulatingInterface#isPopulating()
     */
    @Override
    public boolean isPopulating() {
        return populating;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.PopulatingInterface#setPopulating(boolean)
     */
    @Override
    public void setPopulating(boolean populating) {
        this.populating = populating;
    }
}
