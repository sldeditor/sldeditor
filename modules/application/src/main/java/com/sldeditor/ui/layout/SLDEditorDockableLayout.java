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

package com.sldeditor.ui.layout;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.render.RenderPanelFactory;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import com.sldeditor.ui.sldtext.SLDTextArea;

import bibliothek.gui.dock.common.CContentArea;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;

/**
 * Class that implements the dockable application layout.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorDockableLayout implements UILayoutInterface {

    /** The Constant LAYOUT_FILENAME. */
    private static final String LAYOUT_FILENAME = "layout.data";

    /** The control. */
    private CControl control;

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.UILayoutInterface#createUI(com.sldeditor.SLDEditorInterface, com.sldeditor.SLDEditorUIPanels, java.util.List)
     */
    @Override
    public void createUI(SLDEditorInterface application, SLDEditorUIPanels uiMgr,
            List<ExtensionInterface> extensionList) {
        JFrame frame = application.getApplicationFrame();

        control = new CControl(frame);

        frame.setLayout(new GridLayout(1, 1));
        frame.add(control.getContentArea());

        CGrid grid = new CGrid(control);
        SingleCDockable legend = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.legend"),
                (JPanel) uiMgr.getLegendData());
        SingleCDockable symbol = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.symbol"),
                uiMgr.getSLDSymbolData());
        SingleCDockable sld = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.sld"),
                SLDTextArea.getPanel());
        SingleCDockable map = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.map"),
                RenderPanelFactory.getMapRenderer());
        SingleCDockable dataSource = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.dataSource"),
                uiMgr.getDataSourceConfig());
        SingleCDockable vendorOption = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.vendorOption"),
                uiMgr.getVendorOption());

        SingleCDockable console = create(
                Localisation.getString(SLDEditorDockableLayout.class, "panels.console"),
                ConsoleManager.getInstance().getPanel());

        control.addDockable(sld);
        control.addDockable(legend);
        control.addDockable(symbol);
        control.addDockable(map);
        control.addDockable(dataSource);
        control.addDockable(vendorOption);
        control.addDockable(console);

        ToolManager toolManagerInstance = ToolManager.getInstance();
        toolManagerInstance.setApplication(application);

        for (ExtensionInterface extension : extensionList) {
            extension.initialise(application.getLoadSLDInterface(), toolManagerInstance);

            SingleCDockable dockablePlugin = create(extension.getName(), extension.getPanel());
            control.addDockable(dockablePlugin);
            grid.add(0, 0, 1, 4, dockablePlugin);
        }

        grid.add(1, 0, 2, 4, sld);
        grid.add(1, 0, 2, 4, legend);
        grid.add(1, 0, 2, 4, map);
        grid.add(1, 0, 2, 4, dataSource);
        grid.add(1, 0, 2, 4, vendorOption);
        grid.add(1, 0, 2, 4, symbol);

        grid.add(0, 4, 3, 1, console);

        CContentArea content = control.getContentArea();
        content.deploy(grid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.layout.UILayoutInterface#readLayout(java.lang.String)
     */
    @Override
    public void readLayout(String folder) {
        File file = getFile(folder);

        if (file.exists()) {
            try {
                control.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a SingleCDockable object
     *
     * @param title the title
     * @param panel the panel
     * @return the SingleCDockable
     */
    private static SingleCDockable create(String title, JPanel panel) {
        return new DefaultSingleCDockable(title, title, panel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.layout.UILayoutInterface#writeLayout(java.lang.String)
     */
    @Override
    public void writeLayout(String folder) {

        File file = getFile(folder);

        try {
            control.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the file.
     *
     * @param folder the folder
     * @return the file
     */
    private File getFile(String folder) {
        File file = null;

        if (folder == null) {
            file = new File(LAYOUT_FILENAME);
        } else {
            file = new File(folder, LAYOUT_FILENAME);
        }
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.layout.UILayoutInterface#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "Dockable";
    }
}
