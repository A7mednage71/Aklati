plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.example.aklati"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.negeh_tech.aklati"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.room.common.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Lottie
    implementation(libs.lottie.android)
    implementation(libs.androidx.core.splashscreen)
    // Gson
    implementation(libs.gson)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.adapter.rxjava3)
    // Glide
    implementation(libs.glide)
    // Navigation Component
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava3)
    annotationProcessor(libs.androidx.room.compiler)
    // rx java
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    annotationProcessor(libs.kotlinx.metadata.jvm)
}