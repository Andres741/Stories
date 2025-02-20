plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.0.2").apply(false)
    id("com.android.library").version("8.0.2").apply(false)
    id("io.realm.kotlin").version("1.10.0").apply(false)
    kotlin("android").version("1.9.20").apply(false)
    kotlin("multiplatform").version("1.9.20").apply(false)
    kotlin("plugin.serialization") version "1.9.20"
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.24.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.20")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
