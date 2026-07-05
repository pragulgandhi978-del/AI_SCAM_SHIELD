@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------
@echo off
set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

for /f "usebackq tokens=1,2 delims==" %%a in ("%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties") do (
    if "%%a"=="wrapperUrl" set DOWNLOAD_URL=%%b
)

if exist %WRAPPER_JAR% (
    goto runWithJavaHome
)

echo Downloading Maven wrapper jar...
powershell -Command "(New-Object System.Net.WebClient).DownloadFile('%DOWNLOAD_URL%', %WRAPPER_JAR%)"
if not exist %WRAPPER_JAR% (
    echo ERROR: Failed to download Maven wrapper. Please install Maven manually.
    exit /b 1
)

:runWithJavaHome
java -cp %WRAPPER_JAR% %WRAPPER_LAUNCHER% %MAVEN_PROJECTBASEDIR% %*
