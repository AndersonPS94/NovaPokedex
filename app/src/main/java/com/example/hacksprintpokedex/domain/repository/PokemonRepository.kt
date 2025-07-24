package com.example.hacksprintpokedex.domain.repository

import com.example.hacksprintpokedex.data.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int): List<Pokemon>
}