package com.anderson.hacksprintpokedex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_detail_table")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
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
)
