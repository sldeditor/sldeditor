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
package com.sldeditor.importdata.esri;

import org.apache.log4j.Logger;

/**
 * The Class Progress.
 *
 * @author Robert Ward (SCISYS)
 */
public class Progress {

    /** The dlg. */
    private static ProgressDialog dlg = null;

    /**
     * Creates the ui.
     */
    public static void createUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dlg = new ProgressDialog();

                dlg.setVisible(true);
            }
        });
    }

    /**
     * Info.
     *
     * @param sourceClass the source class
     * @param infoString the info string
     */
    public static void info(Class<?> sourceClass, String infoString) {
        Logger logger = Logger.getLogger(sourceClass);
        logger.info(infoString);

        if(dlg != null)
        {
            dlg.addInfoMessage(infoString);
        }
    }

    /**
     * Error.
     *
     * @param sourceClass the source class
     * @param errorString the error string
     */
    public static void error(Class<?> sourceClass, String errorString) {
        Logger logger = Logger.getLogger(sourceClass);
        logger.error(errorString);

        if(dlg != null)
        {
            dlg.addErrorMessage(errorString);
        }
    }

    /**
     * Warn.
     *
     * @param sourceClass the source class
     * @param errorString the warn string
     */
    public static void warn(Class<?> sourceClass, String warnString) {
        Logger logger = Logger.getLogger(sourceClass);
        logger.warn(warnString);

        if(dlg != null)
        {
            dlg.addWarnMessage(warnString);
        }
    }

    /**
     * Sets the progress.
     *
     * @param count the new progress
     */
    public static void setProgress(int count) {
        if(dlg != null)
        {
            dlg.setProgress(count);
        }
    }

    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public static void setTotal(int total) {
        if(dlg != null)
        {
            dlg.setTotal(total);
        }
    }

    /**
     * Sets the input file.
     *
     * @param name the new input file
     */
    public static void setInputFile(String name) {
        if(dlg != null)
        {
            dlg.setTitle("SLDEditor - Importing : " + name);
        }
    }

    /**
     * Check if should continue.
     *
     * @return true, if successful
     */
    public static boolean shouldContinue() {
        if(dlg != null)
        {
            return dlg.shouldContinue();
        }
        return true;
    }

    /**
     * Finished.
     */
    public static void finished() {
        if(dlg != null)
        {
            dlg.finished();
        }
    }

}
