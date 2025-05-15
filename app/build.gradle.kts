import java.util.Properties

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val openWeatherApiKey = localProperties["OPENWEATHER_API_KEY"] as String? ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "de.comtec.uks.sunny"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.comtec.uks.sunny"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        // API key for OpenWeatherMap
        buildConfigField("String", "OPENWEATHER_API_KEY", "\"$openWeatherApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Core components
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.ui)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // OKHTTP
    implementation(libs.okhttp)
    implementation(platform(libs.okhttpBOM))
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.nav)
    ksp(libs.hilt.compiler)

    // Moshi
    implementation(libs.converter.moshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.moshi.kotlin)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore)

    // Google Fonts
    implementation(libs.androidx.google.fonts)

    // Location
    implementation(libs.androidx.location)

    // Permissions
    implementation(libs.accompanist.permissions)

    // AsyncImage
    implementation(libs.coil)

    // Timber
    implementation(libs.timber)

}


