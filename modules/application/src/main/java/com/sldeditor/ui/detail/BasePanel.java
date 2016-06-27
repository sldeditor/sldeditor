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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpression;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigPopulation;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.PanelConfigInterface;
import com.sldeditor.ui.detail.config.ReadPanelConfig;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
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
public class BasePanel extends JPanel implements MultipleFieldInterface {

    private static final int PANEL_HEIGHT = 750;

    /** The Constant LAST_FIELD_INDEX. */
    private static final int LAST_FIELD_INDEX = -1;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant WIDGET_X_START. */
    public static final int WIDGET_X_START = 90;

    /** The Constant WIDGET_MAX_WIDTH. */
    public static final int WIDGET_STANDARD_WIDTH = 132;

    /** The Constant WIDGET_TITLE_WIDTH. */
    public static final int WIDGET_TITLE_WIDTH = 202;

    /** The Constant FIELD_PANEL_WIDTH. */
    public static final int FIELD_PANEL_WIDTH = 500;

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

    /** The group config map. */
    private Map<GroupIdEnum, GroupConfigInterface> groupConfigMap = new HashMap<GroupIdEnum, GroupConfigInterface>();

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
    private Map<FieldId, Object> defaultFieldMap = null;

    /** The containing panel. */
    private JPanel containingPanel;

    /** The multiple field handler. */
    private MultipleFieldHandler multipleFieldHandler = new MultipleFieldHandler();

    /** The function manager. */
    private FunctionNameInterface functionManager = null;

    private Component padding;

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
     * Adds the field configuration to the list of field configuration on the panel.
     * <p>Uses no vendor option.
     *
     * @param fieldConfig the field config
     */
    protected void addFieldConfig(FieldConfigBase fieldConfig) {
        addFieldConfig(VendorOptionManager.getInstance().getDefaultVendorOptionVersion(), fieldConfig);
    }

    /**
     * Adds the field configuration to the list of all field configuration for this panel.
     *
     * @param vendorOption the vendor option
     * @param fieldConfig the field configuration
     */
    protected void addFieldConfig(VendorOptionVersion vendorOption, FieldConfigBase fieldConfig) {
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
            updateSymbolListener.dataChanged(FieldId.getUnknownValue());
        }
    }

    /**
     * Read panel configuration file.
     *
     * @param parent the parent
     * @param filename the filename
     */
    protected void readConfigFile(UpdateSymbolInterface parent, String filename)
    {
        internal_readConfigFile(parent.getClass(), parent, filename, true, null);
    }

    /**
     * Read config file.
     *
     * @param parent the parent
     * @param filename the filename
     * @param fieldType the field type
     */
    protected void readConfigFile(UpdateSymbolInterface parent, String filename, Class<?> fieldType)
    {
        internal_readConfigFile(parent.getClass(), parent, filename, true, fieldType);
    }

    /**
     * Read panel configuration file no scroll pane.
     *
     * @param parent the parent
     * @param filename the filename
     */
    protected void readConfigFileNoScrollPane(UpdateSymbolInterface parent, String filename)
    {
        internal_readConfigFile(parent.getClass(), parent, filename, false, null);
    }

    /**
     * Read panel configuration file and create user interface including scroll pane.
     *
     * @param panelId the panel id
     * @param parent the parent
     * @param filename the filename
     * @param useScrollFrame the use scroll frame
     * @param fieldType the field type
     */
    private void internal_readConfigFile(Class<?> panelId, UpdateSymbolInterface parent, String filename, boolean useScrollFrame, Class<?> fieldType) {

        ReadPanelConfig readConfig = new ReadPanelConfig();

        readConfig.setFieldType(fieldType);

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
        List<GroupConfigInterface> groupConfigList = config.getGroupList();
        vendorOptionVersion = config.getVendorOptionVersion();
        defaultFieldMap = config.getDefaultFieldMap();

        setBorder(BorderFactory.createTitledBorder(config.getPanelTitle()));

        if(containingPanel == null)
        {
            setLayout(new BorderLayout());

            containingPanel = new JPanel();
            containingPanel.setLayout(new BorderLayout());

            box = Box.createVerticalBox();

            containingPanel.add(box, BorderLayout.CENTER);
        }

        for(GroupConfigInterface groupConfig : groupConfigList)
        {
            populateGroup(parent, box, LAST_FIELD_INDEX, groupConfig, null, false);
        }

        if(scrollFrame == null)
        {
            if(useScrollFrame)
            {
                scrollFrame = new JScrollPane(containingPanel);
                containingPanel.setAutoscrolls(true);

                // Create a vertical strut so all the fields are pushed to the top of the panel
                Dimension boxSize = addPadding();
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
     * Adds the padding.
     *
     * @return the dimension
     */
    private Dimension addPadding() {
        Dimension boxSize = box.getPreferredSize();
        padding = Box.createVerticalStrut(PANEL_HEIGHT - (int)boxSize.getHeight());
        box.add(padding);
        return boxSize;
    }

    /**
     * Removes the padding.
     */
    private void removePadding()
    {
        box.remove(padding);
    }

    /**
     * Populate group.
     *
     * @param parent the parent
     * @param parentBox the parent box
     * @param index the index
     * @param groupConfig the group config
     * @param parentField the parent field
     * @param isFunction the is function flag
     */
    private void populateGroup(UpdateSymbolInterface parent, 
            Box parentBox,
            int index,
            GroupConfigInterface groupConfig, 
            FieldConfigBase parentField,
            boolean isFunction) {
        index = groupConfig.createTitle(parentBox, index, parent);
        groupConfigMap.put(groupConfig.getId(), groupConfig);

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
                addField(parentBox,
                        isFunction ? index : LAST_FIELD_INDEX, // If we are not adding function fields then append
                                parentField,
                                field);
                index ++;
            }

            for(GroupConfig subGroup : group.getSubGroupList())
            {
                populateGroup(parent, parentBox, index, subGroup, parentField, isFunction);
            }
        }
        else if(groupConfig instanceof MultiOptionGroup)
        {
            MultiOptionGroup multiOption = (MultiOptionGroup) groupConfig;

            fieldConfigManager.addMultiOptionGroup(multiOption);

            List<FieldConfigBase> multiOptionFieldConfigList = multiOption.createUI(fieldConfigManager, parentBox, parent.getClass());
            for(FieldConfigBase fieldConfig : multiOptionFieldConfigList)
            {
                addFieldConfig(fieldConfig);
                fieldConfigManager.addField(fieldConfig);
            }

            for(OptionGroup optionGroup : multiOption.getGroupList())
            {
                for(GroupConfig optionGroupConfig : optionGroup.getGroupList())
                {
                    List<FieldConfigBase> fieldList = optionGroupConfig.getFieldConfigList();

                    fieldConfigManager.addGroup(optionGroupConfig);

                    // Register for notifications when data has changed
                    registerForSymbolUpdates(fieldList, parent);

                    groupConfigMap.put(optionGroupConfig.getId(), optionGroupConfig);

                    for(GroupConfig subOptionGroupConfig : optionGroupConfig.getSubGroupList())
                    {
                        fieldConfigManager.addGroup(subOptionGroupConfig);
                        groupConfigMap.put(subOptionGroupConfig.getId(), subOptionGroupConfig);
                    }
                }
            }
        }
    }

    /**
     * Adds the field.
     *
     * @param parentBox the parent box
     * @param index the index
     * @param parentField the parent field
     * @param field the field
     */
    @Override
    public void addField(Box parentBox, int index, FieldConfigBase parentField, FieldConfigBase field) {

        boolean isFunction = (index != LAST_FIELD_INDEX);
        if(parentField != null)
        {
            parentField.addFunction(field);
        }

        multipleFieldHandler.addField(field);

        field.createUI(this, parentBox);
        addFieldConfig(field);

        fieldConfigManager.addField(field);

        parentBox.add(field.getPanel(), index);

        // Add any custom panels
        if(field.getCustomPanels() != null)
        {
            for(Component component : field.getCustomPanels())
            {
                index ++;
                parentBox.add(component, isFunction ? index : LAST_FIELD_INDEX);
            }
        }
    }

    /**
     * Handle field state for a specific combo box field.
     *
     * @param fieldId the field id
     */
    protected void handleFieldState(FieldId fieldId)
    {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            if(fieldConfig instanceof FieldConfigEnum)
            {
                FieldConfigEnum fieldEnum = (FieldConfigEnum)fieldConfig;

                Map<FieldId, Boolean> stateMap = fieldEnum.getFieldEnableState();
                if(stateMap != null)
                {
                    for(FieldId fieldKey : stateMap.keySet())
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

            Map<FieldId, Boolean> stateMap = fieldEnum.getFieldEnableState();
            if(stateMap != null)
            {
                for(FieldId fieldKey : stateMap.keySet())
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
    protected void enableField(FieldId fieldId, boolean enable)
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
    protected void setDefaultValue(FieldId fieldId)
    {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);

        fieldConfig.revertToDefaultValue();
    }

    /**
     * Register for text field button.
     *
     * @param fieldId the field id
     * @param listener the listener
     */
    protected void registerForTextFieldButton(FieldId fieldId, FieldConfigStringButtonInterface listener)
    {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        FieldConfigString textField = (FieldConfigString)fieldConfig;

        textField.addButtonPressedListener(listener);
    }

    /**
     * Gets the group configuration using its id.
     *
     * @param groupId the group id
     * @return the group
     */
    protected GroupConfigInterface getGroup(GroupIdEnum groupId)
    {
        return groupConfigMap.get(groupId);
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
     * Append panel.
     *
     * @param panel the panel
     */
    protected void appendPanel(BasePanel panel)
    {
        removePadding();

        logger.debug(String.format("%s : %s -> %s", Localisation.getString(StandardPanel.class, "StandardPanel.addingPanel"), panel.getClass().getName(), this.getClass().getName()));

        for(int index = 0; index < panel.box.getComponentCount(); index ++)
        {
            box.add(panel.box.getComponent(index));
        }
        addPadding();
    }

    /**
     * Removes the panel.
     *
     * @param panel the panel
     */
    protected void removePanel(BasePanel panel)
    {
        removePadding();
        box.remove(panel.box);
        addPadding();
    }

    /**
     * Clear box.
     */
    protected void clearBox()
    {
        if(box != null)
        {
            box.removeAll();
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
    protected Object getDefaultFieldValue(FieldId fieldId)
    {
        return defaultFieldMap.get(fieldId);
    }

    /**
     * Refresh panel.
     */
    @Override
    public void refreshPanel() {
        revalidate();
        repaint();
    }

    /**
     * Removes the field.
     *
     * @param parentBox the parent box
     * @param field the field
     */
    @Override
    public void removeField(Box parentBox, FieldConfigBase field) {
        if(parentBox != null)
        {
            multipleFieldHandler.removeField(field);
            parentBox.remove(field.getPanel());
            parentBox.revalidate();
        }
    }

    /**
     * Insert group config.
     *
     * @param panelId the panel id
     * @param parent the parent
     * @param functionExpression the function expression
     * @param fieldConfig the field config to insert after
     * @param isFunction the is function
     */
    public void insertGroupConfig(Class<?> panelId,
            UpdateSymbolInterface parent,
            FunctionExpression functionExpression,
            FieldConfigBase fieldConfig,
            boolean isFunction)
    {
        int insertIndex = LAST_FIELD_INDEX;

        for(int index = 0; index < box.getComponentCount(); index ++)
        {
            Component component = box.getComponent(index);

            if(component == fieldConfig.getPanel())
            {
                insertIndex = index;
                break;
            }
        }

        if(insertIndex > LAST_FIELD_INDEX)
        {
            // Remove existing functions
            List<FieldConfigBase> listOfFieldsToRemove = fieldConfig.getAllFunctionFields();

            for(FieldConfigBase functionField : listOfFieldsToRemove)
            {
                removeField(box, functionField);
            }

            fieldConfig.removeFunctionFields();

            // Insert new function
            if(functionExpression != null)
            {
                List<GroupConfigInterface> groupConfigList = functionManager.convertParameters(panelId, fieldConfig.getFieldId(), functionExpression.getFunctionName());

                fieldConfig.setGroupComponents(groupConfigList);
                ArrayList<GroupConfigInterface> copyOfGroupConfigList = new ArrayList<GroupConfigInterface>(groupConfigList);
                for(GroupConfigInterface groupConfig : copyOfGroupConfigList)
                {
                    insertIndex ++;
                    populateGroup(parent, box, insertIndex, groupConfig, fieldConfig, true);
                }

                if(fieldConfig.getFunctionFields() != null)
                {
                    int fieldIndex = 0;
                    populateFunctionFields(fieldIndex, fieldConfig.getFunctionFields(), functionExpression.getParameters());
                }
            }

            Dimension boxSize = box.getPreferredSize();
            scrollFrame.setPreferredSize(boxSize);
            preferredSize = boxSize;

            refreshPanel();
        }
    }

    /**
     * Populate function fields.
     *
     * @param fieldIndex the field index
     * @param functionFields the function fields
     * @param parameters the parameters
     */
    private void populateFunctionFields(int fieldIndex, List<FieldConfigBase> functionFields,
            List<Expression> parameters) {
        int parameterIndex = 0;
        while(fieldIndex < functionFields.size())
        {
            FieldConfigBase fieldConfig = functionFields.get(fieldIndex);

            if(parameterIndex >= parameters.size())
            {
                return;
            }
            Expression expression = parameters.get(parameterIndex);

            fieldConfig.populate(expression);

            parameterIndex ++;
            fieldIndex ++;
        }
    }

    /**
     * Adds the first field.
     *
     * @param fieldConfig the field config
     */
    public void addFirstField(FieldConfigBase fieldConfig) {
        addField(box, 0, null, fieldConfig);
    }
}
