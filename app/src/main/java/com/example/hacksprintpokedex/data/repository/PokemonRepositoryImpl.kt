package com.example.hacksprintpokedex.data.repository

import com.example.hacksprintpokedex.data.local.PokemonDao
import com.example.hacksprintpokedex.data.remote.api.PokeApiService
import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.hacksprintpokedex.data.mapper.toDomainPokemon
import com.example.hacksprintpokedex.data.mapper.toLocalPokemon
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override fun getPokemonList(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemons().map { localPokemonList ->
            localPokemonList.map { it.toDomainPokemon() }
        }
    }

    override suspend fun fetchAndStorePokemonList(limit: Int) {
        withContext(Dispatchers.IO) {
            val response = apiService.getPokemonList(limit, 0)
            val basicList = response.body()?.results ?: emptyList()

            val localPokemons = coroutineScope {
                basicList.mapNotNull { pokemonBasic ->
                    async {
                        val detailResponse = apiService.getPokemon(pokemonBasic.name)
                        if (detailResponse.isSuccessful) {
                            val detail = detailResponse.body()
                            detail?.let {
                                val fullDetail = getPokemonDetail(it.name)
                                fullDetail.toLocalPokemon()
                            }
                        } else {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }
            pokemonDao.insertPokemons(localPokemons)
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): PokemonDetail {
        return withContext(Dispatchers.IO) {
            val detailResponse = apiService.getPokemon(nameOrId)
            val speciesResponse = apiService.getPokemonSpecies(nameOrId)

            if (!detailResponse.isSuccessful || !speciesResponse.isSuccessful) {
                throw Exception("Failed to fetch data")
            }

            val detail = detailResponse.body()!!
            val species = speciesResponse.body()!!

            val formattedName = normalizePokemonName(detail.name)

            val imageUrl =
                "https://play.pokemonshowdown.com/sprites/ani/${formattedName}.gif"
            val shinyImageUrl =
                "https://play.pokemonshowdown.com/sprites/ani-shiny/${formattedName}.gif"

            val description = species.flavorTextEntries.firstOrNull {
                it.language.name == "en" || it.language.name == "pt"
            }?.flavorText?.replace("\n", " ")?.replace("\u000c", " ") ?: "Description not available."

            val abilityName = detail.abilities.firstOrNull()?.ability?.name ?: "Unknown"

            val regionName = species.generation.name.replace("-", " ")

            PokemonDetail(
                id = detail.id,
                name = detail.name,
                imageUrl = imageUrl,
                shinyImageUrl = shinyImageUrl,
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

    private fun normalizePokemonName(name: String): String {
        return name.lowercase()
            .replace("♀", "f")
            .replace("♂", "m")
            .replace("-", "")
            .replace(" ", "")
            .replace(".", "")
            .replace("'", "")
    }
}
