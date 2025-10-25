package com.anderson.hacksprintpokedex.data.repository

import android.util.Log
import com.anderson.hacksprintpokedex.data.local.PokemonDao
import com.anderson.hacksprintpokedex.data.local.PokemonDetailDao
import com.anderson.hacksprintpokedex.data.remote.api.PokeApiService
import com.anderson.hacksprintpokedex.domain.model.Pokemon
import com.anderson.hacksprintpokedex.domain.model.PokemonDetail
import com.anderson.hacksprintpokedex.domain.repository.PokemonRepository
import com.anderson.hacksprintpokedex.data.mapper.toDomainDetail
import com.anderson.hacksprintpokedex.data.mapper.toDomainPokemon
import com.anderson.hacksprintpokedex.data.mapper.toLocalPokemon
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao,
    private val pokemonDetailDao: PokemonDetailDao
) : PokemonRepository {

    override fun getPokemonList(): Flow<List<Pokemon>> {
        return pokemonDao.getAllPokemons().map { localPokemonList ->
            localPokemonList.map { it.toDomainPokemon() }
        }
    }

    override suspend fun fetchAndStorePokemonList() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(POKEMON_LIST_LIMIT)
                if (response.isSuccessful) {
                    val basicList = response.body()?.results ?: emptyList()

                    val localPokemons = coroutineScope {
                        basicList.map {
                            async {
                                try {
                                    apiService.getPokemon(it.name).body()?.let { detail ->
                                        val formattedName = normalizePokemonName(detail.name)
                                        Pokemon(
                                            id = detail.id,
                                            name = detail.name,
                                            imageUrl = "$BASE_IMAGE_URL$formattedName.gif",
                                            shinyImageUrl = "$BASE_SHINY_IMAGE_URL$formattedName.gif",
                                            officialArtworkUrl = detail.sprites.other.officialArtwork.frontDefault,
                                            types = detail.types.map { it.type.name }
                                        ).toLocalPokemon()
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed to fetch details for ${it.name}", e)
                                    null
                                }
                            }
                        }.awaitAll().filterNotNull()
                    }
                    pokemonDao.insertPokemons(localPokemons)
                } else {
                    throw IOException("Failed to fetch pokemon list")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Could not fetch and store pokemon list", e)
            }
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): PokemonDetail {
        pokemonDetailDao.getPokemonDetail(nameOrId)?.let {
            return it.toDomainDetail()
        }

        return withContext(Dispatchers.IO) {
            val detailResponse = apiService.getPokemon(nameOrId)
            val speciesResponse = apiService.getPokemonSpecies(nameOrId)

            if (!detailResponse.isSuccessful || !speciesResponse.isSuccessful) {
                throw IOException("Failed to fetch data for $nameOrId")
            }

            val detail = detailResponse.body() ?: throw IOException("Empty detail response for $nameOrId")
            val species = speciesResponse.body() ?: throw IOException("Empty species response for $nameOrId")

            val formattedName = normalizePokemonName(detail.name)

            val description = species.flavorTextEntries.firstOrNull { it.language.name in SUPPORTED_LANGUAGES }
                ?.flavorText?.replace("\n", " ")?.replace("\u000c", " ")
                ?: "Description not available."

            val pokemonDetail = PokemonDetail(
                id = detail.id,
                name = detail.name,
                imageUrl = "$BASE_IMAGE_URL$formattedName.gif",
                shinyImageUrl = "$BASE_SHINY_IMAGE_URL$formattedName.gif",
                officialArtworkUrl = detail.sprites.other.officialArtwork.frontDefault,
                types = detail.types.sortedBy { it.slot }.map { it.type.name },
                weight = detail.weight / WEIGHT_CONVERSION_FACTOR,
                height = detail.height / HEIGHT_CONVERSION_FACTOR,
                ability = detail.abilities.firstOrNull()?.ability?.name ?: "Unknown",
                region = species.generation.name.replace("-", " "),
                description = description,
                genderRate = species.genderRate
            )
            pokemonDetailDao.insertPokemonDetail(pokemonDetail.toLocalPokemon())
            pokemonDetail
        }
    }

    private fun normalizePokemonName(name: String): String {
        return name.lowercase()
            .replace(" ", "")
            .replace(".", "")
            .replace("'", "")
            .replace("♀", "f")
            .replace("♂", "m")
    }

    companion object {
        private const val TAG = "PokemonRepository"
        private const val POKEMON_LIST_LIMIT = 1025
        private const val BASE_IMAGE_URL = "https://play.pokemonshowdown.com/sprites/ani/"
        private const val BASE_SHINY_IMAGE_URL = "https://play.pokemonshowdown.com/sprites/ani-shiny/"
        private const val WEIGHT_CONVERSION_FACTOR = 10.0
        private const val HEIGHT_CONVERSION_FACTOR = 10.0
        private val SUPPORTED_LANGUAGES = listOf("en", "pt")
    }
}
