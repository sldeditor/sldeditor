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
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Data source connector for shape file data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorShapeFile implements DataSourceConnectorInterface
{
    /** The supported file types. */
    private static String[] supportedFileTypes = {"shp"};

    /** The supported file type list. */
    private List<String> supportedFileTypeList = Arrays.asList(supportedFileTypes);

    /** The data source text field. */
    private JTextField dataSourceTextField = null;

    /** The data source field panel. */
    private JPanel dataSourceFieldPanel = null;

    /** The data source. */
    private DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

    /**
     * Default constructor
     */
    public DataSourceConnectorShapeFile()
    {
        createUI();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return "Shape File";
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
        FlowLayout flowLayout = new FlowLayout();
        dataSourceFieldPanel.setLayout(flowLayout);

        JButton btnNewData = new JButton(Localisation.getString(DataSourceConnectorShapeFile.class, "DataSourceConnectorShapeFile.data"));
        dataSourceFieldPanel.add(btnNewData);

        dataSourceTextField = new JTextField(50);
        dataSourceTextField.setEditable(false);
        dataSourceFieldPanel.add(dataSourceTextField);

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
            String filename = ExternalFilenames.convertURLToFile(dataSourceProperties.getFilename());
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
            if(dsProperties.getDataSourceConnector().getClass() == getClass())
            {
                dataSourceString = ExternalFilenames.convertURLToFile(dsProperties.getFilename());
            }
            else
            {
                dataSourceString = dataSourceTextField.getText();
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Shape files", "shp");
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

                dsProperties = getDataSourceProperties(DataSourceProperties.encodeFilename(selectedFile.toURI().toURL().toString()));

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

    /* (non-Javadoc)
     * @see com.sldeditor.common.DataSourceConnectorInterface#reset()
     */
    @Override
    public void reset() {
        dataSourceTextField.setText("");
    }
}
