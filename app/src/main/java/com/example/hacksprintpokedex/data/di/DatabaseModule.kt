// app/src/main/java/com/example/hacksprintpokedex/di/DatabaseModule.kt
package com.example.hacksprintpokedex.di

import android.content.Context
import androidx.room.Room
import com.example.hacksprintpokedex.data.local.PokemonDao
import com.example.hacksprintpokedex.data.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PokemonDatabase::class.java,
            "pokemon_database" // This is the DATABASE FILE name, not the table name.
        )
            .fallbackToDestructiveMigration() // <--- CRITICAL: Make sure this is still here!
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }
}