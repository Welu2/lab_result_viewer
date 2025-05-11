// --- PLUGIN MANAGEMENT CONFIGURATION ---
// This block defines where Gradle should look for plugins (e.g., Android Gradle Plugin, Kotlin plugins).
pluginManagement {
    // Specifies the repositories (sources) for plugins.
    repositories {
        // Google's Maven repository (primary source for Android-related plugins).
        google {
            // Filters to optimize dependency resolution by including only Android/Google groups.
            content {
                // Regex patterns to include specific groups:
                includeGroupByRegex("com\\.android.*")  // All 'com.android' packages (Android SDK)
                includeGroupByRegex("com\\.google.*")   // All 'com.google' packages (Google libs)
                includeGroupByRegex("androidx.*")       // All AndroidX (Jetpack) libraries
            }
        }
        // General-purpose Maven Central repository (backup for non-Android plugins).
        mavenCentral()
        // Official Gradle Plugin Portal (default source for core Gradle plugins).
        gradlePluginPortal()
    }
}

// --- DEPENDENCY RESOLUTION CONFIGURATION ---
// Configures where the project fetches library dependencies (e.g., Retrofit, Glide).
dependencyResolutionManagement {
    // Sets strict mode: fails if a project declares its own repositories (encourages centralization).
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    
    // Defines dependency repositories (similar to pluginManagement but for app dependencies).
    repositories {
        google()        // Google's Maven repo (AndroidX, Material Design, etc.)
        mavenCentral()  // Maven Central (common open-source libraries)
    }
}

// --- PROJECT STRUCTURE ---
// Sets the root project name (matches the Android Studio project name).
rootProject.name = "LabResultViewer"

// Includes the ':app' module (the main Android application module).
// Additional modules (e.g., ':library') would be listed here if they existed.
include(":app")
