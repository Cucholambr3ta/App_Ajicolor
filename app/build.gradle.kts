plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
<<<<<<< HEAD
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" // <-- AGREGA versión aquí
}

android {
    namespace = "com.example.uinavegacion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.uinavegacion"
=======
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

android {
    namespace = "com.example.appajicolorgrupo4"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appajicolorgrupo4"
>>>>>>> origin/Ale
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
<<<<<<< HEAD
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
=======
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
>>>>>>> origin/Ale
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
<<<<<<< HEAD

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
=======
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Navigation Compose (estable)
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Lifecycle + ViewModel en Compose (alineadas)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // Responsive
    implementation("androidx.compose.material3:material3-window-size-class:1.3.0")
    implementation("androidx.compose.material3.adaptive:adaptive:1.0.0-alpha06")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Testing
>>>>>>> origin/Ale
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
<<<<<<< HEAD
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //librerias nuevas
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    // Material icons (necesarios para Visibility / VisibilityOff)
    implementation("androidx.compose.material:material-icons-extended")

    // Room (SQLite) - runtime y extensiones KTX
    implementation("androidx.room:room-runtime:2.6.1")    // <-- NUEVO
    implementation("androidx.room:room-ktx:2.6.1")        // <-- NUEVO

    // Compilador de Room vía KSP
    ksp("androidx.room:room-compiler:2.6.1")               // <-- NUEVO

    //manipular la carga de imagenes en el cache temporal
    implementation("io.coil-kt:coil-compose:2.7.0")

    //Data Store
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Material 3 con soporte para Carousel (versión actualizada)
    // La versión de Material3 del BOM ya incluye soporte para Carousel experimental
    // No necesitamos dependencias adicionales, solo usar @OptIn(ExperimentalMaterial3Api::class)
}
=======
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
>>>>>>> origin/Ale
