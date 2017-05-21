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
# Creates a MacOS dmg installer using https://github.com/andreyvit/create-dmg
# Needs to be installed in the folder containing the sldeditor source folder. 
# git clone https://github.com/andreyvit/yoursway-create-dmg.git

test -f SLDEditor-Installer.dmg && rm SLDEditor-Installer.dmg
../../../yoursway-create-dmg/create-dmg \
--volname "SLDEditor Installer" \
--volicon "sldeditor.icns" \
--window-pos 200 120 \
--window-size 800 400 \
--icon-size 100 \
--icon Application.app 200 190 \
--hide-extension SLDEditor.app \
--app-drop-link 600 185 \
SLDEditor-Installer.dmg \
release/