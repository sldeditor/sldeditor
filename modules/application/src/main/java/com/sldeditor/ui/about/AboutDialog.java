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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sldeditor.AppSplashScreen;
import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;

/**
 * The About dialog class.
 * 
 * @author Robert Ward (SCISYS)
 */
public class AboutDialog extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new about dialog.
     */
    public AboutDialog() {
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        setResizable(false);
        setTitle(Localisation.getString(AboutDialog.class, "AboutDialog.title"));
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton close = new JButton(Localisation.getString(AboutDialog.class, "AboutDialog.close"));
        panel.add(close);
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });

        URL url = AppSplashScreen.getSplashImageURL();

        JLabel label = new JLabel();
        try {
            BufferedImage image = mergeImageAndText(url, AppSplashScreen.getVersionString(), AppSplashScreen.getTextPosition());
            label.setIcon(new ImageIcon(image));
            label.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
        catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        getContentPane().add(label, BorderLayout.CENTER);
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Merge image and text.
     *
     * @param imageFilePath the image file path
     * @param text the text
     * @param textPosition the text position
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static BufferedImage mergeImageAndText(URL imageFilePath,
            String text, Point textPosition) throws IOException {
        BufferedImage im = ImageIO.read(imageFilePath);
        Graphics2D g2 = im.createGraphics();
        Font font = AppSplashScreen.getFont();

        g2.setFont(font);
        g2.setColor(Color.black);
        g2.drawString(text, textPosition.x, textPosition.y);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(im, "png", baos);

        InputStream in = new ByteArrayInputStream(baos.toByteArray());

        BufferedImage bImageFromConvert = ImageIO.read(in);
        return bImageFromConvert;
    }
}
