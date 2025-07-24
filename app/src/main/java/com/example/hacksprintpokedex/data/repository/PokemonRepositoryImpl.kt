package com.example.hacksprintpokedex.data.repository

import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.data.remote.api.PokeApiService
import com.example.hacksprintpokedex.domain.model.PokemonDetail
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
    override suspend fun getPokemonDetail(nameOrId: String): PokemonDetail {
        val detailResponse = apiService.getPokemon(nameOrId)
        val speciesResponse = apiService.getPokemonSpecies(nameOrId)

        if (!detailResponse.isSuccessful || !speciesResponse.isSuccessful) {
            throw Exception("Failed to fetch data")
        }

        val detail = detailResponse.body()!!
        val species = speciesResponse.body()!!

        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${detail.id}.png"

        val description = species.flavorTextEntries.firstOrNull {
            it.language.name == "en" || it.language.name == "pt"
        }?.flavorText?.replace("\n", " ")?.replace("\u000c", " ") ?: "Description not available."

        val abilityName = detail.abilities.firstOrNull()?.ability?.name ?: "Unknown"

        val regionName = species.generation.name.replace("-", " ")

        return PokemonDetail(
            id = detail.id,
            name = detail.name,
            imageUrl = imageUrl,
            types = detail.types.sortedBy { it.slot }.map { it.type.name },
            weight = detail.weight / 10.0,
            height = detail.height / 10.0,
            ability = abilityName,
            region = regionName,
            description = description,
            genderRate = species.genderRate
        )
    }
}