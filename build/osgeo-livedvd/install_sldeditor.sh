#!/bin/sh
# SLD Editor - The Open Source Java SLD Editor
#
# Copyright (C) 2016, SCISYS UK Limited
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
# This script will install SLD Editor,
#  assumes script is run with sudo priveleges.

./diskspace_probe.sh "`basename $0`" begin
BUILD_DIR=`pwd`
####

#add-apt-repository ppa:robward/sldeditorppa
#apt-get -q update

if [ -z "$USER_NAME" ] ; then
   USER_NAME="user"
fi
USER_HOME="/home/$USER_NAME"

apt-get install sldeditor

cp /usr/share/applications/SLDEditor.desktop "$USER_HOME/Desktop/"
chown -R $USER_NAME.$USER_NAME "$USER_HOME/Desktop/SLDEditor.desktop"

