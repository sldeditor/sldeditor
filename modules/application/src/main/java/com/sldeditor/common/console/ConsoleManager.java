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

package com.sldeditor.common.console;

import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 * Central point to which all messages/errors/exceptions are sent to appear in the console panel.
 *
 * <p>Received messages/errors/exceptions are logged as well.
 *
 * <p>Class is implemented as a singleton.
 *
 * @author Robert Ward (SCISYS)
 */
public class ConsoleManager {

    /** The singleton instance. */
    private static ConsoleManager instance = null;

    /** The panel. */
    private ConsolePanelInterface panel = new DefaultConsolePanel();

    /**
     * Gets the single instance of ConsoleManager.
     *
     * @return single instance of ConsoleManager
     */
    public static ConsoleManager getInstance() {
        if (instance == null) {
            instance = new ConsoleManager();
        }

        return instance;
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public JPanel getPanel() {
        return (JPanel) panel;
    }

    /** Make default constructor private. */
    private ConsoleManager() {
        // Private default constructor
    }

    /**
     * Error.
     *
     * @param obj the obj
     * @param errorMessage the error message
     */
    public void error(Object obj, String errorMessage) {
        Logger logger = Logger.getLogger(obj.getClass());
        logger.error(errorMessage);

        panel.addErrorMessage(errorMessage);
        System.err.println(errorMessage);
    }

    /**
     * Information.
     *
     * @param obj the obj
     * @param infoMessage the info message
     */
    public void information(Object obj, String infoMessage) {
        Logger logger = Logger.getLogger(obj.getClass());

        logger.info(infoMessage);

        panel.addMessage(infoMessage);
    }

    /**
     * Exception.
     *
     * @param clazz the clazz
     * @param e the e
     */
    public void exception(Class<?> clazz, Exception e) {

        Logger logger = Logger.getLogger(clazz);

        internalLogException(e, logger);
    }

    /**
     * Exception.
     *
     * @param obj the obj
     * @param e the e
     */
    public void exception(Object obj, Exception e) {

        Logger logger = Logger.getLogger(obj.getClass());

        internalLogException(e, logger);
    }

    /**
     * Internal log exception method.
     *
     * @param e the e
     * @param logger the logger
     */
    private void internalLogException(Exception e, Logger logger) {
        logger.error(e.getMessage());
        panel.addErrorMessage(e.getMessage());

        StackTraceElement[] stackTrace = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement t : stackTrace) {
            sb.append(t.toString());
            sb.append("\n");
        }

        logger.error(sb.toString());
    }

    /** Clear all displayed console messages. */
    public void clear() {
        panel.clear();
    }
}
