/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
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

    /**
     * Gets the client.
     *
     * @return the client
     */
    public static CheckUpdateClientInterface getClient()
    {
        return gitHubClient;
    }
}
