package com.sldeditor.update;

import java.net.URL;

public interface CheckUpdateClientInterface {

    /**
     * Gets the latest.
     *
     * @return the latest
     */
    UpdateData getLatest();

    /**
     * Get download page URL.
     */
    URL getDownloadURL();

}