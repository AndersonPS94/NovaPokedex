package com.anderson.hacksprintpokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val shinyImageUrl: String,
    val officialArtworkUrl: String, 
    val types: List<String>,
    val weight: Double,
    val height: Double,
    val ability: String,
    val region: String,
    val description: String,
    val genderRate: Int
) : Parcelable
