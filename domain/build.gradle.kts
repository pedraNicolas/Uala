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
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":model"))

    implementation(libs.kotlinx.coroutines.core)
}
