@echo off
echo ================================================
echo  Mini Banking System - Build and Run Script
echo ================================================
echo.

REM === CONFIGURATION ===
set JAVA_SRC=src
set BIN=bin
set LIB=lib
set MAIN=com.banking.Main
set JAR_NAME=MiniBankingSystem.jar

REM === Step 1: Clean ===
echo [1/4] Cleaning old build...
if exist %BIN% rmdir /s /q %BIN%
mkdir %BIN%
echo Done.

REM === Step 2: Compile ===
echo.
echo [2/4] Compiling source files...
javac -source 8 -target 8 -encoding UTF-8 -cp "%LIB%\mysql-connector-java.jar" -d %BIN% -sourcepath %JAVA_SRC% ^
    %JAVA_SRC%\com\banking\db\*.java ^
    %JAVA_SRC%\com\banking\model\*.java ^
    %JAVA_SRC%\com\banking\dao\*.java ^
    %JAVA_SRC%\com\banking\util\*.java ^
    %JAVA_SRC%\com\banking\gui\*.java ^
    %JAVA_SRC%\com\banking\Main.java

if %errorlevel% neq 0 (
    echo COMPILE ERROR. Make sure mysql-connector-java.jar is in the lib folder.
    pause
    exit /b 1
)
echo Compilation successful!

REM === Step 3: Package ===
echo.
echo [3/4] Packaging into JAR...
echo Main-Class: %MAIN% > manifest.txt
echo Class-Path: lib/mysql-connector-java.jar >> manifest.txt
jar cfm %JAR_NAME% manifest.txt -C %BIN% .
del manifest.txt
echo JAR created: %JAR_NAME%

REM === Step 4: Run ===
echo.
echo [4/4] Launching application...
java -cp "%JAR_NAME%;%LIB%\mysql-connector-java.jar" %MAIN%

pause
