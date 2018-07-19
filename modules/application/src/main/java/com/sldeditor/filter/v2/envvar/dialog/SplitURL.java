/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.filter.v2.envvar.dialog;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility method to extract the environment variables from a URL
 *
 * @author Robert Ward (SCISYS)
 */
public class SplitURL {

    /**
     * Split query.
     *
     * @param url the url
     * @return the map
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static Map<String, List<String>> splitQuery(URL url)
            throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();

        if (url != null) {
            final String[] pairs = url.getQuery().split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key =
                        idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value =
                        idx > 0 && pair.length() > idx + 1
                                ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                                : null;
                if (value != null) {
                    query_pairs.get(key).add(value);
                }
            }
        }
        return query_pairs;
    }

    /**
     * Extract environment variables from a string in the form : <key>:<value>[;].
     *
     * @param parameterList the parameter list
     * @return the map
     */
    public static Map<String, String> extractEnvVar(List<String> parameterList) {
        Map<String, String> envVarMap = new HashMap<String, String>();

        if (parameterList != null) {
            for (String parameter : parameterList) {
                String[] componentList = parameter.split(";");

                for (String component : componentList) {
                    String[] childComponentList = component.split(":");

                    String envVarName = childComponentList[0];
                    String envVarValue = "";
                    if (childComponentList.length > 1) {
                        envVarValue = childComponentList[1];
                    }

                    envVarMap.put(envVarName, envVarValue);
                }
            }
        }
        return envVarMap;
    }
}
