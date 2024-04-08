// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    val kotlinVersion = "1.8.20"
    val lifecycleVersion = "2.5.1"
    val navVersion = "2.7.7"
    val versionsWork = "2.8.1"
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    alias(libs.plugins.androidLibrary) apply false
}