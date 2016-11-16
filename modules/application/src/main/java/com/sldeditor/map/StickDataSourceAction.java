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

package com.sldeditor.map;

import java.awt.event.ActionEvent;

import javax.swing.JToggleButton;

import org.geotools.swing.MapPane;
import org.geotools.swing.action.MapAction;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.SLDEditorFile;

/**
 * The Class StickDataSourceAction.
 *
 * @author Robert Ward (SCISYS)
 */
public class StickDataSourceAction extends MapAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4462980379403957817L;

    /** Name for this tool */
    public static final String TOOL_NAME = Localisation.getString(MapRender.class, "MapRender.stickyTool");

    /** Tool tip text */
    public static final String TOOL_TIP = Localisation.getString(MapRender.class, "MapRender.stickyToolTip");

    /** Icon for the control */
    public static final String ICON_IMAGE = "/map/StickyDataSource.png";

    /**
     * Constructor. The associated control will be labelled with an icon.
     * 
     * @param mapPane the map pane being serviced by this action
     */
    public StickDataSourceAction(MapPane mapPane) {
        this(mapPane, false);
    }

    /**
     * Constructor. The associated control will be labelled with an icon and,
     * optionally, the tool name.
     * 
     * @param mapPane the map pane being serviced by this action
     * @param showToolName set to true for the control to display the tool name
     */
    public StickDataSourceAction(MapPane mapPane, boolean showToolName) {
        String toolName = showToolName ? TOOL_NAME : null;

        super.init(mapPane, toolName, TOOL_TIP, ICON_IMAGE);
    }

    /**
     * Called when the control is activated. Calls the map pane to reset the 
     * display 
     *
     * @param ev the event (not used)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        JToggleButton source = (JToggleButton) ev.getSource();
        boolean sticky = source.isSelected();
        SLDEditorFile.getInstance().setStickyDataSource(sticky);
    }
}
