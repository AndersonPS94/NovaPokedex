package com.anderson.hacksprintpokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anderson.hacksprintpokedex.data.local.entities.Pokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<Pokemon>)

    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemons(): Flow<List<Pokemon>>

    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAllPokemons()

    @Query("SELECT lastRefreshed FROM pokemon_table ORDER BY lastRefreshed DESC LIMIT 1")
    suspend fun getLastRefreshTime(): Long?
}