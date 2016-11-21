<img src="https://github.com/robward-scisys/sldeditor/blob/master/doc/img/sldeditor-logo.png" height="92">
<a href="http://gis.scisys.co.uk"><img src="http://www.scisys.co.uk/storage/template/img/logo-scisys.jpg" hspace="100"></a>

[![Build Status](https://img.shields.io/travis/robward-scisys/sldeditor/master.svg)](https://travis-ci.org/robward-scisys/sldeditor)
[![Coverage Status](https://img.shields.io/coveralls/robward-scisys/sldeditor/master.svg)](https://coveralls.io/github/robward-scisys/sldeditor?branch=master)
[![License](https://img.shields.io/github/license/robward-scisys/sldeditor.svg)](https://github.com/robward-scisys/sldeditor/blob/master/LICENSE)
[![GitHub release](https://img.shields.io/github/release/robward-scisys/sldeditor.svg)](https://github.com/robward-scisys/sldeditor/releases/download/v0.5.3/SLDEditor.jar)
[![Github All Releases](https://img.shields.io/github/downloads/robward-scisys/sldeditor/total.svg)](https://github.com/robward-scisys/sldeditor/releases/download/v0.5.3/SLDEditor.jar)

The SLD Editor is a Java desktop application developed by [SCISYS](http://gis.scisys.co.uk) that allows the creation and editing of [OGC Styled Layer Descriptors](http://www.opengeospatial.org/standards/sld) interactively using a graphical user interface.<p>

The aim of the project is to develop an application enabling the generation of SLD files, where a user has complete control over all aspects of the OGC SLD standard.<p>
<p>

Main features include:
* Vector (point, line and polygon) symbol editing
* Raster symbol editing
* Text symbol editing
* Vector and raster legend image generation
* Functions, filters, expressions, transformations all configurable via dialogs.
* Inline feature support
* GeoServer vendor options supported:
  * Labelling
  * WKT geometry
  * Windbarbs
* Integration with GeoServer
* Map viewer
* YSLD import and export facility
* Tools to improve workflow.
* Ability to convert Esri MXD files to SLD files (requires separate licensed components not supplied as part of this project) [Build instructions here](https://github.com/robward-scisys/sldeditor/wiki/generatesld)

## Release
A compiled single executable jar is available here :
* [SLDEditor Release 0.5.3](https://github.com/robward-scisys/sldeditor/releases/download/v0.5.3/SLDEditor.jar) (MD5 : 51acc10cb0f1be65a2cad75710c5fcbb) Released 21 Nov 2016

## Background
I’ve been developing the application for about 18 months as a side project. The idea started when SciSys did some consultancy work and put together a proposal for an organisation to migrate from an Esri system to an OpenLayers/GeoServer stack. When it came to migrating ~1250 layers we didn’t know what number to put down for symbol conversion to SLD.  When you looked at the numbers this was a large percentage of the overall cost and something needed to be done to reduce it if we were going to be competitive in doing migrations to open source GIS systems.

One of our long standing large enterprise GIS systems required a change to an SLD symbol which we used QGIS to edit. It turned out there was a known bug that caused the change to be forgotten.  I looked to fix it, I’m very familiar with C++ and Qt but found it difficult to debug and realised the SLD styling data model was not completely implemented.

I looked at using GeoTools because I knew it could parse SLD files and render them. Another big advantage is that it supports all the GeoServer vendor options, in particular the labelling.  I got something simple working quite quickly and it went from there.

Ideally all this functionality should be in QGIS, but QGIS would have to be rewritten to use a C++ version of GeoTools to be able to render the GeoServer vendor options, which will not happen overnight.  I know of people that would love this capability and understand the reluctance for yet another application.  I think as long as GeoServer is so heavily used, a Java desktop SLD Editor application is acceptable.

There have been discussions about developing a web-front end for the application, a possibility. However not all GIS systems we deliver are web based and some customer networks do not allow web access or a web server to be deployed without going through a lot of hoops.  A standalone desktop application ensures there is no dependency on any other infrastructure.

One of the aims of the application is to work in a connected/disconnected environment.

The project was presented at [![FOSS4G UK 2016](http://uk.osgeo.org/foss4guk2016/images/foss4guk_2016_logo.png)](http://uk.osgeo.org/foss4guk2016/)

## Project State
The application is nearing a version 1.0.<p>

There are some further GeoServer vendor options yet to be implemented.

Further testing is needed to ensure the application is rock solid.

There will be some work needed to understand why some symbols don’t display. I’m thinking windbarbs where I believe I’m passing the correct data but the symbol is not rendered.

## License
The SLD Editor licensed under the [GPLv3](http://www.gnu.org/licenses/gpl-3.0.html).

This application is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

## Documentation
The project documentation exists in the GitHub project wiki.

- [User Guide](https://github.com/robward-scisys/sldeditor/wiki/userguide)
- [Developers Guide](https://github.com/robward-scisys/sldeditor/wiki/devguide)
- [Build documentation](https://github.com/robward-scisys/sldeditor/wiki/build)

