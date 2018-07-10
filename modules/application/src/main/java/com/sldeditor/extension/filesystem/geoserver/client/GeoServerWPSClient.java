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

import com.sldeditor.common.DataTypeEnum;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.localisation.Localisation;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.wps10.ComplexDataCombinationsType;
import net.opengis.wps10.ComplexDataDescriptionType;
import net.opengis.wps10.LiteralOutputType;
import net.opengis.wps10.OutputDescriptionType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.ProcessOutputsType;
import net.opengis.wps10.SupportedCRSsType;
import net.opengis.wps10.SupportedComplexDataType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.impl.ProcessBriefTypeImpl;
import org.eclipse.emf.common.util.EList;
import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.ows.ServiceException;

/**
 * The Class GeoServerWPSClient, reads the GeoServer WPS process functions.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerWPSClient implements GeoServerWPSClientInterface {

    /** The Constant WPS_REQUEST_GET_CAPABILITIES. */
    private static final String WPS_REQUEST_GET_CAPABILITIES =
            "/ows?service=WPS&request=GetCapabilities";

    /** The vector geometry type array. */
    // CHECKSTYLE:OFF
    private static String[] vectorGeometryTypeArray = {
        "text/xml; subtype=wfs-collection/1.0",
        "text/xml; subtype=wfs-collection/1.1",
        "text/xml; subtype=gml/3.1.1",
        "text/xml; subtype=gml/2.1.2",
        "application/wkt",
        "application/json",
        "application/wfs-collection-1.0",
        "application/wfs-collection-1.1"
    };
    // CHECKSTYLE:ON

    /** The vector geometry type list. */
    private static List<String> vectorGeometryTypeList = Arrays.asList(vectorGeometryTypeArray);

    /** The raster geometry type array. */
    private static String[] rasterGeometryTypeArray = {"image/tiff", "application/arcgrid"};

    /** The raster geometry type list. */
    private static List<String> rasterGeometryTypeList = Arrays.asList(rasterGeometryTypeArray);

    /** The GeoServer connection. */
    private GeoServerConnection connection = null;

    /** The process list. */
    private List<ProcessDescriptionType> processList = new ArrayList<ProcessDescriptionType>();

    /**
     * Instantiates a new GeoServer WPS client.
     *
     * @param connection the connection
     */
    public GeoServerWPSClient(GeoServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Gets the capabilities.
     *
     * @return returns true if capabilities read, false if error
     */
    @SuppressWarnings({"rawtypes"})
    @Override
    public boolean getCapabilities() {
        boolean ok = false;

        try {
            processList.clear();

            String connectionString = connection.getUrl().toURI().toASCIIString();
            URL url = new URL(connectionString + WPS_REQUEST_GET_CAPABILITIES);

            WebProcessingService wps = new WebProcessingService(url);
            WPSCapabilitiesType capabilities = wps.getCapabilities();

            ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();

            EList processObjList = processOfferings.getProcess();
            for (Object processObj : processObjList) {
                ProcessBriefTypeImpl process = (ProcessBriefTypeImpl) processObj;
                String functionIdentifier = process.getIdentifier().getValue();

                // create a WebProcessingService as shown above,
                // then do a full describe process on my process
                DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();

                // describe the double addition process
                descRequest.setIdentifier(functionIdentifier);

                // send the request and get the ProcessDescriptionType bean to create a WPSFactory
                DescribeProcessResponse descResponse = wps.issueRequest(descRequest);
                ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
                ProcessDescriptionType pdt =
                        (ProcessDescriptionType) processDesc.getProcessDescription().get(0);

                processList.add(pdt);
            }

            ok = true;
        } catch (URISyntaxException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (ServiceException e) {
            ConsoleManager.getInstance().exception(this, e);
            ConsoleManager.getInstance()
                    .error(
                            this,
                            Localisation.getString(
                                    GeoServerWPSClient.class, "GeoServerWPSClient.noCapabilities"));
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return ok;
    }

    /**
     * Gets the render transformations.
     *
     * @param typeOfData the type of data
     * @return the render transformations
     */
    @Override
    public List<ProcessBriefType> getRenderTransformations(DataTypeEnum typeOfData) {
        List<ProcessBriefType> functionList = new ArrayList<ProcessBriefType>();

        for (ProcessDescriptionType processDescription : processList) {
            ConsoleManager.getInstance()
                    .information(this, processDescription.getTitle().getValue());

            boolean outputParameter =
                    getOutputParameter(typeOfData, processDescription.getProcessOutputs());
            boolean inputParameter = true;
            //            if (outputParameter) {
            //                inputParameter = getInputParameter(typeOfData,
            // processDescription.getDataInputs());
            //            }

            if (inputParameter && outputParameter) {
                functionList.add(processDescription);
            }
        }

        return functionList;
    }

    /**
     * Gets the input parameter.
     *
     * @param typeOfData the type of data
     * @param dataInputs the data inputs
     * @return the input parameter
     */
    /*
        private boolean getInputParameter(DataTypeEnum typeOfData, DataInputsType dataInputs) {
            ConsoleManager.getInstance().information(this,
                    "Inputs");

            for (Object dataInput : dataInputs.getInput()) {
                InputDescriptionType input = (InputDescriptionType) dataInput;

                if (input instanceof InputDescriptionType) {
                    InputDescriptionType inputDescription = (InputDescriptionType) input;

                    LiteralInputType literal = inputDescription.getLiteralData();
                    if (literal != null) {
                        DomainMetadataType dataType = literal.getDataType();

                        ConsoleManager.getInstance().information(this,
                                dataType.getValue());
                        } else {
                        SupportedCRSsType bbox = inputDescription.getBoundingBoxData();
                        if (bbox != null) {
                            ConsoleManager.getInstance().information(this,
                                    "BBox");
                        } else {
                            SupportedComplexDataType complex = inputDescription.getComplexData();
                            if (complex != null) {
                                ComplexDataCombinationsType parameterDataType = complex.getSupported();
                                if (isGeometry(typeOfData, parameterDataType)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    */
    /**
     * Gets the output parameter.
     *
     * @param typeOfData the type of data
     * @param processOutputs the process outputs
     * @return the output parameter
     */
    private boolean getOutputParameter(DataTypeEnum typeOfData, ProcessOutputsType processOutputs) {
        ConsoleManager.getInstance().information(this, "Outputs");

        for (Object o : processOutputs.getOutput()) {
            if (o instanceof OutputDescriptionType) {
                OutputDescriptionType oo = (OutputDescriptionType) o;

                LiteralOutputType literal = oo.getLiteralOutput();
                if (literal != null) {
                    DomainMetadataType dataType = literal.getDataType();

                    if (dataType != null) {
                        ConsoleManager.getInstance().information(this, dataType.getValue());
                    }
                } else {
                    SupportedCRSsType bbox = oo.getBoundingBoxOutput();
                    if (bbox != null) {
                        ConsoleManager.getInstance().information(this, "BBox");
                    } else {
                        SupportedComplexDataType complex = oo.getComplexOutput();
                        if (complex != null) {
                            ComplexDataCombinationsType parameterDataType = complex.getSupported();
                            if (isGeometry(typeOfData, parameterDataType)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if parameter is a geometry.
     *
     * @param typeOfData the type of data
     * @param dataType the data type
     * @return true, if is geometry
     */
    private boolean isGeometry(DataTypeEnum typeOfData, ComplexDataCombinationsType dataType) {

        int count = 0;

        for (Object obj : dataType.getFormat()) {
            if (obj instanceof ComplexDataDescriptionType) {
                ComplexDataDescriptionType format = (ComplexDataDescriptionType) obj;
                ConsoleManager.getInstance().information(this, format.getMimeType());
                switch (typeOfData) {
                    case E_VECTOR:
                        if (vectorGeometryTypeList.contains(format.getMimeType())) {
                            count++;
                        }
                        break;
                    case E_RASTER:
                        if (rasterGeometryTypeList.contains(format.getMimeType())) {
                            count++;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return (count > 0);
    }
}
