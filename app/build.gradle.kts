plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tp3_ex2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tp3_ex2"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room dependencies
    implementation("androidx.room:room-runtime:2.6.1") // Version actuelle de Room
    annotationProcessor("androidx.room:room-compiler:2.6.1") // Pour Java
//material ui
    implementation("com.google.android.material:material:1.9.0")
    // Facultatif : support pour les observables (LiveData)
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
}