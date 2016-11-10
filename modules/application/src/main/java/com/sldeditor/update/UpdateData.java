/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.update;

import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Class UpdateData.
 *
 * @author Robert Ward (SCISYS)
 */
public class UpdateData {

    /** The version. */
    private VersionData version;
    
    /** The description. */
    private String description;

    /**
     * Instantiates a new update data.
     *
     * @param version the version
     * @param description the description
     */
    public UpdateData(VersionData version, String description) {
        super();
        this.version = version;
        this.description = description;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public VersionData getVersion() {
        return version;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
