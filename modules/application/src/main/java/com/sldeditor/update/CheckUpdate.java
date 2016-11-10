/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.update;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Class CheckUpdate.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdate {

    /** The client. */
    private CheckUpdateClientInterface client = null;
    private UpdateData latestData;

    public CheckUpdate(CheckUpdateClientInterface client) {
        this.client = client;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        CheckUpdateClientInterface github = new CheckUpdateGitHub();
        CheckUpdate update = new CheckUpdate(github);

        if(update.shouldUpdate("0.4.2"))
        {
            UpdateData latestData = update.getLatestData();
            System.out.println(latestData.getVersion());
            System.out.println(latestData.getDescription());
        }
    }

    /**
     * Should update.
     *
     * @param latest the latest
     * @param currentVersion the current version
     * @return true, if successful
     */
    public boolean shouldUpdate(String currentVersion) {

        if(client != null)
        {
            latestData = client.getLatest();

            VersionData current = VersionData.decode(getClass(), currentVersion);

            int result = current.compareTo(latestData.getVersion());

            return (result == -1);
        }
        return false;
    }

    /**
     * @return the latestData
     */
    public UpdateData getLatestData() {
        return latestData;
    }

    /**
     * Show download page.
     */
    public void showDownloadPage() {
        if(client != null)
        {
            URL url = client.getDownloadURL();

            if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(url.toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
