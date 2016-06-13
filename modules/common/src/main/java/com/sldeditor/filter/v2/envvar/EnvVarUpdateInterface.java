/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.filter.v2.envvar;

import java.util.List;

/**
 * The Interface EnvVarUpdateInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface EnvVarUpdateInterface {

    /**
     * Environment variables updated.
     *
     * @param envVarList the env var list
     */
    void envVarsUpdated(List<EnvVar> envVarList);
}
