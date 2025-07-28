package com.example.hacksprintpokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hacksprintpokedex.data.local.entities.Pokemon
import com.example.hacksprintpokedex.data.local.converters.ListStringConverter

@Database(entities = [Pokemon::class], version = 2, exportSchema = false)
@TypeConverters(ListStringConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}