@echo off
setlocal enabledelayedexpansion

rem ---- تنظیم آدرس جدید (مثل اسکریپت Shell) ----
set NEW_ADDRESS=https://api.onlineshop.com

rem ---- مسیر فایل pom لایه App ----
set POM_PATH=E:\MyJavaProject\Online_Shop\App\pom.xml

rem ---- خواندن نسخه فعلی از pom.xml با PowerShell ----
for /f "usebackq tokens=*" %%v in (`powershell -Command ^
  "(Select-Xml -Path '%POM_PATH%' -XPath '//artifactId[.=\"app\"]/following-sibling::version[1]').Node.InnerText"`) do (
    set current_version=%%v
)

echo Current version: !current_version!

rem ---- جدا کردن major, minor, patch ----
for /f "tokens=1-3 delims=." %%a in ("!current_version!") do (
    set major=%%a
    set minor=%%b
    set patch_with_extra=%%c
)

rem ---- پاک کردن -RELEASE یا هرچیزی بعد patch ----
for /f "tokens=1 delims=-" %%p in ("!patch_with_extra!") do (
    set /a patch=%%p + 1
)

set new_version=!major!.!minor!.!patch!-RELEASE
echo New version: !new_version!

rem ---- جایگزینی نسخه در pom.xml با PowerShell ----
powershell -Command "(gc '%POM_PATH%') -replace '<version>%current_version%</version>', '<version>%new_version%</version>' | Out-File -Encoding UTF8 '%POM_PATH%'"

echo Version incremented to: !new_version!

rem ---- لیست ماژول‌ها (مسیر کامل یا نسبی نسبت به Online_Shop) ----
set modules=common dataaccess service App

rem ---- حلقه برای اجرای Maven روی هر ماژول ----
for %%m in (%modules%) do (
    if exist "E:\MyJavaProject\Online_Shop\%%m" (
        cd "E:\MyJavaProject\Online_Shop\%%m"
        echo Building module %%m...
        "C:\Program Files\Apache\maven\apache-maven-3.9.11\bin\mvn" clean compile package install
        cd ..
    ) else (
        echo WARNING: Module %%m not found!
    )
)

pause
