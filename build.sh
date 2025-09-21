#!/bin/bash

# Utilities Application Build Script
# Professional Java project build system

echo "Building Utilities Application..."
echo "================================="

# Create build directory
mkdir -p build/classes

# Set classpath
CLASSPATH="build/classes:lib/*"

# Compile Java sources
echo "Compiling Java sources..."
if [ -d "src/main/java" ]; then
    find src/main/java -name "*.java" -print0 | xargs -0 javac -cp "$CLASSPATH" -d build/classes
else
    echo "No Java sources found in src/main/java"
    exit 1
fi

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful"
else
    echo "✗ Compilation failed"
    exit 1
fi

# Create JAR files
echo "Creating JAR files..."

# Main JAR
jar cfe build/utilities.jar com.utilities.UtilitiesApp -C build/classes .

if [ $? -eq 0 ]; then
    echo "✓ Main JAR file created: build/utilities.jar"
else
    echo "✗ Main JAR creation failed"
    exit 1
fi

# Fallback JAR (for systems with OCR compatibility issues)
jar cfe build/utilities-fallback.jar com.utilities.UtilitiesAppFallback -C build/classes .

if [ $? -eq 0 ]; then
    echo "✓ Fallback JAR file created: build/utilities-fallback.jar"
else
    echo "✗ Fallback JAR creation failed"
    exit 1
fi

echo ""
echo "Build completed successfully!"
echo ""
echo "Run options:"
echo "  Main version:     java -cp \"build/classes:lib/*\" com.utilities.UtilitiesApp"
echo "  Main JAR:         java -jar build/utilities.jar"
echo "  Fallback JAR:     java -jar build/utilities-fallback.jar"
echo "  Diagnostic:       ./run_utilities.sh"
