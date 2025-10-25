package com.example.hacksprintpokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hacksprintpokedex.data.local.entities.Pokemon
import com.example.hacksprintpokedex.data.local.entities.PokemonDetailEntity

@Database(entities = [Pokemon::class, PokemonDetailEntity::class], version = 5, exportSchema = false)
@TypeConverters(com.example.hacksprintpokedex.data.local.TypeConverters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonDetailDao(): PokemonDetailDao
}