rem %1 = MXD file
rem %2 = Output folder
rem %3 = Output format, valid values are SLD, YSLD [optional, default is SLD]
set INTERMEDIATE_FILE=%TEMP%\\intermediate.json
"%AGSDESKTOPJAVA%\java\jre\bin\java" -Xss2m -Xmx512m -classpath "%AGSDESKTOPJAVA%\java\lib\arcobjects.jar";ImportMXD.jar com.sldeditor.importdata.esri.ImportMXD %1 %INTERMEDIATE_FILE%
"%JAVA_HOME%"\jre\bin\java -jar ExportSLD.jar %INTERMEDIATE_FILE% %2 %3
