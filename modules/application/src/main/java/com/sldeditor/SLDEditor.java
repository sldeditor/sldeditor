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

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.common.property.PropertyManagerInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.watcher.ReloadManager;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.generated.Version;
import com.sldeditor.map.MapRender;
import com.sldeditor.render.RenderPanelImpl;
import com.sldeditor.ui.layout.UILayoutFactory;
import com.sldeditor.ui.layout.UILayoutInterface;
import com.sldeditor.ui.menu.SLDEditorMenus;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import com.sldeditor.update.CheckUpdatePanel;
import it.geosolutions.jaiext.JAIExt;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main application class.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditor extends JPanel {

    /** The Constant APPLICATION_FRAME_WIDTH. */
    private static final int APPLICATION_FRAME_WIDTH = 1024;

    /** The Constant APPLICATION_FRAME_HEIGHT. */
    private static final int APPLICATION_FRAME_HEIGHT = 800;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant APPLICATION_ICON_SMALL. */
    private static final String APPLICATION_ICON_SMALL = "/icon/AppImageSmall.png";

    /** The Constant APPLICATION_ICON_MEDIUM. */
    private static final String APPLICATION_ICON_MEDIUM = "/icon/AppImage.png";

    /** The frame. */
    protected static JFrame frame = null;

    /** The under test flag. */
    protected static boolean underTestFlag = false;

    /** The main class. */
    private transient SLDEditorMain main = new SLDEditorMain(this);

    /** The test interface. */
    private transient SLDEditorTestInterface testInterface = null;

    static {
        JAIExt.initJAIEXT();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        String tmpFilename = null;
        List<String> tmpExtensionArgList = null;

        if (args.length > 0) {
            // Set up localisation of text
            Localisation.getInstance().parseCommandLine(args);

            if (!args[0].startsWith("-")) {
                tmpFilename = args[0];
            }

            tmpExtensionArgList = ExtensionFactory.getArgumentList(args);
        }

        final String filename = tmpFilename;
        final List<String> extensionArgList = tmpExtensionArgList;

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {

                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (UnsupportedLookAndFeelException
                                | ClassNotFoundException
                                | InstantiationException
                                | IllegalAccessException e) {
                            ConsoleManager.getInstance().exception(this, e);
                        }

                        setOSXAppIcon();
                        AppSplashScreen.splashInit();
                        createAndShowGUI(filename, extensionArgList, false, null);
                    }
                });
    }

    /**
     * Sets the Mac OSX application icon.
     *
     * @return true, if successful
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static boolean setOSXAppIcon() {
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method getApplication = util.getMethod("getApplication", new Class[0]);
            Object application = getApplication.invoke(util);
            Class[] params = new Class[1];
            params[0] = Image.class;
            Method setDockIconImage = util.getMethod("setDockIconImage", params);
            URL url =
                    SLDEditor.class
                            .getClassLoader()
                            .getResource(APPLICATION_ICON_MEDIUM.substring(1));
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            setDockIconImage.invoke(application, image);

            return true;
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException e) {
            // If we get an exception we are not running on MacOS
        }

        return false;
    }

    /**
     * Instantiates a new SLD editor.
     *
     * @param filename the filename
     * @param extensionArgList the extension arg list
     * @param overrideSLDEditorDlg the override SLD editor dlg
     */
    public SLDEditor(
            String filename,
            List<String> extensionArgList,
            SLDEditorDlgInterface overrideSLDEditorDlg) {

        DataSourceFactory.createDataSource(null);

        SLDEditorDlgInterface sldEditorDlg = null;

        if (overrideSLDEditorDlg == null) {
            sldEditorDlg = new SLDEditorDlg();
        } else {
            sldEditorDlg = overrideSLDEditorDlg;
        }

        UndoManager.getInstance().setPopulationCheck(Controller.getInstance());

        if (!underTestFlag) {
            PropertyManagerFactory.setNotUnderTest();
        }
        PropertyManagerInterface propertyManager = PropertyManagerFactory.getInstance();
        propertyManager.readConfig();

        // Set up defaults
        PrefManager.initialise(propertyManager);

        // Extensions
        List<ExtensionInterface> extensionList = ExtensionFactory.getAvailableExtensions();

        SLDEditorOperations.getInstance().setSldEditorDlg(sldEditorDlg);
        ReloadManager.getInstance().addListener(SLDEditorOperations.getInstance());

        String uiLayout = PrefManager.getInstance().getPrefData().getUiLayoutClass();

        UILayoutInterface ui = UILayoutFactory.getUILayout(uiLayout);

        ui.createUI(main, SLDEditorUIPanels.getInstance(), extensionList);

        SLDEditorMenus.createMenus(main, extensionList);

        if (frame != null) {
            frame.setBounds(0, 0, APPLICATION_FRAME_WIDTH, APPLICATION_FRAME_HEIGHT);
        }

        // Set application icon
        setApplicationIcon();

        UILayoutFactory.readLayout(null);

        PrefManager.getInstance().finish();

        ExtensionFactory.updateForPreferences(
                PrefManager.getInstance().getPrefData(), extensionArgList);

        // Set the UI to show now SLD files loaded
        SLDEditorUIPanels.getInstance().populateUI(0);

        // Pass command line arguments to all extensions
        for (ExtensionInterface extension : extensionList) {
            List<String> extensionSpecificArgumentList =
                    ExtensionFactory.getArguments(extension, extensionArgList);

            extension.setArguments(extensionSpecificArgumentList);
        }

        // If specified on the command line, load SLD file
        if (filename != null) {
            loadFromCommandLine(filename);
        }

        // Check application version on startup
        checkAppVersion();
    }

    /** Check application version on startup. */
    private void checkAppVersion() {
        if (!underTestFlag) {
            PrefData prefData = PrefManager.getInstance().getPrefData();
            if ((prefData != null) && prefData.isCheckAppVersionOnStartUp()) {
                CheckUpdatePanel updatePanel = new CheckUpdatePanel();
                updatePanel.showPanelSilent(Version.getVersionNumber());
            }
        }
    }

    /**
     * Load from command line.
     *
     * @param filename the filename
     */
    private void loadFromCommandLine(String filename) {
        File file = new File(filename);

        URL url;
        try {
            url = file.toURI().toURL();
            SLDEditorCommon.getInstance().openURL(url);
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /**
     * Creates and shows the gui.
     *
     * @param filename the filename
     * @param extensionArgList the extension argument list
     * @param underTest the under test flag
     * @param overrideSLDEditorDlg the override SLD editor dlg
     * @return the SLD editor
     */
    public static SLDEditor createAndShowGUI(
            String filename,
            List<String> extensionArgList,
            boolean underTest,
            SLDEditorDlgInterface overrideSLDEditorDlg) {
        underTestFlag = underTest;
        if (underTestFlag) {
            System.out.println("Running in test mode");
        }
        frame = new JFrame(SLDEditorMain.generateApplicationTitleString());

        CoordManager.getInstance().populateCRSList();
        Controller.getInstance().setFrame(frame);

        MapRender.setUnderTest(underTest);
        RenderPanelImpl.setUnderTest(underTest);
        ReloadManager.setUnderTest(underTest);
        SLDEditorOperations.setUnderTest(underTest);

        frame.setDefaultCloseOperation(underTest ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        SLDEditor sldEditor = new SLDEditor(filename, extensionArgList, overrideSLDEditorDlg);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

        return sldEditor;
    }

    /** Sets the application icon. */
    private void setApplicationIcon() {
        if (frame != null) {
            List<Image> icons = new ArrayList<>();
            icons.add(getImage(APPLICATION_ICON_SMALL));
            icons.add(getImage(APPLICATION_ICON_MEDIUM));
            frame.setIconImages(icons);
        }
    }

    /**
     * Gets the image from resource file.
     *
     * @param filename the filename
     * @return the image
     */
    private Image getImage(String filename) {
        BufferedImage buff = null;
        try {
            buff = ImageIO.read(getClass().getResourceAsStream(filename));
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return buff;
    }

    /**
     * Gets the test interface.
     *
     * @return the test interface
     */
    public SLDEditorTestInterface getTestInterface() {
        if (testInterface == null) {
            testInterface = new SLDEditorExternal(main);
        }
        return testInterface;
    }
}
