plugins {
    alias(libs.plugins.android.application) // Manual para construir una App de Android.
    alias(libs.plugins.kotlin.android) // Manual para entender el lenguaje Kotlin.
    alias(libs.plugins.kotlin.compose) // Manual para construir interfaces con Jetpack Compose.
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    //Core de Android KTX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //Jetpack Compose - Se usa la BoM (Bill of Materials) para gestionar las versiones
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //Navigation para Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Lifecycle y ViewModel para Compose
    //Estas librerías deben tener la misma versión para evitar conflictos
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    //Coroutines de Kotlin para Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

