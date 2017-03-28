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

package com.sldeditor.datasource.extension.filesystem.node.database;

import java.awt.datatransfer.DataFlavor;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;

/**
 * File system tree node representing a databaswe feature class.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseFeatureClassNode extends DefaultMutableTreeNode implements NodeInterface {

    /** The connection data. */
    private DatabaseConnection connectData = null;

    /** The feature class. */
    private String featureClass = null;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The handler. */
    private FileSystemInterface handler = null;

    /**
     * Instantiates a new GeoServerStyleNode.
     *
     * @param handler the handler
     * @param connectData the connect data
     * @param featureClass the feature class
     */
    public DatabaseFeatureClassNode(FileSystemInterface handler, DatabaseConnection connectData,
            String featureClass) {
        super(featureClass);
        this.handler = handler;
        this.featureClass = featureClass;
        this.connectData = connectData;
    }

    /**
     * Gets the feature class.
     *
     * @return the feature class
     */
    public String getFeatureClass() {
        return featureClass;
    }

    /**
     * Gets the connect data.
     *
     * @return the connect data
     */
    public DatabaseConnection getConnectionData() {
        return connectData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.NodeInterface#getHandler()
     */
    @Override
    public FileSystemInterface getHandler() {
        return handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.NodeInterface#getDataFlavour()
     */
    @Override
    public DataFlavor getDataFlavour() {
        return BuiltInDataFlavour.DATABASE_DATAITEM_FLAVOUR;
    }

    /**
     * Gets the destination text.
     *
     * @return the destination text
     */
    @Override
    public String getDestinationText() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.NodeInterface#getIcon()
     */
    @Override
    public Icon getIcon() {
        return null;
    }
}
