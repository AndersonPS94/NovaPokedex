// app/src/main/java/com/example/hacksprintpokedex/domain/model/Pokemon.kt
package com.example.hacksprintpokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val shinyImageUrl: String = "", // Adicionado com valor padrão se nem sempre disponível na lista
    val types: List<String>,
    val weight: Double = 0.0,      // Adicionado com valor padrão
    val height: Double = 0.0,      // Adicionado com valor padrão
    val ability: String = "",      // Adicionado com valor padrão
    val region: String = "",       // Adicionado com valor padrão
    val description: String = "",  // Adicionado com valor padrão
    val genderRate: Int = 0        // Adicionado com valor padrão
) : Parcelable