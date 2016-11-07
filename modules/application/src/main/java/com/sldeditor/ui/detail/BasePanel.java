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
package com.sldeditor.ui.detail;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigPopulation;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.detail.config.PanelConfigInterface;
import com.sldeditor.ui.detail.config.ReadPanelConfig;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class BasePanel.
 * <p>Notifies the rest of the application if symbol needs redrawing
 * <p>Contains list of field configuration for the panel
 * <p>Defines size, position and spacing of field ui components
 * <p>Reads panel configuration file and configures ui.</li>
 * 
 * @author Robert Ward (SCISYS)
 */
public class BasePanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant WIDGET_X_START. */
    public static final int WIDGET_X_START = 90;

    /** The Constant WIDGET_MAX_WIDTH. */
    public static final int WIDGET_STANDARD_WIDTH = 132;

    /** The Constant WIDGET_TITLE_WIDTH. */
    public static final int WIDGET_TITLE_WIDTH = 202;

    /** The Constant FIELD_PANEL_WIDTH. */
    public static final int FIELD_PANEL_WIDTH = 550;

    /** The Constant WIDGET_EXTENDED_WIDTH. */
    public static final int WIDGET_EXTENDED_WIDTH = 202;

    /** The Constant WIDGET_HEIGHT. */
    public static final int WIDGET_HEIGHT = 24;

    /** The Constant LABEL_WIDTH. */
    public static final int LABEL_WIDTH = 77;

    /** The Constant ATTRIBUTE_BTN_X. */
    public static final int ATTRIBUTE_BTN_X = 230;

    /** The Constant WIDGET_BUTTON_WIDTH. */
    public static final int WIDGET_BUTTON_WIDTH = 89;

    /** The renderer list. */
    private List<RenderSymbolInterface> rendererList = new ArrayList<RenderSymbolInterface>();

    /** The tree update list. */
    private List<SLDTreeUpdatedInterface> treeUpdateList = new ArrayList<SLDTreeUpdatedInterface>();

    /** The style factory. */
    private StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The field configuration list. */
    private List<FieldConfigBase> fieldConfigList = new ArrayList<FieldConfigBase>();

    /** The field config visitor. */
    protected FieldConfigPopulation fieldConfigVisitor = null;

    /** The update symbol listener. */
    private UpdateSymbolInterface updateSymbolListener = null;

    /** The field configuration manager. */
    protected GraphicPanelFieldManager fieldConfigManager = null;

    /** The scroll frame. */
    private JScrollPane scrollFrame = null;

    /** The preferred size. */
    private Dimension preferredSize = null;

    /** The box containing the fields. */
    private Box box;

    /** The logger. */
    private static Logger logger = Logger.getLogger(BasePanel.class);

    /** The panel id. */
    private Class<?> panelId = null;

    /** The vendor option version. */
    private VendorOptionVersion vendorOptionVersion = null;

    /** The map of default field value. */
    private Map<FieldIdEnum, Object> defaultFieldMap = null;

    /** The containing panel. */
    private JPanel containingPanel;

    /** The function manager. */
    private FunctionNameInterface functionManager = null;

    /** The padding component. */
    private BasePanelPadding padding = null;

    /** The group config list. */
    private List<GroupConfigInterface> groupConfigList;

    /**
     * Default constructor.
     *
     * @param panelId the panel id
     * @param functionManager the function manager
     */
    protected BasePanel(Class<?> panelId, FunctionNameInterface functionManager) {
        super();

        this.panelId = panelId;
        this.functionManager = functionManager;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        fieldConfigVisitor = new FieldConfigPopulation(fieldConfigManager);
    }

    /**
     * Gets the function manager.
     *
     * @return the function manager
     */
    protected FunctionNameInterface getFunctionManager()
    {
        return functionManager;
    }

    /**
     * Adds the renderer.
     *
     * @param renderer the renderer
     */
    public void addRenderer(RenderSymbolInterface renderer)
    {
        if(!rendererList.contains(renderer))
        {
            rendererList.add(renderer);
        }
    }

    /**
     * Fire update symbol.
     */
    protected void fireUpdateSymbol()
    {
        if(!Controller.getInstance().isPopulating())
        {
            // Notify that renderers need to be updated
            for(RenderSymbolInterface renderer : rendererList)
            {
                renderer.renderSymbol();
            }

            if(fieldConfigVisitor.isTreeDataUpdated())
            {
                // Notify the SLD tree if any of the data has
                // changed so the text needs to be updated
                for(SLDTreeUpdatedInterface sldTree : treeUpdateList)
                {
                    sldTree.textUpdated();
                }
                fieldConfigVisitor.resetTreeDataUpdated();
            }
        }
    }

    /**
     * Adds the tree update.
     *
     * @param sldTree the sld tree
     */
    public void addTreeUpdate(SLDTreeUpdatedInterface sldTree) {
        treeUpdateList.add(sldTree);
    }

    /**
     * Adds the field configuration to the list of all field configuration for this panel.
     *
     * @param fieldConfig the field configuration
     */
    protected void addFieldConfig(FieldConfigBase fieldConfig) {
        fieldConfigList.add(fieldConfig);
    }

    /**
     * Add all field configuration supplied to the list of field configuration in this panel.
     *
     * @param basePanel the base panel
     */
    public void updateFieldConfig(BasePanel basePanel) {
        if(basePanel != null)
        {
            fieldConfigList.addAll(basePanel.fieldConfigList);
        }
    }

    /**
     * Set up listener to this class is aware when user
     * changes the expression so the symbol can be redrawn.
     *
     * @param fieldList the field list
     * @param obj object to receive the callbacks
     */
    protected void registerForSymbolUpdates(List<FieldConfigBase> fieldList, UpdateSymbolInterface obj) {
        for(FieldConfigBase fieldConfig : fieldList)
        {
            if(fieldConfig != null)
            {
                fieldConfig.addDataChangedListener(obj);
            }
        }
    }

    /**
     * Gets the field config list.
     *
     * @return the field config list
     */
    protected List<FieldConfigBase> getFieldConfigList() {
        return fieldConfigList;
    }

    /**
     * Gets the style factory.
     *
     * @return the style factory
     */
    protected StyleFactoryImpl getStyleFactory()
    {
        return styleFactory;
    }

    /**
     * Gets the filter factory.
     *
     * @return the filter factory
     */
    protected static FilterFactory getFilterFactory() {
        return ff;
    }

    /**
     * Sets the update symbol listener.
     *
     * @param listener the new update symbol listener
     */
    public void setUpdateSymbolListener(UpdateSymbolInterface listener)
    {
        this.updateSymbolListener = listener;
    }

    /**
     * Data changed, called when symbol should be updated.
     */
    protected void dataHasChanged()
    {
        if(updateSymbolListener != null)
        {
            updateSymbolListener.dataChanged(FieldIdEnum.UNKNOWN);
        }
    }

    /**
     * Read panel configuration file.
     *
     * @param vendorOptionFactory the vendor option factory
     * @param parent the parent
     * @param filename the filename
     */
    protected void readConfigFile(VendorOptionFactoryInterface vendorOptionFactory,
            UpdateSymbolInterface parent,
            String filename)
    {
        internal_readConfigFile(vendorOptionFactory, parent.getClass(), parent, filename, true, false);
    }

    /**
     * Read raster panel configuration file.
     *
     * @param vendorOptionFactory the vendor option factory
     * @param parent the parent
     * @param filename the filename
     */
    protected void readRasterConfigFile(VendorOptionFactoryInterface vendorOptionFactory,
            UpdateSymbolInterface parent, String filename)
    {
        internal_readConfigFile(vendorOptionFactory, parent.getClass(), parent, filename, true, true);
    }

    /**
     * Read panel configuration file no scroll pane.
     *
     * @param vendorOptionFactory the vendor option factory
     * @param parent the parent
     * @param filename the filename
     */
    protected void readConfigFileNoScrollPane(VendorOptionFactoryInterface vendorOptionFactory,
            UpdateSymbolInterface parent, String filename)
    {
        internal_readConfigFile(vendorOptionFactory, parent.getClass(), parent, filename, false, false);
    }

    /**
     * Read panel configuration file and create user interface including scroll pane.
     *
     * @param vendorOptionFactory the vendor option factory
     * @param panelId the panel id
     * @param parent the parent
     * @param filename the filename
     * @param useScrollFrame the use scroll frame
     * @param isRasterSymbol the is raster symbol
     */
    private void internal_readConfigFile(VendorOptionFactoryInterface vendorOptionFactory,
            Class<?> panelId, 
            UpdateSymbolInterface parent,
            String filename,
            boolean useScrollFrame,
            boolean isRasterSymbol) {

        ReadPanelConfig readConfig = new ReadPanelConfig(vendorOptionFactory, isRasterSymbol);

        readConfig.read(panelId, filename);

        configureUI(parent, useScrollFrame, readConfig);
    }

    /**
     * Configure ui.
     *
     * @param parent the parent
     * @param useScrollFrame the use scroll frame
     * @param config the config
     */
    private void configureUI(UpdateSymbolInterface parent,
            boolean useScrollFrame,
            PanelConfigInterface config) {
        groupConfigList = config.getGroupList();
        vendorOptionVersion = config.getVendorOptionVersion();
        defaultFieldMap = config.getDefaultFieldMap();

        setBorder(BorderFactory.createTitledBorder(config.getPanelTitle()));

        if(containingPanel == null)
        {
            setLayout(new BorderLayout());

            containingPanel = new JPanel();
            containingPanel.setLayout(new BorderLayout());

            box = Box.createVerticalBox();
            padding = new BasePanelPadding(box);

            containingPanel.add(box, BorderLayout.CENTER);
        }

        for(GroupConfigInterface groupConfig : groupConfigList)
        {
            populateGroup(parent, box, groupConfig, null);
        }

        if(scrollFrame == null)
        {
            if(useScrollFrame)
            {
                scrollFrame = new JScrollPane(containingPanel);
                containingPanel.setAutoscrolls(true);

                // Create a vertical strut so all the fields are pushed to the top of the panel
                Dimension boxSize = padding.addPadding();
                scrollFrame.setPreferredSize(boxSize);
                preferredSize = boxSize;

                add(scrollFrame, BorderLayout.CENTER);
            }
            else
            {
                add(containingPanel, BorderLayout.CENTER);
                preferredSize = this.getPreferredSize();
            }
        }
    }

    /**
     * Populate group.
     *
     * @param parent the parent
     * @param parentBox the parent box
     * @param groupConfig the group config
     * @param parentField the parent field
     * @param previousFieldIndex the previous field index
     */
    private void populateGroup(UpdateSymbolInterface parent, 
            Box parentBox,
            GroupConfigInterface groupConfig, 
            FieldConfigBase parentField)
    {
        groupConfig.createTitle(parentBox, parent);

        if(groupConfig instanceof GroupConfig)
        {
            GroupConfig group = (GroupConfig) groupConfig;
            List<FieldConfigBase> fieldList = group.getFieldConfigList();

            fieldConfigManager.addGroup(group);

            // Register for notifications when data has changed
            registerForSymbolUpdates(fieldList, parent);

            // Create field user interface
            for(FieldConfigBase field : fieldList)
            {
                field.setParent(parentField);
                addField(parentBox, parentField, field);
            }

            for(GroupConfigInterface subGroup : group.getSubGroupList())
            {
                populateGroup(parent, parentBox, subGroup, parentField);
            }
        }
        else if(groupConfig instanceof MultiOptionGroup)
        {
            MultiOptionGroup multiOption = (MultiOptionGroup) groupConfig;

            fieldConfigManager.addMultiOptionGroup(multiOption);

            multiOption.createUI(fieldConfigManager, parentBox, parent, parent.getClass());

            for(OptionGroup optionGroup : multiOption.getGroupList())
            {
                for(GroupConfigInterface optionGroupConfig : optionGroup.getGroupList())
                {
                    if(optionGroupConfig instanceof GroupConfig)
                    {
                        populateOptionGroup(parent, (GroupConfig)optionGroupConfig);
                    }
                }
            }
        }
    }

    /**
     * Populate option group.
     *
     * @param parent the parent
     * @param optionGroupConfig the option group config
     */
    private void populateOptionGroup(UpdateSymbolInterface parent, GroupConfig optionGroupConfig) {
        List<FieldConfigBase> fieldList = optionGroupConfig.getFieldConfigList();

        fieldConfigManager.addGroup(optionGroupConfig);

        // Create field user interface
        for(FieldConfigBase field : fieldList)
        {
            addFieldConfig(field);

            fieldConfigManager.addField(field);
        }

        // Register for notifications when data has changed
        registerForSymbolUpdates(fieldList, parent);

        for(GroupConfigInterface subOptionGroupConfig : optionGroupConfig.getSubGroupList())
        {
            if(subOptionGroupConfig instanceof GroupConfig)
            {
                populateOptionGroup(parent, (GroupConfig) subOptionGroupConfig);
            }
            else if(subOptionGroupConfig instanceof MultiOptionGroup)
            {
                MultiOptionGroup multiOption = (MultiOptionGroup) subOptionGroupConfig;

                fieldConfigManager.addMultiOptionGroup(multiOption);

                for(OptionGroup optionGroup : multiOption.getGroupList())
                {
                    for(GroupConfigInterface subMultiOptionGroupConfig : optionGroup.getGroupList())
                    {
                        if(subMultiOptionGroupConfig instanceof GroupConfig)
                        {
                            populateOptionGroup(parent, (GroupConfig)subMultiOptionGroupConfig);
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds the field.
     *
     * @param parentBox the parent box
     * @param parentField the parent field
     * @param field the field
     */
    private void addField(Box parentBox, FieldConfigBase parentField, FieldConfigBase field) {

        if(field != null)
        {
            field.createUI();
            addFieldConfig(field);

            fieldConfigManager.addField(field);

            if(parentBox != null)
            {
                parentBox.add(field.getPanel());

                // Add any custom panels
                if(field.getCustomPanels() != null)
                {
                    for(Component component : field.getCustomPanels())
                    {
                        parentBox.add(component);
                    }
                }
            }
        }
    }

    /**
     * Handle field state for a specific combo box field.
     *
     * @param fieldId the field id
     */
    protected void handleFieldState(FieldIdEnum fieldId)
    {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            if(fieldConfig instanceof FieldConfigEnum)
            {
                FieldConfigEnum fieldEnum = (FieldConfigEnum)fieldConfig;

                Map<FieldIdEnum, Boolean> stateMap = fieldEnum.getFieldEnableState();
                if(stateMap != null)
                {
                    for(FieldIdEnum fieldKey : stateMap.keySet())
                    {
                        enableField(fieldKey, stateMap.get(fieldKey));
                    }
                }
            }
        }
        else
        {
            ConsoleManager.getInstance().error(this, String.format("handleFieldState - %s : %s", Localisation.getString(StandardPanel.class, "StandardPanel.unknownField") , fieldId));
        }
    }

    /**
     * Handle field state for all combo box fields.
     */
    protected void handleFieldState()
    {
        List<FieldConfigBase> fieldList = fieldConfigManager.getFields(FieldConfigEnum.class);
        for(FieldConfigBase field : fieldList)
        {
            FieldConfigEnum fieldEnum = (FieldConfigEnum)field;

            Map<FieldIdEnum, Boolean> stateMap = fieldEnum.getFieldEnableState();
            if(stateMap != null)
            {
                for(FieldIdEnum fieldKey : stateMap.keySet())
                {
                    enableField(fieldKey, stateMap.get(fieldKey));
                }
            }
        }
    }

    /**
     * Enable field.
     *
     * @param fieldId the field id
     * @param enable the enable flag
     */
    protected void enableField(FieldIdEnum fieldId, boolean enable)
    {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.setEnabled(enable);
        }
        else
        {
            ConsoleManager.getInstance().error(this, String.format("enableField - %s : %s", Localisation.getString(StandardPanel.class, "StandardPanel.unknownField") , fieldId));
        }
    }

    /**
     * Sets the default value for a field.
     *
     * @param fieldId the field id
     */
    protected void setDefaultValue(FieldIdEnum fieldId)
    {
        if(fieldConfigManager != null)
        {
            FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                fieldConfig.revertToDefaultValue();
            }
        }
    }

    /**
     * Register for text field button.
     *
     * @param fieldId the field id
     * @param listener the listener
     */
    protected void registerForTextFieldButton(FieldIdEnum fieldId, FieldConfigStringButtonInterface listener)
    {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            FieldConfigString textField = (FieldConfigString)fieldConfig;

            textField.addButtonPressedListener(listener);
        }
    }

    /**
     * Gets the group configuration using its id.
     *
     * @param groupId the group id
     * @return the group
     */
    public GroupConfigInterface getGroup(GroupIdEnum groupId)
    {
        return fieldConfigManager.getGroup(getClass(), groupId);
    }

    /**
     * Checks if is panel enabled.
     *
     * @param groupId the group id
     * @return true, if is panel enabled
     */
    protected boolean isPanelEnabled(GroupIdEnum groupId)
    {
        boolean isEnabled = false;
        GroupConfigInterface group = getGroup(groupId);
        if(group != null)
        {
            isEnabled = group.isPanelEnabled();
        }
        return isEnabled;
    }
    /**
     * Gets the scroll frame.
     *
     * @return the scroll frame
     */
    protected JScrollPane getScrollFrame()
    {
        return scrollFrame;
    }

    /**
     * Insert panel.
     *
     * @param fieldConfig the field config
     * @param panel the panel
     * @param optionBox the option box
     */
    public void insertPanel(FieldConfigBase fieldConfig, BasePanel panel, Box optionBox)
    {
        int fieldIndex = -1;

        Box boxToUpdate = null;
        if(optionBox != null)
        {
            boxToUpdate = optionBox;
        }
        else
        {
            boxToUpdate = box;
        }

        if(boxToUpdate != null)
        {
            for(int index = 0; index < boxToUpdate.getComponentCount(); index ++)
            {
                Component component = boxToUpdate.getComponent(index);
                if(fieldConfig.getPanel() == component)
                {
                    fieldIndex = index;
                    break;
                }
            }

            if(panel != null)
            {
                if(padding != null)
                {
                    padding.removePadding();
                }

                logger.debug(String.format("%s : %s -> %s", 
                        Localisation.getString(StandardPanel.class, "StandardPanel.addingPanel"),
                        panel.getClass().getName(),
                        this.getClass().getName()));

                if(fieldIndex > -1)
                {
                    fieldIndex ++;
                }
                
                boxToUpdate.add(panel, fieldIndex);

                if(padding != null)
                {
                    padding.addPadding();
                }
            }
        }
    }

    /**
     * Removes the panel.
     *
     * @param panel the panel
     */
    public void removePanel(BasePanel panel)
    {
        if((panel != null) && (padding != null))
        {
            padding.removePadding();
            box.remove(panel);
            padding.addPadding();
        }
    }

    /**
     * Gets the panel size.
     *
     * @return the panel size
     */
    public Dimension getPanelSize() {
        return preferredSize;
    }

    /**
     * Gets the panel id.
     *
     * @return the panelId
     */
    public Class<?> getPanelId() {
        return panelId;
    }

    /**
     * Gets the vendor option version.
     *
     * @return the vendorOptionVersion
     */
    protected VendorOptionVersion getVendorOptionVersion() {
        return vendorOptionVersion;
    }

    /**
     * Gets the default value for the given field.
     *
     * @param fieldId the field id
     * @return the default field value
     */
    protected Object getDefaultFieldValue(FieldIdEnum fieldId)
    {
        return defaultFieldMap.get(fieldId);
    }

    /**
     * Refresh panel.
     */
    public void refreshPanel() {
        revalidate();
        repaint();
    }

    /**
     * Sets the default values for all the fields.
     */
    protected void setAllDefaultValues() {
        for(FieldConfigBase fieldConfig : fieldConfigList)
        {
            if(fieldConfig != null)
            {
                fieldConfig.revertToDefaultValue();
            }
        }
    }
}
