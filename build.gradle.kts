plugins {
    kotlin("multiplatform") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

val kotlinVersion = "1.4.10"
val reactVersion = "16.13.1"
val reduxVersion = "5.0.7"
val extensionsVersion = "1.0.1"
val preVersion = "124"

group = "es.iaaa"
version = "0.1-SNAPSHOT"

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://kotlin.bintray.com/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlin-dev")
}

kotlin {
    js(IR) {
        browser {}
        useCommonJs()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                // kotlin-extensions
                implementation("org.jetbrains:kotlin-extensions:$extensionsVersion-pre.$preVersion-kotlin-$kotlinVersion")

                // kotlin-react
                implementation("org.jetbrains:kotlin-react:$reactVersion-pre.$preVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactVersion-pre.$preVersion-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-redux:$reduxVersion-pre.$preVersion-kotlin-$kotlinVersion")

                // react
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(npm("react-toastify", "6.0.8"))
                implementation(npm("react-transition-group", "4.3.0"))
                implementation(npm("react-hotkeys", "2.0.0"))
                implementation(npm("react-copy-to-clipboard", "5.0.2"))
                implementation(npm("prism-react-renderer", "1.1.1"))

                // jsearch
                implementation(npm("js-search", "2.0.0"))

                implementation(npm("body-scroll-lock", "3.0.1"))

                implementation(npm("query-string", "6.13.5"))

                // css
                implementation(npm("hamburgers", "1.1.3"))

                // sql.js
                implementation(npm("sql.js", "1.3.0"))
                implementation(npm("copy-webpack-plugin", "6.0.3"))

                // JSZip
                implementation(npm("jszip", "3.2.0"))

                // loaders
                implementation(npm("style-loader", "1.2.1"))
                implementation(npm("css-loader", "4.2.1"))
                implementation(npm("sass-loader", "9.0.3"))
                implementation(npm("html-webpack-plugin", "4.3.0"))
                implementation(npm("file-loader", "6.0.0"))
                implementation(npm("sass", "1.26.10"))
            }
            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }
        }
    }
}
