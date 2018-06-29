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
import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.FillImpl;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.StyledLayerDescriptorImpl;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizerImpl;
import org.geotools.styling.UserLayerImpl;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.SymbolizerSelectedInterface;

/**
 * The Class SymbolizerDetailsPanel handles the display of the correct panel when the user clicks on
 * the SLD tree structure.
 * 
 * <p>
 * Implemented as panel with a card layout, all possible panels are added to the layout and
 * displayed accordingly.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SymbolizerDetailsPanel extends JPanel implements SymbolizerSelectedInterface {

    /** The Constant EMPTY_PANEL_KEY. */
    private static final String EMPTY_PANEL_KEY = "Empty";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The panel map. */
    private Map<String, List<PopulateDetailsInterface>> panelMap = new ConcurrentHashMap<String, List<PopulateDetailsInterface>>();

    /** The details panel. */
    private JPanel detailsPanel = null;

    /** The map that determines whether to use a PointFillDetails or PolygonFillDetails object. */
    private Map<Class<?>, Class<?>> fillMap = new HashMap<Class<?>, Class<?>>();

    private String currentDisplayedPanel;

    /**
     * Constructor.
     *
     * @param rendererList the renderer list
     * @param sldTree the sld tree
     */
    public SymbolizerDetailsPanel(List<RenderSymbolInterface> rendererList,
            SLDTreeUpdatedInterface sldTree) {

        Map<String, List<Class<?>>> classMap = new ConcurrentHashMap<String, List<Class<?>>>();
        classMap.put(EMPTY_PANEL_KEY, Arrays.asList(EmptyPanel.class));
        classMap.put(PointSymbolizerImpl.class.toString(),
                Arrays.asList(PointSymbolizerDetails.class));
        classMap.put(LineSymbolizerImpl.class.toString(),
                Arrays.asList(LineSymbolizerDetails.class));
        classMap.put(TextSymbolizerImpl.class.toString(),
                Arrays.asList(TextSymbolizerDetails.class));
        classMap.put(PolygonSymbolizerImpl.class.toString(),
                Arrays.asList(PolygonSymbolizerDetails.class));
        classMap.put(RasterSymbolizerImpl.class.toString(),
                Arrays.asList(RasterSymbolizerDetails.class));
        classMap.put(RuleImpl.class.toString(), Arrays.asList(RuleDetails.class));
        classMap.put(FeatureTypeStyleImpl.class.toString(),
                Arrays.asList(FeatureTypeStyleDetails.class));
        classMap.put(StyleImpl.class.toString(), Arrays.asList(StyleDetails.class));
        classMap.put(NamedLayerImpl.class.toString(), Arrays.asList(NamedLayerDetails.class));
        classMap.put(UserLayerImpl.class.toString(), Arrays.asList(UserLayerDetails.class));
        classMap.put(StyledLayerDescriptorImpl.class.toString(), Arrays.asList(EmptyPanel.class));
        classMap.put(StrokeImpl.class.toString(), Arrays.asList(StrokeDetails.class));
        classMap.put(FillImpl.class.toString(),
                Arrays.asList(PointFillDetails.class, PolygonFillDetails.class));

        populateMap(classMap);

        fillMap.put(PointSymbolizerImpl.class, PointFillDetails.class);
        fillMap.put(PolygonSymbolizerImpl.class, PolygonFillDetails.class);

        for (String key : panelMap.keySet()) {
            for (PopulateDetailsInterface panelInterface : panelMap.get(key)) {
                BasePanel panel = (BasePanel) panelInterface;

                if (rendererList != null) {
                    for (RenderSymbolInterface renderer : rendererList) {
                        panel.addRenderer(renderer);
                    }
                }
                panel.addTreeUpdate(sldTree);
            }
        }

        setBorder(new LineBorder(Color.BLACK));
        setLayout(new BorderLayout(0, 0));

        setSize(getPreferredSize());

        detailsPanel = new JPanel(false);
        detailsPanel.setLayout(new CardLayout());

        for (String key : panelMap.keySet()) {
            List<PopulateDetailsInterface> panelList = panelMap.get(key);

            for (PopulateDetailsInterface panel : panelList) {
                JPanel component = (JPanel) panel;

                detailsPanel.add(component, encodePanelKey(key, panel));
            }
        }

        add(detailsPanel, BorderLayout.CENTER);
    }

    /**
     * Populate map.
     *
     * @param classMap the class map
     */
    private void populateMap(Map<String, List<Class<?>>> classMap) {
        Set<String> keySet = classMap.keySet();

        for (String key : keySet) {
            List<PopulateDetailsInterface> panelList = panelMap.get(key);

            if (panelList == null) {
                panelList = new ArrayList<PopulateDetailsInterface>();
                panelMap.put(key, panelList);
            }
        }

        keySet.parallelStream().forEach((key) -> {
            List<PopulateDetailsInterface> panelList = panelMap.get(key);

            if (panelList != null) {
                List<Class<?>> clazzList = classMap.get(key);

                for (Class<?> clazz : clazzList) {
                    System.out.println(clazz.getName());
                    PopulateDetailsInterface panelDetails = null;
                    try {
                        panelDetails = (PopulateDetailsInterface) clazz.newInstance();
                    } catch (InstantiationException e) {
                        ConsoleManager.getInstance().exception(this, e);
                    } catch (IllegalAccessException e) {
                        ConsoleManager.getInstance().exception(this, e);
                    }

                    panelList.add(panelDetails);
                }
            }
        });
    }

    /**
     * Encode panel key.
     *
     * @param key the key
     * @param panel the panel
     * @return the string
     */
    private String encodePanelKey(String key, PopulateDetailsInterface panel) {
        return key + "/" + panel.getClass().getName();
    }

    /**
     * Gets the panel.
     *
     * @param parentClass the parent class
     * @param key the key
     * @return the panel
     */
    @Override
    public PopulateDetailsInterface getPanel(Class<?> parentClass, String key) {
        List<PopulateDetailsInterface> panelList = panelMap.get(key);

        if (panelList == null) {
            key = EMPTY_PANEL_KEY;
            panelList = panelMap.get(key);
        }

        if (panelList.size() == 1) {
            return panelList.get(0);
        }

        if (parentClass != null) {
            Class<?> panelTypeToFind = fillMap.get(parentClass);
            for (PopulateDetailsInterface panel : panelList) {
                if (panel.getClass() == panelTypeToFind) {
                    return panel;
                }
            }
        }

        return null;
    }

    /**
     * Show panel for selected tree item.
     *
     * @param parentClass the parent class
     * @param classSelected the class selected
     */
    @Override
    public void show(Class<?> parentClass, Class<?> classSelected) {
        String key = null;
        if (classSelected != null) {
            key = classSelected.toString();
        } else {
            key = EMPTY_PANEL_KEY;
        }

        PopulateDetailsInterface panel = getPanel(parentClass, key);
        if (panel != null) {
            CardLayout cl = (CardLayout) (detailsPanel.getLayout());
            currentDisplayedPanel = encodePanelKey(key, panel);
            cl.show(detailsPanel, currentDisplayedPanel);

            SelectedSymbol selectedSymbol = SelectedSymbol.getInstance();
            panel.populate(selectedSymbol);
        }
        repaint();
    }

    /**
     * Gets the panel ids.
     *
     * @return the panel ids
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.SymbolizerSelectedInterface#getPanelIds()
     */
    @Override
    public Set<String> getPanelIds() {
        return panelMap.keySet();
    }

    /**
     * Merge field data manager.
     *
     * @param mergedData the merged data
     */
    public void mergeFieldDataManager(GraphicPanelFieldManager mergedData) {

        for (String key : panelMap.keySet()) {
            List<PopulateDetailsInterface> panelList = panelMap.get(key);

            for (PopulateDetailsInterface panel : panelList) {
                mergedData.add(panel.getFieldDataManager());
            }
        }
    }

    /**
     * Method called before symbol loaded.
     */
    public void preLoadSymbol() {
        for (String key : panelMap.keySet()) {
            List<PopulateDetailsInterface> panelList = panelMap.get(key);

            for (PopulateDetailsInterface panel : panelList) {
                panel.preLoadSymbol();
            }
        }
    }

    /**
     * Gets the minimum version vendor option present in the SLD.
     *
     * @param parentObj the parent obj
     * @param sldObj the sld obj
     * @param vendorOptionsPresentList the vendor options present list
     */
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        Class<?> parentClass = null;
        Class<?> classSelected = sldObj.getClass();

        if (sldObj instanceof StyledLayerDescriptor) {
            // No parent
        } else if (sldObj instanceof StyledLayer) {
            parentClass = StyledLayerDescriptor.class;
        } else if (sldObj instanceof Style) {
            parentClass = StyledLayer.class;
        } else if (sldObj instanceof FeatureTypeStyle) {
            parentClass = Style.class;
        } else if (sldObj instanceof Rule) {
            parentClass = FeatureTypeStyle.class;
        } else if (sldObj instanceof Symbolizer) {
            parentClass = Rule.class;
        }

        internal_getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList, parentClass,
                classSelected);

        if (sldObj instanceof PointSymbolizerImpl) {
            PointSymbolizerImpl pointSymbolizer = (PointSymbolizerImpl) sldObj;
            parentClass = PointSymbolizerImpl.class;
            classSelected = FillImpl.class;
            internal_getMinimumVersion(pointSymbolizer, pointSymbolizer.getGraphic(),
                    vendorOptionsPresentList, parentClass, classSelected);
        } else if (sldObj instanceof LineSymbolizerImpl) {
            LineSymbolizerImpl lineSymbolizer = (LineSymbolizerImpl) sldObj;
            parentClass = LineSymbolizerImpl.class;
            classSelected = StrokeImpl.class;
            internal_getMinimumVersion(lineSymbolizer, lineSymbolizer.getStroke(),
                    vendorOptionsPresentList, parentClass, classSelected);
        } else if (sldObj instanceof PolygonSymbolizerImpl) {
            PolygonSymbolizerImpl polygonSymbolizer = (PolygonSymbolizerImpl) sldObj;
            parentClass = PolygonSymbolizerImpl.class;
            classSelected = FillImpl.class;
            internal_getMinimumVersion(polygonSymbolizer, polygonSymbolizer.getFill(),
                    vendorOptionsPresentList, parentClass, classSelected);
            classSelected = StrokeImpl.class;
            internal_getMinimumVersion(polygonSymbolizer, polygonSymbolizer.getStroke(),
                    vendorOptionsPresentList, parentClass, classSelected);
        } else if (sldObj instanceof TextSymbolizerImpl) {
            TextSymbolizerImpl textSymbolizer = (TextSymbolizerImpl) sldObj;
            parentClass = Rule.class;
            classSelected = TextSymbolizerImpl.class;
            internal_getMinimumVersion(parentObj, textSymbolizer, vendorOptionsPresentList,
                    parentClass, classSelected);
        } else if (sldObj instanceof RasterSymbolizerImpl) {
            RasterSymbolizerImpl rasterSymbolizer = (RasterSymbolizerImpl) sldObj;
            parentClass = Rule.class;
            classSelected = RasterSymbolizerImpl.class;
            internal_getMinimumVersion(parentObj, rasterSymbolizer, vendorOptionsPresentList,
                    parentClass, classSelected);
        }
    }

    /**
     * Internal get minimum version.
     *
     * @param sldObj the sld obj
     * @param vendorOptionsPresentList the vendor options present list
     * @param parentClass the parent class
     * @param classSelected the class selected
     */
    private void internal_getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList, Class<?> parentClass,
            Class<?> classSelected) {
        String key = null;
        if (classSelected != null) {
            key = classSelected.toString();
        } else {
            key = EMPTY_PANEL_KEY;
        }

        PopulateDetailsInterface panel = getPanel(parentClass, key);

        if (panel != null) {
            panel.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.SymbolizerSelectedInterface#refresh(java.lang.Class,
     * java.lang.Class)
     */
    @Override
    public void refresh(Class<?> parentClass, Class<?> classSelected) {
        String key = null;
        if (classSelected != null) {
            key = classSelected.toString();
        } else {
            key = EMPTY_PANEL_KEY;
        }

        PopulateDetailsInterface panel = getPanel(parentClass, key);
        if ((panel != null) && (currentDisplayedPanel != null)) {
            if (currentDisplayedPanel.equals(encodePanelKey(key, panel))) {
                SelectedSymbol selectedSymbol = SelectedSymbol.getInstance();
                panel.populate(selectedSymbol);
            }
        }
        repaint();
    }

}
