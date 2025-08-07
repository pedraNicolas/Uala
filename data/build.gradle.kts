plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.pedra.uala.data"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":model"))
    implementation(project(":network"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.datastore.preferences)
}