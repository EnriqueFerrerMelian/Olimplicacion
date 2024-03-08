plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.olimplicacion"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.olimplicacion"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    //Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // https://firebase.google.com/docs/android/setup#available-libraries
    //Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Credential Manager es compatible con Android 4.4 (nivel de API 19) y versiones posteriores.
    implementation("androidx.credentials:credentials:1.3.0-alpha01")
    // optional - necesario para dispositivos usandoAndroid 13 y anterior.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.cardview:cardview:1.0.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}