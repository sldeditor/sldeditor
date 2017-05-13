#!/bin/sh

rm -rf debian

mkdir debian
echo "7" > debian/compat

mkdir -p debian/source
echo "3.0 (native)" > debian/source/format

cp copyright debian/

cp control debian/

cp rules debian/
chmod +x debian/rules

cp changelog debian/

cp ../../bin/SLDEditor.jar lib
