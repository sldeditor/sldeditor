#!/bin/sh

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
        cp bin/SLDEditor.jar .
	mvn clean
        mv SLDEditor.jar bin 
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
#cp $dirRunningFrom/SLDEditor.jar bin

find debian/changelog -type f -exec sed -i 's/unstable/'$dist'/g' {} \;
find debian/changelog -type f -exec sed -i 's/ (Closes: #nnnn)  <nnnn is the bug number of your ITP>//g' {} \;

bzr add .
bzr commit -m "Initial commit"
#bzr builddeb -- -us -uc
bzr builddeb -S
#cd ../build-area/
#pbuilder-dist $dist build sldeditor_$version-1.dsc

