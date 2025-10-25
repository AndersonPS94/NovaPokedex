package com.anderson.hacksprintpokedex.domain.repository

import com.anderson.hacksprintpokedex.domain.model.Pokemon
import com.anderson.hacksprintpokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>

    suspend fun fetchAndStorePokemonList()

    suspend fun getPokemonDetail(nameOrId: String): PokemonDetail
}