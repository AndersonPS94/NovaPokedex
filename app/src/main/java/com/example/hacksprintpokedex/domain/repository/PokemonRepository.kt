package com.example.hacksprintpokedex.domain.repository

import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.domain.model.PokemonDetail

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int): List<Pokemon>
    suspend fun getPokemonDetail(nameOrId: String): PokemonDetail
}