plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "org.swm.att"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "org.swm.att"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        dataBinding = true
        buildConfig = true
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.material)
    implementation(libs.bundles.androidx.ui.foundation)
    //Navigation
    implementation(libs.bundles.navigation)

    //Test
    implementation(libs.bundles.basic.test)

    //moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kapt)

    //retrofit
    implementation(libs.bundles.retrofit)

    //okhttp
    implementation(libs.bundles.okhttp)

    //hilt
    implementation(libs.hilt)
    kapt(libs.hilt.kapt)
}