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

package com.sldeditor.ui.about;

import com.sldeditor.AppSplashScreen;
import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.geotools.factory.GeoTools;

/**
 * The About dialog class.
 *
 * @author Robert Ward (SCISYS)
 */
public class AboutDialog extends JDialog {

    /** The Class TextPosition. */
    class TextPosition {
        /** The text string. */
        private String textString;

        /** The position. */
        private Point position;

        /**
         * Instantiates a new text position.
         *
         * @param textString the text string
         * @param position the position
         */
        public TextPosition(String textString, Point position) {
            super();
            this.textString = textString;
            this.position = position;
        }

        /**
         * Gets the text string.
         *
         * @return the textString
         */
        public String getTextString() {
            return textString;
        }

        /**
         * Gets the position.
         *
         * @return the position
         */
        public Point getPosition() {
            return position;
        }
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Instantiates a new about dialog. */
    public AboutDialog() {
        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        setResizable(false);
        setTitle(Localisation.getString(AboutDialog.class, "AboutDialog.title"));
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton close = new JButton(Localisation.getString(AboutDialog.class, "AboutDialog.close"));
        panel.add(close);
        close.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        dispose();
                    }
                });

        URL url = AppSplashScreen.getSplashImageURL();

        JLabel label = new JLabel();
        try {
            List<TextPosition> textList = createTextList();

            BufferedImage image = mergeImageAndText(url, textList);
            label.setIcon(new ImageIcon(image));
            label.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        getContentPane().add(label, BorderLayout.CENTER);
        setModalityType(ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the text list.
     *
     * @return the list
     */
    private List<TextPosition> createTextList() {
        List<TextPosition> textList = new ArrayList<TextPosition>();

        // Application version string
        Point textPosition = AppSplashScreen.getTextPosition();
        textList.add(new TextPosition(AppSplashScreen.getVersionString(), textPosition));

        // GeoTools version string
        Point p =
                new Point(
                        (int) textPosition.getX(),
                        (int) (textPosition.getY() + AppSplashScreen.getFont().getSize2D()));
        String geoToolsVersionString =
                String.format(
                        "%s GeoTools %s",
                        Localisation.getString(AboutDialog.class, "AboutDialog.basedOn"),
                        GeoTools.getVersion().toString());
        textList.add(new TextPosition(geoToolsVersionString, p));

        return textList;
    }

    /**
     * Merge image and text.
     *
     * @param imageFilePath the image file path
     * @param textList the text list
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static BufferedImage mergeImageAndText(URL imageFilePath, List<TextPosition> textList)
            throws IOException {
        BufferedImage im = ImageIO.read(imageFilePath);
        Graphics2D g2 = im.createGraphics();
        Font font = AppSplashScreen.getFont();

        g2.setFont(font);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.black);

        for (TextPosition obj : textList) {
            g2.drawString(obj.textString, obj.position.x, obj.position.y);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(im, "png", baos);

        InputStream in = new ByteArrayInputStream(baos.toByteArray());

        BufferedImage bImageFromConvert = ImageIO.read(in);
        return bImageFromConvert;
    }
}
