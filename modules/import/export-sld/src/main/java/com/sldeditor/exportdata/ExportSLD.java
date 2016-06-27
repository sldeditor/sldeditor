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

package com.sldeditor.exportdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.exportdata.esri.MXDParser;

/**
 * The Class ExportSLD.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExportSLD {

    /** The output format. */
    private static SLDOutputFormatEnum outputFormat = SLDOutputFormatEnum.SLD;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        if((args.length != 2) && (args.length != 3))
        {
            System.out.println("Usage:");
            System.out.println("ExportSLD <input JSON file> <destination folder> [<output format>]");
            System.out.println("Where <output format> is SLD, YSLD.  The default IS SLD");
            System.exit(0);
        }

        String filename = args[0];
        File outputFolder = new File(args[1]);

        if(!outputFolder.exists())
        {
            outputFolder.mkdirs();
        }

        if(args.length == 3)
        {
            SLDOutputFormatEnum format;
            try
            {
                format = SLDOutputFormatEnum.valueOf(args[2]);
                outputFormat = format;
            }
            catch(IllegalArgumentException e)
            {
                System.err.println("Unknown output format : " + args[2]);
                System.exit(1);
            }
        }
        Map<String, SLDDataInterface> layerMap = MXDParser.readLayers(filename, outputFormat);

        if(layerMap != null)
        {
            for(String layerName : layerMap.keySet())
            {
                File f = new File(outputFolder, generateFilename(layerName));

                writeData(f, layerName, layerMap.get(layerName));
            }
        }
    }

    /**
     * Write data.
     *
     * @param f the f
     * @param layerName the layer name
     * @param sldData the sld data
     */
    private static void writeData(File f, String layerName, SLDDataInterface sldData) {
        System.out.println("Writing : " + f.getName());
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(f));
            bufferedWriter.write(sldData.getSld());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(bufferedWriter != null)
                {
                    bufferedWriter.close();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Generate filename.
     *
     * @param layerName the layer name
     * @return the string
     */
    private static String generateFilename(String layerName) {
        String fileExtension = "";

        switch(outputFormat)
        {
        case YSLD:
            fileExtension = "ysld";
            break;
        case SLD:
        default:
            fileExtension = "sld";
            break;
        }
        return String.format("%s.%s", layerName, fileExtension);
    }

}
