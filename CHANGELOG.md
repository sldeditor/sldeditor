# Change Log

## [0.6.0](https://github.com/robward-scisys/sldeditor/tree/0.6.0) (2016-11-29)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.5.3...0.6.0)

**Implemented enhancements:**

- Removed restriction on adding any symbolizer to a symbol [\#168](https://github.com/robward-scisys/sldeditor/issues/168)

**Fixed bugs:**

- Disabling polygon stroke not handled correctly [\#173](https://github.com/robward-scisys/sldeditor/issues/173)
- Solid polygon fill opacity not set correctly [\#170](https://github.com/robward-scisys/sldeditor/issues/170)
- Solid line opacity not set [\#167](https://github.com/robward-scisys/sldeditor/issues/167)
- Save/Save As reload functionality not working [\#166](https://github.com/robward-scisys/sldeditor/issues/166)
- Opacity in line marker symbols is set overall, not individually for fill and stroke [\#164](https://github.com/robward-scisys/sldeditor/issues/164)
- Corrected flag marking MULTILINESTRING as having multiple geometries [\#178](https://github.com/robward-scisys/sldeditor/pull/178) ([robward-scisys](https://github.com/robward-scisys))

## [v0.5.3](https://github.com/robward-scisys/sldeditor/tree/v0.5.3) (2016-11-21)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.5.2...v0.5.3)

**Implemented enhancements:**

- Allow symbolizer geometry field to enter a function [\#156](https://github.com/robward-scisys/sldeditor/issues/156)
- Iss158 check app version at startup [\#159](https://github.com/robward-scisys/sldeditor/pull/159) ([robward-scisys](https://github.com/robward-scisys))
- Iss156 symbolizer geometry field function [\#157](https://github.com/robward-scisys/sldeditor/pull/157) ([robward-scisys](https://github.com/robward-scisys))

**Fixed bugs:**

- Modification of underlying SLD file ignore sticky data source flag [\#152](https://github.com/robward-scisys/sldeditor/issues/152)

**Closed issues:**

- Add feature to check for latest application version on startup  [\#158](https://github.com/robward-scisys/sldeditor/issues/158)
- Create version 0.5.2 [\#154](https://github.com/robward-scisys/sldeditor/issues/154)

**Merged pull requests:**

- Updated application to v0.5.3 [\#161](https://github.com/robward-scisys/sldeditor/pull/161) ([robward-scisys](https://github.com/robward-scisys))

## [v0.5.2](https://github.com/robward-scisys/sldeditor/tree/v0.5.2) (2016-11-18)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.5.1...v0.5.2)

**Implemented enhancements:**

- Correct position of arrow head at the end of a line [\#149](https://github.com/robward-scisys/sldeditor/issues/149)
- Update Maven plugin versions [\#143](https://github.com/robward-scisys/sldeditor/issues/143)
- Add sticky data source tool [\#138](https://github.com/robward-scisys/sldeditor/issues/138)
- Enable anti aliasing for rendered map [\#136](https://github.com/robward-scisys/sldeditor/issues/136)
- When uploading SLD file to GeoServer remove .sld file extension [\#134](https://github.com/robward-scisys/sldeditor/issues/134)
- Add tool to add arrows to line symbols [\#130](https://github.com/robward-scisys/sldeditor/issues/130)
- Handle case where underlying sld file of edited sld is updated [\#120](https://github.com/robward-scisys/sldeditor/issues/120)
- Correct position of arrow head [\#151](https://github.com/robward-scisys/sldeditor/pull/151) ([robward-scisys](https://github.com/robward-scisys))
- Iss138 sticky datasource tool [\#139](https://github.com/robward-scisys/sldeditor/pull/139) ([robward-scisys](https://github.com/robward-scisys))
- Had to create new file to adding anti-aliasing functionality [\#137](https://github.com/robward-scisys/sldeditor/pull/137) ([robward-scisys](https://github.com/robward-scisys))

**Fixed bugs:**

- Pan tool not working with new SLDMapPane class [\#148](https://github.com/robward-scisys/sldeditor/issues/148)
- SLD structure tree on legend panel not updating [\#146](https://github.com/robward-scisys/sldeditor/issues/146)
- Reduce DPI when rendering symbol [\#141](https://github.com/robward-scisys/sldeditor/issues/141)
- GeoServerInput unit tests affecting config.properties file [\#140](https://github.com/robward-scisys/sldeditor/issues/140)
- File changed dialog displayed after saving a file [\#132](https://github.com/robward-scisys/sldeditor/issues/132)
- Remove 'SEVERE: The following locker still has a lock: read on' warning for shapefile data sources [\#128](https://github.com/robward-scisys/sldeditor/issues/128)
- Field names in attribute expressions not being extracted from the SLD accurately [\#126](https://github.com/robward-scisys/sldeditor/issues/126)
- Crash occurs when reloading SLD file with spaces in the file name [\#124](https://github.com/robward-scisys/sldeditor/issues/124)
- Geometry type for internal data source not set correctly [\#121](https://github.com/robward-scisys/sldeditor/issues/121)
- Sticky data source flag respected when reloading SLD file [\#153](https://github.com/robward-scisys/sldeditor/pull/153) ([robward-scisys](https://github.com/robward-scisys))
- Introduced SLDTreeManager class to handle repopulating trees [\#147](https://github.com/robward-scisys/sldeditor/pull/147) ([robward-scisys](https://github.com/robward-scisys))
- Iss141 change symbol render dpi [\#145](https://github.com/robward-scisys/sldeditor/pull/145) ([robward-scisys](https://github.com/robward-scisys))
- Any unit tests writing to the property file now use a different filename [\#142](https://github.com/robward-scisys/sldeditor/pull/142) ([robward-scisys](https://github.com/robward-scisys))
- Iss134 remove sld file extension when uploading [\#135](https://github.com/robward-scisys/sldeditor/pull/135) ([robward-scisys](https://github.com/robward-scisys))

**Closed issues:**

- Create a 0.5.1 release [\#118](https://github.com/robward-scisys/sldeditor/issues/118)

**Merged pull requests:**

- Updated version to 0.5.2 [\#155](https://github.com/robward-scisys/sldeditor/pull/155) ([robward-scisys](https://github.com/robward-scisys))
- Fixed PanTool crash [\#150](https://github.com/robward-scisys/sldeditor/pull/150) ([robward-scisys](https://github.com/robward-scisys))
- Updated maven plugin version. [\#144](https://github.com/robward-scisys/sldeditor/pull/144) ([robward-scisys](https://github.com/robward-scisys))
- Added protection to prevent saving files triggering the reload dialog [\#133](https://github.com/robward-scisys/sldeditor/pull/133) ([robward-scisys](https://github.com/robward-scisys))
- Iss130 arrow tool [\#131](https://github.com/robward-scisys/sldeditor/pull/131) ([robward-scisys](https://github.com/robward-scisys))
- Forgot to close the SimpleFeatureIterator [\#129](https://github.com/robward-scisys/sldeditor/pull/129) ([robward-scisys](https://github.com/robward-scisys))
- Iss126 extract attribute type from expressions [\#127](https://github.com/robward-scisys/sldeditor/pull/127) ([robward-scisys](https://github.com/robward-scisys))
- Forgot to decode the URL properly, spaces were still %20 [\#125](https://github.com/robward-scisys/sldeditor/pull/125) ([robward-scisys](https://github.com/robward-scisys))
- Corrected determing geometry type for internal data sources [\#123](https://github.com/robward-scisys/sldeditor/pull/123) ([robward-scisys](https://github.com/robward-scisys))
- Added ReloadManager and unit test to handle the reloading of SLD files [\#122](https://github.com/robward-scisys/sldeditor/pull/122) ([robward-scisys](https://github.com/robward-scisys))

## [v0.5.1](https://github.com/robward-scisys/sldeditor/tree/v0.5.1) (2016-11-11)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.5.0...v0.5.1)

**Implemented enhancements:**

- Create a check for latest version tool [\#116](https://github.com/robward-scisys/sldeditor/issues/116)
- Create v0.6.0-SNAPSHOT [\#114](https://github.com/robward-scisys/sldeditor/issues/114)
- Create release 0.5.0 [\#112](https://github.com/robward-scisys/sldeditor/issues/112)
- Iss116 check latest version [\#117](https://github.com/robward-scisys/sldeditor/pull/117) ([robward-scisys](https://github.com/robward-scisys))

**Merged pull requests:**

- Updated to version 0.5.1 [\#119](https://github.com/robward-scisys/sldeditor/pull/119) ([robward-scisys](https://github.com/robward-scisys))
- Iss114 v0.6.0 snapshot [\#115](https://github.com/robward-scisys/sldeditor/pull/115) ([robward-scisys](https://github.com/robward-scisys))

## [v0.5.0](https://github.com/robward-scisys/sldeditor/tree/v0.5.0) (2016-11-09)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.4.2...v0.5.0)

**Implemented enhancements:**

- Remember last folder viewed in the file system view [\#110](https://github.com/robward-scisys/sldeditor/issues/110)
- Data source field values are not emptied when new SLD is loaded [\#108](https://github.com/robward-scisys/sldeditor/issues/108)
- Remove unused symbolizer fields [\#106](https://github.com/robward-scisys/sldeditor/issues/106)
- Add raster integration tests [\#104](https://github.com/robward-scisys/sldeditor/issues/104)
- Updates to use GeoTools 16 [\#97](https://github.com/robward-scisys/sldeditor/issues/97)
- Need to work out data source field types [\#96](https://github.com/robward-scisys/sldeditor/issues/96)
- Updated to use GeoTools 15.2 and Geoserver 2.9.2 [\#92](https://github.com/robward-scisys/sldeditor/issues/92)
- Implement raster symbols GeoServer vendor options [\#91](https://github.com/robward-scisys/sldeditor/issues/91)
- Iss108 last folder viewed [\#111](https://github.com/robward-scisys/sldeditor/pull/111) ([robward-scisys](https://github.com/robward-scisys))
- Data source ui fields are cleared when a file is loaded [\#109](https://github.com/robward-scisys/sldeditor/pull/109) ([robward-scisys](https://github.com/robward-scisys))
- Removed name, title and description fields [\#107](https://github.com/robward-scisys/sldeditor/pull/107) ([robward-scisys](https://github.com/robward-scisys))
- Iss104 raster integration tests [\#105](https://github.com/robward-scisys/sldeditor/pull/105) ([robward-scisys](https://github.com/robward-scisys))
- Updated to use GeoTools 16 and GeoServer 2.10.0 [\#100](https://github.com/robward-scisys/sldeditor/pull/100) ([robward-scisys](https://github.com/robward-scisys))
- Iss96 found datasource field types [\#99](https://github.com/robward-scisys/sldeditor/pull/99) ([robward-scisys](https://github.com/robward-scisys))
- Iss91 improve ui [\#95](https://github.com/robward-scisys/sldeditor/pull/95) ([robward-scisys](https://github.com/robward-scisys))
- Iss91 raster contrast normalize vendoroptions [\#94](https://github.com/robward-scisys/sldeditor/pull/94) ([robward-scisys](https://github.com/robward-scisys))
- Iss92 geotools geoserver [\#93](https://github.com/robward-scisys/sldeditor/pull/93) ([robward-scisys](https://github.com/robward-scisys))

**Fixed bugs:**

- NumberFormatExceptions raised when font size specified as float [\#102](https://github.com/robward-scisys/sldeditor/issues/102)
- Data source example values not remembered [\#98](https://github.com/robward-scisys/sldeditor/issues/98)
- HTML colour strings not displayed correctly on legend panel [\#89](https://github.com/robward-scisys/sldeditor/issues/89)
- Iss102 floats in string expecting integer [\#103](https://github.com/robward-scisys/sldeditor/pull/103) ([robward-scisys](https://github.com/robward-scisys))
- Iss98 data source values [\#101](https://github.com/robward-scisys/sldeditor/pull/101) ([robward-scisys](https://github.com/robward-scisys))

**Closed issues:**

- Update version to 0.5.0-SNAPSHOT \(again\) [\#87](https://github.com/robward-scisys/sldeditor/issues/87)
- Release 0.4.2 [\#85](https://github.com/robward-scisys/sldeditor/issues/85)

**Merged pull requests:**

- Release v0.5.0 [\#113](https://github.com/robward-scisys/sldeditor/pull/113) ([robward-scisys](https://github.com/robward-scisys))
- Corrected size of colour fields on the legend options panel [\#90](https://github.com/robward-scisys/sldeditor/pull/90) ([robward-scisys](https://github.com/robward-scisys))
- Updated project version to 0.5.0-SNAPSHOT \(again\) [\#88](https://github.com/robward-scisys/sldeditor/pull/88) ([robward-scisys](https://github.com/robward-scisys))

## [v0.4.2](https://github.com/robward-scisys/sldeditor/tree/v0.4.2) (2016-09-30)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.4.1...v0.4.2)

**Implemented enhancements:**

- Make colour map entries in raster symbolizer accept expressions [\#79](https://github.com/robward-scisys/sldeditor/issues/79)
- Release 0.4.1 [\#73](https://github.com/robward-scisys/sldeditor/issues/73)

**Fixed bugs:**

- Default anchor point and displacement values are not correct [\#82](https://github.com/robward-scisys/sldeditor/issues/82)
- Marker not visible in SLD tree when adding a new marker [\#80](https://github.com/robward-scisys/sldeditor/issues/80)
- New point markers now use default anchor point and displacement [\#83](https://github.com/robward-scisys/sldeditor/pull/83) ([robward-scisys](https://github.com/robward-scisys))
- When adding marker, marker symbol can now be edited [\#81](https://github.com/robward-scisys/sldeditor/pull/81) ([robward-scisys](https://github.com/robward-scisys))

**Closed issues:**

- Unit tests for render transformations [\#77](https://github.com/robward-scisys/sldeditor/issues/77)
- Update version to 0.5.0-SNAPSHOT [\#75](https://github.com/robward-scisys/sldeditor/issues/75)

**Merged pull requests:**

- Create v0.4.2 release [\#86](https://github.com/robward-scisys/sldeditor/pull/86) ([robward-scisys](https://github.com/robward-scisys))
- Iss79 colour map entry expressions [\#84](https://github.com/robward-scisys/sldeditor/pull/84) ([robward-scisys](https://github.com/robward-scisys))
- Iss77 render transformation unit tests [\#78](https://github.com/robward-scisys/sldeditor/pull/78) ([robward-scisys](https://github.com/robward-scisys))
- Update version to 0.5.0-SNAPSHOT [\#76](https://github.com/robward-scisys/sldeditor/pull/76) ([robward-scisys](https://github.com/robward-scisys))

## [v0.4.1](https://github.com/robward-scisys/sldeditor/tree/v0.4.1) (2016-09-22)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.3.0...v0.4.1)

**Implemented enhancements:**

- Set icons for the file system tree [\#70](https://github.com/robward-scisys/sldeditor/issues/70)
- Create release 0.3.0 [\#64](https://github.com/robward-scisys/sldeditor/issues/64)
- Added filesystem icons [\#72](https://github.com/robward-scisys/sldeditor/pull/72) ([robward-scisys](https://github.com/robward-scisys))
- Iss68 more unit tests [\#69](https://github.com/robward-scisys/sldeditor/pull/69) ([robward-scisys](https://github.com/robward-scisys))

**Closed issues:**

- Create more unit tests [\#68](https://github.com/robward-scisys/sldeditor/issues/68)
- Create release 0.4.0-SNAPSHOT [\#66](https://github.com/robward-scisys/sldeditor/issues/66)

**Merged pull requests:**

- Release v0.4.1 [\#74](https://github.com/robward-scisys/sldeditor/pull/74) ([robward-scisys](https://github.com/robward-scisys))
- Updated version to 0.4.0-SNAPSHOT in pom.xml [\#67](https://github.com/robward-scisys/sldeditor/pull/67) ([robward-scisys](https://github.com/robward-scisys))

## [v0.3.0](https://github.com/robward-scisys/sldeditor/tree/v0.3.0) (2016-09-12)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.2.0...v0.3.0)

**Implemented enhancements:**

- Testing for release 0.3.0 [\#62](https://github.com/robward-scisys/sldeditor/issues/62)
- Add dialog confirming discarding of edits [\#60](https://github.com/robward-scisys/sldeditor/issues/60)
- Implement raster legend graphics [\#56](https://github.com/robward-scisys/sldeditor/issues/56)
- Use GeoTools 15.1 [\#54](https://github.com/robward-scisys/sldeditor/issues/54)
- Add colour ramp to be applied to raster symbols [\#51](https://github.com/robward-scisys/sldeditor/issues/51)
- Add image outline support for raster symbols [\#49](https://github.com/robward-scisys/sldeditor/issues/49)
- Provide a GUI for user layer inline features [\#47](https://github.com/robward-scisys/sldeditor/issues/47)
- Implement user layer configuration [\#45](https://github.com/robward-scisys/sldeditor/issues/45)
- Release v0.3.0 [\#65](https://github.com/robward-scisys/sldeditor/pull/65) ([robward-scisys](https://github.com/robward-scisys))
- Iss62 version 0 3 0 tyre kick [\#63](https://github.com/robward-scisys/sldeditor/pull/63) ([robward-scisys](https://github.com/robward-scisys))
- Iss60 discard edits dialog [\#61](https://github.com/robward-scisys/sldeditor/pull/61) ([robward-scisys](https://github.com/robward-scisys))
- Iss56 raster legend [\#57](https://github.com/robward-scisys/sldeditor/pull/57) ([robward-scisys](https://github.com/robward-scisys))
- Updated to use GeoTools 15.1 [\#55](https://github.com/robward-scisys/sldeditor/pull/55) ([robward-scisys](https://github.com/robward-scisys))
- Iss51 raster colour ramp [\#52](https://github.com/robward-scisys/sldeditor/pull/52) ([robward-scisys](https://github.com/robward-scisys))
- Iss49 raster image outline [\#50](https://github.com/robward-scisys/sldeditor/pull/50) ([robward-scisys](https://github.com/robward-scisys))

**Fixed bugs:**

- Testing for release 0.3.0 [\#62](https://github.com/robward-scisys/sldeditor/issues/62)
- Marker symbol stroke attributes not correct [\#58](https://github.com/robward-scisys/sldeditor/issues/58)
- Handle inaccessible folders/drive letters in the file system [\#42](https://github.com/robward-scisys/sldeditor/issues/42)
- Symbol not rendered after raster symbol assigned a data source. [\#36](https://github.com/robward-scisys/sldeditor/issues/36)
- Iss62 version 0 3 0 tyre kick [\#63](https://github.com/robward-scisys/sldeditor/pull/63) ([robward-scisys](https://github.com/robward-scisys))

**Closed issues:**

- Unable to open .sld and .sldeditor [\#41](https://github.com/robward-scisys/sldeditor/issues/41)
- Implement geometry field for all symbolizers. [\#40](https://github.com/robward-scisys/sldeditor/issues/40)

**Merged pull requests:**

- Iss58 marker symbol stroke [\#59](https://github.com/robward-scisys/sldeditor/pull/59) ([robward-scisys](https://github.com/robward-scisys))
- Iss47 inline feature gui [\#48](https://github.com/robward-scisys/sldeditor/pull/48) ([robward-scisys](https://github.com/robward-scisys))
- Iss45 implement userstyle [\#46](https://github.com/robward-scisys/sldeditor/pull/46) ([robward-scisys](https://github.com/robward-scisys))
- Iss40 geometry field [\#44](https://github.com/robward-scisys/sldeditor/pull/44) ([robward-scisys](https://github.com/robward-scisys))
- Iss42 handle inaccessible folders [\#43](https://github.com/robward-scisys/sldeditor/pull/43) ([robward-scisys](https://github.com/robward-scisys))

## [v0.2.0](https://github.com/robward-scisys/sldeditor/tree/v0.2.0) (2016-07-18)
**Implemented enhancements:**

- NewSLDPanel unit test should not display panel [\#35](https://github.com/robward-scisys/sldeditor/issues/35)
- Remove unused opacity parameter in FieldConfigBase.populateExpression\(Object, Expression\) [\#31](https://github.com/robward-scisys/sldeditor/issues/31)
- Remove multiple field code, not used any more [\#29](https://github.com/robward-scisys/sldeditor/issues/29)
- Update XSD documentation in developers guide [\#22](https://github.com/robward-scisys/sldeditor/issues/22)
- Add change log process [\#20](https://github.com/robward-scisys/sldeditor/issues/20)
- Improve code coverage [\#16](https://github.com/robward-scisys/sldeditor/issues/16)
- Reorganise project [\#9](https://github.com/robward-scisys/sldeditor/issues/9)
- Add ability to run integration tests using a GUI in Travis CI. [\#7](https://github.com/robward-scisys/sldeditor/issues/7)

**Fixed bugs:**

- Stroke symbol fields are not enabled/disabled correctly [\#33](https://github.com/robward-scisys/sldeditor/issues/33)
- Code coverage results for integration tests being uploaded to Coveralls [\#27](https://github.com/robward-scisys/sldeditor/issues/27)
- SLDTreeTool.move\(\) not handling Styles and NamedLayers correctly [\#24](https://github.com/robward-scisys/sldeditor/issues/24)
- Problems installing on Windows [\#17](https://github.com/robward-scisys/sldeditor/issues/17)
- Integration tests occasionally failing [\#11](https://github.com/robward-scisys/sldeditor/issues/11)
- Incorrect external graphic field states [\#8](https://github.com/robward-scisys/sldeditor/issues/8)
- ScaleUtilTest fails to due locale dependency [\#2](https://github.com/robward-scisys/sldeditor/issues/2)
- Build failures on ConsoleManagerTest \(Java 8/Maven 3.3.9\) [\#1](https://github.com/robward-scisys/sldeditor/issues/1)
- Symbol rendered when data source updated [\#38](https://github.com/robward-scisys/sldeditor/pull/38) ([robward-scisys](https://github.com/robward-scisys))

**Merged pull requests:**

- Release v0.2.0 [\#39](https://github.com/robward-scisys/sldeditor/pull/39) ([robward-scisys](https://github.com/robward-scisys))
- Ignore NewSLDPanelTest [\#37](https://github.com/robward-scisys/sldeditor/pull/37) ([robward-scisys](https://github.com/robward-scisys))
- Iss33 stroke symbol field state [\#34](https://github.com/robward-scisys/sldeditor/pull/34) ([robward-scisys](https://github.com/robward-scisys))
- Remove FieldConfigBase.populateExpression\(\) opacity parameter [\#32](https://github.com/robward-scisys/sldeditor/pull/32) ([robward-scisys](https://github.com/robward-scisys))
- Iss29 remove multiple fields [\#30](https://github.com/robward-scisys/sldeditor/pull/30) ([robward-scisys](https://github.com/robward-scisys))
- Code coverage only generated for unit tests. [\#28](https://github.com/robward-scisys/sldeditor/pull/28) ([robward-scisys](https://github.com/robward-scisys))
- Iss24 sldtreetool move [\#26](https://github.com/robward-scisys/sldeditor/pull/26) ([robward-scisys](https://github.com/robward-scisys))
- Field states for external graphic now correct [\#25](https://github.com/robward-scisys/sldeditor/pull/25) ([robward-scisys](https://github.com/robward-scisys))
- Iss22 Updated xsd documentation [\#23](https://github.com/robward-scisys/sldeditor/pull/23) ([robward-scisys](https://github.com/robward-scisys))
- Add scripts and configuration to generate CHANGELOG.md [\#21](https://github.com/robward-scisys/sldeditor/pull/21) ([robward-scisys](https://github.com/robward-scisys))
- Iss16 code coverage [\#19](https://github.com/robward-scisys/sldeditor/pull/19) ([robward-scisys](https://github.com/robward-scisys))
- Flag to pre-populate top level folders set to false [\#18](https://github.com/robward-scisys/sldeditor/pull/18) ([robward-scisys](https://github.com/robward-scisys))
- Fixes for unit/integration tests [\#15](https://github.com/robward-scisys/sldeditor/pull/15) ([robward-scisys](https://github.com/robward-scisys))
- Make the SLD Editor appear on the GUI thread [\#13](https://github.com/robward-scisys/sldeditor/pull/13) ([robward-scisys](https://github.com/robward-scisys))
- Try clearing the folder selection first [\#12](https://github.com/robward-scisys/sldeditor/pull/12) ([robward-scisys](https://github.com/robward-scisys))
- Iss9 reorganise project [\#10](https://github.com/robward-scisys/sldeditor/pull/10) ([robward-scisys](https://github.com/robward-scisys))
- YSLD format now supported [\#6](https://github.com/robward-scisys/sldeditor/pull/6) ([robward-scisys](https://github.com/robward-scisys))
- Fixed ScaleUtilTest locale issue [\#5](https://github.com/robward-scisys/sldeditor/pull/5) ([robward-scisys](https://github.com/robward-scisys))
- Rastersymbol fixes [\#4](https://github.com/robward-scisys/sldeditor/pull/4) ([robward-scisys](https://github.com/robward-scisys))
- Text font updates [\#3](https://github.com/robward-scisys/sldeditor/pull/3) ([robward-scisys](https://github.com/robward-scisys))



\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*