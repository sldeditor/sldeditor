
package com.sldeditor.ui.detail;

import java.util.HashMap;
import java.util.Map;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;

/**
 * The Class FieldEnableState, stores the configuration for enabled fields/groups
 *  only when a symbol type menu option is selected.
 */
public class FieldEnableState {

    /** The enabled field state map. */
    private Map<String, Map<String, Map<FieldIdEnum, Boolean>>> enabledFieldStateMap =
            new HashMap<String, Map<String, Map<FieldIdEnum, Boolean>>>();

    /** The enabled group state map. */
    private Map<String, Map<String, Map<GroupIdEnum, Boolean>>> enabledGroupStateMap =
            new HashMap<String, Map<String, Map<GroupIdEnum, Boolean>>>();

    /**
     * Adds the for the given panel the field enable state for a menu option.
     *
     * @param panelName the panel name
     * @param menuOption the menu option
     * @param fieldList the field list
     * @param groupList the group list
     */
    public void add(String panelName, String menuOption, Map<FieldIdEnum, Boolean> fieldList,
            Map<GroupIdEnum, Boolean> groupList) {

        // Add field id
        Map<String, Map<FieldIdEnum, Boolean>> menuOptionFieldMap = enabledFieldStateMap
                .get(panelName);

        if (menuOptionFieldMap == null) {
            menuOptionFieldMap = new HashMap<String, Map<FieldIdEnum, Boolean>>();
            enabledFieldStateMap.put(panelName, menuOptionFieldMap);
        }

        menuOptionFieldMap.put(menuOption, fieldList);

        // Add group id
        Map<String, Map<GroupIdEnum, Boolean>> menuOptionGroupMap = enabledGroupStateMap
                .get(panelName);
        if (menuOptionGroupMap == null) {
            menuOptionGroupMap = new HashMap<String, Map<GroupIdEnum, Boolean>>();
            enabledGroupStateMap.put(panelName, menuOptionGroupMap);
        }

        menuOptionGroupMap.put(menuOption, groupList);
    }

    /**
     * Gets the field id list for the given panel and for the selected item.
     *
     * @param panelName the panel name
     * @param selectedItem the selected item
     * @return the field id list
     */
    public Map<FieldIdEnum, Boolean> getFieldIdList(String panelName, String selectedItem) {
        Map<String, Map<FieldIdEnum, Boolean>> panelMap = enabledFieldStateMap.get(panelName);

        if (panelMap != null) {
            return panelMap.get(selectedItem);
        }

        return null;
    }

    /**
     * Gets the group id list for the given panel and for the selected item.
     *
     * @param panelName the panel name
     * @param selectedItem the selected item
     * @return the group id list
     */
    public Map<GroupIdEnum, Boolean> getGroupIdList(String panelName, String selectedItem) {
        Map<String, Map<GroupIdEnum, Boolean>> panelMap = enabledGroupStateMap.get(panelName);

        if (panelMap != null) {
            return panelMap.get(selectedItem);
        }

        return null;
    }
}
