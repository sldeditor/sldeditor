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

package com.sldeditor.extension.filesystem.geoserver.client;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgressInterface;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;
import it.geosolutions.geoserver.rest.decoder.RESTStyleList;
import it.geosolutions.geoserver.rest.decoder.utils.NameLinkElem;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class that manages the connection to a GeoServer instance. Retrieves style and layer data from
 * and uploads new styles.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerClient implements Serializable, GeoServerClientInterface {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4048849953685331976L;

    /** The Constant DEFAULT_WORKSPACE_NAME. */
    private static final String DEFAULT_WORKSPACE_NAME = "<Default Workspace>";

    /** The parent object. */
    private transient GeoServerReadProgressInterface parentObj = null;

    /** The connection. */
    private transient GeoServerConnection connection = null;

    /** The connected flag. */
    private boolean connected = false;

    /** Default constructor. */
    public GeoServerClient() {
        // Default constructor
    }

    /**
     * Initialise a new GeoServer client.
     *
     * @param parent the parent
     * @param connection the connection
     */
    public void initialise(GeoServerReadProgressInterface parent, GeoServerConnection connection) {
        this.parentObj = parent;
        this.connection = connection;
        GeoServerLayer.setDefaultWorkspaceName(DEFAULT_WORKSPACE_NAME);
    }

    /** Retrieve data from GeoServer. */
    @Override
    public void retrieveData() {
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        if (manager != null) {
            GeoServerRESTReader reader = manager.getReader();

            if (reader != null) {
                if (parentObj != null) {
                    parentObj.startPopulating(connection);
                }

                List<String> localWorkspaceList = getWorkspaceList();

                parseStyleList(reader, localWorkspaceList);
                parseLayerList(reader, localWorkspaceList, null);
            }
        }
    }

    /**
     * Parses the style list.
     *
     * @param reader the reader
     * @param localWorkspaceList the workspace list
     */
    private void parseStyleList(GeoServerRESTReader reader, List<String> localWorkspaceList) {
        Thread t1 =
                new Thread(
                        new Runnable() {
                            public void run() {

                                Map<String, List<StyleWrapper>> styleMap = new LinkedHashMap<>();

                                int count = 1;
                                List<StyleWrapper> styleList = new ArrayList<>();

                                count = parseStyleInDefaultWorkspace(reader, count, styleList);

                                styleMap.put(DEFAULT_WORKSPACE_NAME, styleList);

                                // Read styles from workspaces
                                for (String workspaceName : localWorkspaceList) {
                                    count =
                                            parseStyleInWorkspace(
                                                    reader, styleMap, count, workspaceName);
                                }

                                if (parentObj != null) {
                                    parentObj.readStylesComplete(connection, styleMap, false);
                                }
                            }
                        });
        t1.start();
    }

    /**
     * Parses the layer list.
     *
     * @param reader the reader
     * @param existingWorkspaceList the existing workspace list
     * @param workspaceName the workspace name
     */
    private void parseLayerList(
            GeoServerRESTReader reader, List<String> existingWorkspaceList, String workspaceName) {
        Thread t1 =
                new Thread(
                        new Runnable() {
                            public void run() {
                                parseLayerListWorker(reader, existingWorkspaceList, workspaceName);
                            }
                        });
        t1.start();
    }

    /**
     * Gets the style.
     *
     * @param styleWrapper the style wrapper
     * @return the style
     */
    @Override
    public String getStyle(StyleWrapper styleWrapper) {
        if (styleWrapper == null) {
            return null;
        }

        if (!workspaceValid(styleWrapper.getWorkspace())) {
            return null;
        }

        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        if (manager != null) {
            GeoServerRESTReader reader = manager.getReader();
            if (reader != null) {
                if (isDefaultWorkspace(styleWrapper.getWorkspace())) {
                    return reader.getSLD(styleWrapper.getStyle());
                } else {
                    return reader.getSLD(styleWrapper.getWorkspace(), styleWrapper.getStyle());
                }
            }
        }

        return null;
    }

    /**
     * Checks if supplied workspace name is the default workspace.
     *
     * @param workspaceName the workspace name to check
     * @return true, if is default workspace
     */
    @Override
    public boolean isDefaultWorkspace(String workspaceName) {
        if (workspaceName != null) {
            return (workspaceName.compareTo(DEFAULT_WORKSPACE_NAME) == 0);
        }
        return true;
    }

    /**
     * Connect.
     *
     * @return true, if successful
     */
    @Override
    public boolean connect() {
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        if (manager == null) {
            connected = false;
        } else {
            connected = manager.getReader().existGeoserver();
        }
        return connected;
    }

    /** Disconnect. */
    @Override
    public void disconnect() {
        GeoServerRESTManagerFactory.deleteConnection(connection);
        connected = false;
    }

    /**
     * Upload sld.
     *
     * @param styleWrapper the style wrapper
     * @param sldBody the sld body
     * @return true, if successful
     */
    @Override
    public boolean uploadSLD(StyleWrapper styleWrapper, String sldBody) {
        String workspaceName = styleWrapper.getWorkspace();
        String styleName = styleWrapper.getStyle();

        if (!workspaceValid(workspaceName)) {
            return false;
        }

        boolean result = false;
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        GeoServerRESTPublisher publisher = manager.getPublisher();

        if (publisher != null) {
            if (styleExists(workspaceName, styleName)) {
                result = replaceSLD(sldBody, workspaceName, styleName, publisher);
            } else {
                result = uploadNewStyle(sldBody, workspaceName, styleName, manager, publisher);
            }
        }

        if (result) {
            ConsoleManager.getInstance()
                    .information(
                            this,
                            String.format(
                                    "%s : %s %s/%s",
                                    Localisation.getString(
                                            GeoServerClient.class, "GeoServerClient.styleUploaded"),
                                    connection.getConnectionName(),
                                    workspaceName,
                                    styleName));
        }
        return result;
    }

    /**
     * Upload new style.
     *
     * @param sldBody the sld body
     * @param workspaceName the workspace name
     * @param styleName the style name
     * @param manager the manager
     * @param publisher the publisher
     * @return true, if successful
     */
    private boolean uploadNewStyle(
            String sldBody,
            String workspaceName,
            String styleName,
            GeoServerRESTManager manager,
            GeoServerRESTPublisher publisher) {
        boolean result;
        if (isDefaultWorkspace(workspaceName)) {
            result = publisher.publishStyle(sldBody, styleName, true);
        } else {
            GeoServerRESTReader reader = manager.getReader();

            if (reader != null) {
                if (!reader.existsWorkspace(workspaceName)
                        && !publisher.createWorkspace(workspaceName)) {

                    ConsoleManager.getInstance()
                            .error(
                                    this,
                                    Localisation.getField(
                                                    GeoServerClient.class,
                                                    "GeoServerClient.failedToCreateWorkspace")
                                            + workspaceName);
                    return false;
                }
            } else {
                return false;
            }
            result = publisher.publishStyleInWorkspace(workspaceName, sldBody, styleName);
        }
        return result;
    }

    /**
     * @param sldBody
     * @param workspaceName
     * @param styleName
     * @param publisher
     * @return
     */
    private boolean replaceSLD(
            String sldBody,
            String workspaceName,
            String styleName,
            GeoServerRESTPublisher publisher) {
        boolean result;
        if (isDefaultWorkspace(workspaceName)) {
            result = publisher.updateStyle(sldBody, styleName, true);
        } else {
            result = publisher.updateStyleInWorkspace(workspaceName, sldBody, styleName);
        }
        return result;
    }

    /**
     * Check if Workspace name is valid, e.g can't contain spaces.
     *
     * @param workspaceName the workspace name
     * @return true, if successful
     */
    private boolean workspaceValid(String workspaceName) {
        if (isDefaultWorkspace(workspaceName)) {
            return true;
        }

        return Pattern.matches("\\w+", workspaceName);
    }

    /**
     * Check to see if style exists.
     *
     * @param workspaceName the workspace name
     * @param styleName the style name
     * @return true, if successful
     */
    private boolean styleExists(String workspaceName, String styleName) {
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        GeoServerRESTReader reader = manager.getReader();
        if (reader != null) {
            if (isDefaultWorkspace(workspaceName)) {
                return reader.existsStyle(styleName, true);
            } else {
                // Check workspace exists first
                if (reader.existsWorkspace(workspaceName)) {
                    return reader.existsStyle(workspaceName, styleName);
                }
            }
        }
        return false;
    }

    /**
     * Update layer styles.
     *
     * @param layer the original layer
     * @return true, if successful
     */
    @Override
    public boolean updateLayerStyles(GeoServerLayer layer) {
        if (layer == null) {
            return false;
        }

        StyleWrapper updatedStyle = layer.getStyle();

        boolean ok = false;
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        GeoServerRESTPublisher publisher = manager.getPublisher();

        if (publisher != null) {
            GSLayerEncoder layerEncoder = new GSLayerEncoder();

            String defaultStyle;
            if (isDefaultWorkspace(updatedStyle.getWorkspace())) {
                defaultStyle = updatedStyle.getStyle();
            } else {
                defaultStyle = updatedStyle.getWorkspace() + ":" + updatedStyle.getStyle();
            }
            layerEncoder.setDefaultStyle(defaultStyle);
            ok =
                    publisher.configureLayer(
                            layer.getLayerWorkspace(), layer.getLayerName(), layerEncoder);
        }

        return ok;
    }

    /**
     * Gets the workspace list.
     *
     * @return the workspace list
     */
    @Override
    public List<String> getWorkspaceList() {
        List<String> workspaceList = null;

        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        if (manager != null) {
            GeoServerRESTReader reader = manager.getReader();
            if (reader != null) {
                workspaceList = reader.getWorkspaceNames();
            }
        }

        if (workspaceList == null) {
            workspaceList = new ArrayList<>();
        }

        return workspaceList;
    }

    /**
     * Delete style.
     *
     * @param styleToDelete the style to delete
     * @return true, if successful
     */
    @Override
    public boolean deleteStyle(StyleWrapper styleToDelete) {
        if (styleToDelete == null) {
            return false;
        }

        boolean result = false;
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        GeoServerRESTPublisher publisher = manager.getPublisher();

        if (publisher != null) {
            if (isDefaultWorkspace(styleToDelete.getWorkspace())) {
                result = publisher.removeStyle(styleToDelete.getStyle());
            } else {
                result =
                        publisher.removeStyleInWorkspace(
                                styleToDelete.getWorkspace(), styleToDelete.getStyle());
            }
        }

        return result;
    }

    /**
     * Gets the default workspace name.
     *
     * @return the default workspace name
     */
    @Override
    public String getDefaultWorkspaceName() {
        return DEFAULT_WORKSPACE_NAME;
    }

    /**
     * Returns the is connected flag.
     *
     * @return true, if is connected
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    @Override
    public GeoServerConnection getConnection() {
        return connection;
    }

    /**
     * Parses the style in default workspace.
     *
     * @param reader the reader
     * @param count the count
     * @param styleList the style list
     * @return the int
     */
    private int parseStyleInDefaultWorkspace(
            GeoServerRESTReader reader, int count, List<StyleWrapper> styleList) {
        // Read styles not in a workspace
        RESTStyleList geoServerStyleList = reader.getStyles();

        for (String style : geoServerStyleList.getNames()) {
            StyleWrapper newStyleWrapper = new StyleWrapper(DEFAULT_WORKSPACE_NAME, style);
            styleList.add(newStyleWrapper);

            if (parentObj != null) {
                parentObj.readStylesProgress(connection, count, count);
            }
            count++;
        }
        return count;
    }

    /**
     * Parses the style in workspace.
     *
     * @param reader the reader
     * @param styleMap the style map
     * @param count the count
     * @param workspaceName the workspace name
     * @return the int
     */
    private int parseStyleInWorkspace(
            GeoServerRESTReader reader,
            Map<String, List<StyleWrapper>> styleMap,
            int count,
            String workspaceName) {
        List<StyleWrapper> styleList;
        if (workspaceName != null) {
            RESTStyleList geoServerWorkspaceStyleList = reader.getStyles(workspaceName);

            styleList = new ArrayList<>();

            for (String style : geoServerWorkspaceStyleList.getNames()) {
                StyleWrapper newStyleWrapper = new StyleWrapper(workspaceName, style);
                styleList.add(newStyleWrapper);

                if (parentObj != null) {
                    parentObj.readStylesProgress(connection, count, count);
                }
                count++;
            }

            styleMap.put(workspaceName, styleList);
        }
        return count;
    }

    /**
     * Refresh workspace.
     *
     * @param workspaceName the workspace name
     */
    @Override
    public void refreshWorkspace(String workspaceName) {
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        if (manager != null) {
            GeoServerRESTReader reader = manager.getReader();

            if (reader != null) {
                Map<String, List<StyleWrapper>> styleMap = new LinkedHashMap<>();

                int count = 1;
                List<StyleWrapper> styleList = new ArrayList<>();

                if (workspaceName.compareTo(DEFAULT_WORKSPACE_NAME) == 0) {
                    parseStyleInDefaultWorkspace(reader, count, styleList);

                    styleMap.put(DEFAULT_WORKSPACE_NAME, styleList);
                } else {
                    // Read styles from workspace
                    parseStyleInWorkspace(reader, styleMap, count, workspaceName);
                }

                if (parentObj != null) {
                    parentObj.readStylesComplete(connection, styleMap, true);
                }
            }
        }
    }

    /**
     * Delete workspace.
     *
     * @param workspaceName the workspace name
     * @return true, if successful
     */
    @Override
    public boolean deleteWorkspace(String workspaceName) {
        if (workspaceName == null) {
            return false;
        }

        boolean result = false;
        GeoServerRESTManager manager = GeoServerRESTManagerFactory.getManager(connection);
        GeoServerRESTPublisher publisher = manager.getPublisher();

        if (publisher != null) {
            if (isDefaultWorkspace(workspaceName)) {
                ConsoleManager.getInstance()
                        .error(
                                this,
                                Localisation.getString(
                                        GeoServerClient.class,
                                        "GeoServerClient.cannotDeleteDefaultWorkspace"));
            } else {
                result = publisher.removeWorkspace(workspaceName, false);
            }
        }

        return result;
    }

    /**
     * Parses the layer list worker.
     *
     * @param reader the reader
     * @param existingWorkspaceList the existing workspace list
     * @param workspaceName the workspace name
     */
    private void parseLayerListWorker(
            GeoServerRESTReader reader, List<String> existingWorkspaceList, String workspaceName) {
        List<String> localWorkspaceList = null;

        if (workspaceName == null) {
            localWorkspaceList = existingWorkspaceList;
            localWorkspaceList.add(null); // Add the default workspace last
        }

        Map<String, List<GeoServerLayer>> layerMap = new LinkedHashMap<>();
        RESTLayerList layers = reader.getLayers();

        parseLayer(reader, localWorkspaceList, layerMap, layers);

        if (parentObj != null) {
            parentObj.readLayersComplete(connection, layerMap);
        }
    }

    /**
     * Parses the layer.
     *
     * @param reader the reader
     * @param localWorkspaceList the local workspace list
     * @param layerMap the layer map
     * @param layers the layers
     */
    private void parseLayer(
            GeoServerRESTReader reader,
            List<String> localWorkspaceList,
            Map<String, List<GeoServerLayer>> layerMap,
            RESTLayerList layers) {
        int count = 1;
        int total = layers.size();

        for (NameLinkElem featureTypeName : layers) {
            @SuppressWarnings("deprecation")
            RESTLayer layer = reader.getLayer(featureTypeName.getName());

            if (layer != null) {
                String layerName = layer.getName();
                String workspace = getLayerWorkspace(reader, localWorkspaceList, layerName);
                GeoServerLayer geoServerlayer = new GeoServerLayer();
                geoServerlayer.setLayerWorkspace(workspace);
                geoServerlayer.setLayerName(layerName);
                geoServerlayer.setConnection(connection);

                StyleWrapper styleWrapper = new StyleWrapper();
                styleWrapper.setStyle(layer.getDefaultStyle());

                String styleWorkspace = layer.getDefaultStyleWorkspace();
                styleWrapper.setWorkspace(
                        (styleWorkspace == null) ? DEFAULT_WORKSPACE_NAME : styleWorkspace);

                geoServerlayer.setStyle(styleWrapper);

                List<GeoServerLayer> layerList = layerMap.get(workspace);

                if (layerList == null) {
                    layerList = new ArrayList<>();
                    layerMap.put(workspace, layerList);
                }
                layerList.add(geoServerlayer);

                if (parentObj != null) {
                    parentObj.readLayersProgress(connection, count, total);
                }
            }

            count++;
        }
    }

    /**
     * Gets the layer workspace.
     *
     * @param reader the reader
     * @param localWorkspaceList the local workspace list
     * @param layerName the layer name
     * @return the layer workspace
     */
    private String getLayerWorkspace(
            GeoServerRESTReader reader, List<String> localWorkspaceList, String layerName) {
        String workspace = null;
        for (String workspaceNameToTest : localWorkspaceList) {
            if (reader.existsLayer(workspaceNameToTest, layerName, true)) {
                if (workspaceNameToTest == null) {
                    workspace = DEFAULT_WORKSPACE_NAME;
                } else {
                    workspace = workspaceNameToTest;
                }

                break;
            }
        }
        return workspace;
    }
}
