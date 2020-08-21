plugins {
    kotlin("multiplatform") version "1.4.0"
}

val kotlinVersion = "1.4.0"
val reactVersion = "16.13.1"
val reduxVersion = "5.0.7"
val extensionsVersion = "1.0.1"

group = "es.iaaa"
version = "0.1-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}

kotlin {
    js {
        browser {}
        useCommonJs()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        js().compilations["main"].defaultSourceSet {
            dependencies {
                // kotlin-extensions
                implementation("org.jetbrains:kotlin-extensions:$extensionsVersion-pre.111-kotlin-$kotlinVersion")

                // kotlin-react
                implementation("org.jetbrains:kotlin-react:$reactVersion-pre.111-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactVersion-pre.111-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-redux:$reduxVersion-pre.111-kotlin-$kotlinVersion")

                // react
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(npm("react-toastify", "6.0.8"))
                implementation(npm("react-transition-group", "4.3.0"))

                // css
                implementation(npm("hamburgers", "1.1.3"))

                // loaders
                implementation(npm("style-loader", "1.2.1"))
                implementation(npm("css-loader", "4.2.1"))
                implementation(npm("sass-loader", "9.0.3"))
                implementation(npm("html-webpack-plugin", "4.3.0"))
                implementation(npm("file-loader", "6.0.0"))
                implementation(npm("sass", "1.26.10"))
            }
        }
    }
}
