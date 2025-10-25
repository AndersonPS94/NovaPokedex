# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# --- Regras para bibliotecas comuns ---

# Kotlin & Coroutines
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.flow.** { *; }
-keepclassmembers class ** {
    kotlin.coroutines.Continuation "continuation";
}

# Retrofit & Gson
# Preserva classes de modelo (DTOs, Entities) que são usadas para serialização.
# Adapte o caminho 'com.anderson.hacksprintpokedex.data.**' para o seu projeto.
-keep @com.google.gson.annotations.SerializedName class *
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.anderson.hacksprintpokedex.data.** { *; }
-keepclassmembers class com.anderson.hacksprintpokedex.data.** { *; }

# Hilt
# As regras do Hilt geralmente são incluídas automaticamente, mas estas são uma boa proteção.
-keep class * extends androidx.lifecycle.ViewModel
-keep class dagger.hilt.internal.aggregatedroot.codegen.*
-keep class com.anderson.hacksprintpokedex.Hilt_*.** { *; }
-keep class hilt_aggregated_deps.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { <init>(); }

# Se você usa o Glide ou Coil, eles geralmente incluem as próprias regras.
# Se encontrar problemas com o carregamento de imagens na versão de release,
# descomente as linhas apropriadas.
# -keep public class * implements com.bumptech.glide.module.GlideModule
# -keep public class * extends com.bumptech.glide.module.AppGlideModule
# -keep public class * implements com.coil.ImageLoader
