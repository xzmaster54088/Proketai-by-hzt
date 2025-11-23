#!/bin/bash

# PocketDev Build Script
# This script automates the build process for the PocketDev Android application

set -e

echo "🚀 Starting PocketDev build process..."

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "❌ gradlew not found. Please run this script from the project root directory."
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

# Function to display usage
usage() {
    echo "Usage: $0 [command]"
    echo "Commands:"
    echo "  debug     - Build debug APK"
    echo "  release   - Build release APK and AAB"
    echo "  test      - Run tests"
    echo "  lint      - Run lint checks"
    echo "  clean     - Clean project"
    echo "  all       - Run tests, lint, and build debug"
}

# Function to build debug
build_debug() {
    echo "📱 Building Debug APK..."
    ./gradlew assembleDebug
    echo "✅ Debug APK built successfully: app/build/outputs/apk/debug/app-debug.apk"
}

# Function to build release
build_release() {
    echo "📦 Building Release versions..."
    ./gradlew assembleRelease
    ./gradlew bundleRelease
    echo "✅ Release APK built: app/build/outputs/apk/release/app-release.apk"
    echo "✅ Release AAB built: app/build/outputs/bundle/release/app-release.aab"
}

# Function to run tests
run_tests() {
    echo "🧪 Running tests..."
    ./gradlew test
    echo "✅ Tests completed"
}

# Function to run lint
run_lint() {
    echo "🔍 Running lint checks..."
    ./gradlew lintDebug
    echo "✅ Lint checks completed"
}

# Function to clean project
clean_project() {
    echo "🧹 Cleaning project..."
    ./gradlew clean
    echo "✅ Project cleaned"
}

# Main script logic
case "${1:-}" in
    "debug")
        build_debug
        ;;
    "release")
        build_release
        ;;
    "test")
        run_tests
        ;;
    "lint")
        run_lint
        ;;
    "clean")
        clean_project
        ;;
    "all")
        run_tests
        run_lint
        build_debug
        ;;
    *)
        usage
        exit 1
        ;;
esac

echo "🎉 Build process completed successfully!"
