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

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.vendoroption.VersionData;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The Class CheckUpdate.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdate {

    /** The client. */
    private CheckUpdateClientInterface client = null;

    /** The latest data. */
    private UpdateData latestData = null;

    /**
     * Instantiates a new check update.
     *
     * @param client the client
     */
    public CheckUpdate(CheckUpdateClientInterface client) {
        this.client = client;
    }

    /**
     * Should update.
     *
     * @param currentVersion the current version
     * @return true, if successful
     */
    public boolean shouldUpdate(String currentVersion) {

        if (client != null) {
            latestData = client.getLatest();

            if (latestData != null) {
                VersionData current =
                        VersionData.decode(
                                latestData.getVersion().getVendorOptionType(), currentVersion);

                int result = current.compareTo(latestData.getVersion());

                return (result < 0);
            }
        }
        return false;
    }

    /**
     * Gets the latest data.
     *
     * @return the latestData
     */
    public UpdateData getLatestData() {
        return latestData;
    }

    /** Show download page. */
    public void showDownloadPage() {
        if (client != null) {
            URL url = client.getDownloadURL();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(url.toURI());
                } catch (IOException | URISyntaxException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
        }
    }

    /**
     * Checks if is destination reached.
     *
     * @return the destinationReached flag
     */
    public boolean isDestinationReached() {
        if (client != null) {
            return client.isDestinationReached();
        }
        return false;
    }
}
