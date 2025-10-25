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
            val response = apiService.getPokemonList(1025)
            val basicList = response.body()?.results ?: emptyList()

            val localPokemons = coroutineScope {
                basicList.map { pokemonBasic ->
                    async {
                        try {
                            val detail = apiService.getPokemon(pokemonBasic.name).body()!!
                            Pokemon(
                                id = detail.id,
                                name = detail.name,
                                imageUrl = "https://play.pokemonshowdown.com/sprites/ani/${normalizePokemonName(detail.name)}.gif",
                                shinyImageUrl = "https://play.pokemonshowdown.com/sprites/ani-shiny/${normalizePokemonName(detail.name)}.gif",
                                officialArtworkUrl = detail.sprites.other.officialArtwork.frontDefault,
                                types = detail.types.map { it.type.name }
                            ).toLocalPokemon()
                        } catch (e: Exception) {
                            Log.e("PokemonRepository", "Failed to fetch details for ${pokemonBasic.name}", e)
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }
            pokemonDao.insertPokemons(localPokemons)
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): PokemonDetail {
        val cachedDetail = pokemonDetailDao.getPokemonDetail(nameOrId)
        if (cachedDetail != null) {
            return cachedDetail.toDomainDetail()
        }

        return withContext(Dispatchers.IO) {
            val detailResponse = apiService.getPokemon(nameOrId)
            val speciesResponse = apiService.getPokemonSpecies(nameOrId)

            if (!detailResponse.isSuccessful || !speciesResponse.isSuccessful) {
                throw RuntimeException("Failed to fetch data for $nameOrId")
            }

            val detail = detailResponse.body()!!
            val species = speciesResponse.body()!!

            val formattedName = normalizePokemonName(detail.name)

            val imageUrl =
                "https://play.pokemonshowdown.com/sprites/ani/${formattedName}.gif"
            val shinyImageUrl =
                "https://play.pokemonshowdown.com/sprites/ani-shiny/${formattedName}.gif"
            val officialArtworkUrl = detail.sprites.other.officialArtwork.frontDefault

            val description = species.flavorTextEntries.firstOrNull {
                it.language.name == "en" || it.language.name == "pt"
            }?.flavorText?.replace("\n", " ")?.replace("\u000c", " ") ?: "Description not available."

            val abilityName = detail.abilities.firstOrNull()?.ability?.name ?: "Unknown"

            val regionName = species.generation.name.replace("-", " ")

            val pokemonDetail = PokemonDetail(
                id = detail.id,
                name = detail.name,
                imageUrl = imageUrl,
                shinyImageUrl = shinyImageUrl,
                officialArtworkUrl = officialArtworkUrl,
                types = detail.types.sortedBy { it.slot }.map { it.type.name },
                weight = detail.weight / 10.0,
                height = detail.height / 10.0,
                ability = abilityName,
                region = regionName,
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
}
