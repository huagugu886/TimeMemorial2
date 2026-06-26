pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "TimeMemorial2"
include(":app")

// Local MIUIX blur modules
include(":miuix-shader")
project(":miuix-shader").projectDir = file("_miuix/miuix-shader")
include(":miuix-blur")
project(":miuix-blur").projectDir = file("_miuix/miuix-blur")
