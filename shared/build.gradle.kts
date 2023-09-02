plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            export("dev.icerock.moko:resources:0.23.0")
            export("dev.icerock.moko:graphics:0.9.0")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                api("dev.icerock.moko:resources:0.23.0")

                implementation("io.insert-koin:koin-core:3.3.3")
                api("dev.icerock.moko:mvvm-core:0.16.1")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.insert-koin:koin-androidx-compose:3.4.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.example.stories"
    compileSdk = 33
    defaultConfig {
        minSdk = 28
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.example.stories"
    multiplatformResourcesClassName = "SharedRes"
}
