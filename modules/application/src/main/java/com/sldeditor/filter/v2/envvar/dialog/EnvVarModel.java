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

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.envvar.EnvVar;
import com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface;

/**
 * The Class EnvVarModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvVarModel extends DefaultTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant COL_NAME. */
    private static final int COL_NAME = 0;

    /** The Constant COL_TYPE. */
    private static final int COL_TYPE = 1;

    /** The Constant COL_VALUE. */
    private static final int COL_VALUE = 2;

    /** The columns. */
    private static List<String> columns = new ArrayList<String>();

    /** The data list. */
    private List<EnvVar> dataList = new ArrayList<EnvVar>();

    /** The env mgr. */
    private EnvironmentManagerInterface envMgr = null;

    /**
     * Instantiates a new env var model.
     *
     * @param envMgr the env mgr
     */
    public EnvVarModel(EnvironmentManagerInterface envMgr) {
        this.envMgr = envMgr;

        if (columns.isEmpty()) {
            columns.add(Localisation.getString(EnvVarDlg.class, "EnvVarModel.name"));
            columns.add(Localisation.getString(EnvVarDlg.class, "EnvVarModel.type"));
            columns.add(Localisation.getString(EnvVarDlg.class, "EnvVarModel.value"));
        }
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    @Override
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columns.get(column);
    }

    /**
     * Checks if is cell editable.
     *
     * @param row the row
     * @param column the column
     * @return true, if is cell editable
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        EnvVar envVar = dataList.get(row);

        if (column == COL_VALUE) {
            return true;
        }
        return !envVar.isPredefined();
    }

    /**
     * Gets the value at.
     *
     * @param row the row
     * @param column the column
     * @return the value at
     */
    @Override
    public Object getValueAt(int row, int column) {
        EnvVar envVar = dataList.get(row);

        switch (column) {
        case COL_NAME:
            return envVar.getName();
        case COL_TYPE:
            return envVar.getType();
        case COL_VALUE:
            return envVar.getValue();
        default:
            break;
        }
        return null;
    }

    /**
     * Sets the value at.
     *
     * @param aValue the a value
     * @param row the row
     * @param column the column
     */
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        EnvVar envVar = dataList.get(row);

        switch (column) {
        case COL_NAME: {
            String nameValue = (String) aValue;
            nameValue = nameValue.replace(" ", "_");
            envVar.setName(nameValue);
        }
            break;
        case COL_TYPE:
            envVar.setType((Class<?>) aValue);
            break;
        case COL_VALUE:
            envVar.setValue(aValue);
            break;
        default:
            break;
        }
    }

    /**
     * Populate.
     */
    public void populate() {
        if (this.envMgr != null) {
            dataList = this.envMgr.getEnvVarList();
            this.fireTableDataChanged();
        }
    }

    /**
     * Adds the new.
     *
     * @param parameterList the parameter list
     */
    public void addNew(List<String> parameterList) {
        if (this.envMgr != null) {
            for (String parameter : parameterList) {
                String[] componentList = parameter.split(";");

                for (String component : componentList) {
                    String[] childComponentList = component.split(":");

                    String envVarName = childComponentList[0];
                    String envVarValue = null;
                    if (childComponentList.length > 1) {
                        envVarValue = childComponentList[1];
                    }

                    EnvVar envVar = this.envMgr.addNewEnvVar(envVarName, String.class, envVarValue);

                    if (envVar != null) {
                        dataList.add(envVar);
                    }
                }
            }
            this.fireTableDataChanged();
        }
    }

    /**
     * Adds the new variable.
     */
    public void addNewVariable() {
        EnvVar envVar = this.envMgr.addNewEnvVar("NewEnvVar", String.class, null);

        if (envVar != null) {
            dataList.add(envVar);
            this.fireTableDataChanged();
        }
    }

    /**
     * Removes the env var.
     *
     * @param rowIndex the row index
     */
    public void removeEnvVar(int rowIndex) {
        EnvVar envVar = dataList.get(rowIndex);

        if (this.envMgr != null) {
            this.envMgr.removeEnvVar(envVar);
        }

        dataList.remove(rowIndex);
        this.fireTableDataChanged();
    }

    /**
     * Gets the env var.
     *
     * @param rowIndex the row index
     * @return the env var
     */
    public EnvVar getEnvVar(int rowIndex) {
        if ((rowIndex < 0) || (rowIndex >= dataList.size())) {
            return null;
        }
        return dataList.get(rowIndex);
    }

    /**
     * Update env var manager.
     */
    public void updateEnvVarManager() {
        if (this.envMgr != null) {
            this.envMgr.update(dataList);
        }
    }
}
