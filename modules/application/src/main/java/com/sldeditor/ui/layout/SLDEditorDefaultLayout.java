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

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.map.MapRender;
import com.sldeditor.render.RenderPanelFactory;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.ui.panels.SLDEditorUIPanels;
import com.sldeditor.ui.sldtext.SLDTextArea;

/**
 * Class that implements the default application layout.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorDefaultLayout implements UILayoutInterface
{

    /**
     * Creates the ui.
     *
     * @param application the application
     * @param uiMgr the ui mgr
     * @param extensionList the extension list
     */
    /* (non-Javadoc)
     * @see com.sldeditor.UILayoutInterface#createUI(com.sldeditor.SLDEditorInterface, com.sldeditor.SLDEditorUIPanels, java.util.List)
     */
    @Override
    public void createUI(SLDEditorInterface application, SLDEditorUIPanels uiMgr, List<ExtensionInterface> extensionList) {

        JPanel appPanel = application.getAppPanel();

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        appPanel.setLayout(new BorderLayout(0, 0));

        // Console
        JPanel consolePanel = ConsoleManager.getInstance().getPanel();
        appPanel.add(consolePanel, BorderLayout.SOUTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));

        appPanel.add(panel, BorderLayout.CENTER);

        ToolManager toolManagerInstance = ToolManager.getInstance();
        toolManagerInstance.setApplication(application);
        
        // Initialise extensions
        for(ExtensionInterface extension : extensionList)
        {
            extension.initialise(application.getLoadSLDInterface(), toolManagerInstance);
        }

        // SLD symbol data panel
        JComponent dataPanel = uiMgr.getSLDSymbolData();

        tabbedPane.addTab(Localisation.getString(SLDEditorDefaultLayout.class, "panels.symbol"), null, dataPanel,
                Localisation.getString(SLDEditorDefaultLayout.class, "panels.symbol.tooltip"));

        // Legend data panel
        JComponent legendPanel = uiMgr.getLegendData();

        tabbedPane.addTab(Localisation.getString(SLDEditorDefaultLayout.class, "panels.legend"), null, legendPanel,
                Localisation.getString(SLDEditorDefaultLayout.class, "panels.legend.tooltip"));

        // SLD raw data panel
        JComponent sldPanel = SLDTextArea.getPanel();
        tabbedPane.addTab(Localisation.getString(SLDEditorDefaultLayout.class, "panels.sld"), null, sldPanel,
                Localisation.getString(SLDEditorDefaultLayout.class, "panels.sld.tooltip"));

        MapRender mapTabPanel = RenderPanelFactory.getMapRenderer();
        tabbedPane.addTab(Localisation.getString(SLDEditorDefaultLayout.class, "panels.map"), null, mapTabPanel,
                Localisation.getString(SLDEditorDefaultLayout.class, "panels.map.tooltip"));

        JComponent dataSourceConfig = uiMgr.getDataSourceConfig();
        tabbedPane.addTab(Localisation.getString(SLDEditorDefaultLayout.class, "panels.dataSource"), null, dataSourceConfig,
                Localisation.getString(SLDEditorDefaultLayout.class, "panels.dataSource.tooltip"));

        panel.add(tabbedPane);
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    
                    if(pane.getSelectedComponent() == mapTabPanel)
                    {
                        mapTabPanel.updateStyle();
                    }
                }
            }});

        // Extension tab
        JTabbedPane extensionTab = new JTabbedPane(JTabbedPane.TOP);
        panel.add(extensionTab, BorderLayout.WEST);

        for(ExtensionInterface extension : extensionList)
        {
            extensionTab.addTab(extension.getName(), null, extension.getPanel(), extension.getTooltip());
        }

        application.getApplicationFrame().getContentPane().add(appPanel);
    }

    /**
     * Write layout.
     *
     * @param folder the folder
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.layout.UILayoutInterface#writeLayout(java.lang.String)
     */
    @Override
    public void writeLayout(String folder) {
        // Does nothing
    }

    /**
     * Read layout.
     *
     * @param folder the folder
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.layout.UILayoutInterface#readLayout(java.lang.String)
     */
    @Override
    public void readLayout(String folder) {
        // Does nothing
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.layout.UILayoutInterface#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return "Default";
    }
}
