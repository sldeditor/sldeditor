package com.sldeditor;

import com.sldeditor.common.xml.ui.SelectedTreeItemEnum;
import com.sldeditor.ui.detail.LineSymbolizerDetails;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PointSymbolizerDetails;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.PolygonSymbolizerDetails;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.TextSymbolizerDetails;

/** Class containing data that describes the selected SLD item with in the tree structure. */
public class TreeSelectionData {

    /** The layer index. */
    private int layerIndex = -1;

    /** The style index. */
    private int styleIndex = -1;

    /** The feature type style index. */
    private int featureTypeStyleIndex = -1;

    /** The rule index. */
    private int ruleIndex = -1;

    /** The symbolizer index. */
    private int symbolizerIndex = -1;

    /** The symbolizer detail index. */
    private int symbolizerDetailIndex = -1;

    /** The selected panel. */
    private Class<?> selectedPanel = null;

    /**
     * Gets the layer index.
     *
     * @return the layer index
     */
    public int getLayerIndex() {
        return layerIndex;
    }

    /**
     * Sets the layer index.
     *
     * @param layerIndex the new layer index
     */
    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    /**
     * Gets the style index.
     *
     * @return the style index
     */
    public int getStyleIndex() {
        return styleIndex;
    }

    /**
     * Sets the style index.
     *
     * @param styleIndex the new style index
     */
    public void setStyleIndex(int styleIndex) {
        this.styleIndex = styleIndex;
    }

    /**
     * Gets the feature type style index.
     *
     * @return the feature type style index
     */
    public int getFeatureTypeStyleIndex() {
        return featureTypeStyleIndex;
    }

    /**
     * Sets the feature type style index.
     *
     * @param featureTypeStyleIndex the new feature type style index
     */
    public void setFeatureTypeStyleIndex(int featureTypeStyleIndex) {
        this.featureTypeStyleIndex = featureTypeStyleIndex;
    }

    /**
     * Gets the rule index.
     *
     * @return the rule index
     */
    public int getRuleIndex() {
        return ruleIndex;
    }

    /**
     * Sets the rule index.
     *
     * @param ruleIndex the new rule index
     */
    public void setRuleIndex(int ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    /**
     * Gets the symbolizer index.
     *
     * @return the symbolizer index
     */
    public int getSymbolizerIndex() {
        return symbolizerIndex;
    }

    /**
     * Sets the symbolizer index.
     *
     * @param symbolizerIndex the new symbolizer index
     */
    public void setSymbolizerIndex(int symbolizerIndex) {
        this.symbolizerIndex = symbolizerIndex;
    }

    /**
     * Gets the symbolizer detail index.
     *
     * @return the symbolizer detail index
     */
    public int getSymbolizerDetailIndex() {
        return symbolizerDetailIndex;
    }

    /**
     * Sets the symbolizer detail index.
     *
     * @param symbolizerDetailIndex the new symbolizer detail index
     */
    public void setSymbolizerDetailIndex(int symbolizerDetailIndex) {
        this.symbolizerDetailIndex = symbolizerDetailIndex;
    }

    /**
     * Determine tree item.
     *
     * @param layer the layer
     * @param style the style
     * @param featureTypeStyle the feature type style
     * @param rule the rule
     * @param symbolizer the symbolizer
     * @param symbolizerDetail the symbolizer detail
     * @return true, if successful
     */
    private boolean determineTreeItem(
            boolean layer,
            boolean style,
            boolean featureTypeStyle,
            boolean rule,
            boolean symbolizer,
            boolean symbolizerDetail) {
        return (checkTreeItem(layerIndex, layer)
                && checkTreeItem(styleIndex, style)
                && checkTreeItem(featureTypeStyleIndex, featureTypeStyle)
                && checkTreeItem(ruleIndex, rule)
                && checkTreeItem(symbolizerIndex, symbolizer)
                && checkTreeItem(symbolizerDetailIndex, symbolizerDetail));
    }

    /**
     * Check tree item.
     *
     * @param index the index
     * @param layer the layer
     * @return true, if successful
     */
    private boolean checkTreeItem(int index, boolean selected) {
        return (selected ? (index > -1) : (index == -1));
    }

    /**
     * Gets the selection.
     *
     * @return the selection
     */
    public SelectedTreeItemEnum getSelection() {
        SelectedTreeItemEnum selection = SelectedTreeItemEnum.UNKNOWN;
        if (determineTreeItem(true, false, false, false, false, false)) {
            selection = SelectedTreeItemEnum.LAYER;
        } else if (determineTreeItem(true, true, false, false, false, false)) {
            selection = SelectedTreeItemEnum.STYLE;
        } else if (determineTreeItem(true, true, true, false, false, false)) {
            selection = SelectedTreeItemEnum.FEATURETYPESTYLE;
        } else if (determineTreeItem(true, true, true, true, false, false)) {
            selection = SelectedTreeItemEnum.RULE;
        } else if (determineTreeItem(true, true, true, true, true, false)) {
            selection = determineSymbolizer();
        } else if (determineTreeItem(true, true, true, true, true, true)) {
            selection = determineSymbolizerDetail();
        }

        return selection;
    }

    /**
     * Determine symbolizer detail.
     *
     * @return the selected tree item enum
     */
    private SelectedTreeItemEnum determineSymbolizerDetail() {
        SelectedTreeItemEnum selection;
        if (selectedPanel == PointFillDetails.class) {
            selection = SelectedTreeItemEnum.POINT_FILL;
        } else if (selectedPanel == PolygonFillDetails.class) {
            selection = SelectedTreeItemEnum.POLYGON_FILL;
        } else if (selectedPanel == StrokeDetails.class) {
            selection = SelectedTreeItemEnum.STROKE;
        } else {
            selection = SelectedTreeItemEnum.UNKNOWN;
        }
        return selection;
    }

    /**
     * Determine symbolizer.
     *
     * @return the selected tree item enum
     */
    private SelectedTreeItemEnum determineSymbolizer() {
        SelectedTreeItemEnum selection;
        if (selectedPanel == PointSymbolizerDetails.class) {
            selection = SelectedTreeItemEnum.POINT_SYMBOLIZER;
        } else if (selectedPanel == LineSymbolizerDetails.class) {
            selection = SelectedTreeItemEnum.LINE_SYMBOLIZER;
        } else if (selectedPanel == PolygonSymbolizerDetails.class) {
            selection = SelectedTreeItemEnum.POLYGON_SYMBOLIZER;
        } else if (selectedPanel == TextSymbolizerDetails.class) {
            selection = SelectedTreeItemEnum.TEXT_SYMBOLIZER;
        } else if (selectedPanel == RasterSymbolizerDetails.class) {
            selection = SelectedTreeItemEnum.RASTER_SYMBOLIZER;
        } else {
            selection = SelectedTreeItemEnum.UNKNOWN;
        }
        return selection;
    }

    /**
     * Gets the selected panel.
     *
     * @return the selected panel
     */
    public Class<?> getSelectedPanel() {
        return selectedPanel;
    }

    /**
     * Sets the selected panel.
     *
     * @param selectedPanel the new selected panel
     */
    public void setSelectedPanel(Class<?> selectedPanel) {
        this.selectedPanel = selectedPanel;
    }
}
