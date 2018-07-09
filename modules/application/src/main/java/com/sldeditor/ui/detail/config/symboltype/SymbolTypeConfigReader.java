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

package com.sldeditor.ui.detail.config.symboltype;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue;
import com.sldeditor.common.xml.ui.XMLPanelDetails;
import com.sldeditor.common.xml.ui.XMLSymbolTypeConfig;
import com.sldeditor.ui.detail.config.ReadPanelConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Class that read symbol type configuration data from an XML file.
 *
 * @author Robert Ward (SCISYS)
 */
public class SymbolTypeConfigReader {

    /** The logger. */
    private static Logger logger = Logger.getLogger(SymbolTypeConfigReader.class);

    /** The Constant SCHEMA_RESOURCE. */
    private static final String SCHEMA_RESOURCE = "/xsd/symboltype.xsd";

    /**
     * Read configuration.
     *
     * @param panelId the panel id
     * @param fullResourceName the full resource name
     * @param fieldEnableMap the field enable map
     * @return true, if successful
     */
    public static boolean readConfig(
            Class<?> panelId,
            String fullResourceName,
            Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        XMLSymbolTypeConfig symbolTypeConfig =
                (XMLSymbolTypeConfig)
                        ParseXML.parseUIFile(
                                fullResourceName, SCHEMA_RESOURCE, XMLSymbolTypeConfig.class);

        if (symbolTypeConfig == null) {
            return false;
        }

        for (XMLPanelDetails xmlPanelDetails : symbolTypeConfig.getPanel()) {
            String symbolizerClassName = xmlPanelDetails.getType();
            logger.debug("Symbolizer : " + symbolizerClassName);

            Class<?> symbolizerClass;
            try {
                symbolizerClass = Class.forName(symbolizerClassName);
                List<SymbolTypeConfig> configList = readSymbolizerConfig(panelId, xmlPanelDetails);

                fieldEnableMap.put(symbolizerClass, configList);

            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(SymbolTypeConfigReader.class, e);
                return false;
            }
        }
        return true;
    }

    /**
     * Read symbolizer config.
     *
     * @param panelId the panel id
     * @param xmlPanelDetails the xml panel details
     * @return the symbol type config
     */
    private static List<SymbolTypeConfig> readSymbolizerConfig(
            Class<?> panelId, XMLPanelDetails xmlPanelDetails) {
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();

        for (XMLFieldConfigEnumValue value : xmlPanelDetails.getValue()) {

            SymbolTypeConfig config =
                    ReadPanelConfig.parseSymbolTypeConfig(SymbolTypeConfig.class, panelId, value);

            configList.add(config);
        }

        return configList;
    }
}
