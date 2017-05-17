#!/bin/sh

pwd=$(pwd)

cd ../../../
if [ ! -d pkgdeb ]; then
	mkdir pkgdeb
	cd pkgdeb
	git clone https://github.com/robward-scisys/sldeditor.git
	cd sldeditor
	git checkout iss339-deb-package
	rm -rf .git
	mvn clean install -DskipTests
	cd ..
	tar -zcvf sldeditor-0.7.5.tar.gz sldeditor
	rm -rf sldeditor
else
	cd pkgdeb
	rm -rf sldeditor
fi

export DEBFULLNAME="Robert Ward"
export DEBEMAIL="sldeditor.group@gmail.com"
bzr whoami "$DEBFULLNAME '<'$DEBEMAIL'>'"
bzr dh-make sldeditor 0.7.5 sldeditor-0.7.5.tar.gz
cd sldeditor
cd debian
rm *ex *EX README.*
cd ..
cp $pwd/control debian
cp $pwd/sldeditor.install debian
cp $pwd/copyright debian

bzr add .
bzr commit -m "Initial commit"
bzr builddeb -- -us -uc
