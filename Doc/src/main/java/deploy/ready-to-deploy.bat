@echo off
setlocal enabledelayedexpansion

:: Define new address
set NEW_ADDRESS=shop.badrnezhad.com

:: Get current version from app/pom.xml
for /f "tokens=1,2 delims=<>" %%i in ('findstr "<artifactId>app</artifactId>" app\pom.xml /n') do (
    set /a lineNumber=%%i + 1
)
for /f "tokens=3 delims=<>" %%i in ('more +%lineNumber% app\pom.xml ^| findstr "<version>"') do (
    set current_version=%%i
)

:: Parse the version into major, minor, and patch
for /f "tokens=1,2,3 delims=.-" %%a in ("%current_version%") do (
    set major=%%a
    set minor=%%b
    set patch=%%c
)

:: Increment patch version
set /a patch+=1

:: Form new version string
set new_version=%major%.%minor%.%patch%-RELEASE

:: Update version in pom.xml
powershell -Command "(gc app\pom.xml) -replace '<version>%current_version%</version>', '<version>%new_version%</version>' | Out-File app\pom.xml -encoding ASCII"
echo Version incremented to: %new_version%

:: List of modules
set modules=common dataaccess service app

:: Loop through each module and execute Maven commands
for %%m in (%modules%) do (
    cd %%m
    "C:\Users\holosen\IdeaProjects\maven\apache-maven-3.9.9\bin\mvn" clean compile package install
    cd ..
)

endlocal
pause