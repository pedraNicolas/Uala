plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.pedra.uala.domain"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
    }
}

dependencies {
    implementation(project(":model"))

    implementation(libs.kotlinx.coroutines.core)
}
