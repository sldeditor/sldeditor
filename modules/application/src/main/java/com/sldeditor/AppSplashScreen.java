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

package com.sldeditor;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.generated.Version;
import com.sldeditor.ui.about.AboutDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import org.apache.log4j.Logger;
import org.geotools.factory.GeoTools;

/**
 * The application splash screen.
 *
 * @author Robert Ward (SCISYS)
 */
public final class AppSplashScreen {

    /** The Constant TEXTPOSITION_Y_OFFSET. */
    private static final int TEXTPOSITION_Y_OFFSET = 15;

    /** The Constant TEXTPOSITION_X_OFFSET. */
    private static final int TEXTPOSITION_X_OFFSET = 10;

    /** The Constant TEXT_AREA_WIDTH_FRACTION. */
    private static final double TEXT_AREA_WIDTH_FRACTION = .45;

    /** The Constant TEXT_AREA_Y_FRACTION. */
    private static final double TEXT_AREA_Y_FRACTION = 0.88;

    /** The Constant TEXTAREA_HEIGHT. */
    private static final double TEXTAREA_HEIGHT = 32.0;

    /** The Constant TEXT_AREA_X. */
    private static final double TEXT_AREA_X = 300.0;

    /** The Constant SPLASHSCREEN_HEIGHT. */
    private static final int SPLASHSCREEN_HEIGHT = 459;

    /** The Constant SPLASHSCREEN_WIDTH. */
    private static final int SPLASHSCREEN_WIDTH = 640;

    /** The Constant DEFAULT_FONT_SIZE. */
    private static final int DEFAULT_FONT_SIZE = 14;

    /** The Constant SPLASH_IMAGE, the splash image bitmap file. */
    private static final String SPLASH_IMAGE = "splash/splash.png";

    /** The my splash. */
    private static SplashScreen splashScreenObj;

    /** The splash graphics. */
    private static Graphics2D splashGraphics;

    /** The version text font. */
    private static Font font = new Font("Helvetica", Font.BOLD, DEFAULT_FONT_SIZE);

    /** The splash text area. */
    private static Rectangle2D.Double splashTextArea;

    /** The logger. */
    private static Logger logger = Logger.getLogger(AppSplashScreen.class);

    /** Instantiates a new app splash screen. */
    private AppSplashScreen() {}

    /** Splash initialise. */
    public static void splashInit() {
        splashScreenObj = SplashScreen.getSplashScreen();
        if (splashScreenObj != null) {
            createTextArea();

            // create the Graphics environment for drawing status info
            splashGraphics = splashScreenObj.createGraphics();
            splashGraphics.setFont(font);

            // Display the version number
            splashText(getVersionString());
        }
    }

    /**
     * Gets the version string.
     *
     * @return the version string
     */
    public static String getVersionString() {
        return String.format("Version : %s", Version.getVersionNumber());
    }

    /** Creates the text area. */
    private static void createTextArea() {
        if (splashTextArea == null) {
            Dimension ssDim = null;

            if (splashScreenObj != null) {
                ssDim = splashScreenObj.getSize();
            } else {
                ssDim = new Dimension(SPLASHSCREEN_WIDTH, SPLASHSCREEN_HEIGHT);
            }
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
            splashTextArea =
                    new Rectangle2D.Double(
                            TEXT_AREA_X,
                            height * TEXT_AREA_Y_FRACTION,
                            width * TEXT_AREA_WIDTH_FRACTION,
                            TEXTAREA_HEIGHT);
        }
    }

    /**
     * Splash text.
     *
     * @param str the str
     */
    public static void splashText(final String str) {
        logger.info(str);

        if (splashScreenObj != null && splashScreenObj.isVisible()) {
            // draw the text
            splashGraphics.setPaint(Color.BLACK);

            splashGraphics.setFont(getFont());
            splashGraphics.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            splashGraphics.setColor(Color.black);

            splashGraphics.drawString(
                    str, (int) getTextPosition().getX(), (int) getTextPosition().getY());
            String geoToolsVersionString =
                    String.format(
                            "%s GeoTools %s",
                            Localisation.getString(AboutDialog.class, "AboutDialog.basedOn"),
                            GeoTools.getVersion().toString());
            splashGraphics.drawString(
                    geoToolsVersionString,
                    (int) getTextPosition().getX(),
                    (int) (getTextPosition().getY() + AppSplashScreen.getFont().getSize2D()));

            // make sure it's displayed
            splashScreenObj.update();
        }
    }

    /**
     * Gets the text position.
     *
     * @return the text position
     */
    public static Point getTextPosition() {
        createTextArea();

        return new Point(
                (int) (splashTextArea.getX() + TEXTPOSITION_X_OFFSET),
                (int) (splashTextArea.getY() + TEXTPOSITION_Y_OFFSET));
    }

    /**
     * Gets the font.
     *
     * @return the font
     */
    public static Font getFont() {
        return font;
    }

    /**
     * Gets the splash image url.
     *
     * @return the splash image url
     */
    public static URL getSplashImageURL() {
        return SLDEditor.class.getClassLoader().getResource(SPLASH_IMAGE);
    }
}
