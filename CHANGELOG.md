# Change Log

## [0.3.0](https://github.com/robward-scisys/sldeditor/tree/0.3.0) (2016-09-12)
[Full Changelog](https://github.com/robward-scisys/sldeditor/compare/v0.2.0...0.3.0)

**Implemented enhancements:**

- Add dialog confirming discarding of edits [\#60](https://github.com/robward-scisys/sldeditor/issues/60)
- Implement raster legend graphics [\#56](https://github.com/robward-scisys/sldeditor/issues/56)
- Use GeoTools 15.1 [\#54](https://github.com/robward-scisys/sldeditor/issues/54)
- Add colour ramp to be applied to raster symbols [\#51](https://github.com/robward-scisys/sldeditor/issues/51)
- Add image outline support for raster symbols [\#49](https://github.com/robward-scisys/sldeditor/issues/49)
- Provide a GUI for user layer inline features [\#47](https://github.com/robward-scisys/sldeditor/issues/47)
- Implement user layer configuration [\#45](https://github.com/robward-scisys/sldeditor/issues/45)

**Fixed bugs:**

- Marker symbol stroke attributes not correct [\#58](https://github.com/robward-scisys/sldeditor/issues/58)
- Handle inaccessible folders/drive letters in the file system [\#42](https://github.com/robward-scisys/sldeditor/issues/42)
- Symbol not rendered after raster symbol assigned a data source. [\#36](https://github.com/robward-scisys/sldeditor/issues/36)

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