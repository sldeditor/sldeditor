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
package com.sldeditor.datasource.connector.instance;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sldeditor.common.Controller;
import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Data source connector for raster file data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorRasterFile implements DataSourceConnectorInterface
{
    /** The supported file types. */
    private static String[] supportedFileTypes = {"jpg", "gif", "png", "tif", "tiff"};

    /** The supported file type list. */
    private List<String> supportedFileTypeList = Arrays.asList(supportedFileTypes);

    /** The data source text field. */
    private JTextField dataSourceTextField;

    /** The data source field panel. */
    private JPanel dataSourceFieldPanel;

    /** The data source. */
    private DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

    /**
     * Default constructor
     */
    public DataSourceConnectorRasterFile()
    {
        createUI();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return "Raster File";
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getPanel()
     */
    @Override
    public JPanel getPanel()
    {
        return dataSourceFieldPanel;
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        dataSourceFieldPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) dataSourceFieldPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);

        JButton btnNewData = new JButton(Localisation.getString(DataSourceConnectorRasterFile.class, "DataSourceConnectorRasterFile.data"));
        dataSourceFieldPanel.add(btnNewData);

        dataSourceTextField = new JTextField();
        dataSourceFieldPanel.add(dataSourceTextField);
        dataSourceTextField.setColumns(50);

        btnNewData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseDataSourceToOpen();
            }
        });
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public boolean accept(Map<String, String> propertyMap)
    {
        if(propertyMap == null)
        {
            return false;
        }

        String filename = DataSourceProperties.decodeFilename(propertyMap);
        String fileExtension = DataSourceConnectorFactory.getFileExtension(filename);

        if(fileExtension == null)
        {
            return false;
        }

        return (supportedFileTypeList.contains(fileExtension));
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public Map<String, String> accept(String filename) {
        if(filename == null)
        {
            return null;
        }

        String fileExtension = DataSourceConnectorFactory.getFileExtension(filename);

        if(fileExtension == null)
        {
            return null;
        }

        if(supportedFileTypeList.contains(fileExtension))
        {
            Map<String, String> propertyMap = new HashMap<String,String>();

            propertyMap.put(DataSourceConnectorInterface.FILE_MAP_KEY, filename);

            return propertyMap;
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDataSourceProperties()
     */
    @Override
    public DataSourcePropertiesInterface getDataSourceProperties(Map<String, String> propertyMap)
    {
        DataSourcePropertiesInterface properties = new DataSourceProperties(this);

        properties.setPropertyMap(propertyMap);

        return properties;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#populate(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public void populate(DataSourcePropertiesInterface dataSourceProperties)
    {
        if(dataSourceProperties != null)
        {
            String filename = dataSourceProperties.getFilename();
            dataSourceTextField.setText(filename);
        }
    }

    /**
     * Choose data source to open.
     */
    private void chooseDataSourceToOpen() {

        DataSourcePropertiesInterface dsProperties = SLDEditorFile.getInstance().getDataSource();
        String dataSourceString = null;

        if(dsProperties != null)
        {
            dataSourceString = dsProperties.getFilename();
        }

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Raster files", supportedFileTypes);
        fileChooser.setFileFilter(filter);

        try {
            if(dataSourceString != null)
            {
                File f = new File(new File(dataSourceString).getCanonicalPath());
                if(f.exists())
                {
                    fileChooser.setSelectedFile(f);
                }
            }

            int result = fileChooser.showOpenDialog(Controller.getInstance().getFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                dsProperties = getDataSourceProperties(DataSourceProperties.encodeFilename(selectedFile.getAbsolutePath()));

                SLDEditorFile.getInstance().setDataSource(dsProperties);
                if(dataSource != null)
                {
                    dataSource.connect(SLDEditorFile.getInstance());
                }
            }
        } catch (IOException e1) {
            ConsoleManager.getInstance().exception(this, e1);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#isEmpty()
     */
    @Override
    public boolean isEmpty()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getConnectionProperties(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public Map<String, String> getConnectionProperties(DataSourcePropertiesInterface dataSourceProperties)
    {
        if(dataSourceProperties != null)
        {
            return dataSourceProperties.getAllConnectionProperties();
        }
        return null;
    }

    /**
     * Checks if data source is inline.
     *
     * @return true, if data source is inline
     */
    @Override
    public boolean isInLine() {
        return false;
    }
}
