#!/bin/bash

if [ $# -eq 0 ]
  then
    echo "update_versions.sh <version number>"
    exit 0
fi

NEW_VERSION="$1"

ROOT=../..
CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $ROOT
mvn versions:set -DnewVersion=$NEW_VERSION

cd modules/import
mvn versions:set -DnewVersion=$NEW_VERSION

cd $CURRENT_DIR
find $ROOT \( -name 'pom.xml.versionsBackup' \) -delete
python changelog-vbump.py -i -v $NEW_VERSION ../changelog/.github_changelog_generator

# Force Versions.h to be generated
cd $ROOT
mvn clean install -DskipTests
