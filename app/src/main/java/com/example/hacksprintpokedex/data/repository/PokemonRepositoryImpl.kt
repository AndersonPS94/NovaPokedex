package com.example.hacksprintpokedex.data.repository

import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.data.remote.api.PokeApiService
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
): PokemonRepository {

    override suspend fun getPokemonList(limit: Int): List<Pokemon> {
        val response = apiService.getPokemonList(limit)
        val basicList = response.body()?.results ?: emptyList()

        return basicList.mapNotNull { pokemon ->
            val detailResponse = apiService.getPokemon(pokemon.name).body()
            detailResponse?.let {
                Pokemon(
                    id = it.id,
                    name = it.name,
                    types = it.types.map { type -> type.type.name }
                )
            }
        }

    }
}