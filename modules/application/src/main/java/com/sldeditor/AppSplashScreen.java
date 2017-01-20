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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.generated.Version;
import com.sldeditor.ui.about.AboutDialog;

/**
 * The application splash screen
 * 
 * @author Robert Ward (SCISYS)
 */
public class AppSplashScreen
{
    /** The Constant SPLASH_IMAGE, the splash image bitmap file. */
    private static final String SPLASH_IMAGE = "splash/splash.png";

    /** The my splash. */
    private static SplashScreen splashScreenObj;

    /** The splash graphics. */
    private static Graphics2D splashGraphics;

    /** The version text font. */
    private static Font font = new Font("Helvetica", Font.BOLD, 14);

    /** The splash text area. */
    private static Rectangle2D.Double splashTextArea;

    /** The logger. */
    private static Logger logger = Logger.getLogger(AppSplashScreen.class);

    /**
     * Splash initialise.
     */
    public static void splashInit()
    {
        splashScreenObj = SplashScreen.getSplashScreen();
        if (splashScreenObj != null)
        {
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

    /**
     * Creates the text area.
     */
    private static void createTextArea() {
        if(splashTextArea == null)
        {
            Dimension ssDim = null;

            if(splashScreenObj != null)
            {
                ssDim = splashScreenObj.getSize();
            }
            else
            {
                ssDim = new Dimension(640, 459);
            }
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(300.0, height * 0.88, width * .45, 32.0);
        }
    }

    /**
     * Splash text.
     *
     * @param str the str
     */
    public static void splashText(String str)
    {
        logger.info(str);

        if (splashScreenObj != null && splashScreenObj.isVisible())
        {
            // draw the text
            splashGraphics.setPaint(Color.BLACK);
            Font font = getFont();

            splashGraphics.setFont(font);
            splashGraphics.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            splashGraphics.setColor(Color.black);

            splashGraphics.drawString(str, (int)getTextPosition().getX(), (int)getTextPosition().getY());
            String geoToolsVersionString = String.format("%s GeoTools %s", Localisation.getString(AboutDialog.class, "AboutDialog.basedOn"), GeoTools.getVersion().toString());
            splashGraphics.drawString(geoToolsVersionString, (int)getTextPosition().getX(), (int)(getTextPosition().getY() + AppSplashScreen.getFont().getSize2D()));

            // make sure it's displayed
            splashScreenObj.update();
        }
    }

    /**
     * Gets the text position.
     *
     * @return the text position
     */
    public static Point getTextPosition()
    {
        createTextArea();

        Point p = new Point((int)(splashTextArea.getX() + 10), (int)(splashTextArea.getY() + 15));

        return p;
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
        URL url = SLDEditor.class.getClassLoader().getResource(SPLASH_IMAGE);
        return url;
    }
}
