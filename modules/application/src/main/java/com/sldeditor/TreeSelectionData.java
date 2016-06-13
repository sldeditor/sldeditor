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

/**
 * Class containing data that describes the selected SLD item with in the tree structure.
 */
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
     * Gets the selection.
     *
     * @return the selection
     */
    public SelectedTreeItemEnum getSelection()
    {
        SelectedTreeItemEnum selection = SelectedTreeItemEnum.UNKNOWN;

        if((layerIndex > -1) &&
                (styleIndex == -1) &&
                (featureTypeStyleIndex == -1) &&
                (ruleIndex == -1) &&
                (symbolizerIndex == -1) &&
                (symbolizerDetailIndex == -1))
        {
            selection = SelectedTreeItemEnum.LAYER;
        }
        else if((layerIndex > -1) &&
                (styleIndex > -1) &&
                (featureTypeStyleIndex == -1) &&
                (ruleIndex == -1) &&
                (symbolizerIndex == -1) &&
                (symbolizerDetailIndex == -1))
        {
            selection = SelectedTreeItemEnum.STYLE;
        }
        else if((layerIndex > -1) &&
                (styleIndex > -1) &&
                (featureTypeStyleIndex > -1) &&
                (ruleIndex == -1) &&
                (symbolizerIndex == -1) &&
                (symbolizerDetailIndex == -1))
        {
            selection = SelectedTreeItemEnum.FEATURETYPESTYLE;
        }
        else if((layerIndex > -1) &&
                (styleIndex > -1) &&
                (featureTypeStyleIndex > -1) &&
                (ruleIndex > -1) &&
                (symbolizerIndex == -1) &&
                (symbolizerDetailIndex == -1))
        {
            selection = SelectedTreeItemEnum.RULE;
        }
        else if((layerIndex > -1) &&
                (styleIndex > -1) &&
                (featureTypeStyleIndex > -1) &&
                (ruleIndex > -1) &&
                (symbolizerIndex > -1) &&
                (symbolizerDetailIndex == -1))
        {
            if(selectedPanel == PointSymbolizerDetails.class)
            {
                selection = SelectedTreeItemEnum.POINT_SYMBOLIZER;
            }
            else if(selectedPanel == LineSymbolizerDetails.class)
            {
                selection = SelectedTreeItemEnum.LINE_SYMBOLIZER;
            }
            else if(selectedPanel == PolygonSymbolizerDetails.class)
            {
                selection = SelectedTreeItemEnum.POLYGON_SYMBOLIZER;
            }
            else if(selectedPanel == TextSymbolizerDetails.class)
            {
                selection = SelectedTreeItemEnum.TEXT_SYMBOLIZER;
            }
            else if(selectedPanel == RasterSymbolizerDetails.class)
            {
                selection = SelectedTreeItemEnum.RASTER_SYMBOLIZER;
            }
        }
        else if((layerIndex > -1) &&
                (styleIndex > -1) &&
                (featureTypeStyleIndex > -1) &&
                (ruleIndex > -1) &&
                (symbolizerIndex > -1))
        {
            if(selectedPanel == PointFillDetails.class)
            {
                selection = SelectedTreeItemEnum.POINT_FILL;
            }
            else if(selectedPanel == PolygonFillDetails.class)
            {
                selection = SelectedTreeItemEnum.POLYGON_FILL;
            }
            else if(selectedPanel == StrokeDetails.class)
            {
                selection = SelectedTreeItemEnum.STROKE;
            }
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
