package com.example.hacksprintpokedex.domain.repository

import com.example.hacksprintpokedex.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int): List<Pokemon>
}