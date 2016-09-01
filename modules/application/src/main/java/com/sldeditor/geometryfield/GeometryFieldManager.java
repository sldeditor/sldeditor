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

package com.sldeditor.geometryfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayer;

import com.sldeditor.common.data.SelectedSymbol;

/**
 *  
 * Class implemented as a singleton.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeometryFieldManager {

    /** The Constant DEFAULT_VISIBILITY_VALUE. */
    private static final Boolean DEFAULT_VISIBILITY_VALUE = Boolean.TRUE;

    /** The sld component map. */
    private Map<Object, Boolean> sldComponentMap = new HashMap<Object, Boolean>();

    /** The sld component parent map. */
    private Map<Object, Object> sldComponentParentMap = new HashMap<Object, Object>();

    /** The sld component child map. */
    private Map<Object, List<Object>> sldComponentChildMap = new HashMap<Object, List<Object>>();

    /** The sld component state map. */
    private Map<Object, Boolean> sldComponentStateMap = new HashMap<Object, Boolean>();

    /** The singleton instance. */
    private static GeometryFieldManager singletonInstance = null;

    /** The sld. */
    private StyledLayerDescriptor sld = null;

    /**
     * Default constructor.
     */
    private GeometryFieldManager()
    {
    }

    /**
     * Gets the single instance of GeometryFieldManager.
     *
     * @return single instance of GeometryFieldManager
     */
    public static synchronized GeometryFieldManager getInstance()
    {
        if(singletonInstance == null)
        {
            singletonInstance = new GeometryFieldManager();
        }
        return singletonInstance;
    }

    /**
     * Populate internal data structures.
     *
     * @param component the component
     * @param parentComponent the parent component
     */
    private void populate(Object component, Object parentComponent)
    {
        sldComponentMap.put(component, DEFAULT_VISIBILITY_VALUE);
        sldComponentParentMap.put(component, parentComponent);

        List<Object> childList = null;
        if(sldComponentChildMap.containsKey(parentComponent))
        {
            childList = sldComponentChildMap.get(parentComponent);
        }
        else
        {
            childList = new ArrayList<Object>();
        }
        childList.add(component);
        sldComponentChildMap.put(parentComponent, childList);
        sldComponentStateMap.put(component, getEnabledState(component));
    }

    /**
     * Gets the enabled state.
     *
     * @param component the component
     * @return the enabled state
     */
    private boolean getEnabledState(Object component)
    {
        // Build up list of enable states from the sld component up 
        // to the root StyledLayerDescriptor.
        // If any 'false' values are found then the userObject is disabled.
        List<Boolean> enabledTreePathList = new ArrayList<Boolean>();

        while(component != null)
        {
            Boolean isEnabled = sldComponentMap.get(component);
            enabledTreePathList.add(isEnabled);
            component = sldComponentParentMap.get(component);
        }

        return !enabledTreePathList.contains(false);
    }

    /**
     * New symbol.
     */
    public void newSymbol()
    {
        sldComponentMap.clear();
        sldComponentParentMap.clear();
        sldComponentStateMap.clear();
        sldComponentChildMap.clear();

        sld = SelectedSymbol.getInstance().getSld();
        populate(sld, null);

        for(StyledLayer styledLayer : sld.layers())
        {
            populate(styledLayer, sld);

            List<Style> styleList = null;
            if(styledLayer instanceof NamedLayer)
            {
                NamedLayer namedLayer = (NamedLayer) styledLayer;

                styleList = namedLayer.styles();
            }
            else if(styledLayer instanceof UserLayer)
            {
                UserLayer userLayer = (UserLayer) styledLayer;
                styleList = userLayer.userStyles();
            }

            if(styleList != null)
            {
                for(Style style : styleList)
                {
                    populate(style, styledLayer);
                    for(FeatureTypeStyle fts : style.featureTypeStyles())
                    {
                        populate(fts, style);
                        for(Rule rule : fts.rules())
                        {
                            populate(rule, fts);
                            for(Symbolizer symbolizer : rule.symbolizers())
                            {
                                populate(symbolizer, rule);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds the symbolizer.
     *
     * @param symbolizer the symbolizer
     */
    public void addSymbolizer(Symbolizer symbolizer)
    {
        if(symbolizer != null)
        {
            sldComponentMap.put(symbolizer, DEFAULT_VISIBILITY_VALUE);
        }
    }

    /**
     * Removes the symbolizer.
     *
     * @param symbolizer the symbolizer
     */
    public void removeSymbolizer(Symbolizer symbolizer)
    {
        if(symbolizer != null)
        {
            sldComponentMap.remove(symbolizer);
        }
    }

    /**
     * Replaces the symbolizer.
     *
     * @param oldSymbolizer the old symbolizer
     * @param newSymbolizer the new symbolizer
     */
    public void replaceSymbolizer(Symbolizer oldSymbolizer, Symbolizer newSymbolizer)
    {
        if((oldSymbolizer != null) && (newSymbolizer != null))
        {
            Boolean visibilityValue = sldComponentMap.remove(oldSymbolizer);
            sldComponentMap.put(newSymbolizer, visibilityValue);
        }
    }

    /**
     * Checks if is selected.
     *
     * @param userObject the user object
     * @return true, if is selected
     */
    public synchronized boolean isSelected(Object userObject) {
        if((sldComponentMap != null) && !sldComponentMap.isEmpty())
        {
            if(userObject instanceof String)
            {
                return sldComponentMap.get(sld);
            }
            return sldComponentMap.get(userObject);
        }

        return false;
    }

    /**
     * Sets the selected.
     *
     * @param userObject the user object
     * @param selected the selected
     */
    public void setSelected(Object userObject, boolean selected) {
        sldComponentMap.put(userObject, selected);

        List<Object> affectedComponentList = new ArrayList<Object>();

        // Get all the components above the selected in the tree
        Object component = userObject;

        if(userObject instanceof String)
        {
            component = sld;
        }

        while(component != null)
        {
            affectedComponentList.add(component);
            component = sldComponentParentMap.get(component);
        }

        // Start again and traverse down the tree
        component = userObject;

        if(userObject instanceof String)
        {
            component = sld;
        }

        traverseDown(sldComponentChildMap.get(component), affectedComponentList);

        for(Object obj : affectedComponentList)
        {
            sldComponentStateMap.put(obj, getEnabledState(obj));
        }
    }

    /**
     * Traverse down.
     *
     * @param list the list
     * @param affectedComponentList the affected component list
     */
    private void traverseDown(List<Object> list, List<Object> affectedComponentList) {
        if(list != null)
        {
            for(Object component : list)
            {
                affectedComponentList.add(component);
                traverseDown(sldComponentChildMap.get(component), affectedComponentList);
            }
        }
    }

    /**
     * Checks if sld component is enabled.
     *
     * @param userObject the user object
     * @return true, if is enabled
     */
    public synchronized boolean isEnabled(Object userObject) {
        if((sldComponentStateMap != null) && !sldComponentStateMap.isEmpty())
        {
            Object component = userObject;

            if(userObject instanceof String)
            {
                component = sld;
            }

            Object parentComponent = sldComponentParentMap.get(component);

            boolean parentEnabled = true;

            if(parentComponent != null)
            {
                parentEnabled = sldComponentStateMap.get(parentComponent);
            }

            return !(!parentEnabled && !sldComponentStateMap.get(component));
        }
        return false;
    }
}
