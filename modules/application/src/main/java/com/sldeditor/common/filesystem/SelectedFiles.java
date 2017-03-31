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

package com.sldeditor.common.filesystem;

import java.util.ArrayList;
import java.util.List;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.GeoServerConnection;

/**
 * The Class SelectedFiles.
 *
 * @author Robert Ward (SCISYS)
 */
public class SelectedFiles {

    /** The list of sld data. */
    List<SLDDataInterface> sldData = new ArrayList<SLDDataInterface>();

    /** The is data source. */
    boolean isDataSource = false;

    /** The is folder flag. */
    private boolean isFolder = false;

    /** The folder name. */
    private String folderName;

    /** The connection data. */
    private GeoServerConnection connectionData = null;

    /**
     * Instantiates a new selected files.
     */
    public SelectedFiles() {
    }

    /**
     * Gets the sld data.
     *
     * @return the sldData
     */
    public List<SLDDataInterface> getSldData() {
        return sldData;
    }

    /**
     * Sets the sld data.
     *
     * @param sldData the sldData to set
     */
    public void setSldData(List<SLDDataInterface> sldData) {
        this.sldData = sldData;
    }

    /**
     * Checks if is data source.
     *
     * @return the isDataSource
     */
    public boolean isDataSource() {
        return isDataSource;
    }

    /**
     * Sets the data source.
     *
     * @param isDataSource the isDataSource to set
     */
    public void setDataSource(boolean isDataSource) {
        this.isDataSource = isDataSource;
    }

    /**
     * Sets the checks if is folder.
     *
     * @param folder the new checks if is folder
     */
    public void setIsFolder(boolean folder) {
        isFolder = folder;
    }

    /**
     * Returns the is folder flags.
     *
     * @return true, if is folder
     */
    public boolean isFolder() {
        return isFolder;
    }

    /**
     * Gets the folder name.
     *
     * @return the folderName
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Sets the folder name.
     *
     * @param folderName the folderName to set
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Gets the connection data.
     *
     * @return the connectionData
     */
    public GeoServerConnection getConnectionData() {
        return connectionData;
    }

    /**
     * Sets the connection data.
     *
     * @param connectionData the connectionData to set
     */
    public void setConnectionData(GeoServerConnection connectionData) {
        this.connectionData = connectionData;
    }

    /**
     * Append SLD data.
     *
     * @param newDataList the new data list
     */
    public void appendSLDData(List<SLDDataInterface> newDataList) {
        sldData.addAll(newDataList);
    }

}
