package com.example.hacksprintpokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val types: List<String>,
    val description: String,
    val region: String,
    val weight: Double,
    val height: Double,
    val ability: String,
    val genderRate: Int,
    val imageUrl: String = "",
    val shinyImageUrl: String = ""
)
