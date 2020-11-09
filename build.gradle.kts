plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val kotlinVersion = extra["kotlin.version"] as String
val kotlinSerializationJsonVersion = extra["kotlinx-serialization-json.version"] as String

val reactVersion = extra["react.version"] as String
val reactDomVersion = extra["react-dom.version"] as String
val reactReduxVersion = extra["react-redux.version"] as String
val extensionsVersion = extra["extensions.version"] as String
val wrappersVersionBuild = extra["wrappers.version.build"] as String

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
    js {
        browser {
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")
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
                implementation("org.jetbrains:kotlin-extensions:$extensionsVersion-$wrappersVersionBuild-kotlin-$kotlinVersion")

                // kotlin-react
                implementation("org.jetbrains:kotlin-react:$reactVersion-$wrappersVersionBuild-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactDomVersion-$wrappersVersionBuild-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-redux:$reactReduxVersion-$wrappersVersionBuild-kotlin-$kotlinVersion")

                // npm
                implementation(npm("body-scroll-lock", "3.0.1"))
                implementation(npm("copy-webpack-plugin", "6.0.3"))
                implementation(npm("hamburgers", "1.1.3"))
                implementation(npm("js-search", "2.0.0"))
                implementation(npm("jszip", "3.2.0"))
                implementation(npm("prism-react-renderer", "1.1.1"))
                implementation(npm("query-string", "6.13.5"))
                implementation(npm("react-copy-to-clipboard", "5.0.2"))
                implementation(npm("react-hotkeys", "2.0.0"))
                implementation(npm("react-markdown", "5.0.2"))
                implementation(npm("react-toastify", "6.0.8"))
                implementation(npm("react-transition-group", "4.3.0"))
                implementation(npm("react-syntax-highlighter", "15.3.0"))
                implementation(npm("sql.js", "1.3.0"))

                // webpack
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

