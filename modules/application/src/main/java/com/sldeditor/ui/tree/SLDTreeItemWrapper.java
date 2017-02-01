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

package com.sldeditor.ui.tree;

import org.apache.commons.lang.ObjectUtils;

/**
 * The Class SLDTreeItemWrapper, a wrapper for any class.  The hash code method 
 * allows the differentation of object instances even if the object contents are the same.
 * Need to find the difference between 2 default text symbolizers for example.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeItemWrapper {
    
    /** The sld item. */
    private Object sldItem = null;

    /**
     * Instantiates a new SLD tree item wrapper.
     *
     * @param sldItem the sld item
     */
    public SLDTreeItemWrapper(Object sldItem) {
        super();
        this.sldItem = sldItem;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sldItem == null) ? 0 : sldItem.hashCode());
        if(sldItem != null)
        {
            String string = ObjectUtils.identityToString(sldItem);
            result = prime * result + string.hashCode();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SLDTreeItemWrapper other = (SLDTreeItemWrapper) obj;
        if (sldItem == null) {
            if (other.sldItem != null) {
                return false;
            }
        } else if (!sldItem.equals(other.sldItem)) {
            return false;
        }
        return true;
    }
    

}
