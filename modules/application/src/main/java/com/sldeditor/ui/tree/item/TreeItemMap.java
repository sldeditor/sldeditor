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
public class TreeItemMap {

    /** The tree item map. */
    private Map<Class<?>, SLDTreeItemInterface> theTreeItemMap = new HashMap<>();

    /** The rule tree item. */
    private RuleTreeItem ruleTreeItem = new RuleTreeItem();

    /** The symbolizer tree item. */
    private SymbolizerTreeItem symbolizerTreeItem = new SymbolizerTreeItem();

    /** The singleton instance. */
    private static TreeItemMap instance = null;

    /** Instantiates a new tree item map. */
    private TreeItemMap() {
        createTreeItemMap();
    }

    /** Creates the tree item map. */
    private void createTreeItemMap() {

        if (theTreeItemMap.isEmpty()) {
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

            theTreeItemMap.put(StyledLayerDescriptorImpl.class, sldTreeItem);
            theTreeItemMap.put(StyleImpl.class, styleTreeItem);
            theTreeItemMap.put(FeatureTypeStyleImpl.class, ftsTreeItem);
            theTreeItemMap.put(RuleImpl.class, ruleTreeItem);
            theTreeItemMap.put(PointSymbolizerImpl.class, symbolizerTreeItem);
            theTreeItemMap.put(LineSymbolizerImpl.class, symbolizerTreeItem);
            theTreeItemMap.put(PolygonSymbolizerImpl.class, symbolizerTreeItem);
            theTreeItemMap.put(TextSymbolizerImpl.class, symbolizerTreeItem);
            theTreeItemMap.put(NamedLayerImpl.class, nameLayerTreeItem);
            theTreeItemMap.put(UserLayerImpl.class, userLayerTreeItem);
            theTreeItemMap.put(StrokeImpl.class, strokeTreeItem);
            theTreeItemMap.put(FillImpl.class, fillTreeItem);
            theTreeItemMap.put(RasterSymbolizerImpl.class, symbolizerTreeItem);
        }
    }

    /**
     * Gets the value for the given class type.
     *
     * @param classType the class type
     * @return the value
     */
    public SLDTreeItemInterface getValue(Class<?> classType) {
        return theTreeItemMap.get(classType);
    }

    /**
     * Gets the singleton instance of TreeItemMap.
     *
     * @return singleton instance of TreeItemMap
     */
    public static TreeItemMap getInstance() {
        if (instance == null) {
            instance = new TreeItemMap();
        }
        return instance;
    }
}
