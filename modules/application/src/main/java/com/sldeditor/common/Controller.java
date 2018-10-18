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

package com.sldeditor.common;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Class contains the JFrame instance of the application, used as the parent for other dialogs.
 *
 * <p>Allows the setting and getting of the 'is populating' flag.
 *
 * <p>Class is implemented as a singleton.
 *
 * @author Robert Ward (SCISYS)
 */
public class Controller implements PopulatingInterface {

    /** The singleton instance. */
    private static Controller instance = null;

    /** The populating flag. */
    private boolean populating = false;

    /** The frame. */
    private JFrame frame = null;

    /** Instantiates a new controller. */
    private Controller() {
        // Default constructor
    }

    /**
     * Gets the single instance of Controller.
     *
     * @return single instance of Controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }

        return instance;
    }

    /**
     * Checks if is populating.
     *
     * @return true, if is populating
     */
    @Override
    public boolean isPopulating() {
        return populating;
    }

    /**
     * Sets the populating.
     *
     * @param populating the new populating
     */
    @Override
    public void setPopulating(boolean populating) {
        this.populating = populating;
    }

    /**
     * Gets the frame.
     *
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Sets the frame.
     *
     * @param frame the frame to set
     */
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Sets the dialog position in the centre of the application window.
     *
     * @param dialog the dialog to centre
     */
    public void centreDialog(JDialog dialog) {
        if ((frame != null) && (dialog != null)) {
            final int x = frame.getX() + (frame.getWidth() - dialog.getWidth()) / 2;
            final int y = frame.getY() + (frame.getHeight() - dialog.getHeight()) / 2;
            dialog.setLocation(x, y);
        }
    }
}
