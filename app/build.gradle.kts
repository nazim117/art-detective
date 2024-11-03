plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.actsofkindness"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.actsofkindness"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("com.google.guava:guava:32.1.3-android")
    implementation(libs.coil.compose)

    // CameraX dependencies
    implementation(libs.androidx.camera.camera2) // Core CameraX library
    implementation(libs.androidx.camera.lifecycle) // Lifecycle support for CameraX
    implementation(libs.androidx.camera.view) // Camera View for UI preview

    // Retrofit dependencies for making API requests to Google Cloud Vision
    implementation(libs.retrofit) // Retrofit core library
    implementation(libs.converter.gson) // Gson converter for JSON parsing

    // Gson dependency (for handling JSON parsing manually if needed)
    implementation(libs.gson)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.36.0")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
