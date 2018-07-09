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

package com.sldeditor.tool.dbconnectionlist;

import com.sldeditor.common.data.DatabaseConnection;
import javax.swing.JPanel;

/**
 * The Interface DatabaseConnectionConfigInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DatabaseConnectionConfigInterface {

    /**
     * Sets the connection.
     *
     * @param connection the new connection
     */
    public void setConnection(DatabaseConnection connection);

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    public DatabaseConnection getConnection();

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public JPanel getPanel();
}
