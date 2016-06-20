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
package com.sldeditor.extension.filesystem.file.sldeditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.extension.filesystem.FileSystemUtils;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.impl.DataSourceProperties;
import com.sldeditor.filter.v2.envvar.EnvVar;

/**
 * Class that handles read/writing SLD Editor project files.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorFileHandler implements FileHandlerInterface
{
    private static final String ROOT_ELEMENT = "SLDEditor";
    private static final String YES = "yes";
    private static final String DATASOURCE_ELEMENT = "datasource";
    private static final String SLD_ELEMENT = "sld";
    private static final String ENVVARLIST_ELEMENT = "EnvVarList";
    private static final String ENVVAR_ELEMENT = "EnvVar";
    private static final String ENVVAR_VALUE_ATTRIBUTE = "value";
    private static final String ENVVAR_NAME_ATTRIBUTE = "name";
    private static final String ENVVAR_TYPE_ATTRIBUTE = "type";
    private static final String VENDOR_OPTION_ELEMENT = "vendorOption";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3193761375262410975L;

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileHandlerInterface#getFileExtension()
     */
    @Override
    public List<String> getFileExtensionList()
    {
        return Arrays.asList("sldeditor");
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#populate(com.sldeditor.extension.input.FileSystemInterface, javax.swing.tree.DefaultTreeModel, com.sldeditor.extension.input.file.FileTreeNode)
     */
    @Override
    public boolean populate(FileSystemInterface inputInferface,  DefaultTreeModel treeModel, FileTreeNode node)
    {
        // Do nothing
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileHandlerInterface#getSLDContents(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public List<SLDDataInterface> getSLDContents(NodeInterface node)
    {
        if(node instanceof FileTreeNode)
        {
            FileTreeNode fileTreeNode = (FileTreeNode)node;

            File f = fileTreeNode.getFile();

            if(f != null)
            {
                if(FileSystemUtils.isFileExtensionSupported(f, getFileExtensionList()))
                {
                    List<SLDDataInterface> list = new ArrayList<SLDDataInterface>();

                    SLDDataInterface sldData = readSLDEditorFile(f);
                    list.add(sldData);

                    return list;
                }
            }
        }
        return null;
    }

    /**
     * Read sld editor file.
     *
     * @param file the file
     * @return the SLD data
     */
    private SLDDataInterface readSLDEditorFile(File file)
    {
        SLDDataInterface sldData = null;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            String sldFile = extractTextData(document, SLDEditorFileHandler.SLD_ELEMENT);
            DataSourcePropertiesInterface dataSourceProperties = DataSourceProperties.decodeXML(document, SLDEditorFileHandler.DATASOURCE_ELEMENT);
            List<VersionData> vendorOptionList = extractVendorOptionData(document, SLDEditorFileHandler.VENDOR_OPTION_ELEMENT);
            List<EnvVar> envVarList = extractEnvironmentVariables(document, SLDEditorFileHandler.ENVVAR_ELEMENT);

            File f = new File(sldFile);
            String sldContents = readFile(f, Charset.defaultCharset());
            List<DataSourceFieldInterface> fieldList = null;

            sldData = new SLDData(new StyleWrapper(sldFile), sldContents);
            sldData.setDataSourceProperties(dataSourceProperties);
            sldData.setVendorOptionList(vendorOptionList);
            sldData.setFieldList(fieldList);
            sldData.setSLDFile(f);
            sldData.setReadOnly(false);
            sldData.setSldEditorFile(file);
            sldData.setEnvVarList(envVarList);
        } catch (ParserConfigurationException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (SAXException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return sldData;
    }

    /**
     * Extract environment variables.
     *
     * @param document the document
     * @param envvarElement the environment variable element
     * @return the list
     */
    private List<EnvVar> extractEnvironmentVariables(Document document,
            String elementName) {
        List<EnvVar> list = new ArrayList<EnvVar>();

        NodeList nodeList = document.getElementsByTagName(elementName);
        for(int index = 0; index < nodeList.getLength(); index ++)
        {
            Element element = (Element) nodeList.item(index);

            Class<?> type = null;
            try {
                type = Class.forName(element.getAttribute(ENVVAR_TYPE_ATTRIBUTE));
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
            EnvVar envVar = new EnvVar(element.getAttribute(ENVVAR_NAME_ATTRIBUTE),
                    type, false);  // Assume not predefined, will get sorted out later

            if(element.hasAttribute(ENVVAR_VALUE_ATTRIBUTE))
            {
                envVar.setValue(element.getAttribute(ENVVAR_VALUE_ATTRIBUTE));
            }

            list.add(envVar);
        }

        return list;
    }

    /**
     * Extract text data from XML node.
     *
     * @param document the document
     * @param elementName the element name
     * @return the string
     */
    private static String extractTextData(Document document, String elementName) {
        String value = "";
        NodeList nodeList = document.getElementsByTagName(elementName);
        if(nodeList.getLength() > 0)
        {
            value = nodeList.item(0).getTextContent();
        }

        return value;
    }

    /**
     * Extract vendor option data.
     *
     * @param document the document
     * @param elementName the element name
     * @return the list
     */
    private List<VersionData> extractVendorOptionData(Document document, String elementName) {
        List<VersionData> list = new ArrayList<VersionData>();

        // Try and remove any duplicates
        List<String> nameList = new ArrayList<String>();
        nameList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersion().getLatest().toString());
        list.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersion().getLatest());

        NodeList nodeList = document.getElementsByTagName(elementName);
        for(int index = 0; index < nodeList.getLength(); index ++)
        {
            String value = nodeList.item(index).getTextContent();

            VendorOptionVersion vendorOption = VendorOptionVersion.fromString(value);
            VersionData newVersion = vendorOption.getLatest();

            if(!nameList.contains(newVersion.toString()))
            {
                list.add(newVersion);
                nameList.add(newVersion.toString());
            }
        }

        return list;
    }

    /**
     * A common method for all enums since they can't have another base class.
     *
     * @param <T> Enum type
     * @param c enum type. All enums must be all caps.
     * @param string case insensitive
     * @return corresponding enum, or null
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if( c != null && string != null ) {
            try {
                return Enum.valueOf(c, string.trim());
            } catch(IllegalArgumentException ex) {
            }
        }
        return null;
    }

    /**
     * Read file.
     *
     * @param file the file
     * @param encoding the encoding
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String readFile(File file, Charset encoding)  throws IOException 
    {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        return new String(encoded, encoding);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#open(java.io.File)
     */
    @Override
    public List<SLDDataInterface> open(File file)
    {
        List<SLDDataInterface> list = null;

        if(FileSystemUtils.isFileExtensionSupported(file, getFileExtensionList()))
        {
            list = new ArrayList<SLDDataInterface>();

            SLDDataInterface sldData = readSLDEditorFile(file);
            list.add(sldData);
        }
        return list;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#save(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData)
    {
        if(sldData == null)
        {
            return false;
        }

        File fileToSave = sldData.getSldEditorFile();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element root = doc.createElement(SLDEditorFileHandler.ROOT_ELEMENT);
            doc.appendChild(root);

            // SLD contents
            Element sldElement = doc.createElement(SLDEditorFileHandler.SLD_ELEMENT);
            sldElement.appendChild(doc.createTextNode(sldData.getSLDFile().getAbsolutePath()));
            root.appendChild(sldElement);

            // Vendor options
            List<VersionData> vendorOptionList = sldData.getVendorOptionList();
            if(vendorOptionList != null)
            {
                for(VersionData versionData : vendorOptionList)
                {
                    Element vendorOptionElement = doc.createElement(SLDEditorFileHandler.VENDOR_OPTION_ELEMENT);

                    VendorOptionVersion vendorOption = new VendorOptionVersion(versionData.getVendorOptionType(), versionData);
                    vendorOptionElement.appendChild(doc.createTextNode(vendorOption.toString()));
                    root.appendChild(vendorOptionElement);
                }
            }

            // Write out the data source connector
            DataSourcePropertiesInterface dataSourceProperties = sldData.getDataSourceProperties();
            if(dataSourceProperties != null)
            {
                dataSourceProperties.encodeXML(doc, root, SLDEditorFileHandler.DATASOURCE_ELEMENT);
            }

            // Environment variables
            Element envVarListElement = doc.createElement(SLDEditorFileHandler.ENVVARLIST_ELEMENT);
            root.appendChild(envVarListElement);
            List<EnvVar> envVarList = sldData.getEnvVarList();
            if(envVarList != null)
            {
                for(EnvVar envVar : envVarList)
                {
                    Element envVarElement = doc.createElement(SLDEditorFileHandler.ENVVAR_ELEMENT);

                    envVarElement.setAttribute(SLDEditorFileHandler.ENVVAR_NAME_ATTRIBUTE, envVar.getName());
                    envVarElement.setAttribute(SLDEditorFileHandler.ENVVAR_TYPE_ATTRIBUTE, envVar.getType().getName());

                    if(envVar.getValue() != null)
                    {
                        String value = envVar.getValue().toString();
                        envVarElement.setAttribute(SLDEditorFileHandler.ENVVAR_VALUE_ATTRIBUTE, value);
                    }

                    envVarListElement.appendChild(envVarElement);
                }
            }

            // Output the XML file contents
            TransformerFactory tf = TransformerFactory.newInstance();

            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, SLDEditorFileHandler.YES);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, SLDEditorFileHandler.YES);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
            String outputXML = stringWriter.getBuffer().toString();

            BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter(new FileWriter(fileToSave));
                writer.write(outputXML);
            }
            catch (IOException e)
            {
            }
            finally
            {
                try
                {
                    if (writer != null)
                    {
                        writer.close();
                    }
                }
                catch (IOException e)
                {
                }
            }
        } catch (ParserConfigurationException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (TransformerConfigurationException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (TransformerFactoryConfigurationError e) {
            ConsoleManager.getInstance().exception(this, e.getException());
        } catch (TransformerException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return true;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#getSLDName(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public String getSLDName(SLDDataInterface sldData)
    {
        if(sldData != null)
        {
            return sldData.getLayerNameWithOutSuffix() + ExternalFilenames.addFileExtensionSeparator(SLDEditorFile.getSLDFileExtension());
        }

        return "";
    }

    /**
     * Returns if files selected are a data source, e.g. raster or vector.
     *
     * @return true, if is data source
     */
    @Override
    public boolean isDataSource() {
        return false;
    }
}
