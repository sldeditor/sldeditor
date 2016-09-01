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
package com.sldeditor.map;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.NoToolAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.control.JMapStatusBar;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.style.Style;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateInterface;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.filter.v2.envvar.WMSEnvVarValues;
import com.sldeditor.ui.render.RuleRenderOptions;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Panel to displayed the rendered data source using the current style.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MapRender extends JPanel implements RenderSymbolInterface, PrefUpdateInterface, DataSourceUpdatedInterface, MouseWheelListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant NOMAP_PANEL. */
    private static final String NOMAP_PANEL = "NOMAP";

    /** The Constant MAP_PANEL. */
    private static final String MAP_PANEL = "MAP";

    /** The under test flag. */
    private static boolean underTest = false;

    /** The feature list. */
    private FeatureSource<SimpleFeatureType, SimpleFeature> featureList = null;

    /** The user feature list. */
    private Map<UserLayer, FeatureSource<SimpleFeatureType, SimpleFeature> > userLayerFeatureListMap = null;

    /** The map pane. */
    private JMapPane mapPane = null;

    /** The geometry type. */
    private GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;

    /** The grid coverage. */
    private AbstractGridCoverage2DReader gridCoverage = null;

    /** Name assigned to toolbar button for feature info queries. */
    public static final String TOOLBAR_INFO_BUTTON_NAME = "ToolbarInfoButton";
    /** Name assigned to toolbar button for map panning. */
    public static final String TOOLBAR_PAN_BUTTON_NAME = "ToolbarPanButton";
    /** Name assigned to toolbar button for default pointer. */
    public static final String TOOLBAR_POINTER_BUTTON_NAME = "ToolbarPointerButton";
    /** Name assigned to toolbar button for map reset. */
    public static final String TOOLBAR_RESET_BUTTON_NAME = "ToolbarResetButton";
    /** Name assigned to toolbar button for map zoom in. */
    public static final String TOOLBAR_ZOOMIN_BUTTON_NAME = "ToolbarZoomInButton";
    /** Name assigned to toolbar button for map zoom out. */
    public static final String TOOLBAR_ZOOMOUT_BUTTON_NAME = "ToolbarZoomOutButton";

    /** The click to zoom factor, 1 wheel click is 10% zoom. */
    private final double clickToZoom = 0.1;

    /** The wms env var values. */
    private WMSEnvVarValues wmsEnvVarValues = new WMSEnvVarValues();

    /** The map panel that contains the card layout containing map pane and 'no data source' panel. */
    private JPanel mapPanel;

    /** The map bounds. */
    private ReferencedEnvelope mapBounds = null;

    /**
     * Default constructor.
     */
    public MapRender()
    {
        setLayout(new BorderLayout());

        JPanel mapRenderPanel = new JPanel();

        JPanel noDataSource = noDataSourcePanel();

        mapPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mapPanel.setLayout(cardLayout);
        mapPanel.add(mapRenderPanel, MAP_PANEL);
        mapPanel.add(noDataSource, NOMAP_PANEL);

        this.add(mapPanel, BorderLayout.CENTER);

        cardLayout.show(mapPanel, NOMAP_PANEL);

        mapRenderPanel.setLayout(new BorderLayout());

        mapPane = createMapPane();
        mapRenderPanel.add(createToolbar(), BorderLayout.NORTH);
        mapRenderPanel.add(mapPane, BorderLayout.CENTER);
        mapRenderPanel.add(JMapStatusBar.createDefaultStatusBar(mapPane), BorderLayout.SOUTH);

        PrefManager.getInstance().addListener(this);

        // Listen for changes in data sources
        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.addListener(this);

        // Listen for mouse wheel changes
        mapPane.addMouseWheelListener(this);
    }

    /**
     * Creates the toolbar.
     *
     * @return the j tool bar
     */
    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);

        JButton btn;
        ButtonGroup cursorToolGrp = new ButtonGroup();

        btn = new JButton(new NoToolAction(mapPane));
        btn.setName(TOOLBAR_POINTER_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        btn = new JButton(new ZoomInAction(mapPane));
        btn.setName(TOOLBAR_ZOOMIN_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        btn = new JButton(new ZoomOutAction(mapPane));
        btn.setName(TOOLBAR_ZOOMOUT_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        toolBar.addSeparator();

        btn = new JButton(new PanAction(mapPane));
        btn.setName(TOOLBAR_PAN_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        toolBar.addSeparator();

        btn = new JButton(new ResetAction(mapPane));
        btn.setName(TOOLBAR_RESET_BUTTON_NAME);
        toolBar.add(btn);

        return toolBar;
    }

    /**
     * Update style.
     */
    public void updateStyle()
    {
        internalRenderStyle();
    }

    /**
     * Internal render style.
     */
    private void internalRenderStyle()
    {
        if(!underTest)
        {
            wmsEnvVarValues.setImageWidth(mapPane.getWidth());
            wmsEnvVarValues.setImageHeight(mapPane.getHeight());

            StyledLayerDescriptor sld = SelectedSymbol.getInstance().getSld();

            MapContent mapContent = mapPane.getMapContent();
            if(mapContent == null)
            {
                mapContent = new MapContent();
                mapPane.setMapContent(mapContent);
            }

            // Add the layers back with the updated style
            if(sld != null)
            {
                List<StyledLayer> styledLayerList = sld.layers();

                for(StyledLayer styledLayer : styledLayerList)
                {
                    List<org.geotools.styling.Style> styleList = null;

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
                            renderSymbol(mapContent, styledLayer, style);
                        }
                    }
                }
            }
        }
    }

    /**
     * Render symbol.
     *
     * @param mapContent the map content
     * @param styledLayer the styled layer
     * @param style the style
     */
    private void renderSymbol(MapContent mapContent, StyledLayer styledLayer, Style style)
    {
        switch(geometryType)
        {
        case RASTER:
            mapContent.addLayer(new GridReaderLayer(gridCoverage, (org.geotools.styling.Style) style));
            break;
        case POINT:
        case LINE:
        case POLYGON:
        {
            FeatureSource<SimpleFeatureType, SimpleFeature> tmpFeatureList = null;

            if(styledLayer instanceof UserLayer)
            {
                if(userLayerFeatureListMap != null)
                {
                    tmpFeatureList = userLayerFeatureListMap.get(styledLayer);
                }
            }
            else
            {
                tmpFeatureList = featureList;
            }

            if(tmpFeatureList != null)
            {
                mapContent.addLayer(new FeatureLayer(tmpFeatureList, (org.geotools.styling.Style) style));
            }
        }
        break;
        default:
            break;
        }

        wmsEnvVarValues.setMapBounds(mapBounds);
        EnvironmentVariableManager.getInstance().setWMSEnvVarValues(wmsEnvVarValues);
    }

    /**
     * Use anti alias updated.
     *
     * @param value the value
     */
    /* (non-Javadoc)
     * @see com.sldeditor.preferences.PrefUpdateInterface#useAntiAliasUpdated(boolean)
     */
    @Override
    public void useAntiAliasUpdated(boolean value) {
        internalRenderStyle();
    }

    /**
     * Background colour update.
     *
     * @param backgroundColour the background colour
     */
    @Override
    public void backgroundColourUpdate(Color backgroundColour) {
        mapPane.setBackground(backgroundColour);
    }

    /**
     * Data source loaded.
     *
     * @param geometryType the geometry type
     * @param isConnectedToDataSourceFlag the is connected to data source flag
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType,
            boolean isConnectedToDataSourceFlag) {

        this.geometryType = geometryType;
        featureList = DataSourceFactory.getDataSource().getFeatureSource();

        userLayerFeatureListMap = DataSourceFactory.getDataSource().getUserLayerFeatureSource();
        gridCoverage = DataSourceFactory.getDataSource().getGridCoverageReader();

        calculateMapBounds();

        CardLayout cardLayout = (CardLayout) mapPanel.getLayout();

        if((geometryType == GeometryTypeEnum.UNKNOWN) || !isConnectedToDataSourceFlag)
        {
            cardLayout.show(mapPanel, NOMAP_PANEL);
        }
        else
        {
            cardLayout.show(mapPanel, MAP_PANEL);

            internalRenderStyle();
        }
    }

    private void calculateMapBounds() {
        List<ReferencedEnvelope> refEnvList = new ArrayList<ReferencedEnvelope>();

        if(featureList != null)
        {
            try {
                refEnvList.add(convertToWGS84(featureList.getFeatures().getBounds()));
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(MapRender.class, e);
            }
        }

        if(userLayerFeatureListMap != null)
        {
            for(UserLayer userLayer : userLayerFeatureListMap.keySet())
            {
                try {
                    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = userLayerFeatureListMap.get(userLayer);

                    if(featureSource != null)
                    {
                        refEnvList.add(convertToWGS84(featureSource.getFeatures().getBounds()));
                    }
                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(MapRender.class, e);
                }
            }
        }

        //        if(gridCoverage != null)
        //        {
        //            try {
        //                GridCoverage2D grid = gridCoverage.;
        //           //     refEnvList.add(gridCoverage.);
        //            } catch (IOException e) {
        //                ConsoleManager.getInstance().exception(MapRender.class, e);
        //            }
        //        }

        if(!refEnvList.isEmpty())
        {
            // Combine all the bounding boxes of all the layers
            mapBounds = refEnvList.get(0);

            for(int index = 1; index < refEnvList.size(); index ++)
            {
                mapBounds.expandToInclude(refEnvList.get(index));
            }
        }
    }

    /**
     * Convert referenced envelope to WGS 84.
     *
     * @param bounds the bounds
     * @return the referenced envelope
     */
    private ReferencedEnvelope convertToWGS84(ReferencedEnvelope bounds) {
        if(bounds == null)
        {
            return null;
        }

        CoordinateReferenceSystem wgs84 = CoordManager.getInstance().getWGS84();

        if(wgs84.equals(bounds.getCoordinateReferenceSystem()))
        {
            return bounds;
        }

        if(bounds.getCoordinateReferenceSystem() == null)
        {
            return bounds;
        }

        MathTransform transform = null;
        try {
            transform = CRS.findMathTransform(bounds.getCoordinateReferenceSystem(), wgs84);
        } catch (FactoryException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
        Envelope targetGeometry = null;
        try {
            targetGeometry = JTS.transform(bounds, transform);
        } catch (TransformException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        if(targetGeometry != null)
        {
            ReferencedEnvelope refEnv = new ReferencedEnvelope(targetGeometry.getMinY(), 
                    targetGeometry.getMaxY(), 
                    targetGeometry.getMinX(), 
                    targetGeometry.getMaxX(),
                    wgs84);

            return refEnv;
        }
        return null;
    }

    /**
     * Mouse wheel moved.
     *
     * @param ev the mousewheel event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent ev) {

        int clicks = ev.getWheelRotation();
        // -ve means wheel moved up, +ve means down
        int sign = (clicks < 0 ? -1 : 1);

        Envelope env = mapPane.getDisplayArea();
        double width = env.getWidth();
        double delta = width * clickToZoom * sign;

        env.expandBy(delta);
        mapPane.setDisplayArea((org.opengis.geometry.Envelope) env);

        switch(geometryType)
        {
        case RASTER:
        {
            ReferencedEnvelope refEnv = ReferencedEnvelope.create(env, gridCoverage.getCoordinateReferenceSystem());
            wmsEnvVarValues.setMapBounds(refEnv);
        }
        break;
        case POINT:
        case LINE:
        case POLYGON:
        {
            ReferencedEnvelope refEnv = ReferencedEnvelope.create(env, featureList.getSchema().getCoordinateReferenceSystem());
            wmsEnvVarValues.setMapBounds(refEnv);
        }
        break;
        default:
            break;
        }

        EnvironmentVariableManager.getInstance().setWMSEnvVarValues(wmsEnvVarValues);

        mapPane.repaint();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.RenderSymbolInterface#addSLDOutputListener(com.sldeditor.common.output.SLDOutputInterface)
     */
    @Override
    public void addSLDOutputListener(SLDOutputInterface sldOutput) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.RenderSymbolInterface#renderSymbol()
     */
    @Override
    public void renderSymbol() {
        this.internalRenderStyle();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.RenderSymbolInterface#getRuleRenderOptions()
     */
    @Override
    public RuleRenderOptions getRuleRenderOptions() {
        // Do nothing
        return null;
    }

    /**
     * Sets the under test flag.
     *
     * @param underTest the new under test
     */
    public static void setUnderTest(boolean underTest) {
        MapRender.underTest = underTest;
    }

    /**
     * Creates the no data source panel.
     *
     * @return the j panel
     */
    private JPanel noDataSourcePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel(Localisation.getString(MapRender.class, "MapRender.noDataSource"));
        label.setFont(new Font("Arial", Font.BOLD, 16));
        labelPanel.add(label);

        labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(labelPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the map pane.
     *
     * @return the j map pane
     */
    private JMapPane createMapPane() {
        JMapPane internal_mapPane = new JMapPane();
        internal_mapPane.setBackground(PrefManager.getInstance().getPrefData().getBackgroundColour());
        internal_mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return internal_mapPane;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        if(dataStore == null)
        {
            return;
        }

        MapContent mapContent = mapPane.getMapContent();

        if(mapContent != null)
        {
            // Remove all layers
            for(Layer layer : mapContent.layers())
            {
                    mapContent.removeLayer(layer);
            }
        }
    }
}
