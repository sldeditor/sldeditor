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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.Controller;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.common.property.PropertyManagerInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.watcher.ReloadManager;
import com.sldeditor.create.NewSLDPanel;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.generated.Version;
import com.sldeditor.map.MapRender;
import com.sldeditor.render.RenderPanelImpl;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.RelativePath;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.layout.UILayoutFactory;
import com.sldeditor.ui.layout.UILayoutInterface;
import com.sldeditor.ui.legend.LegendManager;
import com.sldeditor.ui.menu.SLDEditorMenus;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import com.sldeditor.ui.tree.SLDTree;
import com.sldeditor.update.CheckUpdatePanel;

import it.geosolutions.jaiext.JAIExt;

/**
 * The main application class.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDEditor extends JPanel implements SLDEditorInterface, LoadSLDInterface {

    /** The Constant APPLICATION_FRAME_WIDTH. */
    private static final int APPLICATION_FRAME_WIDTH = 1024;

    /** The Constant APPLICATION_FRAME_HEIGHT. */
    private static final int APPLICATION_FRAME_HEIGHT = 800;

    /** The Constant NO_SLDEDITOR_FILE_SET. */
    private static final String NO_SLDEDITOR_FILE_SET = "Not set";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant APPLICATION_ICON_SMALL. */
    private static final String APPLICATION_ICON_SMALL = "/icon/AppImageSmall.png";

    /** The Constant APPLICATION_ICON_MEDIUM. */
    private static final String APPLICATION_ICON_MEDIUM = "/icon/AppImage.png";

    /** The frame. */
    private static JFrame frame = null;

    /** The data source. */
    private DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

    /** The batch import list. */
    private List<ExtensionInterface> extensionList = new ArrayList<ExtensionInterface>();

    /** The user interface manager. */
    private SLDEditorUIPanels uiMgr = new SLDEditorUIPanels();

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /** The data edited flag. */
    private boolean dataEditedFlag = false;

    /** The under test flag. */
    private static boolean underTestFlag = false;

    /** The sld editor dlg. */
    private SLDEditorDlgInterface sldEditorDlg = null;

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

            if (args[0].startsWith("-") == false) {
                tmpFilename = args[0];
            }

            tmpExtensionArgList = ExtensionFactory.getArgumentList(args);
        }

        final String filename = tmpFilename;
        final List<String> extensionArgList = tmpExtensionArgList;

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (UnsupportedLookAndFeelException e) {
                    ConsoleManager.getInstance().exception(this, e);
                } catch (ClassNotFoundException e) {
                    ConsoleManager.getInstance().exception(this, e);
                } catch (InstantiationException e) {
                    ConsoleManager.getInstance().exception(this, e);
                } catch (IllegalAccessException e) {
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static boolean setOSXAppIcon() {
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method getApplication = util.getMethod("getApplication", new Class[0]);
            Object application = getApplication.invoke(util);
            Class params[] = new Class[1];
            params[0] = Image.class;
            Method setDockIconImage = util.getMethod("setDockIconImage", params);
            URL url = SLDEditor.class.getClassLoader()
                    .getResource(APPLICATION_ICON_MEDIUM.substring(1));
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            setDockIconImage.invoke(application, image);

            return true;
        } catch (ClassNotFoundException e) {
            // log exception
        } catch (NoSuchMethodException e) {
            // log exception
        } catch (InvocationTargetException e) {
            // log exception
        } catch (IllegalAccessException e) {
            // log exception
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
    public SLDEditor(String filename, List<String> extensionArgList,
            SLDEditorDlgInterface overrideSLDEditorDlg) {

        if (overrideSLDEditorDlg == null) {
            sldEditorDlg = new SLDEditorDlg();
        } else {
            sldEditorDlg = overrideSLDEditorDlg;
        }

        UndoManager.getInstance().setPopulationCheck(Controller.getInstance());

        PropertyManagerInterface propertyManager = PropertyManagerFactory.getInstance();
        propertyManager.readConfig();

        // Set up defaults
        PrefManager.initialise(propertyManager);

        // Extensions
        extensionList = ExtensionFactory.getAvailableExtensions();

        String uiLayout = PrefManager.getInstance().getPrefData().getUiLayoutClass();

        UILayoutInterface ui = UILayoutFactory.getUILayout(uiLayout);

        ui.createUI(this, uiMgr, extensionList);

        SLDEditorMenus.createMenus(this, extensionList);

        if (frame != null) {
            frame.setBounds(0, 0, APPLICATION_FRAME_WIDTH, APPLICATION_FRAME_HEIGHT);
        }

        // Set application icon
        setApplicationIcon();

        UILayoutFactory.readLayout(null);

        PrefManager.getInstance().finish();

        ReloadManager.getInstance().addListener(this);

        ExtensionFactory.updateForPreferences(PrefManager.getInstance().getPrefData(),
                extensionArgList);

        // Set the UI to show now SLD files loaded
        uiMgr.populateUI(0);

        // Pass command line arguments to all extensions
        for (ExtensionInterface extension : extensionList) {
            List<String> extensionSpecificArgumentList = ExtensionFactory.getArguments(extension,
                    extensionArgList);

            extension.setArguments(extensionSpecificArgumentList);
        }

        // If specified on the command line, load SLD file
        if (filename != null) {
            File file = new File(filename);

            URL url;
            try {
                url = file.toURI().toURL();
                List<SLDDataInterface> sldDataList = null;
                for (ExtensionInterface extension : extensionList) {
                    if (sldDataList == null) {
                        sldDataList = extension.open(url);
                    }
                }
            } catch (MalformedURLException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        // Check application version on startup
        if (!underTestFlag) {
            PrefData prefData = PrefManager.getInstance().getPrefData();
            if (prefData != null) {
                if (prefData.isCheckAppVersionOnStartUp()) {
                    CheckUpdatePanel updatePanel = new CheckUpdatePanel();
                    updatePanel.showPanelSilent(Version.getVersionNumber());
                }
            }
        }
    }

    /**
     * Creates the and show gui.
     *
     * @param filename the filename
     * @param extensionArgList the extension argument list
     * @param underTest the under test flag
     * @param overrideSLDEditorDlg the override SLD editor dlg
     * @return the SLD editor
     */
    public static SLDEditor createAndShowGUI(String filename, List<String> extensionArgList,
            boolean underTest, SLDEditorDlgInterface overrideSLDEditorDlg) {
        underTestFlag = underTest;
        if (underTestFlag) {
            System.out.println("Running in test mode");
        }
        frame = new JFrame(generateApplicationTitleString());

        CoordManager.getInstance().populateCRSList();
        Controller.getInstance().setFrame(frame);

        MapRender.setUnderTest(underTest);
        RenderPanelImpl.setUnderTest(underTest);
        ReloadManager.setUnderTest(underTest);

        frame.setDefaultCloseOperation(underTest ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);

        // Add contents to the window.
        SLDEditor sldEditor = new SLDEditor(filename, extensionArgList, overrideSLDEditorDlg);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

        return sldEditor;
    }

    /**
     * Generate application title string.
     *
     * @return the string
     */
    private static String generateApplicationTitleString() {
        return String.format("%s %s \251%s %s", Version.getAppName(), Version.getVersionNumber(),
                Version.getAppCopyrightYear(), Version.getAppCompany());
    }

    /**
     * Update window title.
     *
     * @param dataEditedFlag the data edited flag
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#updateWindowTitle(boolean)
     */
    @Override
    public void updateWindowTitle(boolean dataEditedFlag) {
        this.dataEditedFlag = dataEditedFlag;
        String docName = NO_SLDEDITOR_FILE_SET;
        File file = SLDEditorFile.getInstance().getSldEditorFile();

        String sldLayerName = SLDEditorFile.getInstance().getSLDData().getLayerName();
        if (file != null) {
            docName = file.getName() + " - " + sldLayerName;
        } else {
            docName = sldLayerName;
        }

        char docDirtyChar = dataEditedFlag ? '*' : ' ';
        if(frame != null)
        {
            frame.setTitle(String.format("%s - %s%c", generateApplicationTitleString(), docName,
                docDirtyChar));
        }
    }

    /**
     * Exit application.
     */
    @Override
    public void exitApplication() {
        UILayoutFactory.writeLayout(null);

        System.exit(0);
    }

    /**
     * Choose new sld.
     */
    @Override
    public void chooseNewSLD() {
        NewSLDPanel panel = new NewSLDPanel();

        List<SLDDataInterface> newSLDList = panel.showDialog(Controller.getInstance().getFrame());

        if (newSLDList != null) {
            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setSldData(newSLDList);

            if (loadSLDString(selectedFiles)) {
                uiMgr.populateUI(1);
            }
        }
    }

    /**
     * Select SLD tree item, called by test framework.
     *
     * @param data the tree selection data
     * @return true, if successful
     */
    public boolean selectTreeItem(TreeSelectionData data) {
        SLDTree sldTree = uiMgr.getSymbolTree();

        if (sldTree == null) {
            return false;
        }

        return sldTree.selectTreeItem(data);
    }

    /**
     * Gets the symbol panel.
     *
     * @return the symbol panel
     */
    public PopulateDetailsInterface getSymbolPanel() {
        SLDTree sldTree = uiMgr.getSymbolTree();

        PopulateDetailsInterface panel = sldTree.getSelectedSymbolPanel();

        return panel;
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    public GraphicPanelFieldManager getFieldDataManager() {
        return uiMgr.getFieldDataManager();
    }

    /**
     * Sets the list of vendor options.
     *
     * @param vendorOptionList the list of vendor options
     */
    public void setVendorOptions(List<VersionData> vendorOptionList) {
        VendorOptionManager.getInstance().overrideSelectedVendorOptions(vendorOptionList);
    }

    /**
     * Empty sld.
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.LoadSLDInterface#emptySLD()
     */
    @Override
    public void emptySLD() {
        String emptyFilename = "";
        SelectedSymbol.getInstance().setFilename(emptyFilename);

        SLDEditorFile.getInstance().setSLDData(null);
        dataSource.reset();

        SelectedSymbol.getInstance().setSld(null);
        uiMgr.populateUI(0);
    }

    /**
     * Save file.
     *
     * @param urlToSave the url to save
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#saveFile(java.net.URL)
     */
    @Override
    public void saveFile(URL urlToSave) {

        String sldContents = getSLDString();

        SLDDataInterface sldData = SLDEditorFile.getInstance().getSLDData();
        sldData.updateSLDContents(sldContents);

        if (RelativePath.isLocalFile(urlToSave)) {
            StyleWrapper style = null;
            try {
                File f = new File(urlToSave.toURI());
                style = new StyleWrapper(f.getName());
            } catch (URISyntaxException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
            sldData.updateStyleWrapper(style);
        }

        ReloadManager.getInstance().setFileSaved();
        saveSLDData(sldData);

        SLDEditorFile.getInstance().fileOpenedSaved();
        UndoManager.getInstance().fileSaved();
    }

    /**
     * Save sld data.
     *
     * @param sldData the sld data
     */
    @Override
    public void saveSLDData(SLDDataInterface sldData) {
        boolean saved = false;

        for (ExtensionInterface extension : extensionList) {
            if (saved == false) {
                saved = extension.save(sldData);
            }
        }
    }

    /**
     * Gets the app panel.
     *
     * @return the app panel
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#getAppPanel()
     */
    @Override
    public JPanel getAppPanel() {
        return this;
    }

    /**
     * Gets the load sld interface.
     *
     * @return the load sld interface
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#getLoadSLDInterface()
     */
    @Override
    public LoadSLDInterface getLoadSLDInterface() {
        return this;
    }

    /**
     * Gets the application frame.
     *
     * @return the application frame
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#getApplicationFrame()
     */
    @Override
    public JFrame getApplicationFrame() {
        return Controller.getInstance().getFrame();
    }

    /**
     * Load sld string.
     *
     * @param selectedFiles the selected files
     * @return true, if successful
     */
    @Override
    public boolean loadSLDString(SelectedFiles selectedFiles) {
        boolean loadNewSymbol = true;

        PrefManager.getInstance().setLastFolderViewed(selectedFiles);

        List<SLDDataInterface> sldFilesToLoad = selectedFiles.getSldData();

        if (!selectedFiles.isFolder()) {
            // Application can only support editing one SLD file at a time
            if (sldFilesToLoad.size() == 1) {
                SLDDataInterface firstObject = sldFilesToLoad.get(0);

                if (firstObject != null) {
                    if (dataEditedFlag && !isUnderTestFlag()) {
                        loadNewSymbol = sldEditorDlg.load(frame);
                    }

                    if (loadNewSymbol) {
                        populate(firstObject);
                    }

                    ReloadManager.getInstance().reset();
                }
            }

            if (!selectedFiles.isDataSource()) {
                // Inform UndoManager that a new SLD file has been
                // loaded and to clear undo history
                UndoManager.getInstance().fileLoaded();

                Controller.getInstance().setPopulating(true);
                uiMgr.populateUI(sldFilesToLoad.size());
                Controller.getInstance().setPopulating(false);
            }
        }

        return loadNewSymbol;
    }

    /**
     * Populate the application with the SLD.
     *
     * @param sldData the sld data
     */
    protected void populate(SLDDataInterface sldData) {
        String layerName = sldData.getLayerName();

        File sldEditorFile = sldData.getSldEditorFile();
        if (sldEditorFile != null) {
            ConsoleManager.getInstance().information(this,
                    String.format("%s : %s",
                            Localisation.getString(getClass(), "SLDEditor.loadedSLDEditorFile"),
                            sldEditorFile.getAbsolutePath()));
        }
        ConsoleManager.getInstance().information(this, String.format("%s : %s",
                Localisation.getString(getClass(), "SLDEditor.loadedSLDFile"), layerName));

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        SelectedSymbol selectedSymbolInstance = SelectedSymbol.getInstance();
        selectedSymbolInstance.setSld(sld);
        selectedSymbolInstance.setFilename(layerName);
        selectedSymbolInstance.setName(layerName);

        SLDEditorFile.getInstance().setSLDData(sldData);

        // Reload data source if sticky flag is set
        boolean isDataSourceSticky = SLDEditorFile.getInstance().isStickyDataSource();
        DataSourcePropertiesInterface previousDataSource = dataSource.getDataConnectorProperties();

        dataSource.reset();

        if (isDataSourceSticky) {
            SLDEditorFile.getInstance().setDataSource(previousDataSource);
        }

        dataSource.connect(ExternalFilenames.removeSuffix(layerName), SLDEditorFile.getInstance());

        VendorOptionManager.getInstance().loadSLDFile(uiMgr, sld, sldData);

        LegendManager.getInstance().SLDLoaded(sldData.getLegendOptions());
        SLDEditorFile.getInstance().fileOpenedSaved();
    }

    /**
     * Read sld file.
     *
     * @param file the file
     * @return the styled layer descriptor
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.LoadSLDInterface#readSLDFile(java.io.File)
     */
    @Override
    public StyledLayerDescriptor readSLDFile(File file) {
        return SLDUtils.readSLDFile(file);
    }

    /**
     * Open file.
     *
     * @param url the url
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#openFile(java.net.URL)
     */
    @Override
    public void openFile(URL url) {
        List<SLDDataInterface> sldDataList = null;
        for (ExtensionInterface extension : extensionList) {
            if (sldDataList == null) {
                sldDataList = extension.open(url);
            }
        }

        if (sldDataList != null) {
            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setSldData(sldDataList);

            loadSLDString(selectedFiles);
        }
    }

    /**
     * Gets the app name.
     *
     * @return the app name
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.SLDEditorInterface#getAppName()
     */
    @Override
    public String getAppName() {
        return Version.getAppName();
    }

    /**
     * Gets the SLD string.
     *
     * @return the SLD string
     */
    public String getSLDString() {
        if (sldWriter == null) {
            sldWriter = SLDWriterFactory.createWriter(null);
        }

        String sldContents = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        return sldContents;
    }

    /**
     * Sets the application icon.
     */
    private void setApplicationIcon() {
        if (frame != null) {
            List<Image> icons = new ArrayList<Image>();
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
            e.printStackTrace();
        }

        return buff;
    }

    /**
     * Checks if is under test flag.
     *
     * @return the underTestFlag
     */
    private static boolean isUnderTestFlag() {
        return underTestFlag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.LoadSLDInterface#preLoad()
     */
    @Override
    public void preLoad() {
        ConsoleManager.getInstance().clear();
        uiMgr.getDataSourceConfig().reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.LoadSLDInterface#reloadSLDFile()
     */
    @Override
    public void reloadSLDFile() {
        boolean reloadFile = true;
        if (!underTestFlag) {
            reloadFile = sldEditorDlg.reload(frame);
        }

        if (reloadFile) {
            SLDDataInterface sldData = SLDEditorFile.getInstance().getSLDData();

            if (sldData != null) {
                URL url = sldData.getSLDURL();
                if (url != null) {
                    List<SLDDataInterface> sldDataList = null;
                    for (ExtensionInterface extension : extensionList) {
                        if (sldDataList == null) {
                            sldDataList = extension.open(url);
                        }
                    }

                    if ((sldDataList != null) && !sldDataList.isEmpty()) {
                        SLDDataInterface firstObject = sldDataList.get(0);
                        populate(firstObject);
                    }
                }
            }

            // Inform UndoManager that a new SLD file has been
            // loaded and to clear undo history
            UndoManager.getInstance().fileLoaded();

            Controller.getInstance().setPopulating(true);
            uiMgr.populateUI(1);
            Controller.getInstance().setPopulating(false);
        }
        ReloadManager.getInstance().reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.SLDEditorInterface#refreshPanel(java.lang.Class, java.lang.Class)
     */
    @Override
    public void refreshPanel(Class<?> parent, Class<?> panelClass) {
        uiMgr.refreshPanel(parent, panelClass);
    }
}
