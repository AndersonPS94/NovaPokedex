plugins {
    id("com.android.application") 
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.hacksprintpokedex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hacksprintpokedex"
        minSdk = 31
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
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Coil
    implementation("io.coil-kt:coil:2.6.0")
    implementation("io.coil-kt:coil-gif:2.6.0")

    // Lottie
    implementation("com.airbnb.android:lottie:6.4.0")

    // Paging
    implementation("androidx.paging:paging-runtime:3.3.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.8")

    // Activity
    implementation("androidx.activity:activity-ktx:1.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")

    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Testes Unitários
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-inline:5.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.lifecycle:lifecycle-livedata-core-testing:2.8.3")
    testImplementation("androidx.room:room-testing:2.6.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Dependências padrões do projeto
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Testes Instrumentados
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}
