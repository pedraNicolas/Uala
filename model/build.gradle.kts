plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.pedra.uala.model"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
    }
}