plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.projectmarketplace"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.projectmarketplace"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.database)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.viewbinding)
    implementation(platform(libs.firebase.bom))
    implementation (libs.play.services.auth)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.firestore.ktx)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.firebase.messaging)
    implementation (libs.firebase.analytics.ktx)
    implementation (libs.firebase.storage.ktx)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    implementation (libs.roundedimageview)
    implementation (libs.glide.transformations)
    implementation (libs.play.services.location)
    implementation (libs.android.maps.utils)
    implementation (libs.play.services.maps)
    implementation (libs.geofire.android.common)
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.image.labeling)
    implementation (libs.coil)


}