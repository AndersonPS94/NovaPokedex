package com.anderson.hacksprintpokedex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val shinyImageUrl: String,
    val officialArtworkUrl: String, // URL para a imagem estática
    val types: List<String>,
    val weight: Double,
    val height: Double,
    val ability: String,
    val region: String,
    val description: String,
    val genderRate: Int,
    val lastRefreshed: Long = System.currentTimeMillis()
)