import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "org.swm.att"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "org.swm.att"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 17
        versionName = "1.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "ATT_BASE_URL", "\"${gradleLocalProperties(rootDir).getProperty("ATT_DEBUG_BASE_URL")}\"")
            buildConfigField("String", "ACCESS_TOKEN", "\"${gradleLocalProperties(rootDir).getProperty("TMP_DEBUG_ACCESS_TOKEN")}\"")
            manifestPlaceholders["SENTRY_DSN"] = gradleLocalProperties(rootDir).getProperty("SENTRY_DSN_DEBUG")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("String", "ATT_BASE_URL", "\"${gradleLocalProperties(rootDir).getProperty("ATT_RELEASE_BASE_URL")}\"")
            buildConfigField("String", "ACCESS_TOKEN", "\"${gradleLocalProperties(rootDir).getProperty("TMP_RELEASE_ACCESS_TOKEN")}\"")
            manifestPlaceholders["SENTRY_DSN"] = gradleLocalProperties(rootDir).getProperty("SENTRY_DSN_RELEASE")
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
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        )
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common-ui"))
    implementation(project(":feature:main"))
    implementation(project(":feature:login"))

    implementation(libs.material)
    implementation(libs.bundles.androidx.ui.foundation)
    implementation(libs.security.crypto)

    //Navigation
    implementation(libs.bundles.navigation)

    //Test
    implementation(libs.bundles.basic.test)
    implementation(libs.arch.core.testing)
    implementation(libs.mockk)

    //hilt
    implementation(libs.hilt)
    kapt(libs.hilt.kapt)
}