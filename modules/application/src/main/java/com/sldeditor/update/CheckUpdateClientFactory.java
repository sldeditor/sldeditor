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

package com.sldeditor.update;

/**
 * A factory for creating CheckUpdateClient objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdateClientFactory {

    /** The git hub client. */
    private static CheckUpdateGitHub gitHubClient = new CheckUpdateGitHub();

    /** Private default constructor */
    private CheckUpdateClientFactory() {
        // Private default constructor
    }

    /**
     * Gets the client.
     *
     * @return the client
     */
    public static CheckUpdateClientInterface getClient() {
        return gitHubClient;
    }
}
