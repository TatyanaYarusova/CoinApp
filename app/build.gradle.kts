plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.coinapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.coinapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Dagger 2
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    //ViewModel
    implementation(libs.viewmodel)

    //Picasso
    implementation (libs.picasso)

    //Pull to Refresh
    implementation(libs.pull.to.refresh)

    //Test junit
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)

    //Mockito
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.jupiter)

    //Coroutine test
    testImplementation(libs.coroutines.test)
}