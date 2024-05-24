plugins {
    alias(libs.plugins.androidApplication)
    id("androidx.navigation.safeargs")
}

android {
    namespace = "edu.tacoma.uw.barber_mobile_application"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.tacoma.uw.barber_mobile_application"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.mockito:mockito-core:3.12.4")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("org.mindrot:jbcrypt:0.4")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.legacy.support.v4)
    implementation (libs.play.services.maps)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation ("androidx.test:rules:1.4.0")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("junit:junit:4.12")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}