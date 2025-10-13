plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.apppolera_ecommerce_grupo4"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.apppolera_ecommerce_grupo4"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Activity Compose (para setContent en MainActivity)
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose BOM (maneja versiones consistentes)
    implementation(platform(libs.androidx.compose.bom))

    // Jetpack Compose core
    implementation("androidx.compose.ui:ui:1.7.5")
    implementation("androidx.compose.foundation:foundation:1.7.5")
    implementation("androidx.compose.ui:ui-graphics:1.7.5")
    implementation("androidx.compose.material3:material3:1.3.0")

    // Tooling y previews
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.5")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.5")

    // Lifecycle ViewModel para Compose (función viewModel())
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6-rc02")

    // API de WindowSizeClass para responsive
    implementation("androidx.compose.material3:material3-window-size-class:1.3.0")

    // Dependencia para layouts adaptativos (opcional)
    implementation("androidx.compose.material3.adaptive:adaptive:1.0.0-alpha06")

    // Coroutines para StateFlow en ViewModel
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
