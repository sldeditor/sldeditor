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
package com.sldeditor.importdata.esri;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * The Class FileData encapsulates input and output file information.
 *
 * @author Robert Ward (SCISYS)
 */
public class FileData {

    /** The input file. */
    private File inputFile;

    /** The output file. */
    private File outputFile;

    /** The overwrite destination file flag. */
    private boolean overwrite = false;

    /**
     * Gets the input file.
     *
     * @return the inputFile
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * Sets the input file.
     *
     * @param inputFile the inputFile to set
     */
    public void setInputFile(String inputFile) {
        this.inputFile = (inputFile == null) ? null : new File(inputFile);
    }

    /**
     * Gets the output file.
     *
     * @return the outputFile
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * Sets the output file.
     *
     * @param outputFile the outputFile to set
     */
    public void setOutputFile(String outputFile) {
        this.outputFile = (outputFile == null) ? null : new File(outputFile);
    }

    /**
     * Checks if input/output files are valid.
     *
     * @param runFromCommandLine the run from command line
     * @param errorMessages the error messages
     * @return true, if is valid
     */
    public boolean isValid(boolean runFromCommandLine, List<String> errorMessages) {
        boolean inputValid = false;
        boolean outputValid = false;

        if(inputFile != null)
        {
            if(!inputFile.isFile())
            {
                errorMessages.add("Input file is not a file.");
            }
            else
            {
                if(!inputFile.exists())
                {
                    errorMessages.add("Input file does not exist.");
                }
                else
                {
                    inputValid = true;
                }
            }
        }
        else
        {
            errorMessages.add("Input file not specified.");
        }

        if(outputFile != null)
        {
            if(runFromCommandLine)
            {
                if(outputFile.exists() && !overwrite)
                {
                    errorMessages.add("Output file already exists, use -overwrite option on the command line.");
                }
                else
                {
                    outputValid = true;
                }
            }
            else
            {
                if(outputFile.exists() && !overwrite)
                {
                    int result = JOptionPane.showConfirmDialog(null, "Overwrite output file?", "Output file exists",
                            JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION)
                    {
                        overwrite = true;
                        outputValid = true;
                    }
                    else
                    {
                        errorMessages.add("Output file already exists.");
                    }
                }
                else
                {
                    outputValid = true;
                }
            }

            // Check folder containing file exists
            if(outputValid)
            {
                if(outputFile.getParentFile() != null)
                {
                    outputValid = outputFile.getParentFile().exists();
                    if(!outputValid)
                    {
                        errorMessages.add("Path of output file does not exist.");
                    }
                }
            }
        }
        else
        {
            errorMessages.add("Output file not specified.");
        }

        return inputValid && outputValid;
    }

    /**
     * Checks if is overwrite.
     *
     * @return the overwrite
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * Sets the overwrite.
     *
     * @param overwrite the overwrite to set
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }
}
