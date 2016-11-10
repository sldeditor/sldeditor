if [%1]==[] goto usage

set NEW_VERSION=%1

set ROOT=..\..
set CURRENT_DIR=%~dp0%

cd %ROOT%
call mvn versions:set -DnewVersion=%NEW_VERSION%

cd modules\import
call mvn versions:set -DnewVersion=%NEW_VERSION%

cd %ROOT%
del /f /s pom.xml.versionsBackup

cd %CURRENT_DIR%
changelog-vbump.py -i -v %NEW_VERSION% ..\changelog\.github_changelog_generator


<<<<<<< HEAD
=======
rem Force Versions.h to be generated
cd %ROOT%
call mvn clean install -DskipTests

>>>>>>> master
goto end

:usage
echo Usage %0 <version number>
:end