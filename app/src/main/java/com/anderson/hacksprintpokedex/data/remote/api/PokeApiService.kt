package com.anderson.hacksprintpokedex.data.remote.api

import com.anderson.hacksprintpokedex.data.model.EvolutionChainResponse
import com.anderson.hacksprintpokedex.data.model.PokemonDetailResponse
import com.anderson.hacksprintpokedex.data.model.PokemonListResponse
import com.anderson.hacksprintpokedex.data.model.PokemonSpeciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0
    ): Response<PokemonListResponse>

    @GET("pokemon/{nameOrId}")
    suspend fun getPokemon(
        @Path("nameOrId") nameOrId: String
    ): Response<PokemonDetailResponse>

    @GET("pokemon-species/{nameOrId}")
    suspend fun getPokemonSpecies(
        @Path("nameOrId") nameOrId: String
    ): Response<PokemonSpeciesResponse>

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(
        @Path("id") id: Int
    ): Response<EvolutionChainResponse>
}