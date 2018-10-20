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

package com.sldeditor.filter.v2.envvar.dialog;

import com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * The Class EnvVarComboBoxModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvVarComboBoxModel extends AbstractListModel<Class<?>>
        implements ComboBoxModel<Class<?>> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The list. */
    private List<Class<?>> list = new ArrayList<>();

    /** The selected item. */
    private Class<?> selectedItem = null;

    /** Instantiates a new env var combo box model. */
    public EnvVarComboBoxModel(EnvironmentManagerInterface envVarMgr) {
        if (envVarMgr != null) {
            list = envVarMgr.getEnvVarTypeList();
        }
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    @Override
    public int getSize() {
        return list.size();
    }

    /**
     * Gets the element at.
     *
     * @param index the index
     * @return the element at
     */
    @Override
    public Class<?> getElementAt(int index) {
        return list.get(index);
    }

    /**
     * Sets the selected item.
     *
     * @param anItem the new selected item
     */
    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = (Class<?>) anItem;
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
}
