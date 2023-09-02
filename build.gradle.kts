plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.0.2").apply(false)
    id("com.android.library").version("8.0.2").apply(false)
    id("com.google.dagger.hilt.android") version "2.44" apply false // TODO: remove?
    kotlin("android").version("1.8.21").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.23.0")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
