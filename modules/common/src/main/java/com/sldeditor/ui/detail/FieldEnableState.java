package com.sldeditor.ui.detail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.ui.detail.config.FieldId;

/**
 * The Class FieldEnableState, stores the configuration for
 * enabled fields only when a symbol type menu option is selected.
 */
public class FieldEnableState {

    /** The enabled field state map. */
    private Map<String, Map<String, List<FieldId> > > enabledFieldStateMap = new HashMap<String, Map<String, List<FieldId> > >();

    /**
     * Adds the for the given panel the field enable state for a menu option.
     *
     * @param panelName the panel name
     * @param menuOption the menu option
     * @param fieldList the field list
     */
    public void add(String panelName, String menuOption, List<FieldId> fieldList) {
        
        Map<String, List<FieldId> > menuOptionFieldMap = enabledFieldStateMap.get(panelName);
        
        if(menuOptionFieldMap == null)
        {
            menuOptionFieldMap = new HashMap<String, List<FieldId> >();
            enabledFieldStateMap.put(panelName, menuOptionFieldMap);
        }
        
        menuOptionFieldMap.put(menuOption, fieldList);
    }

    /**
     * Gets the field id list for the given panel and for the selected item.
     *
     * @param panelName the panel name
     * @param selectedItem the selected item
     * @return the field id list
     */
    public List<FieldId> getFieldIdList(String panelName, String selectedItem) {
        Map<String, List<FieldId> > panelMap = enabledFieldStateMap.get(panelName);

        if(panelMap != null)
        {
            List<FieldId> list = panelMap.get(selectedItem);
            
            return list;
        }
        
        return null;
    }
}
