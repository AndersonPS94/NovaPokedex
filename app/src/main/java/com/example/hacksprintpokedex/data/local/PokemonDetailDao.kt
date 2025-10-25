package com.example.hacksprintpokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hacksprintpokedex.data.local.entities.PokemonDetailEntity

@Dao
interface PokemonDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(pokemonDetail: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_detail_table WHERE name = :name")
    suspend fun getPokemonDetail(name: String): PokemonDetailEntity?
}
