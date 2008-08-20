# package_windows.nsi
# =========================================================================


# build script for Windows installer
!include "MUI.nsh"

Name "PG -- Periodic Graph Utilities"

InstallDir $PROGRAMFILES\PG

InstallDirRegKey HKLM "Software\NSIS_PG" "Install_Dir"

#install macro's
Page custom DetectJRE
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
  !define MUI_FINISHPAGE_NOAUTOCLOSE
  !define MUI_FINISHPAGE_RUN
  !define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLink"
  #!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\README.txt
#

# build script for windows installer
!insertmacro MUI_PAGE_FINISH

#uninstall macro's
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

#languages
!insertmacro MUI_LANGUAGE "English"

Section "-PG"

  SetOutPath $INSTDIR

  File "../../dist/windows/PGVisualizer.exe"
  File "../../dist/windows/PGEmbedder.exe"
  File "../../dist/windows/PG.jar"
  File "../../dist/COPYRIGHT.txt"
  File "../../dist/LICENSE.txt"
  File /r "../../dist/windows/lib"
  #CreateDirectory "$INSTDIR\bin"

  WriteRegStr HKLM SOFTWARE\NSIS_PG "Install_Dir" "$INSTDIR"

  # write Windows Add/Remove uninstall information
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PG" "DisplayName" "PG -- Periodic Graph Utilities"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PG" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegStr HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\PG" "DisplayVersion" "1.0.2"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PG" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PG" "NoRepair" 1

  #WriteRegStr HKLM "Software\JavaSoft\Prefs\be\ugent\caagt\pg\preferences" "config.bindir" "$INSTDIR\bin"
  WriteUninstaller "uninstall.exe"

SectionEnd


Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\PG"
  CreateShortCut "$SMPROGRAMS\PG\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\PG\PGVisualizer.lnk" "$INSTDIR\PGVisualizer.exe" "" "$INSTDIR\PGVisualizer.exe" 0
  CreateShortCut "$SMPROGRAMS\PG\PGEmbedder.lnk" "$INSTDIR\PGEmbedder.exe" "" "$INSTDIR\PGEmbedder.exe" 0
  #CreateShortCut "$SMPROGRAMS\PG\README.lnk" "$INSTDIR\README.txt" "" "$INSTDIR\README.txt" 0
   
SectionEnd

Section "Uninstall"
  
  # Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PG"
  DeleteRegKey HKLM SOFTWARE\NSIS_PG
  DeleteRegKey HKLM "Software\JavaSoft\Prefs\be\ugent\caagt\pg\preferences"
  DeleteRegKey /ifempty HKLM "Software\JavaSoft\Prefs\be\ugent\caagt\pg"
  DeleteRegKey /ifempty HKLM "Software\JavaSoft\Prefs\be\ugent\caagt"
  DeleteRegKey /ifempty HKLM "Software\JavaSoft\Prefs\be\ugent"
  DeleteRegKey /ifempty HKLM "Software\JavaSoft\Prefs\be"

  # Remove files and uninstaller
  Delete $INSTDIR\PGVisualizer.exe
  Delete $INSTDIR\PGEmbedder.exe
  Delete $INSTDIR\PG.jar
  Delete $INSTDIR\uninstall.exe
  # Delete $INSTDIR\README.txt
  Delete $INSTDIR\COPYRIGHT.txt
  Delete $INSTDIR\LICENSE.txt
  RMDir /r /REBOOTOK "$INSTDIR\libs\"
  # RMDir /r /REBOOTOK "$INSTDIR\bin\"

  # Remove shortcuts, if any
  Delete "$SMPROGRAMS\PG\*.*"

  # Remove directories used
  RMDir /REBOOTOK "$SMPROGRAMS\PG"
  RMDir /REBOOTOK "$INSTDIR"

SectionEnd

Function LaunchLink
  ExecShell "" "$INSTDIR\PGVisualizer.exe"
FunctionEnd

Function DetectJRE
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" \
             "CurrentVersion"
  StrCmp $2 "1.5" done
  StrCmp $2 "1.6" done
  
  Call JRENotFound
  
  done:
FunctionEnd

Function JRENotFound
        MessageBox MB_OK "PG uses Java 5.0 or higher, please download it \
                         from www.java.com"
        Quit
FunctionEnd

Function .onInit
 
  ReadRegStr $R0 HKLM \
  "Software\Microsoft\Windows\CurrentVersion\Uninstall\PG" \
  "UninstallString"
  StrCmp $R0 "" done
 
  MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION \
  "PG is already installed. $\n$\nClick `OK` to remove the \
  previous version or `Cancel` to cancel this upgrade." \
  IDOK uninst
  Abort
  
#run the uninstaller
uninst:
  ClearErrors
  Exec $INSTDIR\uninstall.exe
  
done:
 
FunctionEnd
