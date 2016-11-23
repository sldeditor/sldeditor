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
package com.sldeditor.common.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;

/**
 * The Class SelectedSymbol contains the SLD data for the symbol being edited.
 * <p>Methods exists to:
 * <ul>
 * <li>add new structure items, e.g. rules, symbolizers, etc.</li>
 * <li>replace exist structure items with new ones, e.g. rules, symbolizers, etc.</li>
 * <li>remove structure items, e.g. rules, symbolizers, etc.</li>
 * <li>sets the currently selected structure item e.g. rule, symbolizer etc.</li>
 * <li>get the currently selected structure item e.g. rule, symbolizer etc.</li>
 * </ul>
 * <p>Class is implemented as a singleton.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SelectedSymbol {

    /** The logger. */
    private static Logger logger = Logger.getLogger(SelectedSymbol.class);

    /** The instance. */
    private static SelectedSymbol instance = null;

    /** The sld. */
    private StyledLayerDescriptor sld = null;

    /** The symbol data. */
    private SymbolData symbolData = new SymbolData();

    /** The tree update listener. */
    private List<SLDTreeUpdatedInterface> treeUpdateListenerList = new ArrayList<SLDTreeUpdatedInterface>();

    /** The filename. */
    private String filename = null;

    /** The name. */
    private String name;

    /**
     * Gets the single instance of SelectedSymbol.
     *
     * @return single instance of SelectedSymbol
     */
    public static SelectedSymbol getInstance()
    {
        if(instance == null)
        {
            instance = new SelectedSymbol();
        }
        return instance;
    }

    /**
     * Destroy instance.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Instantiates a new selected symbol.
     */
    public SelectedSymbol()
    {
        super();
    }

    /**
     * Gets the symbolizer.
     *
     * @return the symbolizer
     */
    public Symbolizer getSymbolizer() {
        return symbolData.getSymbolizer();
    }

    /**
     * Gets the rule.
     *
     * @return the rule
     */
    public Rule getRule() {
        return symbolData.getRule();
    }

    /**
     * Gets the symbol list.
     *
     * @param symbolToAdd the symbol to add
     * @return the symbol list
     */
    public List<GraphicalSymbol> getSymbolList(GraphicalSymbol symbolToAdd)
    {
        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();

        symbolList.add(symbolToAdd);

        return symbolList;
    }

    /**
     * Gets the sld.
     *
     * @return the sld
     */
    public StyledLayerDescriptor getSld() {
        return sld;
    }

    /**
     * Sets the sld.
     *
     * @param sld the new sld
     */
    public void setSld(StyledLayerDescriptor sld) {
        this.sld = sld;
        symbolData.resetData();
    }

    /**
     * Sets the symbolizer.
     *
     * @param symbolizer the new symbolizer
     */
    public void setSymbolizer(Symbolizer symbolizer) {
        symbolData.resetData();
        this.symbolData.setSymbolizer(symbolizer);
        updateInternalData(SelectedSymbolMask.SymbolMaskEnum.E_SYMBOLIZER);

        logger.debug("Selected symbolizer index : " + this.symbolData.getSelectedSymbolizerIndex());
    }

    /**
     * Sets the style.
     *
     * @param style the new style
     */
    public void setStyle(Style style) {
        symbolData.resetData();
        this.symbolData.setStyle(style);

        updateInternalData(SelectedSymbolMask.SymbolMaskEnum.E_STYLE);

        if(this.symbolData.getStyle() == null)
        {
            logger.debug("Style cleared");
        }
        else
        {
            logger.debug(String.format("Selected style : %s (Style %d)", this.symbolData.getStyle().getName(), this.symbolData.getSelectedStyleIndex()));
        }
    }

    /**
     * Sets the styled layer.
     *
     * @param styledLayer the new styled layer
     */
    public void setStyledLayer(StyledLayer styledLayer) {
        symbolData.resetData();
        this.symbolData.setStyledLayer(styledLayer);

        updateInternalData(SelectedSymbolMask.SymbolMaskEnum.E_STYLED_LAYER);

        if(this.symbolData.getStyledLayer() == null)
        {
            logger.debug("Styled layer cleared");
        }
        else
        {
            logger.debug(String.format("Selected styled layer : %s (Styled %d)", this.symbolData.getStyledLayer().getName(), this.symbolData.getSelectedStyledLayerIndex()));
        }
    }

    /**
     * Sets the rule.
     *
     * @param rule the new rule
     */
    public void setRule(Rule rule) {
        symbolData.resetData();
        this.symbolData.setRule(rule);

        updateInternalData(SelectedSymbolMask.SymbolMaskEnum.E_RULE);

        if(this.symbolData.getRule() == null)
        {
            logger.debug("Rule cleared");
        }
        else
        {
            logger.debug(String.format("Selected rule : %s (FTS %d/Rule %d)", this.symbolData.getRule().getName(), this.symbolData.getSelectedFTSIndex(), this.symbolData.getSelectedRuleIndex()));
        }
    }

    /**
     * Update internal data based on the supplied mask.
     *
     * @param maskValue the mask value
     */
    private void updateInternalData(SelectedSymbolMask.SymbolMaskEnum maskValue)
    {
        SymbolData localSymbolData = new SymbolData();
        SelectedSymbolMask mask = new SelectedSymbolMask(maskValue);

        if(sld == null)
        {
            return;
        }

        StyledLayer[] styledLayers = sld.getStyledLayers();

        if(styledLayers != null)
        {
            localSymbolData.initialiseSelectedStyledLayerIndex();
            for(StyledLayer styledLayer : styledLayers)
            {
                localSymbolData.setStyledLayer(styledLayer);

                if(styledLayer == symbolData.getStyledLayer())
                {
                    this.symbolData.update(localSymbolData);
                    return;
                }

                List<Style> styleList = null;

                if((styledLayer instanceof NamedLayerImpl) && mask.shouldContinue(SelectedSymbolMask.SymbolMaskEnum.E_STYLED_LAYER))
                {
                    NamedLayerImpl namedLayerImpl = (NamedLayerImpl)styledLayer;
                    styleList = namedLayerImpl.styles();
                }
                else if((styledLayer instanceof UserLayerImpl) && mask.shouldContinue(SelectedSymbolMask.SymbolMaskEnum.E_STYLED_LAYER))
                {
                    UserLayerImpl userLayerImpl = (UserLayerImpl)styledLayer;
                    styleList = userLayerImpl.userStyles();
                }

                if(mask.shouldContinue(SelectedSymbolMask.SymbolMaskEnum.E_STYLE) && (styleList != null))
                {
                    localSymbolData.initialiseSelectedStyleIndex();
                    for(Style style : styleList)
                    {
                        localSymbolData.setStyle(style);

                        if(style == symbolData.getStyle())
                        {
                            this.symbolData.update(localSymbolData);
                            return;
                        }

                        if(mask.shouldContinue(SelectedSymbolMask.SymbolMaskEnum.E_FEATURE_TYPE_STYLE))
                        {
                            localSymbolData.initialiseSelectedFTSIndex();

                            for(FeatureTypeStyle fts : style.featureTypeStyles())
                            {
                                localSymbolData.setFeatureTypeStyle(fts);

                                if(fts == symbolData.getFeatureTypeStyle())
                                {
                                    this.symbolData.update(localSymbolData);
                                    return;
                                }

                                if(mask.shouldContinue(SelectedSymbolMask.SymbolMaskEnum.E_RULE))
                                {
                                    localSymbolData.initialiseSelectedRuleIndex();
                                    for(Rule rule : fts.rules())
                                    {
                                        localSymbolData.setRule(rule);

                                        if(rule == symbolData.getRule())
                                        {
                                            this.symbolData.update(localSymbolData);
                                            return;
                                        }

                                        if(mask.shouldContinue(SelectedSymbolMask.SymbolMaskEnum.E_SYMBOLIZER))
                                        {
                                            localSymbolData.initialiseSelectedSymbolizerIndex();

                                            for(Symbolizer symbol : rule.symbolizers())
                                            {
                                                localSymbolData.setSymbolizer(symbol);

                                                if(symbol == this.symbolData.getSymbolizer())
                                                {
                                                    this.symbolData.update(localSymbolData);
                                                    break;
                                                }
                                                else
                                                {
                                                    localSymbolData.incrementSelectedSymbolizerIndex();
                                                }
                                            }
                                        }
                                        localSymbolData.incrementSelectedRuleIndex();
                                    }
                                }
                                localSymbolData.incrementSelectedFTSIndex();
                            }
                        }
                        localSymbolData.incrementSelectedStyleIndex();
                    }
                }
                localSymbolData.incrementSelectedStyledLayerIndex();
            }
        }
    }

    /**
     * Sets the feature type style.
     *
     * @param fts the new feature type style
     */
    public void setFeatureTypeStyle(FeatureTypeStyle fts)
    {
        symbolData.resetData();
        this.symbolData.setFeatureTypeStyle(fts);

        updateInternalData(SelectedSymbolMask.SymbolMaskEnum.E_FEATURE_TYPE_STYLE);

        if(this.symbolData.getFeatureTypeStyle() == null)
        {
            logger.debug("FeatureTypeStyle cleared");
        }
        else
        {
            logger.debug(String.format("Selected feature type style : %s (FTS %d)", this.symbolData.getFeatureTypeStyle().getName(), this.symbolData.getSelectedFTSIndex()));
        }
    }

    /**
     * Checks for only one rule.
     *
     * @return true, if successful
     */
    public boolean hasOnlyOneRule()
    {
        boolean oneRule = false;

        StyledLayer[] styledLayers = sld.getStyledLayers();

        int noOfRules = 0;

        if(styledLayers != null)
        {
            for(StyledLayer styledLayer : styledLayers)
            {
                List<Style> styleList = null;

                if(styledLayer instanceof NamedLayerImpl)
                {
                    NamedLayerImpl namedLayerImpl = (NamedLayerImpl)styledLayer;

                    styleList = namedLayerImpl.styles();
                }
                else if(styledLayer instanceof UserLayerImpl)
                {
                    UserLayerImpl userLayerImpl = (UserLayerImpl)styledLayer;

                    styleList = userLayerImpl.userStyles();
                }

                if(styleList != null)
                {
                    for(Style style : styleList)
                    {
                        for(FeatureTypeStyle fts : style.featureTypeStyles())
                        {
                            noOfRules += fts.rules().size();
                        }
                    }
                }
            }
        }

        oneRule = (noOfRules == 1);

        logger.debug(String.format("Number of rules : %d", noOfRules));

        return oneRule;
    }

    /**
     * Adds the symbolizer to list of symbolizers in the rule.
     *
     * @param newSymbolizer the new symbolizer
     */
    public void addSymbolizerToRule(Symbolizer newSymbolizer) {
        if(this.symbolData.getRule() == null)
        {
            ConsoleManager.getInstance().error(this, "rule == null");
        }
        else
        {
            List<Symbolizer> symbolizerList = (List<Symbolizer>) this.symbolData.getRule().symbolizers();
            symbolizerList.add(newSymbolizer);
        }
    }

    /**
     * Gets the symbol index.
     *
     * @return the symbol index
     */
    public int getSymbolIndex() {
        return symbolData.getSelectedSymbolizerIndex();
    }

    /**
     * Gets the feature type style.
     *
     * @return the feature type style
     */
    public FeatureTypeStyle getFeatureTypeStyle() {
        return symbolData.getFeatureTypeStyle();
    }

    /**
     * Replace rule.
     *
     * @param newRule the new rule
     */
    public void replaceRule(Rule newRule) {
        List<Rule> ruleList = (List<Rule>) this.symbolData.getFeatureTypeStyle().rules();

        int indexFound = -1;
        int index = 0;
        for(Rule rule : ruleList)
        {
            if(rule == this.symbolData.getRule())
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            ruleList.remove(indexFound);
            ruleList.add(indexFound, newRule);
            setRule(newRule);
        }
    }

    /**
     * Replace feature type style.
     *
     * @param newFTS the new fts
     */
    public void replaceFeatureTypeStyle(FeatureTypeStyle newFTS) {
        List<FeatureTypeStyle> ftsList = this.symbolData.getStyle().featureTypeStyles();

        int indexFound = -1;
        int index = 0;
        for(FeatureTypeStyle fts : ftsList)
        {
            if(fts == this.symbolData.getFeatureTypeStyle())
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            ftsList.remove(indexFound);
            ftsList.add(indexFound, newFTS);
            setFeatureTypeStyle(newFTS);
        }
    }

    /**
     * Replace named layer.
     *
     * @param newNamedLayer the new named layer
     */
    public void replaceStyledLayer(NamedLayer newNamedLayer) {

        if(this.sld == null)
        {
            return;
        }
        StyledLayer[] styledLayerList = this.sld.getStyledLayers();

        int indexFound = -1;
        int index = 0;
        for(StyledLayer styledLayer : styledLayerList)
        {
            if(styledLayer == this.symbolData.getStyledLayer())
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            styledLayerList[indexFound] = newNamedLayer;
            this.sld.setStyledLayers(styledLayerList);
            setStyledLayer(newNamedLayer);
        }
    }

    /**
     * Replace styled user layer.
     *
     * @param newUserLayer the new user layer
     */
    public void replaceStyledLayer(UserLayer newUserLayer) {

        if(this.sld == null)
        {
            return;
        }
        StyledLayer[] styledLayerList = this.sld.getStyledLayers();

        int indexFound = -1;
        int index = 0;
        for(StyledLayer styledLayer : styledLayerList)
        {
            if(styledLayer == this.symbolData.getStyledLayer())
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            styledLayerList[indexFound] = newUserLayer;
            this.sld.setStyledLayers(styledLayerList);
            setStyledLayer(newUserLayer);
        }
    }

    /**
     * Replace symbolizer.
     *
     * @param newSymbolizer the new symbolizer
     */
    public void replaceSymbolizer(Symbolizer newSymbolizer) {
        if(this.symbolData != null)
        {
            if(this.symbolData.getRule() != null)
            {
                List<Symbolizer> symbolizerList = (List<Symbolizer>) this.symbolData.getRule().symbolizers();

                Symbolizer oldSymbolizer = null;
                int indexFound = -1;
                int index = 0;
                for(Symbolizer symbolizer : symbolizerList)
                {
                    if(symbolizer == this.symbolData.getSymbolizer())
                    {
                        indexFound = index;
                        oldSymbolizer = symbolizer;
                        break;
                    }
                    else
                    {
                        index ++;
                    }
                }

                if(indexFound > -1)
                {
                    symbolizerList.remove(indexFound);
                    symbolizerList.add(indexFound, newSymbolizer);
                    setSymbolizer(newSymbolizer);
                }

                for(SLDTreeUpdatedInterface listener : treeUpdateListenerList)
                {
                    listener.updateNode(oldSymbolizer, newSymbolizer);
                }
            }
        }
    }

    /**
     * Replace style.
     *
     * @param newStyle the new style
     */
    public void replaceStyle(Style newStyle) {

        List<Style> styleList = null;

        if(this.symbolData.getStyledLayer() instanceof NamedLayerImpl)
        {
            NamedLayerImpl namedLayer = (NamedLayerImpl) this.symbolData.getStyledLayer();

            styleList = namedLayer.styles();
        }
        else if(this.symbolData.getStyledLayer() instanceof UserLayerImpl)
        {
            UserLayerImpl userLayer = (UserLayerImpl) this.symbolData.getStyledLayer();

            styleList = userLayer.userStyles();
        }

        if(styleList != null)
        {
            int indexFound = -1;
            int index = 0;
            for(Style style : styleList)
            {
                if(style == this.symbolData.getStyle())
                {
                    indexFound = index;
                    break;
                }
                else
                {
                    index ++;
                }
            }

            if(indexFound > -1)
            {
                styleList.remove(indexFound);
                styleList.add(indexFound, newStyle);
                setStyle(newStyle);
            }
        }
    }

    /**
     * Returns the style.
     *
     * @return the style
     */
    public Style getStyle() {
        return symbolData.getStyle();
    }

    /**
     * Gets the styled layer.
     *
     * @return the styled layer
     */
    public StyledLayer getStyledLayer() {
        return symbolData.getStyledLayer();
    }

    /**
     * Sets the tree update listener.
     *
     * @param treeObj the new tree update listener
     */
    public void setTreeUpdateListener(SLDTreeUpdatedInterface treeObj) {
        if(!this.treeUpdateListenerList.contains(treeObj))
        {
            this.treeUpdateListenerList.add(treeObj);
        }
    }

    /**
     * Adds the new rule.
     *
     * @param rule the rule
     */
    public void addNewRule(Rule rule) {
        if(this.symbolData.getFeatureTypeStyle() == null)
        {
            ConsoleManager.getInstance().error(this, "featureTypeStyle == null");
        }
        else
        {
            List<Rule> ruleList = (List<Rule>) this.symbolData.getFeatureTypeStyle().rules();

            ruleList.add(rule);
        }
    }

    /**
     * Adds the new style.
     *
     * @param style the style
     */
    public void addNewStyle(Style style) {
        if(this.symbolData.getStyledLayer() == null)
        {
            ConsoleManager.getInstance().error(this, "styledLayer == null");
        }
        else
        {
            if(this.symbolData.getStyledLayer() instanceof NamedLayerImpl)
            {
                NamedLayerImpl namedLayer = (NamedLayerImpl) this.symbolData.getStyledLayer();

                List<Style> styleList = namedLayer.styles();

                styleList.add(style);
            }
            else if(this.symbolData.getStyledLayer() instanceof UserLayerImpl)
            {
                UserLayerImpl userLayer = (UserLayerImpl) this.symbolData.getStyledLayer();

                List<Style> styleList = userLayer.userStyles();

                styleList.add(style);
            }
        }
    }

    /**
     * Adds the new feature type style.
     *
     * @param featureTypeStyle the feature type style
     */
    public void addNewFeatureTypeStyle(FeatureTypeStyle featureTypeStyle) {
        if(this.symbolData.getStyle() == null)
        {
            ConsoleManager.getInstance().error(this, "style == null");
        }
        else
        {
            List<FeatureTypeStyle> ftsList = this.symbolData.getStyle().featureTypeStyles();

            ftsList.add(featureTypeStyle);
        }
    }

    /**
     * Adds the new named styled layer.
     *
     * @param namedLayer the named layer
     */
    public void addNewStyledLayer(NamedLayer namedLayer) {
        if(this.sld == null)
        {
            ConsoleManager.getInstance().error(this, "sld == null");
        }
        else
        {
            this.sld.addStyledLayer(namedLayer);
        }
    }

    /**
     * Adds the new user styled layer.
     *
     * @param userLayer the user layer
     */
    public void addNewStyledLayer(UserLayer userLayer) {
        if(this.sld == null)
        {
            ConsoleManager.getInstance().error(this, "sld == null");
        }
        else
        {
            this.sld.addStyledLayer(userLayer);
        }
    }

    /**
     * Removes the symbolizer.
     *
     * @param symbolizerToDelete the symbolizer to delete
     */
    public void removeSymbolizer(Symbolizer symbolizerToDelete) {
        List<Symbolizer> symbolizerList = (List<Symbolizer>) this.symbolData.getRule().symbolizers();

        int indexFound = -1;
        int index = 0;
        for(Symbolizer symbolizer : symbolizerList)
        {
            if(symbolizer == symbolizerToDelete)
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            symbolizerList.remove(indexFound);
        }
    }

    /**
     * Removes the rule.
     *
     * @param ruleToDelete the rule to delete
     */
    public void removeRule(Rule ruleToDelete) {
        List<Rule> ruleList = (List<Rule>) this.symbolData.getFeatureTypeStyle().rules();

        int indexFound = -1;
        int index = 0;
        for(Rule rule : ruleList)
        {
            if(rule == ruleToDelete)
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            ruleList.remove(indexFound);
        }
    }

    /**
     * Removes the feature type style.
     *
     * @param ftsToDelete the feature type style to delete
     */
    public void removeFeatureTypeStyle(FeatureTypeStyle ftsToDelete) {
        List<FeatureTypeStyle> ftsList = this.symbolData.getStyle().featureTypeStyles();

        int indexFound = -1;
        int index = 0;
        for(FeatureTypeStyle fts : ftsList)
        {
            if(fts == ftsToDelete)
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            ftsList.remove(indexFound);
        }
    }

    /**
     * Removes the style.
     *
     * @param styleToDelete the style to delete
     */
    public void removeStyle(Style styleToDelete) {
        List<Style> styleList = null;

        if(this.symbolData.getStyledLayer() instanceof NamedLayerImpl)
        {
            NamedLayerImpl namedLayer = (NamedLayerImpl) this.symbolData.getStyledLayer();

            styleList = namedLayer.styles();
        }
        else if(this.symbolData.getStyledLayer() instanceof UserLayerImpl)
        {
            UserLayerImpl userLayer = (UserLayerImpl) this.symbolData.getStyledLayer();

            styleList = userLayer.userStyles();
        }

        if(styleList != null)
        {
            int indexFound = -1;
            int index = 0;
            for(Style style : styleList)
            {
                if(style == styleToDelete)
                {
                    indexFound = index;
                    break;
                }
                else
                {
                    index ++;
                }
            }

            if(indexFound > -1)
            {
                styleList.remove(indexFound);
            }
        }
    }

    /**
     * Removes the named/user layer.
     *
     * @param layerToDelete the named/user layer to delete
     */
    public void removeUserNamedLayer(StyledLayer layerToDelete) {
        List<StyledLayer> styledLayerList = this.sld.layers();

        int indexFound = -1;
        int index = 0;
        for(StyledLayer styledLayer : styledLayerList)
        {
            if(styledLayer == layerToDelete)
            {
                indexFound = index;
                break;
            }
            else
            {
                index ++;
            }
        }

        if(indexFound > -1)
        {
            styledLayerList.remove(indexFound);
        }
    }

    /**
     * Removes the styled layer descriptor.
     *
     * @param sldToDelete the styled layer descriptor to delete
     */
    public void removeStyledLayerDescriptor(StyledLayerDescriptor sldToDelete) {
        // Do nothing
    }

    /**
     * Gets the filename.
     *
     * @return the filename
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Sets the filename.
     *
     * @param filename the new filename
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Creates the new sld.
     *
     * @param newSLD the new sld
     */
    public void createNewSLD(StyledLayerDescriptor newSLD) {
        this.sld = newSLD;
    }

    /**
     * Checks for stroke.
     *
     * @return true, if successful
     */
    public boolean hasStroke()
    {
        return SLDTreeLeafFactory.getInstance().hasStroke(getSymbolizer());
    }

    /**
     * Checks for fill.
     *
     * @return true, if successful
     */
    public boolean hasFill()
    {
        return SLDTreeLeafFactory.getInstance().hasFill(getSymbolizer());
    }

    /**
     * Return flag indicating whether symbol is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        return this.symbolData.isValidSymbol();
    }

    /**
     * Mark symbol as as valid/invalid.
     *
     * @param key the key
     * @param validSymbolFlag the valid symbol flag
     */
    public void setValidSymbol(String key, boolean validSymbolFlag) {
        this.symbolData.setValidSymbol(key, validSymbolFlag);
    }

    /**
     * Adds the symbolizer to raster.
     *
     * @param symbolizer the symbolizer
     */
    public void addImageOutlineSymbolizerToRaster(Symbolizer symbolizer) {
        Symbolizer selectedSymbolizer = this.symbolData.getSymbolizer();
        if(selectedSymbolizer == null)
        {
            ConsoleManager.getInstance().error(this, "symbolizer == null");
        }
        else
        {
            if(selectedSymbolizer instanceof RasterSymbolizer)
            {
                RasterSymbolizer rasterSymbolizer = (RasterSymbolizer) selectedSymbolizer;
                rasterSymbolizer.setImageOutline(symbolizer);
            }
        }
    }

    /**
     * Removes the raster image outline.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void removeRasterImageOutline(RasterSymbolizer rasterSymbolizer) {
        if(rasterSymbolizer != null)
        {
            rasterSymbolizer.setImageOutline(null);
        }
    }

    /**
     * Checks if the selected Style contains a raster symbol.
     *
     * @return true, if is raster symbol
     */
    public boolean isRasterSymbol() {
        Style style = getStyle();

        if(style != null)
        {
            for(FeatureTypeStyle fts : style.featureTypeStyles())
            {
                for(Rule rule : fts.rules())
                {
                    for(Symbolizer symbolizer : rule.symbolizers())
                    {
                        if(symbolizer instanceof RasterSymbolizer)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
