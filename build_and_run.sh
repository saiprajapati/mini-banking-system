#!/bin/bash
echo "================================================"
echo " Mini Banking System - Build and Run Script"
echo "================================================"
echo ""

JAVA_SRC="src"
BIN="bin"
LIB="lib"
MAIN="com.banking.Main"
JAR_NAME="MiniBankingSystem.jar"

# Step 1: Clean
echo "[1/4] Cleaning old build..."
rm -rf $BIN && mkdir $BIN
echo "Done."

# Step 2: Compile
echo ""
echo "[2/4] Compiling source files..."
find $JAVA_SRC -name "*.java" > sources.txt
javac -cp "$LIB/mysql-connector-java.jar" -d $BIN @sources.txt
rm sources.txt

if [ $? -ne 0 ]; then
    echo "COMPILE ERROR. Make sure mysql-connector-java.jar is in the lib/ folder."
    exit 1
fi
echo "Compilation successful!"

# Step 3: Package
echo ""
echo "[3/4] Packaging into JAR..."
echo "Main-Class: $MAIN" > manifest.txt
echo "Class-Path: lib/mysql-connector-java.jar" >> manifest.txt
jar cfm $JAR_NAME manifest.txt -C $BIN .
rm manifest.txt
echo "JAR created: $JAR_NAME"

# Step 4: Run
echo ""
echo "[4/4] Launching application..."
java -cp "$JAR_NAME:$LIB/mysql-connector-java.jar" $MAIN
