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
package com.sldeditor.common.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;

/**
 * Reads/writes an XML file using the generated JAXB classes.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ParseXML {

    /** The logger. */
    private static Logger logger = Logger.getLogger(ParseXML.class);

    /** The Constant UI_RESOURCE_FOLDER. */
    private static final String UI_RESOURCE_FOLDER = "/ui/";

    /**
     * Parses the xml files, validates against schema and reports any errors.
     *
     * @param resourceFolder the resource folder
     * @param resourceName the resource name
     * @param schemaResource the schema resource
     * @param classToParse the class to parse
     * @return the object
     */
    public static Object parseFile(String resourceFolder,
            String resourceName, String schemaResource, Class<?> classToParse)
    {
        String fullResourceName = resourceFolder + resourceName;

        logger.debug("Reading : " + fullResourceName);
        InputStream inputStream = ParseXML.class.getResourceAsStream(fullResourceName); 

        if(inputStream == null)
        {
            File file = new File(fullResourceName);

            if(!file.exists())
            {
                ConsoleManager.getInstance().error(ParseXML.class, Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource") + fullResourceName);
                return null;
            }
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                ConsoleManager.getInstance().error(ParseXML.class, Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource") + fullResourceName);
                return null;
            }
        }

        ValidationEventCollector vec = new ValidationEventCollector();
        URL xsdURL = ParseXML.class.getResource(schemaResource);
        try
        {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema schema = sf.newSchema(xsdURL);

            JAXBContext jaxbContext = JAXBContext.newInstance(classToParse);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setSchema(schema);
            jaxbUnmarshaller.setEventHandler(vec);

            return jaxbUnmarshaller.unmarshal(inputStream);
        } catch (SAXException e) {
            ConsoleManager.getInstance().exception(ParseXML.class, e);
        }
        catch (javax.xml.bind.UnmarshalException ex) {

            if (vec != null && vec.hasEvents()) {

                for (ValidationEvent ve : vec.getEvents()) {
                    String msg = ve.getMessage();
                    ValidationEventLocator vel = ve.getLocator();

                    String message = String.format("%s %s %s %s %s %d %s %d %s",
                            Localisation.getField(ParseXML.class, "ParseXML.failedToValidate"),
                            fullResourceName,
                            Localisation.getField(ParseXML.class, "ParseXML.usingXSD"),
                            xsdURL.toString(),
                            Localisation.getField(ParseXML.class, "ParseXML.line"),
                            vel.getLineNumber(),
                            Localisation.getField(ParseXML.class, "ParseXML.column"),
                            vel.getColumnNumber(),
                            msg);
                    ConsoleManager.getInstance().error(ParseXML.class, message);
                }
            }
        } catch (JAXBException e) {
            ConsoleManager.getInstance().exception(ParseXML.class, e);
        }
        return null;
    }

    /**
     * Parses the ui file.
     *
     * @param resourceString the resource string
     * @param schemaResource the schema resource
     * @param classToParse the class to parse
     * @return the object
     */
    public static Object parseUIFile(String resourceString,
            String schemaResource,
            Class<?> classToParse) {
        return parseFile(UI_RESOURCE_FOLDER, resourceString, schemaResource, classToParse);
    }

    /**
     * Write file.
     *
     * @param rootFolder the root folder
     * @param resourceFolder the resource folder
     * @param resourceName the resource name
     * @param schemaResource the schema resource
     * @param objectToWrite the object to write
     */
    public static void writeFile(String rootFolder,
            String resourceFolder,
            String resourceName,
            String schemaResource,
            Object objectToWrite)
    {
        if(objectToWrite == null)
        {
            ConsoleManager.getInstance().error(ParseXML.class, "No object to write");
        }
        
        JAXBContext contextObj;
        String fullResourceName = rootFolder + resourceFolder + resourceName;

        System.out.println("Writing : " + fullResourceName);
        try {
            contextObj = JAXBContext.newInstance(objectToWrite.getClass());
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallerObj.marshal(objectToWrite, new FileOutputStream(fullResourceName));
        } catch (JAXBException e) {
            ConsoleManager.getInstance().exception(ParseXML.class, e);
        } catch (FileNotFoundException e) {
            ConsoleManager.getInstance().exception(ParseXML.class, e);
        }
    } 
}
