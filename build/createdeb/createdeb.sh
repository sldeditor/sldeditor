#!/bin/sh
# SLD Editor - The Open Source Java SLD Editor
#
# Copyright (C) 2017, SCISYS UK Limited
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# About:
# =====
# Creates a Ubuntu debian sources package

if [ -z "$1" ]; then
	echo 'No version set'
	echo 'createdeb.sh <version no>'
	exit 1
fi
version=$1
dist=xenial
dirRunningFrom=$(pwd)

cd ../../../
if [ ! -d pkgdeb ]; then
	mkdir pkgdeb
	cd pkgdeb
	git clone https://github.com/robward-scisys/sldeditor.git
	cd sldeditor
	git checkout iss339-deb-package
	rm -rf .git
        cd build/update_version
        ./update_versions.sh $version
        cd ../..
	mvn clean
	cp $dirRunningFrom/Makefile .
	cd ..
	tar -zcvf sldeditor-$version.tar.gz sldeditor
	rm -rf sldeditor
else
	cd pkgdeb
	rm -rf sldeditor
fi

export DEBFULLNAME="Robert Ward"
export DEBEMAIL="sldeditor.group@gmail.com"
bzr whoami "$DEBFULLNAME '<'$DEBEMAIL'>'"
bzr dh-make sldeditor $version sldeditor-$version.tar.gz
cd sldeditor
cd debian
rm *ex *EX README.*
cd ..
cp $dirRunningFrom/control debian
cp $dirRunningFrom/sldeditor.install debian
cp $dirRunningFrom/copyright debian
cp $dirRunningFrom/rules debian

find debian/changelog -type f -exec sed -i 's/unstable/'$dist'/g' {} \;
find debian/changelog -type f -exec sed -i 's/ (Closes: #nnnn)  <nnnn is the bug number of your ITP>//g' {} \;

bzr add .
bzr commit -m "Initial commit"
bzr builddeb -S

