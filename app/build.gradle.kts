import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.anderson.hacksprintpokedex"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.anderson.hacksprintpokedex"
        minSdk = 31
        targetSdk = 35
        versionCode = 5
        versionName = "5.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // Você deve criar um arquivo keystore.properties na raiz do seu projeto
            // para armazenar suas credenciais de assinatura de forma segura.
            // Não adicione senhas diretamente a este arquivo.
            // O arquivo keystore.properties deve ser adicionado ao .gitignore.
            // Exemplo de keystore.properties:
            // storeFile=release.keystore
            // storePassword=sua_senha_store
            // keyAlias=sua_key_alias
            // keyPassword=sua_senha_key
            val properties = Properties()
            val keystoreFile = project.rootProject.file("keystore.properties")
            if (keystoreFile.exists()) {
                properties.load(keystoreFile.inputStream())
            }
            storeFile = file(properties.getProperty("storeFile", "release.keystore"))
            storePassword = properties.getProperty("storePassword")
            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.coil)
    implementation(libs.coil.gif)

    implementation(libs.lottie)

    implementation(libs.paging.runtime)

    implementation(libs.glide)
    kapt(libs.glide.compiler)

    implementation(libs.picasso)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.androidx.core.testing)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation(libs.room.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}
