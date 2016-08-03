/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.ui.tree.item;

import java.util.HashMap;
import java.util.Map;

import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.FillImpl;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.StyledLayerDescriptorImpl;
import org.geotools.styling.TextSymbolizerImpl;
import org.geotools.styling.UserLayerImpl;

/**
 * The Class TreeItemMap.
 *
 * @author Robert Ward (SCISYS)
 */
/**
 * @author Robert Ward (SCISYS)
 *
 */
public class TreeItemMap {

    /** The tree item map. */
    private Map<Class<?>, SLDTreeItemInterface> treeItemMap = new HashMap<Class<?>, SLDTreeItemInterface>();

    /** The rule tree item. */
    private RuleTreeItem ruleTreeItem = new RuleTreeItem();

    /** The symbolizer tree item. */
    private SymbolizerTreeItem symbolizerTreeItem = new SymbolizerTreeItem();

    /** The singleton instance. */
    private static TreeItemMap instance = null;

    /**
     * Instantiates a new tree item map.
     */
    private TreeItemMap()
    {
        createTreeItemMap();
    }

    /**
     * Creates the tree item map.
     */
    private void createTreeItemMap() {

        if(treeItemMap.isEmpty())
        {
            /** The sld tree item. */
            StyledLayerDescriptorTreeItem sldTreeItem = new StyledLayerDescriptorTreeItem();

            /** The style tree item. */
            StyleTreeItem styleTreeItem = new StyleTreeItem();

            /** The fts tree item. */
            FeatureTypeStyleTreeItem ftsTreeItem = new FeatureTypeStyleTreeItem();

            /** The name layer tree item. */
            NameLayerTreeItem nameLayerTreeItem = new NameLayerTreeItem();

            /** The user layer tree item. */
            UserLayerTreeItem userLayerTreeItem = new UserLayerTreeItem();

            /** The fill tree item. */
            FillTreeItem fillTreeItem = new FillTreeItem();

            /** The stroke tree item. */
            StrokeTreeItem strokeTreeItem = new StrokeTreeItem();

            treeItemMap.put(StyledLayerDescriptorImpl.class, sldTreeItem);
            treeItemMap.put(StyleImpl.class, styleTreeItem);
            treeItemMap.put(FeatureTypeStyleImpl.class, ftsTreeItem);
            treeItemMap.put(RuleImpl.class, ruleTreeItem);
            treeItemMap.put(PointSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(LineSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(PolygonSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(TextSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(NamedLayerImpl.class, nameLayerTreeItem);
            treeItemMap.put(UserLayerImpl.class, userLayerTreeItem);
            treeItemMap.put(StrokeImpl.class, strokeTreeItem);
            treeItemMap.put(FillImpl.class, fillTreeItem);
            treeItemMap.put(RasterSymbolizerImpl.class, symbolizerTreeItem);
        }
    }

    /**
     * Gets the value for the given class type.
     *
     * @param classType the class type
     * @return the value
     */
    public SLDTreeItemInterface getValue(Class<?> classType) {

        SLDTreeItemInterface treeItem = treeItemMap.get(classType);

        return treeItem;
    }

    /**
     * Gets the singleton instance of TreeItemMap.
     *
     * @return singleton instance of TreeItemMap
     */
    public static TreeItemMap getInstance() {
        if(instance == null)
        {
            instance = new TreeItemMap();
        }
        return instance;
    }
}
