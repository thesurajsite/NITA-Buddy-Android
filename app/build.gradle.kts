plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gharaana.nitabuddy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gharaana.nitabuddy"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "2.0.0"

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

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.activity:activity:1.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


    //GSON Converter
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    //Volley
    implementation("com.android.volley:volley:1.2.1")

    //sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.1")

    // swipe refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //In-app Update
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation(kotlin("script-runtime"))

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")


}