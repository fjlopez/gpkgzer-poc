rootProject.name = "gpkgzer"

pluginManagement {
    resolutionStrategy {
        plugins {
            val kotlinVersion = extra["kotlin.version"] as String
            kotlin("multiplatform") version kotlinVersion
            kotlin("plugin.serialization") version kotlinVersion
        }
    }

    repositories {
        gradlePluginPortal()
        maven("https://kotlin.bintray.com/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlin-dev")
    }
}