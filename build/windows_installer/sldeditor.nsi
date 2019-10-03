; sldeditor.nsi
;
; This script is based on example1.nsi, but it remember the directory, 
; has uninstall support and (optionally) installs start menu shortcuts.
;
; It will install example2.nsi into a directory that the user selects,

!include MUI2.nsh

;--------------------------------

; The name of the installer
Name "SLDEditor"
!define MUI_ICON sldeditor.ico

; The file to write
OutFile "..\..\bin\SLDEditor_installer.exe"

; The default installation directory
InstallDir $PROGRAMFILES\SLDEditor

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\SLDEditor" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

;--------------------------------

; Pages

  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"
  
;--------------------------------

; The stuff to install
Section "SLDEditor (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File "..\..\bin\SLDEditor.jar"
  
  ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\SLDEditor "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SLDEditor" "DisplayName" "SLDEditor"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SLDEditor" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SLDEditor" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SLDEditor" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\SLDEditor"
  CreateShortcut "$SMPROGRAMS\SLDEditor\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortcut "$SMPROGRAMS\SLDEditor\SLDEditor.lnk" "$INSTDIR\SLDEditor.jar" "" "$INSTDIR\SLDEditor.jar" 0
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SLDEditor"
  DeleteRegKey HKLM SOFTWARE\SLDEditor

  ; Remove files and uninstaller
  Delete $INSTDIR\SLDEditor.jar
  Delete $INSTDIR\uninstall.exe
  
  Delete "$PROFILE\AppData\Roaming\SLDEditor\*.*"

  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\SLDEditor\*.*"

  ; Remove directories used
  RMDir "$PROFILE\AppData\Roaming\SLDEditor"
  RMDir "$SMPROGRAMS\SLDEditor"
  RMDir $INSTDIR

SectionEnd
