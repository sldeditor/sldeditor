# SLD Editor

The SLD Editor is a graphical editor written in Java that allows a user to create, and edit [Styled Layer Descriptors][1]

[1]: http://www.opengeospatial.org/standards/sld        "Styled Layer Descriptors"

## File System

The file system is displayed in a window, it displays all folders in the file system and any known SLD Editor file types:

| File           | Description                       |
|----------------|-----------------------------------|
| file.sld       | Styled Layer Description File     |
| file.sldeditor | SLD Editor Workspace file         |
| file.json      | Esri MXD Import intermediate file |

By clicking on a file in the file system window it is loaded and displayed in the editing window.

## SLD File
An SLD file can be viewed or edited by:

- Clicking on an SLD file in the file system window.
- Using the SLD->Open menu option.

The symbol can be viewed and edited, any changes can be saved by choosing the SLD->Save or SLD->Save As menu options.

### Data Source Attributes
When an SLD file is loaded the application tries to extract all data source attributes referenced in the SLD.
When specifying a data source field as the source of an SLD data attribute then attributes of the correct type are presented to the user.
Further attributes can be added by extending the data source attributes.
When saving an SLD file only the SLD data is saved, any knowledge of other fields available in the data source are lost unless the SLD is saved as part of an SLD Editor workspace.

SLD Editor workspace files contain the following information:

* Point to an SLD file
* Data source attributes
* SLD options, e.g. vendor options supported

SLD Editor workspace files allow SLD files to be worked on without the constraint of be being attached to the data source.

### Opening SLD Editor Workspace File
SLD Editor workspace files are opened by:

- Selecting a .sldeditor file in the file system window.
- File->Open menu option

### Saving SLD Editor Workspace File
SLD Editor workspace files are saved by selecting either menu option:
- File->Save
- File->Save As

## Data Sources
Data sources contain the data that the SLD renders, typically this is a database, shape file etc.

Clicking on the Data Source tab shows the fields the SLD Editor is aware of. 
When loading a SLD file the SLD Editor extracts all the fields so that they can be be used to specify SLD attributes and the *Data source connector* is set to *No data source*.

To connect an SLD to data source, e.g. shape file, database etc. choose the data source from the drop down.

Configure the properties for the selected data source.

On successful connection to the data source the field names and types are read and stored within the SLD Editor workspace.

### Updating Fields
Fields can be added and removed as required.

## GeoServer
The SLD Editor application can maintain a persisted set of GeoServer connections.

### GeoServer Connections
Connections to GeoServer instances appear at the top of the file system window, under the **GeoServer** tree node.

To add a new connection, click on the **GeoServer** tree node and then click on the *New* button.
A dialog is displayed allowing GeoServer connection properties to be configured, on press the *OK* button the
GeoServer connection is displayed under the **GeoServer** tree node and is written to the application property file.
When the SLD Editor is started in the future the GeoServer connection will be read from the application property file and displayed
in the filesystem window.

Passwords are stored in the application property file as encrypted strings.

### Connecting to GeoServer

To connect to GeoServer click on the GeoServer connection name and then click on the *Connect* button.
On connection to a GeoServer instances the following information is read:

- All styles in all the workspaces.
- All layers in all the workspaces and the styles used to render them.

Once an sld file has been loaded it is displayed in the editor window.

## SLD Tree Structure
The structure of the SLD symbol is displayed in the tree view, clicking on individual tree components will:

- Display the selected part of the symbol in a form allowing attribute data to be viewed and edited.
- Render the selected part of the symbol.

Where a SLD symbol contains multiple rules they are all displayed at once. 

Where items are optional, e.g. fills and strokes, they can be enabled/disabled using the associated checkbox. 

### Adding Items

By selecting an item within the tree structure for SLD to feature type style and clicking on the + button will create an item of the correct type.

E.g. click on a feature type style will create an additional rule.

When clicking on a rule the specific point, line, polygon or text button must be clicked on to create the correct symbolizer.

### SLD Attribute Editing

The attributes for a part of the SLD symbol can be updated in a form view.  As changes are made they are reflected in the rendered symbol.

Most SLD attributes can be configured to take their value from:

| Type          | Description                                                                                        |
|---------------|----------------------------------------------------------------------------------------------------|
| Literal Value | Hardcoded value                                                                                    |
| Attribute     | A list of the fields contained within the associated data source that are valid for the field type |
| Expression    | An expression.                                                                                     |

After each item is selected the Apply button must be pressed.  Pressing Revert will discard any changes.

### Expression Panel
The panel is configured for the data type of the field.
The property fields are read from the connected data source and are restricted to the supplied field data type.
The functions are restricted according to the function return type matching the supplied field data type.

## Vendor Options
The SLD standard can be extended through the use of vendor options.
The vendor option supported for the symbol can be changed by selecting the Tools->Options menu option.
The following vendor options are supported:
- No Vendor options, strict SLD standard with no vendor options.
- GeoServer Vendor Options, the following GeoServer vendor options are supported:

 - Labelling
 - Random fill
 - WKT
 - ext:// shapes
 - windbarb:// Weather wind barbs

### Legend
The legend entry graphic is displayed in the editing window and redrawn when SLD attributes are updated.

The following options are available by clicking the right mouse button on the legend graphic:

- Show Filename - displays the filename of the sld file
- Show Style Name - displays the name of the style
- Copy to clipboard - copies the contents of the displayed legend graphic to the clipboard.
- Save As - presents a file dialog to save the contents of the displayed legend graphic to a file.

Further legend options are available as tools.

## Tools
The SLD Editor application tools are accessed by clicking on items in the file system window.
### Save Legend
Saves the legend images for a single style or group of styles to an output folder.

The tool is available if the following tree items are selected in the file system window:

| File                       | Description                                                                                          |
|----------------------------|------------------------------------------------------------------------------------------------------|
| file.sld                   | Saves the legend image of the selected Styled Layer Description file                                 |
| Folder                     | Saves the legend images of all the Styled Layer Description file in the selected folder              |
| GeoServer Style            | Saves the legend image of the selected GeoServer Style                                               |
| GeoServer Workspace folder | Saves the legend images of all the Styled Layer Description file in the selected GeoServer workspace |
| Layer within file.json     | Saves the legend image of the layer in Esri MXD Import intermediate file                             |
| file.json                  | Saves the legend images of all the layers in Esri MXD Import intermediate file                       |

Clicking on the *Legend* button presents a file dialog to allow the user to select a destination folder into which the legend images will be written.

The filename of the legend image is the name of the style with the file extension of the selected image format.

When saving a legend image the current legend drawing options are applied.  The legend drawing options can set by either:
- Updating them in the editor window
- Clicking on the *Options* button which will ensure the current selection in the file system window is not changed.

### Save Legend HTML
Saves the legend images for a single style or group of styles to an output folder as an HTML page.

The tool is available if the following tree items are selected in the file system window:

| File                       | Description                                                                                          |
|----------------------------|------------------------------------------------------------------------------------------------------|
| file.sld                   | Saves the legend image of the selected Styled Layer Description file                                 |
| Folder                     | Saves the legend images of all the Styled Layer Description file in the selected folder              |
| GeoServer Style            | Saves the legend image of the selected GeoServer Style                                               |
| GeoServer Workspace folder | Saves the legend images of all the Styled Layer Description file in the selected GeoServer workspace |
| Layer within file.json     | Saves the legend image of the layer in Esri MXD Import intermediate file                             |
| file.json                  | Saves the legend images of all the layers in Esri MXD Import intermediate file                       |

Clicking on the *HTML* button presents a file dialog to allow the user to select a destination folder into which the HTML will be written.

When saving a legend image the current legend drawing options are applied.  The legend drawing options can set by either:
- Updating them in the editor window
- Clicking on the *Options* button which will ensure the current selection in the file system window is not changed.

Loading the *index.html* written to the output folder in a web page viewer will display the table of legend entries.

### Scale
Displays in a table the scales at which SLD rules are displayed and allows the user to update them.

The tool is available if the following tree items are selected in the file system window:

| File                       | Description                                                                                                           |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------|
| file.sld                   | Displays the scales at which rules in a Styled Layer Description file are displayed.                                  |
| Folder                     | Displays the scales at which rules in all the Styled Layer Description files in the selected folder are displayed.    |
| GeoServer Style            | Displays the scales at which rules in a GeoServer style are displayed.                                                |
| GeoServer Workspace folder | Displays the scales at which rules in all the GeoServer styles within the selected GeoServer workspace are displayed. |

Clicking on the *Scale* button displays the dialog.

To change a scale double click in the table cell and enter a new scale.  The background of edited scales changes to red, white are unchanged.

| Button | Description                                                   |
|--------|---------------------------------------------------------------|
| Revert | Discard all changes.                                          |
| Apply  | Saves all changes to the updated files.                       |
| Ok     | Saves all changes to the updated files and closes the dialog. |
| Cancel | Discard all changes and close the dialog.                     |

### Updating GeoServer Layer Styles
Allows the style of a GeoServer layer to be updated from the SLD Editor application.

The tool is available if the following tree items are selected in the file system window:
| File            | Description                                          |
|-----------------|------------------------------------------------------|
| GeoServer Layer | Allows the style to be updated for a GeoServer layer |

Clicking on the *Layer* button presents a dialog of all the styles present on the GeoServer instance and allows the user to select a new style.

Clicking *Ok* updates the layer style.

### Save Separate SLD Files
Saves the layers within a collection to separate SLD files in a destination folder.

This tool is only available if the Import Esri MXD extension is built, this will require a valid Esri ArcGIS license to generate the intermediate file from an Esri MXD file.

The tool is available if the following tree items are selected in the file system window:

| File                               | Description                                                                                   |
|------------------------------------|-----------------------------------------------------------------------------------------------|
| Esri MXD Layer - Intermediate file | Saves the selected MXD layer to a SLD file in the supplied output folder.                     |
| Esri MXD File - Intermediate file  | Saves all the MXD layers in the MXD file to separate SLD files in the supplied output folder. |

Clicking on the *SLD* button presents a file dialog to allow the user to select a destination folder into which the SLD files will be written.

The filename of the SLD file is the layer name with the file extension *'.sld*

### Vector
There are two tools available when a user clicks on a supported vector format:

| Tool        | Description                                                                                                                                                     |
|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Import      | Creates a new SLD symbol from the selected vector file.,The data source is set to the selected vector file which therefore contains the data source attributes. |
| Data Source | Assigns the selected vector file as the data source of the currently loaded symbol.                                                                             |

The currently supported vector file formats:

- Shape files

### Raster
There are two tools available when a user clicks on a supported raster format:

| Tool        | Description                                                                                                                                                     |
|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Import      | Creates a new SLD symbol from the selected rasterfile. The data source is set to the selected raster file. |
| Data Source | Assigns the selected raster file as the data source of the currently loaded symbol.                                                                             |

The currently supported raster file formats:

- Image files (tiff/tif, jpg, gif)

## Undo
The SLD Editor application supports undo and redo functionality throughout.

## Importing Symbology
Symbology from other formats can be imported into the SLD Editor application as follows:
- Esri MXD - Esri MXD symbology can be imported into the application using a standalone application which generates an intermediate json file.
The file appears in the file system window as a folder which when expanded shows all the layers within the MXD file.
Clicking on a layer displays it in the editor window which allows it to be edited.
The contents of the intermediate file are read-only, and need to be saved to separate SLD files to retain any changes. 
