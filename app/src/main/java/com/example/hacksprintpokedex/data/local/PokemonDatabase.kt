// app/src/main/java/com/example/hacksprintpokedex/data/local/PokemonDatabase.kt
package com.example.hacksprintpokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hacksprintpokedex.data.local.entities.Pokemon
import com.example.hacksprintpokedex.data.local.converters.ListStringConverter

// --- CRITICAL: Version must be incremented. If you were on 1, go to 2. If already 2, try 3. ---
@Database(entities = [Pokemon::class], version = 2, exportSchema = false) // Or 3 if you've already tried 2
@TypeConverters(ListStringConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}