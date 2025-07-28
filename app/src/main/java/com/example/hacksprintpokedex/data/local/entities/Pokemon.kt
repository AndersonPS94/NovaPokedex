package com.example.hacksprintpokedex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val shinyImageUrl: String,
    val types: List<String>,
    val weight: Double,
    val height: Double,
    val ability: String,
    val region: String,
    val description: String,
    val genderRate: Int
)