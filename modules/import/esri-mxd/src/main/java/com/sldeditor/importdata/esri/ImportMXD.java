/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.esri.arcgis.carto.IActiveView;
import com.esri.arcgis.carto.IEnumLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.IMapDocument;
import com.esri.arcgis.carto.IPageLayout;
import com.esri.arcgis.carto.Map;
import com.esri.arcgis.carto.MapDocument;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.Pointer;

/**
 * Imports an Esri MXD file and writes it out as json.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ImportMXD
{
    /** The data. */
    private ConversionData data = new ConversionData();

    /** The ao init. */
    private static AoInitialize aoInit;

    /** The logger. */
    private static Logger logger = Logger.getLogger(ImportMXD.class);

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args)
    {
        boolean ok = false;

        FileData fileData = null;

        if(args.length == 2)
        {
            fileData = new FileData();
            fileData.setInputFile(args[0]);

            File f = fileData.getInputFile();
            if(!f.exists())
            {
                logger.error("Input mxd file does not exist");
                ok = false;
            }
            else
            {
                ok = true;
            }

            fileData.setOutputFile(args[1]);
            File outputFile = fileData.getOutputFile();
            File parentFolder = outputFile.getParentFile();

            if(!parentFolder.exists())
            {
                ok = parentFolder.mkdirs();

                if(ok)
                {
                    logger.info("Created output folder : " + parentFolder.getAbsolutePath());
                }
            }

            fileData.setOverwrite(true);

            List<String> errorMessages = new ArrayList<String>();
            boolean valid = fileData.isValid(true, errorMessages);
            
            if(!valid)
            {
                for(String errorMessage : errorMessages)
                {
                    System.err.println(errorMessage);
                }
                fileData = null;
                System.exit(2);
            }
        }

        if(!ok)
        {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } 
            catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(fileData != null)
        {
            processFile(fileData);
        }
        else
        {
            System.exit(2);
        }
    }

    /**
     * Process file.
     *
     * @param fileData the file data
     */
    public static void processFile(FileData fileData) {

        EngineInitializer.initializeEngine();

        initializeArcGISLicenses();

        ImportMXD instance = new ImportMXD();
        instance.convert(fileData);
    }

    /**
     * Default constructor
     */
    public ImportMXD()
    {
    }

    /**
     * Read mxd and write out the json file.
     *
     * @param fileData the file data
     */
    public void convert(FileData fileData)
    {
        if(fileData == null)
        {
            return;
        }

        // Get all the known conversion classes
        RegisterClasses.initialise(data);

        System.out.println("Reading MXD : " + fileData.getInputFile().getAbsolutePath());

        SystemWin sWin = new SystemWin();
        Pointer obj = sWin.getDesktopWindow();
        int hWnd = obj.getInt(0);

        try
        {
            System.out.println("Opening mxd...");
            IMapDocument mapDocument = new MapDocument();

            String password = null;

            mapDocument.open(fileData.getInputFile().getAbsolutePath(), password);

            IPageLayout iPageLayout = mapDocument.getPageLayout();
            IActiveView activeView = (IActiveView)iPageLayout;

            IMap iMap = activeView.getFocusMap();

            activeView.activate(hWnd);

            JsonArray jsonLayerlist = new JsonArray();
            int count = 1;
            int total = 0;

            // Find total number of layers
            IEnumLayer layerEnum = iMap.getLayers(null, true);

            ILayer layer = layerEnum.next();

            while(layer != null)
            {
                layer = layerEnum.next();
                total ++;
            }

            // Now work through all the layers
            layerEnum = iMap.getLayers(null, true);

            layer = layerEnum.next();

            ParseLayer parseLayer = new ParseLayer(data);

            while(layer != null)
            {
                parseLayer.convertLayer(count, total, jsonLayerlist, layer, (Map)iMap);
                layer = layerEnum.next();
                count ++;
            }

            JsonObject jsonMXDObject = new JsonObject();

            jsonMXDObject.addProperty("mxd", mapDocument.getDocumentFilename());
            jsonMXDObject.add("layers", jsonLayerlist);

            outputJSON(jsonMXDObject, fileData.getOutputFile());

            System.out.println("Written JSON file : " + fileData.getOutputFile().getAbsolutePath());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Output json.
     *
     * @param jsonMXDObject the json mxd object
     * @param outputFile the output file
     */
    private void outputJSON(JsonObject jsonMXDObject, File outputFile) {
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement el = parser.parse(jsonMXDObject.toString());
        String jsonString = gson.toJson(el);

        if(outputFile == null)
        {
            logger.info(jsonString.toString());
        }
        else
        {
            BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter( new FileWriter(outputFile.getAbsolutePath()));
                writer.write(jsonString);
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
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initialise ArcGIS licenses.
     */
    private static void initializeArcGISLicenses() {
        System.out.println("Initialise ArcGIS License");
        try {
            aoInit = new AoInitialize();

            if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngine) 
                    == esriLicenseStatus.esriLicenseAvailable)
                aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngine);
            else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeBasic) 
                    == esriLicenseStatus.esriLicenseAvailable)
                aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeBasic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
