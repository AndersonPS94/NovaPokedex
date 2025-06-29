package com.example.hacksprintpokedex.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("results")
    val results: List<PokemonBasic>
)

data class PokemonBasic(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)
